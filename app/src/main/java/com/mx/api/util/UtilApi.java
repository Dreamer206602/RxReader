package com.mx.api.util;

import okhttp3.ResponseBody;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Created by boobooL on 2016/5/12 0012
 * Created 邮箱 ：boobooMX@163.com
 */
public interface UtilApi {
    @FormUrlEncoded
    @POST("http://www.weibovideo.com")
    Observable<ResponseBody> getVideoUrl(@Field("weibourl") String weibourl);
}
