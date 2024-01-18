package com.example.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.clock.MainActivity;
import com.example.clock.R;
import com.example.config.ConfigString;
import com.example.database.AlarmDAO;
import com.example.model.AlarmModel;
import com.example.notifycation.MyApplication;

import java.io.IOException;
import java.util.Calendar;

public class MyService extends Service {
    MediaPlayer my_player;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String nameFragment = intent.getStringExtra(ConfigString.nameFragment);
        String label = intent.getStringExtra(ConfigString.label);
        String sound = intent.getStringExtra(ConfigString.sound);
        sendNotification(label, nameFragment);

        RingtoneManager ringtoneManager = new RingtoneManager(this);
        ringtoneManager.setType(RingtoneManager.TYPE_ALARM);
        Cursor cursor = ringtoneManager.getCursor();
        long id = 0;
        while (cursor.moveToNext()) {
            String title = cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX);
            if (title.equals(sound)) {
                id = cursor.getLong(RingtoneManager.ID_COLUMN_INDEX);
                break;
            }
            id = cursor.getLong(RingtoneManager.ID_COLUMN_INDEX);
        }

        Uri alarmUri = ContentUris.withAppendedId(Uri.parse("content://media/internal/audio/media"), id);
        if (ConfigString.stop.equals(intent.getAction())) {
            if (my_player != null) {
                my_player.stop();
                my_player.release();
                my_player = null;
            }
            stopSelf();
        } else {
            try {
                my_player = new MediaPlayer();
                my_player.setDataSource(this, alarmUri);
                my_player.prepare();
                my_player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        my_player.seekTo(0);
                        my_player.start();
                    }
                });
                my_player.start();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private void sendNotification(String label, String nameFragment) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(ConfigString.nameFragment, nameFragment);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) Calendar.getInstance().getTimeInMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.custom_notification_small);
        notificationLayout.setTextViewText(R.id.notification_title, label);
        notificationLayout.setOnClickPendingIntent(R.id.cv_notification, pendingIntent);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, MyApplication.CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_notifications_24)
                .setCustomBigContentView(notificationLayout)
                .setColor(Color.parseColor("#131313"))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent);

        Notification notification = notificationBuilder.build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager != null) {
            notificationManager.notify(1, notification);
        }
        startForeground(1, notification);
    }
}
