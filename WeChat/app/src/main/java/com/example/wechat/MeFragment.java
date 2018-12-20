package com.example.wechat;


import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment {
    private static final int UPDATE_TIMERTASK = 0;
    private static int[] acs = new int[]{R.drawable.ac1,R.drawable.ac2,R.drawable.ac3,R.drawable.ac4};
    int clickCount = 0;
    CircleImageView circleImageView;

    Timer timer;
    TimerTask timerTask;
    Handler mHandler;
    public MeFragment() {
        // Required empty public constructor
    }

    public static MeFragment newInstance(){
        return  new MeFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_me, container, false);
        circleImageView = v.findViewById(R.id.ac1);
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch(msg.what){
                    case UPDATE_TIMERTASK : rotation();
                }
            }
        };
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickCount <= 30)
                clickCount++;
                stopTask();
                startTask();
                switch (clickCount){
                    case 1:Toast.makeText(getContext(),"滚啊舔狗！",Toast.LENGTH_SHORT).show();break;
                    case 10:Toast.makeText(getContext(),"嗯♂？",Toast.LENGTH_SHORT).show();rotation();break;
                    case 20:Toast.makeText(getContext(),"给老子搞快点",Toast.LENGTH_SHORT).show();rotation();break;
                    case 30:Toast.makeText(getContext(),"我已经飙到极限辣！",Toast.LENGTH_SHORT).show();rotation();break;
                }
            }
        });


        return v;
    }

    private void rotation(){
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(circleImageView,"rotationX",0,90);
        ObjectAnimator animator2 = ObjectAnimator.ofFloat(circleImageView,"rotationX",-90,0);
        animator1.setDuration(1000).start();
        animator1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                int itemPosition;
                itemPosition = clickCount / 10;
                circleImageView.setImageDrawable(ContextCompat.getDrawable(getContext(),acs[itemPosition]));
                ObjectAnimator animator2 = ObjectAnimator.ofFloat(circleImageView,"rotationX",-90,0);
                animator2.setDuration(1000).start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    private void startTask(){
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                clickCount -= 10;
                if (clickCount < 0) {
                    clickCount = 0;
                    cancel();
                } else {
                    mHandler.sendEmptyMessage(UPDATE_TIMERTASK);
                }
            }
        };
        timer.schedule(timerTask,5000,5000);
    }
    private void stopTask(){
        if (timer != null){
            timer.cancel();
            timer = null;
        }
        if (timerTask != null){
            timerTask.cancel();
            timer = null;
        }
    }
}
