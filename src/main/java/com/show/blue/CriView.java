package com.show.blue;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by apple on 2016/11/21.
 */

public class CriView extends ViewGroup {
    Paint paint;
    Paint wpaint;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {

            } else {

            }
        }
    };

    public CriView(Context context) {
        super(context);
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setColor(Color.GREEN);
        paint.setAntiAlias(true);

        wpaint = new Paint();
        wpaint.setStyle(Paint.Style.FILL);
        wpaint.setColor(Color.WHITE);
        wpaint.setAntiAlias(true);
//        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        addView(new showEar(context));
        addView(new showEar(context));

    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int w = getMeasuredWidth() / 4;
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            final int freeWidthSpec = MeasureSpec.makeMeasureSpec(
                    w, MeasureSpec.EXACTLY);

            final int freeHeightSpec = MeasureSpec.makeMeasureSpec(
                    200, MeasureSpec.EXACTLY);
            childView.measure(freeWidthSpec, freeHeightSpec);
        }
    }

    public static int getChildMeasureSpec(int spec, int padding, int childDimension) {
        int specMode = MeasureSpec.getMode(spec);
        int specSize = MeasureSpec.getSize(spec);

        int size = Math.max(0, specSize - padding);

        int resultSize = 0;
        int resultMode = 0;

        switch (specMode) {
            // Parent has imposed an exact size on us
            case MeasureSpec.EXACTLY:
                if (childDimension >= 0) {
                    resultSize = childDimension;
                    resultMode = MeasureSpec.EXACTLY;
                } else if (childDimension == LayoutParams.MATCH_PARENT) {
                    // Child wants to be our size. So be it.
                    resultSize = size;
                    resultMode = MeasureSpec.EXACTLY;
                } else if (childDimension == LayoutParams.WRAP_CONTENT) {
                    // Child wants to determine its own size. It can't be
                    // bigger than us.
                    resultSize = size;
                    resultMode = MeasureSpec.AT_MOST;
                }
                break;

            // Parent has imposed a maximum size on us
            case MeasureSpec.AT_MOST:
                if (childDimension >= 0) {
                    // Child wants a specific size... so be it
                    resultSize = childDimension;
                    resultMode = MeasureSpec.EXACTLY;
                } else if (childDimension == LayoutParams.MATCH_PARENT) {
                    // Child wants to be our size, but our size is not fixed.
                    // Constrain child to not be bigger than us.
                    resultSize = size;
                    resultMode = MeasureSpec.AT_MOST;
                } else if (childDimension == LayoutParams.WRAP_CONTENT) {
                    // Child wants to determine its own size. It can't be
                    // bigger than us.
                    resultSize = size;
                    resultMode = MeasureSpec.AT_MOST;
                }
                break;

            // Parent asked to see how big we want to be
            case MeasureSpec.UNSPECIFIED:
                if (childDimension >= 0) {
                    // Child wants a specific size... let him have it
                    resultSize = childDimension;
                    resultMode = MeasureSpec.EXACTLY;
                } else if (childDimension == LayoutParams.MATCH_PARENT) {
                    // Child wants to be our size... find out how big it should
                    // be
                    resultSize = size;
                    resultMode = MeasureSpec.UNSPECIFIED;
                } else if (childDimension == LayoutParams.WRAP_CONTENT) {
                    // Child wants to determine its own size.... find out how
                    // big it should be
                    resultSize = size;
                    resultMode = MeasureSpec.UNSPECIFIED;
                }
                break;
        }
        //noinspection ResourceType
        return MeasureSpec.makeMeasureSpec(resultSize, resultMode);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        int r = getMeasuredWidth() >> 2;
        paint.setShadowLayer(0, 0, 0, 0);
        paint.setColor(Color.GREEN);
        float startAngle = drawArc(canvas, r, paint, 0, 120);
        paint.setShadowLayer(20, 1, 1, Color.WHITE);
        paint.setColor(Color.RED);


//        canvas.scale(-1, -1);
        canvas.save();
        Matrix matrix = new Matrix();
        matrix.setTranslate(canvas.getWidth()/4,0);
        matrix.preScale(-1, 1);
        canvas.setMatrix(matrix);
        startAngle = drawArc(canvas, r, paint, startAngle, 150);
        canvas.restore();
        paint.setShadowLayer(0, 0, 0, 0);
        paint.setColor(Color.DKGRAY);


        drawArc(canvas, r, paint, startAngle, 90);
        int wr = r >> 1;
        drawArc(canvas, wr, wpaint, 0, 360);
    }

    float drawArc(Canvas canvas, int r, Paint paint, float startAngle, float sweepAngle) {
        int centx = getMeasuredWidth() >> 1;
        int left = centx - r;
        int top = (getMeasuredHeight() >> 1) - r;
        RectF rectF = new RectF(left, top, centx + r, top + 2 * r);
        canvas.drawArc(rectF, startAngle, sweepAngle, true, paint);
        return startAngle + sweepAngle;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int centerx = getMeasuredWidth() >> 1;
        int centery = getMeasuredHeight() >> 1;
        int radio = getMeasuredWidth() >> 2;
        View childView = getChildAt(0);
        int x0 = (int) (centerx + radio * Math.cos(0 * 3.1415926 / 180));
        int y0 = (int) (centery + radio * Math.sin(0 * 3.1415926 / 180));
        layout(childView, x0, y0);

        View childView1 = getChildAt(1);
        int x1 = (int) (centerx + radio * Math.cos(300 * 3.1415926 / 180));
        int y1 = (int) (centery + radio * Math.sin(300 * 3.1415926 / 180));
        layout(childView1, x1 + childView1.getMeasuredHeight(), y1);
    }

    void layout(View childView, int l, int t) {
        childView.layout(l, t, l + childView.getMeasuredWidth(), t + childView.getMeasuredHeight());
    }
}
