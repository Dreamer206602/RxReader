package com.mx.model.guokr;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by boobooL on 2016/5/11 0011
 * Created 邮箱 ：boobooMX@163.com
 */
public class GuokHtot {
    @SerializedName("result")
    private ArrayList<GuokrHotItem>result=new ArrayList<>();

    public ArrayList<GuokrHotItem> getResult() {
        return result;
    }

    public void setResult(ArrayList<GuokrHotItem> result) {
        this.result = result;
    }
}
