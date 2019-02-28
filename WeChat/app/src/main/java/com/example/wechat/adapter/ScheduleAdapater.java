package com.example.wechat.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wechat.R;
import com.example.wechat.Schedule.ScheduleActivity;
import com.example.wechat.bean.Schedule;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ScheduleAdapater extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Schedule> scheduleList;
    private Context context;
    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    public ScheduleAdapater(Context context, List<Schedule> scheduleList) {
        this.scheduleList = scheduleList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new ScheduleHolder(LayoutInflater.from(context),viewGroup);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        Schedule schedule = scheduleList.get(i);
        ((ScheduleHolder)viewHolder).bind(schedule);
    }

    public void setScheduleList(List<Schedule> scheduleList){
        this.scheduleList = scheduleList;
    }
    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    private class ScheduleHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView scheduleTitle;
        private TextView scheduleDetail;
        private TextView scheduleDeadLine;
        private TextView scheduleRemainTime;
        private Schedule schedule;
        public ScheduleHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.schedule_item,parent,false));
            itemView.setOnClickListener(this);
            scheduleTitle = itemView.findViewById(R.id.schedule_title);
            scheduleDetail = itemView.findViewById(R.id.schedule_detail);
            scheduleDeadLine = itemView.findViewById(R.id.schedule_deadLine);
            scheduleRemainTime = itemView.findViewById(R.id.schedule_remainTime);
        }

        void bind(Schedule schedule){
            Date currentDate = new Date();
            Date deadLine = schedule.getDeadLine();
            long t1 = System.currentTimeMillis();
            long t2 = deadLine.getTime();
            int remainTime =  (int)((t2 - t1) / (60  * 1000));
            this.schedule = schedule;
            scheduleTitle.setText(schedule.getTitle());
            scheduleDetail.setText(schedule.getDetail());
            scheduleDeadLine.setText(dateFormat.format(deadLine));
            if(remainTime < 0)
                scheduleRemainTime.setTextColor(Color.BLACK);
            if (remainTime / 60  >= 24)
                scheduleRemainTime.setText("还剩下" + String.valueOf(remainTime / (24 * 60)) + "天");
            else if (remainTime / 60 > 0 && remainTime / 60 <= 23)
                scheduleRemainTime.setText("还剩下" + String.valueOf(remainTime /  60) + "小时");
            else if (remainTime >= 0 && remainTime < 60)
                scheduleRemainTime.setText("还剩下" + String.valueOf(remainTime) + "分钟");
            else if (remainTime > -60 && remainTime < 0)
                scheduleRemainTime.setText("过去" + String.valueOf(Math.abs(remainTime)) + "分钟");
            else if (remainTime / 60 > -23 && remainTime / 60 < 0)
                scheduleRemainTime.setText("过去" + String.valueOf(Math.abs(remainTime) / 60) + "小时");
            else if (remainTime / 60 <= -24)
                scheduleRemainTime.setText("过去" + String.valueOf(Math.abs(remainTime) / (24 * 60)) + "天");
        }
        @Override
        public void onClick(View v) {
            Intent intent = ScheduleActivity.newIntent(context, schedule.getUuid());
            context.startActivity(intent);
        }
    }
}
