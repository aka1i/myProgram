package com.example.wechat;

import android.support.v4.app.Fragment;

public class FragmentCreater {
    public static Fragment[] getFragments(){
        Fragment fragments[] = new Fragment[4];
        fragments[0] = MeChatFragment.newInstance();
        fragments[1] = MailListFragment.newInstance();
        fragments[2] = FindFragment.newInstance();
        fragments[3] = MeFragment.newInstance();
        return fragments;
    }

}
