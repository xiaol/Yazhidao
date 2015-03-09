package com.news.yazhidao.MyFragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.news.yazhidao.constant.GlobalParams;
import com.news.yazhidao.R;

/**
 * Created by Berkeley on 2/1/15.
 */
public class SettingButton extends Fragment {

    private ImageView iv_push;
    private boolean pushflag = false;
    private ImageView iv_wifi;
    private boolean wififag = true;

    @Override
    public View onCreateView(LayoutInflater inflater,@Nullable ViewGroup container,@Nullable Bundle savedInstanceState) {
        View view = View.inflate(this.getActivity(), R.layout.settingpage, null);

        if(!GlobalParams.DELETE_FLAG && GlobalParams.view != null) {
            GlobalParams.manager.removeView(GlobalParams.view);
            GlobalParams.DELETE_FLAG = true;
        }

        GlobalParams.ADD_SUN_FLAG = false;

        iv_push = (ImageView) view.findViewById(R.id.iv_push);
        iv_push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pushflag) {
                    iv_push.setBackgroundResource(R.drawable.switch_close);
                    pushflag = false;
                }else{
                    iv_push.setBackgroundResource(R.drawable.switch_open);
                    pushflag = true;
                }
            }
        });


        iv_wifi = (ImageView) view.findViewById(R.id.iv_wifi);
        iv_wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(wififag) {
                    iv_wifi.setBackgroundResource(R.drawable.switch_close);
                    wififag = false;
                }else{
                    iv_wifi.setBackgroundResource(R.drawable.switch_open);
                    wififag = true;
                }
            }
        });

        return view;

    }

}
