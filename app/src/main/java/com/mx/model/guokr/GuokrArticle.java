package com.mx.model.guokr;

import com.google.gson.annotations.SerializedName;

/**
 * Created by boobooL on 2016/5/11 0011
 * Created 邮箱 ：boobooMX@163.com
 */
public class GuokrArticle {

    @SerializedName("result")
    private GuokrArticleResult result;

    public GuokrArticleResult getResult() {
        return result;
    }

    public void setResult(GuokrArticleResult result) {
        this.result = result;
    }
}
