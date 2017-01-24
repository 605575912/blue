package com.show.blue.launcher;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.library.uiframe.BaseActivity;
import com.show.blue.R;
import com.show.blue.TestActivity;
import com.show.blue.home.HomeDatePacket;
import com.show.blue.service.ShowIntentService;
import com.show.blue.servicehandler.SplashHandler;
import com.show.blue.utils.Display;
import com.show.blue.utils.FileLoader;
import com.show.blue.utils.Loader;
import com.support.loader.ServiceLoader;
import com.support.loader.utils.AppManager;
import com.support.loader.utils.DownloadUtil;
import com.support.loader.utils.LogBlue;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by liangzhenxiong on 16/3/6.
 */
public class LauncherActivity extends BaseActivity {
    TextView tv_time;
    ImageView imageview;
    Bitmap adverbitmap;
    private NotificationManager mNotificationManager;
    String ACTION_NOTIFICATION_DELETE = "ACTION_NOTIFICATION_DELETE";
    private PendingIntent mDeletePendingIntent;
    private static final String AUTHORITY = "com.show.blue";
    private static final String NAME = "MM6.0.0.001.01_CT.ANDROID-debug.apk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launcher_layout);
        View v = findViewById(R.id.home_title_bar);
        ServiceLoader.getInstance().Init(getApplicationContext());

//        mNotificationManager = (NotificationManager) getSystemService(
//                Context.NOTIFICATION_SERVICE);
        registerReceiver(mDeleteReceiver, new IntentFilter(ACTION_NOTIFICATION_DELETE));
//        Intent deleteIntent = new Intent(ACTION_NOTIFICATION_DELETE);
//        mDeletePendingIntent = PendingIntent.getBroadcast(this,
//                2323 /* requestCode */, deleteIntent, 0);
//        addNotificationAndReadNumber(this.getApplicationContext());

//        String cacheDirPath = DownloadUtil.getInstance().getFILEPATH();
//        if (TextUtils.isEmpty(cacheDirPath)) {
//            return;
//        }
//        ApplicationInfo appinfo = getApplicationInfo();
//        int targetSDK = appinfo.targetSdkVersion;
//        if (targetSDK >= 23 && Build.VERSION.SDK_INT >= 23) {
//            File fs = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);
//            File f = new File(getFilesDir(), NAME);
//            if (!f.exists()) {
//                AssetManager assets = getResources().getAssets();
//                try {
//                    copy(assets.open(NAME), f);
//                } catch (IOException e) {
//                    Log.e("FileProvider", "Exception copying from assets", e);
//                }
//            }
//            Intent i =
//                    new Intent(Intent.ACTION_INSTALL_PACKAGE)
//                            .setDataAndType(FileProvider.getUriForFile(this, AUTHORITY, f),
//                                    "application/vnd.android.package-archive")
//                            .addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//
//            startActivity(i);
//
//        } else {
//            File file = new File(cacheDirPath, NAME);
//            if (!file.exists()) {
//                AssetManager assets = getResources().getAssets();
//                try {
//                    copy(assets.open(NAME), file);
//                } catch (IOException e) {
//                    Log.e("FileProvider", "Exception copying from assets", e);
//                }
//            }
//            Uri pkg_uri = Uri.fromFile(file);
//            Intent it = new Intent(Intent.ACTION_VIEW);
//            it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            it.setDataAndType(pkg_uri, "application/vnd.android.package-archive");
//            startActivity(it);
//        }
//
////        finish();
        welcomebanner();
    }

//    static private void copy(InputStream in, File dst) throws IOException {
//        FileOutputStream out = new FileOutputStream(dst);
//        byte[] buf = new byte[8192];
//        int len;
//
//        while ((len = in.read(buf)) > 0) {
//            out.write(buf, 0, len);
//        }
//
//        in.close();
//        out.close();
//    }

    private int mNotificationId = 0;

//    private void addNotificationAndReadNumber(Context context) {
//        final Notification.Builder builder = new Notification.Builder(context)
//                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.bco))
//                .setSmallIcon(R.mipmap.bco)
//                .setContentTitle(getString(R.string.action_settings))
//                .setContentText(getString(R.string.leak_canary_display_activity_label))
//                .setAutoCancel(true)
//                .setDeleteIntent(mDeletePendingIntent);
//        Intent intent = new Intent();
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.setClass(context, ShowIntentService.class);
//        PendingIntent redirectPendIntent = PendingIntent.getService(context,
//                101, intent, PendingIntent.FLAG_NO_CREATE);
//
//        if (Build.VERSION.SDK_INT >= 16) {
//            builder.setContentIntent(redirectPendIntent);
//            final Notification notification = builder.build();
////        notification.contentIntent = redirectPendIntent;
//            mNotificationManager.notify(++mNotificationId, notification);
//        }
//
//    }

    private BroadcastReceiver mDeleteReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    };
//    private void insertDummyContactWrapper(Activity activity) {
//        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(activity,
//                Manifest.permission.WRITE_CONTACTS);
//        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {
//            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity,
//                    Manifest.permission.WRITE_CONTACTS)) {
////                showMessageOKCancel("You need to allow access to Contacts",
////                        new DialogInterface.OnClickListener() {
////                            @Override
////                            public void onClick(DialogInterface dialog, int which) {
////                                ActivityCompat.requestPermissions(activity,
////                                        new String[] {Manifest.permission.WRITE_CONTACTS},
////                                        REQUEST_CODE_ASK_PERMISSIONS);
////                            }
////                        });
//                return;
//            }
//            ActivityCompat.requestPermissions(activity,
//                    new String[] {Manifest.permission.WRITE_CONTACTS},
//                    REQUEST_CODE_ASK_PERMISSIONS);
//            return;
//        }
////        insertDummyContact();
//
//    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AppManager.getAppManager().finishActivity(this);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 展示广告图
     */
    private void welcomebanner() {
        initBanerView();
        loadBanner();
    }

    void initBanerView() {
        tv_time = (TextView) findViewById(R.id.tv_time);
        ShapeDrawable shapeDrawable = new ShapeDrawable();
        Shape shape = new Shape() {
            @Override
            public void draw(Canvas canvas, Paint paint) {
                paint.setColor(Color.BLACK);
                paint.setStrokeWidth(1);
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL);
                RectF rectF = new RectF(0, 0, tv_time.getWidth(), tv_time.getHeight());
                paint.setAlpha(125);
                canvas.drawRoundRect(rectF, 4, 4, paint);
            }
        };
        shapeDrawable.setShape(shape);
        tv_time.setBackgroundDrawable(shapeDrawable);
    }

    /**
     * 广告成功后 显示图标等信息
     */
    void initShowAdver(Bitmap bitmap, final String jumpurl) {
        imageview = (ImageView) findViewById(R.id.imageview);
        tv_time.setVisibility(View.VISIBLE);
        imageview.setImageBitmap(bitmap);

        if (!TextUtils.isEmpty(jumpurl)) {
//            imageview.setOnTouchListener(mAccidentProofClick);
            imageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    handler.removeCallbacksAndMessages(null);
//                    startHome(jumpurl);
                }
            });
        }
    }

    /**
     * 读取广告缓存数据
     */
    void loadBanner() {
        String cacheDirPath = DownloadUtil.getInstance().getFILEPATH();
        Display.getDefaultDisplay(LauncherActivity.this);
        if (!TextUtils.isEmpty(cacheDirPath)) {
            LogBlue.i(TAG, "开启广告：5秒后如果没法展示广告，将跳转到首页");
            handler.sendEmptyMessageDelayed(LOADIMAGE_WHAT, 10);//避免加载太长时间,5秒后都要跳转到首页
            //解析JSON 500ms 加载图片500ms，总时间约1秒
            String tmpfilename = cacheDirPath + "/" + SplashHandler.filejson_name;
            final Loader loader = new FileLoader(getApplicationContext());
            loader.loader(tmpfilename, new SplashParser(this.getApplicationContext(), new LoadAdverListener() {

                @Override
                public void loadSuccessBitmap(String path, final String jumpurl) {
                    LogBlue.i(TAG, "开启广告：获取本地广告图片 地址为=" + path);
//                    path ="/storage/emulated/0/1.png";
                    adverbitmap = getimage(path, Display.getDefaultDisplay(LauncherActivity.this)[0], Display.getDefaultDisplay(LauncherActivity.this)[1]);
                    if (adverbitmap != null) {
                        showAdver(jumpurl, adverbitmap);
                    } else {
                        loadBitmapFail();
                    }
                }

                @Override
                public void loadBitmapFail() {
                    LogBlue.i(TAG, "开启广告：暂无符合规则的广告数据，稍后跳转到首页");
                    handler.sendEmptyMessageDelayed(LOADIMAGE_WHAT, 3000);//跳转到首页
                }
            }));
        } else {
            LogBlue.i(TAG, "开启广告：SD卡目录不存在，将跳转到首页");
            handler.sendEmptyMessage(LOADIMAGE_WHAT);//跳转到首页
        }
    }

    /**
     * 展示广告以及倒计时
     *
     * @param jumpurl
     * @param bitmap
     */
    void showAdver(final String jumpurl, final Bitmap bitmap) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                //取消5秒后跳转首页的消息
                handler.removeMessages(LOADIMAGE_WHAT);
                //初始化显示图标
                initShowAdver(bitmap, jumpurl);


            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (adverbitmap != null) {
            //开始倒计时
            Message msg = handler.obtainMessage(DELOAD_WHAT);
            msg.obj = SHOW_LOGO_TIME_SEN;
            handler.sendMessage(msg);
        }
    }

    /**
     * 从本地获取广告图片
     *
     * @param path
     * @param width
     * @param height
     * @return
     */
    Bitmap getimage(String path, int width, int height) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        try {
            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            newOpts.inJustDecodeBounds = false;
            newOpts.inSampleSize = 1;
            Bitmap tempbitmap;
            try {
                tempbitmap = BitmapFactory.decodeFile(path, newOpts);
                return tempbitmap;
            } catch (OutOfMemoryError e) {
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            newOpts.inJustDecodeBounds = true;
            tempbitmap = BitmapFactory.decodeFile(path, newOpts);
            int sreen = width * height;
            int image = tempbitmap.getHeight() * tempbitmap.getWidth();
            if (sreen <= 720) {// 防止过小图
                sreen = 720 * 1080;
            }
            int samplesize = image / sreen;
            if (samplesize < 2) {
                samplesize = 2;
            }
            newOpts.inSampleSize = samplesize;
            newOpts.inJustDecodeBounds = false;
            try {
                tempbitmap = BitmapFactory.decodeFile(path, newOpts);
                return tempbitmap;
            } catch (OutOfMemoryError e) {

            } catch (Exception e) {
                e.printStackTrace();


            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    void startService() {
//        Intent intentService = new Intent("com.show.blue.launcher.LauncherService");
//        intentService.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startService(intentService);
//        Intent intent = new Intent();
//        ComponentName componetName = new ComponentName("com.show.blue.launcher", "com.show.blue.launcher.LauncherService");
//        intent.setComponent(componetName);
//        startActivity(intent);
//        Intent intentService = new Intent();
//        intentService.setClass(this, LauncherService.class);
//        startService(intentService);
    }

    @Override
    public void initViewDelaod() {
        super.initViewDelaod();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adverbitmap != null) {
            adverbitmap.recycle();
            adverbitmap = null;
        }
        if (mDeleteReceiver != null) {
            unregisterReceiver(mDeleteReceiver);
        }
    }

    int SHOW_LOGO_TIME_SEN = 0;// 定时长
    int DELOAD_WHAT = 0;
    int LOADIMAGE_WHAT = 1;
    int BITMAP_WHAT = 2;

    @Override
    public void handleMessage(Message msg) {
//        super.handleMessage(msg);


//        final HotSaleActivity activity = mActivityReference.get();
//        if (activity != null) {
        if (msg.what == DELOAD_WHAT) {
            int time = (Integer) msg.obj;
            if (time <= 0) {

                tv_time.setText("0");
                handler.sendEmptyMessageDelayed(LOADIMAGE_WHAT, 0);
            } else {
                tv_time.setText(String.valueOf(time));
                Message msg0 = handler.obtainMessage(DELOAD_WHAT);
                time--;
                msg0.obj = time;
                handler.sendMessageDelayed(msg0, 1000);
            }

        } else if (msg.what == LOADIMAGE_WHAT) {
            handler.removeMessages(LOADIMAGE_WHAT);
            starthome();
        } else if (msg.what == BITMAP_WHAT) {
//                if (imageview_bk != null) {
//                    imageview_bk.setImageBitmap((Bitmap) msg.obj);
//                }
        }
    }

    void starthome() {
        Intent intent = new Intent();
        intent.setClass(LauncherActivity.this, TestActivity.class);
        startActivity(intent);
        finish();
    }
}
