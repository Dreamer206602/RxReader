package com.mx.model.weiboVideo;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by 蔡小木 on 2016/4/12 0012.
 */
public class WeiboVideoCardsItem {
    //=mod/empty表示没有数据
    @SerializedName("mod_type")
    private String modType;
    @SerializedName("card_group")
    private ArrayList<WeiboVideoBlog> mBlogs;

    public String getModType() {
        return modType;
    }

    public void setModType(String modType) {
        this.modType = modType;
    }


    public ArrayList<WeiboVideoBlog> getBlogs() {
        return mBlogs;
    }

    public void setBlogs(ArrayList<WeiboVideoBlog> blogs) {
        mBlogs = blogs;
    }
}
