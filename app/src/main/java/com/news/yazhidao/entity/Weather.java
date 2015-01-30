package com.news.yazhidao.entity;

/**
 * Created by fengjigang on 14-10-25.
 */
public class Weather {
//    "code": "31", "text": "Clear", "high": "14", "low": "-2", "date": "9 Nov 2014", "day": "Sun"},
    String text;//天气的状况
    String high;//白天的气温
    String low;//夜晚的气温
    String day;//当天是星期几

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    @Override
    public String toString() {
        return text+"="+low+"="+high+"="+day;
    }
}
