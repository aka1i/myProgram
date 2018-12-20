package com.example.wechat;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

import com.example.wechat.bean.Schedule;

import java.util.UUID;

public class ScheduleActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private static String EXTRA_SCHEDUL = "EXTRA_SCHEDUL";
    private Schedule schedule;
    private EditText scheduleTitle;
    private EditText scheduleDetail;
    private FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        init();
    }

    void init(){
        toolbar = findViewById(R.id.schedule_toolbar);
        toolbar.setTitle("事件簿");
        setSupportActionBar(toolbar);

        UUID uuid = (UUID)getIntent().getSerializableExtra(EXTRA_SCHEDUL);
        schedule = ScheduleLab.get(getApplicationContext()).getSchedule(uuid);
        scheduleTitle = findViewById(R.id.schedule_edit_title);
        scheduleDetail = findViewById(R.id.schedule_edit_detail);
        floatingActionButton = findViewById(R.id.fad_change_schedul);

        String title = schedule.getTitle();
        String detail = schedule.getDetail();
        if (title != null)
            scheduleTitle.setText(title);
        if (detail != null)
            scheduleDetail.setText(detail);
        scheduleTitle.setSelection(scheduleTitle.length());
        scheduleDetail.setSelection(scheduleDetail.length());

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                schedule.setTitle(scheduleTitle.getText().toString());
                schedule.setDetail(scheduleDetail.getText().toString());
                ScheduleLab.get(getApplicationContext()).updateCrime(schedule);
                finish();
            }
        });
    }
     public static Intent newIntent(Context packageContext, UUID uuid){
         Intent intent = new Intent(packageContext,ScheduleActivity.class);

         intent.putExtra(EXTRA_SCHEDUL, uuid);
        return intent;
     }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.note_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
}
