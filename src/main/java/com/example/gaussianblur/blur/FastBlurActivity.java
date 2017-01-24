package com.example.gaussianblur.blur;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.graphics.Palette;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.library.uiframe.BaseActivity;
import com.show.blue.R;
import com.support.loader.ServiceLoader;
import com.support.loader.packet.ImageOptions;
import com.support.loader.utils.ImageLoadingListener;

import java.util.ArrayList;


/**
 * Created by paveldudka on 3/4/14.
 */
public class FastBlurActivity extends BaseActivity {
    private final String DOWNSCALE_FILTER = "downscale_filter";

    LinearLayout bk_linear;
    boolean iskitkat;
    TextView tv_tips;
    Button bt_install;
    ImageView iv_logo;
    //    ViewPager viewpager;
    ImageOptions options;
    LinearLayout scrollView_linear;

    @Override
    public void handleMessage(final Message msg) {
        super.handleMessage(msg);
        if (msg.what == 0) {

        }
        if (msg.what == 1) {
            Bitmap bitmap = (Bitmap) msg.obj;
            bt_install.setBackgroundDrawable(new BitmapDrawable(bitmap));

        }


    }

    ImageLoadingListener imageLoadingListener = new Listener();

    class Listener implements ImageLoadingListener {

        @Override
        public void onLoadingStarted(String var1, View var2) {

        }

        @Override
        public void onLoadingFailed(String var1, View var2) {

        }

        @Override
        public void onLoadingComplete(String var1, final View var2, final Bitmap var3) {


        }

        @Override
        public Bitmap onLoadingBitmap(String var1, View var2, final Bitmap var3) {
            if (bk_linear.getTag() == null) {
                bk_linear.setTag(1);
//                        var2.setDrawingCacheEnabled(true);
//                        final Bitmap bitmap = var2.getDrawingCache();

                final Bitmap bitmap = blur(var3, 30);

                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        Drawable bd = new Drawable() {
                            @Override
                            public void draw(Canvas canvas) {
//                                canvas.drawBitmap(var3, 0, 0, new Paint());
                                Rect src = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
                                Rect dst = new Rect(0, 0, canvas.getWidth(), canvas.getHeight());
                                canvas.drawBitmap(bitmap, src, dst, new Paint());
                            }

                            @Override
                            public void setAlpha(int alpha) {

                            }

                            @Override
                            public void setColorFilter(ColorFilter cf) {

                            }

                            @Override
                            public int getOpacity() {
                                return 0;
                            }
                        };
                        bk_linear.setBackgroundDrawable(bd);
                    }
                });
            } else {

            }
            return var3;

        }

        @Override
        public void onLoadingCancelled(String var1, View var2) {

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= 19) {
            Window window = getWindow();
            // Translucent status bar
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

            iskitkat = true;
        }


        setContentView(R.layout.fragment_layout);
        View status_h = findViewById(R.id.status_h);
        if (iskitkat) {
            int h = getStatusHeight(getApplicationContext());
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) status_h.getLayoutParams();
            params.height = h;
        } else {
            status_h.setVisibility(View.GONE);
        }
        bk_linear = (LinearLayout) findViewById(R.id.bk_linear);
        tv_tips = (TextView) findViewById(R.id.tv_tips);
        bt_install = (Button) findViewById(R.id.bt_install);
        iv_logo = (ImageView) findViewById(R.id.iv_logo);
        scrollView_linear = (LinearLayout) findViewById(R.id.scrollView_linear);
//        viewpager = (ViewPager) findViewById(R.id.viewpager);
        options = new ImageOptions(getApplicationContext(), R.mipmap.ic_launcher);
        applyBlur();
        ArrayList<String> strings = getIntent().getStringArrayListExtra("urls");
        if (strings == null) {
            strings = new ArrayList<String>();
            strings.add("http://i3.res.meizu.com/fileserver/app_ad_snap/187/3a3c5a01db924437be3226e491e76fa7.jpg");
            strings.add("http://i3.res.meizu.com/fileserver/app_ad_snap/187/e2430b0f6bba4a2ab936b11a266cd483.jpg");
            strings.add("http://i3.res.meizu.com/fileserver/app_ad_snap/187/50fb38167546490295f6acbe8fc298bf.jpg");
            strings.add("http://i3.res.meizu.com/fileserver/app_ad_snap/187/69383a7825f84f3884b0675498b889c5.jpg");
            strings.add("http://i3.res.meizu.com/fileserver/app_ad_snap/187/a64374204a3d4811bc5ec206f473b00d.jpg");
        } else {

        }

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        for (String s : strings) {
            View view = layoutInflater.inflate(R.layout.pager_item, null);
            ImageView imageView = (ImageView) view.findViewById(R.id.imageview);

//            String URL = fileList.get(position);
            ServiceLoader.getInstance().displayImage(options, s, imageView, imageLoadingListener);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
            layoutParams.width = (int) (metric.widthPixels * 0.5);
            layoutParams.bottomMargin = (int) getResources().getDimension(R.dimen.bab_margin);
            imageView.setLayoutParams(layoutParams);
            scrollView_linear.addView(view);
        }

    }


    /**
     * 获得状态栏的高度
     *
     * @param context
     * @return
     */
    public static int getStatusHeight(Context context) {

        int statusHeight = -1;
        try {
            Class<?> clazz = Class.forName("com.android.internal.R$dimen");
            Object object = clazz.newInstance();
            int height = Integer.parseInt(clazz.getField("status_bar_height")
                    .get(object).toString());
            statusHeight = context.getResources().getDimensionPixelSize(height);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return statusHeight;
    }

    private void applyBlur() {
        Intent intent = getIntent();
        final String URL = intent.getExtras().getString("url");

        bk_linear.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                bk_linear.getViewTreeObserver().removeOnPreDrawListener(this);
                iv_logo.buildDrawingCache();
                ServiceLoader.getInstance().displayImage(options, URL, iv_logo, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String var1, View var2) {

                    }

                    @Override
                    public void onLoadingFailed(String var1, View var2) {

                    }

                    @Override
                    public void onLoadingComplete(String var1, View var2, Bitmap var3) {
                        var2.setDrawingCacheEnabled(true);
                        final Bitmap bitmap = iv_logo.getDrawingCache();
                        blurThread(bitmap);
//                        PaletteColor(bitmap);
                    }

                    @Override
                    public Bitmap onLoadingBitmap(String var1, View var2, Bitmap var3) {
                        return var3;
                    }

                    @Override
                    public void onLoadingCancelled(String var1, View var2) {

                    }
                });

                return true;
            }
        });
    }

    void PaletteColor(Bitmap bitmap) {
        Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                // palette为生成的调色板
                Palette.Swatch s1 = palette.getVibrantSwatch(); //充满活力的色板
//                Palette.Swatch s2 = palette.getDarkVibrantSwatch(); //充满活力的暗色类型色板
                Palette.Swatch s3 = palette.getLightVibrantSwatch(); //充满活力的亮色类型色板
                Palette.Swatch s4 = palette.getMutedSwatch(); //黯淡的色板
                Palette.Swatch s5 = palette.getDarkMutedSwatch(); //黯淡的暗色类型色板（翻译过来没有原汁原味的赶脚啊！）
                Palette.Swatch s6 = palette.getLightMutedSwatch(); //黯淡的亮色类型色板
                if (s3 != null) {
//                        titleView.setBackgroundColor(s1.getRgb());
//                        titleView.setTextColor(swatch.getTitleTextColor()); //设置文本颜色
                    Bitmap overlay = Bitmap.createBitmap(bt_install.getWidth(),
                            bt_install.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(overlay);
                    RectF rectF = new RectF(0, 0, overlay.getWidth(), overlay.getHeight());
                    Paint paint = new Paint();
                    paint.setColor(s3.getRgb());
                    paint.setStrokeWidth(1);
                    paint.setAntiAlias(true);
                    paint.setStyle(Paint.Style.FILL);
                    canvas.drawRoundRect(rectF, overlay.getHeight() / 2, overlay.getHeight() / 2, paint);
                    bt_install.setBackgroundDrawable(new BitmapDrawable(overlay));
                }
//                if (s1 != null) {
////                        titleView.setBackgroundColor(s1.getRgb());
////                        titleView.setTextColor(swatch.getTitleTextColor()); //设置文本颜色
////                    bk_linear.setBackgroundColor(s1.getRgb());
//                }
//                if (s2 != null) {
////                    details_bk.setBackgroundColor(s2.getBodyTextColor());
//                }
            }
        });
    }

    void blurThread(final Bitmap bitmap) {

        new Thread() {
            @Override
            public void run() {
                super.run();

                int color = getColor(bitmap, 0.10, 0.60);
                Bitmap overlay = Bitmap.createBitmap(bt_install.getWidth(),
                        bt_install.getHeight(), Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(overlay);
                RectF rectF = new RectF(0, 0, overlay.getWidth(), overlay.getHeight());
                Paint paint = new Paint();
                paint.setColor(color);
                paint.setStrokeWidth(1);
                paint.setAntiAlias(true);
                paint.setStyle(Paint.Style.FILL);
                canvas.drawRoundRect(rectF, overlay.getHeight() / 2, overlay.getHeight() / 2, paint);
                Message msg1 = handler.obtainMessage(1);
                msg1.obj = overlay;
                handler.sendMessage(msg1);


            }
        }.start();
    }

    Bitmap bitmap;

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bitmap != null) {
            bitmap.recycle();
        }
    }

    static int getColor(Bitmap bitmap, double xf, double yf) {
        int rgb = bitmap.getPixel((int) (bitmap.getWidth() * xf), (int) (bitmap.getHeight() * yf));

//            int alpha = RGBValues >> 24;
//            int red = RGBValues >> 16 & 0xFF;
//            int green = RGBValues >> 8 & 0xFF;
//            int blue = RGBValues & 0xFF;


        int r = Color.red(rgb);
        int g = Color.green(rgb);
        int b = Color.blue(rgb);
//        if ((r > 220 && g > 220 && b > 220) || (r < 40 && g < 40 && b < 40)) {
//            xf = xf + 0.05;
//            if (xf < 0.9) {
//                return getColor(bitmap, xf, yf);
//            } else {
//                r = 125;
//                g = 125;
//                b = 125;
//            }
//        } else {
//            r = (int) (r * 0.85);
//            g = (int) (g * 0.80);
//            b = (int) (b * 0.75);
//        }

        r = (int) Math.floor(r * (1 - 0.1));
        g = (int) Math.floor(g * (1 - 0.1));
        b = (int) Math.floor(b * (1 - 0.1));
        int color = Color.argb(Color.alpha(rgb), r, g, b);
        return color;
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


    @Override
    public String toString() {
        return "Fast blur";
    }

//    private void addCheckBoxes(ViewGroup container) {
//
//        downScale = new CheckBox(FastBlurActivity.this);
//        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//        downScale.setLayoutParams(lp);
//        downScale.setText("Downscale before blur");
//        downScale.setVisibility(View.VISIBLE);
//        downScale.setTextColor(0xFFFFFFFF);
//        downScale.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                blurThread();
//            }
//        });
//        container.addView(downScale);
//    }

    private TextView addStatusText(ViewGroup container) {
        TextView result = new TextView(FastBlurActivity.this);
        result.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        result.setTextColor(0xFFFFFFFF);
        container.addView(result);
        return result;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
//        outState.putBoolean(DOWNSCALE_FILTER, downScale.isChecked());
        super.onSaveInstanceState(outState);
    }
}
