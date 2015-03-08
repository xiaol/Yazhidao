package com.news.yazhidao.MyFragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
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
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.news.yazhidao.R;
import com.news.yazhidao.UI.MyListView;
import com.news.yazhidao.UI.MyRelativeLayout;
import com.news.yazhidao.constant.GlobalParams;
import com.news.yazhidao.constant.HttpConstant;
import com.news.yazhidao.entity.NewsFeed;
import com.news.yazhidao.net.JsonCallback;
import com.news.yazhidao.net.MyAppException;
import com.news.yazhidao.net.NetworkRequest;
import com.news.yazhidao.pages.NewsFeedAdapter;
import com.news.yazhidao.utils.DeviceInfoUtil;
import com.news.yazhidao.utils.NetUtil;
import com.news.yazhidao.utils.helper.UmengShareHelper;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.HashMap;


/**
 * Created by berkley on 31/12/14.
 */
public class FragmentButton extends Fragment implements View.OnClickListener {
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
    private static int SUN_WIDTH = 250;

    private int width;
    private int height;
    private boolean flag = false;

    private View view;

    private static final String TAG = "FragmentButton";
    private ImageView iv_sun;
    private ImageView iv_section;
    private ImageView iv_orbit;
    private MyRelativeLayout rl_content;
    private static MyListView mNewsShowList;
    private static NewsFeedAdapter mNewsFeedAdapter;
    public static NewsFeed mNewsFeed;
    public static final String ARG_PLANET_NUMBER = "planet_number";

    private View mNewsDetailLoadingWrapper;
    private ImageView mNewsLoadingImg;
    private View mNewsDetailCilckRefresh;
    private AnimationDrawable mAniNewsLoading;

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.mNewsDetailCilckRefresh:

                if (NetUtil.checkNetWork(getActivity())) {

                    //有网络的时候
                    mNewsDetailCilckRefresh.setVisibility(View.GONE);
                    rl_content.setVisibility(View.GONE);
                    mNewsDetailLoadingWrapper.setVisibility(View.VISIBLE);

                    loadNewsData(getActivity(), GlobalParams.currentPos);

                } else {
                    //没有网络的时候
                    mNewsDetailCilckRefresh.setVisibility(View.VISIBLE);
                    rl_content.setVisibility(View.GONE);
                    mNewsDetailLoadingWrapper.setVisibility(View.GONE);

                    Toast.makeText(getActivity(), "网络异常，请检查您的网络....", Toast.LENGTH_LONG).show();
                }

        }

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        GlobalParams.manager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);

        width = GlobalParams.manager.getDefaultDisplay().getWidth();
        height = GlobalParams.manager.getDefaultDisplay().getHeight();

        //设置当前页面
        view = View.inflate(this.getActivity(), R.layout.fragment_planet, null);

        rl_content = (MyRelativeLayout) view.findViewById(R.id.rl_content);
        iv_section = (ImageView) view.findViewById(R.id.iv_section);
        GlobalParams.iv_orbit = (ImageView) view.findViewById(R.id.iv_orbit);
        mNewsShowList = (MyListView) view.findViewById(R.id.mNewsShowList);
        mNewsShowList.setDividerHeight((int)(30*1.0/1080*DeviceInfoUtil.getScreenHeight()));
        mNewsLoadingImg = (ImageView) view.findViewById(R.id.mNewsLoadingImg);
        mNewsLoadingImg.setImageResource(R.drawable.news_progress_animation_list);
        mAniNewsLoading = (AnimationDrawable) mNewsLoadingImg.getDrawable();
        mAniNewsLoading.start();
        mNewsDetailLoadingWrapper = view.findViewById(R.id.mNewsDetailLoadingWrapper);
        mNewsDetailCilckRefresh = view.findViewById(R.id.mNewsDetailCilckRefresh);
        mNewsDetailCilckRefresh.setOnClickListener(this);

        if (NetUtil.checkNetWork(getActivity())) {

            //有网络的时候
            mNewsDetailCilckRefresh.setVisibility(View.GONE);
            rl_content.setVisibility(View.GONE);
            mNewsDetailLoadingWrapper.setVisibility(View.VISIBLE);
            boolean isLogin = UmengShareHelper.isAuthenticated(getActivity(), SHARE_MEDIA.SINA);
            if (isLogin) {
                //如果用户在首订的时候登陆了，就进入36°C，否则进入40°C
                GlobalParams.currentPos = 2;
            } else {
                GlobalParams.currentPos = 3;
            }
            loadNewsData(getActivity(), GlobalParams.currentPos);

        } else {
            //没有网络的时候
            mNewsDetailCilckRefresh.setVisibility(View.VISIBLE);
            rl_content.setVisibility(View.GONE);
            mNewsDetailLoadingWrapper.setVisibility(View.GONE);

            Toast.makeText(getActivity(), "网络异常，请检查您的网络....", Toast.LENGTH_LONG).show();
        }


        //mNewsDetailCilckRefresh.setTag(mNewsEle.sourceUrl);


        return view;

    }

    private void initView() {

        setSunview();
        showView();
    }

    private void setSunview() {
        switch (height) {

            case 1920:

                POINT_ONE_X = 80;
                POINT_ONE_Y = 410;
                POINT_TWO_X = 280;
                POINT_TWO_Y = 350;
                POINT_THREE_X = 350;
                POINT_THREE_Y = 210;
                POINT_FOUR_X = 610;
                POINT_FOUR_Y = 150;

                break;

            case 1800:

                POINT_ONE_X = -20;
                POINT_ONE_Y = 340;
                POINT_TWO_X = 250;
                POINT_TWO_Y = 270;
                POINT_THREE_X = 350;
                POINT_THREE_Y = 130;
                POINT_FOUR_X = 800;
                POINT_FOUR_Y = 70;

                break;


            case 1776:

                POINT_ONE_X = 0;
                POINT_ONE_Y = 370;
                POINT_TWO_X = 250;
                POINT_TWO_Y = 300;
                POINT_THREE_X = 360;
                POINT_THREE_Y = 160;
                POINT_FOUR_X = 730;
                POINT_FOUR_Y = 170;
                SUN_WIDTH = 180;

                break;


            case 1280:

                POINT_ONE_X = 60;
                POINT_ONE_Y = 260;
                POINT_TWO_X = 180;
                POINT_TWO_Y = 230;
                POINT_THREE_X = 240;
                POINT_THREE_Y = 130;
                POINT_FOUR_X = 400;
                POINT_FOUR_Y = 80;

                break;

            case 1184:

                POINT_ONE_X = 60;
                POINT_ONE_Y = 260;
                POINT_TWO_X = 175;
                POINT_TWO_Y = 220;
                POINT_THREE_X = 235;
                POINT_THREE_Y = 125;
                POINT_FOUR_X = 400;
                POINT_FOUR_Y = 80;

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

                POINT_ONE_X = 30;
                POINT_ONE_Y = 190;
                POINT_TWO_X = 110;
                POINT_TWO_Y = 155;
                POINT_THREE_X = 160;
                POINT_THREE_Y = 90;
                POINT_FOUR_X = 300;
                POINT_FOUR_Y = 50;

                break;

        }
    }


    private void showView() {

        //设置全局参数
        GlobalParams.view = (LinearLayout) View.inflate(getActivity(), R.layout.view_sun, null);

        TextView iv = (TextView) GlobalParams.view.findViewById(R.id.iv_sun);
        iv.setBackgroundResource(R.drawable.sun);

        ((KitkatStatusBar) getActivity()).setGlobalView(GlobalParams.view);
        GlobalParams.mainSection = (KitkatStatusBar) getActivity();
        ((KitkatStatusBar) getActivity()).setGlobalFlag(true);


        flag = true;
        iv_section.setBackgroundResource(R.drawable.section44);

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
                        GlobalParams.iv_orbit.setVisibility(View.VISIBLE);
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

                            if (GlobalParams.bar != null) {
                                GlobalParams.bar.setTitle("你未见的时代痛楚");
                            }
                            iv_section.setBackgroundResource(R.drawable.section11);
                            GlobalParams.currentPos = 0;

                        } else if (startX > width * SECTION_ONE / STANDARD_WIDTH && startX <= width * SECTION_TWO / STANDARD_WIDTH) {
                            GlobalParams.params.x = (int) (POINT_TWO_X);
                            GlobalParams.params.y = (int) (POINT_TWO_Y);

                            if (GlobalParams.bar != null) {
                                GlobalParams.bar.setTitle("你不知道的冷新闻");
                            }
                            iv_section.setBackgroundResource(R.drawable.section22);
                            GlobalParams.currentPos = 1;

                        } else if (startX > width * SECTION_TWO / STANDARD_WIDTH && startX <= width * SECTION_THREE / STANDARD_WIDTH) {
                            GlobalParams.params.x = (int) (POINT_THREE_X);
                            GlobalParams.params.y = (int) (POINT_THREE_Y);

                            if (GlobalParams.bar != null) {
                                GlobalParams.bar.setTitle("同步你的关注热度");
                            }

                            iv_section.setBackgroundResource(R.drawable.section33);

                            GlobalParams.currentPos = 2;

                        } else if (startX > width * SECTION_THREE / STANDARD_WIDTH && startX <= width) {
                            GlobalParams.params.x = (int) (POINT_FOUR_X);
                            GlobalParams.params.y = (int) (POINT_FOUR_Y);

                            if (GlobalParams.bar != null) {
                                GlobalParams.bar.setTitle("触摸时下热点所在");
                            }

                            iv_section.setBackgroundResource(R.drawable.section44);

                            GlobalParams.currentPos = 3;

                        }

                        GlobalParams.manager.updateViewLayout(GlobalParams.view, GlobalParams.params);
                        if (NetUtil.checkNetWork(getActivity())) {
                            //有网络的时候
                            loadNewsData(getActivity(), GlobalParams.currentPos);
                        } else {
                            //没有网络的时候
                            mNewsDetailCilckRefresh.setVisibility(View.VISIBLE);
                            rl_content.setVisibility(View.GONE);
                            mNewsDetailLoadingWrapper.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "网络异常，请检查您的网络....", Toast.LENGTH_LONG).show();
                            GlobalParams.view.setVisibility(View.GONE);
                        }

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

        boolean isLogin = UmengShareHelper.isAuthenticated(getActivity(), SHARE_MEDIA.SINA);
        if (isLogin) {
            // 指定距离屏幕左边的距离 必须与 Gravity.LEFT同时使用
            GlobalParams.params.x = POINT_THREE_X;
            //GlobalParams.params.x = 75;
            // 指定距离屏幕上边的距离 必须与 Gravity.TOP同时使用
            GlobalParams.params.y = POINT_THREE_Y;
        } else {
            // 指定距离屏幕左边的距离 必须与 Gravity.LEFT同时使用
            GlobalParams.params.x = POINT_FOUR_X;
            //GlobalParams.params.x = 75;
            // 指定距离屏幕上边的距离 必须与 Gravity.TOP同时使用
            GlobalParams.params.y = POINT_FOUR_Y;
        }


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

        GlobalParams.DELETE_FLAG = false;

    }

    private void loadNewsData(final Context mContext, int newsModulePos) {

        GlobalParams.LISTVIEW_HEIGHT = 0;
        GlobalParams.LISTVIEW_ERROR = 0;

        GlobalParams.REFRESH_FLAG = true;
        NetworkRequest request = new NetworkRequest(HttpConstant.URL_FETCH_NEWS_FOR_MODULE, NetworkRequest.RequestMethod.GET);
        HashMap<String, Object> params = new HashMap<>();
        params.put("limit", 9);
        params.put("root_class", newsModulePos);
        params.put("uuid", DeviceInfoUtil.getUUID());
        request.getParams = params;
        request.setCallback(new JsonCallback<NewsFeed>() {

            @Override
            public void success(NewsFeed result) {
                Log.i(">>>" + TAG, result.toString());

                if (result != null) {
                    mNewsFeed = new NewsFeed();
                    mNewsFeedAdapter = new NewsFeedAdapter(mContext, mNewsFeed);
                    mNewsShowList.setAdapter(mNewsFeedAdapter);

                    mNewsFeed = result;
                    mNewsFeedAdapter = new NewsFeedAdapter(mContext, mNewsFeed);
                    mNewsShowList.setAdapter(mNewsFeedAdapter);
                    mNewsFeedAdapter.notifyDataSetChanged();
                    mNewsDetailLoadingWrapper.setVisibility(View.GONE);
                    mAniNewsLoading.stop();
                    mNewsDetailCilckRefresh.setVisibility(View.GONE);
                    rl_content.setVisibility(View.VISIBLE);

                    GlobalParams.iv_orbit.setVisibility(View.GONE);

                    if (GlobalParams.view == null) {
                        initView();
                    } else if (GlobalParams.view.getVisibility() == View.GONE) {
                        GlobalParams.view.setVisibility(View.VISIBLE);
                    } else if (GlobalParams.view.getVisibility() == View.VISIBLE) {
                        GlobalParams.view.setVisibility(View.VISIBLE);
                    }

                    GlobalParams.REFRESH_FLAG = false;

                } else {
                    Toast.makeText(mContext, "网络异常，请查看网络...", Toast.LENGTH_SHORT).show();
                }

            }

            public void failed(MyAppException exception) {
                Log.i(">>>" + TAG, exception.getMessage());
            }
        }.setReturnType(new TypeToken<NewsFeed>() {
        }.getType()));
        request.execute();
    }

    @Override
    public void onDestroy() {
        if (GlobalParams.view != null) {
            GlobalParams.manager.removeView(GlobalParams.view);
            GlobalParams.view = null;
        }
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (GlobalParams.SUN_FLAG == false) {
            GlobalParams.view.setVisibility(View.GONE);
        }

    }
}
