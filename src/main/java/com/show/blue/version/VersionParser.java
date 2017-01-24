package com.show.blue.version;

import android.content.Context;

import com.android.json.stream.JsonObjectReader;
import com.android.json.stream.UniformErrorException;
import com.show.blue.utils.JsonBaseParser;
import com.support.loader.ServiceLoader;
import com.support.loader.packet.LoaderType;

import java.io.IOException;

/**
 * Created by liangzhenxiong on 16/3/7.
 */
public class VersionParser implements JsonBaseParser {
    Context context;

    public VersionParser(Context context) {
        this.context = context;
    }

    @Override
    public void doparser(JsonObjectReader jsonObjectReader, String string) {
        Version version = new Version();
        try {
            if (jsonObjectReader != null) {
                jsonObjectReader.readObject(version);
            }
            Object o = ServiceLoader.getInstance().getListenerLoader(LoaderType.LOAD_UPDATE);
            if (o == null) {
                ServiceLoader.getInstance().setListenerLoader(LoaderType.LOAD_UPDATE, version);
            } else if (o instanceof UpdateListener) {
                UpdateListener updateListener = (UpdateListener) o;
                updateListener.update(version);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (UniformErrorException e) {
            e.printStackTrace();
        }
    }
}
