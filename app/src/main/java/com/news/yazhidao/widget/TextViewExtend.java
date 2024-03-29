package com.news.yazhidao.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewExtend extends TextView {
    private static final String TAG = "TextViewExtend";

    public TextViewExtend(Context context) {
        super(context);
    }

    public TextViewExtend(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomFont(context, attrs);
    }

    public TextViewExtend(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }

    private void setCustomFont(Context ctx, AttributeSet attrs) {
//        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.TextViewPlus);
//        String customFont = a.getString(R.styleable.TextViewPlus_customFont);
//        setCustomFont(ctx, customFont);
//        a.recycle();
    }

    public boolean setCustomFont(Context ctx, String asset) {

//        Typeface tf = FontCache.get(asset,ctx);
//
//        if(tf==null)
//        {
//            return false;
//        }
//
//        setTypeface(tf);
        return true;
    }

}