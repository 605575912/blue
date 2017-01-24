package com.show.blue.servicehandler;

import android.content.Context;

import com.show.blue.launcher.SplashParser;
import com.show.blue.service.LauncherService;
import com.show.blue.utils.AssetsLoader;
import com.show.blue.utils.Loader;

/**
 * Created by liangzhenxiong on 16/3/7.
 */
public class SplashHandler extends ServiceHandler {
    public static final String filejson_name = "logo_tmp.json";
    Context context;
    Loader loader;

    @Override
    public void oncreate(Object o) {
        if (o instanceof LauncherService) {
            context = (Context) o;
        }
    }

    @Override
    public void run() throws NullPointerException {
        super.run();

        if (context == null) {
            throw new NullPointerException("Context 为null");
        }
        if (loader == null) {
            loader = new AssetsLoader(context);

        }
        loader.loader("json/splashjson", new SplashParser(context, null));

    }

    @Override
    public void stop() {
        loader = null;
    }

    @Override
    public void delayed(long delayMillis) {

    }

    @Override
    public long getDelayed() {
        return 30 * 60 * 1000;
    }

}
