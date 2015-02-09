package com.news.yazhidao.MyFragment;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.news.yazhidao.GlobalParams;
import com.news.yazhidao.R;
import com.news.yazhidao.entity.User;
import com.news.yazhidao.utils.helper.DrawableUtil;
import com.news.yazhidao.utils.helper.UmengShareHelper;
import com.news.yazhidao.utils.helper.UserDataManager;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.InputStream;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialAccount;

/**
 * Created by berkley on 20/01/15.
 */
public class KitkatStatusBar extends MaterialNavigationDrawer {

    private String username;
    private String profile;
    InputStream stream = null;

    protected View.OnClickListener listener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {

            if (!drawerTouchLocked) {
                // enter into account properties
                if (accountListener != null) {
                    accountListener.onAccountOpening(currentAccount);
                }

                // close drawer
                layout.closeDrawer(drawer);

                boolean isLogin = UmengShareHelper.isAuthenticated(getApplicationContext(), SHARE_MEDIA.SINA);
                if(isLogin){

                    //弹框 确认是否注销
                    Toast.makeText(getApplicationContext(),"已经登录",Toast.LENGTH_LONG).show();

                }else{
                    UmengShareHelper.oAuthSina(getApplicationContext(),null);
                }

            }
        }
    };


    @Override
    public void init(Bundle savedInstanceState) {

        User user = UserDataManager.readUser();
        if (user != null) {
            username = user.getScreenName();

            if(username == null){
                username = "未登录用户";
            }
            profile = user.getSinaProfileImageUrl();
        }

        // add accounts
        MaterialAccount account = new MaterialAccount(this.getResources(), username, "", R.drawable.icon144, R.drawable.bj);
        this.addAccount(account);

        if(profile != null && profile.length() > 0) {
            DrawableUtil.displayImage2Circle(getApplicationContext(), userphoto, profile);
        }

        setProfileListener(listener);
        userphoto.setOnClickListener(listener);

        //create sections
        GlobalParams.bar = this.getSupportActionBar();

        GlobalParams.section = newSection("主页", R.drawable.my36, new FragmentButton()).setSectionColor(Color.parseColor("#FF7F66"));

        this.addSection(GlobalParams.section);
        this.addSection(newSection("设置", R.drawable.shez, new SettingButton()).setSectionColor(Color.parseColor("#FF7F66")));
        this.addSection(newSection("关于YA知道", R.drawable.guanyu, new AboutButton()).setSectionColor(Color.parseColor("#FF7F66")));
        this.addSection(newSection("意见反馈", R.drawable.fangui, new FeedbackButton()).setSectionColor(Color.parseColor("#FF7F66")));
        //this.addSection(newSection("我的36°", R.drawable.my36, new FeedbackButton()).setSectionColor(Color.parseColor("#FF7F66")));

        // create bottom section
        //this.addBottomSection(newSection("Bottom Section",R.drawable.ic_settings_black_24dp,new Intent(this,Settings.class)));

        this.disableLearningPattern();
    }

}
