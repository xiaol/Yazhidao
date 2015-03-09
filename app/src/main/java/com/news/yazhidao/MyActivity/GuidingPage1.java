package com.news.yazhidao.MyActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.news.yazhidao.R;
import com.news.yazhidao.pages.UserLoginAty;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Berkeley on 2/5/15.
 */
public class GuidingPage1 extends Activity implements ViewPager.OnPageChangeListener {

    private LinearLayout ll_guidingpage1;
    private List<ImageView> imageViewList;
    private int previousEnabledPosition = 0;
    private ViewPager mViewPager;
    private boolean isStop = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.guidingpage1);

        ll_guidingpage1 = (LinearLayout) findViewById(R.id.ll_guidingpage1);

        mViewPager = (ViewPager) findViewById(R.id.viewpager);

        imageViewList = new ArrayList<ImageView>();

        int[] imageResIDs = {
                R.drawable.guidingpage1,
                R.drawable.guidingpage2,
        };

        ImageView iv;
        View view;
        LinearLayout.LayoutParams params;
        for (int id : imageResIDs) {
            iv = new ImageView(this);
            iv.setBackgroundResource(id);
            imageViewList.add(iv);

        }

        mViewPager.setAdapter(new MyAdapter());
        mViewPager.setOnPageChangeListener(this);

        mViewPager.setCurrentItem(0);

    }

    class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageViewList.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(imageViewList.get(position % imageViewList.size()));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {

            ImageView image = imageViewList.get(position % imageViewList.size());

            image.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (previousEnabledPosition == 1) {
                        SharedPreferences sp = getSharedPreferences("guide", 0);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putBoolean("isguide", true);
                        editor.commit();

                        Intent intent = new Intent(GuidingPage1.this, UserLoginAty.class);
                        startActivity(intent);
                        finish();

                        overridePendingTransition(R.anim.animation_in, R.anim.animation_out);
                    }

                    return false;
                }
            });

            container.addView(image);
            return imageViewList.get(position % imageViewList.size());
        }
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageSelected(int position) {

        int newPosition = position % imageViewList.size();

        previousEnabledPosition = newPosition;
    }
}
