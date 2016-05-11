package com.mx.iView;

import com.mx.model.ithome.ItHomeItem;

import java.util.ArrayList;

/**
 * Created by boobooL on 2016/5/11 0011
 * Created 邮箱 ：boobooMX@163.com
 */
public interface ItHomeFragmentView extends IBaseFragmentView{
   void updateList(ArrayList<ItHomeItem>itHomeItems);
}
