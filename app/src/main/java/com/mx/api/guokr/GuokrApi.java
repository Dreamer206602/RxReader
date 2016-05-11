package com.mx.api.guokr;

import com.mx.model.guokr.GuokHtot;
import com.mx.model.guokr.GuokrArticle;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by boobooL on 2016/5/10 0010
 * Created 邮箱 ：boobooMX@163.com
 */
public interface GuokrApi {
    @GET("http://apis.guokr.com/minisite/article.json?retrieve_type=by_minisite")
    Observable<GuokHtot> getGuokrHot(@Query("offset") int offset);

    @GET("http://apis.guokr.com/minisite/article/{id}.json")
    Observable<GuokrArticle> getGuokrArticle(@Path("id") String id);
}
