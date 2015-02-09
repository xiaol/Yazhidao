package com.news.yazhidao.MyActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.LinearLayout;

import com.news.yazhidao.R;
import com.news.yazhidao.pages.UserLoginAty;

/**
 * Created by Berkeley on 2/5/15.
 */
public class SplashActivity extends Activity {

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

        setContentView(R.layout.activity_splash);
        ll_splash = (LinearLayout) findViewById(R.id.ll_splash);

        handler.postDelayed(new  Runnable() {
            @Override
            public void run() {
                loadMainUI();
            }
        }, 2000);
    }

    protected void loadMainUI() {
        SharedPreferences sp = getSharedPreferences("guide", 0);
        Boolean flag = sp.getBoolean("isguide", false);

        if (flag) {
            intent = new Intent(SplashActivity.this, UserLoginAty.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.animation_alpha_in, R.anim.animation_alpha_out);
        } else {
            intent = new Intent(SplashActivity.this, GuidingPage1.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.animation_alpha_in, R.anim.animation_alpha_out);
        }
    }
}
