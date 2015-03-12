package com.news.yazhidao.pages;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.reflect.TypeToken;
import com.news.yazhidao.R;
import com.news.yazhidao.constant.HttpConstant;
import com.news.yazhidao.entity.Image;
import com.news.yazhidao.entity.User;
import com.news.yazhidao.net.MyAppException;
import com.news.yazhidao.net.NetworkRequest;
import com.news.yazhidao.net.UserLoginCallBack;
import com.news.yazhidao.utils.helper.UserDataHelper;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Berkeley on 2/1/15.
 */
public class SignAty extends Activity {

    private LinkedList<Image> imageList = new LinkedList<Image>();
    private GridView gv_channels;
    private DonateGridViewAdapter adapter;
    private LinearLayout ll_config;
    private User user;
    private String uuid;
    private String sinaId;
    private String sinaToken;
    private String screenName;
    private String sinaProfileImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_sign);

        initImagelist();

        user = UserDataHelper.readUser();
        if (user != null) {
            uuid = user.getUuid();
            sinaId = user.getSinaId();
            sinaToken = user.getSinaToken();
            screenName = user.getScreenName();
            sinaProfileImageUrl = user.getSinaProfileImageUrl();
        }

        gv_channels = (GridView) findViewById(R.id.gv_channels);

        gv_channels.setSelector(new ColorDrawable(Color.TRANSPARENT));
        adapter = new DonateGridViewAdapter();
        gv_channels.setAdapter(adapter);
        gv_channels.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Image image = imageList.get(position);

                if (image.isSelected()) {
                    imageList.get(position).setSelected(false);
                    adapter.notifyDataSetChanged();
                } else {
                    imageList.get(position).setSelected(true);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        ll_config = (LinearLayout) findViewById(R.id.ll_config);
        ll_config.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<String> names = new ArrayList<String>();

                for (Image image : imageList) {
                    if (image.isSelected()) {
                        names.add(image.getName());
                    }
                }

                setSubscribe(uuid, sinaId,sinaToken,screenName,sinaProfileImageUrl,names);

            }
        });

    }

    private void initImagelist() {

        int[] ids = new int[]{R.drawable.ic_sign_zhangzishi, R.drawable.ic_sign_dongshenghuo, R.drawable.ic_sign_aozaoxing, R.drawable.ic_sing_youqiangdiao, R.drawable.ic_sign_kaiyanjie, R.drawable.ic_sign_wanchufan};
        String[] names = new String[]{"涨姿势", "懂生活", "凹造型", "有腔调", "开眼界", "玩出范"};

        for (int i = 0; i < 6; i++) {
            Image image = new Image();

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), ids[i]);
            image.setBitmap(bitmap);
            image.setName(names[i]);
            image.setSelected(false);

            imageList.add(image);
        }

    }

    private class DonateGridViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return imageList.size();
        }

        @Override
        public Object getItem(int arg0) {
            return null;
        }

        @Override
        public long getItemId(int arg0) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup root) {
            ViewHolderGridView holder = null;

            if (convertView == null) {
                convertView = View.inflate(SignAty.this, R.layout.aty_sign_gridview_item, null);
                holder = new ViewHolderGridView();
                holder.iv_image = (ImageView) convertView.findViewById(R.id.iv_image);
                holder.iv_rightmark = (ImageView) convertView.findViewById(R.id.iv_rightmark);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolderGridView) convertView.getTag();
            }

            Image image = imageList.get(position);
            holder.iv_image.setImageBitmap(image.getBitmap());

            boolean flag = image.isSelected();

            if (flag) {
                holder.iv_rightmark.setVisibility(View.VISIBLE);
            } else {
                holder.iv_rightmark.setVisibility(View.GONE);
            }

            return convertView;
        }

    }

    private class ViewHolderGridView {

        public ImageView iv_image;
        public ImageView iv_rightmark;

    }

    private  void setSubscribe(String uuid, String sinaId,String sinaToken,String screenName ,String sinaProfileImageUrl, List<String> tagStr) {

        String url = HttpConstant.URL_USER_LOGIN + "uuid=" + uuid + "&sinaId=" + sinaId + "&sinaToken=" + sinaToken + "&screenName=" + screenName + "&sinaProfileImageUrl=" + sinaProfileImageUrl;

        for (int i = 0; i < tagStr.size(); i++) {
            url = url + "&tag=" + tagStr.get(i);
        }

        url = url + "&isNormal=0";
        NetworkRequest request = new NetworkRequest(url, NetworkRequest.RequestMethod.GET);
        request.setCallback(new UserLoginCallBack<User>() {
                    @Override
                    public void success(User result) {
                        if (result != null) {
                            UserDataHelper.saveUser(result);
                        }
                        SignAty.this.finish();
                        Intent intent = new Intent(SignAty.this, HomeAty.class);
                        startActivity(intent);
                    }

                    @Override
                    public void failed(MyAppException exception) {
                        System.out.println("failure");
                        SignAty.this.finish();
                        Intent intent = new Intent(SignAty.this, HomeAty.class);
                        startActivity(intent);
                    }
                }.setReturnType(new TypeToken<User>() {

                }.getType())
        );
        request.execute();

    }
}