package com.example.gaussianblur.blur;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.graphics.Palette;
import android.util.DisplayMetrics;
import android.view.View;
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
import com.support.loader.utils.LogBlue;

/**
 * Created by liangzhenxiong on 16/3/14.
 */
public class BlurDetailsActivity extends BaseActivity {
    boolean iskitkat;
    ImageOptions options;
    ImageView imageView4;
    LinearLayout details_bk;
    LinearLayout bk_linear;
    Button bt_install;
    ImageView imageView5;
    TextView tv_des, tv_title, tv_app, tv_appdes;

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
        setContentView(R.layout.details_layout);
        View status_h = findViewById(R.id.status_h);
        if (iskitkat) {
            int h = FastBlurActivity.getStatusHeight(getApplicationContext());
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) status_h.getLayoutParams();
            params.height = h;
        } else {
            status_h.setVisibility(View.GONE);
        }

        options = new ImageOptions(getApplicationContext(), R.mipmap.ic_launcher);
        details_bk = (LinearLayout) findViewById(R.id.details_bk);
        bk_linear = (LinearLayout) findViewById(R.id.bk_linear);
        imageView4 = (ImageView) findViewById(R.id.imageView4);
        imageView5 = (ImageView) findViewById(R.id.imageView5);
        bt_install = (Button) findViewById(R.id.bt_install);
        tv_des = (TextView) findViewById(R.id.tv_des);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_app = (TextView) findViewById(R.id.tv_app);
        tv_appdes = (TextView) findViewById(R.id.tv_appdes);

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView4.getLayoutParams();
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        int with = (int) (metric.widthPixels - (getResources().getDimension(R.dimen.fab_margin) * 2));
        layoutParams.width = with;
        layoutParams.height = with * 2 / 5;
        imageView4.setLayoutParams(layoutParams);
        imageView4.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                imageView4.getViewTreeObserver().removeOnPreDrawListener(this);
                ServiceLoader.getInstance().displayImage(options, "http://i3.res.meizu.com/fileserver/ad/img/540/a35bede38ed3413f99e0931a85cf9fb7.png", imageView4, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String var1, View var2) {

                    }

                    @Override
                    public void onLoadingFailed(String var1, View var2) {

                    }

                    @Override
                    public void onLoadingComplete(String var1, View var2, Bitmap var3) {
                        var2.setDrawingCacheEnabled(true);
                        final Bitmap bitmap = var2.getDrawingCache();
                        if (bitmap == null) {
                            blurThread(var3);
                            return;
                        } else {
                            blurThread(bitmap);
                        }

//                PaletteColor(bitmap);
                    }

                    @Override
                    public Bitmap onLoadingBitmap(String var1, View var2, Bitmap var3) {
                        return null;
                    }

                    @Override
                    public void onLoadingCancelled(String var1, View var2) {

                    }
                });
                ServiceLoader.getInstance().displayImage(options, "http://i3.res.meizu.com/fileserver/app_icon_origin/804/d5667fb9f5bc49368db3273a29c3e59d.png", imageView5);

                return true;
            }
        });
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
   }

    void PaletteColor(Bitmap bitmap) {
        Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                // palette为生成的调色板
                Palette.Swatch s1 = palette.getVibrantSwatch(); //充满活力的色板
                Palette.Swatch s2 = palette.getDarkVibrantSwatch(); //充满活力的暗色类型色板
                Palette.Swatch s3 = palette.getLightVibrantSwatch(); //充满活力的亮色类型色板
                int s4 = palette.getMutedColor(Color.BLUE); //黯淡的色板
                Palette.Swatch s5 = palette.getDarkMutedSwatch(); //黯淡的暗色类型色板（翻译过来没有原汁原味的赶脚啊！）
                Palette.Swatch s6 = palette.getLightMutedSwatch(); //黯淡的亮色类型色板
                if (s5 != null) {
//                        titleView.setBackgroundColor(s1.getRgb());
//                        titleView.setTextColor(swatch.getTitleTextColor()); //设置文本颜色
                    Bitmap overlay = Bitmap.createBitmap(bt_install.getWidth(),
                            bt_install.getHeight(), Bitmap.Config.ARGB_8888);
                    Canvas canvas = new Canvas(overlay);
                    RectF rectF = new RectF(0, 0, overlay.getWidth(), overlay.getHeight());
                    Paint paint = new Paint();
                    paint.setColor(s5.getRgb());
                    paint.setStrokeWidth(1);
                    paint.setAntiAlias(true);
                    paint.setStyle(Paint.Style.FILL);
                    canvas.drawRoundRect(rectF, overlay.getHeight() / 2, overlay.getHeight() / 2, paint);
                    bt_install.setBackgroundDrawable(new BitmapDrawable(overlay));
                }
//                if (s4 != null) {
//                        titleView.setBackgroundColor(s1.getRgb());
//                        titleView.setTextColor(swatch.getTitleTextColor()); //设置文本颜色
                bk_linear.setBackgroundColor(s4);
            }
//                if (s2!=null){
//                    details_bk.setBackgroundColor(s2.getRgb());
//                }
//                if (s3!=null){
//                    tv_appdes.setTextColor(s2.getRgb());
//                }
//            }
        });
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (msg.what == 0) {
            details_bk.setBackgroundColor(msg.arg1);
        }
        if (msg.what == 1) {
            bk_linear.setBackgroundColor(msg.arg1);
        }
        if (msg.what == 2) {
            bt_install.setBackgroundDrawable(new BitmapDrawable((Bitmap) msg.obj));
        }
        if (msg.what == 3) {
            tv_appdes.setTextColor(msg.arg1);
        }
    }

    void blurThread(final Bitmap bitmap) {
        if (bitmap == null) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                super.run();

//                int color = getColor(bitmap, 0.10, 0.60);


                backgroundColor(bitmap, 0.95, 0.7);
                bkColor(bitmap, 0.8, 0.6);

//                int color2 = getColor(bitmap, 0.40, 0.50);
                buttonColor(bitmap, 0.9f, 0.1f);

            }
        }.start();
    }

    void backgroundColor(Bitmap bitmap, double xf, double yf) {
        int rgb = bitmap.getPixel((int) (bitmap.getWidth() * xf), (int) (bitmap.getHeight() * yf));
        int r = Color.red(rgb);
        int g = Color.green(rgb);
        int b = Color.blue(rgb);
        LogBlue.i("TAG", "r=======");
        LogBlue.i("TAG", "r" + r);
        LogBlue.i("TAG", "g" + g);
        LogBlue.i("TAG", "b" + b);

        int r1 = (int) (r * 2.75);
        int g1 = (int) (g * 2.21);
        int b1 = (int) (b * 1.67);//  83,91,183

        int color1 = Color.argb(rgb, r1, g1, b1);
        Message msg0 = handler.obtainMessage(0);
        msg0.arg1 = color1;
        handler.sendMessage(msg0);


        int r0 = r * 4;
        int g0 = (int) (g * 3.5);
        int b0 = (int) (b * 2.2);
        LogBlue.i("TAG", "r==" + r0);
        LogBlue.i("TAG", "g" + g0);
        LogBlue.i("TAG", "b" + b0);
        LogBlue.i("TAG", "r=======");
        //171,182,251
        int color0 = Color.argb(Color.alpha(rgb), r0, g0, b0);
        Message msg1 = handler.obtainMessage(3);
        msg1.arg1 = color0;
        handler.sendMessage(msg1);
    }

    void bkColor(Bitmap bitmap, double xf, double yf) {
        int rgb = bitmap.getPixel((int) (bitmap.getWidth() * xf), (int) (bitmap.getHeight() * yf));
        int r = Color.red(rgb);
        int g = Color.green(rgb);
        int b = Color.blue(rgb);
        LogBlue.i("TAG", "r" + r);
        LogBlue.i("TAG", "g" + g);
        LogBlue.i("TAG", "b" + b);
        int r1 = r * 2;
        int g1 = (int) (g * 2.8);
        int b1 = (int) (b * 1.6);//96,105,205  83,91,183
        LogBlue.i("TAG", "r" + r1);
        LogBlue.i("TAG", "g" + g1);
        LogBlue.i("TAG", "b" + b1);
        int color1 = Color.argb(rgb, r1, g1, b1);

        Message msg0 = handler.obtainMessage(1);
        msg0.arg1 = color1;
        handler.sendMessage(msg0);


    }

    void buttonColor(Bitmap bitmap, double xf, double yf) {
        int rgb = bitmap.getPixel((int) (bitmap.getWidth() * xf), (int) (bitmap.getHeight() * yf));
        int r = Color.red(rgb);
        int g = Color.green(rgb);
        int b = Color.blue(rgb);
        LogBlue.i("TAG", "r====button===");
        LogBlue.i("TAG", "r" + r);
        LogBlue.i("TAG", "g" + g);
        LogBlue.i("TAG", "b" + b);
        LogBlue.i("TAG", "r====button===reset");
        int r1 = (int) (r * 0.95);
        int g1 = (int) (g * 1.53);
        int b1 = (int) (b * 1.16);//  46,60,141
        LogBlue.i("TAG", "r1" + r1);
        LogBlue.i("TAG", "g1" + g1);
        LogBlue.i("TAG", "b1" + b1);


        int color = Color.argb(Color.alpha(rgb), r, g, b);
        Bitmap overlay = Bitmap.createBitmap(bt_install.getMeasuredWidth(),
                bt_install.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        RectF rectF = new RectF(0, 0, overlay.getWidth(), overlay.getHeight());
        Paint paint = new Paint();
        paint.setColor(color);
        paint.setStrokeWidth(1);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRoundRect(rectF, overlay.getHeight() / 2, overlay.getHeight() / 2, paint);


        Message msg2 = handler.obtainMessage(2);
        msg2.obj = overlay;
        handler.sendMessage(msg2);
    }

    static int getColor(Bitmap bitmap, double xf, double yf) {
        int rgb = bitmap.getPixel((int) (bitmap.getWidth() * xf), (int) (bitmap.getHeight() * yf));
        int r = Color.red(rgb);
        int g = Color.green(rgb);
        int b = Color.blue(rgb);
        if ((r > 220 && g > 220 && b > 220) || (r < 40 && g < 40 && b < 40)) {
            xf = xf + 0.05;
            if (xf < 0.9) {
                return getColor(bitmap, xf, yf);
            } else {
                r = 125;
                g = 125;
                b = 125;
            }
        } else {
            r = (int) (r * 0.85);
            g = (int) (g * 0.80);
            b = (int) (b * 0.75);
        }
        int color = Color.argb(Color.alpha(rgb), r, g, b);
        return color;
    }
}
