package com.project.helper;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;
import com.project.controller.SelectAPKActivity;
import com.project.model.Apk;
import com.project.sandroid.R;

public class APKUploaderService extends IntentService 
{
	private static final int NOTIFICATION = 1;
	private static final String TAG = "APKUploaderService";
	final Context context = this;
	DatabaseHelper dbHelper = new DatabaseHelper(this);
	Apk apk_info;

	public APKUploaderService() 
	{
		super(TAG);
	}
	
	public JSONObject getJson()
	{
		try
		{
			String s3Prefix = "https://s3-us-west-2.amazonaws.com/sandroid/";
			JSONObject json = new JSONObject();
			json.put("app_name", apk_info.getName());
			json.put("app_package", apk_info.getPackageName());
			json.put("app_file_name", apk_info.getFileName());
			json.put("app_s3_apk_link", s3Prefix + apk_info.getFileName());
			json.put("app_s3_details_link", s3Prefix + apk_info.getFileName() + ".details.txt");
			json.put("username", dbHelper.getCurrentUser().getUsername());
			json.put("bucketname", dbHelper.getCurrentUser().getBucketname());
			
			return json;
		}
		catch(Exception e)
		{
			Log.i(TAG, "Error in getJson : "+e.toString());
		}
		return null;
	}
	
	@Override
	protected void onHandleIntent(Intent intent) 
	{
		try 
		{
			Log.v(TAG, "Service Started");
			String apk_path[] = intent.getStringArrayExtra("APK_PATH");
			ArrayList<String> checked_permissions = intent.getStringArrayListExtra("PERMISSIONS");
			apk_info = intent.getParcelableExtra("APK_INFO");

			String secretKey = "IBKrKGw/x1NWyLlNBmZCXIVzopE/cFXgpXIy36jI";
			String accessKey = "AKIAJ5POUGZIFUG3WKWA";
			String bucket = dbHelper.getCurrentUser().getBucketname();
			
			NotificationManager notify_manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			NotificationCompat.Builder notify = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.icon).setContentTitle("SanDroid").setContentText("Uploading File");
			
			Intent resultIntent = new Intent(this, SelectAPKActivity.class);
			
			AWSCredentials credential = new BasicAWSCredentials(accessKey, secretKey);
			AmazonS3 s3client = new AmazonS3Client(credential);
			
			List<Bucket> bucketLists = s3client.listBuckets();
			boolean flag = false;
			for(Bucket b : bucketLists)
			{
				if(b.getName().equalsIgnoreCase(bucket))
					flag = true;
			}
			if(flag == false)
				s3client.createBucket(bucket);
			
			File f = new File(apk_path[0], apk_path[1]);
			
			TransferManager manager = new TransferManager(s3client);
			Upload upload_apk = manager.upload(bucket, f.getName(), f);
			
			notify.setProgress(0, 0, true);
			notify_manager.notify(NOTIFICATION, notify.build());
			while (!upload_apk.isDone()) 
			{}
			
			AmazonClientException upload_result = upload_apk.waitForException();
			if (upload_result == null) 
			{
				File permissions = generateNoteOnSD(f.getName(), checked_permissions);
				Upload upload_permissions = manager.upload(bucket, permissions.getName(), permissions);
				while (!upload_permissions.isDone()) 
				{

				}
				upload_result = upload_permissions.waitForException();
				if (upload_result != null) 
				{
					Log.v(TAG, upload_result.getMessage() + "error from amazon");
					TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
					stackBuilder.addParentStack(SelectAPKActivity.class);
					stackBuilder.addNextIntent(resultIntent);
					
					PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
					notify.setContentIntent(resultPendingIntent);
					notify.setContentText("Error while uploading file. Please retry.");
					notify.setProgress(0, 0, false);
					notify.setAutoCancel(true);

					notify_manager.notify(NOTIFICATION, notify.build());
				} 
				else 
				{
					permissions.delete();
					notify_manager.cancel(NOTIFICATION);
					dbHelper.insertAPKS(apk_info, dbHelper.getCurrentUser().getUsername());
					Log.v(TAG, "Uploaded file");
					

					Log.i(TAG, "UPDATING RDS NOW");
					//UPDATING RDS
					AsyncPhpConnector apc = new AsyncPhpConnector();
					apc.execute(getJson());
				}
			} 
			else 
			{
				Log.v(TAG, upload_result.getMessage() + "error from amazon");
				TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

				stackBuilder.addParentStack(SelectAPKActivity.class);
				stackBuilder.addNextIntent(resultIntent);
				
				PendingIntent resultPendingIntent = stackBuilder
						.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
				notify.setContentIntent(resultPendingIntent);
				notify.setContentText("Error while uploading file. Please retry.");
				notify.setProgress(0, 0, false);
				notify.setAutoCancel(true);

				notify_manager.notify(NOTIFICATION, notify.build());
			}
		} 
		catch (Exception e) 
		{
			Log.v(TAG, e.toString());
			e.printStackTrace();
		}

	}

	public File generateNoteOnSD(String sFileName, ArrayList<String> sBody) 
	{
		try 
		{
			File root = new File(Environment.getExternalStorageDirectory(), "Sandroid");
			if (!root.exists()) 
			{
				root.mkdirs();
			}
			File gpxfile = new File(root, sFileName + ".details.txt");
			FileWriter writer = new FileWriter(gpxfile);
			writer.append(apk_info.getName()+"  |  ");
			writer.append(apk_info.getPackageName()+"  |  ");
			writer.append(apk_info.getFileName()+"  |  ");
			writer.append(dbHelper.getCurrentUser().getBucketname()+"  |  ");
			
			for (int i = 0; i < sBody.size(); i++) 
			{
				writer.append(sBody.get(i) + "  |  ");
			}
			writer.flush();
			writer.close();
			return gpxfile;
		} 
		catch (Exception e) 
		{
			Log.v(TAG, e.toString());
			e.printStackTrace();
		}
		return null;
	}

}
