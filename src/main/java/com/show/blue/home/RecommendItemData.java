package com.show.blue.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.TextView;

import com.nineoldandroids.animation.ValueAnimator;
import com.show.blue.R;
import com.show.blue.view.FishBowlView;
import com.support.loader.adapter.Holder;
import com.support.loader.adapter.ItemData;
import com.support.loader.adapter.UIListAdapter;

/**
 * Created by liangzhenxiong on 16/3/29.
 */
public class RecommendItemData extends ItemData {
    Item item;

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, Context context) {
        Intent intent = new Intent();
        intent.setClass(context, getAclass());
        context.startActivity(intent);

    }

    int with;
    float temp;
    int state = 0;

    @Override
    public View getView(LayoutInflater listContainer, int position, View convertView, ViewGroup arg2) {
        View view = listContainer.inflate(R.layout.home_listview_item, null);
        return view;
    }

    @Override
    public void updateView(UIListAdapter listImageAdapter, Holder vHolder, int position, View convertView) {
        if (vHolder instanceof ViewHolder) {
            if (item == null) {
                return;
            }
            final ViewHolder holder = (ViewHolder) vHolder;
            holder.textView.setText(item.name);
            if (with == 0) {
                ViewGroup.LayoutParams layoutParams = holder.fishbowlview.getLayoutParams();
                with = layoutParams.width;
            }
            if (state == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                holder.fishbowlview.setIspause(false);
            } else if (state == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL || state == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                holder.fishbowlview.setIspause(true);
            }
            holder.fishbowlview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ValueAnimator valueAnimator = ValueAnimator.ofFloat(1, 2.0f);
                    valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            Float value = (Float) valueAnimator.getAnimatedValue();
                            if (temp == value) {
                                return;
                            } else {
                                temp = value;
                            }
                            ViewGroup.LayoutParams layoutParams = holder.fishbowlview.getLayoutParams();
                            layoutParams.width = (int) (with * value);
                            holder.fishbowlview.setLayoutParams(layoutParams);
                        }
                    });
                    valueAnimator.setDuration(400);
                    valueAnimator.start();
                }
            });
        }
    }

    @Override
    public Holder setHolder(Activity activity, Holder vHolder, View convertView) {
        if (vHolder == null) {
            ViewHolder holder = new ViewHolder();
            holder.textView = (TextView) convertView.findViewById(R.id.tv_name);
            holder.fishbowlview = (FishBowlView) convertView.findViewById(R.id.fishbowlview);
            return holder;
        }
        return vHolder;
    }

    @Override
    public void onScrollStateChanged(int i) {
        state = i;
    }
}
