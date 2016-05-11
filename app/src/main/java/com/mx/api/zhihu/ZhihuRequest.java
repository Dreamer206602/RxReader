package com.mx.api.zhihu;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by boobooL on 2016/5/11 0011
 * Created 邮箱 ：boobooMX@163.com
 */
public class ZhihuRequest {

    private static ZhihuApi sZhihuApi=null;
    protected static  final Object monitor=new Object();
    public static  ZhihuApi getZhihuApi(){
        synchronized (monitor){
            if(sZhihuApi==null){
                sZhihuApi=new Retrofit.Builder()
                        .baseUrl("http://news-at.zhihu.com")
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(ZhihuApi.class);
            }
            return sZhihuApi;
        }
    }
}
