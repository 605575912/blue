package com.show.blue.version;

/**
 * Created by liangzhenxiong on 16/3/7.
 */
public class Version {
    String versionName;
    int compel;
    int versionCode;
    String apkURL;
    String message = "";
    String[] content;

    public String getApkURL() {
        return apkURL;
    }

    public void setApkURL(String apkURL) {
        this.apkURL = apkURL;
    }

    public int getCompel() {
        return compel;
    }

    public void setCompel(int compel) {
        this.compel = compel;
    }

    public String[] getContent() {
        return content;
    }

    public void setContent(String[] content) {
        this.content = content;
    }

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
