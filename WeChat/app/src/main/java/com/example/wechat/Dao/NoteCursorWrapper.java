package com.example.wechat.Dao;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.wechat.Dao.NoteDbSchema.NoteTable;
import com.example.wechat.bean.Note;

import java.util.Date;
import java.util.UUID;

public class NoteCursorWrapper extends CursorWrapper {
    public NoteCursorWrapper(Cursor cursor){
        super(cursor);
    }

    public Note getNote(){
        String uuidString = getString(getColumnIndex(NoteTable.Cols.UUID));
        String title = getString(getColumnIndex(NoteTable.Cols.TITLE));
        long date  = getLong(getColumnIndex(NoteTable.Cols.DATE));
        String detail = getString(getColumnIndex(NoteTable.Cols.DETAIL));
        String detailHtmlString = getString(getColumnIndex(NoteTable.Cols.DETAILHEMLSTRING));
        int deleted = getInt(getColumnIndex(NoteTable.Cols.ISDELETED));
        Note note= new Note(UUID.fromString(uuidString),title,detail,new Date(date),detailHtmlString, deleted);
        return  note;
    }
}
