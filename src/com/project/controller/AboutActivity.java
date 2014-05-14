package com.project.controller;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.project.helper.OnSwipeTouchListener;
import com.project.sandroid.R;

public class AboutActivity extends UtilityProviderActivity
{
	private static final String TAG = "AboutActivity";
	final Context context = this;
	OnSwipeTouchListener onSwipeTouchListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about);
		
		try 
		{
			TextView aboutText = (TextView) findViewById(R.id.abouttext);
			aboutText.setText("About: Sandroid is a unique application which provides you with the freedom to run and install android apps according to your needs.\nYour Device. Your apps. Your permissions.\n\n\n\nDeveloped by SanDroid Team:\n-Aditya Nenawati\n-Ankit Srivastava\n-Akash Saxena\n-Ankit Agarwal");
		} 
		catch (Exception e) 
		{
			Log.i(TAG, "Error in onCreate : "+e.toString());
			Toast.makeText(this, "Exception in AboutActivity:onCreate : " + e.toString(), Toast.LENGTH_LONG).show();
		} 
	}
}
