package com.example.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.broadcast.MyBroadcast;
import com.example.clock.MainActivity;
import com.example.clock.R;
import com.example.notifycation.MyApplication;

public class MyService extends Service {
    MediaPlayer my_player;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String nameFragment = intent.getStringExtra("nameFragment");
        sendNotification("stop",nameFragment);
        if ("stop".equals(intent.getAction())) {
            my_player.stop();
            my_player = null;
            stopSelf();
        }else{
            if(my_player == null){
                my_player = MediaPlayer.create( this, R.raw.chuyencuboqua);
                my_player.start();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void sendNotification(String label,String nameFragment) {
        Intent stopIntent = new Intent(this, MyService.class);
        stopIntent.setAction("stop");
        PendingIntent stopPendingIntent = PendingIntent.getService(this, 0, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("nameFragment", nameFragment);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        @SuppressLint("RemoteViewLayout") RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.custom_notification_small);
        notificationLayout.setTextViewText(R.id.tv_label,label);
        notificationLayout.setOnClickPendingIntent(R.id.cv_notification,stopPendingIntent);

        NotificationCompat.Builder  notificationBuilder = new NotificationCompat.Builder(this, MyApplication.CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setCustomContentView(notificationLayout)
                .setColor(Color.parseColor("#131313"))
                .setContentIntent(pendingIntent);
        Notification notification = notificationBuilder.build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(notificationManager != null){
            notificationManager.notify(1,notification);
        }
        startForeground(1, notification);
    }
}
