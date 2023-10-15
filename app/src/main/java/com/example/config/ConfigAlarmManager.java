package com.example.config;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.example.broadcast.MyBroadcast;
import com.example.model.AlarmModel;

import java.util.Calendar;

public class ConfigAlarmManager {

    public void startAlarmRepeat(Context context, AlarmModel alarmModel, String nameFragment) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        Intent intent = new Intent(context, MyBroadcast.class);
        intent.putExtra("nameFragment", nameFragment);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        String[] parts = alarmModel.getTime().split(":");
        String[] days = alarmModel.getRepeatDays().split(",");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(parts[1]));
        calendar.set(Calendar.SECOND, 0);


        for (int i = 0; i < days.length; i++) {
            calendar.set(Calendar.DAY_OF_WEEK, handleRepeatDay(days[i]));
            if (System.currentTimeMillis() > calendar.getTimeInMillis()) {
                calendar.add(Calendar.DAY_OF_WEEK, 7); // Lặp lại hàng tuần
            }
        }

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60 * 2, pendingIntent);
    }

    public void startAlarm(Context context, AlarmModel alarmModel, String nameFragment) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        Intent intent = new Intent(context, MyBroadcast.class);
        intent.putExtra("nameFragment", nameFragment);
        intent.putExtra("label", alarmModel.getLabel());
        intent.putExtra("alarmTone", alarmModel.getSound());
        String[] parts = alarmModel.getTime().split(":");
        String[] days = alarmModel.getRepeatDays().split(",");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(parts[1]));
        calendar.set(Calendar.SECOND, 0);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        if (days[0].isEmpty()) {
            if (System.currentTimeMillis() > calendar.getTimeInMillis()) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            }
            alarmManager.set(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(), alarmIntent);
        }else{
            for (int i = 0; i < days.length; i++) {
                calendar.set(Calendar.DAY_OF_WEEK, handleRepeatDay(days[i]));
                if (System.currentTimeMillis() > calendar.getTimeInMillis()) {
                    calendar.add(Calendar.WEEK_OF_YEAR,1);
                }
                alarmManager.set(AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(), alarmIntent);
//                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 5 * 60 * 1000, alarmIntent);
            }
        }

    }

    public void alarmTimer(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        Intent intent = new Intent(context, MyBroadcast.class);
        intent.putExtra("nameFragment", "timerFragment");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        Calendar calendar = Calendar.getInstance();
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    public int handleRepeatDay(String day) {
        switch (day) {
            case "Monday":
                return Calendar.MONDAY;
            case "Tuesday":
                return Calendar.TUESDAY;
            case "Wednesday":
                return Calendar.WEDNESDAY;
            case "Thursday":
                return Calendar.THURSDAY;
            case "Friday":
                return Calendar.FRIDAY;
            case "Saturday":
                return Calendar.SATURDAY;
            case "Sunday":
                return Calendar.SUNDAY;
            default:
                return 10;
        }
    }
}
