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
import com.mx.iView.IGuokrFragmentView;
import com.mx.model.guokr.GuokrHotItem;
import com.mx.presenter.IGuokrPresenter;
import com.mx.presenter.impl.GuokrPresentImpl;
import com.mx.ui.adapter.GuokrAdapter;
import com.mx.utils.NetWorkUtil;
import com.mx.utils.SharePreferenceUtil;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;

/**
 * A simple {@link Fragment} subclass.
 */
public class GuokrFragment extends BaseFragment implements IGuokrFragmentView, SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.progressBar)
    ProgressBar mProgressBar;
    @BindView(R.id.swipe_target)
    RecyclerView mSwipeTarget;
    @BindView(R.id.swipeToLoadLayout)
    SwipeRefreshLayout mSwipeToLoadLayout;

    private ArrayList<GuokrHotItem>mGuokrHotItems=new ArrayList<>();
    private GuokrAdapter mGuokrAdapter;
    private IGuokrPresenter mIGuokrPresenter;
    private LinearLayoutManager mLinearLayoutManager;
    private int currentOffset;
    private boolean loading=false;
    private  int pastVisibleItems,visibleItemCount,totalItemCount;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_guokr, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
        initView();
    }

    private void initView() {
        mSwipeToLoadLayout.setOnRefreshListener(this);
        mLinearLayoutManager=new LinearLayoutManager(getActivity());
        mSwipeTarget.setLayoutManager(mLinearLayoutManager);
        mSwipeTarget.setHasFixedSize(true);
        mSwipeTarget.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
               if(dy>0){
                   visibleItemCount=mLinearLayoutManager.getChildCount();
                   totalItemCount=mLinearLayoutManager.getItemCount();
                   pastVisibleItems=mLinearLayoutManager.findFirstVisibleItemPosition();

               if(!loading){
                   if((visibleItemCount+pastVisibleItems)>=totalItemCount){
                       loading=true;
                       onLoadMore();
                   }
               }
               }
            }
        });
        mGuokrAdapter=new GuokrAdapter(mGuokrHotItems,getActivity());
        mSwipeTarget.setAdapter(mGuokrAdapter);
        mIGuokrPresenter.getGuokrHotFromCache(0);
        if(SharePreferenceUtil.isRefreshOnlyWifi(getActivity())){
            if(NetWorkUtil.isWifiConnected(getActivity())){
                onRefresh();
            }else{
                Toast.makeText(getActivity(),
                        R.string.toast_wifi_refresh_data,
                        Toast.LENGTH_SHORT).show();
            }
        }else{
            onRefresh();
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mIGuokrPresenter.unSubscribe();

    }

    private void onLoadMore() {
        hideProgressDialog();
        mIGuokrPresenter.getGuokrhot(currentOffset);


    }

    private void initData() {
        mIGuokrPresenter=new GuokrPresentImpl(this,getActivity());
    }

    @Override
    public void update(ArrayList<GuokrHotItem> guokrHotItems) {

        currentOffset++;
        mGuokrHotItems.addAll(guokrHotItems);
        mGuokrAdapter.notifyDataSetChanged();
    }

    @Override
    public void showProgressDialog() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void hideProgressDialog() {
        if (mSwipeToLoadLayout != null) {
            mSwipeToLoadLayout.setRefreshing(false);
            loading=false;
            mProgressBar.setVisibility(View.GONE);
        }

    }

    @Override
    public void showError(String error) {
        mIGuokrPresenter.getGuokrHotFromCache(currentOffset);
        Snackbar.make(mSwipeTarget,
                getString(R.string.common_loading_error)+error,Snackbar.LENGTH_SHORT)
                .setAction(getString(R.string.comon_retry), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mIGuokrPresenter.getGuokrhot(currentOffset);
                    }
                }).show();

    }

    @Override
    public void onRefresh() {
        hideProgressDialog();
        currentOffset=0;
        mGuokrHotItems.clear();
        mGuokrAdapter.notifyDataSetChanged();
        mIGuokrPresenter.getGuokrhot(currentOffset);

        Observable.timer(4, TimeUnit.SECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
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
