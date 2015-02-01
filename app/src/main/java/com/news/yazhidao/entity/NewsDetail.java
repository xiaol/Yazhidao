package com.news.yazhidao.entity;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by fengjigang on 15/1/21.
 */
public class NewsDetail {
    public ResponseBody response_body;

    public class FetchContentArr {
        public ArrayList<ArrayList<Map<String, String>>> content;
        public ArrayList<NewsFeed.Element> elementList;
    }

    public class ResponseBody {
        public FetchContentArr FetchContent;
    }

    public ResponseHead response_head;

    public class ResponseHead {
        public String msg;//ok
        public String result;//success
    }
}
