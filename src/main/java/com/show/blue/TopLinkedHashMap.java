package com.show.blue;

import android.text.TextUtils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by apple on 2016/12/6.
 */

public class TopLinkedHashMap<K, V> {
    private ChangeListener changeListener;
    private final LinkedHashMap<K, V> map;

    public TopLinkedHashMap(ChangeListener changeListener) {
        this.changeListener = changeListener;
        if (changeListener == null) {
            this.changeListener = new ChangeListener() {
                @Override
                public void OnChanged(String tip) {
                }
            };
        }
        this.map = new LinkedHashMap<K, V>(0, 0.75f, true);
    }

    /**
     * 输入提示语
     *
     * @param key
     * @param value
     */
    public void put(K key, String value) {
        if (TextUtils.isEmpty(value)) {
            map.remove(key);
            for (Object o : map.entrySet()) {
                Map.Entry entry = (Map.Entry) o;
                Object val = entry.getValue();
                changeListener.OnChanged((String) val);
                return;
            }
        } else {
            V old = map.put(key, (V) value);
            changeListener.OnChanged(value);
        }
    }

    /**
     * 对应完成的TAG
     *
     * @param key
     */
    public void completeput(K key) {
        map.remove(key);
        Object val = get();
        if (val != null) {
            changeListener.OnChanged((String) val);
        }
    }

    /**
     * 初始化提示语
     *
     * @param key
     * @param v
     */
    public void init(K key, String v) {
        map.put(key, (V) v);
    }

    /**
     * 获取最近的未完成提示语
     *
     * @return
     */
    public Object get() {
        for (Object o : map.entrySet()) {
            Map.Entry entry = (Map.Entry) o;
            Object val = entry.getValue();
            return val;
        }
        return null;
    }
}
