package com.show.blue.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.show.blue.servicehandler.ServiceHandler;
import com.show.blue.servicehandler.SplashHandler;
import com.support.loader.ServiceLoader;
import com.support.loader.utils.LogBlue;
import com.tencent.bugly.crashreport.CrashReport;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by liangzhenxiong on 16/3/6.
 */
public class LauncherService extends Service {
    HashMap<String, ServiceHandler> handlerHashMap;

    void initApp() {
        LogBlue.setIsopen(true);
        ServiceLoader.getInstance().Init(getApplicationContext());
        CrashReport.initCrashReport(this, "900018302", false);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogBlue.i("TAG","===========");
        initApp();
        onCreateHandler();
//        updateAPK();
    }



    void initTimerHandler() {
        handlerHashMap = new HashMap<String, ServiceHandler>();
        handlerHashMap.put("SplashHandler", new SplashHandler());
    }

    int requestCode = 0x110;
    String requestTYPE = "PUSH_TYPE";

    /**
     *
     */
    void onCreateHandler() {
        if (handlerHashMap == null) {
            initTimerHandler();
        }
        ServiceLoader.getInstance().submit(new Runnable() {
            @Override
            public void run() {
                long ltime = 0;
                for (Map.Entry<String, ServiceHandler> entry : handlerHashMap.entrySet()) {
                    ServiceHandler serviceHandler = entry.getValue();
                    serviceHandler.oncreate(LauncherService.this);
                    startHandler(serviceHandler);
                    if (ltime > serviceHandler.getDelayed() || ltime == 0) {
                        ltime = serviceHandler.getDelayed();
                    }
                }
                AlarmManager am = ((AlarmManager) getSystemService(Context.ALARM_SERVICE));
                Intent sIntent = new Intent(LauncherService.this, LauncherService.class);
//                sIntent.putExtra(PUSH_HANDLER, START_BY_ALARM);
                sIntent.putExtra(requestTYPE, requestCode);
                PendingIntent pi = PendingIntent.getService(LauncherService.this, requestCode, sIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                am.setRepeating(AlarmManager.RTC_WAKEUP, 0, ltime, pi);
            }
        });

    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startAlarm(intent);
        return super.onStartCommand(intent, flags, startId);

    }

    void startAlarm(Intent intent) {
        if (intent != null) {
            int intExtra = intent.getIntExtra(requestTYPE, 0);
            if (intExtra == requestCode) {
                ServiceLoader.getInstance().submit(new Runnable() {
                    @Override
                    public void run() {
                        for (Map.Entry<String, ServiceHandler> entry : handlerHashMap.entrySet()) {
                            ServiceHandler serviceHandler = entry.getValue();
                            startHandler(serviceHandler);
                        }

                    }
                });
            }
        }
    }

    void startHandler(ServiceHandler serviceHandler) {
        if (serviceHandler != null) {
            long delay = System.currentTimeMillis() - serviceHandler.getLastTime();
            if (delay >= serviceHandler.getDelayed()) {
                try {
                    serviceHandler.run();
                } catch (Exception e) {
                    e.printStackTrace();
                    LogBlue.e("TAG", serviceHandler.getClass().getSimpleName() + "定时器", e);
                }
            }

        }


    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
