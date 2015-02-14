package com.thermatk.android.l.hsewifi;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.os.Build;
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

    public int onStartCommand(Intent intent, int flags, int startId) {
        handler = new Handler();
        Log.i("HSEWIFI", "4.A HSE SERVICE");
        sendInfo();
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    private void sendInfo() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            ConnectivityManager connection_manager =
                    (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkRequest.Builder request = new NetworkRequest.Builder();
            request.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);

            connection_manager.registerNetworkCallback(request.build(), new ConnectivityManager.NetworkCallback() {

                @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                @Override
                public void onAvailable(Network network) {
                    ConnectivityManager.setProcessDefaultNetwork(network);
                }
            });
        }

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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    for (Network net : cm.getAllNetworks()) {
                        if (cm.getNetworkInfo(net).getType() == ConnectivityManager.TYPE_WIFI) {
                            cm.reportBadNetwork(net);
                        }
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i("HSEWIFI", "6.A HSE FAILED REQUEST" + statusCode);
            }
        });
    }

}
