package com.mx.presenter.impl;

import android.content.Context;

import com.google.gson.Gson;
import com.mx.api.guokr.GuokrRequest;
import com.mx.config.Config;
import com.mx.iView.IGuokrFragmentView;
import com.mx.model.guokr.GuokHtot;
import com.mx.presenter.IGuokrPresenter;
import com.mx.utils.CacheUtil;

import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by boobooL on 2016/5/11 0011
 * Created 邮箱 ：boobooMX@163.com
 */
public class GuokrPresentImpl extends BasePresenterImpl implements IGuokrPresenter{

    private IGuokrFragmentView mGuokrFragmentView;
    private CacheUtil mCacheUtil;

    public GuokrPresentImpl(IGuokrFragmentView guokrFragmentView, Context context) {
        if(guokrFragmentView==null)
            throw  new
                    IllegalArgumentException("guokrFragment must not null");
        mGuokrFragmentView = guokrFragmentView;
        mCacheUtil=CacheUtil.get(context);
    }

    @Override
    public void getGuokrhot(final int offset) {
        mGuokrFragmentView.showProgressDialog();
        Subscription subscription= GuokrRequest.getGuokrApi().getGuokrHot(offset)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<GuokHtot>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mGuokrFragmentView.hideProgressDialog();
                        mGuokrFragmentView.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(GuokHtot guokHtot) {
                            mGuokrFragmentView.hideProgressDialog();
                        mGuokrFragmentView.update(guokHtot.getResult());
                        mCacheUtil.put(Config.GUOKR+offset,new Gson().toJson(guokHtot));
                    }
                });
        addSubSubscription(subscription);

    }

    @Override
    public void getGuokrHotFromCache(int offset) {
        if(mCacheUtil.getAsJSONObject(Config.GUOKR+offset)!=null){
            GuokHtot guokHtot=new Gson().fromJson(mCacheUtil
                    .getAsJSONObject(Config.GUOKR+offset).toString(),GuokHtot.class);
            mGuokrFragmentView.update(guokHtot.getResult());
        }

    }
}
