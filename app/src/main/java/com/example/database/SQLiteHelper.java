package com.example.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteHelper extends SQLiteOpenHelper {
    public static final String SQL_TIME_ZONE = "CREATE TABLE TIMEZONE(id INTEGER primary key AUTOINCREMENT, name TEXT, timezone TEXT, status INTEGER);";
    public static final String SQL_ALARM = "CREATE TABLE ALARM(id INTEGER primary key AUTOINCREMENT, label TEXT, time TEXT, repeatdays TEXT,sound TEXT, status INTEGER ,repeat INTEGER);";
    public static final String INSERT_TIME_ZONE ="INSERT INTO TIMEZONE(id,name,timezone,status) VALUES\n" +
            "(1,'Hà Nội, Việt Nam','+7:00',0),\n" +
            "(2,'Accra, Ghana','00:00',0),\n" +
            "(3,'Acton, America','-24:00',0),\n" +
            "(4,'Aden, Yemen','-04:00',0),\n" +
            "(5,'Adelaide, Australia','+9:30',0)";
    public SQLiteHelper(Context context) {
        super(context,"CLOCK.db",null,1);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_TIME_ZONE);
        sqLiteDatabase.execSQL(INSERT_TIME_ZONE);
        sqLiteDatabase.execSQL(SQL_ALARM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS TIMEZONE");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS ALARM");
    }
}
