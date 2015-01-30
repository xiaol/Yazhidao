package com.news.yazhidao.net;

import com.news.yazhidao.utils.GJsonUtil;
import com.news.yazhidao.utils.Logger;

import java.lang.reflect.Type;

/**
 * Created by fengjigang on 15/1/7.
 */
public abstract class JsonCallback<T> extends AbstractCallBack<T>{
    private Class<T> mReturnClass;
    private Type mReturnType;
    @Override
    protected T bindData(String json) {
        Logger.i(">>> json ", json);
        if(mReturnClass!=null){
            return GJsonUtil.deSerializedByClass(json, mReturnClass);
        }else if(mReturnType!=null){
            return GJsonUtil.deSerializedByType(json,mReturnType);
        }
        return null;
    }
    public JsonCallback setReturnClass(Class<T> clazz){
        this.mReturnClass=clazz;
        return this;
    }
    public JsonCallback setReturnType(Type type){
        this.mReturnType=type;
        return this;
    }


}
