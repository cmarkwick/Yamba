package com.example.yamba;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

public class RefreshScheduleReceiver extends BroadcastReceiver {

	static PendingIntent lastOp;
	
	@Override
	public void onReceive(Context context, Intent intent) {

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		long interval = Long.parseLong(prefs.getString("delay", "900000"));
		PendingIntent operation = PendingIntent.getService(context, -1,
				new Intent(YambaApp.ACTION_REFRESH),
				PendingIntent.FLAG_UPDATE_CURRENT);
		
		AlarmManager alarmManager = (AlarmManager) context
				.getSystemService(Context.ALARM_SERVICE);
		
		alarmManager.cancel(lastOp);

		if (interval > 0) {
			alarmManager.setInexactRepeating(AlarmManager.RTC,
					System.currentTimeMillis(), interval, operation);
		} else {

		}
		
		lastOp = operation;
				
		Log.d("BootReceiver", String.format("onReceive : " + interval));
	}

}
