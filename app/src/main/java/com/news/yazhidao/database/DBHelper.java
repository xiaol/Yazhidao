package com.news.yazhidao.database;

import android.text.TextUtils;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.news.yazhidao.application.YaZhiDaoApplication;
import com.news.yazhidao.entity.NewsChannel;
import com.news.yazhidao.entity.NewsElement;
import com.news.yazhidao.entity.NewsFeed;
import com.news.yazhidao.entity.NewsRoot;
import com.news.yazhidao.utils.Logger;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by fengjigang on 15/3/16.
 */
public class DBHelper {
    private static final String TAG = "DBHelper";
    public static final String TABLE_COLUMN_ROOTID = "root_id";
    public static final String TABLE_COLUMN_CHANNELID = "channelid";
    public static final String TABLE_COLUMN_UPDATETIME = "updateTime";

    /**
     * 获取数据库操作的dao
     *
     * @param pClazz
     * @return
     */
    private static Dao getDao(Class pClazz) {
        NewsSqliteHelperOrmlite _Helper = new NewsSqliteHelperOrmlite(YaZhiDaoApplication.getAppContext());
        try {
            return _Helper.getDao(pClazz);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 把当前获取到的新闻数据源拆分成具体的Element存放到数据库中
     *
     * @param pNewsFeed
     */
    public static void insert(NewsFeed pNewsFeed) {
        long start = System.currentTimeMillis();
        try {
            NewsElement _NewsElement = null;
            ArrayList<NewsFeed.Channel> _Channels = pNewsFeed.channels;
            if (_Channels == null) {
                return;
            }
            //init the NewsRoot object
            NewsRoot _NewsRoot = new NewsRoot(pNewsFeed.root_id, pNewsFeed.root_name, pNewsFeed.root_alias);
            getDao(NewsRoot.class).create(_NewsRoot);
            //init the NewsChannelList object
            NewsChannel _NewsChannel = null;
            for (int i = 0; i < _Channels.size(); i++) {
                _NewsChannel = new NewsChannel();
                _NewsChannel.setChannelId(_Channels.get(i).channelId);
                _NewsChannel.setRoot_id(pNewsFeed.root_id);
                _NewsChannel.setChannelName(_Channels.get(i).channelName);
                getDao(NewsChannel.class).create(_NewsChannel);
            }
            //init the NewsElementList object
            for (int i = 0; i < _Channels.size(); i++) {
                NewsFeed.Channel _Channel = _Channels.get(i);
                ArrayList<NewsFeed.Element> _ElementList = _Channel.elementList;
                for (NewsFeed.Element element : _ElementList) {
                    _NewsElement = new NewsElement();
                    _NewsElement.setId(element.id);
                    _NewsElement.setChannelId(i);
                    _NewsElement.setChannelName(_Channel.channelName);
                    _NewsElement.setRoot_id(pNewsFeed.root_id);
                    _NewsElement.setRoot_name(pNewsFeed.root_name);
                    _NewsElement.setImgUrl(element.imgUrl);
                    _NewsElement.setSourceUrl(element.sourceUrl);
                    _NewsElement.setSourceSiteName(element.sourceSiteName);
                    _NewsElement.setTitle(element.title);
                    _NewsElement.setUpdateTime(element.updateTime);
                    _NewsElement.setDownNum(element.downNum);
                    _NewsElement.setFavorNum(element.favorNum);
                    getDao(NewsElement.class).create(_NewsElement);
                }
            }
            Logger.e("insert db consume time ", "time=" + (System.currentTimeMillis() - start));
        } catch (SQLException e) {
            e.printStackTrace();
            Logger.e(TAG, "insert " + e.getMessage());
        }
    }

    public static NewsFeed queryByRootId(int pRoot_id) {
        //创建一个NewsFeed对象
        NewsFeed _NewsFeed = new NewsFeed();
        _NewsFeed.channels = new ArrayList<>();
        try {
            //获取所有的root_id
            NewsRoot _NewsRoot = (NewsRoot) getDao(NewsRoot.class).queryForId(pRoot_id);
            if (_NewsRoot == null) {
                return null;
            }
            _NewsFeed.root_id = _NewsRoot.getRoot_id();
            _NewsFeed.root_name = _NewsRoot.getRoot_name();
            _NewsFeed.root_alias = _NewsRoot.getRoot_alias();
            //获取该root_id对应的所有channels
            Dao<NewsChannel, ?> _ChannelDao = getDao(NewsChannel.class);
            QueryBuilder<NewsChannel, ?> _ChannelBuilder = _ChannelDao.queryBuilder();
            _ChannelBuilder.where().eq(TABLE_COLUMN_ROOTID, pRoot_id);
            List<NewsChannel> _ChannelList = _ChannelDao.query(_ChannelBuilder.prepare());
            //获取root_id对应的channels下所有的element
            Dao<NewsElement, ?> _ElementDao = getDao(NewsElement.class);
            QueryBuilder<NewsElement, ?> _ElementBuilder = _ElementDao.queryBuilder();
            if (_ChannelList != null) {
                NewsFeed.Channel _NewsFeedChanel = null;
                for (int i = 0; i < _ChannelList.size(); i++) {
                    NewsChannel _NewsChannel = _ChannelList.get(i);
                    _ElementBuilder.limit(9l).orderBy(TABLE_COLUMN_UPDATETIME, false).where().eq(TABLE_COLUMN_ROOTID, pRoot_id).and().eq(TABLE_COLUMN_CHANNELID, _NewsChannel.getChannelId());
                    List<NewsElement> _NewsElementList = _ElementDao.query(_ElementBuilder.prepare());
                    _NewsFeedChanel = new NewsFeed.Channel();
                    _NewsFeedChanel.channelId = _NewsChannel.getChannelId();
                    _NewsFeedChanel.channelName = _NewsChannel.getChannelName();
                    _NewsFeedChanel.elementList = convertNewsElement(_NewsElementList);
                    _NewsFeed.channels.add(_NewsFeedChanel);
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return _NewsFeed;
    }

    /**
     * 把NewsElement集合 转换成NewsFeed.Element集合
     *
     * @param pNewsElementList
     * @return
     */
    private static ArrayList<NewsFeed.Element> convertNewsElement(List<NewsElement> pNewsElementList) {
        ArrayList<NewsFeed.Element> _NewsFeedList = new ArrayList<>();
        NewsFeed.Element _NewsElement = null;
        if (pNewsElementList != null) {
            for (NewsElement element : pNewsElementList) {
                _NewsElement = new NewsFeed.Element();
                _NewsElement.id = element.getId();
                _NewsElement.sourceUrl = element.getSourceUrl();
                _NewsElement.title = element.getTitle();
                _NewsElement.channelName = element.getChannelName();
                _NewsElement.downNum = element.getDownNum();
                _NewsElement.favorNum = element.getFavorNum();
                _NewsElement.imgUrl = element.getImgUrl();
                _NewsElement.sourceSiteName = element.getSourceSiteName();
                _NewsElement.updateTime = element.getUpdateTime();
                _NewsFeedList.add(_NewsElement);
            }
        }

        return ensureFirstItemHasImg(_NewsFeedList);
    }

    /**
     * 确保从数据库中取出的数据头图有图片
     *
     * @param pOriginList
     * @return
     */
    private static ArrayList<NewsFeed.Element> ensureFirstItemHasImg(ArrayList<NewsFeed.Element> pOriginList) {
        ArrayList<NewsFeed.Element> _TargetList = new ArrayList<>();
        ArrayList<NewsFeed.Element> _HasImgList = new ArrayList<>();
        Iterator<NewsFeed.Element> _Iterator = pOriginList.iterator();
        while (_Iterator.hasNext()) {
            NewsFeed.Element _Element = _Iterator.next();
            if (!TextUtils.isEmpty(_Element.imgUrl)) {
                _HasImgList.add(_Element);
                _Iterator.remove();
            }
        }
        _TargetList.addAll(_HasImgList);
        _TargetList.addAll(pOriginList);
        return _TargetList;
    }
}
