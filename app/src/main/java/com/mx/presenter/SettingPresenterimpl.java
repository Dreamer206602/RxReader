package com.mx.presenter;

import com.mx.BuildConfig;
import com.mx.api.zhihu.ZhihuRequest;
import com.mx.iView.ISettingFragmentView;
import com.mx.model.UpdateItem;
import com.mx.presenter.impl.BasePresenterImpl;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by boobooL on 2016/5/12 0012
 * Created 邮箱 ：boobooMX@163.com
 */
public class SettingPresenterImpl extends BasePresenterImpl implements ISettingPresenter{

    private ISettingFragmentView mISettingFragmentView;

    public SettingPresenterImpl(ISettingFragmentView ISettingFragmentView) {
        if (ISettingFragmentView == null) {
            throw new IllegalArgumentException("iSettingFragmentView must not be null");
        }
        mISettingFragmentView = ISettingFragmentView;
    }

    @Override
    public void checkUpdate() {

        Subscription subscription= ZhihuRequest.getZhihuApi().getUpDateInfo()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<UpdateItem>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                            mISettingFragmentView.showError(e.getMessage());
                    }

                    @Override
                    public void onNext(UpdateItem updateItem) {
                        if(updateItem.getVersionCode()> BuildConfig.VERSION_CODE){
                            mISettingFragmentView.showUpdateDialog(updateItem);
                        }else{
                            mISettingFragmentView.showNoUpdate();
                        }
                    }
                });
        addSubSubscription(subscription);

    }
}
