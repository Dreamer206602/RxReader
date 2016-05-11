package com.mx.model.ithome;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Path;
import org.simpleframework.xml.Root;

/**
 * Created by boobooL on 2016/5/11 0011
 * Created 邮箱 ：boobooMX@163.com
 */
@Root(name = "rss")
public class ItHomeArticle {
    @Path("channel/item")
    @Element(name = "newssource")
    private String newssource;
    @Path("channel/item")
    @Element(name = "newsauthor")
    private String newsauthor;
    @Path("channel/item")
    @Element(name = "detail")
    private String detail;
    @Path("channel/item")
    @Element(required = false,name = "z")
    private String z;
    @Path("channel/item")
    @Element(required = false,name = "tags")
    private String tags;

    public String getNewssource() {
        return newssource;
    }

    public void setNewssource(String newssource) {
        this.newssource = newssource;
    }

    public String getNewsauthor() {
        return newsauthor;
    }

    public void setNewsauthor(String newsauthor) {
        this.newsauthor = newsauthor;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getZ() {
        return z;
    }

    public void setZ(String z) {
        this.z = z;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }
}
