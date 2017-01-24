package com.show.blue.utils;

import android.content.Context;
import android.content.res.AssetManager;

import java.io.IOException;
import java.io.InputStream;

/**
 * Created by liangzhenxiong on 16/3/7.
 */
public class AssetsLoader extends Loader {


    public AssetsLoader(Context context) {
        super(context);
    }

    @Override
    public void seturl(String url) {

    }

    @Override
    public void settype(String type) {

    }

    @Override
    public void loader(String url, JsonBaseParser parser) {
        if (parser == null || context == null) {
            return;
        }
        try {
            AssetManager assetManager = context.getAssets();
            if (assetManager == null) {
                return;
            }
            InputStream inputStream = assetManager.open(url);
            parser.doparser(getJsonReader(inputStream), "");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void loader(JsonBaseParser parser) {
        loader(url, parser);
    }
}
