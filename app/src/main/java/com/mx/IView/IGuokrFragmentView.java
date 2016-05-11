package com.mx.iView;

import com.mx.model.guokr.GuokrHotItem;

import java.util.ArrayList;

/**
 * Created by boobooL on 2016/5/11 0011
 * Created 邮箱 ：boobooMX@163.com
 */
public interface IGuokrFragmentView extends IBaseFragmentView {
    void update(ArrayList<GuokrHotItem>guokrHotItems);
}
