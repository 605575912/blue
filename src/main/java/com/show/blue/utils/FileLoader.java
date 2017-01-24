package com.show.blue.utils;

import android.content.Context;
import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by liangzhenxiong on 16/3/7.
 */
public class FileLoader extends Loader {
    public FileLoader(Context context) {
        super(context);
    }

    @Override
    public void seturl(String url) {

    }

    @Override
    public void settype(String type) {

    }

    @Override
    public void loader(String filePath, JsonBaseParser parser) {

        if (parser == null || context == null) {
            return;
        }
        try {

            if (TextUtils.isEmpty(filePath)) {
                return;
            }
            File file = new File(filePath);
            if (!file.exists()) {
                parser.doparser(null, "");
                return;
//                throw new FileNotFoundException();
            }
            InputStream  inputStream = new FileInputStream(file);
            parser.doparser(getJsonReader(inputStream), "");

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void loader(JsonBaseParser parser) {

    }
}
