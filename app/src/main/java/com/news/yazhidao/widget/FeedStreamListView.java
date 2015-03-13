package com.news.yazhidao.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.widget.ListView;

import com.news.yazhidao.constant.GlobalParams;
import com.news.yazhidao.pages.user.FeedStreamFgt;

public class FeedStreamListView extends ListView {

    private int mMostRecentY;
    private int mTouchSlop;

	public FeedStreamListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public FeedStreamListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
	}

	public FeedStreamListView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
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
                    int dy = currentY - mMostRecentY;
                    int scrollY = (getScrollY() + dy);
                    GlobalParams.params.y += scrollY;
                    // 重复第一步的操作 ，重新初始化手指的开始位置。

                    if(GlobalParams.params.y < GlobalParams.mInitPos - 80){
                        GlobalParams.view.setVisibility(View.GONE);
                        GlobalParams.SUN_FLAG = false;

                    }else if(GlobalParams.params.y > GlobalParams.mInitPos){
                        switch (GlobalParams.currentPos){
                            case 0:
                                GlobalParams.params.y = FeedStreamFgt.POINT_ONE_Y;
                                break;

                            case 1:
                                GlobalParams.params.y = FeedStreamFgt.POINT_TWO_Y;
                                break;

                            case 2:
                                GlobalParams.params.y = FeedStreamFgt.POINT_THREE_Y;

                                break;

                            case 3:
                                GlobalParams.params.y = FeedStreamFgt.POINT_FOUR_Y;
                                break;
                        }


                        GlobalParams.view.setVisibility(View.VISIBLE);
                        GlobalParams.SUN_FLAG = true;
//                        GlobalParams.view.updateViewLayout(GlobalParams.view,GlobalParams.params);
                    }

                    mMostRecentY = currentY;

                    break;
                case MotionEvent.ACTION_UP:

                    break;
                //case MotionEvent.
                default:
                    break;
            }

        }
        return super.onTouchEvent(event);
    }

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
    }

	
}
