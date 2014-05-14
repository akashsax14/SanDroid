package com.project.controller;

import java.util.ArrayList;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.project.helper.DatabaseHelper;
import com.project.helper.MyAppsAdapter;
import com.project.model.Apk;
import com.project.sandroid.R;

public class MyAppActivity extends UtilityProviderActivity 
{
	private static final String TAG = "MyAppActivity";
	final Context context = this;
	DatabaseHelper dbHelper = new DatabaseHelper(context);

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_myapp);
		
		try 
		{
			swipeHandler(getIntent().putExtra("parent", "myapp"), findViewById(R.id.myAppActivity), this);
			swipeHandler(getIntent().putExtra("parent", "myapp"), findViewById(getListView().getId()), this);
			menuloader();
			
			showAPKS();
		} 
		catch (Exception e) 
		{
			Log.i(TAG, "Error in onCreate : "+e.toString());
			Toast.makeText(this, "Exception in MyAppActivity:onCreate : " + e.toString(), Toast.LENGTH_LONG).show();
		}
	}

	private void showAPKS() 
	{
		try 
		{
			ArrayList<Apk> apkList = new ArrayList<Apk>();
			apkList = dbHelper.getAllApk(dbHelper.getCurrentUser().getUsername());
			dbHelper.close();
			
			if(apkList!=null && !apkList.isEmpty())
			{
				MyAppsAdapter adapter = new MyAppsAdapter(this, apkList);
				setListAdapter(adapter);
			}
			else
			{
				Toast.makeText(this, "You have no Modified Apps", Toast.LENGTH_LONG).show();
			}
		} 
		catch (Exception e) 
		{
			Log.i(TAG, "Error in showAPKS : "+e.toString());
			Toast.makeText(this, "Exception in MyAppActivity:showAPKS : " + e.toString(), Toast.LENGTH_LONG).show();
		}
	}
}
