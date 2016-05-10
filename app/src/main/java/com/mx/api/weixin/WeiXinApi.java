package com.mx.api.weixin;

import com.mx.config.Config;
import com.mx.model.weixin.TxWeixinResponse;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by boobooL on 2016/5/10 0010
 * Created 邮箱 ：boobooMX@163.com
 */
public interface WeiXinApi {
    @GET("/wxnew/?key="+ Config.TX_APP_KEY+"&num=20")
    Observable<TxWeixinResponse>getWeixin(@Query("page")int page);
}
