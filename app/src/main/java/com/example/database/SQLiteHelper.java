package com.example.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteHelper extends SQLiteOpenHelper {
    public static final String SQL_TIME_ZONE = "CREATE TABLE TIMEZONE(id INTEGER primary key AUTOINCREMENT, name TEXT, timezone TEXT);";
    public static final String SQL_TIME_WORLD = "CREATE TABLE TIMEWORLD(id INTEGER primary key AUTOINCREMENT, name TEXT, timezone TEXT);";
    public static final String SQL_ALARM = "CREATE TABLE ALARM(id INTEGER primary key AUTOINCREMENT, label TEXT, time TEXT, repeatdays TEXT,sound TEXT, status INTEGER ,repeat INTEGER);";
    public static final String INSERT_ALARM = "INSERT INTO ALARM (id, label, time, repeatdays, sound, status, repeat) VALUES " +
                    "(1, 'Alarm', '07:00', '', 'Argon', 0, 0), " +
                    "(2, 'Another Alarm', '08:30', '', 'Argon', 0, 0);";
    public static final String INSERT_TIMEWORLD = "INSERT INTO TIMEWORLD (id,name,timezone) VALUES " +
            "(1, 'Hà Nội, Việt Nam', '+7:00'),\n" +
            "(2, 'New York, USA', '-05:00'),\n" +
            "(3, 'Tokyo, Japan', '+9:00'),\n" +
            "(4, 'Bangkok, Thailand', '+7:00')";

    public static final String INSERT_TIME_ZONE = "INSERT INTO TIMEZONE(id,name,timezone) VALUES\n" +
            "(1, 'Hà Nội, Việt Nam', '+7:00'),\n" +
            "(2, 'Accra, Ghana', '00:00'),\n" +
            "(3, 'Acton, America', '-24:00'),\n" +
            "(4, 'Aden, Yemen', '-04:00'),\n" +
            "(5, 'Adelaide, Australia', '+9:30'),\n" +
            "(6, 'Tokyo, Japan', '+9:00'),\n" +
            "(7, 'London, United Kingdom', '00:00'),\n" +
            "(8, 'New York, USA', '-05:00'),\n" +
            "(9, 'Sydney, Australia', '+11:00'),\n" +
            "(10, 'Berlin, Germany', '+01:00'),\n" +
            "(11, 'Paris, France', '+01:00'),\n" +
            "(12, 'Cairo, Egypt', '+02:00'),\n" +
            "(13, 'Auckland, New Zealand', '+13:00'),\n" +
            "(14, 'Moscow, Russia', '+03:00'),\n" +
            "(15, 'Los Angeles, USA', '-08:00'),\n" +
            "(16, 'Beijing, China', '+08:00'),\n" +
            "(17, 'Bangkok, Thailand', '+07:00'),\n" +
            "(18, 'Toronto, Canada', '-05:00'),\n" +
            "(19, 'Mexico City, Mexico', '-06:00'),\n" +
            "(20, 'Johannesburg, South Africa', '+02:00'),\n" +
            "(21, 'Dubai, United Arab Emirates', '+04:00'),\n" +
            "(22, 'Rome, Italy', '+01:00'),\n" +
            "(23, 'Seoul, South Korea', '+09:00'),\n" +
            "(24, 'Singapore', '+08:00'),\n" +
            "(25, 'Istanbul, Turkey', '+03:00'),\n" +
            "(26, 'Mumbai, India', '+05:30'),\n" +
            "(27, 'Rio de Janeiro, Brazil', '-03:00'),\n" +
            "(28, 'Stockholm, Sweden', '+01:00'),\n" +
            "(29, 'Helsinki, Finland', '+02:00'),\n" +
            "(30, 'Vancouver, Canada', '-08:00'),\n" +
            "(31, 'Madrid, Spain', '+01:00'),\n" +
            "(32, 'Oslo, Norway', '+01:00'),\n" +
            "(33, 'Warsaw, Poland', '+01:00'),\n" +
            "(34, 'Vienna, Austria', '+01:00'),\n" +
            "(35, 'Amsterdam, Netherlands', '+01:00'),\n" +
            "(36, 'Brussels, Belgium', '+01:00'),\n" +
            "(37, 'Copenhagen, Denmark', '+01:00'),\n" +
            "(38, 'Athens, Greece', '+02:00'),\n" +
            "(39, 'Prague, Czech Republic', '+01:00'),\n" +
            "(40, 'Budapest, Hungary', '+01:00'),\n" +
            "(41, 'Lisbon, Portugal', '00:00'),\n" +
            "(42, 'Edinburgh, Scotland', '00:00'),\n" +
            "(43, 'Dublin, Ireland', '00:00'),\n" +
            "(44, 'Jerusalem, Israel', '+02:00'),\n" +
            "(45, 'Cape Town, South Africa', '+02:00'),\n" +
            "(46, 'Montreal, Canada', '-05:00'),\n" +
            "(47, 'Helsinki, Finland', '+02:00'),\n" +
            "(48, 'Zurich, Switzerland', '+01:00'),\n" +
            "(49, 'Geneva, Switzerland', '+01:00'),\n" +
            "(50, 'Barcelona, Spain', '+01:00'),\n" +
            "(51, 'Manchester, United Kingdom', '00:00'),\n" +
            "(52, 'Brisbane, Australia', '+10:00'),\n" +
            "(53, 'Perth, Australia', '+08:00'),\n" +
            "(54, 'Melbourne, Australia', '+11:00'),\n" +
            "(55, 'Wellington, New Zealand', '+13:00'),\n" +
            "(56, 'Ottawa, Canada', '-05:00'),\n" +
            "(57, 'Brasília, Brazil', '-03:00'),\n" +
            "(58, 'Lima, Peru', '-05:00'),\n" +
            "(59, 'Bogotá, Colombia', '-05:00'),\n" +
            "(60, 'Caracas, Venezuela', '-04:00'),\n" +
            "(61, 'Santiago, Chile', '-03:00'),\n" +
            "(62, 'Quito, Ecuador', '-05:00'),\n" +
            "(63, 'Guayaquil, Ecuador', '-05:00'),\n" +
            "(64, 'Panama City, Panama', '-05:00'),\n" +
            "(65, 'San Juan, Puerto Rico', '-04:00'),\n" +
            "(66, 'San José, Costa Rica', '-06:00'),\n" +
            "(67, 'San Salvador, El Salvador', '-06:00'),\n" +
            "(68, 'Tegucigalpa, Honduras', '-06:00'),\n" +
            "(69, 'Managua, Nicaragua', '-06:00'),\n" +
            "(70, 'Belmopan, Belize', '-06:00'),\n" +
            "(71, 'Mexico City, Mexico', '-06:00'),\n" +
            "(72, 'Guatemala City, Guatemala', '-06:00'),\n" +
            "(73, 'Tijuana, Mexico', '-08:00'),\n" +
            "(74, 'Monterrey, Mexico', '-06:00'),\n" +
            "(75, 'Guadalajara, Mexico', '-06:00'),\n" +
            "(76, 'Cancún, Mexico', '-05:00'),\n" +
            "(77, 'Acapulco, Mexico', '-06:00'),\n" +
            "(78, 'Puebla, Mexico', '-06:00'),\n" +
            "(79, 'Veracruz, Mexico', '-06:00'),\n" +
            "(80, 'Merida, Mexico', '-05:00'),\n" +
            "(81, 'Havana, Cuba', '-05:00'),\n" +
            "(82, 'Nassau, Bahamas', '-05:00'),\n" +
            "(83, 'Kingston, Jamaica', '-05:00'),\n" +
            "(84, 'Port-au-Prince, Haiti', '-05:00'),\n" +
            "(85, 'Santo Domingo, Dominican Republic', '-04:00'),\n" +
            "(86, 'Port of Spain, Trinidad and Tobago', '-04:00'),\n" +
            "(87, 'Georgetown, Guyana', '-04:00'),\n" +
            "(88, 'Paramaribo, Suriname', '-03:00'),\n" +
            "(89, 'Brasília, Brazil', '-03:00'),\n" +
            "(90, 'Asunción, Paraguay', '-04:00'),\n" +
            "(91, 'Montevideo, Uruguay', '-03:00'),\n" +
            "(92, 'Buenos Aires, Argentina', '-03:00'),\n" +
            "(93, 'São Paulo, Brazil', '-03:00'),\n" +
            "(94, 'Rio de Janeiro, Brazil', '-03:00'),\n" +
            "(95, 'Recife, Brazil', '-03:00'),\n" +
            "(96, 'Fortaleza, Brazil', '-03:00'),\n" +
            "(97, 'Salvador, Brazil', '-03:00'),\n" +
            "(98, 'Manaus, Brazil', '-04:00'),\n" +
            "(99, 'Belém, Brazil', '-03:00'),\n" +
            "(100, 'Porto Alegre, Brazil', '-03:00')";

    public SQLiteHelper(Context context) {
        super(context, "CLOCK.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_TIME_ZONE);
        sqLiteDatabase.execSQL(SQL_TIME_WORLD);
        sqLiteDatabase.execSQL(INSERT_TIME_ZONE);
        sqLiteDatabase.execSQL(SQL_ALARM);
        sqLiteDatabase.execSQL(INSERT_ALARM);
        sqLiteDatabase.execSQL(INSERT_TIMEWORLD);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS TIMEZONE");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS TIMEWORLD");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS ALARM");
    }
}
