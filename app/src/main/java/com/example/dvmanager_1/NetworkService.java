package com.example.dvmanager_1;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import static com.example.dvmanager_1.MainActivity.showOfflineDialog;
import static com.example.dvmanager_1.MainActivity.showOfflinePOPup;

public class NetworkService extends Service implements Constants {

    public static final int TYPE_WIFI = 1;
    public static final int TYPE_MOBILE = 2;
    public static final int TYPE_NOT_CONNECTED = 0;
    public static final int NETWORK_STATUS_NOT_CONNECTED = 0;
    public static final int NETWORK_STATUS_WIFI = 1;
    public static final int NETWORK_STATUS_MOBILE = 2;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerBatteryLevelReceiver();
        Log.d("myCount", "Service onCreate: screenOnOffReceiver is registered.");
    }

    @Override
    public void onDestroy() {

        unregisterReceiver(network_receiver);
        super.onDestroy();
    }

    private BroadcastReceiver network_receiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            if(getConnectivityStatusString(context).equals("Offline")){
                showOfflinePOPup(context,true);
                showOfflineDialog();
            }else{
                showOfflinePOPup(context, false);
            }
        }
    };

    public String getConnectivityStatusString(Context context) {
        int conn = getConnectivityStatus(context);
        String status = "";
        if (conn == TYPE_WIFI) {
            status = "WIFI";
        } else if (conn == TYPE_MOBILE) {
            status = "Mobile Data";
        } else if (conn == TYPE_NOT_CONNECTED) {
            status = "Offline";
        }
        return status;
    }

    public int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    private void registerBatteryLevelReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        filter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        registerReceiver(network_receiver, filter);
    }

}
