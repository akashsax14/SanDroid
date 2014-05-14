package com.project.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.project.helper.DatabaseHelper;
import com.project.model.User;
import com.project.sandroid.R;

public class SettingsActivity extends UtilityProviderActivity
{
	User user = null;
	DatabaseHelper dh = new DatabaseHelper(this);
	private static final String TAG = "SettingsActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		user = dh.getCurrentUser();
		try 
		{
			TextView tvUserName = (TextView) findViewById(R.id.usernamelabel);
			tvUserName.setText("User Name : " + user.getUsername());
			
			
			swipeHandler(getIntent().putExtra("parent", "settings"), findViewById(R.id.settingsActivity), this);
			menuloader();
		} 
		catch (Exception e) 
		{
			Log.i(TAG, "Error in onCreate : "+e.toString());
			Toast.makeText(this, "Exception in SettingsActivity:onCreate : " + e.toString(), Toast.LENGTH_LONG).show();
		} 
	}
	
	public void delete(View v)
	{
		try 
		{
			dh.deleteUser();
			startActivity(new Intent(this, LoginActivity.class));
			finish();
		} 
		catch (Exception e) 
		{
			Log.i(TAG, "Error in delete : "+e.toString());
			Toast.makeText(this, "Exception in SettingActivity:delete : " + e.toString(), Toast.LENGTH_LONG).show();
		} 
	}
	
	public void about(View v)
	{
		try 
		{
			Intent i = new Intent(this, AboutActivity.class);
			startActivity(i);
		} 
		catch (Exception e) 
		{
			Log.i(TAG, "Error in delete : "+e.toString());
			Toast.makeText(this, "Exception in SettingActivity:delete : " + e.toString(), Toast.LENGTH_LONG).show();
		} 
	}
}
