package com.example.yamba;

import winterwell.jtwitter.Twitter.Status;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.provider.BaseColumns;

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

	public StatusData(Context context) {
		this.context = context;
	}

	public void insert(Status status) {
		context.getContentResolver().insert(StatusProvider.CONTENT_URI, statusToValues(status));
	}

	public Cursor query() {

		return context.getContentResolver().query(StatusProvider.CONTENT_URI,
				null, null, null, StatusData.C_CREATED_AT + " DESC");
	}
	
	public ContentValues statusToValues(Status status){
		
		ContentValues values = new ContentValues();
		values.put(C_ID, status.id);
		values.put(C_CREATED_AT, status.createdAt.getTime());
		values.put(C_USER, status.user.name);
		values.put(C_TEXT, status.text);
		
		context.getContentResolver().insert(StatusProvider.CONTENT_URI, values);
		return values;
	}

}
