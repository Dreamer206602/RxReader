package com.mx.api.weiboVideo;

import com.mx.model.weiboVideo.WeiboVideoResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by boobooL on 2016/5/12 0012
 * Created 邮箱 ：boobooMX@163.com
 */
public interface VideoRequestApi {
    @GET("http://m.weibo.cn/page/json?containerid=1005051914635823_-_WEIBO_SECOND_PROFILE_WEIBO&")
    Observable<WeiboVideoResponse> getWeiboVideo(@Query("page") int page);
}
