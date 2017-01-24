package com.show.blue.home;

import android.content.Context;

import com.show.blue.utils.AssetsLoader;
import com.support.loader.packet.TaskPacket;

/**
 * Created by liangzhenxiong on 16/4/4.
 */
public class HomeDatePacket extends TaskPacket {
    Context context;

    public HomeDatePacket(Context context) {
        this.context = context;
    }

    @Override
    public void handle() {
        super.handle();
        AssetsLoader loader = new AssetsLoader(context);
        loader.loader("json/home_list", new HomeParer(context));
    }
}
