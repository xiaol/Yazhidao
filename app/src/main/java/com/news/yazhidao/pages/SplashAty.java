package com.news.yazhidao.pages;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;

import com.news.yazhidao.R;
import com.news.yazhidao.constant.CommonConstant;
import com.news.yazhidao.utils.DeviceInfoUtil;
import com.news.yazhidao.utils.helper.SettingHelper;

/**
 * Created by Berkeley on 2/5/15.
 */
public class SplashAty extends Activity {

    private Intent intent;
    private LinearLayout ll_splash;
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:

                    loadMainUI();
                    break;
            }
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.aty_splash);
        ll_splash = (LinearLayout) findViewById(R.id.ll_splash);

        handler.postDelayed(new  Runnable() {
            @Override
            public void run() {
                loadMainUI();
            }
        }, 2000);
    }

    protected void loadMainUI() {
        long _GuideVersion=SettingHelper.getLong(CommonConstant.UserInfoConstant.SETTING_FILE,CommonConstant.KEY_APP_GUIDE_VERSION);
        if (_GuideVersion!=0&&_GuideVersion>= DeviceInfoUtil.getApkVersionCode(this)) {
            intent = new Intent(SplashAty.this, HomeAty.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.animation_alpha_in, R.anim.animation_alpha_out);
        } else {
            intent = new Intent(SplashAty.this, GuideAty.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.animation_alpha_in, R.anim.animation_alpha_out);
        }
    }
}
