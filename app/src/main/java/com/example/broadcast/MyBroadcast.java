package com.example.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.service.MyService;

public class MyBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String nameFragment = intent.getStringExtra("nameFragment");
        Intent intent1 = new Intent(context, MyService.class);
        intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent1.putExtra("nameFragment", nameFragment);
        context.startService(intent1);
    }
}
