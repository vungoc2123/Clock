package com.example.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.model.TimeZoneModel;

import java.util.ArrayList;
import java.util.List;

public class TimeZoneDAO {
    private Context context;
    private SQLiteDatabase database;
    private SQLiteHelper helper;

    public TimeZoneDAO(Context context) {
        this.context = context;
        helper = new SQLiteHelper(context);
        database = helper.getWritableDatabase();
    }

    public List<TimeZoneModel> getAllTimeZone() {
        List<TimeZoneModel> list = new ArrayList<>();
        try {
            Cursor cursor = database.rawQuery("SELECT * FROM TIMEZONE", null);
            if (cursor.getCount() != 0) {
                cursor.moveToFirst();
                do {
                    list.add(new TimeZoneModel(cursor.getInt(0), cursor.getString(1), cursor.getString(2)));
                } while (cursor.moveToNext());
            }
            cursor.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return list;
    }


}
