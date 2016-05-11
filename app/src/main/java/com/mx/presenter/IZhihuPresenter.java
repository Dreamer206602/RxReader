package com.mx.presenter;

/**
 * Created by boobooL on 2016/5/11 0011
 * Created 邮箱 ：boobooMX@163.com
 */
public interface IZhihuPresenter extends BasePresenter {
    void getLastZhihuNews();
    void getTheDaily(String date);
    void getLastFromCache();
}
