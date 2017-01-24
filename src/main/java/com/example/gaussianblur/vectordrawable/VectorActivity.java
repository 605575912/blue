//package com.example.gaussianblur.vectordrawable;
//
//import android.app.Activity;
//import android.graphics.drawable.Animatable;
//import android.graphics.drawable.Drawable;
//import android.os.Bundle;
//import android.util.Log;
//import android.widget.ImageView;
//
//import com.example.gaussianblur.R;
//
///**
// * Created by liangzhenxiong on 16/3/8.
// */
//public class VectorActivity extends Activity{
//    private ImageView svgImageView;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.vector_layout);
////        VectorDrawable vectorDrawable = new VectorDrawable();
//        svgImageView = (ImageView)findViewById(R.id.svgImageView);
//        Drawable drawable = svgImageView.getDrawable();
//        if (drawable instanceof Animatable) {
//            ((Animatable) drawable).start();
//            Log.d("Animatable", "true");
//        }else{
//            Log.d("Animatable", "false");
//        }
//    }
//}
