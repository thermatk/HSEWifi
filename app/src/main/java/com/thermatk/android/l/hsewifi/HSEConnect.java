package com.thermatk.android.l.hsewifi;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

public class HSEConnect extends Service {
    private Handler handler;

    public void onCreate() {
        super.onCreate();

    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        handler = new Handler();
        Log.i("HSEWIFI", "4.A HSE SERVICE");
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
        Log.i("HSEWIFI", "5.A HSE SENDING REQUEST");
        client.post("https://wlc22.hse.ru/login.html", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), getApplicationContext().getString(R.string.authsent),
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("HSEWIFI", "6.A HSE FAILED REQUEST" + statusCode);
            }
        });
    }

}
