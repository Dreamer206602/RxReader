package com.mx.presenter;

/**
 * Created by boobooL on 2016/5/10 0010
 * Created 邮箱 ：boobooMX@163.com
 */
public interface IWeixinPresenter extends BasePresenter{
    void getWeiXinNews(int page);
    void getWeixinNewsfromCache(int page);
}
