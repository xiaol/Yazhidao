package com.news.yazhidao;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;

import com.news.yazhidao.MyFragment.CustomDrawerHeader;

public class SignActivity extends Activity {
	
	private LinearLayout ll_config;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		setContentView(R.layout.fra_book);
		
		
		ll_config = (LinearLayout) findViewById(R.id.ll_config);
		ll_config.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(SignActivity.this,CustomDrawerHeader.class);
				startActivity(intent);
			}
		});
	}
	
}
