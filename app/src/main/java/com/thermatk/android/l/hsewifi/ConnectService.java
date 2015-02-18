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

public class ConnectService extends Service {
    private Handler handler;

    public int onStartCommand(Intent intent, int flags, int startId) {
        handler = new Handler();
        Log.i(LogConst.LOG, "CONNECT SERVICE");
        if (isLollipop()){
            switchNetwork();
        }
        makeRequest();
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    private void makeRequest() {

        RequestParams params = new RequestParams();
        params.put("buttonClicked", "4");
        params.put("username", "mosmetro");
        params.put("password", "gfhjkm");
        params.put("redirect_url", "http://vmet.ro");
        params.put("err_flag", "0");

        Log.i(LogConst.LOG, "SENDING REQUEST");
        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://1.1.1.1/login.html", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                toastMessage(getApplicationContext().getString(R.string.authsent));
                Log.i(LogConst.LOG, "GOOD REQUEST " + statusCode);
                if (isLollipop()){
                    Log.i(LogConst.LOG, "LOLLIPOP, REPORT GOOD NETWORK");
                    reportGoodNetwork();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.i(LogConst.LOG, "FAILED REQUEST " + statusCode);
            }
        });
    }

    private boolean isLollipop() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            return true;
        } else {
            return false;
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void switchNetwork() {
        ConnectivityManager connection_manager =
                (ConnectivityManager) getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkRequest.Builder request = new NetworkRequest.Builder();
        request.addTransportType(NetworkCapabilities.TRANSPORT_WIFI);

        connection_manager.registerNetworkCallback(request.build(), new ConnectivityManager.NetworkCallback() {
            @Override
            public void onAvailable(Network network) {
                ConnectivityManager.setProcessDefaultNetwork(network);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void reportGoodNetwork() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        for (Network net : cm.getAllNetworks()) {
            if (cm.getNetworkInfo(net).getType() == ConnectivityManager.TYPE_WIFI) {
                cm.reportBadNetwork(net);
            }
        }
    }

    private void toastMessage(final String message) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public IBinder onBind(Intent intent) {
        return null;
    }
}
