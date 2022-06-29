package com.michaelhefner.michaelhefnerc196;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.sql.Time;
import java.time.LocalDateTime;
import java.util.Date;


public class AlarmIntentService extends IntentService {
    private final String CHANNEL_ID_TIMER = "channel_timer";
    private final int NOTIFICATION_ID = 0;

    public AlarmIntentService() {
        super("AlarmIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Date date = new Date();
        if (intent != null) {
            int duration = intent.getIntExtra("DURATION", 0);
            String message = intent.getStringExtra("MESSAGE");
            date.setTime(System.currentTimeMillis() + duration);
            createTimerNotificationChannel();
            createTimerNotification(message);
            createTimerNotification(intent.getStringExtra("MESSAGE"));

            Handler handler = new Handler(Looper.getMainLooper());
            Runnable runnable = () -> {
                createTimerNotification(message);
            };
            handler.postDelayed(runnable,date.getTime() - System.currentTimeMillis());
        }
    }

    private void createTimerNotificationChannel() {
        if (Build.VERSION.SDK_INT < 26) return;

        CharSequence name = "channel_name";
        String description = "channel_description";
        int importance = NotificationManager.IMPORTANCE_LOW;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID_TIMER, name, importance);
        channel.setDescription(description);

        // Register channel with system
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }


    private void createTimerNotification(String text) {

        // Create notification with various properties
        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID_TIMER)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(getString(R.string.app_name))
                .setContentText(text)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build();

        // Get compatibility NotificationManager
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // Post notification using ID.  If same ID, this notification replaces previous one
        notificationManager.notify(NOTIFICATION_ID, notification);
    }
}