package com.news.yazhidao.pages.user;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.news.yazhidao.R;
import com.news.yazhidao.entity.NewsFeed;
import com.news.yazhidao.utils.DeviceInfoUtil;
import com.news.yazhidao.utils.TextUtil;
import com.news.yazhidao.utils.helper.ImageLoaderHelper;

import java.util.ArrayList;

public class ExpandableWallListViewAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private NewsFeed mNewsFeed;
    private NewsFeed mCurrentNewsFeed;


    public ExpandableWallListViewAdapter(Context context) {
        mContext = context;
        this.mNewsFeed = new NewsFeed();
        this.mCurrentNewsFeed = new NewsFeed();
        mCurrentNewsFeed.channels = new ArrayList<>();
    }

    public void setArrNews(NewsFeed newsFeed) {
        mNewsFeed = null;
        synchronized (this) {
            mNewsFeed = newsFeed;
        }
        setmCurrentNewsFeed(mNewsFeed);
    }


    public void setmCurrentNewsFeed(NewsFeed newsFeed) {
        if (mCurrentNewsFeed.channels.size() == 0) {
            ArrayList<NewsFeed.Channel> channels = new ArrayList<>();
            for (int j = 0; j < newsFeed.channels.size(); j++) {
                NewsFeed.Channel channel = new NewsFeed.Channel();
                NewsFeed.Channel currentChannel = newsFeed.channels.get(j);
                channel.channelAlias = currentChannel.channelAlias;
                channel.channelId = currentChannel.channelId;
                channel.channelName = currentChannel.channelName;
                channel.lastUpdateTime = currentChannel.lastUpdateTime;
                ArrayList<NewsFeed.Element> elementList = new ArrayList<>();
                for (int i = 1; i < 4; i++) {
                    NewsFeed.Element element = new NewsFeed.Element();
                    NewsFeed.Element currentElement = newsFeed.channels.get(j).elementList.get(i);
                    element.RootClass = currentElement.RootClass;
                    element.RootName = currentElement.RootName;
                    element.channelName = currentElement.channelName;
                    element.id = currentElement.id;
                    element.imgUrl = currentElement.imgUrl;
                    element.sourceSiteName = currentElement.sourceSiteName;
                    element.sourceUrl = currentElement.sourceUrl;
                    element.title = currentElement.title;
                    element.updateTime = currentElement.updateTime;
                    element.tag = currentElement.tag;
                    elementList.add(element);
                }
                channel.elementList = elementList;
                channels.add(channel);
            }
            mCurrentNewsFeed.channels.addAll(channels);
        }
    }


    @Override
    public int getGroupCount() {
        // TODO Auto-generated method stub
        return mCurrentNewsFeed.channels == null ? 0 : mCurrentNewsFeed.channels.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        // TODO Auto-generated method stub
        return mCurrentNewsFeed.channels.get(groupPosition).elementList == null ? 0 : mCurrentNewsFeed.channels.get(groupPosition).elementList.size();
    }


    @Override
    public Object getGroup(int groupPosition) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getGroupId(int groupPosition) {
        // TODO Auto-generated method stub
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        // TODO Auto-generated method stub
        return true;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        final GroupHolder holder;
        if (convertView == null) {
            holder = new GroupHolder();
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.adapter_news_group, null, false);
            holder.mTableChannelName = (TextView) convertView.findViewById(R.id.mTableChannelName);
            holder.mTableHeaderImg = (ImageView) convertView.findViewById(R.id.mTableHeaderImg);
            holder.mTableHeaderImg.setLayoutParams(new RelativeLayout.LayoutParams((int) (DeviceInfoUtil.getScreenWidth() * 0.96), (int) (DeviceInfoUtil.getScreenWidth() * 0.95 * 0.555)));
            holder.mTableHeaderTitle = (TextView) convertView.findViewById(R.id.mTableHeaderTitle);
            holder.mTableSetting = (FrameLayout) convertView.findViewById(R.id.mTableSetting);
            holder.mTableHeaderWrapper = (RelativeLayout) convertView.findViewById(R.id.mTableHeaderWrapper);
            holder.mTableChannelNameWrapper = convertView.findViewById(R.id.mTableChannelNameWrapper);
            holder.mTableChannelNameWrapper.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (DeviceInfoUtil.getScreenHeight() * 0.062)));
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }
        final ArrayList<NewsFeed.Element> elementList = mNewsFeed.channels.get(groupPosition).elementList;
        holder.mTableChannelName.setText(elementList.get(0).channelName);
        ImageLoaderHelper.getImageLoader(mContext).displayImage(elementList.get(0).imgUrl, holder.mTableHeaderImg, ImageLoaderHelper.getOption());
        holder.mTableHeaderTitle.setText(elementList.get(0).title);
        convertView.setOnClickListener(null);
        return convertView;
    }


    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final ElementHolder holder;
        if (convertView == null) {
            holder = new ElementHolder();
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.aty_news_show_list_cell, null, false);
            holder.mCellImage = (ImageView) convertView.findViewById(R.id.mCellImage);
            holder.mCellSourceSiteName = (TextView) convertView.findViewById(R.id.mCellSourceSiteName);
            holder.mCellTitle = (TextView) convertView.findViewById(R.id.mCellTitle);
            holder.mCellTemperature = (TextView) convertView.findViewById(R.id.mCellTemperature);
            holder.mCellPraiseWrapper = convertView.findViewById(R.id.mCellPraiseWrapper);
            holder.mCellImageWrapper = convertView.findViewById(R.id.mCellImageWrapper);
            holder.mCellPraiseImg = (ImageView) convertView.findViewById(R.id.mCellPraiseImg);
            holder.mCellPraiseTv = (TextView) convertView.findViewById(R.id.mCellPraiseTv);
            holder.mTablePullDown = (RelativeLayout) convertView.findViewById(R.id.mTablePullDown);
            holder.mCellPraisePlus = (TextView) convertView.findViewById(R.id.mCellPraisePlus);
            convertView.setTag(holder);
        } else {
            holder = (ElementHolder) convertView.getTag();
        }
        holder.mStatus = childPosition;
        if (childPosition < 3 && childPosition == mCurrentNewsFeed.channels.get(groupPosition).elementList.size() - 1) {
            holder.mTablePullDown.setVisibility(View.VISIBLE);
        } else {
            holder.mTablePullDown.setVisibility(View.GONE);
        }
        holder.mTablePullDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.mStatus = -1;
                holder.mTablePullDown.setVisibility(View.GONE);
                for (int i = 5; i < mNewsFeed.channels.get(groupPosition).elementList.size(); i++) {
                    NewsFeed.Element element = new NewsFeed.Element();
                    NewsFeed.Element currentElement = mNewsFeed.channels.get(groupPosition).elementList.get(i);
                    element.RootClass = currentElement.RootClass;
                    element.RootName = currentElement.RootName;
                    element.channelName = currentElement.channelName;
                    element.id = currentElement.id;
                    element.imgUrl = currentElement.imgUrl;
                    element.sourceSiteName = currentElement.sourceSiteName;
                    element.sourceUrl = currentElement.sourceUrl;
                    element.title = currentElement.title;
                    element.updateTime = currentElement.updateTime;
                    element.tag = currentElement.tag;
                    mCurrentNewsFeed.channels.get(groupPosition).elementList.add(element);
                }
                notifyDataSetChanged();
            }
        });
        NewsFeed.Element element = mCurrentNewsFeed.channels.get(groupPosition).elementList.get(childPosition);
        if (TextUtils.isEmpty(element.imgUrl)) {
            holder.mCellImageWrapper.setVisibility(View.GONE);
        } else {
            ImageLoaderHelper.getImageLoader(mContext).displayImage(element.imgUrl, holder.mCellImage, ImageLoaderHelper.getOption());
        }
        holder.mCellSourceSiteName.setText(element.sourceSiteName);
        holder.mCellTitle.setText(TextUtil.trimBlankSpace(element.title));
        holder.mCellTemperature.setText(TextUtil.convertTemp(mNewsFeed.root_alias));
        convertView.setOnClickListener(null);
        if (DeviceInfoUtil.getScreenWidth() <= 480) {
            convertView.setLayoutParams(new AbsListView.LayoutParams((int) (DeviceInfoUtil.getScreenWidth() * 0.95), (int) (DeviceInfoUtil.getScreenHeight() * 0.18)));
        } else {
            convertView.setLayoutParams(new AbsListView.LayoutParams((int) (DeviceInfoUtil.getScreenWidth() * 0.95), (int) (DeviceInfoUtil.getScreenHeight() * 0.16)));
        }
        return convertView;
    }


    class GroupHolder {
        public TextView mTableChannelName;
        public ImageView mTableHeaderImg;
        public TextView mTableHeaderTitle;

        public FrameLayout mTableSetting;
        public RelativeLayout mTableHeaderWrapper;
        public View mTableChannelNameWrapper;
    }


    class ElementHolder {
        ImageView mCellImage;
        TextView mCellSourceSiteName;
        TextView mCellTitle;
        TextView mCellTemperature;
        RelativeLayout mTablePullDown;
        View mCellPraiseWrapper;
        View mCellImageWrapper;
        ImageView mCellPraiseImg;
        TextView mCellPraiseTv;
        TextView mCellPraisePlus;
        int mStatus;
    }

}
