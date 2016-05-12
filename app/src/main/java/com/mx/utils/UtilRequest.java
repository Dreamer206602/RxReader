package com.mx.utils;

import com.mx.api.util.UtilApi;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;

/**
 * Created by boobooL on 2016/5/12 0012
 * Created 邮箱 ：boobooMX@163.com
 */
public class UtilRequest {
    private static UtilApi utilApi = null;
    protected static final Object monitor = new Object();

    public static UtilApi getUtilApi() {
        synchronized (monitor) {
            if (utilApi == null) {
                utilApi = new Retrofit.Builder()
                        .baseUrl("http://www.baidu.com")
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .build().create(UtilApi.class);
            }
            return utilApi;
        }
    }
}
