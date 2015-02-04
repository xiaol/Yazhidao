package com.news.yazhidao.MyFragment;

import android.graphics.Color;
import android.os.Bundle;

import com.news.yazhidao.GlobalParams;
import com.news.yazhidao.R;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialAccount;

/**
 * Created by neokree on 20/01/15.
 */
public class KitkatStatusBar extends MaterialNavigationDrawer {

    @Override
    public void init(Bundle savedInstanceState) {
        // add accounts
        MaterialAccount account = new MaterialAccount(this.getResources(),"Kristina","", R.drawable.profile, R.drawable.bj);
        this.addAccount(account);

        //create sections
        GlobalParams.bar = this.getSupportActionBar();

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
