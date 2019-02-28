package com.example.wechat;

import android.animation.ArgbEvaluator;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wechat.Note.MeChatFragment;
import com.example.wechat.Utils.OnlineUtils;
import com.example.wechat.adapter.MyFragmentPagerAdapter;
import com.example.wechat.bean.NoteLab;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static String[] titleText = new String[]{"MeChat", "事件簿", "发现", "我"};
    private static int[] incons = new int[]{R.drawable.weixin, R.drawable.tongxunlu, R.drawable.faxian, R.drawable.wo};
    private ArrayList<ImageView> tabImageViews = new ArrayList<>();
    private ArrayList<TextView> tabTextViews = new ArrayList<>();
    private Fragment[] fragments = FragmentCreater.getFragments();
    private TabLayout tabLayout;
    private MyFragmentPagerAdapter adapter;
    private ViewPager viewPager;


    private CountDownTimer countDownTimer;
    private ArgbEvaluator argbEvaluator = new ArgbEvaluator();
    private static final int tabSelectedColor = Color.parseColor("#FF4500");
    private static final int tabUnsSelectedColor = Color.parseColor("#000000");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        setContentView(R.layout.activity_main);
        init();
        new OnlineUtils(getApplicationContext());
//        AVObject testObject = new AVObject("TestObject");
//        testObject.put("words","Hello World!");
//        testObject.saveInBackground(new SaveCallback() {
//            @Override
//            public void done(AVException e) {
//                if(e == null){
//                    Log.d("saved","success!");
//                }
//            }
//        });

    }

    @Override
    protected void onResume() {
        super.onResume();


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

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()){
            case R.id.create_schedule:{
                Toast.makeText(getApplicationContext(),"施工中(#^.^#)",Toast.LENGTH_SHORT).show();
//                Schedule schedule = new Schedule(UUID.randomUUID(),"","",new Date());
//                ScheduleLab.get(getApplicationContext()).add(schedule);
//                Intent intent = ScheduleActivity.newIntent(getApplication(), schedule.getUuid());
//                startActivity(intent);
                break;
            }
            case R.id.main_refresh:{
                Toast.makeText(getApplicationContext(),"开始同步，30s后可再次同步",Toast.LENGTH_SHORT).show();
                countDownTimer = new CountDownTimer(30 * 1000,30 * 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        item.setEnabled(false);
                    }

                    @Override
                    public void onFinish() {
                        item.setEnabled(true);
                    }
                };
                countDownTimer.start();
                OnlineUtils.synchronizeToNet();
                OnlineUtils.synchronizeFromNet();
                break;
            }
            case R.id.main_sort:{
                final String items[] = {"按照修改日期降序", "按照修改日期升序", "按照文本内容长度升序", "按照文本内容长度降序"};
                AlertDialog dialog = new AlertDialog.Builder(this)
                        .setTitle("选择笔记的排序方式")//设置对话框的标题
                        .setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                NoteLab.COMFLAG = which;
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                NoteLab.COMFLAG = 0;
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                NoteLab.setComparator(NoteLab.COMFLAG);
                                NoteLab.COMFLAG = 0;
                                MeChatFragment.updateWithoutData();
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer!=null)
            countDownTimer.cancel();
    }
}

