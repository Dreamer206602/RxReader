package com.mx.iView;

import com.mx.model.ithome.ItHomeArticle;

/**
 * Created by boobooL on 2016/5/12 0012
 * Created 邮箱 ：boobooMX@163.com
 */
public interface IItHomeArticleView {
    void showError(String error);
    void showItHomeArticle(ItHomeArticle itHomeArticle);

}
