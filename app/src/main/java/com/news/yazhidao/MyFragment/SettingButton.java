package com.news.yazhidao.MyFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.news.yazhidao.GlobalParams;
import com.news.yazhidao.R;

/**
 * Created by Berkeley on 2/1/15.
 */
public class SettingButton extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        View view = View.inflate(this.getActivity(), R.layout.settingpage, null);

        TextView iv = (TextView) GlobalParams.view.findViewById(R.id.iv_sun);
        iv.setVisibility(View.GONE);

        return view;

    }

}
