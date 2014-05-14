package com.project.model;

import android.graphics.drawable.Drawable;

public class DeviceApps
{
	String appLabel;
	String packageName;
	Drawable icon;
	String size;

	public DeviceApps() 
	{
		super();
	}

	public DeviceApps(String appLabel, String packageName, Drawable icon, String size) 
	{
		super();
		this.appLabel = appLabel;
		this.packageName = packageName;
		this.icon = icon;
		this.size = size;
	}
	
	public String getAppLabel() {
		return appLabel;
	}

	public void setAppLabel(String appLabel) {
		this.appLabel = appLabel;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public Drawable getIcon() {
		return icon;
	}

	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	
	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}
}
