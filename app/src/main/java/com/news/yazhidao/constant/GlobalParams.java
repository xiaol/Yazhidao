package com.news.yazhidao.constant;

import android.support.v4.app.Fragment;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.news.yazhidao.pages.HomeAty;

import it.neokree.materialnavigationdrawer.elements.MaterialSection;

public class GlobalParams {
    public static String PROXY_IP = "";
    public static int PROXY_PORT;
    public static WindowManager manager;
    public static LinearLayout view = null;
    public static WindowManager.LayoutParams params;
    public static int LISTVIEW_HEIGHT = 0;
    public static int LISTVIEW_ERROR = 0;
    public static MaterialSection<Fragment> section;
    public static android.support.v7.app.ActionBar bar;
    public static boolean SUN_FLAG = true;
    public static boolean DELETE_FLAG = false;
    public static boolean ONE_FLAG = false;
    public static boolean REFRESH_FLAG = false;
    public static boolean ADD_SUN_FLAG = false;
    public static HomeAty mainSection = null;
    public static ImageView iv_orbit = null;
    public static int currentPos = 3;
    public static int previousPos = 0;
    public static int mInitPos;

}
