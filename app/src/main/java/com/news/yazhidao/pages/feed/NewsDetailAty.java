package com.news.yazhidao.pages.feed;

import android.graphics.Paint;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.news.yazhidao.R;
import com.news.yazhidao.common.BaseActivity;
import com.news.yazhidao.constant.CommonConstant;
import com.news.yazhidao.constant.HttpConstant;
import com.news.yazhidao.entity.NewsDetail;
import com.news.yazhidao.entity.NewsFeed;
import com.news.yazhidao.net.JsonCallback;
import com.news.yazhidao.net.MyAppException;
import com.news.yazhidao.net.NetworkRequest;
import com.news.yazhidao.net.TextUtils;
import com.news.yazhidao.utils.DensityUtil;
import com.news.yazhidao.utils.DeviceInfoUtil;
import com.news.yazhidao.utils.helper.ImageLoaderHelper;
import com.news.yazhidao.utils.Logger;
import com.news.yazhidao.utils.TextUtil;

/**
 * Created by fengjigang on 15/1/21.
 */
public class NewsDetailAty extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "NewsDetailActivity";
    private ListView mNewsDetailListContent;
    private NewsDetailAdapter mNewsDetailAdapter;
    private NewsFeed.Element mNewsEle;
    private View mNewsCommonHeaderLeftBack;
    private TextView mNewsCommonHeaderTitle;
    private View mNewsDetailLoadingWrapper;
    private ImageView mNewsLoadingImg;
    private View mNewsDetailCilckRefresh;
    private AnimationDrawable mAniNewsLoading;

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
        mNewsLoadingImg=(ImageView)findViewById(R.id.mNewsLoadingImg);
        mNewsLoadingImg.setImageResource(R.drawable.list_news_progress_animation);
        mAniNewsLoading = (AnimationDrawable) mNewsLoadingImg.getDrawable();
        mAniNewsLoading.start();
        mNewsDetailLoadingWrapper=findViewById(R.id.mNewsDetailLoadingWrapper);
        mNewsDetailCilckRefresh = findViewById(R.id.mNewsDetailCilckRefresh);
        mNewsDetailCilckRefresh.setOnClickListener(this);
        mNewsDetailCilckRefresh.setTag(mNewsEle.sourceUrl);
        loadNewsDetail(mNewsEle.id);
    }

    private void loadNewsDetail(String newsId) {
        mNewsDetailLoadingWrapper.setVisibility(View.VISIBLE);
        mNewsDetailCilckRefresh.setVisibility(View.GONE);
        NetworkRequest request = new NetworkRequest(HttpConstant.URL_FETCH_NEWS_FOR_DETAIL + newsId, NetworkRequest.RequestMethod.GET);
        final String finalNewsId = newsId;
        request.setCallback(new JsonCallback<NewsDetail>() {
            @Override
            public void success(NewsDetail result) {
                if (result != null && result.content != null && result.content.size() > 0) {
                    mNewsDetailListContent.addHeaderView(generateListHeader());
                    mNewsDetailAdapter = new NewsDetailAdapter(NewsDetailAty.this, result, mNewsEle,mNewsEle.imgUrl,finalNewsId);
                    mNewsDetailListContent.setAdapter(mNewsDetailAdapter);
                    mNewsDetailLoadingWrapper.setVisibility(View.GONE);
                    mAniNewsLoading.stop();
                    mNewsDetailCilckRefresh.setVisibility(View.GONE);
                } else {
                    Toast.makeText(NewsDetailAty.this, "此条新闻暂时无法查看", Toast.LENGTH_SHORT).show();
                    NewsDetailAty.this.finish();
                }
            }

            @Override
            public void failed(MyAppException exception) {
                Logger.i(TAG, ">>> failed result " + exception.getMessage());
                MyAppException.ExceptionStatus status = exception.getExceptionStatus();
                if (status == MyAppException.ExceptionStatus.ServerException || status == MyAppException.ExceptionStatus.TimeOutException) {
                    Toast.makeText(NewsDetailAty.this, "网络不给力", Toast.LENGTH_SHORT).show();
                }
                mAniNewsLoading.stop();
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
            ImageLoaderHelper.dispalyImage(NewsDetailAty.this, mNewsEle.imgUrl, mNewsDetailHeaderImg);

        }
        mNewsEle.title= TextUtil.trimBlankSpace(mNewsEle.title);
        mNewsDetailHeaderTitleNoImg.setText(mNewsEle.title);
        mNewsDetailHeaderTitle.setText(mNewsEle.title);
        int textSize=24;
        int textSpace = DeviceInfoUtil.getScreenWidth()-DensityUtil.dip2px(this, 15);
        Logger.e("xxx",textSpace+"textSpace");
        Paint mPaint=new Paint();
        mPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, textSize, this.getResources().getDisplayMetrics()));
        float length = mPaint.measureText(mNewsEle.title);
        if(length<textSpace){
            mNewsDetailHeaderTitle.setLines(1);
            mNewsDetailHeaderTitleNoImg.setLines(1);
        }else{
            Logger.e("xxx",length+"outer");
            while (textSpace*2<length+50){
                mPaint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, --textSize, this.getResources().getDisplayMetrics()));
                length=mPaint.measureText(mNewsEle.title);
                Logger.e("xxx",length+"inner  "+mNewsEle.title.replace("  ",""));
            }
            mNewsDetailHeaderTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);
            mNewsDetailHeaderTitleNoImg.setTextSize(TypedValue.COMPLEX_UNIT_SP,textSize);
        }
        String originAndTime = String.format(NewsDetailAty.this.getResources().getString(R.string.news_detail_origin_and_time), mNewsEle.sourceSiteName, mNewsEle.updateTime);
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
