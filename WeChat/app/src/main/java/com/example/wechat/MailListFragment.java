package com.example.wechat;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wechat.adapter.ScheduleAdapater;
import com.example.wechat.bean.Schedule;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MailListFragment extends Fragment {
    RecyclerView recyclerView;
    ScheduleAdapater adapater;
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
        ScheduleLab scheduleLab = ScheduleLab.get(getContext());
        List<Schedule> schedules = scheduleLab.getSchedules();
        adapater = new ScheduleAdapater(getActivity(),schedules);
        recyclerView =  v.findViewById(R.id.schedule_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setAdapter(adapater);
        recyclerView.setHasFixedSize(true);
    }

    void update(){
        ScheduleLab scheduleLab = ScheduleLab.get(getActivity());
        List<Schedule> schedules = scheduleLab.getSchedules();

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
