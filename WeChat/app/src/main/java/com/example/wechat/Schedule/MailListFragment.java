package com.example.wechat.Schedule;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.FrameLayout;

import com.example.wechat.R;
import com.example.wechat.Utils.CustomRecyclerScrollViewListener;
import com.example.wechat.adapter.ScheduleAdapater;
import com.example.wechat.bean.Schedule;
import com.example.wechat.bean.ScheduleLab;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;

import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 */
public class MailListFragment extends Fragment {
    private RecyclerView recyclerView;
    private FloatingActionButton mFloatingActionButton;
    private Button mTimeButton;
    private ScheduleAdapater adapater;
    private List<Schedule> schedules;
    public MailListFragment() {
        // Required empty public constructor
    }

    public static MailListFragment newInstance(){
        return new MailListFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_mail_list, container, false);
        init(v);
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        update();
    }

    void init(View v){
        recyclerView =  v.findViewById(R.id.schedule_list);
        mFloatingActionButton = v.findViewById(R.id.fad_create_schedule);

        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new CustomRecyclerScrollViewListener() {
            @Override
            public void show() {
                mFloatingActionButton.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }

            @Override
            public void hide() {

                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mFloatingActionButton.getLayoutParams();
                int fabMargin = lp.bottomMargin;
                mFloatingActionButton.animate().translationY(mFloatingActionButton.getHeight() + fabMargin).setInterpolator(new AccelerateInterpolator(2.0f)).start();
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setAdapter(adapater);
        recyclerView.setHasFixedSize(true);

        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Schedule schedule = new Schedule(UUID.randomUUID(),"","",new Date());
                ScheduleLab.get(getContext()).add(schedule);
                Intent intent = ScheduleActivity.newIntent(getContext(), schedule.getUuid());
                startActivity(intent);
            }
        });



    }

    void update(){
        schedules = ScheduleLab.get(getContext()).getSchedules();

        if (adapater == null) {
            adapater = new ScheduleAdapater(getContext(),schedules);
            recyclerView.setAdapter(adapater);
        }else {
            adapater.setScheduleList(schedules);
            adapater.notifyDataSetChanged();
        }
        adapater.notifyDataSetChanged();
    }
}
