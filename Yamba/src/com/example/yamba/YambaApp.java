package com.example.yamba;

import winterwell.jtwitter.Twitter;
import android.app.Application;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.preference.PreferenceManager;
import android.util.Log;

public class YambaApp extends Application implements OnSharedPreferenceChangeListener {

	static final String TAG = "YambaApp";
	private Twitter twitter;
	SharedPreferences prefs;

	@Override
	public void onCreate() {
		super.onCreate();
		
		//prefs stuff
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);
		Log.d(TAG,"OnCreated");
	}
	
	//twitter stuff
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

	@Override
	public void onSharedPreferenceChanged(SharedPreferences prefs,
			String key) {
		twitter = null;
		this.prefs = prefs;
		Log.d(TAG,"onSharedPreferenceChanged(SharedPreferences for key: " + key);
		
	}
}
