package com.example.wechat.Note;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.transition.Slide;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.wechat.R;
import com.example.wechat.adapter.NoteAdapater;
import com.example.wechat.bean.Note;
import com.example.wechat.bean.NoteLab;

import java.util.List;

public class NoteDustbinActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private List<Note> deletedNotes;
    private NoteAdapater mAdapater;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dustbin);
        init();
    }
    private void init(){
        toolbar = findViewById(R.id.deleted_note_toolbar);
        toolbar.setTitle("笔记垃圾箱");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        Slide slide1 = new Slide();
        slide1.setDuration(500);
        slide1.setSlideEdge(Gravity.END);
        Slide slide2 = new Slide();
        slide2.setDuration(500);
        slide2.setSlideEdge(Gravity.START);
        getWindow().setEnterTransition(slide1);
        getWindow().setReturnTransition(slide2);
        getWindow().getAllowEnterTransitionOverlap();
        getWindow().setAllowReturnTransitionOverlap(true);



        mRecyclerView = findViewById(R.id.deleted_note_list);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        update();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_dustbin,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.clear:
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("是否清空")//设置对话框的标题
                        //设置对话框的按钮
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                clear();
                                update();
                            }
                        }).create();
                dialog.show();


                return true;

            default:
                return  super.onOptionsItemSelected(item);
        }
    }

    void update(){
        deletedNotes = NoteLab.get(getApplicationContext()).getmDeletedNotes();
        if (mAdapater == null) {
            mAdapater = new NoteAdapater(this,deletedNotes);
            mRecyclerView.setAdapter(mAdapater);
        }else {
            mAdapater.setNotes(deletedNotes);
        }
        mAdapater.notifyDataSetChanged();
    }
    private void clear(){
        NoteLab.get(getApplicationContext()).clearAll();
    }
}
