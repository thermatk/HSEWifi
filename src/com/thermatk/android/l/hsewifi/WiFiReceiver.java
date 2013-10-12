package com.thermatk.android.l.hsewifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.widget.Toast;

public class WiFiReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		NetworkInfo networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
		if (intent.getAction().equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)	&& networkInfo.getState() == NetworkInfo.State.CONNECTED) {
			WifiInfo wifiInfo = intent.getParcelableExtra(WifiManager.EXTRA_WIFI_INFO);
			if (wifiInfo == null) {
				return;
			}
			if(wifiInfo.getSSID().equals("\"StrategikonOfMaurice\"")) {
				Toast.makeText(context, context.getString(R.string.connectedto)+ " " + "HSE", Toast.LENGTH_SHORT).show();
				context.startService(new Intent(context, HSEConnect.class));
			}
		}
	}
}