package com.mx.model.ithome;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by boobooL on 2016/5/11 0011
 * Created 邮箱 ：boobooMX@163.com
 */
@Root(name = "rss")
public class ItHomeResponse {
    @Element(name = "channel")
    ItHomeChannel channel;

    @Attribute(name = "version")
    private String version;

    public ItHomeChannel getChannel() {
        return channel;
    }

    public void setChannel(ItHomeChannel channel) {
        this.channel = channel;
    }
}
