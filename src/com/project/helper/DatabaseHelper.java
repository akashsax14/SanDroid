package com.project.helper;

import java.util.ArrayList;

import com.project.model.Apk;
import com.project.model.User;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper 
{

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "modified_apks";
	private static final String TABLE_APKS = "apks";
	private static final String COLUMN_APK_ID = "_id"; // convention
	private static final String COLUMN_APK_NAME = "name";
	private static final String COLUMN_APK_PACKAGE_NAME = "package_name";
	private static final String COLUMN_APK_FILE_NAME = "file_name";
	
	private static final String TABLE_USER = "user";
	private static final String COLUMN_USER_ID = "_id"; // convention
	private static final String COLUMN_USER_NAME = "username";
	private static final String COLUMN_BUCKET_NAME = "bucket";
	
	private static final String TAG = "Databasehelper";
	public DatabaseHelper(Context context)
	{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		db.execSQL("create table " + TABLE_APKS + "( " 
				+ COLUMN_APK_ID + " integer not null primary key autoincrement, " 
				+ COLUMN_APK_NAME + " text not null, " 
				+ COLUMN_APK_PACKAGE_NAME + " text not null, "
				+ COLUMN_APK_FILE_NAME + " text not null, "
				+ COLUMN_USER_NAME + " text not null )");
		
		db.execSQL("create table " + TABLE_USER + "( " 
				+ COLUMN_USER_ID + " integer not null primary key autoincrement, " 
				+ COLUMN_USER_NAME + " text not null, "
				+ COLUMN_BUCKET_NAME + " text not null )");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) 
	{
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_APKS);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);
		onCreate(db);

	}

	public void insertAPKS(Apk apk_info, String username) 
	{
		try
		{
			ContentValues cv = new ContentValues();
			cv.put(COLUMN_APK_NAME, apk_info.getName());
			cv.put(COLUMN_APK_PACKAGE_NAME, apk_info.getPackageName());
			cv.put(COLUMN_APK_FILE_NAME, apk_info.getFileName());
			cv.put(COLUMN_USER_NAME, username);
			getWritableDatabase().insert(TABLE_APKS, null, cv);
		}
		catch(Exception e)
		{
			Log.i(TAG, "Error in insertAPKS : " + e.toString());
		}
	}
	
	public void insertUser(User user) 
	{
		try
		{
			ContentValues cv = new ContentValues();
			cv.put(COLUMN_USER_NAME, user.getUsername());
			cv.put(COLUMN_BUCKET_NAME, user.getBucketname());
			long id = getWritableDatabase().insert(TABLE_USER, null, cv);
			user.setUserid(id);
		}
		catch(Exception e)
		{
			Log.i(TAG, "Error in insertUser : " + e.toString());
		}
	}
	
	public int deleteApkEntry(Apk apk_info, String username)
	{
		try
		{
			SQLiteDatabase db = this.getReadableDatabase();
			String where = COLUMN_USER_NAME + " = ? AND " + COLUMN_APK_PACKAGE_NAME + " = ?";
			String[] whereArgs = {username, apk_info.getPackageName()};
			int result = db.delete(TABLE_APKS, where, whereArgs);
			
			return result;
		}
		catch(Exception e)
		{
			Log.i(TAG, "Error in deleteApkEntry : " + e.toString());
			return -1;
		}
	}
	
	public int deleteAllApkEntries(String username)
	{
		try
		{
			SQLiteDatabase db = this.getReadableDatabase();
			int result = db.delete(TABLE_APKS, COLUMN_USER_NAME + " = ?", new String[]{username});
			
			return result;
		}
		catch(Exception e)
		{
			Log.i(TAG, "Error in deleteApkEntry : " + e.toString());
			return -1;
		}
	}
	
	public int deleteUser()
	{
		try
		{
			SQLiteDatabase db = this.getReadableDatabase();
			int result = db.delete(TABLE_USER, null, null);
			
			return result;
		}
		catch(Exception e)
		{
			Log.i(TAG, "Error in deleteUser : " + e.toString());
			return -1;
		}
	}
	
	public User getCurrentUser()
	{
		User user = new User();
		try
		{
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery("SELECT * FROM "+ TABLE_USER, null);
			if(cursor.moveToFirst())
			{
				user.setUserid(cursor.getLong(0));
				user.setUsername(cursor.getString(1));
				user.setBucketname(cursor.getString(2));
				
				return user;
			}
			else
			{
				return null;
			}
		}
		catch(Exception e)
		{
			Log.i(TAG, "Error in getCurrentUser : " + e.toString());
			return null;
		}
	}
	
	public ArrayList<Apk> getAllApk(String username) 
	{
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.query(true, TABLE_APKS, new String[]{COLUMN_APK_NAME, COLUMN_APK_PACKAGE_NAME, COLUMN_APK_FILE_NAME}, COLUMN_USER_NAME + " = ?", new String[]{username}, null, null, null, null);
		try 
		{
			ArrayList<Apk> apkList = new ArrayList<Apk>();		

			if (cursor != null) 
			{
				for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) 
				{
					Apk apk = new Apk(cursor.getString(0), cursor.getString(1), cursor.getString(2), null, null);
					apkList.add(apk);
				}
				return apkList;
			} 
			else 
			{
				return null;
			}
		} 
		catch (Exception e) 
		{
			Log.i(TAG, "Error in getAllApk : " + e.toString());
			return null;
		}
		finally
		{
			cursor.close();
		}
	}
}
