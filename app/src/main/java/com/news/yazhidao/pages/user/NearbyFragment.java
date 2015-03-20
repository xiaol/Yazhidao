package com.news.yazhidao.pages.user;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.Toast;
import com.google.gson.reflect.TypeToken;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshExpandableListView;
import com.news.yazhidao.R;
import com.news.yazhidao.constant.GlobalParams;
import com.news.yazhidao.constant.HttpConstant;
import com.news.yazhidao.entity.NewsFeed;
import com.news.yazhidao.net.JsonCallback;
import com.news.yazhidao.net.MyAppException;
import com.news.yazhidao.net.NetworkRequest;
import com.news.yazhidao.utils.DeviceInfoUtil;

import java.util.HashMap;

public class NearbyFragment extends Fragment {

    private View rootView;
    private PullToRefreshExpandableListView m_plvNearby;
    private ExpandableWallListViewAdapter m_pNearbyAdapter;
    NewsFeed mNewsFeed;
//    private BUNearbyHeaderView m_pBUNearbyHeaderView;
    private int m_pNum;
    private boolean m_pIsContinue = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_nearby, container, false);
        initVars();
        findViews();
        return rootView;
    }


    private void initVars() {
        mNewsFeed = new NewsFeed();
        m_pNearbyAdapter = new ExpandableWallListViewAdapter(getActivity());
//        m_pBUNearbyHeaderView = new BUNearbyHeaderView(getActivity());

    }

    private void findViews() {
        m_plvNearby = (PullToRefreshExpandableListView) rootView.findViewById(R.id.nearby_scrollView);
        ExpandableListView mExpandableListView = m_plvNearby.getRefreshableView();
//        mExpandableListView.addHeaderView(m_pBUNearbyHeaderView);
        m_plvNearby.setMode(PullToRefreshBase.Mode.DISABLED);
        m_plvNearby.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ExpandableListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ExpandableListView> refreshView) {
//                TSFilterMgr.getInstance().RequestAllCategories();
//                TSLocalWallMgr.getInstance().RequestLocalWalls();
            }
        });
        mExpandableListView.setAdapter(m_pNearbyAdapter);

    }

    public void initData() {
        loadNewsData(getActivity(),1);
    }


    @Override
    public void onStart() {
        super.onStart();
        m_pNearbyAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    private void loadNewsData(final Context mContext, int newsModulePos) {

        GlobalParams.LISTVIEW_HEIGHT = 0;
        GlobalParams.LISTVIEW_ERROR = 0;

        GlobalParams.REFRESH_FLAG = true;
        NetworkRequest request = new NetworkRequest(HttpConstant.URL_FETCH_NEWS_FOR_MODULE, NetworkRequest.RequestMethod.GET);
        HashMap<String, Object> params = new HashMap<>();
        params.put("limit", 9);
        params.put("root_class", newsModulePos);
        params.put("uuid", DeviceInfoUtil.getUUID());
        request.getParams = params;
        request.setCallback(new JsonCallback<NewsFeed>() {

            @Override
            public void success(NewsFeed result) {
                if (result != null) {
                    mNewsFeed = result;
                    m_pNearbyAdapter.setArrNews(mNewsFeed);
                    ExpandableListView mExpandableListView = m_plvNearby.getRefreshableView();
                    for (int i = 0; i < m_pNearbyAdapter.getGroupCount(); i++) {
                        mExpandableListView.expandGroup(i);
                    }
                    m_pNearbyAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(mContext, "网络异常，请查看网络...", Toast.LENGTH_SHORT).show();
                }

            }

            public void failed(MyAppException exception) {
            }
        }.setReturnType(new TypeToken<NewsFeed>() {
        }.getType()));
        request.execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}