package com.news.yazhidao.MyActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.news.yazhidao.MyFragment.KitkatStatusBar;
import com.news.yazhidao.R;
import com.news.yazhidao.constant.HttpConstant;
import com.news.yazhidao.net.NetworkRequest;

/**
 * Created by Berkeley on 2/1/15.
 */
public class SignActivity extends Activity {

    private GridView gv_channels;
    private MyAdapter adapter;
    private LinearLayout ll_config;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fra_book);

        gv_channels = (GridView) findViewById(R.id.gv_channels);

        gv_channels.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new MyAdapter(SignActivity.this);
        gv_channels.setAdapter(adapter);

        ll_config = (LinearLayout) findViewById(R.id.ll_config);
        ll_config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //loadNewsData("123","456",new String[]{"涨姿势"});
                Intent intent = new Intent(SignActivity.this, KitkatStatusBar.class);
                startActivity(intent);

            }
        });

    }

    //自定义适配器
    class MyAdapter extends BaseAdapter {
        //上下文对象
        private Context context;
        //图片数组
        private Integer[] imgs = {
                R.drawable.zhangzishi, R.drawable.aozaoxing, R.drawable.dongshenghuo, R.drawable.kaiyanjie
                , R.drawable.wanchufan, R.drawable.youqiangdiao
        };

        MyAdapter(Context context) {
            this.context = context;
        }

        public int getCount() {
            return imgs.length;
        }

        public Object getItem(int item) {
            return item;
        }

        public long getItemId(int id) {
            return id;
        }

        //创建View方法
        public View getView(int position, View convertView, ViewGroup parent) {

            View v = null;
            ImageView imageView = null;

            if (convertView == null) {
                v = View.inflate(SignActivity.this, R.layout.item_gridview, null);
                imageView = (ImageView) v.findViewById(R.id.iv_channel);
                imageView.setAdjustViewBounds(false);//设置边界对齐
            } else {
                v = convertView;
                imageView = (ImageView) convertView.findViewById(R.id.iv_channel);
            }
            imageView.setImageResource(imgs[position]);//为ImageView设置图片资源
            return v;
        }
    }

    private static void loadNewsData(String uuid,String sinaId, String[] tagStr) {

        String url = HttpConstant.URL_USER_LOGIN + "uuid=" + uuid + "&sinaId=" + sinaId;

        for(int i = 0; i < tagStr.length; i ++){
            url = url + "&tag=" + tagStr[i];
        }

        url = url + "?isNormal=0";
        NetworkRequest request = new NetworkRequest(url, NetworkRequest.RequestMethod.GET);
        //request.setCallback();
        request.execute();
    }
}