package com.show.blue.utils;


public interface APPData {
    String SYS_XSW = "XSWstudent";
    String sessionId = "sessionId";
    String USERNAME = "username";
    String USERPSW = "userpsw";
    String uid = "uid";
    String realname = "realname";
    String sex = "sex";
    String birthday = "birthday";
    String addr = "addr";
    String hobby = "hobby";
    String intro_short = "intro_short";
    String face = "face";
    String update_time = "update_time";
    String school = "school";
    String grade = "grade";
    String grade_sub = "grade_sub";
    String course_adept = "course_adept";
    String versionCode = "versionCode";
    String course_update_at = "course_update_at";
    String push_swith = "push_swith";

    String nonPayment = "nonPayment";
    String order_phone = "order_phone";
    String order_name = "order_name";
    String order_adress = "order_adress";
    String apkname = "xsw.apk";
    String Registration_Id = "Registration_Id";
    String push_message = "push_message";
    String push_time = "push_time";

    String send_name = "send_name";//发送者的名字
    String send_image = "send_image";//发送者的头像URL
    String send_phone = "send_phone"; //发送者的手机号码
    String receive_image = "receive_image";//接受者的头像URL
    String receive_name = "receive_name";//接受者的名字
    String receive_phone = "receive_phone";//接受者的手机号码

    String city_name = "city_name";
    String city_id = "city_id";
    String city_ischoose = "city_ischoose"; //定位选择 的城市

    public static String V2_URL_HOST = "http://v3.api.51xuanshi.com";
    String heard_image = "@200w_200h";
    String browse_image = "@200w_200h";
    String url_apk = "http://115.231.191.147/dd.myapp.com/16891/2B7E9781A7EA701AF79C2813F75FA649.apk?mkey=5701c96b623fc28f&f=8a5d&fsname=com.tencent.mobileqq_6.3.0_348.apk&p=.apk";
    String check_apk = V2_URL_HOST
            + "/app/UpgradeTest?type=0&user_type=3&version_code=";
//	final String url_apk = XswApplication.V2_URL_HOST
//			+ "/app/GetAndroidAPK?type=3&test=1";


}
