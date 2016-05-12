package com.mx.model.weiboVideo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by YiuChoi on 2016/4/12 0012.
 */
public class WeiboVideoMBlog implements Parcelable{
    @SerializedName("created_at")
    private String createTime;
    @SerializedName("text")
    private String text;
    @SerializedName("page_info")
    private WeiboVideoPageInfo mPageInfo;
    @SerializedName("retweeted_status")
    private WeiboVideoMBlog mBlog;

    protected WeiboVideoMBlog(Parcel in) {
        createTime = in.readString();
        text = in.readString();
        mPageInfo = in.readParcelable(WeiboVideoPageInfo.class.getClassLoader());
        mBlog = in.readParcelable(WeiboVideoMBlog.class.getClassLoader());
    }

    public static final Creator<WeiboVideoMBlog> CREATOR = new Creator<WeiboVideoMBlog>() {
        @Override
        public WeiboVideoMBlog createFromParcel(Parcel in) {
            return new WeiboVideoMBlog(in);
        }

        @Override
        public WeiboVideoMBlog[] newArray(int size) {
            return new WeiboVideoMBlog[size];
        }
    };

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public WeiboVideoPageInfo getPageInfo() {
        return mPageInfo;
    }

    public void setPageInfo(WeiboVideoPageInfo pageInfo) {
        mPageInfo = pageInfo;
    }

    public WeiboVideoMBlog getmBlog() {
        return mBlog;
    }

    public void setmBlog(WeiboVideoMBlog mBlog) {
        this.mBlog = mBlog;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(createTime);
        dest.writeString(text);
        dest.writeParcelable(mPageInfo,flags);
        dest.writeParcelable(mBlog, flags);
    }
}
