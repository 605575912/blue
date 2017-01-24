package com.example.gaussianblur.blur;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

/**
 * Created by liangzhenxiong on 16/3/8.
 */
public class ImageUtils {
    public static Bitmap getimage(Resources resources,int id, int width, int height) {

        try {
            BitmapFactory.Options newOpts = new BitmapFactory.Options();
            newOpts.inJustDecodeBounds = false;
            newOpts.inSampleSize = 1;
            Bitmap tempbitmap;
            try {
                tempbitmap = BitmapFactory.decodeResource(resources, id, newOpts);
                return tempbitmap;
            } catch (OutOfMemoryError e) {
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
            newOpts.inJustDecodeBounds = true;
            tempbitmap = BitmapFactory.decodeResource(resources, id, newOpts);
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
                tempbitmap = BitmapFactory.decodeResource(resources, id, newOpts);
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
}
