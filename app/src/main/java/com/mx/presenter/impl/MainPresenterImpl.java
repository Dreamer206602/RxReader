package com.mx.presenter.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;

import com.mx.R;
import com.mx.config.Config;
import com.mx.presenter.IMainPresenter;
import com.mx.iView.IMainView;
import com.mx.ui.fragment.GuokrFragment;
import com.mx.ui.fragment.ItHomeFragment;
import com.mx.ui.fragment.VideoFragment;
import com.mx.ui.fragment.WeixinFragment;
import com.mx.ui.fragment.ZhihuFragment;
import com.mx.utils.SharePreferenceUtil;

import java.util.ArrayList;
import java.util.Collections;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by boobooL on 2016/5/10 0010
 * Created 邮箱 ：boobooMX@163.com
 */
public class MainPresenterImpl extends BasePresenterImpl implements IMainPresenter {
    private ArrayList<Fragment> mFragments;
    private ArrayList<Integer> titles;
    private IMainView mIMainView;
    private ArrayList<Config.Channel> savedChannelList;
    private SharedPreferences mSharedPreferences;
    private CompositeSubscription mCompositeSubscription;

    public MainPresenterImpl(IMainView main, Context context) {
        if (main == null)
            throw new IllegalArgumentException("main must not be null");
        mSharedPreferences = context.getSharedPreferences(
                SharePreferenceUtil.SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        mIMainView = main;
        mFragments = new ArrayList<>();
        titles = new ArrayList<>();
        savedChannelList = new ArrayList<>();
    }


    @Override
    public void initMenu(final NavigationView navigationView) {

        Subscription subscription = Observable.create
                (new Observable.OnSubscribe<Boolean>() {
                    @Override
                    public void call(Subscriber<? super Boolean> subscriber) {
                        savedChannelList.clear();
                        titles.clear();
                        Menu menu = navigationView.getMenu();
                        menu.clear();
                        mFragments.clear();
                        String savedChannel = mSharedPreferences.
                                getString(SharePreferenceUtil.SAVED_CHANNEL, null);
                        if (TextUtils.isEmpty(savedChannel)) {
                            Collections.addAll(savedChannelList,
                                    Config.Channel.values());
                        } else {
                            for (String s : savedChannel.split(",")) {
                                savedChannelList.add(Config.Channel.valueOf(s));
                            }
                        }

                        for (int i = 0; i < savedChannelList.size(); i++) {
                            MenuItem menuItem = menu.
                                    add(0, i, 0, savedChannelList.get(i).getTitle());
                            titles.add(savedChannelList.get(i).getTitle());
                            menuItem.setIcon(savedChannelList.get(i).getIcon());
                            menuItem.setCheckable(true);
                            addFragment(savedChannelList.get(i).name());
                            if (i == 0) {
                                menuItem.setChecked(true);
                            }
                        }
                        subscriber.onNext(true);


                    }
                })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        navigationView.inflateMenu(R.menu.activity_main_drawer);
                        mIMainView.initMenu(mFragments, titles);
                    }
                });

        addSubSubscription(subscription);

    }


    @Override
    public void checkUpdate() {

    }

    private void addFragment(String name) {
        switch (name) {
            case "GUOKR":
                mFragments.add(new GuokrFragment());
                break;
            case "WEIXIN":
                mFragments.add(new WeixinFragment());
                break;
            case "ZHIHU":
                mFragments.add(new ZhihuFragment());
                break;
            case "VIDEO":
                mFragments.add(new VideoFragment());
                break;
            case "IT":
                mFragments.add(new ItHomeFragment());
                break;
        }

    }
}
