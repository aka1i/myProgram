package com.example.wechat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.wechat.Dao.NoteCursorWrapper;
import com.example.wechat.Dao.NoteDbHelp;
import com.example.wechat.Dao.NoteDbSchema;
import com.example.wechat.Dao.NoteDbSchema.NoteTable;
import com.example.wechat.Dao.ScheduleCursorWrapper;
import com.example.wechat.Dao.ScheduleDbSchema;
import com.example.wechat.bean.Note;
import com.example.wechat.bean.Schedule;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NoteLab {
    private static NoteLab noteLab;
    private List<Note> notes;
    private SQLiteDatabase database;
    private Context mcontext;

    public NoteLab(Context context){
        mcontext = context.getApplicationContext();
        database = new NoteDbHelp(context).getWritableDatabase();
    }
    public static NoteLab get(Context context){
        if (noteLab == null){
            noteLab = new NoteLab(context);
        }
        return noteLab;
    }

    public   List<Note> getNotes() {
        List<Note> notes = new ArrayList<>();
        NoteCursorWrapper noteCursorWrapper = queryNote(null,null);
        try {
            noteCursorWrapper.moveToFirst();
            while (!noteCursorWrapper.isAfterLast()){
                notes.add(noteCursorWrapper.getNote());
                noteCursorWrapper.moveToNext();
            }
        }finally {
            noteCursorWrapper.close();
        }
        this.notes = notes;
        return notes;
    }

    public Note getNote(UUID uuid){
        NoteCursorWrapper noteCursorWrapper = queryNote(ScheduleDbSchema.ScheduleTable.Cols.UUID + " = ?", new String[] {uuid.toString()});
        try{
            if (noteCursorWrapper.getCount() == 0){
                return null;
            }
            noteCursorWrapper.moveToFirst();
            return noteCursorWrapper.getNote();
        }finally {
            noteCursorWrapper.close();
        }
    }

    public  void add(Note note){
        ContentValues contentValues = getContentValues(note);
        database.insert(NoteTable.NAME,null,contentValues);
    }

    public void deleteNote(Note note){
        database.delete(NoteTable.NAME,NoteTable.Cols.UUID + " = ? ",new String[] {note.getUuid().toString()});
    }

    public void updateNote(Note note){
        String uuidString = note.getUuid().toString();
        ContentValues values = getContentValues(note);
        database.update(NoteTable.NAME,values,NoteTable.Cols.UUID + " = ? ", new String[]{uuidString});
    }

    public NoteCursorWrapper queryNote(String whereClause, String[] whereArgs){
        Cursor cursor = database.query(NoteTable.NAME,null,whereClause,whereArgs,null,null,null);
        return new NoteCursorWrapper(cursor);
    }
    private static ContentValues getContentValues(Note note){
        ContentValues values = new ContentValues();
        values.put(NoteTable.Cols.UUID,note.getUuid().toString());
        values.put(NoteTable.Cols.TITLE,note.getTitle());
        values.put(NoteTable.Cols.DETAIL,note.getDetail());
        values.put(NoteTable.Cols.DATE,note.getChangetime().getTime());
        return values;
    }
}
