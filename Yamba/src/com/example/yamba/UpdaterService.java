package com.example.yamba;

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
		new Thread() {
			public void run() {
				try {
					while (running) {
						((YambaApp)getApplication()).pullAndInsert();
						int delay = Integer
								.parseInt(((YambaApp) getApplication()).prefs
										.getString("delay", "30"));
						Thread.sleep(delay * 1000);
					}

				} catch (InterruptedException e) {
					Log.e(TAG, "Updater interrupted");
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
