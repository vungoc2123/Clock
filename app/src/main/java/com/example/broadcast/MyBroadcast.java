package com.example.broadcast;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.content.ContextCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.example.config.ConfigString;
import com.example.service.MyService;

public class MyBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String nameFragment = intent.getStringExtra(ConfigString.nameFragment);
        String label = intent.getStringExtra(ConfigString.label);
        String alarmTone = intent.getStringExtra(ConfigString.sound);
        Intent intent1 = new Intent(context, MyService.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent1.putExtra(ConfigString.nameFragment, nameFragment);
        intent1.putExtra(ConfigString.label, label);
        intent1.putExtra(ConfigString.sound, alarmTone);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent1);
        } else {
            context.startService(intent1);
        }

    }
}
