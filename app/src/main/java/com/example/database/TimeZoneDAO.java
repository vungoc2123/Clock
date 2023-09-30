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

    public List<TimeZoneModel> getAllTimeZone(){
        Cursor cursor = database.rawQuery("SELECT * FROM TIMEZONE",null);
        List<TimeZoneModel> list = new ArrayList<>();
        if(cursor.getCount()!=0){
            cursor.moveToFirst();
            do{
                list.add(new TimeZoneModel(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getInt(3)));
            }while (cursor.moveToNext());
        }
        return list;
    }

    public int updateTimeZone(TimeZoneModel timeZoneModel){
        ContentValues values = new ContentValues();
        values.put("name", timeZoneModel.getName());
        values.put("timezone", timeZoneModel.getTimezone());
        values.put("status", timeZoneModel.getStatus());
        int kq = database.update("TIMEZONE",values,"id=?",new String[]{String.valueOf(timeZoneModel.getId())});
        if(kq<=0){
            return -1;
        }
        return 1;
    }

}
