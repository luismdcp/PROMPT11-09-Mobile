package com.application.yamba;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper
{
	static final String TAG = DbHelper.class.getSimpleName();
	static final String DB_NAME = "timeline.db";
	static final int DB_VERSION = 1;
	static final String TABLE = "timeline";
	static final String COLUMN_ID = BaseColumns._ID;
	static final String COLUMN_CREATED_AT = "created_at";
	static final String COLUMN_TEXT = "txt";
	static final String COLUMN_USER = "user";
	Context context;
	
	public DbHelper(Context context) 
	{
		super(context, DB_NAME, null, DB_VERSION);
		this.context = context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		String sql = "create table " + TABLE + " (" + COLUMN_ID + " int primary key, " + COLUMN_CREATED_AT + " int, " + COLUMN_USER + " text, " + COLUMN_TEXT + " text)";
		db.execSQL(sql);
		Log.d(TAG, "onCreated sql: " + sql);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
	{
		db.execSQL("drop table if exists " + TABLE);
		Log.d(TAG, "onUpdated");
		onCreate(db);
	}
}