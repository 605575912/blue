package com.show.blue.icon;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.library.uiframe.BaseActivity;
import com.show.blue.R;
import com.support.loader.ServiceLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.zip.ZipFile;

/**
 * Created by liangzhenxiong on 16/3/20.
 */
public class FishActivity extends BaseActivity {
    TextView tv_;

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (msg.what == 0) {
            tv_.setText((String) msg.obj);
//            ShowToast.showTips(getApplicationContext(), (String) msg.obj);
        }
        if (msg.what == 1) {
            Toast.makeText(getApplicationContext(), "copy 完成", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(1);
                    }
                });
                getWindow().getDecorView().removeCallbacks(this);
            }
        });
//        try {
//            Thread.sleep(4000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        try {
//            Thread.sleep(4000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fish_layout);
        Button button = (Button) findViewById(R.id.button);
        Button button1 = (Button) findViewById(R.id.button1);
        tv_ = (TextView) findViewById(R.id.tv_);
        TextView textView = (TextView) findViewById(R.id.textView);
        String path = Environment.getExternalStorageDirectory()
                .getAbsolutePath();// 存放照片的文件夹
        path = path + "/";
        final File file = new File(path + "Blue.apk");
//        textView.setText("被注释的APK");
//        button.setVisibility(View.GONE);
        if (file != null) {
            if (file.exists()) {
                textView.setText("存在要注释的APK");
            }
        }
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServiceLoader.getInstance().submit(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = handler.obtainMessage(1);
//                        if (Build.VERSION.SDK_INT >= 19) {
//                            try {
//                                Startplay startplay = new Startplay(getApplicationContext());
//                                startplay.play(getApplicationContext());
//                            writeApk(file, "这是注释入APK的内容");
                        try {
                            PackerNg.Helper.writeMarket(file, "这是注释入APK的内容");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
//                        } else {
//                            msg.what = 0;
//                            msg.obj = "注释APK需要 API19";
//                        }
                        handler.sendMessage(msg);
                    }
                });
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
//                        String comment = readApk(file);
//                        if (TextUtils.isEmpty(comment)) {
//                            comment = "读取不到APK 注释内容";
//                        }
                        String comment = null;
//                        T.getstant(FishActivity.this);
//                        try {
                             comment = PackerNg.getMarket(FishActivity.this.getApplicationContext());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
                        if (TextUtils.isEmpty(comment)) {
                            comment = "读取不到APK 注释内容";
                        }
//                        String comment = PackerNg.getMarket(this);
                        Message msg = handler.obtainMessage(0);
                        msg.obj = comment;
                        handler.sendMessage(msg);
//                        Log.i("TAG", "comment" + comment);
                    }
                }.start();
            }
        });
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Message msg = handler.obtainMessage(0);
                        msg.obj = "333";
                        handler.sendMessage(msg);
                    }
                });
                getWindow().getDecorView().removeCallbacks(this);
            }
        });
    }

    @SuppressLint("NewApi")
    public static void writeApk(File file, String comment) {
        ZipFile zipFile = null;
        ByteArrayOutputStream outputStream = null;
        RandomAccessFile accessFile = null;
        try {
            zipFile = new ZipFile(file);
            String zipComment = zipFile.getComment();
            if (zipComment != null) {
                return;
            }

            byte[] byteComment = comment.getBytes();
            outputStream = new ByteArrayOutputStream();

            outputStream.write(byteComment);
            outputStream.write(MCPTool.short2Stream((short) byteComment.length));

            byte[] data = outputStream.toByteArray();

            accessFile = new RandomAccessFile(file, "rw");
            accessFile.seek(file.length() - 2);
            accessFile.write(MCPTool.short2Stream((short) data.length));
            accessFile.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (zipFile != null) {
                    zipFile.close();
                }
                if (outputStream != null) {
                    outputStream.close();
                }
                if (accessFile != null) {
                    accessFile.close();
                }
            } catch (Exception e) {

            }

        }
    }

    public static String getPackagePath(Context context) {
        if (context != null) {
            return context.getPackageCodePath();
        }
        return null;
    }

    public static String readApk(File file) {
        byte[] bytes = null;
        try {
            RandomAccessFile accessFile = new RandomAccessFile(file, "r");
            long index = accessFile.length();

            bytes = new byte[2];
            index = index - bytes.length;
            accessFile.seek(index);
            accessFile.readFully(bytes);

            int contentLength = MCPTool.stream2Short(bytes, 0);

            bytes = new byte[contentLength];
            index = index - bytes.length;
            accessFile.seek(index);
            accessFile.readFully(bytes);

            return new String(bytes, "utf-8");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            AppManager.getAppManager().finishActivity(this);
//            return false;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
}
