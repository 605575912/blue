package com.show.blue;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.support.loader.adapter.Holder;
import com.support.loader.adapter.ItemData;
import com.support.loader.adapter.UIListAdapter;

import javax.inject.Inject;

public class ChooseItem extends ItemData {
    protected String name = "";
    protected String tst = "";
    int tag = 0;
    Activity activity;

    public ChooseItem(String name, int tag, Activity activity) {
        this.name = name;
        this.tag = tag;
        this.activity = activity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, Context context) {

    }


    @Override
    public View getView(LayoutInflater listContainer, int position, View convertView, ViewGroup arg2) {
        View viewa = listContainer.inflate(R.layout.item_layout, null);
        return viewa;
    }

    boolean i = false;

    @Override
    public void updateView(UIListAdapter listImageAdapter, Holder vHolder, final int position, final View convertView) {
        final ChooseHolder viewHolder = (ChooseHolder) vHolder;
//        if (tag == 0) {
//            viewHolder.download_btn.setItemState(DownloadField.WAIT, "1");
//        } else if (tag == 1) {
//            viewHolder.download_btn.setItemState(DownloadField.STOP, "1");
//        } else if (tag == 2) {
//            viewHolder.download_btn.setItemState(DownloadField.PAUSE, "1");
//        } else if (tag == 3) {
//            viewHolder.download_btn.setItemState(DownloadField.END, "1");
//        } else if (tag == 4) {
//            viewHolder.download_btn.setItemState(DownloadField.INSTALLED, "1");
//        } else if (tag == 5) {
//            viewHolder.download_btn.setItemState(DownloadField.DELETED, "1");
//        } else if (tag == 6) {
//            viewHolder.download_btn.setItemState(DownloadField.ORDER, "1");
//        } else if (tag == 7) {
//            viewHolder.download_btn.setItemState(DownloadField.ORDER_FAIL, "1");
//        } else if (tag == 8) {
//            viewHolder.download_btn.setItemState(DownloadField.ASSEMBLING, "1");
//        } else if (tag == 9) {
//            viewHolder.download_btn.setItemState(DownloadField.WAIT_FOR_WIFI, "1");
//        } else if (tag == 10) {
//            viewHolder.download_btn.setItemState(DownloadField.INSTALLING, "1");
//        } else if (tag == 11) {
//            viewHolder.download_btn.setItemState(DownloadField.UNKOWN, MMPackageManager.DOWNLOAD);
//        } else if (tag == 12) {
//            viewHolder.download_btn.setItemState(DownloadField.UNKOWN, MMPackageManager.INSTALLED_OPEN);
//        } else if (tag == 13) {
//            viewHolder.download_btn.setItemState(DownloadField.UNKOWN, MMPackageManager.DOWNLOAD);
//        } else {
//            viewHolder.download_btn.showProgress(true);
//            viewHolder.download_btn.setspeedSize(DownloadField.START, 250);
        viewHolder.button9.setText(getName());
//        }

//
//        TranslateAnimation animation = new TranslateAnimation(-convertView.getWidth() / 8 , 0, 0, 0);
//        animation.setDuration(600);
//        animation.setFillAfter(true);
//        convertView.setAnimation(animation);
//        animation.start();
    }


    @Override
    public Holder setHolder(Activity activity, Holder vHolder, View convertView) {
        if (vHolder == null) {
            ChooseHolder viewHolder = new ChooseHolder();
            viewHolder.button9 = (TextView) convertView.findViewById(R.id.button9);
            return viewHolder;
        }
        return vHolder;
    }

    @Override
    public void onScrollStateChanged(int i) {

    }
}
