package com.news.yazhidao.UI;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.news.yazhidao.GlobalParams;
import com.news.yazhidao.R;
import com.news.yazhidao.constant.CommonConstant;
import com.news.yazhidao.utils.Logger;

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
    private static int SECTION_ONE = 260;
    private static int SECTION_TWO = 380;
    private static int SECTION_THREE = 480;
    private static int SUN_WIDTH = 180;
    private static final String TAG = "SlideMenu";
    private int currentPos = 0;
    private static int LISTVIEW_HEIGHT = 0;
    private int error = 150;
    private int mMostRecentY;
    private int delta;
    private int width;
    private int height;
    private Scroller scroller;
    private Context context;
    private int mTouchSlop;
    private ListView listView;
    private ImageView iv_section;
    private ImageView iv_sun;
    private final int ANIMATION_SCREEN = 1;	// 鍔ㄧ敾鐣岄潰
    private final int MAIN_SCREEN = 2;	// 涓荤晫闈�
    private int currentScreen = MAIN_SCREEN;		// 褰撳墠鐨勫睆骞曟樉绀虹晫闈�, 榛樿涓轰富鐣岄潰

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
        

        iv_section = (ImageView) this.getChildAt(0);
        int view_height = SECTION_VIEW_HEIGHT * width / STANDARD_WIDTH;
        iv_section.layout(0,0,width,view_height);

        listView = (ListView) this.getChildAt(1);
        LISTVIEW_HEIGHT = listView.getMeasuredHeight();

        listView.layout(0, view_height, width, LISTVIEW_HEIGHT + view_height);
        
        if(GlobalParams.view == null){
        	showView();
        }
    }
    
    private void showView() {
    	
		GlobalParams.view = (LinearLayout) View.inflate(context, R.layout.view_sun, null);
    	
		TextView iv = (TextView) GlobalParams.view.findViewById(R.id.iv_sun);
		iv.setBackgroundResource(R.drawable.sun);

		// 给view对象组成触摸的监听器
		GlobalParams.view.setOnTouchListener(new OnTouchListener() {
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

					GlobalParams.manager.updateViewLayout(GlobalParams.view,GlobalParams.params);

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
						
						GlobalParams.params.x = (int)(POINT_ONE_X * width  / STANDARD_WIDTH);
						GlobalParams.params.y = (int)(POINT_ONE_Y * width  / STANDARD_WIDTH);
						
						iv_section.setBackgroundResource(R.drawable.section1);
                        currentPos = 0;
						
						//END_WINDOW = WINDOW_ONE;

					} else if (startX > width * SECTION_ONE  / STANDARD_WIDTH && startX <= width * SECTION_TWO  / STANDARD_WIDTH) {
						GlobalParams.params.x = (int)(POINT_TWO_X * width  / STANDARD_WIDTH);
						GlobalParams.params.y = (int)(POINT_TWO_Y * width / STANDARD_WIDTH);
						
						iv_section.setBackgroundResource(R.drawable.section2);
                        currentPos = 1;


					} else if (startX > width * SECTION_TWO  / STANDARD_WIDTH && startX <= width * SECTION_THREE  / STANDARD_WIDTH) {
						GlobalParams.params.x = (int)(POINT_THREE_X * width  / STANDARD_WIDTH);
						GlobalParams.params.y = (int)(POINT_THREE_Y * width  / STANDARD_WIDTH);
						
						iv_section.setBackgroundResource(R.drawable.section3);
                        currentPos = 2;

					} else if (startX > width * SECTION_THREE  / STANDARD_WIDTH && startX <= width) {
						GlobalParams.params.x = (int)(POINT_FOUR_X * width  / STANDARD_WIDTH);
						GlobalParams.params.y = (int)(POINT_FOUR_Y * width  / STANDARD_WIDTH);
						
						iv_section.setBackgroundResource(R.drawable.section4);
                        currentPos = 3;

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
		GlobalParams.params.x = (int)(POINT_ONE_X * width / STANDARD_WIDTH);
		//GlobalParams.params.x = 75;
		// 指定距离屏幕上边的距离 必须与 Gravity.TOP同时使用
		GlobalParams.params.y = POINT_ONE_Y * width / STANDARD_WIDTH;
		
		// 土司的宽高
		GlobalParams.params.height = (int)(SUN_WIDTH * width  / STANDARD_WIDTH);
		//GlobalParams.params.height = 270;
		GlobalParams.params.width = (int)(SUN_WIDTH * width  / STANDARD_WIDTH);
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
                intent.putExtra(CommonConstant.KEY_NEWS_MODULE_POSITION, currentPos);
                context.sendBroadcast(intent);
            }
        }, 500);
        Logger.i(TAG, ">>currentPos=" + currentPos);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mMostRecentY = (int) event.getY();
                break;
            case MotionEvent.ACTION_MOVE:

                int currentY = (int) event.getY();

                int delta = mMostRecentY - currentY;

                int scrollY = getScrollY() + delta;

                int bottom = LISTVIEW_HEIGHT + SECTION_VIEW_HEIGHT * width / STANDARD_WIDTH - height;

                if(scrollY < 0) {		// 褰撳墠瓒呭嚭浜嗕笂杈圭晫
                    scrollTo(0, 0);	// 婊氬姩鍒拌彍鍗曠殑涓婅竟鐣�
                } else if(scrollY > bottom + error) {		// 瓒呭嚭浜嗕笅杈圭晫
                    scrollTo(0, bottom +error);		// 婊氬姩鍒颁富鐣岄潰鐨勪笅杈圭晫
                } else {		// 姝ｅ父绉诲姩
                    scrollBy(0,delta);
                }

                mMostRecentY = currentY;

                int visibility = GlobalParams.view.getVisibility();

                if(visibility == View.VISIBLE){
                    GlobalParams.view.setVisibility(View.GONE);
                }

                break;
            case MotionEvent.ACTION_UP:


                int view_height = SECTION_VIEW_HEIGHT * width / STANDARD_WIDTH;
                // 鑿滃崟鐨勪腑蹇冪偣
                int menuCenter = view_height/2;

                // 鑾峰緱褰撳墠灞忓箷x杞寸殑鍋忕Щ閲�
                int _y = getScrollY(); 	// x杞村綋鍓嶇殑鍋忕Щ閲�

                if(_y > menuCenter && _y < view_height) {		// 鍒囨崲鍒颁富鐣岄潰
                    scrollTo(0, view_height);
                    currentScreen = MAIN_SCREEN;
                } else if(_y < menuCenter){	// 鍒囨崲鍒拌彍鍗曠晫闈�
                    scrollTo(0, 0);
                    currentScreen = ANIMATION_SCREEN;

                    if(GlobalParams.view.getVisibility() == View.GONE){
                        GlobalParams.view.setVisibility(View.VISIBLE);
                    }

                }

                break;
            //case MotionEvent.
            default:
                break;
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
                if(Math.abs(diff) > mTouchSlop) {
                    return true;		// 璁や负鏄í鍚戠Щ鍔ㄧ殑娑堣�楁帀姝や簨浠�
                }

                break;
//            case MotionEvent.ACTION_UP:
//                onF
            default:
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public void computeScroll() {
        // 鏇存柊褰撳墠灞忓箷鐨剎杞寸殑鍋忕Щ閲�

        if(scroller.computeScrollOffset()) {		// 杩斿洖true浠ｈ〃姝ｅ湪妯℃嫙鏁版嵁涓�, false 宸茬粡鍋滄妯℃嫙鏁版嵁
            scrollTo(0, getScrollY());		// 鏇存柊浜唜杞寸殑鍋忕Щ閲�

            invalidate();
        }
    }

    /**
     * 鏍规嵁currentScreen鍙橀噺鍒囨崲灞忓箷
     */
    private void switchScreen() {

        int view_height = SECTION_VIEW_HEIGHT * width / STANDARD_WIDTH;

        int startY = getScrollY();
        int dy = 0;

        if(currentScreen == MAIN_SCREEN) {
            dy = 0 - startY;
        } else if(currentScreen == ANIMATION_SCREEN) {
            dy = view_height - startY;
        }

        // 寮�濮嬫ā鎷熸暟鎹�
        scroller.startScroll(0, startY, 0, dy, Math.abs(dy) * 5);

        // 鍒锋柊鐣岄潰
        invalidate();  // invalidate -> drawChild -> child.draw -> computeScroll
    }

}
