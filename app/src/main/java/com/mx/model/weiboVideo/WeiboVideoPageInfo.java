package com.mx.model.weiboVideo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by YiuChoi on 2016/4/12 0012.
 */
public class WeiboVideoPageInfo implements Parcelable{
    @SerializedName("page_pic")
    private String videoPic;
    @SerializedName("page_url")
    private String videoUrl;

    protected WeiboVideoPageInfo(Parcel in) {
        videoPic = in.readString();
        videoUrl = in.readString();
    }

    public static final Creator<WeiboVideoPageInfo> CREATOR = new Creator<WeiboVideoPageInfo>() {
        @Override
        public WeiboVideoPageInfo createFromParcel(Parcel in) {
            return new WeiboVideoPageInfo(in);
        }

        @Override
        public WeiboVideoPageInfo[] newArray(int size) {
            return new WeiboVideoPageInfo[size];
        }
    };

    public String getVideoPic() {
        return videoPic;
    }

    public void setVideoPic(String videoPic) {
        this.videoPic = videoPic;
    }

    public String getVideoUrl() {
        if (videoUrl.contains("http://miaopai.com"))
            videoUrl = videoUrl.replace("http://miaopai.com", "http://www.miaopai.com");
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(videoPic);
        dest.writeString(videoUrl);
    }
}
