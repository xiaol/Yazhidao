
package com.news.yazhidao.entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * 用于保存到数据库的新闻具体entity
 * Created by fengjigang on 15/3/13.
 */
@DatabaseTable(tableName = "tb_news_element")
public class NewsElement implements Serializable {
    @DatabaseField(columnName = "id",id = true)
    private String id;
    @DatabaseField(columnName = "root_id")
    private String root_id;
    @DatabaseField(columnName = "root_name")
    private String root_name;
    @DatabaseField(columnName = "channelId")
    private int channelId;

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    @DatabaseField(columnName = "channelName")

    private String channelName;
    @DatabaseField(columnName = "imgUrl")
    private String imgUrl;
    @DatabaseField(columnName = "sourceSiteName")
    private String sourceSiteName;
    @DatabaseField(columnName = "sourceUrl")
    private String sourceUrl;
    @DatabaseField(columnName = "title")
    private String title;
    @DatabaseField(columnName = "updateTime")
    private String updateTime;
    @DatabaseField(columnName = "downNum")
    private Integer downNum;
    @DatabaseField(columnName = "favorNum")
    private Integer favorNum;

    public Integer getDownNum() {
        return downNum;
    }

    public void setDownNum(Integer downNum) {
        this.downNum = downNum;
    }

    public Integer getFavorNum() {
        return favorNum;
    }

    public void setFavorNum(Integer favorNum) {
        this.favorNum = favorNum;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoot_id() {
        return root_id;
    }

    public void setRoot_id(String root_id) {
        this.root_id = root_id;
    }

    public String getRoot_name() {
        return root_name;
    }

    public void setRoot_name(String root_name) {
        this.root_name = root_name;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getSourceSiteName() {
        return sourceSiteName;
    }

    public void setSourceSiteName(String sourceSiteName) {
        this.sourceSiteName = sourceSiteName;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
        return "id="+id+",root_id="+root_id+",channelid="+channelId;
    }
}
