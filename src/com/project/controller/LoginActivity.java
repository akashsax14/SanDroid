package com.project.controller;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.project.helper.DatabaseHelper;
import com.project.model.User;
import com.project.sandroid.R;

public class LoginActivity extends UtilityProviderActivity
{
	private static final String TAG = "LoginActivity";
	final Context context = this;
	DatabaseHelper dh = new DatabaseHelper(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		//menuloader();
		try 
		{
			if(dh.getCurrentUser() != null)
			{
				Intent i = new Intent(this, MainActivity.class);
				startActivity(i);
				finish();
			}
			else
			{
				Vibrator v = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
				v.vibrate(50);
				Toast.makeText(this, "Please Register!", Toast.LENGTH_SHORT).show();
			}
			
		} 
		catch (Exception e) 
		{
			Log.i(TAG, "Error in onCreate : "+e.toString());
			Toast.makeText(this, "Exception in LoginActivity:onCreate : " + e.toString(), Toast.LENGTH_LONG).show();
		} 
	}
	
	public void create(View v)
	{
		try 
		{
			EditText un = (EditText)findViewById(R.id.loginid);
			String userName = un.getText().toString();
			Toast.makeText(this, userName, Toast.LENGTH_SHORT).show();
			if(!userName.trim().isEmpty())
			{	
				User u = new User(-1, userName, userName+"-bucket");
				dh.insertUser(u);
				Intent i = new Intent(this, MainActivity.class);
				startActivity(i);
				finish();
			}
			else
			{
				Vibrator vb = (Vibrator) this.context.getSystemService(Context.VIBRATOR_SERVICE);
				vb.vibrate(50);
				Toast.makeText(this, "Please Enter a valid user name!", Toast.LENGTH_SHORT).show();
			}
		}
		catch (Exception e) 
		{
			Log.i(TAG, "Error in create : "+e.toString());
			Toast.makeText(this, "Exception in LoginActivity:create : " + e.toString(), Toast.LENGTH_LONG).show();
		} 
	}
}
