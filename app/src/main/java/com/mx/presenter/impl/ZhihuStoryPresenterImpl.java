package com.mx.presenter.impl;

import com.mx.api.guokr.GuokrRequest;
import com.mx.api.zhihu.ZhihuRequest;
import com.mx.iView.IZhihuStoryView;
import com.mx.model.guokr.GuokrArticle;
import com.mx.model.zhihu.ZhihuStory;
import com.mx.presenter.IZhihuStoryPresenter;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by boobooL on 2016/5/11 0011
 * Created 邮箱 ：boobooMX@163.com
 */
public class ZhihuStoryPresenterImpl extends BasePresenterImpl implements IZhihuStoryPresenter {

    private IZhihuStoryView mIZhihuStoryView;

    public ZhihuStoryPresenterImpl(IZhihuStoryView IZhihuStoryView) {
        if(IZhihuStoryView==null){
            throw new IllegalArgumentException("zhiStoryView must be not null");
        }
        mIZhihuStoryView = IZhihuStoryView;
    }

    @Override
    public void getZhihuStory(String id) {
        Subscription subscription= ZhihuRequest.getZhihuApi().getZhihuStory(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ZhihuStory>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                            mIZhihuStoryView.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(ZhihuStory zhihuStory) {
                        mIZhihuStoryView.showZhihuStory(zhihuStory);

                    }
                });
        addSubSubscription(subscription);

    }

    @Override
    public void getGuokrArticle(String id) {
        Subscription subscription= GuokrRequest.getGuokrApi().getGuokrArticle(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GuokrArticle>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mIZhihuStoryView.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(GuokrArticle guokrArticle) {
                        mIZhihuStoryView.showGuokrArticle(guokrArticle);

                    }
                });
        addSubSubscription(subscription);

    }
}
