package com.example.wechat.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wechat.DiaryEditorActivity;
import com.example.wechat.R;
import com.example.wechat.bean.Note;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class NoteAdapater extends RecyclerView.Adapter {
    private List<Note> notes;
    private Context context;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public NoteAdapater(Context context, List<Note> notes) {
        this.notes = notes;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new NoteHolder(LayoutInflater.from(context),viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Note note = notes.get(i);
        ((NoteHolder)viewHolder).bind(note);
    }

    public void setNotes(List<Note> notes){
        this.notes = notes;
    }
    @Override
    public int getItemCount() {
        return notes.size();
    }

    private class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView noteTitle;
        private TextView noteDetail;
        private TextView noteChangeTime;
        private Note note;
        public NoteHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.note_item,parent,false));
            itemView.setOnClickListener(this);
            noteTitle = itemView.findViewById(R.id.note_title);
            noteDetail = itemView.findViewById(R.id.note_detail);
            noteChangeTime = itemView.findViewById(R.id.note_changetime);
        }

        void bind(Note note){
            Date changeTime = note.getChangetime();
            this.note = note;
            noteTitle.setText(note.getTitle());
            String detail = note.getDetail();
            if (detail.length() > 50)
                detail = detail.substring(0,50) + "。。。";
            noteDetail.setText(detail);
            noteChangeTime.setText("最后修改于：" + dateFormat.format(changeTime));
        }
        @Override
        public void onClick(View v) {
            Intent intent = new DiaryEditorActivity().newIntent(context,note.getUuid(),note.isDeleted());
            context.startActivity(intent);
        }
    }
}
