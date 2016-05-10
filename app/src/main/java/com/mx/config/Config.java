package com.mx.config;

import com.mx.R;

/**
 * Created by boobooL on 2016/5/10 0010
 * Created 邮箱 ：boobooMX@163.com
 */
public class Config {
    public static final String DB__IS_READ_NAME = "IsRead";
    public static final String WEIXIN = "weixin";
    public static final String GUOKR = "guokr";
    public static final String ZHIHU = "zhihu";
    public static final String VIDEO = "video";
    public static final String IT = "it";

    public enum Channel {
        WEIXIN( R.string.fragment_wexin_title, R.drawable.icon_weixin),
        GUOKR(R.string.fragment_guokr_title, R.drawable.icon_guokr),
        ZHIHU(R.string.fragment_zhihu_title, R.drawable.icon_zhihu),
        VIDEO(R.string.fragment_video_title, R.drawable.icon_video),
        IT( R.string.fragment_it_title, R.drawable.icon_it);

        private int title;
        private int icon;

        Channel(int title, int icon) {
            this.title = title;
            this.icon = icon;
        }

        public int getTitle() {
            return title;
        }

        public void setTitle(int title) {
            this.title = title;
        }

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }
    }
}
