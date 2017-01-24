//package com.show.blue.version;
//
//import android.app.Activity;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager.NameNotFoundException;
//import android.os.Handler;
//import android.os.Message;
//
//import com.support.loader.packet.TaskPacket;
//import com.support.loader.packet.TaskPacketType;
//
//import org.json.JSONArray;
//import org.json.JSONObject;
//
///**
// * 获取更新信息 2015-6-3 @author lzx
// */
//public class UpdatePacket extends TaskPacket {
//    Activity context;
//    Handler handler;
//
//    public UpdatePacket(Activity context, Handler handler) {
//        this.context = context;
//        this.handler = handler;
//        setPriority(TaskPacketType.Task_DATA);
//        setTaskId("update");
//    }
//
//    @Override
//    public void handle() {
//        // TODO Auto-generated method stub
//        PackageInfo info;
//        int versionCode = 1;
//        try {
//            info = context.getPackageManager().getPackageInfo(
//                    context.getPackageName(), 0);
//            versionCode = info.versionCode;
//        } catch (NameNotFoundException e1) {
//            // TODO Auto-generated catch block
//            e1.printStackTrace();
//        }
////        String text = HTTPUtil.getString(APPData.check_apk
////                + versionCode);
////            AssetsLoader       loader = new AssetsLoader(context);
//
////        loader.loader("json/version", new VersionParser(context));
//        String  text="{\"ret\":40001,\"msg\":\"\\u9700\\u8981\\u5347\\u7ea7\",\"datas\":{\"version\":{\"versionCode\":19,\"versionName\":\"3.4.2\"},\"compel\":1,\"content\":[\"\\u589e\\u52a0\\u4e86\\u6d3b\\u52a8\\u7248\\u5757\\uff0c\\u7cbe\\u5f69\\u6d3b\\u52a8\\u4e0d\\u65ad\",\"\\u52a9\\u5b66\\u8c46\\u7cfb\\u7edf\\u4e0a\\u7ebf\\uff0c\\u5feb\\u6765\\u9886\\u52a9\\u5b66\\u8c46\\u5151\\u8c6a\\u793c\\u5427\\uff01\",\"\\u589e\\u52a0\\u5fae\\u4fe1\\u652f\\u4ed8\\u529f\\u80fd\",\"\\u4f18\\u5316\\u90e8\\u5206UI\\u548c\\u4ea4\\u4e92\\u4f53\\u9a8c\\uff0c\\u4fee\\u590d\\u5df2\\u77e5BUG\"]}}";
//        if (text != null) {
//            try {
//                JSONObject jsonVersion = new JSONObject(text);
//                if (jsonVersion != null) {
//                    if (jsonVersion.has("datas")) {
//                        JSONObject datas = jsonVersion.getJSONObject("datas");
//                        if (datas.has("version")) {
//                            JSONObject version = datas.getJSONObject("version");
//                            if (version.has("versionCode")) {
//                                Update update = new Update();
//                                int code = version.getInt("versionCode");
//                                if (code > versionCode) {
//                                    String versionName = version
//                                            .getString("versionName");
//                                    update.setVersionName(versionName);
//                                    int compel = 0;
//                                    if (datas.has("compel")) {
////                                        compel = datas.getInt("compel");
////                                        compel =1;
//                                        update.setCompel(compel);
//
//                                    }
//                                    String conString = "";
//                                    if (datas.has("content")) {
//                                        JSONArray content = datas
//                                                .getJSONArray("content");
//                                        for (int i = 0; i < content.length(); i++) {
//                                            if (!conString.equals("")) {
//                                                conString = conString + "\n";
//                                            }
//                                            conString = conString + (i + 1)
//                                                    + "."
//                                                    + content.getString(i);
//                                        }
//                                        update.setMessage(conString);
//
//                                    }
//                                    if (handler != null) {
//                                        Message msg = handler.obtainMessage();
//                                        msg.what = UpdateManager.UPDATE_SUCCESS;
//                                        msg.obj = update;
//                                        handler.sendMessage(msg);
//                                        return;
//                                    }
//
//                                    return;
//
//                                } else if (code <= versionCode) {
//                                    if (handler != null) {
//                                        handler.sendEmptyMessage(UpdateManager.UPDATE_INIT);
//                                    }
//                                    return;
//                                }
//                            }
//                        }
//
//                    }
//
//                }
//            } catch (Exception e) {
//                // TODO: handle exception
//
//            }
//        } else {
//            if (handler != null) {
//                handler.sendEmptyMessage(UpdateManager.UPDATE_FAIL);
//            }
//            return;
//        }
//        if (handler != null) {
//            handler.sendEmptyMessage(UpdateManager.UPDATE_INIT);
//        }
//    }
//}
