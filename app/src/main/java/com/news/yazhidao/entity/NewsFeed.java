package com.news.yazhidao.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by fengjigang on 15/1/16.
 */
public class NewsFeed {
    public ResponseHead response_head;

    public class ResponseHead{
        public String msg;
        public String result;
    }
    public ResponseBody response_body;
    public class ResponseBody {
        public GetAllChannels getAllChannels;
    }
    public class GetAllChannels{
        public ArrayList<Channel> channels;
        public String root_alias;
        public String root_id;
        public String root_name;
    }
    public class Channel{
        public String channelAlias;
        public int channelId;
        public String channelName;
        public ArrayList<Element> elementList;
        public String lastUpdateTime;
    }
    public class Element implements Serializable{
     public String  channelName;
     public String  id;
     public String  imgUrl;
     public String  sourceSiteName;
     public String  sourceUrl;
     public String  title;
     public String  updateTime;
     public ArrayList<String> tag;

        @Override
        public String toString() {
            return id+","+channelName+","+imgUrl+","+sourceSiteName+","+sourceUrl+","+title+","+updateTime;
        }
    }
}
