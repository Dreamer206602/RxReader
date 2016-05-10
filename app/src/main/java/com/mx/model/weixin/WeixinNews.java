package com.mx.model.weixin;

import com.google.gson.annotations.SerializedName;

/**
 * Created by boobooL on 2016/5/10 0010
 * Created 邮箱 ：boobooMX@163.com
 */
public class WeixinNews {
    @SerializedName("ctime")
    private String ctime;
    @SerializedName("title")
    private String title;
    @SerializedName("description")
    private String description;
    @SerializedName("picUrl")
    private String picUrl;
    @SerializedName("url")
    private String url;

    public String getCtime() {
        return ctime;
    }

    public void setCtime(String ctime) {
        this.ctime = ctime;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
