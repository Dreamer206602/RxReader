package com.mx.presenter.impl;

import com.mx.api.itHome.ItHomeRequest;
import com.mx.iView.IItHomeArticleView;
import com.mx.model.ithome.ItHomeArticle;
import com.mx.presenter.ItHomeArticlePresenter;
import com.mx.utils.ItHomeUtils;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by boobooL on 2016/5/12 0012
 * Created 邮箱 ：boobooMX@163.com
 */
public class ItHomeArticlePresenterImpl extends BasePresenterImpl implements ItHomeArticlePresenter {

    private IItHomeArticleView mIItHomeArticleView;

    public ItHomeArticlePresenterImpl(IItHomeArticleView IItHomeArticleView) {
        if (IItHomeArticleView == null) {
            throw new IllegalArgumentException("iTtHomeArticle must not be null");
        }
        mIItHomeArticleView = IItHomeArticleView;
    }

    @Override
    public void getItHomeArticle(String id) {
        Subscription subscription = ItHomeRequest.getItHomeApi()
                .getItHomeArticle(ItHomeUtils.getSplitNewsId(id))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ItHomeArticle>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mIItHomeArticleView.showError(e.getMessage());

                    }

                    @Override
                    public void onNext(ItHomeArticle itHomeArticle) {
                        mIItHomeArticleView.showItHomeArticle(itHomeArticle);

                    }
                });
        addSubSubscription(subscription);
    }
}
