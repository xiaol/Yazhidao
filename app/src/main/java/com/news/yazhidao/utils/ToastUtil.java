package com.news.yazhidao.utils;

import android.content.Context;
import android.content.res.Resources;
import android.widget.Toast;

import com.news.yazhidao.application.YaZhiDaoApplication;

/**
 * Created by fengjigang on 15/2/4.
 */
public class ToastUtil {
    public static void toastLong(String text) {
        Toast.makeText(YaZhiDaoApplication.getAppContext(), text, Toast.LENGTH_LONG).show();
    }

    public static void toastLong(int resId) {
        Resources res = YaZhiDaoApplication.getAppContext().getResources();
        Toast.makeText(YaZhiDaoApplication.getAppContext(), res.getString(resId), Toast.LENGTH_LONG).show();
    }

    public static void toastShort(int resId) {
        Resources res = YaZhiDaoApplication.getAppContext().getResources();
        Toast.makeText(YaZhiDaoApplication.getAppContext(), res.getString(resId), Toast.LENGTH_SHORT).show();
    }

    public static void toastShort(String text) {
        Toast.makeText(YaZhiDaoApplication.getAppContext(), text, Toast.LENGTH_SHORT).show();
    }
}
