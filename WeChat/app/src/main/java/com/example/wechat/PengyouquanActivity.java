package com.example.wechat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Slide;
import android.view.Gravity;

public class PengyouquanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengyouquan);
        init();
    }
    private void init(){
        Slide slide1 = new Slide();
        slide1.setDuration(500);
        slide1.setSlideEdge(Gravity.END);
        Slide slide2 = new Slide();
        slide2.setDuration(500);
        slide2.setSlideEdge(Gravity.START);
        getWindow().setEnterTransition(slide1);
        getWindow().setReturnTransition(slide2);
        getWindow().getAllowEnterTransitionOverlap();
        getWindow().setAllowReturnTransitionOverlap(true);
    }
}
