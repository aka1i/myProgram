package com.example.wechat.Utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.wechat.MeChatFragment;
import com.example.wechat.bean.Note;
import com.example.wechat.bean.NoteLab;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OnlineUtils {
    private static Context mContext;
    private static AVObject avObject = null;
    private static int STATE = 0; //0表示上传，1表示下载
    public OnlineUtils(Context context){
        this.mContext = context;
    }
    public static void synchronizeToNet(){
        STATE = 0;
        NoteLab noteLab = NoteLab.get(mContext);
        ArrayList<AVObject> notes = new ArrayList<AVObject> ();
        for (Note note : noteLab.getNotes()){
            queryAndUpdateNotebyUUID(note);
        }
        for (Note note : noteLab.getDeletedNotes()){
            queryAndUpdateNotebyUUID(note);
        }
    }
    public static void synchronizeFromNet(){
        if (AVUser.getCurrentUser() == null)
            return;
        STATE = 1;
        AVQuery<AVObject> query = new AVQuery<>("Note");
        query.whereEqualTo("user",AVUser.getCurrentUser());


        final NoteLab noteLab =  NoteLab.get(mContext);
        noteLab.destoryTable();
        noteLab.clearAll();

        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException avException) {
                for (AVObject avObject : avObjects){
                    Note note = tranAVToNote(avObject);
                    if (note.isDeleted() == 0)
                        noteLab.add(note);
                    else
                        noteLab.addDeleted(note);
                }
                STATE = 0;
                MeChatFragment.updateWithoutData();
            }
        });
    }

    public static void addNote(Note note){
        if (STATE == 1 || AVUser.getCurrentUser() == null)
            return;
        transNoteToAV(note).saveEventually();// 保存到服务端
    }


    public static void deleteNote(Note note){
        if (STATE == 1|| AVUser.getCurrentUser() == null)
            return;
        AVQuery<AVObject> query = new AVQuery<>("Note");
        query.whereEqualTo("uuid",note.getUuid().toString());
        query.whereEqualTo("isdeleted",0);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException avException) {
                if (avObjects.size()!=0){
                    avObjects.get(0).put("isdeleted",1);
                    avObjects.get(0).saveEventually();
                }
            }
        });
        return;
    }
    private static void updateAV(AVObject onlineNote,Note note){
        if (STATE == 1|| AVUser.getCurrentUser() == null)
            return;
        onlineNote.put("title", note.getTitle());
        onlineNote.put("detail", note.getDetail());
        onlineNote.put("date", note.getChangetime());
        onlineNote.put("detailhtmlstring", note.getDetailHtmlString());
        onlineNote.put("isdeleted",note.isDeleted());
        onlineNote.saveEventually();
    }
    public static void queryAndUpdateNotebyUUID(final Note note){
        if (STATE == 1|| AVUser.getCurrentUser() == null)
            return;
        AVQuery<AVObject> query = new AVQuery<>("Note");
        query.whereEqualTo("uuid",note.getUuid().toString());
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException avException) {
                if (avObjects.size()==0)
                    addNote(note);
                else
                    updateAV(avObjects.get(0),note);
            }
        });
        return;
    }

    public static void realDeleteNote(Note note){
        if (STATE == 1|| AVUser.getCurrentUser() == null)
            return;
        AVQuery<AVObject> query = new AVQuery<>("Note");
        query.whereEqualTo("uuid",note.getUuid().toString());
        query.whereEqualTo("isdeleted",1);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException avException) {
                if (avObjects.size()!=0)
                    avObjects.get(0).deleteEventually();
            }
        });
        return;
    }

    public static void cancleDeletedNote(Note note){
        if (STATE == 1|| AVUser.getCurrentUser() == null)
            return;
        AVQuery<AVObject> query = new AVQuery<>("Note");
        query.whereEqualTo("uuid",note.getUuid().toString());
        query.whereEqualTo("isdeleted",1);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> avObjects, AVException avException) {
                if (avObjects.size()!=0)
                    avObjects.get(0).put("isdeleted",0);
                    avObjects.get(0).saveEventually();
            }
        });
        return;
    }
    public static void deleteAllDeletedNote(){
        if (STATE == 1|| AVUser.getCurrentUser() == null)
            return;
        AVQuery<AVObject> query = new AVQuery<>("Note");
        query.whereEqualTo("isdeleted",1);
        query.findInBackground(new FindCallback<AVObject>() {
            @Override
            public void done(List<AVObject> list, AVException e) {
                for (AVObject avObject : list){
                    avObject.deleteEventually();
                }
            }
        });
    }


    private static Note tranAVToNote(AVObject avObject){
        Note note = new Note();
        note.setUuid(UUID.fromString(avObject.getString("uuid")));
        note.setTitle(avObject.getString("title"));
        note.setDetail(avObject.getString("detail"));
        note.setChangetime(avObject.getDate("date"));
        note.setDetailHtmlString(avObject.getString("detailhtmlstring"));
        note.setDeleted(avObject.getInt("isdeleted"));
        return note;
    }
    private static AVObject transNoteToAV(Note note){
        AVObject onlineNote = new AVObject("Note");// 构建对象
        onlineNote.put("uuid",note.getUuid().toString());
        onlineNote.put("title", note.getTitle());
        onlineNote.put("detail", note.getDetail());
        onlineNote.put("date", note.getChangetime());
        onlineNote.put("detailhtmlstring", note.getDetailHtmlString());
        onlineNote.put("isdeleted",note.isDeleted());
        onlineNote.put("user",AVUser.getCurrentUser());
        return onlineNote;
    }


}
