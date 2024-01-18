package com.example.config;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import com.example.broadcast.MyBroadcast;
import com.example.clock.R;
import com.example.fragment.addAlarmFragment;
import com.example.model.AlarmModel;

import java.util.Calendar;

public class ConfigAlarmManager {
    public void startAlarm(Context context, AlarmModel alarmModel, String nameFragment, Boolean isRepeat) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        Intent intent = new Intent(context, MyBroadcast.class);
        intent.putExtra(ConfigString.nameFragment, nameFragment);
        intent.putExtra(ConfigString.label, alarmModel.getLabel());
        intent.putExtra(ConfigString.sound, alarmModel.getSound());
        String[] parts = alarmModel.getTime().split(":");
        String repeatDay = ConfigDay.handleAddComma(alarmModel.getRepeatDays());
        String listDay = repeatDay.replaceAll("\\s+", " ");
        String[] days = listDay.split(" ");
        if (days[0].equals(context.getString(R.string.every))) {
            if (Resources.getSystem().getConfiguration().locale.getLanguage().equals("vi")) {
                if (days[1].equals(context.getString(R.string.day))) {
                    days[0] = days[0] + " " + days[1];
                    days[1] = "";
                }else{
                    days[0] = days[0] + " " + days[1] + " " + days[2];
                    days[1] = "";
                    days[2] = "";
                }
            } else {
                days[0] =days[0] + " " + days[1];
                days[1] = "";
            }
        }
        if (Resources.getSystem().getConfiguration().locale.getLanguage().equals("vi")) {
            days = repeatDay.split(",");
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(parts[0]));
        calendar.set(Calendar.MINUTE, Integer.parseInt(parts[1]));
        calendar.set(Calendar.SECOND, 0);

        if (days[0].isEmpty()) {
            if (System.currentTimeMillis() > calendar.getTimeInMillis()) {
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            }
            PendingIntent alarmIntent = PendingIntent.getBroadcast(context, (int) calendar.getTimeInMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                    calendar.getTimeInMillis(), alarmIntent);
        } else {
            if(days[0].equals(context.getString(R.string.every_day))){
                days = new String[7];
                days[0] = context.getString(R.string.mon);
                days[1] = context.getString(R.string.tue);
                days[2] = context.getString(R.string.wed);
                days[3] = context.getString(R.string.thu);
                days[4] = context.getString(R.string.fri);
                days[5] = context.getString(R.string.sat);
                days[6] = context.getString(R.string.sun);

            }
            for (int i = 0; i < days.length; i++) {
                Calendar dayCalendar = (Calendar) calendar.clone();
                dayCalendar.set(Calendar.DAY_OF_WEEK, handleRepeatDay(context, days[i].trim()));
                if (System.currentTimeMillis() > dayCalendar.getTimeInMillis()) {
                    dayCalendar.add(Calendar.WEEK_OF_YEAR, 1);
                }
                PendingIntent alarmIntent = PendingIntent.getBroadcast(context, (int) dayCalendar.getTimeInMillis(), intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                        dayCalendar.getTimeInMillis(), alarmIntent);
            }
        }

    }

    public void alarmTimer(Context context, String sound) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
        Intent intent = new Intent(context, MyBroadcast.class);
        intent.putExtra(ConfigString.nameFragment, ConfigString.timerFragment);
        intent.putExtra(ConfigString.label, "timer");
        intent.putExtra(ConfigString.sound, sound);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        Calendar calendar = Calendar.getInstance();
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    public int handleRepeatDay(Context context, String day) {
        if (day.equals(context.getString(R.string.mon))) {
            return Calendar.MONDAY;
        } else if (day.equals(context.getString(R.string.tue))) {
            return Calendar.TUESDAY;
        } else if (day.equals(context.getString(R.string.wed))) {
            return Calendar.WEDNESDAY;
        } else if (day.equals(context.getString(R.string.thu))) {
            return Calendar.THURSDAY;
        } else if (day.equals(context.getString(R.string.fri))) {
            return Calendar.FRIDAY;
        } else if (day.equals(context.getString(R.string.sat))) {
            return Calendar.SATURDAY;
        } else if (day.equals(context.getString(R.string.sun))) {
            return Calendar.SUNDAY;
        } else if (day.equals(context.getString(R.string.every_monday))) {
            return Calendar.MONDAY;
        } else if (day.equals(context.getString(R.string.every_tuesday))) {
            return Calendar.TUESDAY;
        } else if (day.equals(context.getString(R.string.every_wednesday))) {
            return Calendar.WEDNESDAY;
        } else if (day.equals(context.getString(R.string.every_thursday))) {
            return Calendar.THURSDAY;
        } else if (day.equals(context.getString(R.string.every_friday))) {
            return Calendar.FRIDAY;
        } else if (day.equals(context.getString(R.string.every_saturday))) {
            return Calendar.SATURDAY;
        } else if (day.equals(context.getString(R.string.every_sunday))) {
            return Calendar.SUNDAY;
        } else {
            return 0;
        }
    }
}
