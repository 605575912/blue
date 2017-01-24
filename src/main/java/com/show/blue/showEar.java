package com.show.blue;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by apple on 2016/11/21.
 */

public class showEar extends ViewGroup {
    Paint paint;
    Path path = new Path();

    public showEar(Context context) {
        super(context);
        setWillNotDraw(false);

        paint = new Paint();
        paint.setStrokeWidth(1);
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#aaee11"));
        paint.setStyle(Paint.Style.FILL);
//        setOrientation(LinearLayout.VERTICAL);
        TextView textView = new TextView(context);
        textView.setTextColor(Color.BLACK);
        textView.setGravity(Gravity.CENTER);
        textView.setText("1200");
//        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(textView);

        TextView textView1 = new TextView(context);
        textView1.setText("卖鸡所得");
        textView1.setTextColor(Color.BLACK);
        textView1.setGravity(Gravity.CENTER);
        addView(textView1);
        setBackgroundColor(Color.argb(100, 100, 100, 100));
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        paint.setStyle(Paint.Style.FILL);
        float top = (float) (getMeasuredHeight() * 0.75);
        RectF rectF = new RectF(0, top, 0 + 12, top + 12);
        canvas.drawArc(rectF, 0, 360, true, paint);
        paint.setStyle(Paint.Style.STROKE);
        path.reset();
        path.moveTo(rectF.centerX(), rectF.centerY());
        path.lineTo((float) (startpadding * 0.5), getMeasuredHeight() / 2);
        path.lineTo(getMeasuredWidth(), getMeasuredHeight() / 2);
        canvas.drawPath(path, paint);
    }

    int startpadding = 35;
    int maxw = 0;

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        int maxh = 0;
        for (int i = 0; i < getChildCount(); i++) {
            View childView = getChildAt(i);
            if (childView instanceof TextView) {
                TextView textView0 = (TextView) childView;
                int th = textView0.getPaint().getFontMetricsInt().bottom - textView0.getPaint().getFontMetricsInt().top;
                int tw = (int) textView0.getPaint().measureText("人") * textView0.getText().toString().length();
                final int freeHeightSpec = MeasureSpec.makeMeasureSpec(
                        th, MeasureSpec.EXACTLY);
                final int freeWidthSpec = MeasureSpec.makeMeasureSpec(
                        tw, MeasureSpec.EXACTLY);
                childView.measure(freeWidthSpec, freeHeightSpec);
                if (maxw < tw) {
                    maxw = tw;
                }
                maxh = maxh + childView.getMeasuredHeight();
            }
        }
        final int freeHeightSpec = MeasureSpec.makeMeasureSpec(
                maxh, MeasureSpec.EXACTLY);
        final int freeWidthSpec = MeasureSpec.makeMeasureSpec(
                maxw + startpadding, MeasureSpec.EXACTLY);
        super.onMeasure(freeWidthSpec, freeHeightSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        View childView = getChildAt(0);
        View childView1 = getChildAt(1);
        int vl = startpadding + 0;
        layout(childView, vl, 0);
        layout(childView1, vl, getMeasuredHeight() - childView1.getMeasuredHeight());
    }

    void layout(View childView, int l, int t) {
        childView.layout(l, t, l + childView.getMeasuredWidth(), t + childView.getMeasuredHeight());
    }
}
