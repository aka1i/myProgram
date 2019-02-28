package com.example.wechat.bean;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.wechat.Dao.NoteCursorWrapper;
import com.example.wechat.Dao.NoteDbHelp;
import com.example.wechat.Dao.NoteDbSchema.DeletedNoteTable;
import com.example.wechat.Dao.NoteDbSchema.NoteTable;
import com.example.wechat.Utils.OnlineUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class NoteLab {
    private static NoteLab sNoteLab;
    private List<Note> mNotes;
    private List<Note> mDeletedNotes;
    private SQLiteDatabase mDatabase;
    private Context mContext;
    public static Comparator<Note> mNoteComparor = new LowerNoteComparorBychangetime();
    final public static int LOWER_NOTECOMPAROR_BY_CHANGETIME = 0;
    final public static int UPPERR_NOTECOMPAROR_BY_CHANGETIME = 1;
    final public static int LOWER__NOTECOMPAROR_BY_TEXTLENTH = 2;
    final public static int UPPER__NOTECOMPAROR_BY_TEXTLENTH = 3;
    public static int COMFLAG = 0;
    public NoteLab(Context context){
        mContext = context.getApplicationContext();
        mDatabase = new NoteDbHelp(context).getWritableDatabase();
    }
    public static NoteLab get(Context context){
        if (sNoteLab == null){
            sNoteLab = new NoteLab(context);
        }
        return sNoteLab;
    }

    public   List<Note> getmNotes() {
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
        this.mNotes = notes;
        Collections.sort(notes,mNoteComparor);
        return notes;
    }

    public   List<Note> getmDeletedNotes() {
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
        this.mNotes = notes;
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
        mDatabase.insert(NoteTable.NAME,null,contentValues);
        OnlineUtils.addNote(note);
    }

    public void deleteNote(Note note){
        note.setDeleted(1);
        addDeleted(note);
        mDatabase.delete(NoteTable.NAME,NoteTable.Cols.UUID + " = ? ",new String[] {note.getUuid().toString()});
        OnlineUtils.deleteNote(note);
    }

    public void updateNote(Note note){
        String uuidString = note.getUuid().toString();
        ContentValues values = getContentValues(note);
        mDatabase.update(NoteTable.NAME,values,NoteTable.Cols.UUID + " = ? ", new String[]{uuidString});
        OnlineUtils.queryAndUpdateNotebyUUID(note);
    }

    public NoteCursorWrapper queryNote(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(NoteTable.NAME,null,whereClause,whereArgs,null,null,null);
        return new NoteCursorWrapper(cursor);
    }
    public void destoryTable(){
        mDatabase.delete(NoteTable.NAME,null,null);
        mDatabase.delete(DeletedNoteTable.NAME,null,null);
    }

    /**
     * 已删除note的增删改查
     */
    public  void addDeleted(Note note){
        ContentValues contentValues = getContentValues(note);
        mDatabase.insert(DeletedNoteTable.NAME,null,contentValues);
    }

    public void realDeleteNote(Note note){
        mDatabase.delete(DeletedNoteTable.NAME,NoteTable.Cols.UUID + " = ? ",new String[] {note.getUuid().toString()});
        OnlineUtils.realDeleteNote(note);
    }

    public void cancleDeleted(Note note){
        note.setDeleted(0);
        realDeleteNote(note);
        add(note);
        OnlineUtils.cancleDeletedNote(note);
    }

    public void updateDeletedNote(Note note){
        String uuidString = note.getUuid().toString();
        ContentValues values = getContentValues(note);
        mDatabase.update(DeletedNoteTable.NAME,values,NoteTable.Cols.UUID + " = ? ", new String[]{uuidString});
        OnlineUtils.queryAndUpdateNotebyUUID(note);
    }

    public NoteCursorWrapper queryDeletedNote(String whereClause, String[] whereArgs){
        Cursor cursor = mDatabase.query(DeletedNoteTable.NAME,null,whereClause,whereArgs,null,null,null);
        return new NoteCursorWrapper(cursor);
    }

    public void clearAll(){
        mDatabase.delete(DeletedNoteTable.NAME,null,null);
        OnlineUtils.deleteAllDeletedNote();
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

    public static void setComparator(int flag){
        switch (flag){
            case LOWER_NOTECOMPAROR_BY_CHANGETIME:{
                mNoteComparor = new LowerNoteComparorBychangetime();
                break;
            }
            case UPPERR_NOTECOMPAROR_BY_CHANGETIME:{
                mNoteComparor = new UpperNoteComparorBychangetime();
                break;
            }
            case LOWER__NOTECOMPAROR_BY_TEXTLENTH:{
                mNoteComparor = new LowerNoteComparorByTextlenth();
                break;
            }
            case UPPER__NOTECOMPAROR_BY_TEXTLENTH:{
                mNoteComparor = new UpperNoteComparorByTextlenth();
                break;
            }
        }

    }

    public  static class LowerNoteComparorBychangetime implements Comparator<Note>{
        @Override
        public int compare(Note n1, Note n2) {
            return n1.getChangetime().getTime() < n2.getChangetime().getTime() ? 1 : -1;
        }
    }
    private static class UpperNoteComparorBychangetime implements Comparator<Note>{
        @Override
        public int compare(Note n1, Note n2) {
            return n1.getChangetime().getTime() > n2.getChangetime().getTime() ? 1 : -1;
        }
    }
    private  static class LowerNoteComparorByTextlenth implements Comparator<Note>{
        @Override
        public int compare(Note n1, Note n2) {
            return n1.getDetail().length() > n2.getDetail().length() ? 1 : -1;
        }
    }
    private  static class UpperNoteComparorByTextlenth implements Comparator<Note>{
        @Override
        public int compare(Note n1, Note n2) {
            return n1.getDetail().length() < n2.getDetail().length() ? 1 : -1;
        }
    }
}
