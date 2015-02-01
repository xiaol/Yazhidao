package com.news.yazhidao.pages;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.news.yazhidao.R;
import com.news.yazhidao.constant.CommonConstant;
import com.news.yazhidao.entity.NewsDetail;
import com.news.yazhidao.entity.NewsFeed;
import com.news.yazhidao.utils.ImageLoaderHelper;
import com.news.yazhidao.utils.Logger;
import com.news.yazhidao.utils.TextUtil;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by fengjigang on 15/1/21.
 */
public class NewsDetailAdapter extends BaseAdapter {
    private Context mContext;
    private NewsDetail mNewsDetail;
    private ArrayList<ArrayList<Map<String, String>>> mSectionArr;
    private String mSubjectUrl;
    private ArrayList<NewsFeed.Element> mElementArr;
    private String mNewsId;
    private View mRelateView;
    private boolean isPraise = true;

    public NewsDetailAdapter(Context mContext, NewsDetail newsDetail, String subjectUrl, String newsId) {
        this.mContext = mContext;
        this.mNewsDetail = newsDetail;
        this.mSectionArr = newsDetail.response_body.FetchContent.content;
        this.mSubjectUrl = subjectUrl;
        this.mElementArr = newsDetail.response_body.FetchContent.elementList;
        this.mNewsId = newsId;
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
            final ImageView mNewsDetailShare = (ImageView) share.findViewById(R.id.mNewsDetailShare);
            final TextView mNewsDetailPraiseTv = (TextView) share.findViewById(R.id.mNewsDetailPraiseTv);
            final ImageView mNewsDetailPraiseImg = (ImageView) share.findViewById(R.id.mNewsDetailPraiseImg);
            praise.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "click praise " + mNewsId, Toast.LENGTH_SHORT).show();
                    if (isPraise) {
                        mNewsDetailPraiseImg.setImageResource(R.drawable.news_list_table_cell_unpraised);
                        mNewsDetailPraiseTv.setText((TextUtil.parsePraiseNumber(mNewsDetailPraiseTv.getText().toString()) - 1) + "人热赞");
                        mNewsDetailPraiseTv.setTextColor(mContext.getResources().getColor(R.color.black));
                        isPraise = false;
                    } else {
                        mNewsDetailPraiseImg.setImageResource(R.drawable.news_list_table_cell_praised);
                        mNewsDetailPraiseTv.setText((TextUtil.parsePraiseNumber(mNewsDetailPraiseTv.getText().toString()) + 1) + "人热赞");
                        mNewsDetailPraiseTv.setTextColor(mContext.getResources().getColor(R.color.common_theme_color));
                        isPraise = true;
                    }
                }
            });
            mNewsDetailShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, "click share", Toast.LENGTH_SHORT).show();
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
                TextView mCellTitle = (TextView) relateViewItem.findViewById(R.id.mCellTitle);
                ImageView mCellImage = (ImageView) relateViewItem.findViewById(R.id.mCellImage);
                View mCellPraise = relateViewItem.findViewById(R.id.mCellPraiseWrapper);
                mCellPraise.setVisibility(View.GONE);
                View mCellTemperature = relateViewItem.findViewById(R.id.mCellTemperature);
                mCellTemperature.setVisibility(View.GONE);
                View mCellSourceSiteName = relateViewItem.findViewById(R.id.mCellSourceSiteName);
                mCellSourceSiteName.setVisibility(View.GONE);

                mCellTitle.setText(mElementArr.get(index).title);
                ImageLoaderHelper.dispalyImage(mContext, mElementArr.get(index).imgUrl, mCellImage);
                final int finalIndex = index;
                relateViewItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startNewsDetailPage(mElementArr.get(finalIndex).RootName, mElementArr.get(finalIndex));
                    }
                });
                relateViewWrapper.addView(relateViewItem);
            }
        }
        return relateViewWrapper;
    }

    /**
     * 打开新闻详情页
     *
     * @param channelName
     * @param element
     */
    private void startNewsDetailPage(String channelName, NewsFeed.Element element) {
        Intent newsDetail = new Intent(mContext, NewsDetailActivity.class);
        newsDetail.putExtra(CommonConstant.KEY_NEWS_DETAIL, element);
        newsDetail.putExtra(CommonConstant.KEY_NEWS_TITLE, channelName);
        mContext.startActivity(newsDetail);
    }

}
