package com.mx.iView;

import android.support.v4.app.Fragment;

import com.mx.model.UpdateItem;

import java.util.ArrayList;

/**
 * Created by boobooL on 2016/5/10 0010
 * Created 邮箱 ：boobooMX@163.com
 */
public interface IMainView {
    void initMenu(ArrayList<Fragment>fragments,
                  ArrayList<Integer>titles);
    void showUpDate(UpdateItem updateItem);
}
