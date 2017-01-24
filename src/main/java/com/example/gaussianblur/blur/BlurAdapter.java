package com.example.gaussianblur.blur;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.show.blue.R;
import com.support.loader.ServiceLoader;
import com.support.loader.packet.ImageOptions;
import com.support.loader.utils.ImageLoadingListener;

import java.util.List;

/**
 * Created by liangzhenxiong on 16/3/12.
 */
public class BlurAdapter extends BaseAdapter {
    List<BlurItem> list;
    Context context;
    ImageOptions options;

    public BlurAdapter(Context context, List<BlurItem> list) {
        this.list = list;
        this.context = context;
        options = new ImageOptions(context, R.mipmap.ic_launcher);
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        BlurItem s = list.get(position);
        Holder vHolder = null;
        if (convertView == null) {
            vHolder = new Holder();
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.blur_listview_item, null);
            vHolder.textView = (TextView) convertView.findViewById(R.id.tv_name);
            vHolder.imageView3 = (ImageView) convertView.findViewById(R.id.imageView3);
            convertView.setTag(vHolder);
        } else {
            vHolder = (Holder) convertView.getTag();
        }
        vHolder.textView.setText(s.name);
        ServiceLoader.getInstance().displayImage(options, s.url, vHolder.imageView3, new ImageLoadingListener() {
            @Override
            public void onLoadingStarted(String var1, View var2) {

            }

            @Override
            public void onLoadingFailed(String var1, View var2) {

            }

            @Override
            public void onLoadingComplete(String var1, View var2, Bitmap var3) {

            }

            @Override
            public Bitmap onLoadingBitmap(String var1, View var2, Bitmap var3) {
                return var3;
            }

            @Override
            public void onLoadingCancelled(String var1, View var2) {

            }
        });
        return convertView;
    }

    class Holder {
        ImageView imageView3;
        TextView textView;
    }
}
