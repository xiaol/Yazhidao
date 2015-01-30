package com.news.yazhidao.pages;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.news.yazhidao.R;
import com.news.yazhidao.constant.CommonConstant;
import com.news.yazhidao.constant.HttpConstant;
import com.news.yazhidao.entity.NewsDetail;
import com.news.yazhidao.entity.NewsFeed;
import com.news.yazhidao.net.JsonCallback;
import com.news.yazhidao.net.MyAppException;
import com.news.yazhidao.net.NetworkRequest;
import com.news.yazhidao.utils.ImageLoaderHelper;
import com.news.yazhidao.utils.Logger;
import com.news.yazhidao.utils.gifview.GifView;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by fengjigang on 15/1/21.
 */
public class NewsDetailActivity extends Activity implements View.OnClickListener {
    private static final String TAG = "NewsDetailActivity";
    private ListView mNewsDetailListContent;
    private NewsDetailAdapter mNewsDetailAdapter;
    private NewsFeed.Element mNewsEle;
    private View mNewsCommonHeaderLeftBack;
    private TextView mNewsCommonHeaderTitle;
    private View mNewsDetailLoadingWrapper;
    private GifView mNewsDetailLoading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNewsEle = (NewsFeed.Element) getIntent().getSerializableExtra(CommonConstant.KEY_NEWS_DETAIL);
        setContentView(R.layout.aty_news_detail);
        mNewsCommonHeaderLeftBack=findViewById(R.id.mNewsCommonHeaderLeftBack);
        mNewsCommonHeaderLeftBack.setOnClickListener(this);
        mNewsCommonHeaderTitle=(TextView)findViewById(R.id.mNewsCommonHeaderTitle);
        mNewsCommonHeaderTitle.setText(getIntent().getStringExtra(CommonConstant.KEY_NEWS_TITLE));
        mNewsDetailListContent = (ListView) findViewById(R.id.mNewsDetailListContent);
        mNewsDetailLoading=(GifView)findViewById(R.id.mNewsDetailLoading);
        mNewsDetailLoading.setGifImage(R.drawable.loading);
        mNewsDetailLoadingWrapper=findViewById(R.id.mNewsDetailLoadingWrapper);
        loadNewsDetail(mNewsEle.sourceUrl);
    }

    private void loadNewsDetail(String newsId) {
        NetworkRequest request = new NetworkRequest(HttpConstant.URL_FETCH_NEWS_FOR_DETAIL + newsId, NetworkRequest.RequestMethod.GET);
        request.setCallback(new JsonCallback<NewsDetail>() {
            public void success(NewsDetail result) {
                Logger.i(TAG, ">>> success  result " + result.response_body.FetchContent.content.size());
                ArrayList<ArrayList<Map<String, String>>> content = result.response_body.FetchContent.content;
                for (ArrayList<Map<String, String>> list : content) {
                    for (Map<String, String> map : list) {
                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            Logger.i(">>>", "key=" + entry.getKey() + ",value=" + entry.getValue());
                        }
                    }
                    Logger.i(">>>", ">>>>>>>>>.");
                }

                mNewsDetailListContent.addHeaderView(generateListHeader());
                mNewsDetailAdapter = new NewsDetailAdapter(NewsDetailActivity.this, result,mNewsEle.imgUrl);
                mNewsDetailListContent.setAdapter(mNewsDetailAdapter);
            }

            public void failed(MyAppException exception) {
                Logger.i(TAG, ">>> failed result " + exception.getMessage());
            }
        }.setReturnType(new TypeToken<NewsDetail>() {
        }.getType()));
        request.execute();
    }

    private View generateListHeader() {
        View listHeader = View.inflate(NewsDetailActivity.this, R.layout.aty_news_detail_list_header, null);
        TextView mNewsDetailHeaderTitle = (TextView) listHeader.findViewById(R.id.mNewsDetailHeaderTitle);
        ImageView mNewsDetailHeaderImg = (ImageView) listHeader.findViewById(R.id.mNewsDetailHeaderImg);
        TextView mNewsDetailHeaderOriginAndTime = (TextView) listHeader.findViewById(R.id.mNewsDetailHeaderOriginAndTime);
        ImageLoaderHelper.dispalyImage(NewsDetailActivity.this, mNewsEle.imgUrl, mNewsDetailHeaderImg);
        mNewsDetailHeaderTitle.setText(mNewsEle.title);
        String originAndTime = String.format(NewsDetailActivity.this.getResources().getString(R.string.news_detail_origin_and_time), mNewsEle.sourceSiteName, mNewsEle.updateTime);
        mNewsDetailHeaderOriginAndTime.setText(originAndTime);
        listHeader.setEnabled(false);
        listHeader.setOnClickListener(null);
        return listHeader;
    }

    @Override
    public void onClick(View v) {
        int viewId=v.getId();
        if(viewId== R.id.mNewsCommonHeaderLeftBack){
            this.finish();
        }
    }
}
