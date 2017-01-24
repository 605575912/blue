//package com.show.blue.version;
//
//import android.os.Handler;
//import android.os.Message;
//
//import com.support.loader.utils.DownloadUtil;
//
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.net.HttpURLConnection;
//import java.net.MalformedURLException;
//import java.net.URL;
//
//public class ApkRunnable implements Runnable {
//    String apkurl = "";
//    String apkname = "bear.apk";
//    private boolean interceptFlag = false;
//    Handler handler;
//
//    public ApkRunnable(String apkurl, Handler handler, String apkname) {
//        this.apkurl = apkurl;
//        this.handler = handler;
//        this.apkname = apkname;
//    }
//
//    @Override
//    public void run() {
//        Thread.currentThread();
//        FileOutputStream fos = null;
//        InputStream is = null;
//        String apkFile = DownloadUtil.getInstance().getFILEPATH() + "/" + apkname;
//        File ApkFile = new File(apkFile);
//        try {
//            fos = new FileOutputStream(ApkFile);
//        } catch (FileNotFoundException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//            handler.sendEmptyMessage(UpdateManager.UPDATE_FAIL);
//            return;
//        }
//        try {
//
//            URL url = new URL(apkurl);
//
//            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//            conn.setConnectTimeout(30000);
//            conn.setReadTimeout(30000);
//            conn.setRequestProperty("Accept-Encoding", "identity");
//            Message mlegth = handler.obtainMessage();
//            mlegth.what = UpdateManager.UPDATE_LENAGTH;
//
//
//            mlegth.obj = 0.0;
//            handler.sendMessage(mlegth);
//            conn.connect();
//
//            int length = conn.getContentLength();
//            is = conn.getInputStream();
//            int count = 0;
//            byte buf[] = new byte[2048];
//            float add = 0;
//            int dat = 5;
//            int progress = 0;
//            Message msglegth = handler.obtainMessage();
//            msglegth.what = UpdateManager.UPDATE_LENAGTH;
//
//
//            msglegth.obj = length/1024/1024.0;
//            handler.sendMessage(msglegth);
//            do {
//                Message msg = handler.obtainMessage();
//                if (length > 1024 * 1024 * 3) {
//                    dat = 1;
//                }
//                int numread = is.read(buf);
//                count += numread;
//                add = ((float) count / length) * 100;
//                if ((add - progress) > dat && add < 100) {
//                    progress = (int) add;
//                    msg.what = UpdateManager.UPDATE_DOWN;
//                    msg.arg1 = progress;
//                    handler.sendMessage(msg);
//                }
//
//                if (numread <= 0) {
//                    // 下载完成通知安装
//                    progress = 100;
//                    msg.what = UpdateManager.UPDATE_DOWN;
//                    msg.arg1 = progress;
//                    handler.sendMessage(msg);
//                    handler.sendEmptyMessageDelayed(UpdateManager.UPDATE_INSTALL, 500);
//                    interceptFlag = true;
//                }
//                fos.write(buf, 0, numread);
//                Thread.sleep(10);
//            } while (!interceptFlag);// 点击取消就停止下载.
//            fos.close();
//            is.close();
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            // downloadDialog.dismiss();
//            // errorDialog();
//            Thread.currentThread().interrupt();
//        }
//        if (fos != null) {
//            try {
//                fos.close();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//        if (is != null) {
//            try {
//                is.close();
//            } catch (IOException e) {
//                // TODO Auto-generated catch block
//                e.printStackTrace();
//            }
//        }
//        if (interceptFlag) {
//
//        } else {
//            handler.sendEmptyMessage(UpdateManager.UPDATE_FAIL);
//        }
//    }
//}
