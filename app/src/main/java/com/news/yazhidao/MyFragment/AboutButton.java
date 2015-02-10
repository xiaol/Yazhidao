package com.news.yazhidao.MyFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.news.yazhidao.GlobalParams;
import com.news.yazhidao.R;

/**
 * Created by Berkeley on 2/1/15.
 */
public class AboutButton extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = View.inflate(this.getActivity(), R.layout.aboutpage, null);

        if(!GlobalParams.DELETE_FLAG && GlobalParams.view != null) {
            GlobalParams.manager.removeView(GlobalParams.view);
            GlobalParams.DELETE_FLAG = true;
        }

        return view;

    }
}
