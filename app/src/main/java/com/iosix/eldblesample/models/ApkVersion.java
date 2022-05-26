package com.iosix.eldblesample.models;

import com.google.gson.annotations.SerializedName;

public class ApkVersion {

    @SerializedName("apk_version")
    private String apk_version;

    @SerializedName("mobile_version")
    private String mobile_version;

    public ApkVersion(String apk_version,String mobile_version) {
        this.apk_version = apk_version;
        this.mobile_version = mobile_version;
    }

    public String getApk_version() {
        return apk_version;
    }

    public void setApk_version(String apk_version) {
        this.apk_version = apk_version;
    }

    public String getMobile_version() {
        return mobile_version;
    }

    public void setMobile_version(String mobile_version) {
        this.mobile_version = mobile_version;
    }
}
