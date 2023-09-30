package com.example.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.model.AlarmModel;
import com.example.model.TimeZoneModel;

import java.util.ArrayList;
import java.util.List;

public class AlarmDAO {
    private Context context;
    private SQLiteDatabase database;
    private SQLiteHelper helper;

    public AlarmDAO(Context context) {
        this.context = context;
        helper = new SQLiteHelper(context);
        database = helper.getWritableDatabase();
    }

    public int insertAlarm(AlarmModel alarmModel){
        ContentValues values = new ContentValues();
        values.put("label", alarmModel.getLabel());
        values.put("time", alarmModel.getTime());
        values.put("repeatdays", alarmModel.getRepeatDays());
        values.put("sound", alarmModel.getSound());
        values.put("status", alarmModel.getStatus());
        values.put("repeat", alarmModel.getRepeat());
        long kq = database.insert("ALARM",null,values);
        if(kq<=0){
            return -1;
        }
        return 1;
    }

    public List<AlarmModel> getAllAlarm(){
        Cursor cursor = database.rawQuery("SELECT * FROM ALARM",null);
        List<AlarmModel> list = new ArrayList<>();
        if(cursor.getCount()!=0){
            cursor.moveToFirst();
            do{
                list.add(new AlarmModel(cursor.getInt(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4),cursor.getInt(5),cursor.getInt(6)));
            }while (cursor.moveToNext());
        }
        return list;
    }

    public int updateAlarm(AlarmModel alarmModel){
        ContentValues values = new ContentValues();
        values.put("label", alarmModel.getLabel());
        values.put("time", alarmModel.getTime());
        values.put("repeatdays", alarmModel.getRepeatDays());
        values.put("sound", alarmModel.getSound());
        values.put("status", alarmModel.getStatus());
        values.put("repeat", alarmModel.getRepeat());
        int kq = database.update("ALARM",values,"id=?",new String[]{String.valueOf(alarmModel.getId())});
        if(kq<=0){
            return -1;
        }
        return 1;
    }

    public int delete(int id){
        int kq = database.delete("ALARM","id=?",new String[]{String.valueOf(id)});
        if(kq<=0){
            return -1;
        }
        return 1;
    }

}
