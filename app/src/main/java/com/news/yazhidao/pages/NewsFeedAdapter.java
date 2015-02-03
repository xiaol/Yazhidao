package com.news.yazhidao.pages;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;
import com.news.yazhidao.R;
import com.news.yazhidao.constant.CommonConstant;
import com.news.yazhidao.entity.NewsFeed;
import com.news.yazhidao.utils.DeviceInfoUtil;
import com.news.yazhidao.utils.ImageLoaderHelper;
import com.news.yazhidao.utils.Logger;
import com.news.yazhidao.utils.TextUtil;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Created by fengjigang on 15/1/19.
 */
public class NewsFeedAdapter extends BaseAdapter {
    private static final String TAG = "NewsFeedAdapter";
    private final Animation animation;
    private Context mContext;
    private ArrayList<NewsFeed.Channel> mChannelsArr;
    private TreeSet<Integer> mCacheAddedView;
    private NewsFeed mNewsFeed;


    public NewsFeedAdapter(Context mContext, NewsFeed mNewsFeed) {
        this.mContext = mContext;
        this.mNewsFeed = mNewsFeed;
        this.mChannelsArr = new ArrayList<NewsFeed.Channel>();
        this.mCacheAddedView = new TreeSet<Integer>();
        this.animation= AnimationUtils.loadAnimation(mContext, R.anim.news_praise_plus_one);
        handle(mNewsFeed);
    }

    private void handle(NewsFeed mNewsFeed) {
        ArrayList<NewsFeed.Channel> channels;
        if(mNewsFeed.channels==null){
            channels=new ArrayList<NewsFeed.Channel>();
        }else {
            channels = mNewsFeed.channels;
        }
        if (channels != null) {
            for (NewsFeed.Channel element : channels) {
                if (element.elementList != null && element.elementList.size() > 0) {
                    mChannelsArr.add(element);
                }
            }
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        Logger.i(">>>", "getView " + position + " parent=" + parent);
        final ViewHolder holder;
        if (convertView == null) {
            Logger.i(">>>","not reuse the convertView");
            convertView = LayoutInflater.from(mContext).inflate(R.layout.aty_news_show_list_table, null);
            holder = new ViewHolder();
            holder.mTableChannelName = (TextView) convertView.findViewById(R.id.mTableChannelName);
            holder.mTableHeaderImg = (ImageView) convertView.findViewById(R.id.mTableHeaderImg);
//            holder.mTableHeaderImg.setLayoutParams(new RelativeLayout.LayoutParams((int) (DeviceInfoUtil.getScreenWidth()*0.88), (int) (DeviceInfoUtil.getScreenWidth()*0.88*0.55)));
            holder.mTableHeaderTitle = (TextView) convertView.findViewById(R.id.mTableHeaderTitle);
            holder.mTablePullDown = (RelativeLayout) convertView.findViewById(R.id.mTablePullDown);
            holder.mTableSetting = (FrameLayout) convertView.findViewById(R.id.mTableSetting);
            holder.mTableHeaderWrapper = (RelativeLayout) convertView.findViewById(R.id.mTableHeaderWrapper);
            convertView.setTag(holder);
        } else {
            Logger.i(">>>","reused the convertView");
            holder = (ViewHolder) convertView.getTag();
        }
        final LinearLayout layout = (LinearLayout) convertView;
        final ArrayList<NewsFeed.Element> elementList = mChannelsArr.get(position).elementList;
        //判断当前的view是否已经添加过两个view
        if (!mCacheAddedView.contains(layout.hashCode())) {
            for (int index = 1; index < 3; index++) {
                NewsFeed.Element element = elementList.get(index);
                final int finalIndex = index;
                View childView=generateNewsCell(element,new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Logger.i(TAG,"open news >> " + elementList.get(finalIndex).title);
                        startNewsDetailPage(mChannelsArr.get(position).channelName,elementList.get(finalIndex));
                    }
                });
                layout.addView(childView, layout.getChildCount() - 2);
                Logger.i(">>> layout >>", "" + layout.hashCode());
            }
            mCacheAddedView.add(layout.hashCode());
        }
        Logger.i(">>> layout size>>", "" + mCacheAddedView.size());
        holder.mTableChannelName.setText(mChannelsArr.get(position).channelName);
        ImageLoaderHelper.getImageLoader(mContext).displayImage(elementList.get(0).imgUrl, holder.mTableHeaderImg, ImageLoaderHelper.getOption());
        holder.mTableHeaderTitle.setText(elementList.get(0).title);

        holder.mTableSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.i(TAG,"onclik position " + position);
            }
        });
        holder.mTableHeaderWrapper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.i(TAG,"open news " + elementList.get(0).title);
                startNewsDetailPage(mChannelsArr.get(position).channelName, elementList.get(0));
            }
        });

        final LinearLayout finalLayout = layout;
        holder.mTablePullDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (elementList != null) {
                    for (int index = 3; index < elementList.size(); index++) {
                        NewsFeed.Element element = elementList.get(index);
                        final int finalIndex = index;
                        View childView=generateNewsCell(element,new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Logger.i(TAG,"open news >> " + elementList.get(finalIndex).title);
                                startNewsDetailPage(mChannelsArr.get(position).channelName, elementList.get(finalIndex));
                            }
                        });
                        layout.addView(childView, layout.getChildCount() - 2);
                    }
                }
                Logger.i(TAG,"onclick pull down ");
                holder.mTablePullDown.setVisibility(View.GONE);
            }
        });
        return finalLayout;
    }


    static class ViewHolder {

        public TextView mTableChannelName;
        public ImageView mTableHeaderImg;
        public TextView mTableHeaderTitle;
        public RelativeLayout mTablePullDown;
        public FrameLayout mTableSetting;
        public RelativeLayout mTableHeaderWrapper;
    }

    /**
     * 打开新闻详情页
     * @param channelName
     * @param element
     */
    private void startNewsDetailPage(String channelName, NewsFeed.Element element){
        Intent newsDetail=new Intent(mContext,NewsDetailActivity.class);
        newsDetail.putExtra(CommonConstant.KEY_NEWS_DETAIL,element);
        newsDetail.putExtra(CommonConstant.KEY_NEWS_TITLE,channelName);
        mContext.startActivity(newsDetail);
    }
    /**
     * 生成一个NewsCell 对象
     * @param element
     * @param listener
     * @return
     */
    private View generateNewsCell(NewsFeed.Element element,View.OnClickListener listener){
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
                if(isPraise){
                    mCellPraiseImg.setImageResource(R.drawable.news_list_table_cell_unpraised_in_home);
                    mCellPraiseTv.setText((TextUtil.parsePraiseNumber(mCellPraiseTv.getText().toString())-1)+"人热赞");
                    mCellPraiseTv.setTextColor(mContext.getResources().getColor(R.color.news_list_cell_sourcesitename));
                    isPraise=false;
                }else{
                    mCellPraisePlus.setVisibility(View.VISIBLE);
                    mCellPraisePlus.startAnimation(animation);
                    new Handler().postDelayed(new Runnable(){
                        public void run() {
                            mCellPraisePlus.setVisibility(View.GONE);
                        }
                    }, 1000);
                    mCellPraiseImg.setImageResource(R.drawable.news_list_table_cell_praised);
                    mCellPraiseTv.setText((TextUtil.parsePraiseNumber(mCellPraiseTv.getText().toString())+1)+"人热赞");
                    mCellPraiseTv.setTextColor(mContext.getResources().getColor(R.color.common_theme_color));
                    isPraise=true;
                }
            }
        });
        ImageLoaderHelper.getImageLoader(mContext).displayImage(element.imgUrl, mCellImage, ImageLoaderHelper.getOption());
        mCellSourceSiteName.setText(element.sourceSiteName);
        mCellTitle.setText(element.title);
        mCellTemperature.setText(TextUtil.convertTemp(mNewsFeed.root_alias));
        childView.setOnClickListener(listener);
        return childView;
    }

}
