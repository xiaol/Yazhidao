package com.news.yazhidao.MyFragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.news.yazhidao.GlobalParams;
import com.news.yazhidao.R;
import com.news.yazhidao.UI.MyListView;
import com.news.yazhidao.UI.MyRelativeLayout;
import com.news.yazhidao.constant.CommonConstant;
import com.news.yazhidao.constant.HttpConstant;
import com.news.yazhidao.entity.NewsFeed;
import com.news.yazhidao.net.JsonCallback;
import com.news.yazhidao.net.MyAppException;
import com.news.yazhidao.net.NetworkRequest;
import com.news.yazhidao.pages.NewsFeedAdapter;
import com.news.yazhidao.utils.Logger;


/**
 * Created by berkley on 31/12/14.
 */
public class FragmentButton extends Fragment {
    private static int STANDARD_WIDTH = 720;
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

    private int width;
    private int height;
    private int currentPos = 0;

    private static final String TAG = "FragmentButton";
    private ImageView iv_sun;
    private ImageView iv_section;
    private MyRelativeLayout rl_content;
    private static MyListView mNewsShowList;
    private static NewsFeedAdapter mNewsFeedAdapter;
    public static NewsFeed mNewsFeed;
    public static final String ARG_PLANET_NUMBER = "planet_number";
    private ChangeNewsModulBroRec mChangeNewsModulBroRec = new ChangeNewsModulBroRec();

    public class ChangeNewsModulBroRec extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int newsModulePos = intent.getIntExtra(CommonConstant.KEY_NEWS_MODULE_POSITION, 0);
            Logger.i(TAG, ">>>change news module ");
            loadNewsData(context, newsModulePos);
        }
    }

    private void registerBReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(CommonConstant.ACTION_CHANGE_NEWS_MODULE);
        getActivity().registerReceiver(mChangeNewsModulBroRec, filter);
    }

    private void unregisterBReceiver() {
        getActivity().unregisterReceiver(mChangeNewsModulBroRec);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        registerBReceiver();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        unregisterBReceiver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(this.getActivity(), R.layout.fragment_planet, null);

        GlobalParams.view = (LinearLayout) View.inflate(getActivity(), R.layout.view_sun, null);

        TextView iv = (TextView) GlobalParams.view.findViewById(R.id.iv_sun);
        iv.setBackgroundResource(R.drawable.sun);

        ((KitkatStatusBar) getActivity()).setGlobalView(GlobalParams.view);
        GlobalParams.mainSection = (KitkatStatusBar) getActivity();
        ((KitkatStatusBar) getActivity()).setGlobalFlag(true);

        rl_content = (MyRelativeLayout) view.findViewById(R.id.rl_content);
        iv_section = (ImageView) view.findViewById(R.id.iv_section);

        mNewsShowList = (MyListView) view.findViewById(R.id.mNewsShowList);
        mNewsFeed = new NewsFeed();
        mNewsFeedAdapter = new NewsFeedAdapter(this.getActivity(), mNewsFeed);
        mNewsShowList.setAdapter(mNewsFeedAdapter);

        GlobalParams.manager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);

        width = GlobalParams.manager.getDefaultDisplay().getWidth();
        height = GlobalParams.manager.getDefaultDisplay().getHeight();

        setSunview();
        showView();

        //loadNewsData(getActivity(), 3);

        return view;

    }

    private void setSunview() {
        switch (height) {

            case 1920:

                POINT_ONE_X = 70;
                POINT_ONE_Y = 460;
                POINT_TWO_X = 346;
                POINT_TWO_Y = 390;
                POINT_THREE_X = 445;
                POINT_THREE_Y = 240;
                POINT_FOUR_X = 740;
                POINT_FOUR_Y = 180;
                SUN_WIDTH = 180;

                break;

            case 1800:

                POINT_ONE_X = 70;
                POINT_ONE_Y = 440;
                POINT_TWO_X = 350;
                POINT_TWO_Y = 370;
                POINT_THREE_X = 445;
                POINT_THREE_Y = 220;
                POINT_FOUR_X = 730;
                POINT_FOUR_Y = 150;
                SUN_WIDTH = 180;

                break;


            case 1776:

                POINT_ONE_X = 70;
                POINT_ONE_Y = 470;
                POINT_TWO_X = 350;
                POINT_TWO_Y = 395;
                POINT_THREE_X = 445;
                POINT_THREE_Y = 245;
                POINT_FOUR_X = 730;
                POINT_FOUR_Y = 170;
                SUN_WIDTH = 180;

                break;


            case 1280:

                POINT_ONE_X = 55;
                POINT_ONE_Y = 300;
                POINT_TWO_X = 220;
                POINT_TWO_Y = 250;
                POINT_THREE_X = 290;
                POINT_THREE_Y = 150;
                POINT_FOUR_X = 480;
                POINT_FOUR_Y = 105;
                SUN_WIDTH = 180;

                break;

            case 854:

                POINT_ONE_X = 37;
                POINT_ONE_Y = 210;
                POINT_TWO_X = 155;
                POINT_TWO_Y = 185;
                POINT_THREE_X = 200;
                POINT_THREE_Y = 110;
                POINT_FOUR_X = 320;
                POINT_FOUR_Y = 87;
                SUN_WIDTH = 180;

                break;

            case 800:

                POINT_ONE_X = 35;
                POINT_ONE_Y = 210;
                POINT_TWO_X = 150;
                POINT_TWO_Y = 180;
                POINT_THREE_X = 200;
                POINT_THREE_Y = 110;
                POINT_FOUR_X = 330;
                POINT_FOUR_Y = 87;
                SUN_WIDTH = 180;

                break;

        }
    }


    private void showView() {

        GlobalParams.manager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);

        width = GlobalParams.manager.getDefaultDisplay().getWidth();
        height = GlobalParams.manager.getDefaultDisplay().getHeight();

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

                            GlobalParams.params.x = (int) (POINT_ONE_X);
                            GlobalParams.params.y = (int) (POINT_ONE_Y);

                            //iv_section.setBackgroundResource(R.drawable.section1);
                            if (GlobalParams.bar != null) {
                                GlobalParams.bar.setTitle("你未见的时代痛楚");
                            }
                            GlobalParams.currentPos = 0;

                        } else if (startX > width * SECTION_ONE / STANDARD_WIDTH && startX <= width * SECTION_TWO / STANDARD_WIDTH) {
                            GlobalParams.params.x = (int) (POINT_TWO_X);
                            GlobalParams.params.y = (int) (POINT_TWO_Y);

                            //iv_section.setBackgroundResource(R.drawable.section2);
                            if (GlobalParams.bar != null) {
                                GlobalParams.bar.setTitle("你不知道的冷新闻");
                            }
                            GlobalParams.currentPos = 1;


                        } else if (startX > width * SECTION_TWO / STANDARD_WIDTH && startX <= width * SECTION_THREE / STANDARD_WIDTH) {
                            GlobalParams.params.x = (int) (POINT_THREE_X);
                            GlobalParams.params.y = (int) (POINT_THREE_Y);

                            //iv_section.setBackgroundResource(R.drawable.section3);
                            if (GlobalParams.bar != null) {
                                GlobalParams.bar.setTitle("同步你的关注热度");
                            }
                            GlobalParams.currentPos = 2;

                        } else if (startX > width * SECTION_THREE / STANDARD_WIDTH && startX <= width) {
                            GlobalParams.params.x = (int) (POINT_FOUR_X);
                            GlobalParams.params.y = (int) (POINT_FOUR_Y);

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
        GlobalParams.params.x = (int) (POINT_FOUR_X);
        //GlobalParams.params.x = 75;
        // 指定距离屏幕上边的距离 必须与 Gravity.TOP同时使用
        GlobalParams.params.y = POINT_FOUR_Y;

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

        GlobalParams.LISTVIEW_HEIGHT = 0;
        GlobalParams.LISTVIEW_ERROR = 0;

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(CommonConstant.ACTION_CHANGE_NEWS_MODULE);
                intent.putExtra(CommonConstant.KEY_NEWS_MODULE_POSITION, GlobalParams.currentPos);
                getActivity().sendBroadcast(intent);
            }
        }, 500);
        Logger.i(TAG, ">>currentPos=" + GlobalParams.currentPos);
    }


    private static void loadNewsData(final Context mContext, int newsModulePos) {
        NetworkRequest request = new NetworkRequest(HttpConstant.URL_FETCH_NEWS_FOR_MODULE + newsModulePos, NetworkRequest.RequestMethod.GET);
        request.setCallback(new JsonCallback<NewsFeed>() {

            @Override
            public void success(NewsFeed result) {
                Log.i(">>>" + TAG, result.toString());
                mNewsFeed = result;
                mNewsFeedAdapter = new NewsFeedAdapter(mContext, mNewsFeed);
                mNewsShowList.setAdapter(mNewsFeedAdapter);
                mNewsFeedAdapter.notifyDataSetChanged();
            }

            public void failed(MyAppException exception) {
                Log.i(">>>" + TAG, exception.getMessage());
            }
        }.setReturnType(new TypeToken<NewsFeed>() {
        }.getType()));
        request.execute();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (GlobalParams.SUN_FLAG == false) {
            GlobalParams.view.setVisibility(View.GONE);
        }

    }
}
