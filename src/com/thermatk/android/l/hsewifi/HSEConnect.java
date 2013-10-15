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
		RequestParams params = new RequestParams();
        params.put("buttonClicked", "4");
        params.put("err_flag", "0");
        params.put("err_msg", "");
        params.put("info_flag", "0");
        params.put("info_msg", "");
        params.put("redirect_url", "");
        params.put("username", "hseguest");
        params.put("password", "hsepassword");
        client.post("https://wlc22.hse.ru/login.html", params, new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(final String response) {
				handler.post(new Runnable() {
					@Override
					public void run() {
						Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.authsent),
								Toast.LENGTH_SHORT).show();
					}
				});
			}
		});
	}

}
