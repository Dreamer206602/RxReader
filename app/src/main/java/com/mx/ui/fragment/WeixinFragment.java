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
import com.mx.iView.IWeixinFragmentView;
import com.mx.model.weixin.WeixinNews;
import com.mx.presenter.IWeixinPresenter;
import com.mx.presenter.impl.WeixinPresenterImpl;
import com.mx.ui.adapter.WeixinAdapter;
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
public class WeixinFragment extends BaseFragment implements IWeixinFragmentView, SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @BindView(R.id.swipeToLoadLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private WeixinAdapter mWeixinAdapter;
    private IWeixinPresenter mIWeixinPresenter;
    private ArrayList<WeixinNews> mWeixinNewses = new ArrayList<>();
    private int currentPage = 1;
    private LinearLayoutManager mLinearLayoutManager;
    private boolean loading = false;
    private int pastVisiblesItems, visibleItemCount, totaItemCount;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_weixin, container, false);
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
        mIWeixinPresenter = new WeixinPresenterImpl(this, getActivity());

    }

    private void initView() {
        showProgressDialog();
        mSwipeRefreshLayout.setOnRefreshListener(this);
        setSwipeRefreshLayoutColor(mSwipeRefreshLayout);
        mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mSwipeTarget.setLayoutManager(mLinearLayoutManager);
        mSwipeTarget.setHasFixedSize(true);
        mSwipeTarget.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    visibleItemCount = mLinearLayoutManager.getChildCount();
                    totaItemCount = mLinearLayoutManager.getItemCount();
                    pastVisiblesItems = mLinearLayoutManager
                            .findFirstVisibleItemPosition();

                    if (!loading) {
                        if ((visibleItemCount + pastVisiblesItems) >= totaItemCount) {
                            loading = true;
                            onLoadMore();
                        }
                    }
                }
            }
        });
        mWeixinAdapter = new WeixinAdapter(getActivity(), mWeixinNewses);
        mSwipeTarget.setAdapter(mWeixinAdapter);
        mIWeixinPresenter.getWeiXinNews(1);
        if (SharePreferenceUtil.isRefreshOnlyWifi(getActivity())) {
            if (NetWorkUtil.isWifiConnected(getActivity())) {
                onRefresh();
            } else {
                Toast.makeText(getActivity(),
                        getString(R.string.toast_wifi_refresh_data),
                        Toast.LENGTH_SHORT).show();

            }
        }else{
            onRefresh();
        }

    }

    private void onLoadMore() {
        mIWeixinPresenter.getWeiXinNews(currentPage);

    }

    @Override
    public void updateList(ArrayList<WeixinNews> weixinNewses) {
        hideProgressDialog();
        currentPage++;
        mWeixinNewses.addAll(weixinNewses);
        mWeixinAdapter.notifyDataSetChanged();

    }

    @Override
    public void showProgressDialog() {
        if(mProgressBar!=null){
            mProgressBar.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void hideProgressDialog() {
        if(mSwipeRefreshLayout!=null){
            mSwipeRefreshLayout.setRefreshing(false);
            loading=false;
        }
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
        }

    }

    @Override
    public void showError(String error) {
        if(mSwipeRefreshLayout!=null){
            mIWeixinPresenter.getWeixinNewsfromCache(currentPage);
            Snackbar.make(mSwipeTarget,
                    getString(R.string.common_loading_error)+error,
                    Snackbar.LENGTH_INDEFINITE)
                    .setAction("重试", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mIWeixinPresenter.getWeiXinNews(currentPage);
                        }
                    }).show();
        }

    }

    @Override
    public void onRefresh() {
        hideProgressDialog();
        currentPage=1;
        mWeixinNewses.clear();;
        mWeixinAdapter.notifyDataSetChanged();
        mIWeixinPresenter.getWeiXinNews(currentPage);

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
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });


    }

}
