package com.news.yazhidao.MyFragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import com.news.yazhidao.GlobalParams;
import com.news.yazhidao.R;
import com.news.yazhidao.entity.User;
import com.news.yazhidao.utils.helper.UserDataManager;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialAccount;

/**
 * Created by berkley on 20/01/15.
 */
public class KitkatStatusBar extends MaterialNavigationDrawer {

    private String username;
    private String profile;
    InputStream stream = null;


    @Override
    public void init(Bundle savedInstanceState) {

        User user = UserDataManager.readUser();
        if (user != null) {
            username = user.getScreenName();
            profile = user.getSinaProfileImageUrl();
        }

        Bitmap image = BitmapFactory.decodeStream(loadInputstream(profile));

        // add accounts
        MaterialAccount account = new MaterialAccount(this.getResources(), username, "", R.drawable.profile, R.drawable.bj);
        this.addAccount(account);

        //create sections
        GlobalParams.bar = this.getSupportActionBar();

        GlobalParams.section = newSection("主页", R.drawable.my36, new FragmentButton()).setSectionColor(Color.parseColor("#FF7F66"));

        this.addSection(GlobalParams.section);
        this.addSection(newSection("设置", R.drawable.shez, new SettingButton()).setSectionColor(Color.parseColor("#FF7F66")));
        this.addSection(newSection("关于", R.drawable.guanyu, new AboutButton()).setSectionColor(Color.parseColor("#FF7F66")));
        this.addSection(newSection("反馈", R.drawable.fangui, new FeedbackButton()).setSectionColor(Color.parseColor("#FF7F66")));
        this.addSection(newSection("我的36°", R.drawable.my36, new FeedbackButton()).setSectionColor(Color.parseColor("#FF7F66")));

        // create bottom section
        //this.addBottomSection(newSection("Bottom Section",R.drawable.ic_settings_black_24dp,new Intent(this,Settings.class)));

        this.disableLearningPattern();
    }


    private InputStream loadInputstream(final String urladdr) {
        new Thread(

                new Runnable() {
                    @Override
                    public void run() {

                        try {

                            URL url = new URL(urladdr);
                            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("GET");
                            conn.setConnectTimeout(5000);
                            stream = (InputStream) conn.getInputStream();

                        } catch (Exception e) {
                            System.out.println(e.getMessage().toString());
                        }

                    }
                }

        ).start();


        return stream;
    }
}
