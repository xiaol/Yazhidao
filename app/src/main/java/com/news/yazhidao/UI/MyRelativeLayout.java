package com.news.yazhidao.UI;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.news.yazhidao.R;
import com.news.yazhidao.constant.GlobalParams;
import com.news.yazhidao.utils.DensityUtil;

public class MyRelativeLayout extends RelativeLayout {

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
    private static int SUN_WIDTH = 180;
    private static final String TAG = "SlideMenu";
    private int currentPos = 0;
    private int error = 150;
    private int mMostRecentY;
    private int delta;
    private int width;
    private int height;
    private boolean image_flag = false;
    private boolean listview_flag = false;
    private Scroller scroller;
    private Context context;
    private int mTouchSlop;
    private MyListView listView;
    private ImageView iv_section;
    private ImageView iv_orbit;
    private boolean sun_flag;
    private ImageView iv_sun;
    private int margin;
    private final int ANIMATION_SCREEN = 1;
    private final int MAIN_SCREEN = 2;
    private int currentScreen = MAIN_SCREEN;
    private boolean flag = false;
    private int view_height = 0;

    public MyRelativeLayout(Context context) {
        super(context);
        this.context = context;
        scroller = new Scroller(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public MyRelativeLayout(Context context, AttributeSet attrs) {

        //scroller = new Scroller(context);
        super(context, attrs);
        this.context = context;
        scroller = new Scroller(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public MyRelativeLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        scroller = new Scroller(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        GlobalParams.manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);

        width = GlobalParams.manager.getDefaultDisplay().getWidth();
        height = GlobalParams.manager.getDefaultDisplay().getHeight();

        margin = DensityUtil.dip2px(context, 10);

        setError();

        FrameLayout layout = (FrameLayout) this.getChildAt(0);
        iv_section = (ImageView)layout.findViewById(R.id.iv_section);

        view_height = SECTION_VIEW_HEIGHT * width / STANDARD_WIDTH;
        layout.layout(0, 0, width, view_height);

        ImageView shadow = (ImageView) this.getChildAt(1);
        shadow.layout(0, view_height - 3, width, view_height + 10);

        listView = (MyListView) this.getChildAt(2);

        if (listView.getMeasuredHeight() != 0) {
            GlobalParams.LISTVIEW_HEIGHT = listView.getMeasuredHeight() + GlobalParams.LISTVIEW_ERROR;
        }

        listView.layout(0, view_height + margin, width, view_height + GlobalParams.LISTVIEW_HEIGHT + margin);

    }

    private void setError() {

        switch (height) {

            case 1920:
                error = 280;
                break;

            case 1800:
                error = 360;
                break;

            case 1776:
                error = 280;
                break;

            case 1280:
                error = 190;
                break;

            case 1184:
                error = 190;
                break;

            case 800:
                error = 130;
                break;

            case 854:
                error = 130;
                break;
        }
    }

    private void setImageBackground() {

        switch (GlobalParams.currentPos) {

            case 0:
                iv_section.setBackgroundResource(R.drawable.section11);
                break;
            case 1:
                iv_section.setBackgroundResource(R.drawable.section22);
                break;
            case 2:
                iv_section.setBackgroundResource(R.drawable.section33);
                break;
            case 3:
                iv_section.setBackgroundResource(R.drawable.section44);
                break;

        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(!GlobalParams.REFRESH_FLAG){
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mMostRecentY = (int) event.getY();
                    break;
                case MotionEvent.ACTION_MOVE:

                    int currentY = (int) event.getY();
                    int delta = 0;
                    if (GlobalParams.SUN_FLAG) {
                        delta = mMostRecentY - currentY;
                    } else {
                        delta = (int) ((mMostRecentY - currentY) * 2);
                    }

                    int scrollY = (getScrollY() + delta);

                    int bottom = GlobalParams.LISTVIEW_HEIGHT + SECTION_VIEW_HEIGHT * width / STANDARD_WIDTH - height;

                    int visibility = GlobalParams.view.getVisibility();

                    if (scrollY < 0) {
                        scrollTo(0, 0);

                        GlobalParams.iv_orbit.setVisibility(View.VISIBLE);

                        AlphaAnimation mAnimation = new AlphaAnimation(0.0f, 1.0f);

                        mAnimation.setDuration(1000);

                        mAnimation.setAnimationListener(new Animation.AnimationListener() {
                            @Override
                            public void onAnimationStart(Animation animation) {

                            }

                            @Override
                            public void onAnimationEnd(Animation animation) {
                                AlphaAnimation mBigAnimation =  new AlphaAnimation(1.0f,0.0f);

                                mBigAnimation.setDuration(1000);

                                GlobalParams.iv_orbit.startAnimation(mBigAnimation);
                            }

                            @Override
                            public void onAnimationRepeat(Animation animation) {

                            }
                        });

                        GlobalParams.iv_orbit.startAnimation(mAnimation);

                    } else if (scrollY > bottom + error) {
                        if(GlobalParams.ONE_FLAG){

                            if(delta < 0){
                                scrollBy(0, delta);
                            }
                        }else {
                            scrollTo(0, bottom + error);
                        }

                        if (visibility == View.VISIBLE) {
                            GlobalParams.view.setVisibility(View.GONE);
                        }

                    } else {        // 姝ｅ父绉诲姩
                        scrollBy(0, delta);

                        if (visibility == View.VISIBLE) {
                            GlobalParams.view.setVisibility(View.GONE);
                        }
                    }

                    mMostRecentY = currentY;

                    break;
                case MotionEvent.ACTION_UP:

                    int view_height = SECTION_VIEW_HEIGHT * width / STANDARD_WIDTH;
                    // 鑿滃崟鐨勪腑蹇冪偣
                    int menuCenter = view_height / 2;

                    int _y = getScrollY();


                    if (_y < view_height && _y > 0) {
                        if (GlobalParams.SUN_FLAG) {
                            scrollTo(0, view_height);
                            currentScreen = MAIN_SCREEN;

                            GlobalParams.SUN_FLAG = false;
                            GlobalParams.mainSection.setGlobalFlag(false);//太阳隐藏的时候
                        } else{
                            scrollTo(0, 0);
                            currentScreen = ANIMATION_SCREEN;

                            if (GlobalParams.view.getVisibility() == View.GONE) {
                                GlobalParams.view.setVisibility(View.VISIBLE);
                            }

                            GlobalParams.SUN_FLAG = true;
                            GlobalParams.mainSection.setGlobalFlag(true);//太阳展示的时候
                        }

                    }else if(_y == 0){

                        scrollTo(0, 0);
                        currentScreen = ANIMATION_SCREEN;

                        if (GlobalParams.view.getVisibility() == View.GONE) {
                            GlobalParams.view.setVisibility(View.VISIBLE);
                        }

                        GlobalParams.SUN_FLAG = true;
                        GlobalParams.mainSection.setGlobalFlag(true);//太阳展示的时候

                    }

                    GlobalParams.iv_orbit.setVisibility(View.GONE);

                    break;
                //case MotionEvent.
                default:
                    break;
            }

        }
        return super.onTouchEvent(event);
    }


    /**
     * 鐢ㄤ簬鎷︽埅浜嬩欢
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mMostRecentY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) ev.getY();

                int diff = moveY - mMostRecentY;
                if (Math.abs(diff) > mTouchSlop) {
                    return true;
                }

                break;

            case MotionEvent.ACTION_UP:

                int aaa = (int) ev.getY();

                break;
            default:
                break;
        }
       return super.onInterceptTouchEvent(ev);
        //return true;
    }

    @Override
    public void computeScroll() {

        if (scroller.computeScrollOffset()) {        // 杩斿洖true浠ｈ〃姝ｅ湪妯℃嫙鏁版嵁涓�, false 宸茬粡鍋滄妯℃嫙鏁版嵁

            scrollTo(0, getScrollY());        // 鏇存柊浜唜杞寸殑鍋忕Щ閲�

            invalidate();
        }
    }

    private void switchScreen() {

        int view_height = SECTION_VIEW_HEIGHT * width / STANDARD_WIDTH;

        int startY = getScrollY();
        int dy = 0;

        if (currentScreen == MAIN_SCREEN) {
            dy = 0 - startY;
        } else if (currentScreen == ANIMATION_SCREEN) {
            dy = view_height - startY;
        }

        scroller.startScroll(0, startY, 0, dy, Math.abs(dy) * 5);

        invalidate();  // invalidate -> drawChild -> child.draw -> computeScroll
    }

}
