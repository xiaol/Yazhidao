package com.news.yazhidao.MyActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.news.yazhidao.R;
import com.news.yazhidao.pages.UserLoginAty;

/**
 * Created by Berkeley on 2/5/15.
 */
public class GuidingPage2 extends Activity {

    private LinearLayout ll_guidingpage2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.guidingpage2);

        ll_guidingpage2 = (LinearLayout) findViewById(R.id.ll_guidingpage2);
        ll_guidingpage2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sp = getSharedPreferences("guide",0);
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("isguide",true);
                editor.commit();

                Intent intent = new Intent(GuidingPage2.this,UserLoginAty.class);
                startActivity(intent);
                finish();

                overridePendingTransition(R.anim.animation_in,R.anim.animation_out);
            }
        });
    }
}
