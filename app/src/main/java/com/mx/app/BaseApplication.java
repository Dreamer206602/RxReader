package com.mx.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by boobooL on 2016/5/9 0009
 * Created 邮箱 ：boobooMX@163.com
 */
public class BaseApplication extends Application {
    private static BaseApplication mBaseApplication;
    @Override
    public void onCreate() {
        super.onCreate();
        mBaseApplication=this;
    }
    public static Context getContext(){
        return mBaseApplication;
    }
}
