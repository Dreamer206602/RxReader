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

import com.mx.R;
import com.mx.iView.IZhihuFragmentView;
import com.mx.model.zhihu.ZhihuDaily;
import com.mx.model.zhihu.ZhihuDailyItem;
import com.mx.presenter.IZhihuPresenter;
import com.mx.presenter.impl.ZhihuPresenterImpl;
import com.mx.ui.adapter.ZhihuAdapter;
import com.mx.utils.NetWorkUtil;
import com.mx.utils.SharePreferenceUtil;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class ZhihuFragment extends BaseFragment implements IZhihuFragmentView, SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @BindView(R.id.swipeToLoadLayout)
    SwipeRefreshLayout mSwipeToLoadLayout;


    private String currentLoaderDate;
    private ZhihuAdapter mZhihuAdapter;
    private IZhihuPresenter mIZhihuPresenter;
    private ArrayList<ZhihuDailyItem>mZhihuDailyItems=new ArrayList<>();
    private LinearLayoutManager mLinearLayoutManager;
    private boolean isLoading=false;
    private int pastVisibleItems,visibleItemCount,totalItemCount;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_zhihu, container, false);
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
        mIZhihuPresenter=new ZhihuPresenterImpl(this,getActivity());
    }

    private void initView() {
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mLinearLayoutManager=new LinearLayoutManager(getActivity());
        mSwipeTarget.setLayoutManager(mLinearLayoutManager);
        mSwipeTarget.setHasFixedSize(true);
        mZhihuAdapter=new ZhihuAdapter(getActivity(),mZhihuDailyItems);
        mSwipeTarget.setAdapter(mZhihuAdapter);
        mSwipeTarget.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        mIZhihuPresenter.getLastFromCache();
        if(SharePreferenceUtil.isRefreshOnlyWifi(getActivity())){
            if(NetWorkUtil.isWifiConnected(getActivity())){
                onRefresh();
            }
        }else{
            onRefresh();
        }
    }

    private void onLoadMore() {
        mIZhihuPresenter.getTheDaily(currentLoaderDate);

    }

    @Override
    public void updateList(ZhihuDaily zhihuDaily) {
        currentLoaderDate=zhihuDaily.getDate();
        mZhihuDailyItems.addAll(zhihuDaily.getStories());
        mZhihuAdapter.notifyDataSetChanged();
        //若未填满屏幕
        if(!mSwipeTarget.canScrollVertically(View.SCROLL_INDICATOR_BOTTOM)){
            onLoadMore();
        }

    }

    @Override
    public void showProgressDialog() {

        if (mSwipeTarget != null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideProgressDialog() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
        }
        if (mSwipeToLoadLayout != null) {
            mSwipeToLoadLayout.setRefreshing(false);
            isLoading=false;
        }

    }

    @Override
    public void showError(String error) {

        if (mSwipeTarget != null) {
            Snackbar.make(mSwipeTarget,getString(R.string.common_loading_error)+error,
                    Snackbar.LENGTH_SHORT)
                    .setAction("重试", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(currentLoaderDate.equals("0")){
                                mIZhihuPresenter.getLastZhihuNews();
                            }else{
                                mIZhihuPresenter.getTheDaily(currentLoaderDate);
                            }
                        }
                    }).show();
        }
    }

    @Override
    public void onRefresh() {
        currentLoaderDate="0";
        mZhihuDailyItems.clear();
        mZhihuAdapter.notifyDataSetChanged();
        mIZhihuPresenter.getLastZhihuNews();

        Observable.timer(4, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        mSwipeToLoadLayout.setRefreshing(false);
                    }
                });
    }
}
