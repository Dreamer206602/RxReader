package com.mx.model.ithome;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.ArrayList;

/**
 * Created by boobooL on 2016/5/11 0011
 * Created 邮箱 ：boobooMX@163.com
 */
@Root(name = "channel")
public class ItHomeChannel {
    @ElementList(inline = true,name = "items")
    ArrayList<ItHomeItem>mItems;

    public ArrayList<ItHomeItem> getItems() {
        return mItems;
    }

    public void setItems(ArrayList<ItHomeItem> items) {
        mItems = items;
    }
}
