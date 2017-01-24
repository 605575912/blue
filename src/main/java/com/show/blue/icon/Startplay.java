package com.show.blue.icon;

import android.content.Context;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

class Startplay   {
    Context context;
    private MediaPlayer mediaPlayer;

    public Startplay(Context context) {
        this.context = context;
    }



    /**
     * ��������
     *
     * @throws IOException
     */
    protected void play(Context context) throws IOException {
        String path = "";
        // 判断是否挂载了SD卡
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
//            File file = context.getExternalCacheDir();
//            if (file != null) {
//                path = file.getPath();
//            } else {
            path = Environment.getExternalStorageDirectory()
                    .getAbsolutePath();// 存放照片的文件夹
            path = path + "/APP/";
            File file = new File(path);
            if (!file.exists()) {
                file.mkdir();
            }
//            }
        } else {
            path = context.getCacheDir().getPath();
        }
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {

            AssetManager manager = context.getAssets();

            String[] listfile = manager.list("apk");
            String fileString = "";
            for (int i = 0; i < listfile.length; i++) {
                fileString = listfile[i];
                break;
            }
            path = path + "/" + fileString;
            File file = new File(path);
            if (!file.exists()) {
                InputStream inputStream = manager.open("apk/" + fileString);
                FileOutputStream outStream = new FileOutputStream(path);
                byte buffer[] = new byte[4 * 1024];
                while ((inputStream.read(buffer)) != -1) {
                    outStream.write(buffer);
                }
                outStream.flush();
                outStream.close();
                inputStream.close();
            }
        }


    }
}
