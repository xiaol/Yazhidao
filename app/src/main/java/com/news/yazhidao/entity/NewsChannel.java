package com.news.yazhidao.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by fengjigang on 15/3/17.
 */
@DatabaseTable(tableName = "tb_news_channel")
public class NewsChannel {
    @DatabaseField(uniqueCombo = true)
    private String root_id;
    @DatabaseField(uniqueCombo =true)
    private String channelId;
    @DatabaseField
    private String channelName;
    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getRoot_id() {
        return root_id;
    }

    public void setRoot_id(String root_id) {
        this.root_id = root_id;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }
}
