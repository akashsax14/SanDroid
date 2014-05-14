package com.project.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.project.helper.DeviceAppsAdapter;
import com.project.model.DeviceApps;
import com.project.sandroid.R;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class SelectAPKActivity  extends UtilityProviderActivity
{
	final Context context = this;
	private static final String TAG = "SelectAPKActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selectapk);

		try 
		{
			menuloader();
			getAPK();
		} 
		catch (Exception e) 
		{
			Log.i(TAG, "Error in onCreate : "+e.toString());
			Toast.makeText(this, "Exception in SelectAPKActivity:onCreate : " + e.toString(), Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		try 
		{
			if(requestCode ==1 && resultCode == RESULT_OK)
			{
				finish();
			}
			super.onActivityResult(requestCode, resultCode, data);
		} 
		catch (Exception e) 
		{
			Log.i(TAG, "Error in onActivityResult : "+e.toString());
			Toast.makeText(this, "Exception in SelectAPKActivity:onActivityResult : " + e.toString(), Toast.LENGTH_LONG).show();
		}
	}

	public void getAPK() 
	{
		try
		{
			final PackageManager pm = getPackageManager();
			List<PackageInfo> appsList = pm.getInstalledPackages(PackageManager.GET_META_DATA|PackageManager.GET_PERMISSIONS);
			
			final ArrayList<DeviceApps> apps = new ArrayList<DeviceApps>();
			for (PackageInfo packageInfo : appsList) 
			{				
				ApplicationInfo ai = pm.getApplicationInfo(packageInfo.packageName, PackageManager.GET_META_DATA);
				if((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0)
					continue;
				Drawable icon = getResources().getDrawable(R.drawable.ic_launcher); 
				
				String appLabel = "";
				String packageName = packageInfo.packageName;
				if(ai != null)
				{
					String pn = (String)pm.getApplicationLabel(ai);
					if(pn != null)
						appLabel = pn;
					else
						appLabel = packageInfo.packageName;
					
					icon = ai.loadIcon(getPackageManager());
				}
				
				String apkPath = "//";
				apkPath = packageInfo.applicationInfo.sourceDir;
				File f = new File(apkPath);
				long s = f.length();
				String size = s < 1024 ? s+" byte" : s < 1024*1024 ? (s/(1024))+" Kb" : (s/(1024*1024))+" Mb";
				
				DeviceApps da = new DeviceApps(appLabel, packageName, icon, "Size : " + size);
				apps.add(da);
			}

			Collections.sort(apps, new Comparator<DeviceApps>(){
				public int compare(DeviceApps d1, DeviceApps d2){
					return d1.getAppLabel().compareToIgnoreCase(d2.getAppLabel());
				}
			});
			
			DeviceAppsAdapter adapter = new DeviceAppsAdapter(this, apps);
			setListAdapter(adapter);
		}
		catch(Exception e)
		{
			Toast.makeText(this, "Exception in SelectAPKActivity:getAPK : " + e.toString(), Toast.LENGTH_LONG).show();
		}
	}
	
	@Override
    public void onListItemClick(ListView l, View v, int position, long id) 
	{
		try
		{
			TextView tv_packageName = (TextView) v.findViewById(R.id.packname);
			String packageName = tv_packageName.getText().toString();
			PackageManager pm = getPackageManager();
			PackageInfo pi = pm.getPackageInfo(packageName, PackageManager.GET_META_DATA|PackageManager.GET_PERMISSIONS);
			Intent intent = new Intent(this, SelectPermissionActivity.class);
			intent.putExtra("PACKAGE_NAME", pi);

			startActivityForResult(intent, 1);
		}
		catch(Exception e)
		{
			Toast.makeText(this, "Exception in SelectAPKActivity:onListItemClick : " + e.toString(), Toast.LENGTH_LONG).show();
		}
	}
}