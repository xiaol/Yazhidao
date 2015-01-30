package com.news.yazhidao.MyFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.news.yazhidao.GlobalParams;
import com.news.yazhidao.R;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;

/**
 * Created by neokree on 18/01/15.
 */
public class CustomDrawerHeader extends MaterialNavigationDrawer {

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

        // create sections
//        this.addSection(newSection("Section 1", new FragmentIndex()));
//        this.addSection(newSection("Section 2",new FragmentIndex()));

//        this.addSection(newSection("下载视频", R.drawable.xiazai,new FragmentButton()).setSectionColor(Color.parseColor("#FF7F66")));
//        this.addSection(newSection("最近观看", R.drawable.guankan,new FragmentButton()).setSectionColor(Color.parseColor("#FF7F66")));
//        this.addSection(newSection("订阅专题", R.drawable.jilu,new FragmentButton()).setSectionColor(Color.parseColor("#FF7F66")));
//        this.addSection(newSection("我的收藏", R.drawable.shouc,new FragmentButton()).setSectionColor(Color.parseColor("#FF7F66")));
//        this.addSection(newSection("我的36", R.drawable.my36,new FragmentButton()).setSectionColor(Color.parseColor("#FF7F66")));
//        this.addSection(newSection("设置", R.drawable.shez,new FragmentButton()).setSectionColor(Color.parseColor("#FF7F66")));
//        this.addSection(newSection("反馈", R.drawable.fangui,new FragmentButton()).setSectionColor(Color.parseColor("#FF7F66")));

        this.addSection(newSection("我的36°", R.drawable.my36,new FragmentButton()).setSectionColor(Color.parseColor("#FF7F66")));
        this.addSection(newSection("设置", R.drawable.shez,new FragmentButton()).setSectionColor(Color.parseColor("#FF7F66")));
        this.addSection(newSection("关于", R.drawable.guanyu,new FragmentButton()).setSectionColor(Color.parseColor("#FF7F66")));
        this.addSection(newSection("反馈", R.drawable.fangui,new FragmentButton()).setSectionColor(Color.parseColor("#FF7F66")));

        // create bottom section
        //this.addBottomSection(newSection("Bottom Section",R.drawable.ic_settings_black_24dp,new Intent(this,Settings.class)));

        this.disableLearningPattern();

    }
}
