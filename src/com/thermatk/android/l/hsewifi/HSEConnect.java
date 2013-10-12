package com.thermatk.android.l.hsewifi;

import com.loopj.android.http.*;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

public class HSEConnect extends Service {
	private Handler handler;

	public void onCreate() {
		super.onCreate();

	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		handler = new Handler();
		sendInfo();
		stopSelf();
		return super.onStartCommand(intent, flags, startId);
	}

	public void onDestroy() {
		super.onDestroy();

	}

	public IBinder onBind(Intent intent) {
		return null;
	}

	private void sendInfo() {
		AsyncHttpClient client = new AsyncHttpClient();
		client.get("http://ipecho.net/plain", new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(final String response) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(getApplicationContext(), response,
								Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}

}
