package com.example.wechat;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.wechat.RichText.RichEditText;
import com.example.wechat.Utils.RichTextUtils;
import com.example.wechat.bean.Note;
import com.example.wechat.bean.NoteLab;

import java.util.Date;
import java.util.UUID;

public class DiaryEditorActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    private EditText noteTitle;
    private RichEditText noteDetail;
    private ImageButton mBoldButton;
    private ImageButton mItalicButton;
    private ImageButton mStrikethroughButton;
    private ImageButton mUnderlineButton;
    private static String EXTRA_NOTE = "EXTRA_NOTE";
    private static String ISDELETED = "isdeleted";
    private Note note;
    private int isdeleted;
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

        mBoldButton = findViewById(R.id.bold);
        mBoldButton.setOnClickListener(this);
        mItalicButton = findViewById(R.id.italic);
        mItalicButton.setOnClickListener(this);
        mStrikethroughButton = findViewById(R.id.strikethrough);
        mStrikethroughButton.setOnClickListener(this);
        mUnderlineButton = findViewById(R.id.underline);
        mUnderlineButton.setOnClickListener(this);

        UUID uuid = (UUID)getIntent().getSerializableExtra(EXTRA_NOTE);
        isdeleted = getIntent().getIntExtra(ISDELETED, 0);
        if (isdeleted == 0)
            note = NoteLab.get(getApplicationContext()).getNote(uuid);
        else
            note = NoteLab.get(getApplicationContext()).getDeleteNote(uuid);
        noteTitle = findViewById(R.id.note_edit_title);
        noteDetail = findViewById(R.id.note_edit_detail);
        floatingActionButton = findViewById(R.id.fad_change_schedul);


        String title = note.getTitle();
        String detail = note.getDetailHtmlString();

        noteTitle.setText(title);
        noteDetail.setText(RichTextUtils.convertRichTextToSpanned(detail));
        noteTitle.setSelection(noteTitle.length());
        noteDetail.setSelection(noteDetail.length());


        floatingActionButton = findViewById(R.id.fad_finisheditor);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                note.setTitle(noteTitle.getText().toString());
                note.setDetail(noteDetail.getText().toString());
                note.setChangetime(new Date());
                note.setDetailHtmlString(RichTextUtils.convertSpannedToRichText(noteDetail.getText()));
                NoteLab.get(getApplicationContext()).updateNote(note);
                finish();
            }
        });
    }
    public static Intent newIntent(Context packageContext, UUID uuid,int isdeleted){
        Intent intent = new Intent(packageContext,DiaryEditorActivity.class);
        intent.putExtra(EXTRA_NOTE, uuid);
        intent.putExtra(ISDELETED,isdeleted);
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
                if (isdeleted == 0){
                    AlertDialog dialog = new AlertDialog.Builder(this)
                            .setTitle("是否移入回收站")//设置对话框的标题
                            //设置对话框的按钮
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    NoteLab.get(getApplicationContext()).deleteNote(note);
                                    finish();
                                }
                            }).create();
                    dialog.show();
                }
                else{
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("是否永久删除（无法恢复）")//设置对话框的标题
                        //设置对话框的按钮
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                NoteLab.get(getApplicationContext()).realDeleteNote(note);
                                finish();
                            }
                        }).create();
                dialog.show();
                }
                return true;
            case R.id.chexiao:
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("是否恢复该笔记")//设置对话框的标题
                        //设置对话框的按钮
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                NoteLab.get(getApplicationContext()).cancleDeleted(note);
                                finish();
                            }
                        }).create();
                dialog.show();
                return true;
            default:
                return  super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.bold: {
                noteDetail.applyBoldEffect(Boolean.TRUE);
                break;
            }
            case R.id.italic:{
                noteDetail.applyItalicEffect(Boolean.TRUE);
                break;
            }
            case R.id.strikethrough:{
                noteDetail.applyStrikethroughEffect(Boolean.TRUE);
                break;
            }
            case R.id.underline:{
                noteDetail.applyUnderlineEffect(Boolean.TRUE);
                break;
            }
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (isdeleted == 0)
            menu.findItem(R.id.chexiao).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }
}


