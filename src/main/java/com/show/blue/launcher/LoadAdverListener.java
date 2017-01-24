package com.show.blue.launcher;

/**
 * Created by liangzhenxiong on 16/3/7.
 */
public interface LoadAdverListener {
    /**
     * 广告图片
     */
    void loadSuccessBitmap(String path, String jumpurl);

    void loadBitmapFail();
}
