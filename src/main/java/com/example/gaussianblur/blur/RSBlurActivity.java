//package com.example.gaussianblur.blur;
//
//import android.app.Activity;
//import android.graphics.Bitmap;
//import android.graphics.Canvas;
//import android.graphics.Paint;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.util.DisplayMetrics;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.ViewTreeObserver;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.example.gaussianblur.R;
//
//
///**
// * Created by paveldudka on 3/4/14.
// */
//public class RSBlurActivity extends Activity {
//    private final String DOWNSCALE_FILTER = "downscale_filter";
//
//    private ImageView image;
//    private CheckBox downScale;
//    private TextView statusText;
//    Bitmap bitmap;
//    long startMs;
//    Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            if (msg.what == 0) {
//                image.setImageBitmap((Bitmap) msg.obj);
//                statusText.setText(System.currentTimeMillis() - startMs + "ms");
//            }
//
//        }
//    };
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.fragment_layout);
//        image = (ImageView) findViewById(R.id.picture);
//        statusText = addStatusText((ViewGroup) findViewById(R.id.controls));
//        addCheckBoxes((ViewGroup) findViewById(R.id.controls));
//
//        if (savedInstanceState != null) {
//            downScale.setChecked(savedInstanceState.getBoolean(DOWNSCALE_FILTER));
//        }
//        applyBlur();
//    }
//
//
//    private void applyBlur() {
//        if (bitmap == null) {
//            DisplayMetrics metric = new DisplayMetrics();
//            getWindowManager().getDefaultDisplay().getMetrics(metric);
//            int width = metric.widthPixels; // 屏幕宽度（像素）
//            int height = metric.heightPixels; // 屏幕高度（像素）
//            bitmap = ImageUtils.getimage(getResources(), R.drawable.picture, width, height);
//        }
//
//        image.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
//            @Override
//            public boolean onPreDraw() {
//                image.getViewTreeObserver().removeOnPreDrawListener(this);
//                image.buildDrawingCache();
//
////                Bitmap bmp = image.getDrawingCache();
//
//                blurThread();
//                return true;
//            }
//        });
//    }
//
//    void blurThread() {
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                startMs = System.currentTimeMillis();
//                Bitmap bmp = blur(bitmap, image);
//                Message msg = handler.obtainMessage(0);
//                msg.obj = bmp;
//                handler.sendMessage(msg);
//            }
//        }.start();
//    }
//
//    private Bitmap blur(Bitmap bkg, View view) {
//
//        float scaleFactor = 1;
//        float radius = 1;
//        if (downScale.isChecked()) {
//            scaleFactor = 8;
//            radius = 10;
//        } else {
//            return bkg;
//        }
//        Bitmap overlay = Bitmap.createBitmap((int) (view.getMeasuredWidth() / scaleFactor),
//                (int) (view.getMeasuredHeight() / scaleFactor), Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(overlay);
//        canvas.translate(-view.getLeft() / scaleFactor, -view.getTop() / scaleFactor);
//        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
//        Paint paint = new Paint();
//        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
//        canvas.drawBitmap(bkg, 0, 0, paint);
//        overlay = RSBlur.doBlur(overlay, (int) radius, RSBlurActivity.this);
//        return overlay;
//
//
//    }
//
//    @Override
//    public String toString() {
//        return "Fast blur";
//    }
//
//    private void addCheckBoxes(ViewGroup container) {
//
//        downScale = new CheckBox(RSBlurActivity.this);
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
//
//    private TextView addStatusText(ViewGroup container) {
//        TextView result = new TextView(RSBlurActivity.this);
//        result.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        result.setTextColor(0xFFFFFFFF);
//        container.addView(result);
//        return result;
//    }
//
//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        outState.putBoolean(DOWNSCALE_FILTER, downScale.isChecked());
//        super.onSaveInstanceState(outState);
//    }
//}
