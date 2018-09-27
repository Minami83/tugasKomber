package com.example.minami.indoorpositioning;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
import java.lang.Math;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.net.wifi.WifiInfo;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        printer();
        initializeWiFiListener();
    }

    public void printer(String a) {
        setContentView(R.layout.activity_main);
        TextView textView = (TextView) findViewById(R.id.mainText);
        textView.setText(a);
    }

    private void initializeWiFiListener() {
//        Log.i(TAG, "executing initializeWiFiListener");

        String context = Context.WIFI_SERVICE;
        final WifiManager wifi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

//        int rssi = wifiManager.getConnectionInfo().getRssi();
//        printer(String.valueOf(rssi));


        if (!wifi.isWifiEnabled()) {
            if (wifi.getWifiState() != WifiManager.WIFI_STATE_ENABLING) {
                wifi.setWifiEnabled(true);
            }
        }

        registerReceiver(new BroadcastReceiver() {

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onReceive(Context context, Intent intent) {

                WifiInfo info = wifi.getConnectionInfo();

                int value = info.getRssi();
                int freq = info.getFrequency();

                printer(String.valueOf(value));
                double fspl = 27.55; //some constant

                TextView notView = (TextView) findViewById(R.id.notText);
                notView.setText(String.valueOf(freq));

                double m = Math.pow(10, ((fspl - (20 * Math.log10(freq)) + value) / 20));
                TextView mainView = (TextView) findViewById(R.id.mainText);
                mainView.setText(String.valueOf(String.valueOf(m)));
            }

        }, new IntentFilter(WifiManager.RSSI_CHANGED_ACTION));
    }

}
