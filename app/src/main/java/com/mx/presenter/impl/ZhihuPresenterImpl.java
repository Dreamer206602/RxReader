package com.mx.presenter.impl;

import android.content.Context;

import com.google.gson.Gson;
import com.mx.api.zhihu.ZhihuRequest;
import com.mx.config.Config;
import com.mx.iView.IZhihuFragmentView;
import com.mx.model.zhihu.ZhihuDaily;
import com.mx.model.zhihu.ZhihuDailyItem;
import com.mx.presenter.IZhihuPresenter;
import com.mx.utils.CacheUtil;

import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by boobooL on 2016/5/11 0011
 * Created 邮箱 ：boobooMX@163.com
 */
public class ZhihuPresenterImpl extends BasePresenterImpl implements IZhihuPresenter {

    private IZhihuFragmentView mIZhihuFragmentView;
    private CacheUtil mCacheUtil;
    private Gson mGson=new Gson();

    public ZhihuPresenterImpl(IZhihuFragmentView IZhihuFragmentView, Context context) {
        if(IZhihuFragmentView==null){
            throw new IllegalArgumentException(" IZhihuFragmentView must not be null");
        }
        mIZhihuFragmentView = IZhihuFragmentView;
        mCacheUtil=CacheUtil.get(context);
    }


    @Override
    public void getLastZhihuNews() {
        mIZhihuFragmentView.showProgressDialog();
        Subscription subscription= ZhihuRequest.getZhihuApi().getLastDaily()
                .map(new Func1<ZhihuDaily, ZhihuDaily>() {
                    @Override
                    public ZhihuDaily call(ZhihuDaily zhihuDaily) {
                        String date = zhihuDaily.getDate();
                        for (ZhihuDailyItem zhihuDailyItem:zhihuDaily.getStories()){
                            zhihuDailyItem.setDate(date);
                        }
                        return zhihuDaily;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ZhihuDaily>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mIZhihuFragmentView.hideProgressDialog();
                        mIZhihuFragmentView.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(ZhihuDaily zhihuDaily) {
                        mIZhihuFragmentView.hideProgressDialog();
                        mCacheUtil.put(Config.ZHIHU,mGson.toJson(zhihuDaily));
                        mIZhihuFragmentView.updateList(zhihuDaily);

                    }
                });
        addSubSubscription(subscription);
    }

    @Override
    public void getTheDaily(String date) {
        Subscription subscription=ZhihuRequest.getZhihuApi().getTheDaily(date)
                .map(new Func1<ZhihuDaily, ZhihuDaily>() {
                    @Override
                    public ZhihuDaily call(ZhihuDaily zhihuDaily) {
                        String date=zhihuDaily.getDate();
                        for(ZhihuDailyItem zhihuDailyItem:zhihuDaily.getStories()){
                            zhihuDailyItem.setDate(date);
                        }
                        return zhihuDaily;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ZhihuDaily>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mIZhihuFragmentView.hideProgressDialog();
                        mIZhihuFragmentView.showError(e.getMessage());

                    }

                    @Override
                    public void onNext(ZhihuDaily zhihuDaily) {
                        mIZhihuFragmentView.hideProgressDialog();
                        mIZhihuFragmentView.updateList(zhihuDaily);


                    }
                });
        addSubSubscription(subscription);

    }

    @Override
    public void getLastFromCache() {
        if(mCacheUtil.getAsJSONObject(Config.ZHIHU)!=null){
            ZhihuDaily zhihuDaily = mGson.fromJson(mCacheUtil.getAsJSONObject(Config.ZHIHU).toString()
                    , ZhihuDaily.class);
            mIZhihuFragmentView.updateList(zhihuDaily);
        }

    }
}
