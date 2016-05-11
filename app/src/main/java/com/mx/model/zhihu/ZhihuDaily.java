package com.mx.model.zhihu;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by boobooL on 2016/5/11 0011
 * Created 邮箱 ：boobooMX@163.com
 */
public class ZhihuDaily {
    @SerializedName("date")
    private String date;
    @SerializedName("top_stories")
    private ArrayList<ZhihuDailyItem>mZhihuDailyItems;
    @SerializedName("stories")
    private ArrayList<ZhihuDailyItem>stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<ZhihuDailyItem> getZhihuDailyItems() {
        return mZhihuDailyItems;
    }

    public void setZhihuDailyItems(ArrayList<ZhihuDailyItem> zhihuDailyItems) {
        mZhihuDailyItems = zhihuDailyItems;
    }

    public ArrayList<ZhihuDailyItem> getStories() {
        return stories;
    }

    public void setStories(ArrayList<ZhihuDailyItem> stories) {
        this.stories = stories;
    }
}
