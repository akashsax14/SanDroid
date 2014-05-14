package com.project.controller;

import java.util.ArrayList;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PermissionInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.project.helper.APKUploaderService;
import com.project.helper.PermissionsAdapter;
import com.project.model.Apk;
import com.project.model.Permission;
import com.project.sandroid.R;

public class SelectPermissionActivity extends UtilityProviderActivity
{
	private static final String TAG = "SELECTPERMISSIONACTIVITY";
	private String apkPath = "//";
	PermissionsAdapter adapter;
	ArrayList<String> checkedPermissions;
	
	Apk apk_info;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_selectpermission);

		try 
		{
			Intent intent = getIntent();
			PackageInfo pi = intent.getParcelableExtra("PACKAGE_NAME");

			// To Print Application name in the header
			ApplicationInfo ai = pi.applicationInfo;
			String appFullName = (String) getPackageManager().getApplicationLabel(
					ai);		
			if (appFullName == null) 
			{
				appFullName = ai.packageName;
				apk_info = new Apk(appFullName, ai.packageName, null, null, null);
				if (appFullName.contains(".")) 
				{
					String pnsplit[] = appFullName.split("\\.");
					appFullName = pnsplit[pnsplit.length - 1];
				}
			}
			else
			{
				apk_info = new Apk(appFullName, ai.packageName, null, null, null);
			}
			TextView apkname = (TextView) findViewById(R.id.apkname);
			apkname.setText(appFullName.toUpperCase());

			menuloader();
			getPermissionsView(pi);
		} 
		catch (Exception e) 
		{
			Log.i(TAG, "Error in onCreate : "+e.toString());
			Toast.makeText(this, "Exception in SelectPermissionActivity:onCreate : " + e.toString(), Toast.LENGTH_LONG).show();
		}
	}

	public void getPermissionsView(PackageInfo pi) 
	{
		try 
		{
			String permissions[] = pi.requestedPermissions;
			String permissionName = null;
			String permissionFull = null;
			String permissionDescription = null;
			ArrayList<Permission> permission = new ArrayList<Permission>();
			PackageManager pm = getPackageManager();
			apkPath = pi.applicationInfo.sourceDir;

			for (int j = 0; permissions != null && j < permissions.length; j++) 
			{
				try
				{
					PermissionInfo pinfo = pm.getPermissionInfo(permissions[j], PackageManager.GET_META_DATA);
					permissionDescription = pinfo.loadDescription(pm).toString();
				}
				catch(Exception e)
				{
					permissionDescription = null;
				}
				
				if (permissions[j].contains(".") && permissions[j].contains("permission")) 
				{
					permissionFull = permissions[j];
					String fullname[] = permissions[j].split("\\.");
					permissionName = fullname[fullname.length - 1].replace("_", " ");
				}
				
				if(permissionFull == null || permissionFull.isEmpty())
					continue;
				
				permissionName = permissionName.isEmpty() || permissionName == null ? permissionFull : permissionName;
				permissionDescription = permissionDescription == null || permissionDescription.isEmpty() ? "" : permissionDescription;
				
				Permission perm = new Permission(permissionName, permissionFull, permissionDescription);
				permission.add(perm);
			}
			if (permissions == null || permissions.length == 0) 
			{
				permission.add(new Permission("NO PERMISSIONS", "NO PERMISSIONS", "-"));
			}

			adapter = new PermissionsAdapter(this, permission);
			this.getListView().setAdapter(adapter);
		} 
		catch (Exception e) 
		{
			Log.i(TAG, "Error in getPermissionsView : "+e.toString());
			Toast.makeText(this, "Exception in SelectPermissionActivity:getPermissionsView : " + e.toString(), Toast.LENGTH_LONG).show();
		}
	}
	
	public void getCheckedPermissions(View v)
	{
		try 
		{
			checkedPermissions = new ArrayList<String>();
			for(Permission p : adapter.getPermissions())
			{
				checkedPermissions.add(p.getPermissionFull());
			}
		}
		catch (Exception e) 
		{
			Log.i(TAG, "Error in getCheckedPermissions : "+e.toString());
			Toast.makeText(this, "Exception while getting checked permissions : " + e.toString(), Toast.LENGTH_LONG).show();
		}
	}
	
	public void sendToServer(View v) 
	{
		try 
		{
			getCheckedPermissions(v);
			
			String path = apkPath.substring(0, apkPath.lastIndexOf("/"));
			String name = apkPath.substring(apkPath.lastIndexOf("/") + 1, apkPath.length());

			Intent intent = new Intent(this, APKUploaderService.class);
			intent.putExtra("APK_PATH", new String[]{path, name});
			intent.putExtra("PERMISSIONS", checkedPermissions);
			apk_info.setFileName(name);
			intent.putExtra("APK_INFO", apk_info);

			Log.v(TAG, "Starting service");
			startService(intent);
			setResult(RESULT_OK);
			finish();
		}
		catch (Exception e) 
		{
			Log.i(TAG, "Error in sendToServer : "+e.toString());
			Toast.makeText(this, "Exception in SelectPermissionActivity:sendToServer : " + e.toString(), Toast.LENGTH_LONG).show();
		}
	}
}
