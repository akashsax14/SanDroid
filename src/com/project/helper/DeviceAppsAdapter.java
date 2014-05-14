package com.project.helper;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.project.model.DeviceApps;
import com.project.sandroid.R;

public class DeviceAppsAdapter extends ArrayAdapter<DeviceApps>
{
	private final Context context;
	private ArrayList<DeviceApps> apps= null;
	private static final String TAG = "DeviceAppsAdapter";
	
	public DeviceAppsAdapter(Context context, ArrayList<DeviceApps> apps)
	{
		super(context, R.layout.selectapk_row, apps);

		this.context = context;
		this.apps = apps;
	}

	@Override
  	public View getView(int position, View convertView, ViewGroup parent) 
	{
		View row = convertView;
		
		try 
		{
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = (View)inflater.inflate(R.layout.selectapk_row, parent, false);
			
			ImageView appIcon = (ImageView) row.findViewById(R.id.apklogo);
			TextView appName = (TextView) row.findViewById(R.id.apkname);
			TextView packName = (TextView) row.findViewById(R.id.packname);
			TextView appSize = (TextView) row.findViewById(R.id.apksize);
			
			DeviceApps da = apps.get(position);
			appIcon.setImageDrawable(da.getIcon());
			appName.setText(da.getAppLabel());
			packName.setText(da.getPackageName());
			appSize.setText(da.getSize());
		} 
		catch (Exception e) 
		{
			Log.i(TAG, "Error in getView : "+e.toString());
		}
		
		return row;
	}
}