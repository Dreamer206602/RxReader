package com.mx.presenter.impl;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.mx.config.Config;
import com.mx.iView.IChangeChannelView;
import com.mx.presenter.IChangeChannelPresenter;
import com.mx.utils.SharePreferenceUtil;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by boobooL on 2016/5/13 0013
 * Created 邮箱 ：boobooMX@163.com
 */
public class ChangeChannelPresenterImpl implements IChangeChannelPresenter {

    private IChangeChannelView mIChangeChannelView;
    private SharedPreferences mSharedPreferences;
    private ArrayList<Config.Channel> savedChannelList;
    private ArrayList<Config.Channel> dismissChannelList;

    public ChangeChannelPresenterImpl(IChangeChannelView IChangeChannelView, Context context) {
        mIChangeChannelView = IChangeChannelView;

        mSharedPreferences = context.getSharedPreferences(SharePreferenceUtil.SHARED_PREFERENCE_NAME,
                Context.MODE_PRIVATE);
        savedChannelList = new ArrayList<>();
        dismissChannelList = new ArrayList<>();
    }

    @Override
    public void getChannel() {
        String savedChannel = mSharedPreferences.getString(SharePreferenceUtil.
                SAVED_CHANNEL, null);
        if (TextUtils.isEmpty(savedChannel)) {
            Collections.addAll(savedChannelList, Config.Channel.values());
        } else {
            for (String s : savedChannel.split(",")) {
                savedChannelList.add(Config.Channel.valueOf(s));
            }

        }

        for (Config.Channel channel : Config.Channel.values()) {
            if (!savedChannelList.contains(channel)) {
                dismissChannelList.add(channel);
            }
        }

        mIChangeChannelView.showChannel(savedChannelList, dismissChannelList);

    }

    @Override
    public void saveChannel(ArrayList<Config.Channel> channels) {
        StringBuilder stringBuilder = new StringBuilder();
        for (Config.Channel channel : channels) {
            stringBuilder.append(channel.name()).append(",");
        }
        mSharedPreferences.edit().putString(SharePreferenceUtil.SAVED_CHANNEL,
                stringBuilder.toString()).apply();

    }
}
