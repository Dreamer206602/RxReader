package com.mx.iView;

import com.mx.model.weixin.WeixinNews;

import java.util.ArrayList;

/**
 * Created by boobooL on 2016/5/10 0010
 * Created 邮箱 ：boobooMX@163.com
 */
public interface IWeixinFragmentView extends IBaseFragmentView {
    void updateList(ArrayList<WeixinNews>weixinNewses);
}
