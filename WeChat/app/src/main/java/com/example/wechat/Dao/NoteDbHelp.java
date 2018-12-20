package com.example.wechat.Dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.wechat.Dao.NoteDbSchema.NoteTable;

public class NoteDbHelp extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "saved_note.db";
    private static final int DATABASE_VERSION = 1;




    public NoteDbHelp(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + NoteTable.NAME +
                "(" + "_id integer primary key autoincrement, " + NoteTable.Cols.UUID + ", "+
                NoteTable.Cols.TITLE + ", " +
                NoteTable.Cols.DETAIL + ", " +
                NoteTable.Cols.DATE  + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
