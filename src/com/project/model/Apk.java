package com.project.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Apk implements Parcelable 
{	
	private String name;
	private String packageName;
	private String fileName;
	private String apk_s3url;
	private String details_s3url;

	public String getApk_s3url() {
		return apk_s3url;
	}

	public void setApk_s3url(String apk_s3url) {
		this.apk_s3url = apk_s3url;
	}

	public String getDetails_s3url() {
		return details_s3url;
	}

	public void setDetails_s3url(String details_s3url) {
		this.details_s3url = details_s3url;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getName() 
	{
		return name;
	}

	public void setName(String name) 
	{
		this.name = name;
	}

	public String getPackageName() 
	{
		return packageName;
	}

	public void setPackageName(String packageName) 
	{
		this.packageName = packageName;
	}

	public static final Parcelable.Creator<Apk> CREATOR = new Parcelable.Creator<Apk>() {
		public Apk createFromParcel(Parcel p) {
			return new Apk(p);
		}

		public Apk[] newArray(int size) {
			return new Apk[size];
		}
	};
	
	public Apk(Parcel p) {		
		String s[] = new String[5]; 
		p.readStringArray(s);
		this.name = s[0];
		this.packageName = s[1];
		this.fileName = s[2];
		this.apk_s3url = s[3];
		this.details_s3url = s[4];
	}
	
	public Apk(String ...args) {		
		this.name = args[0];
		this.packageName = args[1];
		this.fileName = args[2];
		this.apk_s3url = args[3];
		this.details_s3url = args[4];
	}	

	@Override
	public void writeToParcel(Parcel out, int arg1) {
		out.writeStringArray(new String[]{this.name, this.packageName, this.fileName, this.apk_s3url, this.details_s3url});
	}
	
	@Override
	public int describeContents() {
		// Do not implement!
		return 0;
	}
}
