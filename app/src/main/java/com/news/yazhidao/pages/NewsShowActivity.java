package com.news.yazhidao.pages;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.gson.reflect.TypeToken;
import com.news.yazhidao.R;
import com.news.yazhidao.constant.HttpConstant;
import com.news.yazhidao.entity.NewsFeed;
import com.news.yazhidao.net.JsonCallback;
import com.news.yazhidao.net.MyAppException;
import com.news.yazhidao.net.NetworkRequest;

/**
 * Created by fengjigang on 15/1/16.
 */
public class NewsShowActivity extends Activity {
    public static String TAG = "NewsShowActivity";
    private ListView mNewsShowList;
    private NewsFeedAdapter mNewsFeedAdapter;
    private NewsFeed mNewsFeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_news_show);
        mNewsShowList = (ListView) findViewById(R.id.mNewsShowList);
        loadData();
    }

    private void loadData() {
        NetworkRequest request = new NetworkRequest(HttpConstant.URL_FETCH_NEWS_FOR_MODULE+"1", NetworkRequest.RequestMethod.GET);
        request.setCallback(new JsonCallback<NewsFeed>() {
            @Override
            public void success(NewsFeed result) {
                Log.i(">>>" + TAG, result.toString());
                NewsShowActivity.this.mNewsFeed = result;
                Log.i(">>>" + TAG, mNewsFeed.response_head.result);
                mNewsFeedAdapter = new NewsFeedAdapter(NewsShowActivity.this, mNewsFeed);
                mNewsShowList.setAdapter(mNewsFeedAdapter);
            }

            @Override
            public void failed(MyAppException exception) {
                Log.i(">>>" + TAG, exception.getMessage());
            }
        }.setReturnType(new TypeToken<NewsFeed>() {
        }.getType()));
        request.execute();
    }
}
