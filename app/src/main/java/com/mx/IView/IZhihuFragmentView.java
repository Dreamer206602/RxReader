package com.mx.iView;

import com.mx.model.zhihu.ZhihuDaily;

/**
 * Created by boobooL on 2016/5/11 0011
 * Created 邮箱 ：boobooMX@163.com
 */
public interface IZhihuFragmentView extends IBaseFragmentView{
    void updateList(ZhihuDaily zhihuDaily);
}
