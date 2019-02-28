package com.example.wechat;


import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.transition.Slide;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.wechat.Note.NoteDustbinActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class FindFragment extends Fragment {

    TextView pengyouquan;
    public FindFragment() {
        // Required empty public constructor
    }

    public static FindFragment newInstance(){
        return new FindFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_find, container, false);
        init(v);
        return v;
    }
    private void init(View v){
        pengyouquan = v.findViewById(R.id.pengyouquan_textview);
        pengyouquan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Slide slide1 = new Slide(Gravity.END);//左边平移
                slide1.setDuration(500);
                Slide slide2 = new Slide(Gravity.START);//右边平移
                slide2.setDuration(500);
                getActivity().getWindow().setReenterTransition(slide1);
                getActivity(). getWindow().setExitTransition(slide2);
                getActivity(). getWindow().setAllowEnterTransitionOverlap(true);
                getActivity(). getWindow().setAllowReturnTransitionOverlap(true);

                Intent intent = new Intent(getContext(), NoteDustbinActivity.class);
                ActivityOptions transitionActivityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity());
                startActivity(intent,transitionActivityOptions.toBundle());
            }
        });
    }
}
