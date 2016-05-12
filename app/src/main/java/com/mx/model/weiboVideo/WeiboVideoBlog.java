package com.mx.model.weiboVideo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by YiuChoi on 2016/4/12 0012.
 */
public class WeiboVideoBlog implements Parcelable {
    @SerializedName("mblog")
    private WeiboVideoMBlog mBlog;

    protected WeiboVideoBlog(Parcel in) {
        in.readParcelable(WeiboVideoMBlog.class.getClassLoader());
    }

    public WeiboVideoMBlog getBlog() {
        return mBlog;
    }

    public void setBlog(WeiboVideoMBlog blog) {
        mBlog = blog;
    }

    public static final Creator<WeiboVideoBlog> CREATOR = new Creator<WeiboVideoBlog>() {
        @Override
        public WeiboVideoBlog createFromParcel(Parcel in) {
            return new WeiboVideoBlog(in);
        }

        @Override
        public WeiboVideoBlog[] newArray(int size) {
            return new WeiboVideoBlog[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mBlog, flags);
    }
}
