package com.news.yazhidao.utils.helper;

import android.content.Context;
import android.content.SharedPreferences;

import com.news.yazhidao.application.YaZhiDaoApplication;
import com.news.yazhidao.utils.Logger;

/**
 * Created by fengjigang on 15/2/1.
 */
public class SettingHelper {
    public static SharedPreferences getSettings(String name, int mode){
        return YaZhiDaoApplication.getAppContext().getSharedPreferences(name, mode);
    }

    public static void save(String settingName, String key, String value){
        SharedPreferences.Editor e = getSettings(settingName, Context.MODE_PRIVATE).edit();
        e.putString(key, value);
        e.commit();
    }

    public static void save(String settingName, String key, long value){
        Logger.d("SettingsManager", "SettingsManager : " + settingName + ":" + "key : " + key + "value : " + value);
        SharedPreferences.Editor e = getSettings(settingName, Context.MODE_PRIVATE).edit();
        e.putLong(key, value);
        e.commit();
    }

    public static void save(String settingName, String key, boolean value){
        SharedPreferences.Editor e = getSettings(settingName, Context.MODE_PRIVATE).edit();
        e.putBoolean(key, value);
        e.commit();
    }


    public static void clear(String settingName){
        SharedPreferences.Editor editor = getSettings(settingName, Context.MODE_PRIVATE).edit();
        editor.clear();
        editor.commit();
    }

    public static String get(String settingName, String key){
        return getSettings(settingName, Context.MODE_PRIVATE).getString(key, "");
    }

    public static boolean getBoolean(String settingName, String key){
        return getSettings(settingName, Context.MODE_PRIVATE).getBoolean(key, false);
    }

    public static boolean getBoolean(String settingName, String key, boolean defaultValue){
        return getSettings(settingName, Context.MODE_PRIVATE).getBoolean(key, defaultValue);
    }

    public static long getLong(String settingName, String key){
        return getSettings(settingName, Context.MODE_PRIVATE).getLong(key, 0);
    }

    public static void remove(String settingName, String...keys){
        if(keys == null) return;
        SharedPreferences.Editor e = getSettings(settingName, Context.MODE_PRIVATE).edit();
        for(String key : keys){
            e.remove(key);
        }
        e.commit();
    }
}
