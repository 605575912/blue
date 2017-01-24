package com.show.blue.launcher;

import android.content.Context;
import android.text.TextUtils;

import com.android.json.stream.JsonObjectReader;
import com.android.json.stream.JsonObjectWriter;
import com.android.json.stream.UniformErrorException;
import com.show.blue.servicehandler.SplashHandler;
import com.show.blue.utils.JsonBaseParser;
import com.support.loader.utils.DownloadUtil;
import com.support.loader.utils.LogBlue;
import com.support.loader.utils.Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by liangzhenxiong on 16/3/7.
 */
public class SplashParser implements JsonBaseParser {
    LoadAdverListener loadAdverListener;

    public SplashParser(Context context, LoadAdverListener loadAdverListener) {
        super();
        this.loadAdverListener = loadAdverListener;
    }

    @Override
    public void doparser(JsonObjectReader jsonObjectReader, String string) {
        Splashs splashs = new Splashs();
        try {
            if (jsonObjectReader == null) {
                return;
            }
            jsonObjectReader.readObject(splashs);
            if (loadAdverListener == null) {
                writeAdverLogoItems(splashs);
            }
            if (splashs.splashs == null) {
                return;
            }
            HandleData(splashs.splashs);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UniformErrorException e) {
            e.printStackTrace();
        }

    }

    /**
     * 对广告数据进行处理
     *
     * @param splashs
     */
    void HandleData(SplashItem[] splashs) {
        long maxid = 0;//同一天内的取ID最大值
        String currentpath = null;
        String jumpurl = null;
        if (loadAdverListener != null) {//展示广告时候
            for (SplashItem splashItem : splashs) {
                if (isEffective(splashItem)) {
                    String path = getImagePath(splashItem);
                    if (isexistImageFile(path)) {
                        if (isIncludeToday(splashItem.getBegintime())) {
                            if (maxid < splashItem.getId()) {
                                maxid = splashItem.getId();
                                currentpath = path;
                                jumpurl = splashItem.getJumpurl();
                            }
                        }
                    }
                }
            }
            if (!TextUtils.isEmpty(currentpath)) {
                loadAdverListener.loadSuccessBitmap(currentpath, jumpurl);
            } else {
                loadAdverListener.loadBitmapFail();
            }
        } else {        //获取数据时候处理数据方式
            ArrayList<String> undeleteFiles = new ArrayList<String>();
            for (SplashItem splashItem : splashs) {
                if (isEffective(splashItem)) {
                    String path = getImagePath(splashItem);
                    undeleteFiles.add(path);
                    String image_path = downImage(splashItem);
                    if (TextUtils.isEmpty(image_path)) {
                        LogBlue.i(getClass().getSimpleName(), "广告图片下载失败");
                    }
                }
            }
            deleteFileUnIndex(undeleteFiles);//删除旧图片文件
        }
    }


    /**
     * 除了了列表中的路径 都删除
     */
    void deleteFileUnIndex(ArrayList<String> undeleteFiles) {
        String cacheDirPath = DownloadUtil.getInstance().getFILEPATH();
        if (TextUtils.isEmpty(cacheDirPath)) {
            return;
        }
        boolean isall = false;
        if (undeleteFiles == null || undeleteFiles.isEmpty()) {
            isall = true;
        }
        String imageFilePath = cacheDirPath + "/logoimage";
        File file = new File(imageFilePath);
        if (file.exists() && file.isDirectory()) {
            File[] files = file.listFiles();
            for (File file0 : files) {
                if (!file0.isDirectory()) {
                    if (isall) {
                        file0.delete();
                    } else {
                        if (undeleteFiles.indexOf(file0.getPath()) < 0) {
                            file0.delete();
                        }
                    }
                }
            }
        }
    }

    /**
     * 是否当天范围内
     *
     * @param milliseconds
     * @return
     */
    boolean isIncludeToday(long milliseconds) {
        if (System.currentTimeMillis() > milliseconds || milliseconds == 0) {
            return true;
        }
//        Calendar cal = Calendar.getInstance();
//        cal.setTimeInMillis(milliseconds);
//        int day = cal.get(Calendar.HOUR_OF_DAY);
//        if (day > -1 & day < 24) {
//            return true;
//        }
        return false;
    }

    /**
     * 结束时间 以及是否可以生成文件路径判断是否有效广告数据
     *
     * @param splashItem
     * @return
     */
    boolean isEffective(SplashItem splashItem) {
        if (splashItem.getEndtime() < System.currentTimeMillis() && splashItem.getEndtime() != 0) {//过期广告数据  0无限制
            return false;
        }
        String path = getImagePath(splashItem);
        if (TextUtils.isEmpty(path)) {
            return false;
        }
        return true;
    }

    /**
     * 图片命名
     *
     * @param splashItem
     * @return
     */
    String getImageName(SplashItem splashItem) {
        if (splashItem != null && !TextUtils.isEmpty(splashItem.getImgurl())) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("mm_");
            stringBuilder.append(splashItem.getId());
            stringBuilder.append("_");
            stringBuilder.append(splashItem.getBegintime());
            stringBuilder.append("_");
            stringBuilder.append(splashItem.getEndtime());
            if (splashItem.getImgurl().lastIndexOf(".jpg") > -1) {
                stringBuilder.append(".jpg");
            } else if (splashItem.getImgurl().lastIndexOf(".png") > -1) {
                stringBuilder.append(".png");
            } else {
                stringBuilder.append(".jpg");
            }
            return stringBuilder.toString();
        }
        return null;
    }

    /**
     * 获取该广告图片SD卡路径
     *
     * @param splashItem
     * @return
     */
    String getImagePath(SplashItem splashItem) {
        String imageName = getImageName(splashItem);
        String cacheDirPath = DownloadUtil.getInstance().getFILEPATH();
        if (TextUtils.isEmpty(imageName) || TextUtils.isEmpty(cacheDirPath)) {
            return null;
        }
        String imageFilePath = cacheDirPath + "/logoimage";
        File imageFile = new File(imageFilePath);
        if (!imageFile.exists()) {
            imageFile.mkdir();
        }
        return imageFilePath + "/" + imageName;
    }

    /**
     * 是否存在该路径的图片
     *
     * @param iamgepath
     * @return
     */
    boolean isexistImageFile(String iamgepath) {
        if (TextUtils.isEmpty(iamgepath)) {
            return false;
        }
        File imageFile = new File(iamgepath);
        if (imageFile.exists()) {
            if (Util.isValidImageFile(iamgepath)) {
                LogBlue.i(getClass().getSimpleName(), "广告：已存在该广告完整图片");
                return true;
            } else {
                LogBlue.i(getClass().getSimpleName(), "广告：已存在该广告图片,但图片有损");
            }
        }
        return false;
    }

    /**
     * 下载广告图片
     *
     * @param splashItem
     * @return
     */
    private String downImage(SplashItem splashItem) {
        String path = getImagePath(splashItem);
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        if (isexistImageFile(path)) {
            return path;
        }
        boolean interceptFlag = false;
        URL myFileUrl = null;
        FileOutputStream fos;
        try {
            fos = new FileOutputStream(path);
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return null;
        }
        try {
            myFileUrl = new URL(splashItem.getImgurl());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        HttpURLConnection conn;
        try {
            conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.setConnectTimeout(3000);
            conn.setReadTimeout(3000);
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            return null;
        }


        byte buf[] = new byte[2048];
        int count = 0;
        int time = 0;
        while (time < 3) {
            try {
                time++;
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();
                do {
                    int numread = is.read(buf);
                    if (numread <= -1) {
                        break;
                    }
                    count += numread;
                    fos.write(buf, 0, numread);
                    if (count >= length) {
                        break;
                    }

                } while (!interceptFlag);// 点击取消就停止下载.
                fos.close();
                is.close();
                return path;
            } catch (Exception e) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
                e.printStackTrace();
                return null;
            }
        }

        return path;
    }

    private void writeAdverLogoItems(Object writeobj) {
        String cacheDirPath = DownloadUtil.getInstance().getFILEPATH();
        if (!TextUtils.isEmpty(cacheDirPath)) {
            String tmpfilename = cacheDirPath + "/" + SplashHandler.filejson_name;
            FileOutputStream fos = null;
            JsonObjectWriter writer = null;
            try {
                fos = new FileOutputStream(tmpfilename);
                writer = new JsonObjectWriter(fos);
                writer.writeObject(writeobj);
            } catch (FileNotFoundException e) {
                LogBlue.e(getClass().getSimpleName(), "FileNotFoundException, 广告tmpfilename = " + tmpfilename, e);
                return;
            } catch (Exception e) {
                LogBlue.e(getClass().getSimpleName(), "write file error, 广告tmpfilename = " + tmpfilename, e);
                return;
            } finally {
                if (writer != null) {
                    writer.close();
                }
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                    }
                }
            }

        }
    }
}
