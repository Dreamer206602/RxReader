package com.mx.api.weixin;

import com.mx.app.BaseApplication;
import com.mx.utils.NetWorkUtil;

import java.io.File;
import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by boobooL on 2016/5/10 0010
 * Created 邮箱 ：boobooMX@163.com
 */
public class WeiXinRequest {
    private static final Interceptor REWRITE_CACHHE_CONTROL_INTERCEPTOR =
            new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Response originalResponse = chain.proceed(chain.request());
                    if (NetWorkUtil.isNetWorkAvailable(BaseApplication.getContext())) {
                        int maxAge = 60;//在线缓存1分钟内可读取
                        return originalResponse.newBuilder()
                                .removeHeader("Pragma")
                                .removeHeader("Cache-Control")
                                .header("Cache-Control", "public, max-age=" + maxAge)
                                .build();

                    } else {
                        int maxScale = 60 * 60 * 24 * 28;//离线时缓存保存4周
                        return originalResponse.newBuilder()
                                .removeHeader("Pragma")
                                .removeHeader("Cache-Control")
                                .header("Cache-Control", "public," +
                                        "only-if-cached,max-stale=" + maxScale)
                                .build();
                    }

                }
            };

    static File httpCacheDirectory = new File(
            BaseApplication.getContext().getCacheDir(), "txCache");
    static int cacheSize = 10 * 1024 * 1024;
    static Cache sCache = new Cache(httpCacheDirectory, cacheSize);
    static OkHttpClient sClient = new OkHttpClient.Builder()
            .addNetworkInterceptor(REWRITE_CACHHE_CONTROL_INTERCEPTOR)
            .cache(sCache)
            .build();
    private static WeiXinApi sWeiXinApi = null;
    protected static final Object monitor = new Object();

    public static WeiXinApi getWeiXinApi() {
        synchronized (monitor) {
            if (sWeiXinApi == null) {
                sWeiXinApi = new Retrofit.Builder()
                        .baseUrl("http://api.huceo.com")
                        .client(sClient)
                        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                        .create(WeiXinApi.class);

            }
            return sWeiXinApi;
        }
    }


}

