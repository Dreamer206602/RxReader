package com.mx.iView;

import com.mx.model.guokr.GuokrArticle;
import com.mx.model.zhihu.ZhihuStory;

/**
 * Created by boobooL on 2016/5/11 0011
 * Created 邮箱 ：boobooMX@163.com
 */
public interface IZhihuStoryView {
    void showError(String error);
    void showZhihuStory(ZhihuStory zhihuStory);
    void showGuokrArticle(GuokrArticle guokrArticle);
}
