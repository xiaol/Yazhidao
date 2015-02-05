package com.news.yazhidao.MyActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.news.yazhidao.R;

/**
 * Created by Berkeley on 2/5/15.
 */
public class GuidingPage1 extends Activity {

    private LinearLayout ll_guidingpage1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.guidingpage1);

        ll_guidingpage1 = (LinearLayout) findViewById(R.id.ll_guidingpage1);
        ll_guidingpage1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GuidingPage1.this,GuidingPage2.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.animation_in,R.anim.animation_out);
            }
        });


    }
}
