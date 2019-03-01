package com.example.wechat.Dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.wechat.bean.Schedule;

public class ScheduleDbHelp extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "saved_schedule.db";
    private static final int DATABASE_VERSION = 1;




    public ScheduleDbHelp(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ScheduleDbSchema.ScheduleTable.NAME +
                "(" + "_id integer primary key autoincrement, " + ScheduleDbSchema.ScheduleTable.Cols.UUID + ", "+
                ScheduleDbSchema.ScheduleTable.Cols.TITLE + ", " +
                ScheduleDbSchema.ScheduleTable.Cols.DETAIL + ", " +
                ScheduleDbSchema.ScheduleTable.Cols.DATE  + "," +
                ScheduleDbSchema.ScheduleTable.Cols.HAS_REMIND  + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
