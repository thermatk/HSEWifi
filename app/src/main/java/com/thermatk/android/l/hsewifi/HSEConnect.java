package com.thermatk.android.l.hsewifi;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

import static com.thermatk.android.l.hsewifi.Logger.log;


public class HSEConnect extends Service {
    private Handler handler;
    private ConnectivityManager connectivityManager;
    private OkHttpClient client;

    public int onStartCommand(Intent intent, int flags, int startId) {
        handler = new Handler();

        connectivityManager =
                (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(BuildConfig.DEBUG) {
            client = new OkHttpClient.Builder()
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build();
        } else {
            client = new OkHttpClient();
        }

        log("AutoLogin service started");

        defaultToWifi();
        sendInfo();
        stopSelf();

        return super.onStartCommand(intent, flags, startId);
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    private void sendInfo() {
        RequestBody formBody = new FormBody.Builder()
                .add("buttonClicked", "4")
                .add("err_flag", "0")
                .add("err_msg", "")
                .add("info_flag", "0")
                .add("info_msg", "")
                .add("redirect_url", "")
                .add("username", "hseguest")
                .add("password", "hsepassword")
                .build();

        Request request = new Request.Builder()
                .url("https://wlc22.hse.ru/login.html")
                .post(formBody)
                .build();

        log("Sending AuthRequest");

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                log("AuthRequest failed: " + e.getMessage());
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    log("AuthRequest unexpected code " + response);
                }

                toast(R.string.authsent);
                recheckNetwork();

                Headers responseHeaders = response.headers();
                for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                    log("OkHttp Header " + responseHeaders.name(i) + ": " + responseHeaders.value(i));
                }

                // necessary to close ResponseBody
                String responseString = response.body().string();
                if(responseString!=null){
                    log("OkHttp ResponseBody: " + response.body().string());
                }
            }
        });
    }

    private void toast(final int resId) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), resId,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void defaultToWifi() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            connectivityManager.setNetworkPreference(ConnectivityManager.TYPE_WIFI);
            return;
        }

        Network network = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            network = getNetwork();
        }

        if(network != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                connectivityManager.bindProcessToNetwork(getNetwork());
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                try {
                    ConnectivityManager.setProcessDefaultNetwork(network);
                } catch (IllegalStateException ignored) {

                }
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private Network getNetwork() {
        for (Network network : connectivityManager.getAllNetworks()) {
            NetworkInfo info = connectivityManager.getNetworkInfo(network);
            if (info != null && info.getType() == ConnectivityManager.TYPE_WIFI) {
                return network;
            }
        }
        log("No WiFi found, null");
        return null;
    }

    @SuppressWarnings("deprecation")
    private void recheckNetwork() {
        Network network;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            network = getNetwork();
        } else {
            return;
        }

        if(network != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                connectivityManager.reportNetworkConnectivity(network,true);
                return;
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                connectivityManager.reportBadNetwork(network);
            }
        }
    }

}
