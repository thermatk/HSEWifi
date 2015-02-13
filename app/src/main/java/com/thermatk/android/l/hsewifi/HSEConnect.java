package com.thermatk.android.l.hsewifi;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.util.Iterator;
import java.util.List;

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
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
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
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                    WifiManager wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    if (wm.setWifiEnabled(true)) {

                        Log.i("HSEWIFI", "HSE WIFI ONN");
                        List<WifiConfiguration> networks = wm.getConfiguredNetworks();
                        Iterator<WifiConfiguration> iterator = networks.iterator();
                        while (iterator.hasNext()) {
                            Log.i("HSEWIFI", "HSE WIFI ON 222");
                            WifiConfiguration wifiConfig = iterator.next();
                            if (wifiConfig.SSID.equals("\"HSE\"")) {
                                Log.i("HSEWIFI", "HSE WIFI ON 232323");
                                boolean state = wm.enableNetwork(wifiConfig.networkId, true);
                                Log.i("HSEWIFI", "HSE WIFI ON " + state);

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Успех!",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else
                                wm.disableNetwork(wifiConfig.networkId);
                        }
                        wm.reconnect();
                        Log.i("HSEWIFI", "HSE WIFI ON 3333");
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
