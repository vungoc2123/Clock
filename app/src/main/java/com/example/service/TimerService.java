package com.example.service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.CountDownTimer;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.clock.R;
import com.example.config.ConfigAlarmManager;
import com.example.repository.TimerRepository;
import com.example.viewModel.TimeFragmentViewModel;

public class TimerService extends Service {
    public static CountDownTimer countDownTimer;
    private long timeSelected =0;


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        timeSelected = intent.getLongExtra("timeSelected",0);
        startTimer(timeSelected);
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private void startTimer(long time) {
        countDownTimer = new CountDownTimer(time, 1) {
            @Override
            public void onTick(long l) {
                long totalSeconds = l/ 1000;
                if(totalSeconds < 0){
                    countDownTimer.onFinish();
                }
                int hours = (int) (totalSeconds / 3600);
                int minutes = (int) ((totalSeconds % 3600) / 60);
                int seconds = (int) (totalSeconds % 60);
                TimerRepository.getInstance().setData(String.format("%02d:%02d:%02d",hours, minutes, seconds));
                TimerRepository.getInstance().setProgress((int)l);
            }
            @Override
            public void onFinish() {
                TimerRepository.getInstance().setProgress(0);
                ConfigAlarmManager configAlarmManager = new ConfigAlarmManager();
                configAlarmManager.alarmTimer(getApplicationContext());
//                setStart();
//                resetTimer();
//                alarm();
//                SharedPreferences sharedPreferences = getSharedPreferences("My_file", Context.MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putBoolean("isStart", false);
//                editor.putLong("timeInitial", 0);
//                editor.putInt("timeRemaining", 0);
//                editor.apply();
            }
        };
        countDownTimer.start();
    }
}
