package com.example.wechat;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wechat.adapter.MyFragmentPagerAdapter;
import com.example.wechat.bean.Schedule;
import com.example.wechat.bean.ScheduleLab;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private static String[] titleText = new String[]{"MeChat", "事件簿", "发现", "我"};
    private static int[] incons = new int[]{R.drawable.weixin, R.drawable.tongxunlu, R.drawable.faxian, R.drawable.wo};
    private static ArrayList<ImageView> tabImageViews = new ArrayList<>();
    private static ArrayList<TextView> tabTextViews = new ArrayList<>();
    private Fragment[] fragments = FragmentCreater.getFragments();
    private TabLayout tabLayout;
    private MyFragmentPagerAdapter adapter;
    private ViewPager viewPager;



    private ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    private static final int tabSelectedColor = Color.parseColor("#FF4500");
    private static final int tabUnsSelectedColor = Color.parseColor("#000000");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       // getSupportActionBar().setTitle("aaaa");


        //初始化viewPager
        adapter = new MyFragmentPagerAdapter(getSupportFragmentManager(),fragments);
        viewPager = findViewById(R.id.content_viewpager);
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private Handler handler = new Handler();
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                chageTabColor(i,v);
            }

            @Override
            public void onPageSelected(final int i) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        changeTabColor(i);
                    }
                },100);


            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        //初始化底部导航
        initTab();

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.create_schedule:{
                        Schedule schedule = new Schedule(UUID.randomUUID(),"","",new Date());
                        ScheduleLab.get(getApplicationContext()).add(schedule);
                        Intent intent = ScheduleActivity.newIntent(getApplication(), schedule.getUuid());
                        startActivity(intent);
                    }
                    case R.id.menu3:{

                    }
                }
                return false;
            }
        });
    }

    private void initTab() {
        tabLayout = findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < titleText.length; i++) {
            View v = LayoutInflater.from(this).inflate(R.layout.tab_item, null);
            TextView textView = v.findViewById(R.id.tab_text);
            ImageView imageView = v.findViewById(R.id.tab_icon);
            textView.setText(titleText[i]);
            imageView.setImageDrawable(getResources().getDrawable(incons[i]));
            tabImageViews.add(imageView);
            tabTextViews.add(textView);
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(v);

        }
    }

    private void changeTabColor(int index) {
        for (int i = 0; i < tabImageViews.size();i++){
            if(i == index){
                tabTextViews.get(i).setTextColor(tabSelectedColor);
                tabImageViews.get(i).setColorFilter(tabSelectedColor);
            }else {
                tabTextViews.get(i).setTextColor(tabUnsSelectedColor);
                tabImageViews.get(i).setColorFilter(tabUnsSelectedColor);
            }
        }
    }

    private void chageTabColor(int position, float positionOffset){
        ImageView imageViewFrom;
        TextView textViewFrom;
        ImageView imageViewTo;
        TextView textViewTo;
        int unselectedcolor = (int) argbEvaluator.evaluate(positionOffset, tabSelectedColor, tabUnsSelectedColor);
        int selectedcolor = (int) argbEvaluator.evaluate(positionOffset, tabUnsSelectedColor, tabSelectedColor);
        imageViewFrom = tabImageViews.get(position);
        textViewFrom = tabTextViews.get(position);
        Log.d("position111", String.valueOf(position));
        if(position != tabImageViews.size() - 1){
            imageViewTo = tabImageViews.get((position + 1));
            textViewTo = tabTextViews.get((position + 1));
        }else {
            textViewTo = null;
            imageViewTo = null;
        }


        if (imageViewTo != null){
            imageViewTo.setColorFilter(selectedcolor);
        }
        if (textViewTo != null){
            textViewTo.setTextColor(selectedcolor);
        }
        if (imageViewFrom != null){
            imageViewFrom.setColorFilter(unselectedcolor);
        }
        if (textViewFrom != null){
            textViewFrom.setTextColor(unselectedcolor);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       getMenuInflater().inflate(R.menu.option,menu);
        return super.onCreateOptionsMenu(menu);
    }
}

