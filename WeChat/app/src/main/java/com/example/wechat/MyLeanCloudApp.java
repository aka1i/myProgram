package com.example.wechat;


import android.app.Application;

import com.avos.avoscloud.AVOSCloud;

public class MyLeanCloudApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        // 初始化参数依次为 this, AppId, AppKey
        AVOSCloud.initialize(this,"BiVGiwzPS4Ql90eYNp7Ob0o4-gzGzoHsz","hCTfMCpjDOE0vnqSuEkPAruT");
        AVOSCloud.setDebugLogEnabled(true);
    }
}
