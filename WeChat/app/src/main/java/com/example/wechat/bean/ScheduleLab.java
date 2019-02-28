package com.example.wechat.bean;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.wechat.Dao.ScheduleDbHelp;
import com.example.wechat.Dao.ScheduleCursorWrapper;
import com.example.wechat.Dao.ScheduleDbSchema.ScheduleTable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class ScheduleLab {
    private static ScheduleLab sScheduleLab;
    private static List<Schedule> sSchedules;
    private SQLiteDatabase mDatabase;
    private Context mContext;

    public ScheduleLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new ScheduleDbHelp(context).getWritableDatabase();
    }
    public static ScheduleLab get(Context context){
        if (sScheduleLab == null){
            sScheduleLab = new ScheduleLab(context);
        }
        return sScheduleLab;
    }

    public   List<Schedule> getSchedules() {
        List<Schedule> schedules = new ArrayList<>();
        ScheduleCursorWrapper scheduleCursorWrapper = querySchedule(null,null);
        try {
            scheduleCursorWrapper.moveToFirst();
            while (!scheduleCursorWrapper.isAfterLast()){
                schedules.add(scheduleCursorWrapper.getSchdule());
                scheduleCursorWrapper.moveToNext();
            }
        }finally {
            scheduleCursorWrapper.close();
        }
        this.sSchedules = schedules;
        Collections.sort(schedules,new Comparator<Schedule>() {
            @Override
            public int compare(Schedule o1, Schedule o2) {
                return (int)(o2.getDeadLine().getTime() / 1000 - o1.getDeadLine().getTime() / 1000);
            }
        });
        return schedules;
    }

    public Schedule getSchedule(UUID uuid){
        ScheduleCursorWrapper crimeCursorWrapper = querySchedule(ScheduleTable.Cols.UUID + " = ?", new String[] {uuid.toString()});
        try{
            if (crimeCursorWrapper.getCount() == 0){
                return null;
            }
            crimeCursorWrapper.moveToFirst();
            return crimeCursorWrapper.getSchdule();
        }finally {
            crimeCursorWrapper.close();
        }
    }

    public  void add(Schedule schedule){
        ContentValues contentValues = getContentValues(schedule);
        mDatabase.insert(ScheduleTable.NAME,null,contentValues);
    }

    public void deleteCrime(Schedule schedule){
        mDatabase.delete(ScheduleTable.NAME,ScheduleTable.Cols.UUID + " = ? ",new String[] {schedule.getUuid().toString()});
    }

    public void updateCrime(Schedule schedule){
        String uuidString = schedule.getUuid().toString();
        ContentValues values = getContentValues(schedule);
        Log.d("Dasdsa",String.valueOf(schedule.getTitle()));
        mDatabase.update(ScheduleTable.NAME,values,ScheduleTable.Cols.UUID + " = ? ", new String[]{uuidString});
    }

    public ScheduleCursorWrapper querySchedule(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(ScheduleTable.NAME,null,whereClause,whereArgs,null,null,null);
        return new ScheduleCursorWrapper(cursor);
    }
    private static ContentValues getContentValues(Schedule schedule){
        ContentValues values = new ContentValues();
        values.put(ScheduleTable.Cols.UUID,schedule.getUuid().toString());
        values.put(ScheduleTable.Cols.TITLE,schedule.getTitle());
        values.put(ScheduleTable.Cols.DETAIL,schedule.getDetail());
        values.put(ScheduleTable.Cols.DATE,schedule.getDeadLine().getTime());
        return values;
    }
}
