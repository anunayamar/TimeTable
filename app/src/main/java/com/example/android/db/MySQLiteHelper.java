package com.example.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Anunay on 5/1/16.
 */
public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String TABLE_TASK = "TASK";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DAY = "day";
    public static final String COLUMN_TASK = "task";
    public static final String COLUMN_TASK_DESCRIPTION = "taskDescription";
    public static final String COLUMN_TASK_TIME = "taskTime";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_TASK_ADDRESS = "taskAddress";

    private static final String DATABASE_NAME = "timetable.db";
    private static final int DATABASE_VERSION = 6;

    // Database creation sql statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_TASK + "(" + COLUMN_ID
            + " integer primary key autoincrement, " + COLUMN_DAY
            + " text not null," + COLUMN_TASK
            + " text not null," + COLUMN_TASK_DESCRIPTION
            + " text not null," + COLUMN_TASK_TIME
            + " text not null," + COLUMN_LATITUDE
            + " real not null," + COLUMN_LONGITUDE
            + " real not null," + COLUMN_TASK_ADDRESS
            + " text not null);";

    public MySQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASK);
        onCreate(db);
    }


}
