package com.news.yazhidao.common;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.news.yazhidao.utils.helper.UmengShareHelper;
import com.umeng.socialize.sso.UMSsoHandler;

/**
 * Created by fengjigang on 15/2/1.
 */
public class BaseActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /**使用SSO授权必须添加如下代码 */
        UMSsoHandler ssoHandler = UmengShareHelper.mController.getConfig().getSsoHandler(requestCode) ;
        if(ssoHandler != null){
            ssoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }
}
