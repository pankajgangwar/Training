package com.electrobit.trainingsample.background;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.electrobit.trainingsample.AppReceiver;
import com.electrobit.trainingsample.R;

public class FetchLocationService extends Service {

    private static final String CHANNEL_ID = "ServiceChannelId";
    private static final int NOTIFICATION_ID = 11111;
    String TAG = FetchLocationService.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        boolean shouldStop = intent.getBooleanExtra("stopKey", false);
        Log.d(TAG, "shouldStop " + shouldStop);
        if(shouldStop){
            stopForeground(true);
            return super.onStartCommand(intent, flags, startId);
        }
        startForeground(NOTIFICATION_ID, createConnectionStatusNotification());
        requestLocationUpdates();
        return super.onStartCommand(intent, flags, startId);
    }

    private Notification createConnectionStatusNotification() {

        // You only need to create the channel on API 26+ devices
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
        Intent launchIntentForPackage = getPackageManager().getLaunchIntentForPackage(getPackageName());

        if (launchIntentForPackage == null) {
            return null;
        }

        Intent resumeAppIntent = launchIntentForPackage.setPackage(null).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);

        PendingIntent resumeAppPendingIntent, stopSignalIntent = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            resumeAppPendingIntent = PendingIntent.getActivity(getApplicationContext(), (int) System.currentTimeMillis(), resumeAppIntent, PendingIntent.FLAG_IMMUTABLE); //immutable pending intent
            stopSignalIntent = PendingIntent.getBroadcast(getApplicationContext(), (int) System.currentTimeMillis(), new Intent(getApplicationContext(), AppReceiver.class), PendingIntent.FLAG_IMMUTABLE);
        } else {
            resumeAppPendingIntent = PendingIntent.getActivity(getApplicationContext(), (int) System.currentTimeMillis(), resumeAppIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            stopSignalIntent = PendingIntent.getBroadcast(getApplicationContext(), (int) System.currentTimeMillis(), new Intent(getApplicationContext(), AppReceiver.class), PendingIntent.FLAG_IMMUTABLE);
        }

        //icon is added for the action just to support pre-nougat devices
        NotificationCompat.Builder notificationCompatBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setContentTitle("Foreground service")
                .setSmallIcon(R.drawable.baseline_notifications_none_24)
                .setChannelId(CHANNEL_ID)
                .setContentText("Service is running")
                .setPriority(Notification.PRIORITY_MAX)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setContentIntent(resumeAppPendingIntent)
                .addAction(R.drawable.baseline_notifications_none_24,
                        "Stop", stopSignalIntent)
                .setColor(ContextCompat.getColor(getApplicationContext(),
                        R.color.white));

        return notificationCompatBuilder.build();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationManager
                mNotificationManager =
                (NotificationManager) getApplicationContext()
                        .getSystemService(Context.NOTIFICATION_SERVICE);
        // The id of the channel.
        String id = CHANNEL_ID;
        // The user-visible name of the channel.
        CharSequence name = "Channel name";
        // The user-visible description of the channel.
        String description = "channel description";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel mChannel = new NotificationChannel(id, name, importance);
        // Configure the notification channel.
        mChannel.setDescription(description);
        mChannel.setShowBadge(false);
        mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        mNotificationManager.createNotificationChannel(mChannel);
    }

    private void requestLocationUpdates() {
        LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                5000L, 100.0f, new LocationListener() {
                    @Override
                    public void onLocationChanged(@NonNull Location location) {
                        Log.d(TAG, "onLocationChanged " + location.getLatitude() + "," + location.getLongitude());
                    }
                });

    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
