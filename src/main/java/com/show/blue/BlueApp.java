package com.show.blue;

import android.app.Application;
import android.content.Context;
import android.os.StrictMode;

import com.support.loader.utils.LogBlue;

import static android.os.Build.VERSION.SDK_INT;


/**
 * Created by liangzhenxiong on 16/4/4.
 */
public class BlueApp extends Application{
    public static  int  value = -1;
   static BlueApp app;
    @Override public void onCreate() {
        super.onCreate();
        app = this;
        LogBlue.i("TAG","Application=====onCreate");
//enabledStrictMode();
//        		 CrashHandler.getInstance().init(app);

//        LeakCanary.install(this);
    }
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
//        MultiDex.install(this);

    }
    private void enabledStrictMode() {
        if (SDK_INT >= 9) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder() //
                    .detectAll() //
                    .penaltyLog() //
                    .penaltyDeath() //
                    .build());
        }
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

}
