package com.news.yazhidao.entity;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by fengjigang on 15/1/21.
 */
public class NewsDetail {
    public String rc;
    public ArrayList<ArrayList<Map<String, String>>> content;
    public ArrayList<NewsFeed.Element> elementList;
}
