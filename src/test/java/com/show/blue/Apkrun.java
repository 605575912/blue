package com.show.blue;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Created by apple on 2016/12/10.
 */

public class Apkrun implements Runnable {
    private final BlockingQueue<Pack> queue = new ArrayBlockingQueue<Pack>(
            300, true);
    boolean isexit = false;
    String apkname;
    String MM_online_channel_ = "MM_online_channel_";
    String unzipfile;
    String unzippath;//项目路径
    SimpleDateFormat simpleFormatter = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    final String spl = "/";
    String keystorepath = "/Users/apple/Downloads/aspire_android_key.keystore";
    String assettxt = spl + "assets" + spl + "mmconfig.ini";
    String config;
    String dist = spl + "dist" + spl;

    public Apkrun(String unzippath) {
        this.unzippath = unzippath;
    }

    private void unzip(String cmd) {
        try {
            Process pro = Runtime.getRuntime().exec(cmd);
            InputStream in = null;
            pro.waitFor();
            in = pro.getInputStream();
            BufferedReader read = new BufferedReader(new InputStreamReader(in));
            String result = read.readLine();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendpack(Pack pack) {
        queue.offer(pack);
        if (queue.size() > 1) {

        } else {

            synchronized (queue) {
                queue.notifyAll();
            }
        }
    }

    /**
     * @return packet
     */
    private Pack nextPacket(BlockingQueue<Pack> taskPackets) {
        Pack packet = null;
        while ((packet = taskPackets.poll()) == null) {

            try {
                synchronized (taskPackets) {
                    taskPackets.wait();
                }
            } catch (InterruptedException ie) {
                // Do nothing
            }
        }
        return packet;
    }

    @Override
    public void run() {
        while (!isexit) {
            final Pack packet = nextPacket(queue);
            if (packet != null) {
                if (packet.id == null) {
                    isexit = true;
                } else {
                    apk(packet.id, packet.name);
                }
            }
        }
    }

    private void write(String path, String config, String channelid) {
        FileOutputStream fos = null;
        try {
            File file = new File(path);
            fos = new FileOutputStream(file);
            int start = config.indexOf("<channel_id>");
            int end = config.lastIndexOf("</channel_id>");
            String temps = config.substring(start, end);
            config = config.replace(temps, "<channel_id>" + channelid);
            int sdkstart = config.indexOf("<sdk_channel_id>");
            int sdkend = config.lastIndexOf("</sdk_channel_id>");
            temps = config.substring(sdkstart, sdkend);
            config = config.replace(temps, "<sdk_channel_id>" + channelid);
            byte[] temp = config.getBytes();
            fos.write(temp);
            fos.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String read(String path) {
        FileInputStream fileInputStream = null;
        BufferedReader br = null;
        try {
            File file = new File(path);
            if (!file.exists()) {
                return null;
            }
            fileInputStream = new FileInputStream(file);
            br = new BufferedReader(new InputStreamReader(fileInputStream,
                    "UTF-8"));
            String str = null;
            StringBuilder stringBuilder = new StringBuilder();
            while ((str = br.readLine()) != null) {
                stringBuilder.append(str);
            }
            String string = stringBuilder.toString();
            return string;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();

            }
        }
        return null;
    }

    public void rename(String path, String newname) {
        File file = new File(path);
        File file1 = new File(newname);
        file.renameTo(file1);
    }


    String unAPK(String apkpath) {
        this.apkname = apkpath;
        unzipfile = apkpath.substring(0, apkpath.lastIndexOf(".apk"));//要解压的文件夹名字
        System.out.println("==" + unzippath + apkpath);
        unzip("apktool d -f " + unzippath + apkpath);
        String newpak = unzipfile + "_.apk";
        rename(unzippath + apkpath, unzippath + newpak);
        return newpak;
    }

    private void apk(String id, String name) {
        if (config == null) {
            config = read(unzippath + unzipfile + assettxt);
        }
        if (config != null && config.length() > 1) {
            System.out.print(unzippath + unzipfile + assettxt + "\n");
            write(unzippath + unzipfile + assettxt, config, id);
            unzip("apktool b " + unzippath + unzipfile);
            String newname = MM_online_channel_ + id + ".apk";
            String oupfile = unzippath + unzipfile + dist;
            rename(oupfile + apkname, oupfile + newname);
            File file = new File(unzippath + "outout_" + name);
            if (!file.exists()) {
                file.mkdir();
            }
            String signerapk = file.getPath() + spl + newname;
            String unsignerapk = oupfile + newname;
            unzip("jarsigner -keystore " + keystorepath + " -storepass aspire -signedjar " + signerapk + " " + unsignerapk + " aspire");
        } else {
            config = "not";
        }
        System.out.print(simpleFormatter.format(new Date()) + "完成");
    }

}
