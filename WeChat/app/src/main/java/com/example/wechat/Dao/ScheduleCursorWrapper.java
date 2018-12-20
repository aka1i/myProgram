package com.example.wechat.Dao;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.wechat.Dao.ScheduleDbSchema.ScheduleTable;
import com.example.wechat.bean.Schedule;

import java.util.Date;
import java.util.UUID;

public class ScheduleCursorWrapper extends CursorWrapper {
    public ScheduleCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public Schedule getSchdule(){
        String uuidString = getString(getColumnIndex(ScheduleTable.Cols.UUID));
        String title = getString(getColumnIndex(ScheduleTable.Cols.TITLE));
        long date  = getLong(getColumnIndex(ScheduleTable.Cols.DATE));
        String detail = getString(getColumnIndex(ScheduleTable.Cols.DETAIL));
        Schedule schedule = new Schedule(UUID.fromString(uuidString),title,detail,new Date(date));
        return  schedule;
    }
}
