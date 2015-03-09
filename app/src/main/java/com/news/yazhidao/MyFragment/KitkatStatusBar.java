package com.news.yazhidao.MyFragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.news.yazhidao.constant.GlobalParams;
import com.news.yazhidao.R;
import com.news.yazhidao.entity.User;
import com.news.yazhidao.utils.helper.DrawableUtil;
import com.news.yazhidao.utils.helper.UmengShareHelper;
import com.news.yazhidao.utils.helper.UserDataManager;
import com.news.yazhidao.widget.AlertDialogImpl;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.InputStream;

import it.neokree.materialnavigationdrawer.MaterialNavigationDrawer;
import it.neokree.materialnavigationdrawer.elements.MaterialAccount;

/**
 * Created by berkley on 20/01/15.
 */
public class KitkatStatusBar extends MaterialNavigationDrawer {
    public static final String ACTION_USER_LOGIN="com.news.yazhidao.ACTION_USER_LOGIN_IN_HOME";
    private MaterialAccount account;
    private String username;
    private String profile;
    InputStream stream = null;
    private BroRecUserLogin mBroRecUserLogin=new BroRecUserLogin();

    private class BroRecUserLogin extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if(ACTION_USER_LOGIN.equals(intent.getAction())){
                setSinaUserLogin();
            }
        }
    }
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
                    showLogoutDialog();

                    if(GlobalParams.view != null) {
                        GlobalParams.manager.removeView(GlobalParams.view);
                        GlobalParams.ADD_SUN_FLAG = false;
                    }

                }else{
                    UmengShareHelper.oAuthSina(KitkatStatusBar.this,null);

                    GlobalParams.ADD_SUN_FLAG = false;
                }

            }
        }
    };
    AlertDialogImpl dialog;
    private void showLogoutDialog() {
        dialog=new AlertDialogImpl.Builder(this).setTitle("退出登录").setMessage("确定要退出登陆吗？").setPositive("确定", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UmengShareHelper.logout(KitkatStatusBar.this);
                setSinaUserLogout();
                dialog.dismiss();

                if (GlobalParams.view != null) {
                    GlobalParams.manager.addView(GlobalParams.view, GlobalParams.params);
                    GlobalParams.ADD_SUN_FLAG = true;
                }
            }
        }).setNegative("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                if (GlobalParams.view != null) {
                    GlobalParams.manager.addView(GlobalParams.view, GlobalParams.params);
                    GlobalParams.ADD_SUN_FLAG = true;
                }
            }
        }).show();
        dialog.setCancelable(false);
    }

    private void setSinaUserLogout() {
        account.setTitle("立即登录");
        DrawableUtil.displayImage2Circle(getApplicationContext(),userphoto,R.drawable.icon144);
        notifyAccountDataChanged();
    }
    private void setSinaUserLogin() {
        account.setTitle(UserDataManager.readUser().getScreenName());
        notifyAccountDataChanged();
        String profile=UserDataManager.readUser().getSinaProfileImageUrl();
        if(profile != null && profile.length() > 0) {
            DrawableUtil.displayImage2Circle(getApplicationContext(), userphoto, profile);
        }
    }


    @Override
    public void init(Bundle savedInstanceState) {

        User user = UserDataManager.readUser();
        if (user != null) {
            username = user.getScreenName();

            if(username == null){
                username = "立即登录";
            }
            profile = user.getSinaProfileImageUrl();
        }

        // add accounts
        account = new MaterialAccount(this.getResources(), username, "", R.drawable.icon144, R.drawable.bj);
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
        IntentFilter filter=new IntentFilter();
        filter.addAction(ACTION_USER_LOGIN);
        this.registerReceiver(mBroRecUserLogin,filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.unregisterReceiver(mBroRecUserLogin);
    }
}
