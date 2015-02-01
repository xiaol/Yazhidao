package com.news.yazhidao.pages;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.news.yazhidao.R;
import com.news.yazhidao.constant.CommonConstant;
import com.news.yazhidao.constant.HttpConstant;
import com.news.yazhidao.entity.NewsDetail;
import com.news.yazhidao.entity.NewsFeed;
import com.news.yazhidao.net.JsonCallback;
import com.news.yazhidao.net.MyAppException;
import com.news.yazhidao.net.NetworkRequest;
import com.news.yazhidao.net.TextUtils;
import com.news.yazhidao.utils.ImageLoaderHelper;
import com.news.yazhidao.utils.Logger;
import com.news.yazhidao.utils.gifview.GifView;

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
    private View mNewsDetailCilckRefresh;

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
        mNewsDetailCilckRefresh = findViewById(R.id.mNewsDetailCilckRefresh);
        mNewsDetailCilckRefresh.setOnClickListener(this);
        mNewsDetailCilckRefresh.setTag(mNewsEle.sourceUrl);
        loadNewsDetail(mNewsEle.sourceUrl);
    }

    private void loadNewsDetail(String newsId) {
        mNewsDetailLoadingWrapper.setVisibility(View.VISIBLE);
        mNewsDetailCilckRefresh.setVisibility(View.GONE);
        NetworkRequest request = new NetworkRequest(HttpConstant.URL_FETCH_NEWS_FOR_DETAIL + newsId, NetworkRequest.RequestMethod.GET);
        final String finalNewsId = newsId;
        request.setCallback(new JsonCallback<NewsDetail>() {
            @Override
            public void success(NewsDetail result) {
                NewsDetail.FetchContentArr newsArr=null ;
                if(result!=null){
                    if(result.response_body!=null){
                        newsArr=result.response_body.FetchContent;
                    }
                }
                if (newsArr != null && newsArr.content != null && newsArr.content.size() > 0) {
                    mNewsDetailListContent.addHeaderView(generateListHeader());
                    mNewsDetailAdapter = new NewsDetailAdapter(NewsDetailActivity.this, result, mNewsEle.imgUrl,finalNewsId);
                    mNewsDetailListContent.setAdapter(mNewsDetailAdapter);
                    mNewsDetailLoadingWrapper.setVisibility(View.GONE);
                    mNewsDetailCilckRefresh.setVisibility(View.GONE);
                } else {
                    Toast.makeText(NewsDetailActivity.this, "此条新闻暂时无法查看", Toast.LENGTH_SHORT).show();
                    NewsDetailActivity.this.finish();
                }
            }

            @Override
            public void failed(MyAppException exception) {
                Logger.i(TAG, ">>> failed result " + exception.getMessage());
                MyAppException.ExceptionStatus status = exception.getExceptionStatus();
                if (status == MyAppException.ExceptionStatus.ServerException || status == MyAppException.ExceptionStatus.TimeOutException) {
                    Toast.makeText(NewsDetailActivity.this, "网络不给力", Toast.LENGTH_SHORT).show();
                }
                mNewsDetailLoadingWrapper.setVisibility(View.GONE);
                mNewsDetailCilckRefresh.setVisibility(View.VISIBLE);
            }
        }.setReturnType(new TypeToken<NewsDetail>() {
        }.getType()));
        request.execute();
    }

    private View generateListHeader() {
        View listHeader = View.inflate(this, R.layout.aty_news_detail_list_header, null);
        final ImageView mNewsDetailHeaderImg = (ImageView) listHeader.findViewById(R.id.mNewsDetailHeaderImg);
        TextView mNewsDetailHeaderOriginAndTime = (TextView) listHeader.findViewById(R.id.mNewsDetailHeaderOriginAndTime);
        final TextView mNewsDetailHeaderTitle = (TextView) listHeader.findViewById(R.id.mNewsDetailHeaderTitle);
        View mNewsDetailHeaderWrapper = listHeader.findViewById(R.id.mNewsDetailHeaderWrapper);
        View mNewsDetailHeaderTitleNoImgWrapper = listHeader.findViewById(R.id.mNewsDetailHeaderTitleNoImgWrapper);
        TextView mNewsDetailHeaderTitleNoImg=(TextView)listHeader.findViewById(R.id.mNewsDetailHeaderTitleNoImg);
        TextView mNewsDetailHeaderOriginAndTimeNoImg = (TextView) listHeader.findViewById(R.id.mNewsDetailHeaderOriginAndTimeNoImg);
        if(!TextUtils.isValidate(mNewsEle.imgUrl)){
            mNewsDetailHeaderWrapper.setVisibility(View.GONE);
            mNewsDetailHeaderTitleNoImgWrapper.setVisibility(View.VISIBLE);
        }else{
            mNewsDetailHeaderWrapper.setVisibility(View.VISIBLE);
            mNewsDetailHeaderTitleNoImgWrapper.setVisibility(View.GONE);
            ImageLoaderHelper.dispalyImage(NewsDetailActivity.this, mNewsEle.imgUrl, mNewsDetailHeaderImg);

        }
        mNewsDetailHeaderTitleNoImg.setText(mNewsEle.title);
        mNewsDetailHeaderTitle.setText(mNewsEle.title);
        String originAndTime = String.format(NewsDetailActivity.this.getResources().getString(R.string.news_detail_origin_and_time), mNewsEle.sourceSiteName, mNewsEle.updateTime);
        mNewsDetailHeaderOriginAndTime.setText(originAndTime);
        mNewsDetailHeaderOriginAndTimeNoImg.setText(originAndTime);
        listHeader.setEnabled(false);
        listHeader.setOnClickListener(null);
        return listHeader;
    }

    @Override
    public void onClick(View v) {
        int viewId=v.getId();
        if(viewId==R.id.mNewsCommonHeaderLeftBack){
            this.finish();
        }else if (viewId == R.id.mNewsDetailCilckRefresh) {
            loadNewsDetail((String) mNewsDetailCilckRefresh.getTag());
        }
    }
}
