package com.show.blue;


import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.LruCache;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.TextView;

import com.example.gaussianblur.blur.FastBlur;
import com.library.uiframe.BaseActivity;
import com.support.loader.adapter.ItemData;
import com.support.loader.adapter.UIListAdapter;
import com.support.loader.utils.ReflectHelper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by liangzhenxiong on 16/4/19.
 */
@SuppressWarnings("ALL")
public class TestActivity extends BaseActivity {


    //    View view;
    AppComponent appComponent;
    Handler h;
    int i = 0;
    int before;
    int after;


    private boolean isKeyboardShown(View rootView) {
        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
    }


    private void setFadingMarquee() {
        ViewConfiguration vc = ViewConfiguration.get(this);
        ViewConfiguration viewConfiguration;
        Object obj = ReflectHelper.getDeclaredFieldValue(vc,
                ViewConfiguration.class.getName(), "mFadingMarqueeEnabled");
        if (obj != null && obj instanceof Boolean) {
            Boolean enabled = (Boolean) obj;
            if (!enabled) {
                ReflectHelper.setDeclaredFieldValue(vc,
                        ViewConfiguration.class.getName(),
                        "mFadingMarqueeEnabled", Boolean.TRUE);
            }
        }
    }


    int s = 2;
    TopLinkedHashMap topLinkedHashMap;
    EditText tv_name;
    String string = "";


    /*
     * 得到图片字节流 数组大小
     * */
    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.write(0);
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
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
                if (tempbitmap == null) {
                    Log.i("TAG", "广告图片解码失败！");
                    FileInputStream inputStream = new FileInputStream(path);
                    byte[] temp = readStream(inputStream);
                    tempbitmap = BitmapFactory.decodeByteArray(temp, 0, temp.length);
                    //如果图片为null, 图片不完整则删除掉图片
//                    file.delete();
                }
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

    // 返回三个资源值, 背景资源, 标题资源, 正文资源
    public int[] getSpecialColors(Context context) {
        String deviceUa = (Build.VERSION.SDK);// Integer.parseInt(Build.VERSION.SDK);
        deviceUa = (deviceUa == null ? "" : deviceUa.toLowerCase());
        int[] colors = null;
        final String[] USE_SPE_OF_DEVICES = {"bird-t9609", "tcl m2u"};
        for (String deviceua : USE_SPE_OF_DEVICES) {
            if (deviceUa.contains(deviceua)) {
//                colors = context.getResources().getIntArray(R.array.bird_t9609_mmnotification_colors);
            }
        }
        if (colors == null) {
            colors = tryGetColors(context);
        }
        return colors;
    }

    private int[] tryGetColors(Context mContext) {
        int[] mColors = getColorsFromNotificationLayout(mContext);
        if (mColors == null) {
            final int SDK_VER = android.os.Build.VERSION.SDK_INT;
            if (SDK_VER >= 23) {// 统一使用android6.0 默认背景色和文字颜色 白底黑字
                mColors = mContext.getResources().getIntArray(R.array.default_mmnotification_colors_v23);
            } else if (SDK_VER <= 20) {// 统一使用android4.0 默认背景色和文字颜色 黑底白字
                mColors = mContext.getResources().getIntArray(R.array.default_mmnotification_colors_v14);
            }
        }
        return mColors;
    }

    /**
     * 找第几个文本
     *
     * @param index
     * @return
     */
    private View findTextView(ViewGroup vg, int index) {
        int N = vg.getChildCount();
        View childview = null;
        for (int k = 0; k < N; k++) {
            childview = vg.getChildAt(k);
            if (childview instanceof TextView) {
                index--;
                if (index < 0) {
                    return childview;
                }
            } else if (childview instanceof ViewGroup) {
                childview = findTextView((ViewGroup) childview, index);
                if (childview != null) {
                    return childview;
                }
            }
        }
        return null;
    }

    private int[] getColorsFromNotificationLayout(Context mContext) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(mContext);
        RemoteViews contentView = null;
        Notification nf = builder.build();
        ;
        contentView = nf.contentView;
        if (contentView == null) {
            contentView = nf.tickerView;
        }
        if (contentView == null) {
            return null;
        }
        int bgcolor, titlecolor, infocolor;
        int layoutid = contentView.getLayoutId();
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(layoutid, null);
        View childview = view.findViewById(android.R.id.title);
        if (childview == null) { //不是标准的通知栏，直接返回
            childview = findTextView((ViewGroup) view, 0); //找第一1个TextView
            if (childview == null) {
                return null;
            }
        }
        titlecolor = mContext.getResources().getColor(android.R.color.white);
        infocolor = titlecolor;
        if (childview instanceof TextView) {
//            ColorStateList csl = ((TextView) childview).getCurrentTextColor();
            titlecolor = ((TextView) childview).getDrawingCacheBackgroundColor();
//            titlecolor = csl.getColorForState(childview.getDrawableState(), titlecolor);
        }
        Object infoid_obj = ReflectHelper.getStaticFieldValue("com.android.internal.R$id", "info");
        if (infoid_obj != null) {
            childview = view.findViewById((Integer) infoid_obj);
        }
        if (childview == null) { // 非标准通知栏
            childview = findTextView((ViewGroup) view, 1); // 找第二个TextView
        }
        if (childview != null && childview instanceof TextView) {
            ColorStateList csl = ((TextView) childview).getTextColors();
            infocolor = csl.getColorForState(childview.getDrawableState(), titlecolor);
        }

        bgcolor = getBackgroundColor(mContext, view);
//        if (isSameColor(bgcolor, titlecolor)) {
//            if (isWhite(bgcolor)) {
//                bgcolor = mContext.getResources().getColor(android.R.color.black);
//            } else {
//                bgcolor = 0xfffafafa;
//            }
//        }
        return new int[]{bgcolor, titlecolor, infocolor};
    }

    private boolean isSameColor(int bgcolor, int textcolor) {
        bgcolor &= 0x00ffffff;
        textcolor &= 0x00ffffff;
        int red = (textcolor >> 16) & 0x0ff;
        int green = (textcolor >> 8) & 0xff;
        int blue = (textcolor & 0x0ff);
        boolean iswhite1 = red == green && red == blue;
        int bgred = (bgcolor >> 16) & 0x0ff;
        int bggreen = (bgcolor >> 8) & 0xff;
        int bgblue = (bgcolor & 0x0ff);
        boolean iswhite2 = (bgred == bggreen && bgred == bgblue);
        int delta = Math.abs(red - bgred);
        return (iswhite1 == iswhite2 && delta < 0x1f) || bgcolor == textcolor;
    }

    private boolean isWhite(int color) {
        color &= 0x00ffffff;
        int red = (color >> 16) & 0x0ff;
        int green = (color >> 8) & 0xff;
        int blue = (color & 0x0ff);
        boolean iswhite1 = red == green && red == blue;
        return iswhite1 && color > 0x1f;
    }

    private int getBackgroundColor(Context mContext, View view) {
        int bgcolorid = 0;
        //com.android.internal.R$color.notification_legacy_background_color see also com.android.systemui.statusbar.ActivatableNotificationView
//	notification_legacy_background_color = #ff1a1a1a
//	notification_material_background_color = #fffafafa
        int targetSDK = mContext.getApplicationInfo().targetSdkVersion;
        int version = Build.VERSION.SDK_INT;
        String model = Build.MODEL;
        String brand = Build.BRAND;
        Integer exceptBg = getExceptionBgColor(brand, model, version, targetSDK);
        if (exceptBg != null) {
            return exceptBg.intValue();
        }
        int bgcolor = 0; //0xfffafafa ;
        int default_bgcolor = 0;
        if (version <= 20) {
            default_bgcolor = mContext.getResources().getColor(android.R.color.black);
        } else if (version == 21) {//三星 SM-A8000 在targetSDK为23时会出现底为透明底，所以给它这个颜色
            default_bgcolor = 0xfffafafa;
        }
        if (targetSDK < version && targetSDK >= Build.VERSION_CODES.GINGERBREAD && targetSDK < 21) { //Build.VERSION_CODES.LOLLIPOP == 21
            ///frameworks/base/packages/SystemUI/src/com/android/systemui/statusbar
            //参照com.android.systemui.statusbar.BaseStatusBar 的; applyColorsAndBackgrounds .
            default_bgcolor = 0xfffafafa; //即为notification_material_background_color，此颜色定义在com.android.systemui.R.color中
        }
        Object obj = ReflectHelper.getDeclaredFieldValue(view, "android.view.View", "mBackgroundResource");
        if (obj != null && obj instanceof Integer) {
            bgcolorid = ((Integer) obj).intValue();
            if (bgcolorid != 0) {
                try {
                    bgcolor = mContext.getResources().getColor(bgcolorid);
                    return bgcolor;
                } catch (Exception e) {

                }
            }
        }
        Drawable dw = view.getBackground();
        if (dw == null) {
            return default_bgcolor;
        }
        //HM NOTE 4.4.2 机采用这种方法取出来的背景就正确了，原先的效果是黑色的
        Bitmap bg = Bitmap.createBitmap(8, 8,
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bg);
        dw.draw(canvas);
        bgcolor = bg.getPixel(3, 3);
        bg.recycle();
        return bgcolor;
    }

    private Integer getExceptionBgColor(String brand, String model, int sdkversion, int targetsdk) {
//        for (ExceptionColor e: EXCEPTION_COLORS ){
//            if (e.mBrand.equals(brand) && e.mModel.equals(model) && e.mSDKVersion == sdkversion /*&& e.mTargetSDK == targetsdk*/){
//                return Integer.valueOf(e.mBackgroundColor);
//            }
//        }
        return null;
    }

    private void dial(String number) {
        Class<TelephonyManager> c = TelephonyManager.class;
        Method getITelephonyMethod = null;
        try {
            getITelephonyMethod = c.getDeclaredMethod("getITelephony",
                    (Class[]) null);
            getITelephonyMethod.setAccessible(true);
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            Object iTelephony;
            iTelephony = (Object) getITelephonyMethod.invoke(tManager, (Object[]) null);
            Method dial = iTelephony.getClass().getDeclaredMethod("dial", String.class);
            dial.invoke(iTelephony, number);
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void call(Activity activity, String number) {
        Class<TelephonyManager> c = TelephonyManager.class;
        Method getITelephonyMethod = null;
        try {
            getITelephonyMethod = c.getDeclaredMethod("getITelephony",
                    (Class[]) null);
            getITelephonyMethod.setAccessible(true);
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
            TelephonyManager tManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            Object iTelephony;
            iTelephony = (Object) getITelephonyMethod.invoke(tManager, (Object[]) null);
            Method dial = iTelephony.getClass().getDeclaredMethod("call", String.class, String.class);
            dial.invoke(iTelephony, "com.show.blue", number);


        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    DlnaFrameLayout dlnaView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
//        Linkify.addLinks()
//        String path = "/storage/emulated/0/mm/cache/logoimage/mm_355_1481731200000_1481903999000.png";

        topLinkedHashMap = new TopLinkedHashMap(new ChangeListener() {
            @Override
            public void OnChanged(String tip) {
//                button.setTextxt(tip);
            }
        });
        tv_name = (EditText) findViewById(R.id.tv_name);

        TD.INSTANCE.string = "不展示的试图";
//        tv_name.setText(TD.INSTANCE.string);
//        tv_name.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v1) {
//
//                if (dlnaView != null) {
//                    return;
//                }
//                String s;
//                TextView textView = new TextView(TestActivity.this);
//                textView.setText("不展示的试图");
//                textView.setTextColor(Color.BLUE);
//                textView.setBackgroundColor(Color.BLACK);
//                textView.setTextSize(20);
//                dlnaView = new DlnaFrameLayout(TestActivity.this.getApplication());
//                FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                FrameLayout.LayoutParams lparams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//                dlnaView.setLayoutParams(lparams);
//                dlnaView.addView(textView, params);
//                dlnaView.startCath();
////                view.setDrawingCacheEnabled(true);
////                Bitmap bitmap = view.getDrawingCache();
//
////                final WindowManager.LayoutParams params =new  WindowManager.LayoutParams();
////                WindowManager windowManager = (WindowManager) TestActivity.this.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
////                params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
////                params.width = 100;
////                params.height = 100;
////                textView.setOnClickListener(new View.OnClickListener() {
////                    @Override
////                    public void onClick(View v) {
////                        textView.setText("打电话"+Math.random());
////                    }
////                });
////                params.type = WindowManager.LayoutParams.TYPE_TOAST;
////                windowManager.addView(textView, params);
////                handler.postDelayed(new Runnable() {
////                    @Override
////                    public void run() {
//////                        PhoneFactory.getDefaultPhone；
////                        call(TestActivity.this, "13502461871");
////                    }
////                }, 600);
////                Sip
//            }
//        });

//        int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//        switch (orientation) {
//        Looper.prepare();
//        Looper.loop();
//        Looper.myLooper().quit();
        ListView list_view = (ListView) findViewById(R.id.list_view);

        List<ItemData> datas = new ArrayList<>();
        datas.add(new WindItem("", 1, this));
        datas.add(new TimeItem("", 1, this, topLinkedHashMap));
        datas.add(new LineItem("", 1, this));
        datas.add(new LightItem("", 1, this));
        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
        appComponent.inject(this);
        datas.add(new ChooseItem("", 1, this, topLinkedHashMap));
//
        LruCache c;
////        Resources res = getResources();
////        String songsFound = res.getQuantityString(R.plurals.numberOfSongsAvailable, 0, cd);
        datas.add(new ChooseItem("", 2, this, topLinkedHashMap));
        datas.add(new ChooseItem("", 3, this, topLinkedHashMap));
        datas.add(new ChooseItem("", 4, this, topLinkedHashMap));
        datas.add(new ChooseItem("", 5, this, topLinkedHashMap));
        UIListAdapter uiListAdapter = new UIListAdapter(this, datas);
        list_view.setAdapter(uiListAdapter);
//        ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
//        am.getRunningAppProcesses();
//        ServiceLoader.getInstance().submit(new Runnable() {
//            @Override
//            public void run() {
//                Looper.prepare();
//                h = new Handler() {
//                    @Override
//                    public void handleMessage(Message msg) {
//                        super.handleMessage(msg);
//                        Log.i("TAG", "=====");
//                        if (msg.what == 2) {
//                            h.getLooper().quit();
//                        }
//                        sendEmptyMessageDelayed(1, 5000);
//                    }
//                };
//                Looper.loop();
//                Log.i("TAG", "==+++++++===");
//
//            }
//        });
//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (i == 0) {
//                    h.sendEmptyMessage(1);
//                    i++;
//                } else {
//                    h.sendEmptyMessage(2);
//                }
//
//            }
//        });
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i("TAG", "onKeyDown=====" + event.getAction());
        return super.onKeyDown(keyCode, event);
    }

    void setload(File dir, String soname) {
        //        File dir = this.getDir("lib", Activity.MODE_PRIVATE);
//        Log.i("TAG", "===dir" + dir.getAbsolutePath());
//        setload(dir, "libEasyAR.so");
//        setload(dir, "libEasyAR.so");
        File distFile = new File(dir.getAbsolutePath() + File.separator + soname);
        if (distFile.exists()) {
            //使用load方法加载内部储存的SO库
            System.load(distFile.getAbsolutePath());
            return;
        }
        if (copyFileFromAssets(this, soname, distFile.getAbsolutePath())) {

            //使用load方法加载内部储存的SO库
            System.load(distFile.getAbsolutePath());

        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Log.i("TAG", "========");
    }

    public static boolean copyFileFromAssets(Context context, String fileName, String path) {

        boolean copyIsFinish = false;

        try {

            InputStream is = context.getAssets().open(fileName);

            File file = new File(path);

            file.createNewFile();

            FileOutputStream fos = new FileOutputStream(file);

            byte[] temp = new byte[1024];

            int i = 0;

            while ((i = is.read(temp)) > 0) {

                fos.write(temp, 0, i);

            }

            fos.close();

            is.close();

            copyIsFinish = true;

        } catch (IOException e) {

            e.printStackTrace();


        }

        return copyIsFinish;

    }

    MainFragmentComponent getappComponent() {
        return appComponent.mainFragmentComponent();
    }

    double getdensity(Activity activity) {
        Point point;
        DisplayMetrics dm = getResources().getDisplayMetrics();
        if (Build.VERSION.SDK_INT > 13) {
            dm = getResources().getDisplayMetrics();
            point = new Point();
            if (Build.VERSION.SDK_INT > 16) {
                activity.getWindowManager().getDefaultDisplay().getRealSize(point);
            } else {
                activity.getWindowManager().getDefaultDisplay().getSize(point);
            }
            double x = Math.pow(point.x / dm.xdpi, 2);
            double y = Math.pow(point.y / dm.ydpi, 2);
            double screenInches = Math.sqrt(x + y);
            double density = Math.sqrt(Math.pow(point.x, 2) + Math.pow(point.y, 2)) / screenInches / DisplayMetrics.DENSITY_MEDIUM;
            return density;
        }
        return dm.density;
    }


    private Bitmap blur(Bitmap var3, int radius) {
        Bitmap temp = Bitmap.createBitmap(var3, var3.getWidth() / 2, var3.getHeight() / 2, var3.getWidth() * 1 / 2 - 2, var3.getHeight() / 2 - 1);
        Bitmap overlay = Bitmap.createBitmap((temp.getWidth()),
                (temp.getHeight()), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        Paint paint = new Paint();
//        float scaleFactor = 0.5f;
//        canvas.scale(scaleFactor, scaleFactor);
        canvas.translate(1.3f, 1.3f);
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(temp, 0, 0, paint);
        Bitmap bitmap = FastBlur.doBlur(overlay, 5, false);
        overlay.recycle();
        temp.recycle();


        Bitmap overlay_db = Bitmap.createBitmap((bitmap.getWidth()),
                (bitmap.getHeight()), Bitmap.Config.ARGB_8888);
        Canvas canvas_db = new Canvas(overlay_db);
//                                Paint paint_db = new Paint();
        float scaleFactor = 1.3f;
        canvas_db.scale(scaleFactor, scaleFactor);
        canvas_db.translate(-10, -10);
//                                paint_db.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas_db.drawBitmap(bitmap, 0, 0, paint);
        return overlay_db;


    }

    Bitmap getBackgroundBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), (bitmap.getHeight() >> 1),
                Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);
        Bitmap temp = Bitmap.createBitmap(bitmap, 0, (bitmap.getHeight() >> 2),
                bitmap.getWidth(), (bitmap.getHeight() * 3 >> 2));
        Bitmap blurbitmap = FastBlur.doBlur(temp, 1, false);

        temp.recycle();
        Paint p = new Paint();
//        p.setAlpha(179);
        canvas.drawBitmap(blurbitmap, 0, 0, p);
        blurbitmap.recycle();
        Rect wrect = new Rect(0, (int) ((output.getHeight() * 0.45)), output.getWidth(), output.getHeight());
        p.reset();
        LinearGradient lgw = new LinearGradient((wrect.width() >> 1), wrect.top,
                (wrect.width() >> 1), wrect.top + wrect.height() / 4, Color.argb(255, 255, 255, 255),
                Color.argb(255, 255, 255, 255), Shader.TileMode.CLAMP);
        p.setShader(lgw);
        canvas.drawRect(wrect, p);
        p.reset();
        Rect rect = new Rect(0, 0, output.getWidth(), wrect.top);
        LinearGradient lg = new LinearGradient((rect.width() >> 1), 0,
                (rect.width() >> 1), (rect.bottom), Color.argb(65, 255, 255, 255),
                Color.argb(242, 255, 255, 255), Shader.TileMode.CLAMP);
        p.setShader(lg);
        canvas.drawRect(rect, p);
        return output;
    }
//    Bitmap getBackgroundBitmap(Bitmap bitmap) {
//        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), (bitmap.getHeight() >> 1),
//                Bitmap.Config.ARGB_8888);
//
//        Canvas canvas = new Canvas(output);
//        Bitmap temp = Bitmap.createBitmap(bitmap, 0, (bitmap.getHeight() >> 2),
//                bitmap.getWidth(), (bitmap.getHeight() * 3 >> 2));
//        Bitmap blurbitmap = FastBlur.doBlur(temp, 8, false);
//
//        temp.recycle();
//        Paint p = new Paint();
//        p.setAlpha(179);
//        canvas.drawBitmap(blurbitmap, 0, 0, p);
//        blurbitmap.recycle();
//        Rect wrect = new Rect(0, (int) ((output.getHeight() * 0.45)), output.getWidth(), output.getHeight());
//        p.reset();
//        LinearGradient lgw = new LinearGradient((wrect.width() >> 1), wrect.top,
//                (wrect.width() >> 1), wrect.top + wrect.height() / 4, Color.argb(242, 255, 255, 255),
//                Color.argb(253, 255, 255, 255), Shader.TileMode.CLAMP);
//        p.setShader(lgw);
//        canvas.drawRect(wrect, p);
//        p.reset();
//        Rect rect = new Rect(0, 0, output.getWidth(), wrect.top);
//        LinearGradient lg = new LinearGradient((rect.width() >> 1), 0,
//                (rect.width() >> 1), (rect.bottom), Color.argb(65, 255, 255, 255),
//                Color.argb(242, 255, 255, 255), Shader.TileMode.CLAMP);
//        p.setShader(lg);
//        canvas.drawRect(rect, p);
//        return output;
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    protected void onPause() {
        super.onPause();
    }


//    @SuppressWarnings("NewApi")
//    @SuppressLint("NewApi")
//    void set(Context context, int mJobId, ComponentName jobServicet) {
//        if (Build.VERSION.SDK_INT >= 21) {
//            JobInfo uploadTask = new JobInfo.Builder(mJobId,
//                    jobServicet)
//                    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
//                    .build();
//            JobScheduler jobScheduler =
//                    (JobScheduler) context.getSystemService(JOB_SCHEDULER_SERVICE);
//            jobScheduler.schedule(uploadTask);
//        }
//    }

    private void send() {
        Intent intent = new Intent();
        intent.setAction("com.aspire.action.STARTUP");
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo(this.getPackageName(), 0);
            intent.putExtra("package", info.packageName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        sendBroadcast(intent);
    }

}
