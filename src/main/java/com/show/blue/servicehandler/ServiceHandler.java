package com.show.blue.servicehandler;

/**
 * Created by liangzhenxiong on 16/3/7.
 */
public class ServiceHandler {
    long lasttime = 0;
    long delayMillis = 60 * 1000;


    public void oncreate(Object o) {

    }

    public void run() {
        setLastTime(System.currentTimeMillis());
    }

    public void stop() {

    }

    public void delayed(long delayMillis) {
        this.delayMillis = delayMillis;
    }

    public long getDelayed() {
        return delayMillis;
    }

    public long getLastTime() {
        return lasttime;
    }

    public void setLastTime(long delayMillis) {
        lasttime = delayMillis;
    }

}
