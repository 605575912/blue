package com.show.blue.version;

import android.content.Context;

import com.show.blue.utils.AssetsLoader;
import com.support.loader.packet.TaskPacket;

/**
 * Created by liangzhenxiong on 16/3/7.
 */
public class UpdatePacket extends TaskPacket {
    Context context;

    public UpdatePacket(Context context) {
        this.context = context;
    }

    @Override
    public void handle() {
        super.handle();
//        try {
//            Thread.sleep(8000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        AssetsLoader loader = new AssetsLoader(context);
        loader.loader("json/version", new VersionParser(context));


    }
}
