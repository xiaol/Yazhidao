package com.news.yazhidao.MyActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;

import com.news.yazhidao.R;
import com.news.yazhidao.pages.UserLoginAty;

/**
 * Created by Berkeley on 2/5/15.
 */
public class SplashActivity extends Activity {

    private Intent intent;
    private LinearLayout ll_splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_splash);
        ll_splash = (LinearLayout) findViewById(R.id.ll_splash);

        SharedPreferences sp = getSharedPreferences("guide", 0);
        Boolean flag = sp.getBoolean("isguide", false);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (Exception e) {
                    System.out.println("aaaaa");
                }
            }
        });

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
