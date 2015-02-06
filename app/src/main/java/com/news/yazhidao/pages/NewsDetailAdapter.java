package com.news.yazhidao.pages;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.news.yazhidao.R;
import com.news.yazhidao.constant.CommonConstant;
import com.news.yazhidao.constant.HttpConstant;
import com.news.yazhidao.entity.NewsDetail;
import com.news.yazhidao.entity.NewsFeed;
import com.news.yazhidao.net.MyAppException;
import com.news.yazhidao.net.NetworkRequest;
import com.news.yazhidao.net.StringCallback;
import com.news.yazhidao.net.request.UserPraiseNewsRequest;
import com.news.yazhidao.utils.DeviceInfoUtil;
import com.news.yazhidao.utils.ImageLoaderHelper;
import com.news.yazhidao.utils.Logger;
import com.news.yazhidao.utils.TextUtil;
import com.news.yazhidao.utils.ToastUtil;
import com.news.yazhidao.utils.helper.UmengShareHelper;
import com.news.yazhidao.widget.StrokeTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by fengjigang on 15/1/21.
 */
public class NewsDetailAdapter extends BaseAdapter {
    private Context mContext;
    private ArrayList<ArrayList<Map<String, String>>> mSectionArr;
    private String mSubjectUrl;
    private ArrayList<NewsFeed.Element> mElementArr;
    private String mNewsId;
    private View mRelateView;
    private boolean isPraise ;
    private android.view.animation.Animation mAnimation;
    public NewsDetailAdapter(Context mContext, NewsDetail newsDetail, String subjectUrl, String newsId) {
        this.mContext = mContext;
        this.mSectionArr = newsDetail.content;
        this.mSubjectUrl = subjectUrl;
        this.mElementArr = newsDetail.elementList;
        this.mNewsId = newsId;
        this.mAnimation =AnimationUtils.loadAnimation(mContext, R.anim.news_praise_plus_one);
    }

    @Override
    public int getCount() {

        if (mElementArr != null && mElementArr.size() > 0) {
            return mSectionArr.size() + 2;
        }
        return mSectionArr.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return mSectionArr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return super.getViewTypeCount();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Logger.i(">>>", "NewsDetailAdapter position=" + position);
        if (position == mSectionArr.size()) {
            View share = View.inflate(mContext, R.layout.aty_news_detail_item_share, null);
            final View praise = share.findViewById(R.id.mNewsDetailPraiseWrapper);
            final View mNewsDetailShare =  share.findViewById(R.id.mNewsDetailShare);
            final TextView mNewsDetailPraiseTv = (TextView) share.findViewById(R.id.mNewsDetailPraiseTv);
            final ImageView mNewsDetailPraiseImg = (ImageView) share.findViewById(R.id.mNewsDetailPraiseImg);
            final TextView mNewsDetailPraisePlus=(TextView)share.findViewById(R.id.mNewsDetailPraisePlus);
            praise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isPraise) {
                        mNewsDetailPraiseImg.setImageResource(R.drawable.news_list_table_cell_unpraised);
                        mNewsDetailPraiseTv.setText((TextUtil.parsePraiseNumber(mNewsDetailPraiseTv.getText().toString()) - 1) + "人热赞");
                        isPraise = false;
                        //TODO 向后台确认点赞
                        UserPraiseNewsRequest.praiseNews(mNewsId,false,new UserPraiseNewsRequest.PraiseNewsCallback() {
                            @Override
                            public void success() {
                                ToastUtil.toastShort("取消点赞成功");
                            }

                            @Override
                            public void failed() {
                                ToastUtil.toastShort("取消点赞失败");
                            }
                        });
                    } else {
                        mNewsDetailPraisePlus.setVisibility(View.VISIBLE);
                        mNewsDetailPraisePlus.startAnimation(mAnimation);
                        new Handler().postDelayed(new Runnable(){
                            public void run() {
                                mNewsDetailPraisePlus.setVisibility(View.GONE);
                            }
                        }, 1000);
                        mNewsDetailPraiseImg.setImageResource(R.drawable.news_list_table_cell_praised);
                        mNewsDetailPraiseTv.setText((TextUtil.parsePraiseNumber(mNewsDetailPraiseTv.getText().toString()) + 1) + "人热赞");
                        isPraise = true;
                        //TODO 向后台取消点赞
                        UserPraiseNewsRequest.praiseNews(mNewsId,true,new UserPraiseNewsRequest.PraiseNewsCallback() {
                            @Override
                            public void success() {
                                ToastUtil.toastShort("点赞成功");
                            }

                            @Override
                            public void failed() {
                                ToastUtil.toastShort("点赞失败");
                            }
                        });
                    }
                }
            });
            mNewsDetailShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //TODO 新浪分享
                    NewsFeed.Element news=new NewsFeed.Element();
                    news.sourceUrl=mNewsId;
                    news.imgUrl=mSubjectUrl;
                    UmengShareHelper.shareSina(mContext,news);
                }
            });
            share.setOnClickListener(null);
            share.setEnabled(false);
            return share;
        }
        if (position == mSectionArr.size() + 1) {
            if (mRelateView == null) {
                mRelateView = generateRelateView();
            }
            return mRelateView;
        }
        View detail = View.inflate(mContext, R.layout.aty_news_detail_item, null);
        ImageView mNewsDetailItemImg = (ImageView) detail.findViewById(R.id.mNewsDetailItemImg);
        TextView mNewsDetailItemTxt = (TextView) detail.findViewById(R.id.mNewsDetailItemTxt);
        ArrayList<Map<String, String>> section = mSectionArr.get(position);
        String imgUrl = null;
        String text = null;
        for (Map<String, String> map : section) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if ("img".equals(entry.getKey())) {
                    imgUrl = entry.getValue();
                } else if ("txt".equals(entry.getKey())) {
                    text = entry.getValue();
                }
            }
        }
        if (imgUrl != null && !imgUrl.equals(mSubjectUrl)) {

            ImageLoaderHelper.dispalyImage(mContext, imgUrl, mNewsDetailItemImg);
        } else {
            mNewsDetailItemImg.setVisibility(View.GONE);
        }
        if (text != null) {
            mNewsDetailItemTxt.setText(text.trim());

        } else {
            mNewsDetailItemTxt.setVisibility(View.GONE);
        }
        detail.setEnabled(false);
        detail.setOnClickListener(null);
        return detail;
    }

    private View generateRelateView() {
        LinearLayout relateViewWrapper = (LinearLayout) View.inflate(mContext, R.layout.aty_news_detail_item_relate, null);

        if (mElementArr != null && mElementArr.size() > 0) {
            for (int index = 0; index < mElementArr.size(); index++) {
                View relateViewItem = View.inflate(mContext, R.layout.aty_news_show_list_cell, null);
                relateViewItem.setLayoutParams(new AbsListView.LayoutParams(DeviceInfoUtil.getScreenWidth(), (int) (DeviceInfoUtil.getScreenHeight()*0.16)));
                TextView mCellTitle = (TextView) relateViewItem.findViewById(R.id.mCellTitle);
                ImageView mCellImage = (ImageView) relateViewItem.findViewById(R.id.mCellImage);
                View mCellPraise = relateViewItem.findViewById(R.id.mCellPraiseWrapper);
                StrokeTextView mCellTemperature = (StrokeTextView) relateViewItem.findViewById(R.id.mCellTemperature);
                TextView mCellSourceSiteName = (TextView) relateViewItem.findViewById(R.id.mCellSourceSiteName);
                final TextView mCellPraisePlus= (TextView) relateViewItem.findViewById(R.id.mCellPraisePlus);
                final ImageView mCellPraiseImg= (ImageView) relateViewItem.findViewById(R.id.mCellPraiseImg);
                final TextView mCellPraiseTv= (TextView) relateViewItem.findViewById(R.id.mCellPraiseTv);

                mCellTitle.setText(mElementArr.get(index).title);
                mCellTemperature.setText(convertClassToTemp(mElementArr.get(index).RootClass));
                mCellSourceSiteName.setText(mElementArr.get(index).sourceSiteName);
                ImageLoaderHelper.dispalyImage(mContext, mElementArr.get(index).imgUrl, mCellImage);
                final int finalIndex = index;
                relateViewItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startNewsDetailPage(mElementArr.get(finalIndex).RootName, mElementArr.get(finalIndex));
                    }
                });
                mCellPraise.setOnClickListener(new View.OnClickListener() {
                    private boolean isRelatePraise ;
                    @Override
                    public void onClick(View view) {
                        if(isRelatePraise){
                            mCellPraiseImg.setImageResource(R.drawable.news_list_table_cell_unpraised_in_home);
                            mCellPraiseTv.setText((TextUtil.parsePraiseNumber(mCellPraiseTv.getText().toString()) - 1) + "人热赞");
                            mCellPraiseTv.setTextColor(mContext.getResources().getColor(R.color.news_list_cell_sourcesitename));
                            isRelatePraise=false;
                            //TODO 向后台取消点赞
                            UserPraiseNewsRequest.praiseNews(mNewsId,false,new UserPraiseNewsRequest.PraiseNewsCallback() {
                                @Override
                                public void success() {
                                    ToastUtil.toastShort("取消点赞成功");
                                }

                                @Override
                                public void failed() {
                                    ToastUtil.toastShort("取消点赞失败");
                                }
                            });
                        }else{
                            mCellPraisePlus.setVisibility(View.VISIBLE);
                            mCellPraisePlus.startAnimation(mAnimation);
                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    mCellPraisePlus.setVisibility(View.GONE);
                                }
                            }, 1000);
                            mCellPraiseImg.setImageResource(R.drawable.news_list_table_cell_praised);
                            mCellPraiseTv.setText((TextUtil.parsePraiseNumber(mCellPraiseTv.getText().toString()) + 1) + "人热赞");
                            mCellPraiseTv.setTextColor(mContext.getResources().getColor(R.color.common_theme_color));
                            isRelatePraise=true;
                            //TODO 向后台取消点赞
                            UserPraiseNewsRequest.praiseNews(mNewsId,true,new UserPraiseNewsRequest.PraiseNewsCallback() {
                                @Override
                                public void success() {
                                    ToastUtil.toastShort("点赞成功");
                                }

                                @Override
                                public void failed() {
                                    ToastUtil.toastShort("点赞失败");
                                }
                            });
                        }
                    }
                });
                relateViewWrapper.addView(relateViewItem);
            }
        }
        return relateViewWrapper;
    }

    /**
     * 把rootClass 转换成对应的温度
     * @param rootClass
     * @return
     */
    private String convertClassToTemp(String rootClass) {
        String temp="40°C";
        if ("0".equals(rootClass)) {
            temp="-40°C";
        } else if ("1".equals(rootClass)) {
            temp="0°C";
        } else if ("2".equals(rootClass)) {
            temp="36°C";
        } else if ("3".equals(rootClass)) {
            temp="40°C";
        }
        return temp;
    }

    /**
     * 打开新闻详情页
     *
     * @param channelName
     * @param element
     */
    private void startNewsDetailPage(String channelName, NewsFeed.Element element) {
        Intent newsDetail = new Intent(mContext, NewsDetailAty.class);
        newsDetail.putExtra(CommonConstant.KEY_NEWS_DETAIL, element);
        newsDetail.putExtra(CommonConstant.KEY_NEWS_TITLE, channelName);
        mContext.startActivity(newsDetail);
    }
}
