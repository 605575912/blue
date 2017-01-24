package com.show.blue.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;

import com.support.loader.utils.LogBlue;

/**
 * Created by liangzhenxiong on 16/3/24.
 */
public class PlayView extends View {

    public PlayView(Context context) {
        super(context);
    }

    public PlayView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measurewidth = measure(widthMeasureSpec);
        int measureheight = measure(heightMeasureSpec);
//
        Log.i("TAG", "h=====" + measureheight);
        Log.i("TAG", "===w==" + measurewidth);
        setMeasuredDimension(measurewidth, measureheight);
    }

    private int measure(int measureSpec) {

        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);

        // Default size if no limits are specified.

        int result = measureSpec;
        if (specMode == View.MeasureSpec.EXACTLY) {

            /**
             * 表示父视图希望子视图的大小应该是由specSize的值来决定的，
             * 系统默认会按照这个规则来设置子视图的大小，
             * 开发人员当然也可以按照自己的意愿设置成任意的大小;
             * 精准 和 match_parent时候()
             */

            result = specSize;
            LogBlue.i("TAG", "==EXACTLY==");
        } else if (specMode == MeasureSpec.AT_MOST) {

            /**
             * 表示子视图最多只能是specSize中指定的大小，开发人员应该尽可能小得去设置这个视图，并且保证不会超过specSize。
             * 系统默认会按照这个规则来设置子视图的大小，开发人员当然也可以按照自己的意愿设置成任意的大小
             * 最大这个数，注意父控件
             */
            result = specSize / 2;
            LogBlue.i("TAG", "===AT_MOST=");
        } else {
            LogBlue.i("TAG", "==UNSPECIFIED==");
        }
        return result;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(Color.YELLOW);
        super.onDraw(canvas);
    }

    @Override
    public boolean onDragEvent(DragEvent event) {
        return super.onDragEvent(event);
    }
}
