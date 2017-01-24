package com.show.blue.view;

import android.os.Bundle;
import android.os.Message;

import com.library.uiframe.BaseActivity;
import com.show.blue.R;

/**
 * Created by liangzhenxiong on 16/3/24.
 */
public class PlayActivity extends BaseActivity {
    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_layout);
    }
}
