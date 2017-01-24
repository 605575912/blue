package com.example.gaussianblur.color;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

import com.example.gaussianblur.blur.ImageUtils;
import com.show.blue.R;

/**
 * Created by liangzhenxiong on 16/3/11.
 */
public class ColorIntActivity extends Activity {
    ImageView imageView, imageView2;
    Bitmap bitmap;
    Bitmap temp;
    int drawColor;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                imageView.setImageBitmap((Bitmap) msg.obj);
                imageView.setDrawingCacheEnabled(true);
                temp = imageView.getDrawingCache();
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.colorint_layout);
        imageView = (ImageView) findViewById(R.id.imageView);
        imageView2 = (ImageView) findViewById(R.id.imageView2);
        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blurThread();
            }
        });
        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    return true;
                }
                if (event.getAction() == MotionEvent.ACTION_MOVE) {
                    if (temp.getHeight() > event.getY() && temp.getWidth() > event.getX() && event.getY() > 0 && event.getX() > 0) {
                        int color = temp.getPixel((int) event.getX(), (int) event.getY());
                        if (drawColor == color) {
                            return true;
                        }
                        drawColor = color;
                        Bitmap overlay = Bitmap.createBitmap((imageView.getWidth()),
                                (imageView.getHeight()), Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(overlay);
                        canvas.drawColor(color);
                        imageView2.setImageBitmap(overlay);
                    }

                    return true;
                }
                return true;
            }
        });
    }

    void blurThread() {


        new Thread() {
            @Override
            public void run() {
                super.run();
                if (bitmap == null) {
                    DisplayMetrics metric = new DisplayMetrics();
                    getWindowManager().getDefaultDisplay().getMetrics(metric);
                    int width = metric.widthPixels; // 屏幕宽度（像素）
                    int height = 300; // 屏幕高度（像素）
                    bitmap = ImageUtils.getimage(getResources(), R.mipmap.pk_bg, width, height);
                }
                int[] arrayColor=new int[bitmap.getWidth()*bitmap.getHeight()];
                int count=0;
                for(int i=0;i<bitmap.getHeight();i++){
                    for(int j=0;j<bitmap.getWidth();j++){
                        int color1=bitmap.getPixel(j,i);
                        //这里也可以取出 R G B 可以扩展一下 做更多的处理，
                        //暂时我只是要处理除了透明的颜色，改变其他的颜色
                        if(color1!=0){
                        }else{
//                            color1=color;
                        }
//                        arrayColor[count]=color;
                        count++;
                    }
                }
                Message msg = handler.obtainMessage(0);
                msg.obj = bitmap;
                handler.sendMessage(msg);
            }
        }.start();
    }
}
