package com.example.yamba;

import java.util.List;

import winterwell.jtwitter.Twitter.Status;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class UpdaterService extends Service {
	static final String TAG = "UpdaterService";
	static final int DELAY = 30;
	Boolean running = false;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.d(TAG, "OnCreate");
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		running = true;
		final int delay = ((YambaApp) getApplication()).prefs.getInt("delay", DELAY);
		new Thread() {
			public void run() {
				try {
					List<Status> timeline = ((YambaApp) getApplication())
							.getTwitter().getPublicTimeline();
					for (Status status : timeline) {
						Log.d(TAG, String.format("%s: %s", status.user.name,
								status.text));
					}
					Thread.sleep(delay * 1000);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.start();

		Log.d(TAG, "OnStartCommand");
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		Log.d(TAG, "OnDestroy");
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

}
