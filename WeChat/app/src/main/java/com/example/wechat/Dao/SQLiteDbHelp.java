package com.example.wechat.Dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteDbHelp extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "saved_schedule.db";
    private static final int DATABASE_VERSION = 1;




    public SQLiteDbHelp(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ScheduleDbSchema.ScheduleTable.NAME +
                "(" + "_id integer primary key autoincrement, " + ScheduleDbSchema.ScheduleTable.Cols.UUID + ", "+
                ScheduleDbSchema.ScheduleTable.Cols.TITLE + ", " +
                ScheduleDbSchema.ScheduleTable.Cols.DETAIL + ", " +
                ScheduleDbSchema.ScheduleTable.Cols.DATE  + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
