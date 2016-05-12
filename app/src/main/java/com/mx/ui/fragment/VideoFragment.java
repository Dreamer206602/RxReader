package com.mx.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mx.R;
import com.mx.iView.IVideoFragmentView;
import com.mx.model.weiboVideo.WeiboVideoBlog;
import com.mx.presenter.IVideoPresenter;
import com.mx.presenter.impl.VideoPresenterImpl;
import com.mx.ui.adapter.VideoAdapter;
import com.mx.utils.NetWorkUtil;
import com.mx.utils.SharePreferenceUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class VideoFragment extends BaseFragment implements IVideoFragmentView, SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.swipe_target)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipeToLoadLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private LinearLayoutManager mLinearLayoutManager;
    private boolean isLoading=false;
    private int pastVisibleItems,visibleItemCount,totalItemCount;
    private ArrayList<WeiboVideoBlog>mWeiboVideoBlogs=new ArrayList<>();
    private int currentPage=1;
    private IVideoPresenter mIVideoPresenter;
    private VideoAdapter mVideoAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView();
    }

    private void initData() {
        mIVideoPresenter=new VideoPresenterImpl(this,getActivity());
    }

    private void initView() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
        setSwipeRefreshLayoutColor(mSwipeRefreshLayout);
        mLinearLayoutManager=new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(dy>0){
                    visibleItemCount=mLinearLayoutManager.getChildCount();
                    totalItemCount=mLinearLayoutManager.getItemCount();
                    pastVisibleItems=mLinearLayoutManager.findFirstVisibleItemPosition();

                    if(!isLoading){
                        if((visibleItemCount+pastVisibleItems)>=totalItemCount){
                            isLoading=true;
                            onLoadMore();
                        }
                    }
                }
            }
        });
        mVideoAdapter=new VideoAdapter(getActivity(),mWeiboVideoBlogs);
        mRecyclerView.setAdapter(mVideoAdapter);
        mIVideoPresenter.getVideoFromCache(1);
        if(SharePreferenceUtil.isRefreshOnlyWifi(getActivity())){
            if(NetWorkUtil.isWifiConnected(getActivity())){
                onRefresh();
            }else{
                Toast.makeText(getActivity(),
                        getString(R.string.toast_wifi_refresh_data),
                        Toast.LENGTH_SHORT).show();
            }
        }else{
            onRefresh();
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIVideoPresenter.unSubscribe();
    }

    private void onLoadMore() {
        mIVideoPresenter.getVideo(currentPage);

    }

    @Override
    public void updateList(ArrayList<WeiboVideoBlog> weiboVideoBlogs) {
        currentPage++;
        mWeiboVideoBlogs.addAll(weiboVideoBlogs);
        mVideoAdapter.notifyDataSetChanged();
    }

    @Override
    public void showProgressDialog() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void hideProgressDialog() {

        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
            isLoading=false;
        }
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
        }



    }

    @Override
    public void showError(String error) {
        if (mRecyclerView != null) {
            mIVideoPresenter.getVideoFromCache(currentPage);
            Snackbar.make(mRecyclerView, getString(R.string.common_loading_error) + error,
                    Snackbar.LENGTH_SHORT).setAction("重试", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mIVideoPresenter.getVideo(currentPage);
                }
            }).show();
        }

    }

    @Override
    public void onRefresh() {

        currentPage=1;
        mWeiboVideoBlogs.clear();
        mVideoAdapter.notifyDataSetChanged();
        mIVideoPresenter.getVideo(currentPage);

    }
}
