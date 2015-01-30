package com.news.yazhidao.pages;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.news.yazhidao.R;
import com.news.yazhidao.entity.NewsDetail;
import com.news.yazhidao.utils.ImageLoaderHelper;

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
    public NewsDetailAdapter(Context mContext, NewsDetail newsDetail,String subjectUrl) {
        this.mContext = mContext;
        this.mNewsDetail = newsDetail;
        this.mSectionArr = newsDetail.response_body.FetchContent.content;
        this.mSubjectUrl=subjectUrl;
    }

    @Override
    public int getCount() {
        return mSectionArr.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
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
        if (imgUrl != null&&!imgUrl.equals(mSubjectUrl)) {

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
}
