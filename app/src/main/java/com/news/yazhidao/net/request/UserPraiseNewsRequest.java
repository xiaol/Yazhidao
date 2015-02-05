package com.news.yazhidao.net.request;

import android.text.TextUtils;
import android.widget.Toast;

import com.news.yazhidao.application.YaZhiDaoApplication;
import com.news.yazhidao.constant.HttpConstant;
import com.news.yazhidao.net.MyAppException;
import com.news.yazhidao.net.NetworkRequest;
import com.news.yazhidao.net.StringCallback;
import com.news.yazhidao.utils.DeviceInfoUtil;
import com.news.yazhidao.utils.ToastUtil;

import java.util.HashMap;

/**
 * Created by fengjigang on 15/2/4.
 */
public class UserPraiseNewsRequest {
    public interface PraiseNewsCallback{
        void success();
        void failed();
    }
    public static void praiseNews(String mNewsId,boolean isFavor, final PraiseNewsCallback callback){
        NetworkRequest request=new NetworkRequest(HttpConstant.URL_PRAISE_NEWS, NetworkRequest.RequestMethod.GET);
        HashMap<String,Object> params=new HashMap<String, Object>();
        params.put("id",mNewsId);
        params.put("isFavor",false);
        params.put("uuid", DeviceInfoUtil.getUUID());
        request.getParams=params;
        request.setCallback(new StringCallback() {
            @Override
            public void success(String result) {
                if(!TextUtils.isEmpty(result)&&result.contains("200")){
                    if(callback!=null){
                        callback.success();
                    }
                }else{
                    if(callback!=null){
                        callback.failed();
                    }
                }
            }

            @Override
            public void failed(MyAppException exception) {
                if(callback!=null){
                    callback.failed();
                }
            }
        });
        request.execute();
    }
}
