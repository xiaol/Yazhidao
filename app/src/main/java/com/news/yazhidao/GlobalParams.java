package com.news.yazhidao;

import android.support.v4.app.Fragment;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.news.yazhidao.MyFragment.KitkatStatusBar;

import it.neokree.materialnavigationdrawer.elements.MaterialSection;

public class GlobalParams {
	
	public static WindowManager manager;
	public static LinearLayout view;
	public static WindowManager.LayoutParams params;
    public static int LISTVIEW_HEIGHT = 0;
    public static int LISTVIEW_ERROR = 0;
    public static MaterialSection<Fragment> section;
    public static android.support.v7.app.ActionBar bar;
    public static boolean SUN_FLAG = true;
    public static KitkatStatusBar mainSection = null;
    public static int currentPos = 3;
}
