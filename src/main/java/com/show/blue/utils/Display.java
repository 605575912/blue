package com.show.blue.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Created by liangzhenxiong on 16/3/7.
 */
public class Display {
    private static final String TAG = "Display";
    private static int [] defaultDisplay = null;
    /**
     * 获取当前屏幕分辨率   宽度×高度
     * @param activity
     * @return
     */
    public static int[] getDefaultDisplay(Activity activity) {
        synchronized (TAG) {
            if (defaultDisplay == null) {
                DisplayMetrics metric = new DisplayMetrics();
                activity.getWindowManager().getDefaultDisplay().getMetrics(metric);
                int width = metric.widthPixels; // 屏幕宽度（像素）
                int height = metric.heightPixels; // 屏幕高度（像素）
                defaultDisplay = new int[] { width, height };
            }
        }
        return defaultDisplay;
    }
}
