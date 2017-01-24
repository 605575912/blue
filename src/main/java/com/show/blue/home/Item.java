package com.show.blue.home;

/**
 * Created by liangzhenxiong on 16/4/4.
 */
public class Item {
    String name;
    int type = 0;

    public String getName() {
        if (name == null) {
            name = "";
        }
        return name;
    }

    public int getType() {
        return type;
    }
}
