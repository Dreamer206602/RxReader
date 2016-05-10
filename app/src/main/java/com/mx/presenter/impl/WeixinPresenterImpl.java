package com.mx.presenter.impl;

import android.content.Context;

import com.google.gson.Gson;
import com.mx.api.weixin.WeiXinRequest;
import com.mx.config.Config;
import com.mx.iView.IWeixinFragmentView;
import com.mx.model.weixin.TxWeixinResponse;
import com.mx.presenter.IWeixinPresenter;
import com.mx.utils.CacheUtil;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by boobooL on 2016/5/10 0010
 * Created 邮箱 ：boobooMX@163.com
 */
public class WeixinPresenterImpl extends BasePresenterImpl
        implements IWeixinPresenter {
    private CacheUtil mCacheUtil;
    private IWeixinFragmentView mIWeixinFragmentView;
    private Gson mGson = new Gson();

    public WeixinPresenterImpl(IWeixinFragmentView weixinFragmentView, Context context) {
        if (weixinFragmentView == null) {
            throw new IllegalArgumentException("weixinFragmentView must not be null");
        }
        this.mIWeixinFragmentView = weixinFragmentView;
        mCacheUtil = CacheUtil.get(context);
    }


    @Override
    public void getWeiXinNews(final int page) {
        mIWeixinFragmentView.showProgressDialog();
        Subscription subscription = WeiXinRequest.getWeiXinApi().getWeixin(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<TxWeixinResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mIWeixinFragmentView.hideProgressDialog();
                        mIWeixinFragmentView.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(TxWeixinResponse txWeixinResponse) {
                        mIWeixinFragmentView.showProgressDialog();
                        if (txWeixinResponse.getCode() == 200) {
                            mIWeixinFragmentView
                                    .updateList(
                                            txWeixinResponse.getNewslist());
                            mCacheUtil.put
                                    (Config.WEIXIN+page,
                                            mGson.toJson(txWeixinResponse));
                        }else{
                            mIWeixinFragmentView.showError("服务器内部错误");
                        }
                    }
                });
        addSubSubscription(subscription);

    }

    @Override
    public void getWeixinNewsfromCache(int page) {
        if(mCacheUtil.getAsJSONObject(
                Config.WEIXIN+page)!=null){
            TxWeixinResponse txWeixinResponse=
                    mGson.fromJson(mCacheUtil.
                                    getAsJSONObject(Config.WEIXIN+page).toString(),
                            TxWeixinResponse.class);
            mIWeixinFragmentView.updateList(txWeixinResponse.getNewslist());
        }

    }
}
