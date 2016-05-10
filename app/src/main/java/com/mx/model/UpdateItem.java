package com.mx.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by boobooL on 2016/5/10 0010
 * Created 邮箱 ：boobooMX@163.com
 */
public class UpdateItem {
    @SerializedName("versionCode")
    private int versionCode;
    @SerializedName("versionName")
    private String versionName;
    @SerializedName("downloadUrl")
    private String doenloadUrl;
    @SerializedName("releaseNote")
    private String releaseNote;

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public String getDoenloadUrl() {
        return doenloadUrl;
    }

    public void setDoenloadUrl(String doenloadUrl) {
        this.doenloadUrl = doenloadUrl;
    }

    public String getReleaseNote() {
        return releaseNote;
    }

    public void setReleaseNote(String releaseNote) {
        this.releaseNote = releaseNote;
    }
}
