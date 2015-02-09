package com.news.yazhidao.pages;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.news.yazhidao.MyActivity.SignActivity;
import com.news.yazhidao.MyFragment.KitkatStatusBar;
import com.news.yazhidao.R;
import com.news.yazhidao.common.BaseActivity;
import com.news.yazhidao.utils.helper.UmengShareHelper;
import com.umeng.socialize.bean.SHARE_MEDIA;

/**
 * Created by fengjigang on 15/2/4.
 */
public class UserLoginAty extends BaseActivity implements View.OnClickListener {
    public static final String ACTION_USER_LOGIN="com.news.yazhidao.ACTION_USER_LOGIN";
    private Button mUserLoginBtn;
    private BroRecUserLogin mBroRecUserLogin=new BroRecUserLogin();
    private Button mUserLoginSkip;

    private class BroRecUserLogin extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(ACTION_USER_LOGIN.equals(intent.getAction())){
            UserLoginAty.this.finish();
            startActivity(new Intent(UserLoginAty.this, SignActivity.class));
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_user_login);
        mUserLoginBtn=(Button)findViewById(R.id.mUserLoginBtn);
        mUserLoginBtn.setOnClickListener(this);
        mUserLoginSkip=(Button)findViewById(R.id.mUserLoginSkip);
        mUserLoginSkip.setOnClickListener(this);

//        boolean isLogin = UmengShareHelper.isAuthenticated(this, SHARE_MEDIA.SINA);
//        if(isLogin){
//            UserLoginAty.this.finish();
//            startActivity(new Intent(UserLoginAty.this, SignActivity.class));
//        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter=new IntentFilter();
        filter.addAction(ACTION_USER_LOGIN);
        registerReceiver(mBroRecUserLogin,filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroRecUserLogin);
    }

    @Override
    public void onClick(View v) {
        int viewId=v.getId();
        if(viewId==R.id.mUserLoginBtn){
            boolean isLogin = UmengShareHelper.isAuthenticated(this, SHARE_MEDIA.SINA);
            if(isLogin){
                UserLoginAty.this.finish();
                startActivity(new Intent(UserLoginAty.this, SignActivity.class));
            }else{
                UmengShareHelper.oAuthSina(this,null);
            }
        }else{
            UserLoginAty.this.finish();
            startActivity(new Intent(this, KitkatStatusBar.class));
        }
    }
}
