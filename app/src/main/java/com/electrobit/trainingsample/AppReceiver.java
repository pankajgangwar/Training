package com.electrobit.trainingsample;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.electrobit.trainingsample.background.FetchLocationService;

public class AppReceiver extends BroadcastReceiver {
    String TAG = AppReceiver.class.getSimpleName();
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, FetchLocationService.class);
        serviceIntent.putExtra("stopKey", true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startService(serviceIntent);
        }
    }
}
