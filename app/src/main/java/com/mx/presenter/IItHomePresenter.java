package com.mx.presenter;

/**
 * Created by boobooL on 2016/5/11 0011
 * Created 邮箱 ：boobooMX@163.com
 */
public interface IItHomePresenter extends BasePresenter {
    void  getNewItHomeNews();
    void getMoreItHomeNews(String lastNewsId);
    void getNewsFromCache();
}
