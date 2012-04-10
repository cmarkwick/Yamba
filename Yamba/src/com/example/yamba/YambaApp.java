package com.example.yamba;

import java.util.List;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.Twitter.Status;
import winterwell.jtwitter.TwitterException;
import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;

public class YambaApp extends Application implements
		OnSharedPreferenceChangeListener {

	static final String TAG = "YambaApp";
	static final String ACTION_NEW_STATUS = "com.example.yamba.NEW_STATUS";
	static final String ACTION_REFRESH = "com.example.yamba.RefreshService";
	static final String ACTION_REFRESH_ALARM = "com.example.yamba.RefreshAlarm";

	private Twitter twitter;
	SharedPreferences prefs;
	StatusData statusData;

	@Override
	public void onCreate() {
		super.onCreate();

		// prefs stuff
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
		Log.d(TAG, "OnCreated");

		statusData = new StatusData(this);
	}

	// twitter stuff
	public Twitter getTwitter() {
		if (twitter == null) {
			String username = prefs.getString("username", "");
			String password = prefs.getString("password", "");
			String server = prefs.getString("server", "");
			twitter = new Twitter(username, password);
			twitter.setAPIRootUrl(server);
		}
		return twitter;

	}
	static final Intent refreshAlarm = new Intent(ACTION_REFRESH_ALARM);
	@Override
	public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
		twitter = null;
		sendBroadcast(refreshAlarm);
		Log.d(TAG, "onSharedPreferenceChanged(SharedPreferences for key: "
				+ key);

	}

	long lastTimestampSeen = -1;

	public int pullAndInsert() {
		int count = 0;
		long biggestTimestamp = -1;
		try {
			List<Status> timeline = getTwitter().getPublicTimeline();

			for (Status status : timeline) {
				statusData.insert(status);
				//if (biggestTimestamp == -1) biggestTimestamp = status.createdAt.getTime();
				if (status.createdAt.getTime() > this.lastTimestampSeen) {
					count++;
					biggestTimestamp = (status.createdAt.getTime() > biggestTimestamp) ? status.createdAt
							.getTime() : biggestTimestamp;
					Log.d(TAG, String.format("%s: %s", status.user.name,
							status.text));
				}
			}
		} catch (TwitterException e) {
			Log.e(TAG, "Failed to pull timeline ", e);
		}
		if (count > 0) {
			sendBroadcast(new Intent(ACTION_NEW_STATUS)
					.putExtra("count", count));
		}
		this.lastTimestampSeen = biggestTimestamp;
		return count;
	}
}
