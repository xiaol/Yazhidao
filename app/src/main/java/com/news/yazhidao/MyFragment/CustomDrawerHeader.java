package com.news.yazhidao.MyFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.news.yazhidao.constant.GlobalParams;
import com.news.yazhidao.R;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;

/**
 * Created by berkley on 18/01/15.
 */
public class CustomDrawerHeader extends MaterialNavigationDrawer {

    @Override
    public void init(Bundle savedInstanceState) {

//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        // create and set the header
        View view = LayoutInflater.from(this).inflate(R.layout.custom_drawer,null);
        setDrawerHeaderCustom(view);

        GlobalParams.bar = this.getSupportActionBar();
        GlobalParams.bar.setIcon(R.drawable.my36);

        GlobalParams.section = newSection("主页", R.drawable.my36,new FragmentButton()).setSectionColor(Color.parseColor("#FF7F66"));
        this.addSection(GlobalParams.section);
        this.addSection(newSection("设置", R.drawable.shez,new SettingButton()).setSectionColor(Color.parseColor("#FF7F66")));
        this.addSection(newSection("关于", R.drawable.guanyu,new AboutButton()).setSectionColor(Color.parseColor("#FF7F66")));
        this.addSection(newSection("反馈", R.drawable.fangui,new FeedbackButton()).setSectionColor(Color.parseColor("#FF7F66")));
        this.addSection(newSection("我的36°", R.drawable.my36,new FeedbackButton()).setSectionColor(Color.parseColor("#FF7F66")));

        // create bottom section
        //this.addBottomSection(newSection("Bottom Section",R.drawable.ic_settings_black_24dp,new Intent(this,Settings.class)));

        this.disableLearningPattern();

    }

}
