package com.mx.api.zhihu;

import com.mx.model.UpdateItem;
import com.mx.model.zhihu.ZhihuDaily;
import com.mx.model.zhihu.ZhihuStory;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by boobooL on 2016/5/11 0011
 * Created 邮箱 ：boobooMX@163.com
 */
public interface ZhihuApi {
    @GET("/api/4/news/latest")
    Observable<ZhihuDaily> getLastDaily();

    @GET("/api/4/news/before/{date}")
    Observable<ZhihuDaily> getTheDaily(@Path("date") String date);

    @GET("/api/4/news/{id}")
    Observable<ZhihuStory> getZhihuStory(@Path("id") String id);

    //图片的处理

    @GET("http://caiyao.name/releases/MrUpdate.json")
    Observable<UpdateItem>getUpDateInfo();




}
