//package com.show.blue;
//
//import org.apache.poi.xssf.usermodel.XSSFSheet;
//
//import java.util.ArrayList;
//
///**
// * Created by apple on 2016/12/12.
// */
//
//public class MainApk {
//    public void run() {
//        String unzipapk = "Blue_5.4.0.apk";//要解压的目录
//        String unzippath = "/Volumes/Android/MyApp/blue/";//要解压的目录
//
//
//        Apkrun apkrun = new Apkrun(unzippath);
//        String temapk = apkrun.unAPK(unzipapk);
//        Apkrun apkrun1 = new Apkrun(unzippath);
//        temapk = apkrun1.unAPK(temapk);
//        Apkrun apkrun2 = new Apkrun(unzippath);
//        temapk = apkrun2.unAPK(temapk);
//        apkrun.rename(unzippath + temapk, unzippath + unzipapk);
//
////        new Thread(apkrun).start();
//        new Thread(apkrun1).start();
//        new Thread(apkrun2).start();
//        ExcelUtil excelUtil = new ExcelUtil();
//        ArrayList<XSSFSheet> s = excelUtil.opnexlsx(unzippath + "2.xlsx");
//        ArrayList<Pack> objects = new ArrayList<Pack>();
//        for (int i = 0; i < s.size(); i++) {
//            objects.addAll(excelUtil.readExcel2007(s.get(i), i));
//        }
//
//        int size = objects.size();
//        for (int j = 0; j < size; ) {
//
//            apkrun1.sendpack(objects.get(j));
//            if (j + 1 < size) {
//
//                apkrun2.sendpack(objects.get(j + 1));
//            }
//            if (j + 2 < size) {
//
//                apkrun.sendpack(objects.get(j + 2));
//            }
//            j = j + 3;
//        }
//        apkrun1.sendpack(new Pack(null, null));
//        apkrun2.sendpack(new Pack(null, null));
//        apkrun.sendpack(new Pack(null, null));
//        apkrun.run();
//        while (!apkrun1.isexit) {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//        while (!apkrun2.isexit) {
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//}
