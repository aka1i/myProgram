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
import java.util.List;
import java.util.UUID;

public class ScheduleLab {
    private static ScheduleLab scheduleLab;
    private static List<Schedule> schedules;
    private SQLiteDatabase database;
    private Context mcontext;

    public ScheduleLab(Context context){
        mcontext = context.getApplicationContext();
        database = new ScheduleDbHelp(context).getWritableDatabase();
    }
    public static ScheduleLab get(Context context){
        if (scheduleLab == null){
            scheduleLab = new ScheduleLab(context);
        }
        return scheduleLab;
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
        this.schedules = schedules;
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
        database.insert(ScheduleTable.NAME,null,contentValues);
    }

    public void deleteCrime(Schedule schedule){
        database.delete(ScheduleTable.NAME,ScheduleTable.Cols.UUID + " = ? ",new String[] {schedule.getUuid().toString()});
    }

    public void updateCrime(Schedule schedule){
        String uuidString = schedule.getUuid().toString();
        ContentValues values = getContentValues(schedule);
        Log.d("Dasdsa",String.valueOf(schedule.getTitle()));
        database.update(ScheduleTable.NAME,values,ScheduleTable.Cols.UUID + " = ? ", new String[]{uuidString});
    }

    public ScheduleCursorWrapper querySchedule(String whereClause, String[] whereArgs){
        Cursor cursor = database.query(ScheduleTable.NAME,null,whereClause,whereArgs,null,null,null);
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
