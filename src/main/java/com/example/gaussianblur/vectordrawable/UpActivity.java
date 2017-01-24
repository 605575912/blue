//package com.example.gaussianblur.vectordrawable;
//
//import android.app.Activity;
//import android.graphics.drawable.Animatable;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.widget.ImageView;
//
//import com.example.gaussianblur.R;
//
//
///**
// * Created by liangzhenxiong on 16/3/8.
// */
//public class UpActivity extends Activity{
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.up_layout);
//        ImageView mCpuImageView = (ImageView) findViewById(R.id.cpu);
//        Drawable drawable = mCpuImageView.getDrawable();
//        if (drawable instanceof Animatable) {
//            ((Animatable) drawable).start();
//        }
//
//        ImageView mPathImageView = (ImageView) findViewById(R.id.path);
//        Drawable PathDrawable = mPathImageView.getDrawable();
//        if (PathDrawable instanceof Animatable) {
//            ((Animatable) PathDrawable).start();
//        }
//    }
//}
