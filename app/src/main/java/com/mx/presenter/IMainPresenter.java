package com.mx.presenter;

import android.support.design.widget.NavigationView;

/**
 * Created by boobooL on 2016/5/10 0010
 * Created 邮箱 ：boobooMX@163.com
 */
public interface IMainPresenter extends BasePresenter{
    void initMenu(NavigationView navigationView);
    void checkUpdate();
}
