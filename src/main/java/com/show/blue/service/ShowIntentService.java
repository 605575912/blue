package com.show.blue.service;

import android.content.Intent;

import com.show.blue.TestActivity;

/**
 * Created by apple on 16/6/30.
 */
public class ShowIntentService extends android.app.IntentService{
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public ShowIntentService() {
        super("ShowIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Intent intent1 = new Intent();
        intent1.setClass(getApplicationContext(), TestActivity.class);
        startActivity(intent1);
    }
}
