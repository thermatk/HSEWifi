package com.thermatk.android.l.hsewifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

import static com.thermatk.android.l.hsewifi.Logger.log;

public class WiFiReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        log("Broadcast received");

        WifiManager wifiMan = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        NetworkInfo networkInfo = intent.getParcelableExtra(wifiMan.EXTRA_NETWORK_INFO);

        if (intent.getAction().equals(wifiMan.NETWORK_STATE_CHANGED_ACTION) && networkInfo.getState() == NetworkInfo.State.CONNECTED) {
            log("WiFi state: connected");

            WifiInfo wifiInfo = intent.getParcelableExtra(wifiMan.EXTRA_WIFI_INFO);
            if (wifiInfo == null) {
                log("WiFi info is null");
                return;
            }
            String wifiName = wifiInfo.getSSID();
            log("WiFi name is " + wifiName);

            if (wifiName.equals("\"HSE\"") || wifiName.equals("HSE")) {
                Toast.makeText(context, R.string.connectedto, Toast.LENGTH_SHORT).show();
                context.startService(new Intent(context, HSEConnect.class));
            }
        }
    }
}