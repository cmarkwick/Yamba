package com.example.yamba;

import winterwell.jtwitter.Twitter.Status;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class StatusData {

	static final String TAG = "StatusData";
	public static final String DB_NAME = "timeline.db";
	public static final int DB_VERSION = 1;
	public static final String TABLE = "status";
	public static final String C_ID = BaseColumns._ID;
	public static final String C_CREATED_AT = "created_at";
	public static final String C_USER = "username";
	public static final String C_TEXT = "status_text";

	Context context;
	
	DbHelper dbHelper;
	SQLiteDatabase db;
	
	public StatusData(Context context){
		this.context = context;
		dbHelper = new DbHelper();
	}
	
	public void insert(Status status) {
		
		ContentValues values = new ContentValues();
		values.put(C_ID, status.id);
		values.put(C_CREATED_AT, status.createdAt.getTime());
		values.put(C_USER, status.user.name);
		values.put(C_TEXT, status.text);
		
		db = dbHelper.getWritableDatabase();
		db.insertWithOnConflict(TABLE, null, values, SQLiteDatabase.CONFLICT_IGNORE);
	}

	class DbHelper extends SQLiteOpenHelper {

		public DbHelper() {
			super(context, DB_NAME, null, DB_VERSION);

		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			String sql = String.format("Create table %s "
					+ "(%s int primary key, %s int, %s text, %s text)", TABLE,
					C_ID, C_CREATED_AT, C_USER, C_TEXT);
			Log.d(TAG, "onCreate with SQL: " +sql);
			db.execSQL(sql);

		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			//Usually ALTER TABLE statement
			db.execSQL("drop if exists" +TABLE);
			onCreate(db);

		}

	}
}
