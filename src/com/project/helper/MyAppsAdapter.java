package com.project.helper;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.project.model.Apk;
import com.project.sandroid.R;

public class MyAppsAdapter extends ArrayAdapter<Apk>
{
	private final Context context;
	private ArrayList<Apk> apk = null;
	private LayoutInflater inflater;
	private static final String TAG = "MyAppsAdapter";
	DatabaseHelper dh;
	public MyAppsAdapter(Context context, ArrayList<Apk> apk)
	{
		super(context, R.layout.myapp_row, apk);

		this.context = context;
		this.apk = apk;
		inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		dh = new DatabaseHelper(context);
	}
	@Override
  	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View row = convertView;
		try 
		{
			row = (View)inflater.inflate(R.layout.myapp_row, parent, false);
			final Apk app = apk.get(position);
			
			TextView appname = (TextView) row.findViewById(R.id.myappname);
			Button uninstall = (Button) row.findViewById(R.id.btnuninstall);
			Button download = (Button) row.findViewById(R.id.btndownload);			
			Button install = (Button) row.findViewById(R.id.btninstall);	
			Button remove = (Button) row.findViewById(R.id.btnremove);
			
			appname.setText(app.getName());
			
			uninstall.setOnClickListener
			(
				new View.OnClickListener() 
			    {
					@Override
					public void onClick(View v) 
					{
						Uri packageURI = Uri.parse("package:" + app.getPackageName());
						Intent intent = new Intent(Intent.ACTION_UNINSTALL_PACKAGE, packageURI);					
						context.startActivity(intent);
					}
				}
			);
			
			download.setOnClickListener
			(
				new View.OnClickListener() 
				{
					@Override
					public void onClick(View v) 
					{
						Intent intent = new Intent (context, APKDownloaderService.class);
						intent.putExtra("filename", app.getFileName());
						context.startService(intent);
					}
				}
			);
			
			remove.setOnClickListener
			(
				new View.OnClickListener() 
				{
					@Override
					public void onClick(View v) 
					{
						DatabaseHelper dh = new DatabaseHelper(context);
						dh.deleteApkEntry(app, dh.getCurrentUser().getUsername());
						//Dirty fix : Can be changed
						notifyDataSetChanged();
					}
				}
			);
			
			File root = new File(Environment.getExternalStorageDirectory(), "Sandroid");//gets the Sandroid folder
			boolean flag = false;
			for(File f : root.listFiles())
			{
				if(f.getName().equalsIgnoreCase(app.getFileName()))
				{
					flag = true;
					break;
				}
			}
			if(flag)
			{
				install.setClickable(true);
				install.setOnClickListener
				(
					new View.OnClickListener() 
					{
						@Override
						public void onClick(View v) 
						{
							Intent intent = new Intent(Intent.ACTION_VIEW);
						    intent.setDataAndType(Uri.fromFile(new File(Environment.getExternalStorageDirectory() + "/Sandroid/" + app.getFileName())), "application/vnd.android.package-archive");
						    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						    context.startActivity(intent);
						}
					}
				);
			}
			else
			{
				install.setClickable(false);
				install.setTextColor(0x55000000 + Integer.parseInt("A3A3A3",16));
			}

			return row;
		} 
		catch (Exception e) 
		{
			Log.i(TAG, "Error in getView : " + e.toString());
			return convertView;
		}
	}
}
