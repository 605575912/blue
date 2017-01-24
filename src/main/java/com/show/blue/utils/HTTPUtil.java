package com.show.blue.utils;

import com.support.loader.utils.HttpListener;
import com.support.loader.utils.UploadUtil;

public class HTTPUtil {
    HttpListener httpListener;
    static UploadUtil uploadUtil;

    static String getString(String urlString, String PHPSESSID) {
        if (uploadUtil == null) {
            uploadUtil = new UploadUtil();
        }
        if (PHPSESSID != null && !PHPSESSID.equals("")) {
            String relust = uploadUtil.getString(urlString, PHPSESSID);
            if (relust != null) {
//                try {
////                    JSONObject jsonObject = new JSONObject(relust);
////                    boolean unlogin = getsessionId(jsonObject);
////                    if (unlogin) {
////                        // relust = getString(urlString, true);
////                    }
//                } catch (JSONException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
            }
            return relust;
        } else {
            String relust = uploadUtil.getString(urlString, "");
            return relust;
        }

    }

    public static String getString(String urlString) {
        return getString(urlString, "");
    }

//    public static String logingetString(String urlString) {
//        return getString(urlString, XswApplication.getInstance().getSessionId());
//    }
//
//    public static String logingetString(String urlString, String PHPSESSID) {
//        return getString(urlString, PHPSESSID);
//    }

//    public static String postString(String urlString, String params) {
//        return postString(urlString, params, false);
//    }
//
//    public static String loginPostString(String urlString, String params) {
//        return postString(urlString, params, true);
//    }

//    static boolean getsessionId(JSONObject jsonObject) {
//        if (jsonObject != null) {
//            if (jsonObject.has("ret")) {
//                int ret;
//                try {
//                    ret = jsonObject.getInt("ret");
//                    if (ret == 20001) {
//                        XswApplication.getInstance().setSessionId("");
//                        if (HXSDKHelper.getInstance().isLogined()) {
//                            EMChatManager.getInstance().logout();
//                        }
//
//                        Intent intent = new Intent();
//                        intent.setAction(Action.APP_LOGIN);
//                        intent.addCategory("com.xsw.student");
//                        Bundle bundle = new Bundle();
//                        intent.putExtras(bundle);
//                        XswApplication.app.sendBroadcast(intent);
//                    }
//                } catch (JSONException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//
//            }
//        }
//        return false;
//    }

//    static String postString(String urlString, String params, boolean needlogin) {
//        if (uploadUtil == null) {
//            uploadUtil = new UploadUtil();
//        }
//
//        if (needlogin) {
////            String relust = uploadUtil.postString(urlString, params, false,
////                    XswApplication.getInstance().getSessionId(), null);
////            if (relust != null) {
////                try {
////                    JSONObject jsonObject = new JSONObject(relust);
////                    boolean unlogin = getsessionId(jsonObject);
////                    if (unlogin) {
////                        // relust = postString(urlString, params, true);
////                    }
////                } catch (JSONException e) {
////                    // TODO Auto-generated catch block
////                    e.printStackTrace();
////                }
////            }
//            return relust;
//        } else {
//            String relust = uploadUtil.postString(urlString, params, false, "",
//                    null);
//            return relust;
//        }
//
//    }
}
