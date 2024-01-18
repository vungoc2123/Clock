package com.example.notifycation;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;

import com.example.clock.R;

public class MyApplication extends Application {
    public static final String CHANNEL_ID = "push_notification_id";
    @Override
    public void onCreate() {
        super.onCreate();
        createChannelNotification();
    }

    private void createChannelNotification() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "PushNotification", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager = getSystemService(NotificationManager.class);
//            AudioAttributes audioAttributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build();
            manager.createNotificationChannel(channel);
        }
    }
}
