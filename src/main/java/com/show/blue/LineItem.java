package com.show.blue;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import com.support.loader.adapter.Holder;
import com.support.loader.adapter.ItemData;
import com.support.loader.adapter.UIListAdapter;

public class LineItem extends ItemData {
    String name = "";
    int tag = 0;
    Activity activity;

    public LineItem(String name, int tag, Activity activity) {
        this.name = name;
        this.tag = tag;
        this.activity = activity;
    }
    public final String getName() {
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

    int id = 0x22000;

    @Override
    public View getView(LayoutInflater listContainer, int position, View convertView, ViewGroup arg2) {
        HorizontalDividerView divisibleView = new HorizontalDividerView(activity, 1);
        divisibleView.setBackgroundColor(Color.WHITE);
        return divisibleView;
    }

    @Override
    public void updateView(UIListAdapter listImageAdapter, Holder vHolder, final int position, final View convertView) {
        final ChooseHolder viewHolder = (ChooseHolder) vHolder;
//        viewHolder.tv_.setText("行业里几乎所有的公司都把IP挂在嘴边，其中一些新入行的资本和公司则对买IP、卖IP电影显得尤为迫切。除去上影之外，《盗墓笔记》这一诱人IP背后，还有2014年初成立的南派投资与小米互娱，以及2011年末成立的乐视影业；而投资《夏有乔木 雅望天堂》的上海儒意影视制作有限公司和山东嘉博文化发展有限公司都成立于2013年，其中嘉博文化的主要作品有《小时代》第三部和第四部、《致青春2》等，而儒意影视的出品片单里大量的IP电影，《致青春》《老男孩》《小时代3》以及正在制作的《三生三世十里桃花》；主投、参投《我的新野蛮女友》、《极限挑战之皇家宝藏》的摩天轮文化则成立于2010年");


    }

     class ChooseHolder implements Holder {
        ImageView imageView;
    }

    @Override
    public Holder setHolder(Activity activity, Holder vHolder, View convertView) {
        if (vHolder == null) {
            ChooseHolder viewHolder = new ChooseHolder();
//            viewHolder.tv_ = (TextView) convertView.findViewById(R.id.tv_);
            return viewHolder;
        }
        return vHolder;
    }

    @Override
    public void onScrollStateChanged(int i) {

    }
}
