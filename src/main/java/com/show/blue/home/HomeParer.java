package com.show.blue.home;

import android.content.Context;

import com.android.json.stream.JsonObjectReader;
import com.android.json.stream.UniformErrorException;
import com.show.blue.utils.JsonBaseParser;
import com.support.loader.ServiceLoader;
import com.support.loader.packet.LoaderType;

import java.io.IOException;

/**
 * Created by liangzhenxiong on 16/4/4.
 */
public class HomeParer implements JsonBaseParser {

    Context context;

    public HomeParer(Context context) {
        this.context = context;
    }

    @Override
    public void doparser(JsonObjectReader jsonObjectReader, String string) {
        MainItem mainItem = new MainItem();
        try {
            if (jsonObjectReader != null) {
                jsonObjectReader.readObject(mainItem);
            }
            Object o = ServiceLoader.getInstance().getListenerLoader(LoaderType.LOAD_HOMEDATA);
            if (o == null) {
                ServiceLoader.getInstance().setListenerLoader(LoaderType.LOAD_HOMEDATA, mainItem);
            } else if (o instanceof HomeDataListener) {
                HomeDataListener updateListener = (HomeDataListener) o;
                updateListener.update(mainItem);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (UniformErrorException e) {
            e.printStackTrace();
        }
    }
}
