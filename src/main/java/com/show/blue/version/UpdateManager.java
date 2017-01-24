//package com.show.blue.version;
//
//import android.app.Activity;
//import android.app.Dialog;
//import android.app.Notification;
//import android.app.NotificationManager;
//import android.app.PendingIntent;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.graphics.Color;
//import android.net.Uri;
//import android.os.Handler;
//import android.os.Looper;
//import android.os.Message;
//import android.view.KeyEvent;
//import android.view.View;
//import android.widget.Button;
//import android.widget.LinearLayout;
//import android.widget.ProgressBar;
//import android.widget.RemoteViews;
//import android.widget.TextView;
//
//import com.show.blue.R;
//import com.show.blue.utils.APPData;
//import com.support.loader.ServiceLoader;
//import com.support.loader.packet.LoaderType;
//import com.support.loader.utils.AppManager;
//import com.support.loader.utils.DownloadUtil;
//import com.support.loader.utils.OnTimeCheck;
//import com.support.loader.utils.StringUtils;
//
//import java.io.File;
//
///**
// * 更新管理
// *
// * @author lzx
// */
//public class UpdateManager implements View.OnClickListener {
//
//    public final static int UPDATE_FAIL = 0x1001;
//    public final static int UPDATE_SUCCESS = 0x1002;
//    public final static int UPDATE_INIT = 0x1003;
//    public final static int UPDATE_LENAGTH = 0x1004;
//    public final static int UPDATE_INSTALL = 0x1005;
//    public final static int UPDATE_DOWN = 0x1006;
//
//    // private Activity mContext;
//    /* 进度条与通知ui刷新的handler和msg常量 */
//    NotificationManager mNotificationManager;
//    Notification mNotification;
//    private int progress;
//    View view_0;
//    String apkname = "bear.apk";
//    MyReceiver myReceiver;
//    public static boolean isrun = false;
//    Activity activity;
//    int compel = 0;
//    String versionName = "升级通知";
//    String conString = "升级通知";
//    boolean ispack = false;
//    Dialog dialog;
//    Button bt_ok, bt_cancel;
//    TextView messages, tv_progress;
//    ProgressBar update_progress;
//    TextView tv_title;
//    LinearLayout lin_progress;
//    Handler handler = new Handler(Looper.getMainLooper()) {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case UpdateManager.UPDATE_FAIL: {
//                    update_fail();
//                }
//                break;
//                case UpdateManager.UPDATE_INSTALL: {
//                    if (UpdateManager.isrun) {
//                        UpdateManager.isrun = false;
//                        installApk();
//                    }
//                }
//                break;
//                case UpdateManager.UPDATE_DOWN:
//
//                    update_down(msg.arg1);
//
//                    break;
//                case UpdateManager.UPDATE_SUCCESS:
//
//                    showupdate((Version) msg.obj);
//                    break;
//                case UpdateManager.UPDATE_LENAGTH: {
//
//                    update_lenagth((Double) msg.obj);
//
//                }
//                break;
//            }
//        }
//    };
//
//    double MB = 0.0;
//    long keytime = 0;
//
//    public UpdateManager(Activity activity, String apkname) {
//        this.apkname = apkname;
//        this.activity = activity;
//    }
//
//    public void update_lenagth(double mb) {
//        MB = mb;
//        if (dialog != null && dialog.isShowing()) {
//            tv_progress.setText("升级进度    0%" + StringUtils.getString(MB, 1) + "MB");
//        } else {
//            mNotification.contentView.setProgressBar(R.id.update_progress,
//                    100, progress, false);
//            mNotification.contentView.setTextViewText(R.id.notify_pro,
//                    progress + "%");
//            mNotificationManager.notify(0, mNotification);
//        }
//
//
//    }
//
//    public void update_fail() {
//        progress = 0;
//        isrun = false;
//        if (dialog != null && dialog.isShowing()) {
//            ShowProgressBar.showTitleDialog(activity, "更新失败",
//                    "ok", new ShowProgressBar.DialogHandler() {
//
//                        @Override
//                        public void confirmButton(Dialog dialog,
//                                                  Object object) {
//                            // TODO Auto-generated method stub
//                            if (compel == 0) {
//                                if (dialog != null && dialog.isShowing()) {
//                                    dialog.cancel();
//                                }
//                            } else {
//                                AppManager.getAppManager().AppExit(activity);
//                            }
//
//                        }
//
//                        @Override
//                        public void cancelButton(Dialog dialog,
//                                                 Object object) {
//                            // TODO Auto-generated method stub
//
//                        }
//                    }, null);
//        } else {
//            if (mNotification != null) {
//                mNotification.contentView.setViewVisibility(
//                        R.id.update_progress, View.GONE);
//                mNotification.contentView.setTextViewText(R.id.notify_pro,
//                        progress + "%" + "更新失败");
//                mNotificationManager.notify(0, mNotification);
//            }
//        }
//    }
//
//    public void update_down(int pro) {
//        isrun = true;
//        this.progress = pro;
//
//        if (dialog != null && dialog.isShowing()) {
//            if (progress <= 100) {
//
//                update_progress.setProgress(progress);
//            } else {
//                progress = 100;
//            }
//            tv_progress.setText("升级进度    " + progress + "%      " + StringUtils.getString((progress * MB / 100.0), 1) + "/" + StringUtils.getString(MB, 1) + "MB");
//
//        } else {
//            if (mNotification != null) {
//                mNotification.contentView.setProgressBar(R.id.update_progress,
//                        100, progress, false);
//                mNotification.contentView.setTextViewText(R.id.notify_pro,
//                        progress + "%");
//                mNotificationManager.notify(0, mNotification);
//            }
//
//        }
//    }
//
//    public void showupdate(Version update) {
//        compel = update.getCompel();
//        versionName = update.getVersionName();
//        conString = update.getMessage();
//
//
//        if (dialog == null) {
//            dialog = new Dialog(activity, R.style.theme_dialog_alert);
//
//        }
//        dialog.getWindow().setLayout(
//                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
//                android.view.ViewGroup.LayoutParams.MATCH_PARENT);
//        dialog.setContentView(R.layout.update_dialog);
//        if (update.getCompel() == 0) {
//            dialog.setCancelable(true);
//        } else {
//            dialog.setCancelable(false);
//        }
//        view_0 = dialog.findViewById(R.id.view_0);
//        tv_title = (TextView) dialog.findViewById(R.id.tv_title);
//        tv_progress = (TextView) dialog.findViewById(R.id.tv_progress);
//        lin_progress = (LinearLayout) dialog.findViewById(R.id.lin_progress);
//        bt_cancel = (Button) dialog.findViewById(R.id.bt_cancel);
//        if (compel == 1) {
//            bt_cancel.setText("退出");
//            tv_title.setText("版本太久,更新后才能使用");
//            tv_title.setTextColor(Color.RED);
//        } else {
//            tv_title.setText("发现新版本V" + versionName + ",是否更新?");
//        }
//        messages = (TextView) dialog.findViewById(R.id.messages);
//        update_progress = (ProgressBar) dialog.findViewById(R.id.update_progress);
//
//
//        bt_ok = (Button) dialog.findViewById(R.id.bt_ok);
//        bt_ok.setOnClickListener(this);
//        bt_cancel.setOnClickListener(this);
//        messages.setText(StringUtils.ToDBC(conString));
//        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
//                if (keyEvent.getKeyCode() == KeyEvent.KEYCODE_BACK) {// 后退
//                    if (compel == 0) {
//                        if (dialog != null && dialog.isShowing()) {
//                            dialog.cancel();
//                        }
//                    } else {
//                        if (isrun) {
//                            if (System.currentTimeMillis() - keytime > 2000) {
//                                keytime = System.currentTimeMillis();
//                                ShowToast.showTips(activity, "再按一次退出");
//                            } else {
//
//                                AppManager.getAppManager().AppExit(
//                                        activity);
//                            }
//                        } else {
//                            AppManager.getAppManager().AppExit(activity);
//                        }
//
//                    }
//                }
//                return false;
//            }
//        });
//        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialogInterface) {
//                if (myReceiver != null) {
//                    activity.unregisterReceiver(myReceiver);
//                }
//                if (mNotificationManager != null) {
//                    mNotificationManager.cancel(0);
//                }
//            }
//        });
//        try {
//            dialog.show();
//        } catch (Exception e) {
//            // TODO: handle exception
//        }
//    }
//
//    public void checkUpdate() {
//        Object o = ServiceLoader.getInstance().getListenerLoader(LoaderType.LOAD_UPDATE);
//        if (o == null) {
//            ServiceLoader.getInstance().setListenerLoader(LoaderType.LOAD_UPDATE, new UpdateListener() {
//                @Override
//                public void update(Version version) {
//                    VersionHandler(version);
//                }
//            });
//        } else if (o instanceof Version) {
//            Version version = (Version) o;
//            VersionHandler(version);
//        }
//    }
//
//    public static void updateAPK(Context context) {
//        UpdatePacket packet = new UpdatePacket(context);
//        ServiceLoader.getInstance().sendPacket(packet);
//    }
//
//    void VersionHandler(Version version) {
//        if (version == null) {
//            return;
//        }
//        String conString = "";
//        for (int i = 0; i < version.getContent().length; i++) {
//            if (!conString.equals("")) {
//                conString = conString + "\n";
//            }
//            conString = conString + (i + 1)
//                    + "."
//                    + version.getContent()[i];
//        }
//        version.setMessage(conString);
//        Message msg = handler.obtainMessage();
//        msg.what = UpdateManager.UPDATE_SUCCESS;
//        msg.obj = version;
//        handler.sendMessage(msg);
//    }
//
//    @Override
//    public void onClick(View arg0) {
//        // TODO Auto-generated method stub
//        if (!OnTimeCheck.onclick(arg0)) {
//            return;
//        }
//        if (arg0.getId() == R.id.bt_ok) {
//            if (!isrun) {
//
//                if (compel == 0) {
//                    if (dialog != null && dialog.isShowing()) {
//                        dialog.cancel();
//                    }
//                    showDownloadDialog(R.drawable.ic_launcher,
//                            APPData.url_apk);
//                } else {
//                    if (ispack) {
//                        installApk();
//                    } else {
//                        isrun = true;
//                        if (dialog != null) {
//                            bt_ok.setText("安装");
//                            tv_title.setVisibility(View.GONE);
//                            view_0.setVisibility(View.GONE);
//                            messages.setVisibility(View.GONE);
//                            lin_progress.setVisibility(View.VISIBLE);
//                        }
//
//                        ServiceLoader.getInstance().submit(
//                                new ApkRunnable(APPData.url_apk, handler,
//                                        APPData.apkname));
//                    }
//
//                }
//            }
//
//        } else if (arg0.getId() == R.id.bt_cancel) {
//            if (compel == 0) {
//                if (dialog != null) {
//                    dialog.cancel();
//                }
//            } else {
//                AppManager.getAppManager().AppExit(activity);
//            }
//        }
//
//    }
//
//    class MyReceiver extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            // TODO Auto-generated method stub
//            if (intent.getAction().equals("intent.APK")) {
//                if (progress >= 100) {
//                    isrun = false;
//                    installApk();
//                    mNotificationManager.cancel(0);
//                }
//            }
//        }
//
//    }
//
//    /**
//     * 创建通知
//     */
//    private void setMsgNotification(int icon) {
//        progress = 0;
//        CharSequence tickerText = "";
//        long when = System.currentTimeMillis();
//        mNotification = new Notification(icon, tickerText, when);
//        mNotification.flags = Notification.FLAG_ONGOING_EVENT;
//        RemoteViews contentView = new RemoteViews(
//                activity.getPackageName(), R.layout.notify_view);
//        contentView.setImageViewResource(R.id.notify_imageLog, icon);
//        contentView.setTextViewText(R.id.notify_pro, "0%");
//        contentView.setProgressBar(R.id.update_progress, 100, 0, false);
//        mNotification.contentView = contentView;
//        mNotification.tickerText = "开始下载新版";
//        Intent intent = new Intent("intent.APK");
//        PendingIntent contentIntent = PendingIntent.getBroadcast(
//                activity, 0, intent, 0);
//        mNotification.contentIntent = contentIntent;
//        mNotificationManager = (NotificationManager) activity
//                .getSystemService(Context.NOTIFICATION_SERVICE);
//        mNotificationManager.notify(0, mNotification);
////        mNotificationManager.
//    }
//
//    public void showDownloadDialog(int icon, String apkurl) {
//        IntentFilter filter = new IntentFilter();
//        filter.addAction("intent.APK");
//        filter.setPriority(1100);
//        myReceiver = new MyReceiver();
//        activity.registerReceiver(myReceiver, filter);
//
//        setMsgNotification(icon);
//        ServiceLoader.getInstance().submit(
//                new ApkRunnable(apkurl, handler, apkname));
//
//    }
//
//    /**
//     * 安装apk
//     */
//    public void installApk() {
//        File apkfile = new File(DownloadUtil.getInstance().getFILEPATH() + "/" + apkname);
//        if (!apkfile.exists()) {
//            return;
//        }
//        Intent i = new Intent(Intent.ACTION_VIEW);
//        i.setDataAndType(Uri.parse("file://" + apkfile.toString()),
//                "application/vnd.android.package-archive");
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        activity.startActivity(i);
//
//        try {
//            if (myReceiver != null) {
//                activity.unregisterReceiver(myReceiver);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//
//        }
//        if (mNotificationManager != null) {
//            mNotificationManager.cancel(0);
//        }
//
//    }
//}
