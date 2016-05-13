package com.mx.iView;

import com.mx.config.Config;

import java.util.ArrayList;

/**
 * Created by boobooL on 2016/5/13 0013
 * Created 邮箱 ：boobooMX@163.com
 */
public interface IChangeChannelView {
    void showChannel(ArrayList<Config.Channel>savedChannel, ArrayList<Config.Channel>otherChannel);
}
