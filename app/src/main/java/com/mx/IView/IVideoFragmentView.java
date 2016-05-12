package com.mx.iView;

import com.mx.model.weiboVideo.WeiboVideoBlog;

import java.util.ArrayList;

/**
 * Created by boobooL on 2016/5/12 0012
 * Created 邮箱 ：boobooMX@163.com
 */
public interface IVideoFragmentView extends IBaseFragmentView {
    void updateList(ArrayList<WeiboVideoBlog>weiboVideoBlogs);
}
