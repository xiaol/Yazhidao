package com.news.yazhidao.entity;

import com.google.gson.reflect.TypeToken;
import com.news.yazhidao.utils.GsonUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by fengjigang on 15/2/2.
 */
public class User implements Serializable{
    private String uuid;
    private String sinaId;
    private String sinaToken;
    private String gender;
    private String createTime;
    private String screenName;
    private ArrayList<String> subscribedTags;

    public ArrayList<String> getSubscribedTags() {
        return subscribedTags;
    }

    public void setSubscribedTags(ArrayList<String> subscribedTags) {
        this.subscribedTags = subscribedTags;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getScreenName() {
        return screenName;
    }

    public void setScreenName(String screenName) {
        this.screenName = screenName;
    }

    private String sinaProfileImageUrl;

    public String getUUID() {
        return uuid;
    }

    public void setUUID(String uuid) {
        this.uuid = uuid;
    }

    public String getSinaId() {
        return sinaId;
    }

    public void setSinaId(String sinaId) {
        this.sinaId = sinaId;
    }

    public String getSinaToken() {
        return sinaToken;
    }

    public void setSinaToken(String sinaToken) {
        this.sinaToken = sinaToken;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getSinaProfileImageUrl() {
        return sinaProfileImageUrl;
    }

    public void setSinaProfileImageUrl(String sinaProfileImageUrl) {
        this.sinaProfileImageUrl = sinaProfileImageUrl;
    }
    public String toJsonString(){
        return GsonUtil.serialized(this);
    }

    @Override
    public String toString() {
        return "uuid="+uuid+",sinaId="+sinaId+",sinaToken="+sinaToken+",screenName="+screenName;
    }

    public static User parseUser(String userStr) {
        return GsonUtil.deSerializedByType(userStr, new TypeToken<User>(){}.getType());
    }
}
