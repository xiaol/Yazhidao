package com.news.yazhidao.pages.user;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.news.yazhidao.R;
import com.news.yazhidao.constant.GlobalParams;
import com.news.yazhidao.constant.HttpConstant;
import com.news.yazhidao.database.DBHelper;
import com.news.yazhidao.entity.NewsFeed;
import com.news.yazhidao.net.JsonCallback;
import com.news.yazhidao.net.MyAppException;
import com.news.yazhidao.net.NetworkRequest;
import com.news.yazhidao.pages.HomeAty;
import com.news.yazhidao.pages.feed.NewsFeedAdapter;
import com.news.yazhidao.utils.DeviceInfoUtil;
import com.news.yazhidao.utils.Logger;
import com.news.yazhidao.utils.NetUtil;
import com.news.yazhidao.utils.ToastUtil;
import com.news.yazhidao.utils.helper.UmengShareHelper;
import com.news.yazhidao.widget.FeedStreamLayout;
import com.news.yazhidao.widget.FeedStreamListView;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.text.DecimalFormat;
import java.util.HashMap;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;


/**
 * Created by berkley on 31/12/14.
 */
public class FeedStreamFgt extends Fragment implements View.OnClickListener {
    private static int STANDARD_WIDTH = 720;
    public static int POINT_ONE_X = 55;
    public static int POINT_ONE_Y = 300;
    public static int POINT_TWO_X = 220;
    public static int POINT_TWO_Y = 250;
    public static int POINT_THREE_X = 290;
    public static int POINT_THREE_Y = 150;
    public static int POINT_FOUR_X = 480;
    public static int POINT_FOUR_Y = 105;
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
    private FeedStreamLayout rl_content;
    private static FeedStreamListView mNewsShowList;
    private static NewsFeedAdapter mNewsFeedAdapter;
    public static NewsFeed mNewsFeed;
    public static final String ARG_PLANET_NUMBER = "planet_number";

    private View mNewsDetailLoadingWrapper;
    private ImageView mNewsLoadingImg;
    private View mNewsDetailCilckRefresh;
    private AnimationDrawable mAniNewsLoading;
    private RelativeLayout header_view;
    private AbsListView.LayoutParams params;
    private TextView mTips;
    private SharedPreferences sp;
    private Boolean first_flag;

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.mNewsDetailCilckRefresh:

                if (NetUtil.checkNetWork(getActivity())) {
                    GlobalParams.view.setVisibility(View.GONE);
                    //有网络的时候
                    mNewsDetailCilckRefresh.setVisibility(View.GONE);
                    mNewsDetailLoadingWrapper.setVisibility(View.VISIBLE);

                    loadNewsData(getActivity(), GlobalParams.currentPos);

                } else {
                    //没有网络的时候
                    mNewsDetailCilckRefresh.setVisibility(View.VISIBLE);
                    mNewsDetailLoadingWrapper.setVisibility(View.GONE);
                    ToastUtil.toastShort(R.string.network_break_prompt);
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

        sp = getActivity().getSharedPreferences("first", Activity.MODE_PRIVATE);
        first_flag = sp.getBoolean("first_flag", false);

        params = new AbsListView.LayoutParams((int) (width * 0.97), AbsListView.LayoutParams.WRAP_CONTENT);

        //设置当前页面
        view = View.inflate(this.getActivity(), R.layout.fgt_feedstream, null);
        header_view = (RelativeLayout) View.inflate(this.getActivity(), R.layout.header_view, null);
        header_view.setLayoutParams(params);

        iv_section = (ImageView) header_view.findViewById(R.id.iv_section);
        GlobalParams.iv_orbit = (ImageView) header_view.findViewById(R.id.iv_orbit);
        mTips = (TextView) header_view.findViewById(R.id.mTips);
        RelativeLayout.LayoutParams params_tip = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params_tip.leftMargin = (int) (width * .60f);
        params_tip.topMargin = (int) (iv_section.getMeasuredHeight() * .33f);
        mTips.setLayoutParams(params_tip);

        mNewsShowList = (FeedStreamListView) view.findViewById(R.id.mNewsShowList);
        mNewsShowList.setDividerHeight((int) (30 * 1.0 / 1080 * DeviceInfoUtil.getScreenHeight()));
//        RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(width, ViewGroup.LayoutParams.WRAP_CONTENT);
//        header_view.setLayoutParams(params1);
        mNewsShowList.addHeaderView(header_view);
        mNewsShowList.setAdapter(null);//显示headerview

        mNewsShowList.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == 0) {
                    //当屏幕停止滚动时为0；当屏幕滚动且用户使用的触碰或手指还在屏幕上时为1；
                    //由于用户的操作，屏幕产生惯性滑动时为2
                    int firstVisiblePosition = mNewsShowList.getFirstVisiblePosition();
                    if (firstVisiblePosition == 0) {
                        int top = mNewsShowList.getChildAt(firstVisiblePosition).getTop();
                        if (top >= -110) {
                            GlobalParams.view.setVisibility(View.VISIBLE);
                            GlobalParams.SUN_FLAG = true;
                            GlobalParams.context.setGlobalFlag(true);
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        mNewsLoadingImg = (ImageView) view.findViewById(R.id.mNewsLoadingImg);
        mNewsLoadingImg.setImageResource(R.drawable.list_news_progress_animation);
        mAniNewsLoading = (AnimationDrawable) mNewsLoadingImg.getDrawable();
        mAniNewsLoading.start();
        mNewsDetailLoadingWrapper = view.findViewById(R.id.mNewsDetailLoadingWrapper);
        mNewsDetailCilckRefresh = view.findViewById(R.id.mNewsDetailCilckRefresh);
        mNewsDetailCilckRefresh.setOnClickListener(this);


        if (NetUtil.checkNetWork(getActivity())) {
            //有网络的时候
            mNewsDetailCilckRefresh.setVisibility(View.GONE);
            mNewsDetailLoadingWrapper.setVisibility(View.VISIBLE);
            boolean isLogin = UmengShareHelper.isAuthenticated(getActivity(), SHARE_MEDIA.SINA);
            if (isLogin) {
                //如果用户在首订的时候登陆了，就进入36°C，否则进入40°C
                GlobalParams.currentPos = 2;
                GlobalParams.previousPos = 2;
            } else {
                GlobalParams.currentPos = 3;
                GlobalParams.previousPos = 3;
            }
            if (MaterialNavigationDrawer.mCommonHeaderTitle != null) {
                MaterialNavigationDrawer.mCommonHeaderTitle.setText(getResources().getTextArray(R.array.mFeedChannleNames)[GlobalParams.currentPos]);
            }
            loadNewsData(getActivity(), GlobalParams.currentPos);
        } else if (DBHelper.queryRootIsCache(GlobalParams.currentPos)) {
            loadNewsData(getActivity(), GlobalParams.currentPos);
        } else {
            //没有网络的时候
            mNewsDetailCilckRefresh.setVisibility(View.VISIBLE);
            mNewsDetailLoadingWrapper.setVisibility(View.GONE);
            ToastUtil.toastShort(R.string.network_break_prompt);
        }
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

                POINT_ONE_X = 80;
                POINT_ONE_Y = 370;
                POINT_TWO_X = 275;
                POINT_TWO_Y = 305;
                POINT_THREE_X = 350;
                POINT_THREE_Y = 170;
                POINT_FOUR_X = 610;
                POINT_FOUR_Y = 110;

                break;


            case 1280:
                if (width == 720) {
                    POINT_ONE_X = 60;
                    POINT_ONE_Y = 260;
                    POINT_TWO_X = 180;
                    POINT_TWO_Y = 210;
                    POINT_THREE_X = 240;
                    POINT_THREE_Y = 120;
                    POINT_FOUR_X = 400;
                    POINT_FOUR_Y = 80;
                } else if (width == 800) {//1280*800

                    POINT_ONE_X = 90;
                    POINT_ONE_Y = 260;
                    POINT_TWO_X = 205;
                    POINT_TWO_Y = 225;
                    POINT_THREE_X = 255;
                    POINT_THREE_Y = 130;
                    POINT_FOUR_X = 440;
                    POINT_FOUR_Y = 80;
                } else {

                    POINT_ONE_X = 60;
                    POINT_ONE_Y = 260;
                    POINT_TWO_X = 180;
                    POINT_TWO_Y = 210;
                    POINT_THREE_X = 240;
                    POINT_THREE_Y = 120;
                    POINT_FOUR_X = 400;
                    POINT_FOUR_Y = 80;
                }

                break;

            case 1184:

                POINT_ONE_X = 60;
                POINT_ONE_Y = 250;
                POINT_TWO_X = 175;
                POINT_TWO_Y = 215;
                POINT_THREE_X = 235;
                POINT_THREE_Y = 120;
                POINT_FOUR_X = 400;
                POINT_FOUR_Y = 75;

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
                POINT_ONE_Y = 170;
                POINT_TWO_X = 115;
                POINT_TWO_Y = 150;
                POINT_THREE_X = 160;
                POINT_THREE_Y = 70;
                POINT_FOUR_X = 260;
                POINT_FOUR_Y = 40;

                break;

        }
    }


    private void showView() {

//设置全局参数
        if (GlobalParams.view == null) {
            GlobalParams.view = (LinearLayout) View.inflate(getActivity(), R.layout.view_sun, null);
        }

        TextView iv = (TextView) GlobalParams.view.findViewById(R.id.iv_sun);
        iv.setBackgroundResource(R.drawable.sun);

        ((HomeAty) getActivity()).setGlobalView(GlobalParams.view);
        GlobalParams.mainSection = (HomeAty) getActivity();
        ((HomeAty) getActivity()).setGlobalFlag(true);
        GlobalParams.context = (HomeAty) getActivity();

        flag = true;
        iv_section.setBackgroundResource(R.drawable.ic_feed_animation_scene44);

        // 给view对象组成触摸的监听器
        GlobalParams.view.setOnTouchListener(new View.OnTouchListener() {
            // 手指在屏幕上第一次按下的初始坐标
            int startX;
            int startY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                first_flag = sp.getBoolean("first_flag", false);

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
                        mTips.setVisibility(View.GONE);
                        if (startX > 0 && startX <= width * SECTION_ONE / STANDARD_WIDTH) {
                            //view.scrollTo(50, 320);
                            GlobalParams.params.x = (int) (POINT_ONE_X);
                            GlobalParams.params.y = (int) (POINT_ONE_Y);

                            GlobalParams.mInitPos = GlobalParams.params.y;

                            //太阳变动位置时，修改自定义actionbar的title
                            if (MaterialNavigationDrawer.mCommonHeaderTitle != null) {
                                MaterialNavigationDrawer.mCommonHeaderTitle.setText(getResources().getTextArray(R.array.mFeedChannleNames)[0]);
                            }
                            iv_section.setBackgroundResource(R.drawable.ic_feed_animation_scene1);
                            GlobalParams.currentPos = 0;
                            MobclickAgent.onEvent(getActivity(), "sun_positon_1");
                        } else if (startX > width * SECTION_ONE / STANDARD_WIDTH && startX <= width * SECTION_TWO / STANDARD_WIDTH) {
                            GlobalParams.params.x = (int) (POINT_TWO_X);
                            GlobalParams.params.y = (int) (POINT_TWO_Y);

                            GlobalParams.mInitPos = GlobalParams.params.y;

                            if (MaterialNavigationDrawer.mCommonHeaderTitle != null) {
                                MaterialNavigationDrawer.mCommonHeaderTitle.setText(getResources().getTextArray(R.array.mFeedChannleNames)[1]);
                            }
                            iv_section.setBackgroundResource(R.drawable.ic_feed_animation_scene2);
                            GlobalParams.currentPos = 1;
                            MobclickAgent.onEvent(getActivity(), "sun_positon_2");
                        } else if (startX > width * SECTION_TWO / STANDARD_WIDTH && startX <= width * SECTION_THREE / STANDARD_WIDTH) {
                            GlobalParams.params.x = (int) (POINT_THREE_X);
                            GlobalParams.params.y = (int) (POINT_THREE_Y);

                            GlobalParams.mInitPos = GlobalParams.params.y;

                            if (MaterialNavigationDrawer.mCommonHeaderTitle != null) {
                                MaterialNavigationDrawer.mCommonHeaderTitle.setText(getResources().getTextArray(R.array.mFeedChannleNames)[2]);
                            }

                            iv_section.setBackgroundResource(R.drawable.ic_feed_animation_scene3);
                            MobclickAgent.onEvent(getActivity(), "sun_positon_3");
                            GlobalParams.currentPos = 2;

                        } else if (startX > width * SECTION_THREE / STANDARD_WIDTH && startX <= width) {
                            GlobalParams.params.x = (int) (POINT_FOUR_X);
                            GlobalParams.params.y = (int) (POINT_FOUR_Y);

                            GlobalParams.mInitPos = GlobalParams.params.y;

                            if (MaterialNavigationDrawer.mCommonHeaderTitle != null) {
                                MaterialNavigationDrawer.mCommonHeaderTitle.setText(getResources().getTextArray(R.array.mFeedChannleNames)[3]);
                            }
                            iv_section.setBackgroundResource(R.drawable.ic_feed_animation_scene44);

                            GlobalParams.currentPos = 3;
                            MobclickAgent.onEvent(getActivity(), "sun_positon_4");
                        }

                        GlobalParams.manager.updateViewLayout(GlobalParams.view, GlobalParams.params);

                        //popupwindow消失
                        if (GlobalParams.popup != null && GlobalParams.popup.isShowing()) {
                            GlobalParams.popup.dismiss();
                        }

                        if (NetUtil.checkNetWork(getActivity())) {
                            //有网络的时候
                            if (GlobalParams.currentPos != GlobalParams.previousPos) {
                                GlobalParams.previousPos = GlobalParams.currentPos;
                                loadNewsData(getActivity(), GlobalParams.currentPos);
                            }
                        } else if (DBHelper.queryRootIsCache(GlobalParams.currentPos)) {
                            loadNewsData(getActivity(), GlobalParams.currentPos);
                        } else {
                            //没有网络的时候
                            mNewsDetailCilckRefresh.setVisibility(View.VISIBLE);
                            mNewsDetailLoadingWrapper.setVisibility(View.GONE);
                            ToastUtil.toastShort(R.string.network_break_prompt);
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

        GlobalParams.mInitPos = GlobalParams.params.y;
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
        if (!GlobalParams.ADD_SUN_FLAG) {
            GlobalParams.manager.addView(GlobalParams.view, GlobalParams.params);
            GlobalParams.ADD_SUN_FLAG = true;
        }

        if (GlobalParams.DELETE_FLAG) {
            GlobalParams.manager.addView(GlobalParams.view, GlobalParams.params);
            GlobalParams.DELETE_FLAG = false;
        }

    }

    private void loadNewsData(final Context mContext, final int newsModulePos) {

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
            public NewsFeed preRequest() {
                Long start = System.currentTimeMillis();
                NewsFeed _NewsFeed = DBHelper.queryByRootId(newsModulePos);
                Logger.e("consume time db query", System.currentTimeMillis() - start + "");
                return _NewsFeed;
            }

            @Override
            public NewsFeed postRequest(NewsFeed newsFeed) {
                Long start = System.currentTimeMillis();
                DBHelper.insert(newsFeed);
                Logger.e("consume time db insert", System.currentTimeMillis() - start + "");
                return super.postRequest(newsFeed);
            }

            @Override
            public void success(NewsFeed result) {
                Logger.i(">>>" + TAG, result.toString());
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
                    GlobalParams.view.setVisibility(View.VISIBLE);
                    GlobalParams.iv_orbit.setVisibility(View.GONE);

                    if (GlobalParams.view == null) {
                        initView();
                    } else if (GlobalParams.view.getVisibility() == View.GONE) {
                        GlobalParams.view.setVisibility(View.VISIBLE);
                    } else if (GlobalParams.view.getVisibility() == View.VISIBLE) {
                        GlobalParams.view.setVisibility(View.VISIBLE);
                    }

                    GlobalParams.REFRESH_FLAG = false;

                    if (!first_flag) {
                        if (GlobalParams.popup == null) {
                            GlobalParams.popup = new PopupWindow(getActivity());
                        }
                        View popView = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_mask, null);
                        GlobalParams.popup.setContentView(popView);
                        GlobalParams.popup.setFocusable(true);
                        DisplayMetrics metric = new DisplayMetrics();
                        GlobalParams.manager.getDefaultDisplay().getMetrics(metric);
                        int width = metric.widthPixels; // 屏幕宽度（像素）
                        int height = metric.heightPixels; // 屏幕高度（像素）
                        GlobalParams.popup.setWidth(Integer.parseInt(new DecimalFormat("0").format(width)));
                        GlobalParams.popup.setHeight(Integer.parseInt(new DecimalFormat("0").format(height)));
                        ColorDrawable dw = new ColorDrawable(0xb0000000);
                        //设置SelectPicPopupWindow弹出窗体的背景
                        GlobalParams.popup.setBackgroundDrawable(dw);
                        GlobalParams.popup.showAtLocation(view, Gravity.CENTER, 0, 0);

                        SharedPreferences.Editor editor = sp.edit();
                        editor.putBoolean("first_flag", true);
                        editor.commit();
                    }

                } else {
                    ToastUtil.toastShort(R.string.network_break_prompt);
                }

            }

            public void failed(MyAppException exception) {
                Logger.i(">>>" + TAG, exception.getMessage());
            }
        }.setReturnType(new TypeToken<NewsFeed>() {
        }.getType()));
        request.execute();
    }

    @Override
    public void onStop() {

        if (GlobalParams.view != null) {
            GlobalParams.manager.removeView(GlobalParams.view);
            GlobalParams.view = null;
        }

        GlobalParams.ADD_SUN_FLAG = false;

        super.onStop();
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!GlobalParams.ADD_SUN_FLAG) {
            initView();
        }

        if (GlobalParams.SUN_FLAG == false) {
            GlobalParams.view.setVisibility(View.GONE);
        }

    }
}
