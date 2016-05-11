package com.mx.presenter.impl;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mx.api.itHome.ItHomeRequest;
import com.mx.config.Config;
import com.mx.iView.ItHomeFragmentView;
import com.mx.model.ithome.ItHomeItem;
import com.mx.model.ithome.ItHomeResponse;
import com.mx.presenter.IItHomePresenter;
import com.mx.utils.CacheUtil;
import com.mx.utils.ItHomeUtils;

import java.util.ArrayList;
import java.util.Iterator;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by boobooL on 2016/5/11 0011
 * Created 邮箱 ：boobooMX@163.com
 */
public class ItHomePresenterImpl extends BasePresenterImpl implements IItHomePresenter {
    private Gson mGson = new Gson();
    private ItHomeFragmentView mItHomeFragmentView;
    private CacheUtil mCacheUtil;

    public ItHomePresenterImpl(ItHomeFragmentView itHomeFragmentView, Context context) {
        if (itHomeFragmentView == null)
            throw new IllegalArgumentException("itHomeFragmentView must not be null");
        mItHomeFragmentView = itHomeFragmentView;
        mCacheUtil = CacheUtil.get(context);
    }

    @Override
    public void getNewItHomeNews() {
        mItHomeFragmentView.showProgressDialog();
        Subscription subscription = ItHomeRequest.getItHomeApi().getItHomeNews()
                .map(new Func1<ItHomeResponse, ArrayList<ItHomeItem>>() {
                    @Override
                    public ArrayList<ItHomeItem> call(ItHomeResponse itHomeResponse) {
                        //过滤广告新闻
                        ArrayList<ItHomeItem> itHomeItems = itHomeResponse.getChannel().getItems();
                        Iterator<ItHomeItem> iterator = itHomeItems.iterator();
                        while (iterator.hasNext()) {
                            ItHomeItem item = iterator.next();
                            if (item.getUrl().contains("digi")) {
                                iterator.remove();
                            }
                        }

                        return itHomeItems;
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<ItHomeItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mItHomeFragmentView.showError(e.getMessage());
                        mItHomeFragmentView.hideProgressDialog();

                    }

                    @Override
                    public void onNext(ArrayList<ItHomeItem> itHomeItems) {
                        mItHomeFragmentView.hideProgressDialog();
                        mItHomeFragmentView.updateList(itHomeItems);
                        mCacheUtil.put(Config.IT, mGson.toJson(itHomeItems));

                    }
                });
        addSubSubscription(subscription);

    }

    @Override
    public void getMoreItHomeNews(String lastNewsId) {
        Subscription subscription = ItHomeRequest.getItHomeApi().getMoreItHomeNews(ItHomeUtils.getMinNewsId(lastNewsId))
                .map(new Func1<ItHomeResponse, ArrayList<ItHomeItem>>() {
                    @Override
                    public ArrayList<ItHomeItem> call(ItHomeResponse itHomeResponse) {
                        //过滤广告新闻
                        ArrayList<ItHomeItem> itHomeItems = itHomeResponse.getChannel().getItems();
                        Iterator<ItHomeItem> iterator = itHomeItems.iterator();
                        while (iterator.hasNext()) {
                            ItHomeItem itHomeItem = iterator.next();
                            if (itHomeItem.getUrl().contains("digi")) {
                                iterator.remove();
                            }
                        }

                        return itHomeItems;
                    }
                })
                .subscribeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ArrayList<ItHomeItem>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mItHomeFragmentView.hideProgressDialog();
                        mItHomeFragmentView.showError(e.getMessage());

                    }

                    @Override
                    public void onNext(ArrayList<ItHomeItem> itHomeItems) {
                        mItHomeFragmentView.hideProgressDialog();
                        mItHomeFragmentView.updateList(itHomeItems);

                    }
                });
        addSubSubscription(subscription);


    }

    @Override
    public void getNewsFromCache() {
        if (mCacheUtil.getAsJSONObject(Config.IT) != null &&
                mCacheUtil.getAsJSONObject(Config.IT).length() != 0) {
            ArrayList<ItHomeItem> it = mGson.fromJson(mCacheUtil.getAsJSONObject(Config.IT).toString(),
                    new TypeToken<ArrayList<ItHomeItem>>() {
                    }.getType());
            mItHomeFragmentView.updateList(it);
        }

    }
}
