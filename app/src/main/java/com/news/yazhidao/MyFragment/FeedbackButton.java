package com.news.yazhidao.MyFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.news.yazhidao.constant.GlobalParams;
import com.news.yazhidao.R;
import com.news.yazhidao.utils.DensityUtil;

/**
 * Created by Berkeley on 2/1/15.
 */
public class FeedbackButton extends Fragment {

    private EditText et_email;
    private EditText et_content;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = View.inflate(this.getActivity(), R.layout.feedbackpage, null);
        if(!GlobalParams.DELETE_FLAG && GlobalParams.view != null) {
            GlobalParams.manager.removeView(GlobalParams.view);
            GlobalParams.DELETE_FLAG = true;
        }

        et_email = (EditText) view.findViewById(R.id.et_email);

        int a = DensityUtil.dip2px(getActivity(), 10);
        et_email.setPadding(a,a,a,a);

        et_content = (EditText) view.findViewById(R.id.et_content);
        et_content.setPadding(a,a,a,a);

        return view;

    }
}
