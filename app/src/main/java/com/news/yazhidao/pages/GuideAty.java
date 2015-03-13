package com.news.yazhidao.pages;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.news.yazhidao.R;
import com.news.yazhidao.utils.CreateShut;
import com.news.yazhidao.utils.helper.UmengShareHelper;
import com.news.yazhidao.widget.RoundedImageView;
import com.news.yazhidao.widget.TextViewExtend;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;

//第一次运行的引导页代码
public class GuideAty extends Activity implements ViewPager.OnPageChangeListener,
        View.OnClickListener {

    private ViewPager m_pViewPager;
    private PagerAdapter m_pPagerAdapter;
    private TextViewExtend m_tvMain, m_tvSina, m_tvQQ, m_tvWeiXin;
    private LinearLayout m_pIndicatorLayout;
    private ArrayList<View> m_pViews;
    private RoundedImageView[] m_pIndicators = null;
    private int[] m_pImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        initVars();
        findViews();
        setListener();
    }

    private void initVars() {
        // 创建桌面快捷方式
        new CreateShut(this);
        // 设置引导图片
        //!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!  仅需在这设置图片 指示器和page自动添加
        m_pImages = new int[]{R.drawable.ic_guide_page1, R.drawable.ic_guide_page2,
                R.drawable.ic_guide_page3, R.drawable.ic_guide_page3};
        m_pViews = new ArrayList<View>();
                m_pIndicators = new RoundedImageView[m_pImages.length]; // 定义指示器数组大小
        m_pIndicators = new RoundedImageView[m_pImages.length]; // 定义指示器数组大小
        m_pIndicators = new RoundedImageView[m_pImages.length]; // 定义指示器数组大小
    }

    // 初始化视图
    private void findViews() {
        // 实例化视图控件
        m_pViewPager = (ViewPager) findViewById(R.id.viewpage);
        m_pIndicatorLayout = (LinearLayout) findViewById(R.id.indicator);
        m_tvMain = (TextViewExtend) findViewById(R.id.main_textview);
        m_tvSina = (TextViewExtend) findViewById(R.id.login_sina_textView);
        m_tvQQ = (TextViewExtend) findViewById(R.id.login_qq_textView);
        m_tvWeiXin = (TextViewExtend) findViewById(R.id.login_weixin_textView);
        for (int i = 0; i < m_pImages.length; i++) {
            // 循环加入图片
            ImageView imageView = new ImageView(this);
            m_pViews.add(imageView);
            imageView.setImageDrawable(getResources().getDrawable(m_pImages[i]));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            // 循环加入指示器
            m_pIndicators[i] = new RoundedImageView(this);
            m_pIndicators[i].setScaleType(ImageView.ScaleType.CENTER_CROP);

            m_pIndicators[i].setBorderWidth(3.0f);
            m_pIndicators[i].setOval(false);
            m_pIndicators[i].setCornerRadius(10.0f);
            m_pIndicatorLayout.addView(m_pIndicators[i]);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) m_pIndicators[i].getLayoutParams();
            lp.width = 20;
            lp.height = 20;
            lp.leftMargin = 15;
            lp.rightMargin = 15;
            m_pIndicators[i].setLayoutParams(lp);
            m_pIndicators[i].setBorderColor(getResources().getColor(R.color.news_list_table_channelname));
            if (i == 0) {/**/
//                m_pIndicators[i].setBorderColor(getResources().getColor(R.color.new_yellow));
                ColorDrawable dw = new ColorDrawable(getResources().getColor(R.color.white));
                m_pIndicators[i].setImageDrawable(dw);
            } else {
//                m_pIndicators[i].setBorderColor(getResources().getColor(R.color.new_yellow));
                ColorDrawable dw = new ColorDrawable(getResources().getColor(R.color.news_list_table_channelname));
                m_pIndicators[i].setImageDrawable(dw);
            }

        }
        m_pPagerAdapter = new GuideBasePagerAdapter(m_pViews);
        m_pViewPager.setAdapter(m_pPagerAdapter); // 设置适配器
    }

    private void setListener() {
        m_pViewPager.setOnPageChangeListener(this);
        m_tvMain.setOnClickListener(this);
        m_tvSina.setOnClickListener(this);
        m_tvQQ.setOnClickListener(this);
        m_tvWeiXin.setOnClickListener(this);
    }

    //按钮的点击事件
    @Override
    public void onClick(View v) {
        Intent intent;
        SharedPreferences sp = getSharedPreferences("guide", 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putBoolean("isguide", true);
        editor.commit();
        switch (v.getId()) {
            case R.id.main_textview:
                intent = new Intent(GuideAty.this, HomeAty.class);
                startActivity(intent);
                overridePendingTransition(R.anim.animation_in, R.anim.animation_out);
                this.finish();
                break;
            case R.id.login_sina_textView:
                boolean isLogin = UmengShareHelper.isAuthenticated(this, SHARE_MEDIA.SINA);
                if(isLogin){
                    this.finish();
                    startActivity(new Intent(GuideAty.this, SignAty.class));
                }else{
                    UmengShareHelper.oAuthSina(this,null);
                }
                overridePendingTransition(R.anim.animation_in, R.anim.animation_out);
                break;
            case R.id.login_qq_textView:
                break;
            case R.id.login_weixin_textView:
                break;
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

    // 监听viewpage
    @Override
    public void onPageSelected(int arg0) {
        // 显示最后一个图片时显示按钮
        if (arg0 == m_pIndicators.length - 1) {
            m_tvMain.setVisibility(View.VISIBLE);
            m_tvSina.setVisibility(View.VISIBLE);
            m_tvQQ.setVisibility(View.VISIBLE);
            m_tvWeiXin.setVisibility(View.VISIBLE);
        } else {
            m_tvMain.setVisibility(View.INVISIBLE);
            m_tvSina.setVisibility(View.INVISIBLE);
            m_tvQQ.setVisibility(View.INVISIBLE);
            m_tvWeiXin.setVisibility(View.INVISIBLE);
        }
        // 更改指示器图片
        for (int i = 0; i < m_pIndicators.length; i++) {
            if (arg0 != i) {
                Log.i("---", "LightGray" + i);
//                m_pIndicators[i].setBorderColor(getResources().getColor(R.color.LightGray));
                ColorDrawable dw1 = new ColorDrawable(getResources().getColor(R.color.news_list_table_channelname));
                m_pIndicators[i].setImageDrawable(dw1);
            } else {
                Log.i("---", "BGBlue" + i);
//                m_pIndicators[i].setBorderColor(getResources().getColor(R.color.new_yellow));
                ColorDrawable dw = new ColorDrawable(getResources().getColor(R.color.white));
                m_pIndicators[i].setImageDrawable(dw);
            }
        }
    }


    //引导页使用的pageview适配器
    public class GuideBasePagerAdapter extends PagerAdapter {
        private List<View> views;

        public GuideBasePagerAdapter(List<View> views) {
            this.views = views;
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            return views.size();
        }

        @Override
        public void destroyItem(View container, int position, Object object) {
            if (views.size() > position)
                ((ViewPager) container).removeView(views.get(position));
        }

        @Override
        public Object instantiateItem(View container, int position) {
            ((ViewPager) container).addView(views.get(position));
            return views.get(position);
        }
    }
}

