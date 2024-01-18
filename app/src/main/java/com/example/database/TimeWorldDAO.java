package com.example.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.model.AlarmModel;
import com.example.model.TimeZoneModel;

import java.util.ArrayList;
import java.util.List;

public class TimeWorldDAO {

    private Context context;
    private SQLiteDatabase database;
    private SQLiteHelper helper;

    public TimeWorldDAO(Context context) {
        this.context = context;
        helper = new SQLiteHelper(context);
        database = helper.getWritableDatabase();
    }

    public int insert(TimeZoneModel timeZoneModel) {
        try {
            ContentValues values = new ContentValues();
            values.put("name", timeZoneModel.getName());
            values.put("timezone", timeZoneModel.getTimezone());
            long kq = database.insert("TIMEWORLD", null, values);
            if (kq <= 0) {
                return -1;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return 1;
    }

    public List<TimeZoneModel> getAllTimeWorld() {
        List<TimeZoneModel> list = new ArrayList<>();
        try {
            Cursor cursor = database.rawQuery("SELECT * FROM TIMEWORLD", null);
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

    public int delete(int id) {
        try {
            int kq = database.delete("TIMEWORLD", "id=?", new String[]{String.valueOf(id)});
            if (kq <= 0) {
                return -1;
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return 1;
    }
}
