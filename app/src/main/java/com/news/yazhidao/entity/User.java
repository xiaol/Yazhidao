package com.news.yazhidao.entity;

import com.google.gson.reflect.TypeToken;
import com.news.yazhidao.utils.GsonUtil;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by fengjigang on 15/2/2.
 */
public class User implements Serializable{
    private String uuid;
    private String id;
    private String token;
    private String profileImageUrl;
    private String gender;
    private String createTime;
    private String screenName;
    private String type;
    private ArrayList<String> subscribedTags;


    public ArrayList<String> getSubscribedTags() {
        return subscribedTags;
    }

    public void setSubscribedTags(ArrayList<String> subscribedTags) {
        this.subscribedTags = subscribedTags;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String toJsonString(){
        return GsonUtil.serialized(this);
    }

    @Override
    public String toString() {
        return "uuid="+uuid+",id="+id+",token="+token+",screenName="+screenName;
    }

    public static User parseUser(String userStr) {
        return GsonUtil.deSerializedByType(userStr, new TypeToken<User>(){}.getType());
    }
}
