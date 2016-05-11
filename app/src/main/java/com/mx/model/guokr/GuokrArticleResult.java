package com.mx.model.guokr;

import com.google.gson.annotations.SerializedName;

/**
 * Created by boobooL on 2016/5/11 0011
 * Created 邮箱 ：boobooMX@163.com
 */
public class GuokrArticleResult {

    @SerializedName("small_image")
    private String mSmallImage;
    @SerializedName("title")
    private String title;
    @SerializedName("url")
    private String url;
    @SerializedName("content")
    private String content;

    public String getSmallImage() {
        return mSmallImage;
    }

    public void setSmallImage(String smallImage) {
        mSmallImage = smallImage;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
