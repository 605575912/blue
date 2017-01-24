package com.show.blue.utils;

import android.content.Context;

import com.android.json.stream.JsonObjectReader;
import com.android.json.stream.JsonReader;
import com.android.json.stream.JsonUniformErrorObjectReader;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

/**
 * Created by liangzhenxiong on 16/3/7.
 */
public class Loader {
    String url = "";

    String type = "";
    Context context;

    public Loader(Context context) {
        this.context = context;
    }

    void seturl(String url) {

    }


    void settype(String type) {

    }


    public void loader(String url, JsonBaseParser parser) {

    }


    void loader(JsonBaseParser parser) {
        loader(url, parser);

    }

    JsonObjectReader getJsonReader(InputStream inputStream) throws UnsupportedEncodingException {
        JsonReader jsonReader = new JsonReader(new InputStreamReader(inputStream, "UTF-8"));
        jsonReader.setLenient(true);
//            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream,
//                    "UTF-8"));
//            StringBuilder stringBuilder = new StringBuilder();
//            String str;
//            while ((str = br.readLine()) != null) {
//                stringBuilder.append(str);
//            }
        JsonUniformErrorObjectReader jor = null;
        if (jsonReader != null) {
            jor = new JsonUniformErrorObjectReader(jsonReader);
            return jor;
        }
        return null;
    }

}
