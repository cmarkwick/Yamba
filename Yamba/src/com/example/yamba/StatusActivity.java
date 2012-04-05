package com.example.yamba;

import winterwell.jtwitter.Twitter;
import winterwell.jtwitter.TwitterException;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class StatusActivity extends Activity {

	static final String TAG = "StatusActivity";
	EditText editStatus;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Debug.startMethodTracing("Yamba.trace");

		Log.d(TAG, "OnCreated with Bundle: " + savedInstanceState);
		setContentView(R.layout.status);

		editStatus = (EditText) findViewById(R.id.edit_status);

	}

	// @Override
	// protected void onStop() {
	//
	// super.onStop();
	// Debug.stopMethodTracing();
	// }

	public void onClick(View v) {
		final String statusText = editStatus.getText().toString();

		new PostToTwitter().execute(statusText);

		Log.d(TAG, "OnClicked with text: " + statusText);
	}

	class PostToTwitter extends AsyncTask<String, Void, String> {

		// New Thread
		@Override
		protected String doInBackground(String... params) {
			try {
				Twitter twitter = new Twitter("student", "password");
				twitter.setAPIRootUrl("http://yamba.marakana.com/api");
				twitter.setStatus(params[0]);
				Log.d(TAG, "Successfully posted ");
			} catch (TwitterException e) {
				Log.e(TAG, "Died", e);
				e.printStackTrace();
				return "Failed" + params[0];
			}
			return "Successfully posted " + params[0];
		}

		// UI Thread
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Toast.makeText(StatusActivity.this, result, Toast.LENGTH_LONG)
					.show();
		}

	}

}
