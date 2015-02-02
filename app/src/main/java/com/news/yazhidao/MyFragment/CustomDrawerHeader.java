package com.news.yazhidao.MyFragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.news.yazhidao.GlobalParams;
import com.news.yazhidao.R;
import com.news.yazhidao.constant.CommonConstant;
import com.news.yazhidao.utils.Logger;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;

/**
 * Created by neokree on 18/01/15.
 */
public class CustomDrawerHeader extends MaterialNavigationDrawer {

    private static int STANDARD_WIDTH = 720;
    private static int SECTION_VIEW_HEIGHT = 480;
    private static int POINT_ONE_X = 55;
    private static int POINT_ONE_Y = 300;
    private static int POINT_TWO_X = 220;
    private static int POINT_TWO_Y = 250;
    private static int POINT_THREE_X = 290;
    private static int POINT_THREE_Y = 150;
    private static int POINT_FOUR_X = 480;
    private static int POINT_FOUR_Y = 105;
    private static int SECTION_ONE = 260;
    private static int SECTION_TWO = 380;
    private static int SECTION_THREE = 480;
    private static int SUN_WIDTH = 180;
    private static final String TAG = "SlideMenu";

    private int width;
    private int height;
    private ImageView iv_section;

    @Override
    protected void onResume() {
        if(GlobalParams.view != null){
            GlobalParams.view.setVisibility(View.VISIBLE);
        }
        super.onResume();
    }

    @Override
    protected void onRestart() {
        if(GlobalParams.view != null){
            GlobalParams.view.setVisibility(View.VISIBLE);
        }

        super.onRestart();
    }

    @Override
    public void init(Bundle savedInstanceState) {

//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        // create and set the header
        View view = LayoutInflater.from(this).inflate(R.layout.custom_drawer,null);
        setDrawerHeaderCustom(view);

        GlobalParams.bar = this.getSupportActionBar();

        setGlobalView();

        GlobalParams.section = newSection("我的36°", R.drawable.my36,new FragmentButton()).setSectionColor(Color.parseColor("#FF7F66"));
        this.addSection(GlobalParams.section);
        this.addSection(newSection("设置", R.drawable.shez,new SettingButton()).setSectionColor(Color.parseColor("#FF7F66")));
        this.addSection(newSection("关于", R.drawable.guanyu,new AboutButton()).setSectionColor(Color.parseColor("#FF7F66")));
        this.addSection(newSection("反馈", R.drawable.fangui,new FeedbackButton()).setSectionColor(Color.parseColor("#FF7F66")));

        // create bottom section
        //this.addBottomSection(newSection("Bottom Section",R.drawable.ic_settings_black_24dp,new Intent(this,Settings.class)));

        this.disableLearningPattern();

    }

    private void setGlobalView() {




    }

    private void showView() {

        GlobalParams.manager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);

        width = GlobalParams.manager.getDefaultDisplay().getWidth();
        height = GlobalParams.manager.getDefaultDisplay().getHeight();
        iv_section = (ImageView) this.findViewById(R.id.iv_section);
        // 给view对象组成触摸的监听器
        GlobalParams.view.setOnTouchListener(new View.OnTouchListener() {
            // 手指在屏幕上第一次按下的初始坐标
            int startX;
            int startY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:// 手指按下屏幕 第一次接触屏幕
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();

                        break;

                    case MotionEvent.ACTION_MOVE: // 手指在屏幕上触摸移动

                        int newX = (int) event.getRawX();
                        int newY = (int) event.getRawY();
                        int dx = newX - startX;
                        int dy = newY - startY;
                        // 立刻让控件也跟随着手指移动 dx dy。
                        GlobalParams.params.x += dx;
                        GlobalParams.params.y += dy;
                        // 边界判断
                        if (GlobalParams.params.x < 0) {// 向左移出屏幕
                            GlobalParams.params.x = 0;
                        }
                        if (GlobalParams.params.y < 0) {// 向上移出屏幕
                            GlobalParams.params.y = 0;
                        }
                        if (GlobalParams.params.x > (GlobalParams.manager.getDefaultDisplay().getWidth() - GlobalParams.view.getWidth())) {// 向右移出屏幕
                            GlobalParams.params.x = GlobalParams.manager.getDefaultDisplay().getWidth() - GlobalParams.view.getWidth();
                        }
                        if (GlobalParams.params.y > (GlobalParams.manager.getDefaultDisplay().getHeight() - GlobalParams.view.getHeight())) {// 向右移出屏幕
                            GlobalParams.params.y = GlobalParams.manager.getDefaultDisplay().getHeight() - GlobalParams.view.getHeight();
                        }
                        // 重复第一步的操作 ，重新初始化手指的开始位置。

                        GlobalParams.manager.updateViewLayout(GlobalParams.view, GlobalParams.params);

                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;

                    case MotionEvent.ACTION_UP:// 手指离开屏幕一瞬间对应的动作
                        // Editor editor = sp.edit();
                        // editor.putInt("paramsx", params.x);
                        // editor.putInt("paramsy", params.y);
                        // editor.commit();

                        if (startX > 0 && startX <= width * SECTION_ONE / STANDARD_WIDTH) {
                            //view.scrollTo(50, 320);

                            GlobalParams.params.x = (int) (POINT_ONE_X * width / STANDARD_WIDTH);
                            GlobalParams.params.y = (int) (POINT_ONE_Y * width / STANDARD_WIDTH);

                            //iv_section.setBackgroundResource(R.drawable.section1);
                            if (GlobalParams.bar != null) {
                                GlobalParams.bar.setTitle("你未见的时代痛楚");
                            }
                            GlobalParams.currentPos = 0;

                            //END_WINDOW = WINDOW_ONE;

                        } else if (startX > width * SECTION_ONE / STANDARD_WIDTH && startX <= width * SECTION_TWO / STANDARD_WIDTH) {
                            GlobalParams.params.x = (int) (POINT_TWO_X * width / STANDARD_WIDTH);
                            GlobalParams.params.y = (int) (POINT_TWO_Y * width / STANDARD_WIDTH);

                            //iv_section.setBackgroundResource(R.drawable.section2);
                            if (GlobalParams.bar != null) {
                                GlobalParams.bar.setTitle("你不知道的冷新闻");
                            }
                            GlobalParams.currentPos = 1;


                        } else if (startX > width * SECTION_TWO / STANDARD_WIDTH && startX <= width * SECTION_THREE / STANDARD_WIDTH) {
                            GlobalParams.params.x = (int) (POINT_THREE_X * width / STANDARD_WIDTH);
                            GlobalParams.params.y = (int) (POINT_THREE_Y * width / STANDARD_WIDTH);

                            //iv_section.setBackgroundResource(R.drawable.section3);
                            if (GlobalParams.bar != null) {
                                GlobalParams.bar.setTitle("同步你的关注热度");
                            }
                            GlobalParams.currentPos = 2;

                        } else if (startX > width * SECTION_THREE / STANDARD_WIDTH && startX <= width) {
                            GlobalParams.params.x = (int) (POINT_FOUR_X * width / STANDARD_WIDTH);
                            GlobalParams.params.y = (int) (POINT_FOUR_Y * width / STANDARD_WIDTH);

                            //iv_section.setBackgroundResource(R.drawable.section4);
                            if (GlobalParams.bar != null) {
                                GlobalParams.bar.setTitle("触摸时下热点所在");
                            }
                            GlobalParams.currentPos = 3;

                        }

                        GlobalParams.manager.updateViewLayout(GlobalParams.view, GlobalParams.params);

//					animation = new RotateAnimation(fromDegree, toDegree, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//
//					animation.setDuration(500);// 设置动画持续时间
//					animation.setFillAfter(true);// 动画执行完后是否停留在执行完的状态
//					earthView.startAnimation(animation);
                        delayNotifyChangeNews();

                        break;
                }
                return false;// True if the listener has consumed the event,
                // false otherwise.
                // true 代表监听器 处理掉了这个事件，false监听器没有处理这个事件。
            }
        });

        // 土司显示的参数
        GlobalParams.params = new WindowManager.LayoutParams();
        // 对其方式
        GlobalParams.params.gravity = Gravity.LEFT + Gravity.TOP;

        // 指定距离屏幕左边的距离 必须与 Gravity.LEFT同时使用
        GlobalParams.params.x = (int) (POINT_ONE_X * width / STANDARD_WIDTH);
        //GlobalParams.params.x = 75;
        // 指定距离屏幕上边的距离 必须与 Gravity.TOP同时使用
        GlobalParams.params.y = POINT_ONE_Y * width / STANDARD_WIDTH;

        // 土司的宽高
        GlobalParams.params.height = (int) (SUN_WIDTH * width / STANDARD_WIDTH);
        //GlobalParams.params.height = 270;
        GlobalParams.params.width = (int) (SUN_WIDTH * width / STANDARD_WIDTH);
        //GlobalParams.params.width = 270;
        // 土司的参数 不可获取焦点 不可以别点击 保存屏幕常亮
        GlobalParams.params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
        // 半透明窗体
        GlobalParams.params.format = PixelFormat.TRANSLUCENT;
        GlobalParams.params.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;//
        // 改用电话优先级的窗体类型，这种类型可以相应触摸事件。
        GlobalParams.manager.addView(GlobalParams.view, GlobalParams.params);

        delayNotifyChangeNews();
    }

    private void delayNotifyChangeNews() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(CommonConstant.ACTION_CHANGE_NEWS_MODULE);
                intent.putExtra(CommonConstant.KEY_NEWS_MODULE_POSITION, GlobalParams.currentPos);
                getApplicationContext().sendBroadcast(intent);
            }
        }, 500);
        Logger.i(TAG, ">>currentPos=" + GlobalParams.currentPos);
    }

}
