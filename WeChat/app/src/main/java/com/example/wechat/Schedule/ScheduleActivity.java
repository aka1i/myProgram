package com.example.wechat.Schedule;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.example.wechat.MainActivity;
import com.example.wechat.R;
import com.example.wechat.ScheduleNotificationService;
import com.example.wechat.bean.Schedule;
import com.example.wechat.bean.ScheduleLab;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class ScheduleActivity extends AppCompatActivity implements OnDateSetListener {
    private Toolbar toolbar;
    private static String EXTRA_SCHEDUL = "EXTRA_SCHEDUL";
    private Schedule schedule;
    private EditText scheduleTitle;
    private EditText scheduleDetail;
    private TextView mScheduleDateTimeReminderTextView;
    private FloatingActionButton floatingActionButton;
    private SwitchCompat mSwitchCompat;
    private Button mSetTimeButton;
    private Date deadLine;

    private boolean hasRemaind;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd HH时mm分");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        init();
    }

    void init(){
        toolbar = findViewById(R.id.schedule_toolbar);
        toolbar.setTitle("事件簿");
        mScheduleDateTimeReminderTextView = findViewById(R.id.scheduleDateTimeReminderTextView);
        mSwitchCompat = findViewById(R.id.reminder_switch);
        mSetTimeButton = findViewById(R.id.set_schedule_time_button);

        setSupportActionBar(toolbar);


        final UUID uuid = (UUID)getIntent().getSerializableExtra(EXTRA_SCHEDUL);
        schedule = ScheduleLab.get(getApplicationContext()).getSchedule(uuid);
        scheduleTitle = findViewById(R.id.schedule_edit_title);
        scheduleDetail = findViewById(R.id.schedule_edit_detail);
        floatingActionButton = findViewById(R.id.fad_change_schedul);

        deadLine = schedule.getDeadLine();



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
                if (hasRemaind){
                    Intent i = new Intent(v.getContext(), ScheduleNotificationService.class);
                    i.putExtra(ScheduleNotificationService.Schedule_TITILE, schedule.getTitle());
                    i.putExtra(ScheduleNotificationService.Schedule_TEXT, schedule.getDetail());
                    i.putExtra(ScheduleNotificationService.Schedule_UUID, schedule.getUuid());
                    createAlarm(i, schedule.getUuid().hashCode(), schedule.getDeadLine().getTime());
                }else {
                    Intent i = new Intent(v.getContext(), ScheduleNotificationService.class);
                    i.putExtra(ScheduleNotificationService.Schedule_TITILE, schedule.getTitle());
                    i.putExtra(ScheduleNotificationService.Schedule_TEXT, schedule.getDetail());
                    i.putExtra(ScheduleNotificationService.Schedule_UUID, schedule.getUuid());
                    deleteAlarm(i, schedule.getUuid().hashCode());
                }
            }
        });


        Intent intent = new Intent(this, ScheduleNotificationService.class);

        mScheduleDateTimeReminderTextView.setText("设置在：" + dateFormat.format(deadLine));

        if (!doesPendingIntentExist(intent,schedule.getUuid().hashCode())){
            mSwitchCompat.setChecked(false);
        }
        else {
            mSwitchCompat.setChecked(true);
        }

        hasRemaind = mSwitchCompat.isChecked();

        mSwitchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                hasRemaind = !hasRemaind;
            }
        });

        mSetTimeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                long tenYears = (long)1000 * 24 * 60 * 60 * 365 * 10;
                TimePickerDialog timePickerDialog = new TimePickerDialog.Builder()
                        .setCallBack((OnDateSetListener)v.getContext())
                        .setCancelStringId("取消")
                        .setSureStringId("确定")
                        .setTitleStringId("选择时间")
                        .setYearText("年")
                        .setMonthText("月")
                        .setDayText("日")
                        .setHourText("小时")
                        .setMinuteText("分钟")
                        .setCyclic(false)
                        .setMinMillseconds(System.currentTimeMillis())
                        .setMaxMillseconds(System.currentTimeMillis() + tenYears)
                        .setCurrentMillseconds(System.currentTimeMillis())
                        .setThemeColor(getResources().getColor(R.color.timepicker_dialog_bg))
                        .setType(Type.ALL)
                        .setWheelItemTextNormalColor(getResources().getColor(R.color.timetimepicker_default_text_color))
                        .setWheelItemTextSelectorColor(getResources().getColor(R.color.timepicker_toolbar_bg))
                        .setWheelItemTextSize(12)
                        .build();

                timePickerDialog.show(getSupportFragmentManager(),"ScheduleDate");




//                Intent i = new Intent(v.getContext(), ScheduleActivity.class);
//                NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//                if (Build.VERSION.SDK_INT >= 26){
//                    String id ="channel_100";//channel的id
//                    String description = "123";//channel的描述信息
//                    int importance = NotificationManager.IMPORTANCE_LOW;//channel的重要性
//                    NotificationChannel channel = new NotificationChannel(id, "123", importance);
//                    channel.enableLights(true);
//                    channel.enableVibration(true);
//
//                    manager.createNotificationChannel(channel);
//
//
//
//
//                    Notification.Builder builder = new Notification.Builder(v.getContext(),id)
//                            .setCategory(Notification.CATEGORY_MESSAGE)
//                            .setSmallIcon(R.drawable.ic_done_white_24dp)
//                            //   .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_done_white_24dp))
//
//                            .setContentIntent(PendingIntent.getActivity(v.getContext(), schedule.getUuid().hashCode(), i, PendingIntent.FLAG_UPDATE_CURRENT))
//                            .setAutoCancel(true)
//                            .setVisibility(Notification.VISIBILITY_PUBLIC);
//                    //.setDeleteIntent(PendingIntent.getService(this, mTodoUUID.hashCode(), deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT))
//
//                    Intent hangIntent = new Intent();
//                    hangIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    hangIntent.setClass(this,MainActivity.class);
//                    PendingIntent hangPendingIntent = PendingIntent.getActivity(this, mTodoUUID.hashCode(), i, PendingIntent.FLAG_CANCEL_CURRENT);
//                    builder.setFullScreenIntent(hangPendingIntent,true);
//                    manager.notify(100, builder.build());
//                }else {
//
//                    Notification.Builder builder = new Notification.Builder(this)
//                            .setContentTitle(mScheduleTitle)
//                            .setContentText(mScheduleText)
//                            .setSmallIcon(R.drawable.ic_done_white_24dp)
//                            //       .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_done_white_24dp))
//                            .setAutoCancel(true)
//                            .setDefaults(Notification.DEFAULT_SOUND)
//                            //       .setDeleteIntent(PendingIntent.getService(this, mTodoUUID.hashCode(), deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT))
//                            .setContentIntent(PendingIntent.getActivity(this, mTodoUUID.hashCode(), i, PendingIntent.FLAG_UPDATE_CURRENT))
//                            .setVisibility(Notification.VISIBILITY_PUBLIC);
//
//                    Intent hangIntent = new Intent();
//                    hangIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                    hangIntent.setClass(this,MainActivity.class);
//                    PendingIntent hangPendingIntent = PendingIntent.getActivity(this, 1, i, PendingIntent.FLAG_CANCEL_CURRENT);
//                    builder.setFullScreenIntent(hangPendingIntent, true);
//                    manager.notify(100, builder.build());
//                }
//
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.delete:
                ScheduleLab.get(getApplicationContext()).deleteCrime(schedule);
                finish();
                return true;

            default:
                return  super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
        schedule.setDeadLine(new Date(millseconds));
        deadLine = schedule.getDeadLine();
        mScheduleDateTimeReminderTextView.setText("设置在：" + dateFormat.format(deadLine));
    }


    private AlarmManager getAlarmManager() {
        return (AlarmManager) getSystemService(ALARM_SERVICE);
    }

    private boolean doesPendingIntentExist(Intent i, int requestCode) {
        PendingIntent pi = PendingIntent.getService(this, requestCode, i, PendingIntent.FLAG_NO_CREATE);
        return pi != null;
    }

    private void createAlarm(Intent i, int requestCode, long timeInMillis) {
        AlarmManager am = getAlarmManager();
        PendingIntent pi = PendingIntent.getService(this, requestCode, i, PendingIntent.FLAG_UPDATE_CURRENT);
        am.set(AlarmManager.RTC_WAKEUP, timeInMillis, pi);
    }

    private void deleteAlarm(Intent i, int requestCode) {
        if (doesPendingIntentExist(i, requestCode)) {
            PendingIntent pi = PendingIntent.getService(this, requestCode, i, PendingIntent.FLAG_NO_CREATE);
            pi.cancel();
            getAlarmManager().cancel(pi);
        }
    }


}
