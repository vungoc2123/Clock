package com.example.config;

import android.content.Context;
import android.content.res.Resources;

import com.example.clock.R;

import java.util.Calendar;

public class ConfigDay {
    public static String handleConvertText(Context context, String day) {
        if (day.equals(context.getString(R.string.every_monday))) {
            return context.getString(R.string.mon);
        } else if (day.equals(context.getString(R.string.every_tuesday))) {
            return context.getString(R.string.tue);
        } else if (day.equals(context.getString(R.string.every_wednesday))) {
            return context.getString(R.string.wed);
        } else if (day.equals(context.getString(R.string.every_thursday))) {
            return context.getString(R.string.thu);
        } else if (day.equals(context.getString(R.string.every_friday))) {
            return context.getString(R.string.fri);
        } else if (day.equals(context.getString(R.string.every_saturday))) {
            return context.getString(R.string.sat);
        } else if (day.equals(context.getString(R.string.every_sunday))) {
            return context.getString(R.string.sun);
        } else {
            return day;
        }
    }


    public static String handleConvertNumber(Context context, String day) {
        if (day.equals(context.getString(R.string.mon))) {
            return context.getString(R.string.every_monday);
        } else if (day.equals(context.getString(R.string.tue))) {
            return context.getString(R.string.every_tuesday);
        } else if (day.equals(context.getString(R.string.wed))) {
            return context.getString(R.string.every_wednesday);
        } else if (day.equals(context.getString(R.string.thu))) {
            return context.getString(R.string.every_thursday);
        } else if (day.equals(context.getString(R.string.fri))) {
            return context.getString(R.string.every_friday);
        } else if (day.equals(context.getString(R.string.sat))) {
            return context.getString(R.string.every_saturday);
        } else if (day.equals(context.getString(R.string.sun))) {
            return context.getString(R.string.every_sunday);
        } else {
            return day;
        }
    }

    public static String handleAddComma(String days) {
        if (Resources.getSystem().getConfiguration().locale.getLanguage().equals("vi")) {
            return days.replace("2", "2,")
                    .replace("3", "3,")
                    .replace("4", "4,")
                    .replace("5", "5,")
                    .replace("6", "6,")
                    .replace("7", "7,")
                    .replace("CN", "CN,");
        }
        return days;
    }
}
