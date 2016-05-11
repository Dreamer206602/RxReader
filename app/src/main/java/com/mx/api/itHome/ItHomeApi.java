package com.mx.api.itHome;

import com.mx.model.ithome.ItHomeArticle;
import com.mx.model.ithome.ItHomeResponse;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by boobooL on 2016/5/11 0011
 * Created 邮箱 ：boobooMX@163.com
 */
public interface ItHomeApi {

    @GET("/xml/newslist/news.xml")
    Observable<ItHomeResponse> getItHomeNews();

    @GET("/xml/newslist/news_{minNewsId}.xml")
    Observable<ItHomeResponse> getMoreItHomeNews(@Path("minNewsId") String minNewsId);

    @GET("/xml/newscontent/{id}.xml")
    Observable<ItHomeArticle> getItHomeArticle(@Path("id") String id);

}
