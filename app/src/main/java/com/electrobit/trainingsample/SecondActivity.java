package com.electrobit.trainingsample;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.electrobit.trainingsample.background.FetchLocationService;
import com.electrobit.trainingsample.storage.AppDatabase;
import com.electrobit.trainingsample.storage.Note;
import com.electrobit.trainingsample.storage.NotesDao;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class SecondActivity extends AppCompatActivity {

    public static String BUNDLE_KEY = "value";
    private static final String TAG = SecondActivity.class.getSimpleName();
    TextView secondaryText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent previousActivity = getIntent();
        Bundle bundle = previousActivity.getExtras();
        long calculation = bundle.getLong(BUNDLE_KEY);

        Button button = (Button)findViewById(R.id.getNoteButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showSavedNotes();
                secondaryText.setText("Now value is " + calculation);
            }
        });

        Button serviceButton = (Button)findViewById(R.id.startServiceButton);
        serviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startLocationUpdates();
            }
        });

        secondaryText = (TextView) findViewById(R.id.secondaryData);
        Log.d(TAG, "Thread Id " + Thread.currentThread().getId());

        secondaryText.setText("Calculated value is " + calculation);

        //showNotification();
        Thread backgroundthread = new Thread(new BackgroundTask(calculation));
        backgroundthread.start();


        //insertNote();

        Log.d(TAG, "Now the value is" + calculation);
    }

    public void startLocationUpdates(){
        Intent serviceIntent = new Intent(this, FetchLocationService.class);
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    private void showNotification(){

        Notification.Builder builder = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            builder = new Notification.Builder(this, "ChannelId");
        }
        builder.setContentText("Notification body");
        builder.setContentTitle("Notification title");
        builder.setSmallIcon(R.drawable.baseline_notifications_none_24);
        Notification notification = builder.build();
        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("ChannelId",
                    "notification", NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(channel);
        }

        manager.notify(1111,notification);
    }

    class BackgroundTask implements Runnable {

        long value;
        public BackgroundTask(long value){
            this.value = value;
        }

        @Override
        public void run() {
            while (true){
                try {
                    Thread.sleep(2000);
                    Log.d(TAG, "value is " + ++value + " Thread id " + Thread.currentThread().getId() );
                    secondaryText.setText("Updated from background thread value is " + value);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
