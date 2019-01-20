package com.example.wechat;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wechat.adapter.ScheduleAdapater;
import com.example.wechat.bean.Schedule;
import com.example.wechat.bean.ScheduleLab;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MailListFragment extends Fragment {
    RecyclerView recyclerView;
    ScheduleAdapater adapater;
    List<Schedule> schedules;
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        recyclerView.setAdapter(adapater);
        recyclerView.setHasFixedSize(true);
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
