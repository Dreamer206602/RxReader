package com.mx.presenter;

/**
 * Created by boobooL on 2016/5/12 0012
 * Created 邮箱 ：boobooMX@163.com
 */
public interface IVideoPresenter extends BasePresenter{
    void getVideo(int page);
    void getVideoFromCache(int page);

}
