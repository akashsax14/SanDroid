package com.project.controller;

import com.project.helper.OnSwipeTouchListener;
import com.project.sandroid.R;

import android.os.Bundle;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends UtilityProviderActivity 
{
	private static final String TAG = "MainActivity";
	final Context context = this;
	OnSwipeTouchListener onSwipeTouchListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		try 
		{
			swipeHandler(getIntent().putExtra("parent", "home"), findViewById(R.id.main), this);
			menuloader();
		} 
		catch (Exception e) 
		{
			Log.i(TAG, "Error in onCreate : "+e.toString());
			Toast.makeText(this, "Exception in MainActivity:onCreate : " + e.toString(), Toast.LENGTH_LONG).show();
		} 
	}
	
	public void startSelectAPK(View view)
	{
		try 
		{
			Intent intent = new Intent(this, SelectAPKActivity.class);
			startActivity(intent);
		} 
		catch (Exception e) 
		{
			Log.i(TAG, "Error in startSelectAPK");
			Toast.makeText(this, "Exception in MainActivity:startSelectAPK : " + e.toString(), Toast.LENGTH_LONG).show();
		}
	}
}
