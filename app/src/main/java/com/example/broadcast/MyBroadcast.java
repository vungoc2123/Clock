package com.example.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.content.ContextCompat;

import com.example.service.MyService;

public class MyBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String nameFragment = intent.getStringExtra("nameFragment");
        String label = intent.getStringExtra("label");
        String alarmTone = intent.getStringExtra("alarmTone");
        Intent intent1 = new Intent(context, MyService.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent1.putExtra("nameFragment", nameFragment);
        intent1.putExtra("label", label);
        intent1.putExtra("alarmTone", alarmTone);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent1);
        } else {
            context.startService(intent1);
        }
    }
}
