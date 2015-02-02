package com.news.yazhidao.application;

import android.app.Application;
import android.content.Context;

import com.news.yazhidao.utils.helper.UmengShareHelper;

/**
 * Created by fengjigang on 15/2/1.
 */
public class YaZhiDaoApplication extends Application {
    public static Context mContext;
    @Override
    public void onCreate() {
        mContext=this;
        super.onCreate();
    }
    public static Context getAppContext(){
        return mContext;
    }
}
