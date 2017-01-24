package com.example.gaussianblur.blur;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.library.uiframe.BaseActivity;
import com.show.blue.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liangzhenxiong on 16/3/14.
 */
public class BlurActivity extends BaseActivity {
    ListView list_view;
    BlurAdapter adapter;
    List<BlurItem> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blur_layout);
        list_view = (ListView) findViewById(R.id.list_view);
        list = new ArrayList<BlurItem>();
        ArrayList<String> strings0 = new ArrayList<String>();
        strings0.add("http://i3.res.meizu.com/fileserver/app_ad_snap/187/3a3c5a01db924437be3226e491e76fa7.jpg");
        strings0.add("http://i3.res.meizu.com/fileserver/app_ad_snap/187/e2430b0f6bba4a2ab936b11a266cd483.jpg");
        strings0.add("http://i3.res.meizu.com/fileserver/app_ad_snap/187/50fb38167546490295f6acbe8fc298bf.jpg");
        strings0.add("http://i3.res.meizu.com/fileserver/app_ad_snap/187/69383a7825f84f3884b0675498b889c5.jpg");
        strings0.add("http://i3.res.meizu.com/fileserver/app_ad_snap/187/a64374204a3d4811bc5ec206f473b00d.jpg");
        list.add(getBlurItem("美颜相机", "http://i3.res.meizu.com/fileserver/app_icon/6025/998df05eed25439aa1a04b4e1d184d61.png",strings0));

        ArrayList<String> strings1 = new ArrayList<String>();
        strings1.add("http://i3.res.meizu.com/fileserver/app_ad_snap/187/5181b64ce054452da2e823374b25ada0.jpg");
        strings1.add("http://i3.res.meizu.com/fileserver/app_ad_snap/187/96789dd54c21448aa4bf3d1f5f3500cc.jpg");
        strings1.add("http://i3.res.meizu.com/fileserver/app_ad_snap/187/3ea05101571c484a8256bdd09c9ed20f.jpg");
        strings1.add("http://i3.res.meizu.com/fileserver/app_ad_snap/187/09bdd4951c6e46be848789efba7bb1f7.jpg");
        list.add(getBlurItem("淘宝", "http://i3.res.meizu.com/fileserver/app_icon/6077/26a60f1adb784428a3e724e8b7074cc0.png",strings1));
        ArrayList<String> strings2 = new ArrayList<String>();
        strings2.add("http://i1.res.meizu.com/fileserver/app_ad_snap/187/0b0729fb1541426695c819a4c91deb14.jpeg");
        strings2.add("http://i1.res.meizu.com/fileserver/app_ad_snap/187/7bb4eab7ad474a9ea47315639b3ecb5e.jpeg");
        strings2.add("http://i1.res.meizu.com/fileserver/app_ad_snap/187/c9d7919702a14a5785e4e9018c01b56b.jpeg");
        strings2.add("http://i1.res.meizu.com/fileserver/app_ad_snap/187/d6f04fd81db54deb9f76e881c5b6473c.jpeg");
        list.add(getBlurItem("花椒", "http://i3.res.meizu.com/fileserver/app_icon_origin/803/fce38feb0b2d4daf87c9a92e63b039b8.png",strings2));
        ArrayList<String> strings3 = new ArrayList<String>();
        strings3.add("http://i3.res.meizu.com/fileserver/app_ad_snap/187/597a3673071043cd811a4903a2d81191.jpg");
        strings3.add("http://i3.res.meizu.com/fileserver/app_ad_snap/187/5fcbb7f7be794011ab23fd4e6ea3ee2e.jpg");
        strings3.add("http://i3.res.meizu.com/fileserver/app_ad_snap/187/09b626e4ea1e47e6b2bb38283c50b05e.jpg");
        strings3.add("http://i3.res.meizu.com/fileserver/app_ad_snap/187/593c0b48ae8749378056d14693b1b3fc.jpg");
        strings3.add("http://i3.res.meizu.com/fileserver/app_ad_snap/187/3c480dabc84d44d992173c4707ee95a1.jpg");
        list.add(getBlurItem("霸道天下", "http://i1.res.meizu.com/fileserver/app_icon/6104/c5ec7d8e1eb64e9c80543921857c9bae.png",strings3));

        list.add(getBlurItem("逐鹿天下", "http://i3.res.meizu.com/fileserver/app_icon/6066/f37feebee97043dba8024a1cdab47169.png",strings1));

        list.add(getBlurItem("学霸君", "http://i3.res.meizu.com/fileserver/app_icon/6089/d1b9f3673eb946fe88192411ca13d345.png",strings1));

        list.add(getBlurItem("美团", "http://i3.res.meizu.com/fileserver/app_icon/6128/7d1f42257a714eadb179e7f5fa2f8a57.png",strings1));

        list.add(getBlurItem("吉美团购", "http://img.wdjimg.com/mms/icon/v1/e/1e/be0315619315ef8105da7473c57941ee_256_256.png",strings1));
        list.add(getBlurItem("京东", "http://i3.res.meizu.com/fileserver/app_icon/6089/272f85aea9534b2d88e5d60b54b0157a.png",strings1));
        list.add(getBlurItem("微博", "http://i3.res.meizu.com/fileserver/app_icon/6130/5e57a3f1056c4c9181e167791bbd9b0d.png",strings1));
        list.add(getBlurItem("QQ", "http://i1.res.meizu.com/fileserver/app_icon/6060/184aed415f6343dba060f2fc1a5cb59d.png",strings1));
        list.add(getBlurItem("微信", "http://i2.res.meizu.com/fileserver/app_icon/6054/95fb44e40b374d049a797443c19fa4b5.png",strings1));
        list.add(getBlurItem("QQ音乐", "http://i2.res.meizu.com/fileserver/app_icon/6130/d90f593c1a304e45b9ae047ee9b30a37.png",strings1));
//        list.add(getBlurItem("",""));
        adapter = new BlurAdapter(this, list);
        list_view.setAdapter(adapter);
        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                BlurItem blurItem = (BlurItem) parent.getAdapter().getItem(position);
                intent.putExtra("name", blurItem.name);
                intent.putExtra("url", blurItem.url);
                intent.putExtra("urls", blurItem.strings);
                intent.setClass(BlurActivity.this, FastBlurActivity.class);
                startActivity(intent);
            }
        });
    }

    BlurItem getBlurItem(String name, String url, ArrayList<String> urls) {
        BlurItem blurItem = new BlurItem();
        blurItem.name = name;
        blurItem.url = url;
        blurItem.strings = urls;
        return blurItem;
    }
}
