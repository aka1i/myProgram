package com.example.wechat;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.example.wechat.bean.Note;
import com.example.wechat.bean.Schedule;

import java.util.Date;
import java.util.UUID;

public class DiaryEditorActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    private EditText noteTitle;
    private EditText noteDetail;
    private static String EXTRA_NOTE = "EXTRA_NOTE";
    private Note note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_editor);
        init();
    }
    private void init(){
        toolbar = findViewById(R.id.note_toolbar);
        toolbar.setTitle("笔记");
        setSupportActionBar(toolbar);


        UUID uuid = (UUID)getIntent().getSerializableExtra(EXTRA_NOTE);
        note = NoteLab.get(getApplicationContext()).getNote(uuid);
        noteTitle = findViewById(R.id.note_edit_title);
        noteDetail = findViewById(R.id.note_edit_detail);
        floatingActionButton = findViewById(R.id.fad_change_schedul);

        String title = note.getTitle();
        String detail = note.getDetail();
        if (title != null)
            noteTitle.setText(title);
        if (detail != null)
            noteDetail.setText(detail);
        noteTitle.setSelection(noteTitle.length());
        noteDetail.setSelection(noteDetail.length());


        floatingActionButton = findViewById(R.id.fad_finisheditor);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note.setTitle(noteTitle.getText().toString());
                note.setDetail(noteDetail.getText().toString());
                NoteLab.get(getApplicationContext()).updateNote(note);
                finish();
            }
        });
    }
    public static Intent newIntent(Context packageContext, UUID uuid){
        Intent intent = new Intent(packageContext,DiaryEditorActivity.class);
        intent.putExtra(EXTRA_NOTE, uuid);
        return intent;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete:

                return true;

            default:
                return  super.onOptionsItemSelected(item);
        }
    }
}
