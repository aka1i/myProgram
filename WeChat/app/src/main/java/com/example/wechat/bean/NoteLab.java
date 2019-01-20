package com.example.wechat.bean;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.wechat.Dao.NoteCursorWrapper;
import com.example.wechat.Dao.NoteDbHelp;
import com.example.wechat.Dao.NoteDbSchema;
import com.example.wechat.Dao.NoteDbSchema.DeletedNoteTable;
import com.example.wechat.Dao.NoteDbSchema.NoteTable;
import com.example.wechat.Dao.ScheduleDbSchema;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class NoteLab {
    private static NoteLab noteLab;
    private List<Note> notes;
    private List<Note> deletedNotes;
    private SQLiteDatabase database;
    private Context mcontext;
    private NoteComparor mNoteComparor;
    public NoteLab(Context context){
        mNoteComparor = new NoteComparor();
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
        Collections.sort(notes,mNoteComparor);
        return notes;
    }

    public   List<Note> getDeletedNotes() {
        List<Note> notes = new ArrayList<>();
        NoteCursorWrapper noteCursorWrapper = queryDeletedNote(null,null);
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
        Collections.sort(notes,mNoteComparor);
        return notes;
    }

    public Note getNote(UUID uuid){
        NoteCursorWrapper noteCursorWrapper = queryNote(NoteTable.Cols.UUID + " = ?", new String[] {uuid.toString()});
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

    public Note getDeleteNote(UUID uuid){
        NoteCursorWrapper noteCursorWrapper = queryDeletedNote(DeletedNoteTable.Cols.UUID + " = ?", new String[] {uuid.toString()});
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

    /**
     * 未删除note增删改查
     */
    public  void add(Note note){
        ContentValues contentValues = getContentValues(note);
        database.insert(NoteTable.NAME,null,contentValues);
    }

    public void deleteNote(Note note){
        note.setDeleted(1);
        addDeleted(note);
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


    /**
     * 已删除note的增删改查
     */
    public  void addDeleted(Note note){
        ContentValues contentValues = getContentValues(note);
        database.insert(DeletedNoteTable.NAME,null,contentValues);
    }

    public void realDeleteNote(Note note){
        database.delete(DeletedNoteTable.NAME,NoteTable.Cols.UUID + " = ? ",new String[] {note.getUuid().toString()});
    }

    public void cancleDeleted(Note note){
        note.setDeleted(0);
        realDeleteNote(note);
        add(note);
    }

    public void updateDeletedNote(Note note){
        String uuidString = note.getUuid().toString();
        ContentValues values = getContentValues(note);
        database.update(DeletedNoteTable.NAME,values,NoteTable.Cols.UUID + " = ? ", new String[]{uuidString});
    }

    public NoteCursorWrapper queryDeletedNote(String whereClause, String[] whereArgs){
        Cursor cursor = database.query(DeletedNoteTable.NAME,null,whereClause,whereArgs,null,null,null);
        return new NoteCursorWrapper(cursor);
    }

    public void clearAll(){
        database.delete(DeletedNoteTable.NAME,null,null);
    }




    private static ContentValues getContentValues(Note note){
        ContentValues values = new ContentValues();
        values.put(NoteTable.Cols.UUID,note.getUuid().toString());
        values.put(NoteTable.Cols.TITLE,note.getTitle());
        values.put(NoteTable.Cols.DETAIL,note.getDetail());
        values.put(NoteTable.Cols.DATE,note.getChangetime().getTime());
        values.put(NoteTable.Cols.DETAILHEMLSTRING,note.getDetailHtmlString());
        values.put(NoteTable.Cols.ISDELETED,note.isDeleted());
        return values;
    }

    private  class NoteComparor implements Comparator<Note>{
        @Override
        public int compare(Note n1, Note n2) {
            return n1.getChangetime().getTime() < n2.getChangetime().getTime() ? 1 : -1;
        }
    }
}
