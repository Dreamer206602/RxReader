package com.mx.iView;

import com.mx.model.UpdateItem;

/**
 * Created by boobooL on 2016/5/12 0012
 * Created 邮箱 ：boobooMX@163.com
 */
public interface ISettingFragmentView {
    void showError(String error);
    void showUpdateDialog(UpdateItem updateItem);
    void showNoUpdate();
}
