package com.news.yazhidao.net;

/**
 * Created by fengjigang on 15/1/6.
 */
public abstract class StringCallback extends AbstractCallBack<String>{
    @Override
    protected String bindData(String content) {
        return content;
    }
}
