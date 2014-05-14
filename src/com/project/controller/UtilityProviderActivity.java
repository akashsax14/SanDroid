package com.project.controller;

import java.util.ArrayList;

import com.project.helper.DatabaseHelper;
import com.project.helper.OnSwipeTouchListener;
import com.project.model.Apk;
import com.project.sandroid.R;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.LinearLayout.LayoutParams;

public class UtilityProviderActivity extends ListActivity
{

	private static final String TAG = "UtilityProviderActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
	}
	
	/****************************For Handling Swipe Events****************************/
	void swipeHandler(Intent intent, View v, Context c)
	{
		try 
		{
			if(intent.getStringExtra("parent").equalsIgnoreCase("home"))
			{
				final Intent ileft = new Intent(c, MyAppActivity.class);
				final Intent iRight = new Intent(c, SettingsActivity.class);
				v.setOnTouchListener(new OnSwipeTouchListener(c) {
				    @Override
				    public void onSwipeLeft() {
						startActivity(ileft);
						finish();
				    }
				    @Override
				    public void onSwipeRight() {
						startActivity(iRight);
						finish();
				    }
				});
			}
			if(intent.getStringExtra("parent").equalsIgnoreCase("myapp"))
			{
				final Intent ileft = new Intent(c, SettingsActivity.class);
				final Intent iRight = new Intent(c, MainActivity.class);
				v.setOnTouchListener(new OnSwipeTouchListener(c) {
				    @Override
				    public void onSwipeLeft() {
						startActivity(ileft);
						finish();
				    }
				    @Override
				    public void onSwipeRight() {
						startActivity(iRight);
						finish();
				    }
				});
			}
			if(intent.getStringExtra("parent").equalsIgnoreCase("settings"))
			{
				final Intent ileft = new Intent(c, MainActivity.class);
				final Intent iRight = new Intent(c, MyAppActivity.class);
				v.setOnTouchListener(new OnSwipeTouchListener(c) {
				    @Override
				    public void onSwipeLeft() {
						startActivity(ileft);
						finish();
				    }
				    @Override
				    public void onSwipeRight() {
						startActivity(iRight);
						finish();
				    }
				});
			}
		} 
		catch (Exception e)
		{
			Log.i(TAG, "Error in swipeHandler : "+e.toString());
			Toast.makeText(this, "Exception in UtilityProviderActivity:swipeHandler : " + e.toString(), Toast.LENGTH_LONG).show();
		}
	}
	
	/****************************FOR LOADING MENU****************************/
	public int dp(int dps)
	{
		final float scale = this.getResources().getDisplayMetrics().density;
		int pixels = (int) (dps * scale + 0.5f);
		return pixels;
	}
	public float pixel(float dp)
	{
		final float scale = this.getResources().getDisplayMetrics().densityDpi;
	    float px = dp * (scale / 160f);
	    return px;
	}
	public void menubuttonhandler(View v)
	{
		Button b = (Button)v;
		Intent intent;
		switch(b.getId())
		{
			case 10000:
				intent = new Intent(this, MainActivity.class);
				startActivity(intent);
				finish();
				break;
			case 20000:
				intent = new Intent(this, MyAppActivity.class);
				startActivity(intent);
				finish();
				break;
			case 30000:
				intent = new Intent(this, SettingsActivity.class);
				startActivity(intent);
				finish();
				break;
			case 90000:
				DatabaseHelper dh = new DatabaseHelper(this);
				dh.deleteAllApkEntries(dh.getCurrentUser().getUsername());
				recreate();
				break;
		}
	}
	public void menuloader()
	{
		try
		{
			DisplayMetrics displaymetrics = new DisplayMetrics();
			getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
			int width = displaymetrics.widthPixels;
			int rem_width = width - (int)pixel(0);
			
			View.OnClickListener onClickHandler = new View.OnClickListener() 
			{
				public void onClick(View v)
				{
					menubuttonhandler(v);
				}
			};
			
			// parent menu layout which holds the title layout and menu button layout
			LinearLayout menuLayout = (LinearLayout) findViewById(R.id.menulayout);
			menuLayout.setOrientation(LinearLayout.VERTICAL);
			
			// title layout which holds the sandroid icon and title
			LinearLayout titleLayout = new LinearLayout(this);
			LayoutParams titleLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			titleLayout.setLayoutParams(titleLayoutParams);
			titleLayout.setOrientation(LinearLayout.HORIZONTAL);
			
			//LinearLayout titleChild1 = new LinearLayout(this);
			//titleChild1.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			
			// sandroid icon
			ImageView icon = new ImageView(this);
			LayoutParams iconViewParams = new LayoutParams(dp(50), dp(50));
			icon.setLayoutParams(iconViewParams);
			icon.setImageDrawable(getResources().getDrawable(R.drawable.icon));
			icon.setLayoutParams(iconViewParams);
			
			// sandroid title
			TextView title = new TextView(this);
			LayoutParams titleViewParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
			title.setLayoutParams(titleViewParams);
			title.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
			title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 30);
			title.setTextColor(0xFF000000 + Integer.parseInt("FFFFFF",16));
			title.setText("SanDroid");
			
			titleLayout.addView(icon);
			titleLayout.addView(title);
			
			// Menu button layout which hold the three menu buttons
			LinearLayout menuBtnLayout = new LinearLayout(this);//(LinearLayout) findViewById(R.id.menulayout);
			LayoutParams menuBtnLayoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			menuBtnLayoutParams.height = dp(50);
			menuBtnLayout.setLayoutParams(menuBtnLayoutParams);
			menuBtnLayout.setOrientation(LinearLayout.HORIZONTAL);
			
			Button homeButton = new Button(this);
			LayoutParams hbparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
			homeButton.setLayoutParams(hbparams);
			homeButton.setId(10000);
			homeButton.setClickable(true);
			homeButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
			homeButton.setPadding(dp(5), dp(5), dp(5), dp(5));
			homeButton.setWidth(rem_width/3-dp(2));
			homeButton.setOnClickListener(onClickHandler);
			//homeButton.setBackground(getResources().getDrawable(R.drawable.button_background));
			homeButton.setTextColor(0xFF000000 + Integer.parseInt("FFFFFF",16));
			homeButton.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
			homeButton.setText("Home");
			
			Button myappButton = new Button(this);
			LayoutParams mabparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
			mabparams.leftMargin = dp(3);
			myappButton.setLayoutParams(mabparams);
			myappButton.setId(20000);
			myappButton.setClickable(true);
			myappButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
			myappButton.setPadding(dp(5), dp(5), dp(5), dp(5));
			myappButton.setWidth(rem_width/3-dp(2));
			myappButton.setOnClickListener(onClickHandler);
			//myappButton.setBackground(getResources().getDrawable(R.drawable.button_background));
			myappButton.setTextColor(0xFF000000 + Integer.parseInt("FFFFFF",16));
			myappButton.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
			myappButton.setText("MyApp");
			
			Button settingButton = new Button(this);
			LayoutParams sbparams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
			sbparams.leftMargin = dp(3);
			settingButton.setLayoutParams(sbparams);
			settingButton.setId(30000);
			settingButton.setClickable(false);
			settingButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
			settingButton.setPadding(dp(5), dp(5), dp(5), dp(5));
			settingButton.setWidth(rem_width/3-dp(2));
			settingButton.setOnClickListener(onClickHandler);
			//settingButton.setBackground(getResources().getDrawable(R.drawable.button_background));
			settingButton.setTextColor(0xFF000000 + Integer.parseInt("FFFFFF",16));
			settingButton.setTypeface(Typeface.MONOSPACE, Typeface.BOLD);
			settingButton.setText("Settings");
			
			// implemented only for MyAppActivity
			Button clearButton = null;
			
			String className = this.getClass().getSimpleName();
			if(className.equalsIgnoreCase("MainActivity"))
			{
				homeButton.setBackgroundColor(0xFF000000 + Integer.parseInt("FFFFFF",16));
				homeButton.setTextColor(0xFF000000 + Integer.parseInt("000000",16));
				myappButton.setBackgroundColor(0x55000000 + Integer.parseInt("FFFFFF",16));
				settingButton.setBackgroundColor(0x55000000 + Integer.parseInt("FFFFFF",16));
			}
			else if(className.equalsIgnoreCase("MyAppActivity"))
			{
				myappButton.setBackgroundColor(0xFF000000 + Integer.parseInt("FFFFFF",16));
				myappButton.setTextColor(0xFF000000 + Integer.parseInt("000000",16));
				homeButton.setBackgroundColor(0x55000000 + Integer.parseInt("FFFFFF",16));
				settingButton.setBackgroundColor(0x55000000 + Integer.parseInt("FFFFFF",16));
				
				clearButton = new Button(this);
				LayoutParams cbparams = new LayoutParams(LayoutParams.WRAP_CONTENT, dp(40));
				cbparams.leftMargin = dp(100);
				clearButton.setLayoutParams(cbparams);
				clearButton.setId(90000);
				clearButton.setClickable(true);
				clearButton.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
				clearButton.setOnClickListener(onClickHandler);
				clearButton.setTextColor(0xFF000000 + Integer.parseInt("FFFFFF",16));
				clearButton.setText("Clear");

				DatabaseHelper dh = new DatabaseHelper(this);
				String username = dh.getCurrentUser().getUsername();
				ArrayList<Apk> apkList = dh.getAllApk(username);
				if((apkList!=null && apkList.isEmpty()) || apkList == null)
				{
					clearButton.setClickable(false);
					clearButton.setTextColor(0xFF000000 + Integer.parseInt("A3A3A3",16));
				}
				titleLayout.addView(clearButton);
			}
			else if(className.equalsIgnoreCase("SettingsActivity"))
			{
				settingButton.setBackgroundColor(0xFF000000 + Integer.parseInt("FFFFFF",16));
				settingButton.setTextColor(0xFF000000 + Integer.parseInt("000000",16));
				homeButton.setBackgroundColor(0x55000000 + Integer.parseInt("FFFFFF",16));
				myappButton.setBackgroundColor(0x55000000 + Integer.parseInt("FFFFFF",16));
			}
			else
			{
				//if buttons need to be modified on other screens
				homeButton.setBackgroundColor(0x55000000 + Integer.parseInt("FFFFFF",16));
				myappButton.setBackgroundColor(0x55000000 + Integer.parseInt("FFFFFF",16));
				settingButton.setBackgroundColor(0x55000000 + Integer.parseInt("FFFFFF",16));
			}
			
			menuBtnLayout.addView(homeButton);
			menuBtnLayout.addView(myappButton);
			menuBtnLayout.addView(settingButton);
			
			menuLayout.addView(titleLayout);
			menuLayout.addView(menuBtnLayout);
		}
		catch(Exception e)
		{
			Log.i(TAG, "Error in menuLoader : "+e.toString());
			Toast.makeText(this, "Exception in UtilityProviderActivity:menuLoader : " + e.toString(), Toast.LENGTH_LONG).show();
		}
	}
}
