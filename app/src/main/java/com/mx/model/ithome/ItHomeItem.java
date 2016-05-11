package com.mx.model.ithome;

import android.os.Parcel;
import android.os.Parcelable;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

/**
 * Created by boobooL on 2016/5/11 0011
 * Created 邮箱 ：boobooMX@163.com
 */
@Root(name = "item")
public class ItHomeItem implements Parcelable {

    @Element(name = "newsid")
    private String newsid;
    @Element(name = "title")
    private String title;
    @Element(name = "c", required = false)
    private String c;
    @Element(required = false,name = "v")
    private String v;
    @Element(name = "url")
    private String url;
    @Element(name = "postdate")
    private String postdate;
    @Element(name = "image")
    private String image;
    @Element(required = false,name = "description")//处理可能为空的情况
    private String description;
    @Element(required = false,name = "hitcount")
    private int hitcount;
    @Element(required = false,name = "commentcount")
    private int commentcount;
    @Element(required = false,name = "forbidcomment")
    private String forbidcomment;
    @Element(required = false,name = "tags")
    private String tags;
    @Element(required = false,name = "z")
    private String z;

    public ItHomeItem() {
    }

    protected ItHomeItem(Parcel in) {
        newsid = in.readString();
        title = in.readString();
        c = in.readString();
        v = in.readString();
        url = in.readString();
        postdate = in.readString();
        image = in.readString();
        description = in.readString();
        hitcount = in.readInt();
        commentcount = in.readInt();
        forbidcomment = in.readString();
        tags = in.readString();
        z = in.readString();
    }

    public static final Creator<ItHomeItem> CREATOR = new Creator<ItHomeItem>() {
        @Override
        public ItHomeItem createFromParcel(Parcel in) {
            return new ItHomeItem(in);
        }

        @Override
        public ItHomeItem[] newArray(int size) {
            return new ItHomeItem[size];
        }
    };

    public String getNewsid() {
        return newsid;
    }

    public void setNewsid(String newsid) {
        this.newsid = newsid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPostdate() {
        return postdate;
    }

    public void setPostdate(String postdate) {
        this.postdate = postdate;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(newsid);
        dest.writeString(title);
        dest.writeString(c);
        dest.writeString(v);
        dest.writeString(url);
        dest.writeString(postdate);
        dest.writeString(image);
        dest.writeString(description);
        dest.writeInt(hitcount);
        dest.writeInt(commentcount);
        dest.writeString(forbidcomment);
        dest.writeString(tags);
        dest.writeString(z);
    }
}
