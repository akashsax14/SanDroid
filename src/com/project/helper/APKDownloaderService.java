package com.project.helper;

import java.io.File;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.project.sandroid.R;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class APKDownloaderService extends IntentService
{
	private static final String TAG = "APKDownloaderService";
	private static final int NOTIFICATION = 1;
	DatabaseHelper dh = new DatabaseHelper(this);
	
	public APKDownloaderService() 
	{
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent intent) 
	{
		try 
		{
			NotificationManager notify_manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			NotificationCompat.Builder notify = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.icon).setContentTitle("SanDroid").setContentText("Downloading File");
			
			Log.v(TAG, "Download Service Started");
			String fileName = intent.getStringExtra("filename");//filename of format : com.application.____.apk
			
			String secretKey = "IBKrKGw/x1NWyLlNBmZCXIVzopE/cFXgpXIy36jI";
			String accessKey = "AKIAJ5POUGZIFUG3WKWA";
			String bucket = dh.getCurrentUser().getBucketname();
			
			AWSCredentials credential = new BasicAWSCredentials(accessKey, secretKey);
			AmazonS3Client s3Client = new AmazonS3Client(credential);
		
			File root = new File(Environment.getExternalStorageDirectory(), "Sandroid");//gets the Sandroid folder
			if (!root.exists()) 
			{
				root.mkdirs();
			}
			Log.v(TAG, fileName);
			File downloaded_apk = new File(root.getAbsolutePath(), fileName);//create new file having name of the format com.application.____.apk
			
			notify.setProgress(0, 0, true);
			notify_manager.notify(NOTIFICATION, notify.build());
			try
			{
				GetObjectRequest objreq = new GetObjectRequest(bucket, "download." + fileName);
				s3Client.getObject(objreq, downloaded_apk);
				
				//Delete File after downloading
				DeleteObjectRequest dor = new DeleteObjectRequest(bucket, "download." + fileName);
				s3Client.deleteObject(dor);
				
				notify_manager.cancel(NOTIFICATION);
			}
			catch(AmazonS3Exception ae)
			{
				notify.setContentText("Error while downloading file!");
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
}
