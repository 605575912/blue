package com.show.blue;

import android.content.Context;
import android.view.View;

/**
 * 分割线 空白的
 * Created by lzx on 16/6/22.
 */
public class HorizontalDividerView extends View {
    int h = 0;
    public HorizontalDividerView(Context context, int h) {
        super(context);
        this.h = h;

    }

    public void setH(int h) {
        this.h = h;
        requestLayout();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specSize = MeasureSpec.getSize(h);
        setMeasuredDimension(widthMeasureSpec, specSize);
    }
}
