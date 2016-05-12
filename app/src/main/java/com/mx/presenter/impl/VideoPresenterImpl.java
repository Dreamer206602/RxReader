package com.mx.presenter.impl;

import android.content.Context;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mx.api.weiboVideo.VideoRequest;
import com.mx.config.Config;
import com.mx.iView.IVideoFragmentView;
import com.mx.model.weiboVideo.WeiboVideoBlog;
import com.mx.model.weiboVideo.WeiboVideoResponse;
import com.mx.presenter.IVideoPresenter;
import com.mx.utils.CacheUtil;

import java.util.ArrayList;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by boobooL on 2016/5/12 0012
 * Created 邮箱 ：boobooMX@163.com
 */
public class VideoPresenterImpl extends BasePresenterImpl implements IVideoPresenter {
    private IVideoFragmentView mIVideoFragmentView;
    private CacheUtil mCacheUtil;
    private Gson mGson = new Gson();

    public VideoPresenterImpl(IVideoFragmentView IVideoFragmentView, Context context) {
        if (IVideoFragmentView != null) {
            throw new IllegalArgumentException("iVideoFragment must not bull");
        }
        mIVideoFragmentView = IVideoFragmentView;
        mCacheUtil = CacheUtil.get(context);

    }

    @Override
    public void getVideo(final int page) {
        Subscription subscription = VideoRequest.getVideoRequestApi().getWeiboVideo(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<WeiboVideoResponse, ArrayList<WeiboVideoBlog>>() {
                    @Override
                    public ArrayList<WeiboVideoBlog> call(WeiboVideoResponse weiboVideoResponse) {
                        ArrayList<WeiboVideoBlog> arrayList =
                                new ArrayList<WeiboVideoBlog>();
                        if (!weiboVideoResponse.getCardsItems()[0]
                                .getModType().equals("mod/empty")) {
                            ArrayList<WeiboVideoBlog>
                                    a = weiboVideoResponse.getCardsItems()[0]
                                    .getBlogs();
                            for (WeiboVideoBlog w : a) {
                                if (w.getBlog().getmBlog() != null) {
                                    //处理转发的微博
                                    w.setBlog(w.getBlog().getmBlog());
                                }

                                if (w.getBlog().getPageInfo() != null
                                        && !TextUtils.isEmpty(w.getBlog()
                                        .getPageInfo().getVideoPic())) {
                                    //处理无视频的微博
                                    arrayList.add(w);

                                }

                            }

                        }
                        return arrayList;
                    }
                })
                .subscribe(new Subscriber<ArrayList<WeiboVideoBlog>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        mIVideoFragmentView.hideProgressDialog();
                        mIVideoFragmentView.showError(e.getMessage());

                    }

                    @Override
                    public void onNext(ArrayList<WeiboVideoBlog> weiboVideoBlogs) {
                        mIVideoFragmentView.hideProgressDialog();
                        mIVideoFragmentView.updateList(weiboVideoBlogs);
                        mCacheUtil.put(Config.VIDEO + page,
                                mGson.toJson(weiboVideoBlogs));

                    }
                });
        addSubSubscription(subscription);


    }

    @Override
    public void getVideoFromCache(int page) {

        if (mCacheUtil.getAsJSONObject(Config.VIDEO + page) != null
                &&
                mCacheUtil.getAsJSONObject(Config.VIDEO + page).length() > 0) {

            ArrayList<WeiboVideoBlog> weboVideoBlogs =
                    mGson.fromJson(mCacheUtil.getAsJSONObject(Config.VIDEO + page).toString(),
                    new TypeToken<ArrayList<WeiboVideoBlog>>() {
                    }.getType());
            mIVideoFragmentView.updateList(weboVideoBlogs);
        }


    }
}
