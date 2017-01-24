package com.example.gaussianblur.iconfont;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.show.blue.R;


/**
 * Created by liangzhenxiong on 16/3/9.
 */
public class IconfontActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.iconfont_layout);
        Typeface iconfont = Typeface.createFromAsset(getAssets(), "iconfont/iconfont.ttf");
        TextView textview = (TextView) findViewById(R.id.like);
        textview.setTypeface(iconfont);
        textview.setTextSize(40);
        textview.setTextColor(Color.BLUE);
        textview.setText(Html.fromHtml("&#x3605; &#13741; &#x35ae; &#x35af;"));
        TextView tv_ = (TextView) findViewById(R.id.tv_);
        tv_.setTypeface(iconfont);
        tv_.setTextSize(20);
        tv_.setText(Html.fromHtml("&#x35ae; &#x35af; &#x346e;"));
    }
}
