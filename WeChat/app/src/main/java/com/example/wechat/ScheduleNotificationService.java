package com.example.wechat;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.wechat.Schedule.ScheduleActivity;
import com.example.wechat.bean.Schedule;

import java.util.UUID;

public class ScheduleNotificationService extends IntentService {
    public static final String Schedule_TITILE = "com.example.wechat.ScheduleNotificationService_TITLE";
    public static final String Schedule_TEXT = "com.example.wechat.ScheduleNotificationService_TEXT";
    public static final String Schedule_UUID = "com.example.wechat.ScheduleNotificationService_UUID";
    private String mScheduleText;
    private String mScheduleTitle;
    private UUID mTodoUUID;
    private Context mContext;
    public ScheduleNotificationService() {
        super("ScheduleNotificationService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mScheduleText = intent.getStringExtra(Schedule_TEXT);
        mScheduleTitle = intent.getStringExtra(Schedule_TITILE);
        mTodoUUID = (UUID) intent.getSerializableExtra(Schedule_UUID);

        Intent i = new Intent(this, MainActivity.class);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= 26){
            String id ="channel_100";//channel的id
            String description = "123";//channel的描述信息
            int importance = NotificationManager.IMPORTANCE_HIGH;//channel的重要性
            NotificationChannel channel = new NotificationChannel(id, "123", importance);
                                channel.enableLights(true);
                                channel.enableVibration(true);

            manager.createNotificationChannel(channel);




            NotificationCompat.Builder builder = new NotificationCompat.Builder(this,id)
                    .setCategory(Notification.CATEGORY_MESSAGE)
                    .setSmallIcon(R.drawable.ic_done_white_24dp)
                 //   .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_done_white_24dp))
                    .setContentTitle(mScheduleTitle)
                    .setContentText(mScheduleText)
                    .setContentIntent(PendingIntent.getActivity(this, mTodoUUID.hashCode(), i, PendingIntent.FLAG_UPDATE_CURRENT))
                    .setAutoCancel(true)
                    .setVisibility(Notification.VISIBILITY_PUBLIC);
                    //.setDeleteIntent(PendingIntent.getService(this, mTodoUUID.hashCode(), deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT))
//
//            Intent hangIntent = new Intent();
//            hangIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            hangIntent.setClass(this,MainActivity.class);
//            PendingIntent hangPendingIntent = PendingIntent.getActivity(this, mTodoUUID.hashCode(), i, PendingIntent.FLAG_CANCEL_CURRENT);
//            builder.setFullScreenIntent(hangPendingIntent,true);
            manager.notify(100, builder.build());
        }else {

            Notification.Builder builder = new Notification.Builder(this)
                    .setContentTitle(mScheduleTitle)
                    .setContentText(mScheduleText)
                    .setSmallIcon(R.drawable.ic_done_white_24dp)
             //       .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_done_white_24dp))
                    .setAutoCancel(true)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    //       .setDeleteIntent(PendingIntent.getService(this, mTodoUUID.hashCode(), deleteIntent, PendingIntent.FLAG_UPDATE_CURRENT))
                    .setContentIntent(PendingIntent.getActivity(this, mTodoUUID.hashCode(), i, PendingIntent.FLAG_UPDATE_CURRENT))
                    .setVisibility(Notification.VISIBILITY_PUBLIC);

            Intent hangIntent = new Intent();
            hangIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            hangIntent.setClass(this,MainActivity.class);
            PendingIntent hangPendingIntent = PendingIntent.getActivity(this, 1, i, PendingIntent.FLAG_CANCEL_CURRENT);
            builder.setFullScreenIntent(hangPendingIntent, true);
            manager.notify(100, builder.build());
        }

//        i.putExtra(ScheduleNotificationService.TODOUUID, mTodoUUID);
//        Intent deleteIntent = new Intent(this, DeleteNotificationService.class);
//        deleteIntent.putExtra(TODOUUID, mTodoUUID);


    }
}
