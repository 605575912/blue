package com.show.blue.launcher;

import com.support.loader.proguard.IProguard;

/**
 * Created by liangzhenxiong on 16/1/15.
 */
public class SplashItem implements IProguard.ProtectMembers {
    long id = 0;
    String imgurl = "";
    String jumpurl = "";//跳转地址;该字段不存在，则不需要跳转
    long begintime = 0;
    long endtime = 0;//结束的时间戳 0 代表不限制结束时间

    public long getBegintime() {
        return begintime;
    }

    public long getEndtime() {
        return endtime;
    }

    public long getId() {
        return id;
    }

    public String getImgurl() {
        return imgurl;
    }

    public String getJumpurl() {
        return jumpurl;
    }
}
