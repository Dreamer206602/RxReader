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
import com.mx.iView.ItHomeFragmentView;
import com.mx.model.ithome.ItHomeItem;
import com.mx.presenter.IItHomePresenter;
import com.mx.presenter.impl.ItHomePresenterImpl;
import com.mx.ui.adapter.ItHomeAdapter;
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
public class ItHomeFragment extends Fragment implements ItHomeFragmentView, SwipeRefreshLayout.OnRefreshListener {


    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @BindView(R.id.swipeToLoadLayout)
    SwipeRefreshLayout mSwipeToLoadLayout;

    private ItHomeAdapter mItHomeAdapter;
    private IItHomePresenter mIItHomePresenter;
    private ArrayList<ItHomeItem>mItems=new ArrayList<>();
    private String currentNewsId="0";
    private LinearLayoutManager mLinearLayoutManager;
    private boolean isLoading=false;
    private int pastVisibleItems,visibleItemCount,totalItemCount;




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_it_home, container, false);
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
        mIItHomePresenter=new ItHomePresenterImpl(this,getActivity());

    }

    private void initView() {
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mLinearLayoutManager=new LinearLayoutManager(getActivity());
        mSwipeTarget.setHasFixedSize(true);
        mSwipeTarget.setLayoutManager(mLinearLayoutManager);
        mSwipeTarget.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if(dy>0){
                    pastVisibleItems=mLinearLayoutManager.findFirstVisibleItemPosition();
                    totalItemCount=mLinearLayoutManager.getItemCount();
                    visibleItemCount=mLinearLayoutManager.getChildCount();
                    if(!isLoading){
                        if((visibleItemCount+pastVisibleItems)>=totalItemCount){
                            onLoadMore();
                        }
                    }
                }
            }
        });
        mItHomeAdapter=new ItHomeAdapter(getActivity(),mItems);
        mSwipeTarget.setAdapter(mItHomeAdapter);
        mIItHomePresenter.getNewsFromCache();
        if(SharePreferenceUtil.isRefreshOnlyWifi(getActivity())){
            if(NetWorkUtil.isWifiConnected(getActivity())){
                onRefresh();
            }else{
                Toast.makeText(getActivity(), getString(R.string.toast_wifi_refresh_data),
                        Toast.LENGTH_SHORT).show();
            }
        }else{
            onRefresh();
        }

    }

    private void onLoadMore() {
        mIItHomePresenter.getMoreItHomeNews(currentNewsId);
    }

    @Override
    public void updateList(ArrayList<ItHomeItem> itHomeItems) {
        currentNewsId=itHomeItems.get(itHomeItems.size()-1).getNewsid();
        mItems.addAll(itHomeItems);
        mItHomeAdapter.notifyDataSetChanged();

    }

    @Override
    public void showProgressDialog() {
        if (mProgressBar != null) {
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
        Snackbar.make(mSwipeToLoadLayout, getString(R.string.common_loading_error) + error, Snackbar.LENGTH_SHORT).setAction(getString(R.string.comon_retry), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentNewsId.equals("0")) {
                    mIItHomePresenter.getNewItHomeNews();
                } else {
                    mIItHomePresenter.getMoreItHomeNews(currentNewsId);
                }
            }
        }).show();

    }

    @Override
    public void onRefresh() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.GONE);
        }
        currentNewsId="0";
        mItems.clear();
        //2016-04-05修复Inconsistency detected. Invalid view holder adapter positionViewHolder
        mItHomeAdapter.notifyDataSetChanged();
        mIItHomePresenter.getNewItHomeNews();

        Observable.timer(3, TimeUnit.SECONDS)
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mIItHomePresenter.unSubscribe();
    }
}
