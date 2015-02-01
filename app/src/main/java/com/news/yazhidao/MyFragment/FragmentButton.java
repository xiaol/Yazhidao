package com.news.yazhidao.MyFragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.news.yazhidao.R;
import com.news.yazhidao.UI.MyRelativeLayout;
import com.news.yazhidao.constant.CommonConstant;
import com.news.yazhidao.constant.HttpConstant;
import com.news.yazhidao.entity.NewsFeed;
import com.news.yazhidao.net.JsonCallback;
import com.news.yazhidao.net.MyAppException;
import com.news.yazhidao.net.NetworkRequest;
import com.news.yazhidao.pages.NewsFeedAdapter;
import com.news.yazhidao.utils.Logger;
import com.news.yazhidao.utils.gifview.GifView;


/**
 * Created by neokree on 31/12/14.
 */
public class FragmentButton extends Fragment {
    private static final String TAG = "FragmentButton";
    private MyRelativeLayout rl_content;
    private static ListView mNewsShowList;
    private static NewsFeedAdapter mNewsFeedAdapter;
    public static NewsFeed mNewsFeed;
    public static final String ARG_PLANET_NUMBER = "planet_number";
    private ChangeNewsModulBroRec mChangeNewsModulBroRec = new ChangeNewsModulBroRec();

    public class ChangeNewsModulBroRec extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int newsModulePos = intent.getIntExtra(CommonConstant.KEY_NEWS_MODULE_POSITION, 0);
            Logger.i(TAG, ">>>change news module ");
            loadNewsData(context, newsModulePos);
        }
    }

    private void registerBReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(CommonConstant.ACTION_CHANGE_NEWS_MODULE);
        getActivity().registerReceiver(mChangeNewsModulBroRec, filter);
    }

    private void unregisterBReceiver() {
        getActivity().unregisterReceiver(mChangeNewsModulBroRec);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        registerBReceiver();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        unregisterBReceiver();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(this.getActivity(), R.layout.fragment_planet, null);


        rl_content = (MyRelativeLayout) view.findViewById(R.id.rl_content);

        mNewsShowList = (ListView) view.findViewById(R.id.mNewsShowList);
        mNewsFeed = new NewsFeed();
        mNewsFeedAdapter = new NewsFeedAdapter(this.getActivity(), mNewsFeed);
        mNewsShowList.setAdapter(mNewsFeedAdapter);

        loadNewsData(getActivity(), 0);

        return view;

    }


    private static void loadNewsData(final Context mContext, int newsModulePos) {
        NetworkRequest request = new NetworkRequest(HttpConstant.URL_FETCH_NEWS_FOR_MODULE + newsModulePos, NetworkRequest.RequestMethod.GET);
        request.setCallback(new JsonCallback<NewsFeed>() {

            @Override
            public void success(NewsFeed result) {
                Log.i(">>>" + TAG, result.toString());
                mNewsFeed = result;
                Log.i(">>>" + TAG, mNewsFeed.response_head.result);
                mNewsFeedAdapter = new NewsFeedAdapter(mContext, mNewsFeed);
                mNewsShowList.setAdapter(mNewsFeedAdapter);
                mNewsFeedAdapter.notifyDataSetChanged();
            }

            public void failed(MyAppException exception) {
                Log.i(">>>" + TAG, exception.getMessage());
            }
        }.setReturnType(new TypeToken<NewsFeed>() {
        }.getType()));
        request.execute();
    }
}
