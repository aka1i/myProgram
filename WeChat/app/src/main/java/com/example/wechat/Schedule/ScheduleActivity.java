package com.example.wechat.Schedule;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
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
import android.widget.Toast;

import com.example.wechat.MainActivity;
import com.example.wechat.R;
import com.example.wechat.ScheduleNotificationService;
import com.example.wechat.Utils.CalendarReminderUtils;
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
    private String TAG = "ScheduleActivity";

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
    private Date oldDeadLine;
    private int hasRemind;
    private int hasSettedRemind;
    private DateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd HH时mm分");

    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.WRITE_CALENDAR
    };

    private static int REQUEST_PERMISSION_CODE = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        init();

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE, REQUEST_PERMISSION_CODE);
            }
        }


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

        hasRemind = schedule.isHasRemind(); //0为无 1 为有
        hasSettedRemind = hasRemind;
        deadLine = schedule.getDeadLine();
        oldDeadLine = schedule.getDeadLine();

        Log.d(TAG, String.valueOf(hasRemind));
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
                Log.d(TAG, "old: " + oldDeadLine.toString());
                Log.d(TAG,"new " + deadLine.toString());
                if (hasRemind == 1){
                    if (hasSettedRemind == 1)
                        CalendarReminderUtils.updateCalendarEvent(getApplicationContext(),schedule.getTitle(),oldDeadLine.getTime(),deadLine.getTime());
                    else
                        CalendarReminderUtils.addCalendarEvent(getApplicationContext(),schedule.getTitle(),schedule.getDetail(),deadLine.getTime(),0);
                }else {
                    CalendarReminderUtils.deleteCalendarEvent(getApplicationContext(),schedule.getTitle(),schedule.getDeadLine().getTime());
                }
                Log.d(TAG,"setmind" + String.valueOf(hasRemind));
                schedule.setHasRemind(hasRemind);
                schedule.setTitle(scheduleTitle.getText().toString());
                schedule.setDetail(scheduleDetail.getText().toString());
                ScheduleLab.get(getApplicationContext()).updateCrime(schedule);
                finish();

            }
        });

        mScheduleDateTimeReminderTextView.setText("设置在：" + dateFormat.format(deadLine));


        if (hasRemind == 0)
            mSwitchCompat.setChecked(false);
        else
            mSwitchCompat.setChecked(true);

        mSwitchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (hasRemind == 0){
                    hasRemind = 1;
                    schedule.setHasRemind(1);
                }
                else{
                    hasRemind = 0;
                    schedule.setHasRemind(0);
                }
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





    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION_CODE ){
            if (permissions[0].equals(Manifest.permission.READ_CALENDAR)
                    &&grantResults[0] == PackageManager.PERMISSION_GRANTED){
           //     Toast.makeText(this, "用户同意使用权限", Toast.LENGTH_SHORT).show();
            }else{
                //用户不同意，向用户展示该权限作用
                Toast.makeText(this, "用户不同意，向用户展示该权限作用", Toast.LENGTH_SHORT).show();
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    AlertDialog dialog = new AlertDialog.Builder(this)
                            .setMessage("该功能需要赋予访问的权限，不开启将无法正常工作！")
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            }).create();
                    dialog.show();
                    return;
                }
                finish();
            }
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.findItem(R.id.chexiao).setVisible(false);
        return super.onPrepareOptionsMenu(menu);
    }
}
