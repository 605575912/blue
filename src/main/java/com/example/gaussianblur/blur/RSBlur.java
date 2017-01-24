//package com.example.gaussianblur.blur;
//
//import android.annotation.TargetApi;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.os.Build;
//import android.renderscript.Allocation;
//import android.renderscript.RenderScript;
//import android.renderscript.ScriptIntrinsicBlur;
//
///**
// * Created by liangzhenxiong on 16/3/8.
// */
//public class RSBlur {
//    @TargetApi(Build.VERSION_CODES.KITKAT)
//    static Bitmap doBlur(Bitmap overlay, int radius, Context context) {
//        RenderScript rs = RenderScript.create(context);
//
//        Allocation overlayAlloc = Allocation.createFromBitmap(
//                rs, overlay);
//
//        ScriptIntrinsicBlur blur = ScriptIntrinsicBlur.create(
//                rs, overlayAlloc.getElement());
//
//        blur.setInput(overlayAlloc);
//
//        blur.setRadius(radius);
//
//        blur.forEach(overlayAlloc);
//
//        overlayAlloc.copyTo(overlay);
//        rs.destroy();
//        return overlay;
//
//
//    }
//}
