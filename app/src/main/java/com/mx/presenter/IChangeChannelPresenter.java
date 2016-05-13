package com.mx.presenter;

import com.mx.config.Config;

import java.util.ArrayList;

/**
 * Created by boobooL on 2016/5/13 0013
 * Created 邮箱 ：boobooMX@163.com
 */
public interface IChangeChannelPresenter {
    void getChannel();
    void saveChannel(ArrayList<Config.Channel>channels);
}
