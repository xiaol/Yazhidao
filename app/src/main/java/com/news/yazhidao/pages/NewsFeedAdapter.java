package com.news.yazhidao.pages;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.news.yazhidao.GlobalParams;
import com.news.yazhidao.R;
import com.news.yazhidao.constant.CommonConstant;
import com.news.yazhidao.entity.NewsFeed;
import com.news.yazhidao.utils.ImageLoaderHelper;
import com.news.yazhidao.utils.Logger;
import com.news.yazhidao.utils.TextUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeSet;

/**
 * Created by fengjigang on 15/1/19.
 */
public class NewsFeedAdapter extends BaseAdapter implements View.OnClickListener {
    private static final String TAG = "NewsFeedAdapter";
    private final Animation animation;
    private final WindowManager manager;
    private final int width;
    private final int height;
    private Context mContext;
    private ArrayList<NewsFeed.Channel> mChannelsArr;
    private TreeSet<Integer> mClickMoredView;
    private NewsFeed mNewsFeed;
    private HashMap<Integer, ArrayList<View>> mCacheView;

    public NewsFeedAdapter(Context mContext, NewsFeed mNewsFeed) {
        this.mContext = mContext;
        this.mNewsFeed = mNewsFeed;
        this.mChannelsArr = new ArrayList<NewsFeed.Channel>();
        this.mClickMoredView = new TreeSet<Integer>();
        this.mCacheView = new HashMap<Integer, ArrayList<View>>();
        this.animation = AnimationUtils.loadAnimation(mContext, R.anim.news_praise_plus_one);
        manager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        width = manager.getDefaultDisplay().getWidth();
        height = manager.getDefaultDisplay().getHeight();
        handle(mNewsFeed);

    }

    private void handle(NewsFeed mNewsFeed) {
        ArrayList<NewsFeed.Channel> channels;
        if (mNewsFeed.channels == null) {
            channels = new ArrayList<NewsFeed.Channel>();
        } else {
            channels = mNewsFeed.channels;
        }
        if (channels != null) {
            for (NewsFeed.Channel element : channels) {
                if (element.elementList != null && element.elementList.size() > 0) {
                    mChannelsArr.add(element);
                }
            }
            for (int outer = 0; outer < mChannelsArr.size(); outer++) {
                final ArrayList<NewsFeed.Element> elementList = mChannelsArr.get(outer).elementList;
                ArrayList<View> cellView = new ArrayList<View>();
                for (int inner = 1; inner < elementList.size(); inner++) {
                    cellView.add(generateNewsCell(elementList.get(inner)));
                }
                mCacheView.put(outer, cellView);
            }
            Logger.e("cacheMap",mCacheView.toString());
        }
    }

    @Override
    public int getCount() {
        return mChannelsArr.size();
    }

    @Override
    public Object getItem(int position) {
        return mChannelsArr.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        Logger.i(">>>", "getView " + position + " parent=" + parent);
        final ViewHolder holder;
        if (convertView == null) {
            Logger.i(">>>", "not reuse the convertView");
            convertView = LayoutInflater.from(mContext).inflate(R.layout.aty_news_show_list_table, null);
            holder = new ViewHolder();
            holder.mTableChannelName = (TextView) convertView.findViewById(R.id.mTableChannelName);
            holder.mTableHeaderImg = (ImageView) convertView.findViewById(R.id.mTableHeaderImg);
//            holder.mTableHeaderImg.setLayoutParams(new RelativeLayout.LayoutParams((int) (DeviceInfoUtil.getScreenWidth()*0.88), (int) (DeviceInfoUtil.getScreenWidth()*0.88*0.55)));
            holder.mTableHeaderTitle = (TextView) convertView.findViewById(R.id.mTableHeaderTitle);
            holder.mTablePullDown = (RelativeLayout) convertView.findViewById(R.id.mTablePullDown);
            holder.mTableSetting = (FrameLayout) convertView.findViewById(R.id.mTableSetting);
            holder.mTableHeaderWrapper = (RelativeLayout) convertView.findViewById(R.id.mTableHeaderWrapper);
            holder.mTableCellWrapper = (LinearLayout) convertView.findViewById(R.id.mTableCellWrapper);//
            convertView.setTag(holder);
        } else {
            Logger.i(">>>", "reused the convertView");
            holder = (ViewHolder) convertView.getTag();
        }
        holder.mTableCellWrapper.removeAllViews();
        final ArrayList<NewsFeed.Element> elementList = mChannelsArr.get(position).elementList;
        ArrayList<View> childs = mCacheView.get(position);
        for (int index = 0; index < childs.size(); index++) {
            View child = childs.get(index);
            if (index > 1 && !mClickMoredView.contains(position)) {
                child.setVisibility(View.GONE);
            }
            holder.mTableCellWrapper.addView(child);
        }
        holder.mTableChannelName.setText(mChannelsArr.get(position).channelName);
        ImageLoaderHelper.getImageLoader(mContext).displayImage(elementList.get(0).imgUrl, holder.mTableHeaderImg, ImageLoaderHelper.getOption());
        holder.mTableHeaderTitle.setText(elementList.get(0).title);
        if (mClickMoredView.contains(position)) {
            holder.mTablePullDown.setVisibility(View.GONE);
        } else {
            holder.mTablePullDown.setVisibility(View.VISIBLE);
        }
        holder.mTableSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.i(TAG, "onclik position " + position);
            }
        });
        holder.mTableHeaderWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.i(TAG, "open news " + elementList.get(0).title);
                startNewsDetailPage(mChannelsArr.get(position).channelName, elementList.get(0));
            }
        });

        holder.mTablePullDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (elementList != null) {
                    for (int index = 2; index < holder.mTableCellWrapper.getChildCount(); index++) {
                        holder.mTableCellWrapper.getChildAt(index).setVisibility(View.VISIBLE);
                    }
                }

                Logger.i(TAG, "onclick pull down ");
                holder.mTablePullDown.setVisibility(View.GONE);
                mClickMoredView.add(position);
                setListViewHeight();

            }
        });
        return convertView;
    }

    @Override
    public void onClick(View v) {
        Object tag = v.getTag();
        if(tag!=null&&tag instanceof NewsFeed.Element){
            NewsFeed.Element element=(NewsFeed.Element)tag;
            startNewsDetailPage(mChannelsArr.get(mChannelsArr.indexOf(element)).channelName,element);
        }
    }

    static class ViewHolder {

        public TextView mTableChannelName;
        public ImageView mTableHeaderImg;
        public TextView mTableHeaderTitle;
        public RelativeLayout mTablePullDown;
        public FrameLayout mTableSetting;
        public RelativeLayout mTableHeaderWrapper;
        public LinearLayout mTableCellWrapper;
    }

    private void setListViewHeight() {

        switch (height) {

            case 1920:

                GlobalParams.LISTVIEW_ERROR += 1700;

                break;

            case 1800:
                GlobalParams.LISTVIEW_ERROR += 1420;
                break;

            case 1776:
                GlobalParams.LISTVIEW_ERROR += 1700;
                break;

            case 1280:
                GlobalParams.LISTVIEW_ERROR += 1150;
                break;

            case 800:
                GlobalParams.LISTVIEW_ERROR += 840;
                break;

            case 854:
                GlobalParams.LISTVIEW_ERROR += 840;
                break;
        }


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

    /**
     * 生成一个NewsCell 对象
     *
     * @param element
     * @return
     */
    private View generateNewsCell(NewsFeed.Element element) {
        View childView = LayoutInflater.from(mContext).inflate(R.layout.aty_news_show_list_cell, null);
        ImageView mCellImage = (ImageView) childView.findViewById(R.id.mCellImage);
//        mCellImage.setLayoutParams(new RelativeLayout.LayoutParams(230,130));
        TextView mCellSourceSiteName = (TextView) childView.findViewById(R.id.mCellSourceSiteName);
        TextView mCellTitle = (TextView) childView.findViewById(R.id.mCellTitle);
        TextView mCellTemperature = (TextView) childView.findViewById(R.id.mCellTemperature);
        View mCellPraiseWrapper = childView.findViewById(R.id.mCellPraiseWrapper);
        final ImageView mCellPraiseImg = (ImageView) childView.findViewById(R.id.mCellPraiseImg);
        final TextView mCellPraiseTv = (TextView) childView.findViewById(R.id.mCellPraiseTv);
        final TextView mCellPraisePlus = (TextView) childView.findViewById(R.id.mCellPraisePlus);
        mCellPraiseWrapper.setOnClickListener(new View.OnClickListener() {
            boolean isPraise;

            @Override
            public void onClick(View v) {
                if (isPraise) {
                    mCellPraiseImg.setImageResource(R.drawable.news_list_table_cell_unpraised_in_home);
                    mCellPraiseTv.setText((TextUtil.parsePraiseNumber(mCellPraiseTv.getText().toString()) - 1) + "人热赞");
                    mCellPraiseTv.setTextColor(mContext.getResources().getColor(R.color.news_list_cell_sourcesitename));
                    isPraise = false;
                } else {
                    mCellPraisePlus.setVisibility(View.VISIBLE);
                    mCellPraisePlus.startAnimation(animation);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            mCellPraisePlus.setVisibility(View.GONE);
                        }
                    }, 1000);
                    mCellPraiseImg.setImageResource(R.drawable.news_list_table_cell_praised);
                    mCellPraiseTv.setText((TextUtil.parsePraiseNumber(mCellPraiseTv.getText().toString()) + 1) + "人热赞");
                    mCellPraiseTv.setTextColor(mContext.getResources().getColor(R.color.common_theme_color));
                    isPraise = true;
                }
            }
        });
        ImageLoaderHelper.getImageLoader(mContext).displayImage(element.imgUrl, mCellImage, ImageLoaderHelper.getOption());
        mCellSourceSiteName.setText(element.sourceSiteName);
        mCellTitle.setText(element.title);
        mCellTemperature.setText(TextUtil.convertTemp(mNewsFeed.root_alias));
        childView.setTag(element);
        childView.setOnClickListener(this);
        return childView;
    }

}
