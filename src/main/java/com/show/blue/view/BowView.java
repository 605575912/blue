package com.show.blue.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.show.blue.R;
import com.support.loader.utils.LogBlue;

/**
 * Created by liangzhenxiong on 16/4/19.
 */
public class BowView extends SurfaceView implements SurfaceHolder.Callback {
    public static final Double PI = 3.14159265358979323846;
    private float mWaveToTop;
    private int mWaterColor;
    private int mWaterCenterColor;
    private int mWaterEndColor;
    private Paint mWaterPaint;
    private Paint mRPaint;
    private int mProgress;
    private int mMaxProgress;
    // private Drawable mFishBowlDrawable;
    private Path mWaterPath;
    private Path mRingPath;
    private int x_zoom = 100;
    // private Shader mWaveShader;

    /**
     * wave crest
     */
    private int y_zoom = 16;// 高度
    /**
     * offset of X
     */
    private final float offset = 0.1f;
    private final float max_right = x_zoom * offset;

    // wave animation
    private float waterOffset = 0.0f;
    /**
     * offset of Y
     */
    private float animOffset = 0.15f;
    private int mRefreshTime = 60;
    private boolean mIsStop;
    private boolean mHaveAnimation;
    private float mTopPadding = 0.0f;
    private float mBottomPadding = 0.0f;
    private float mStep = 2.0f;
    private float mLeftPadding;
    private float mRightPadding;

    public BowView(Context context) {
        super(context);
    }

    public BowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public BowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    @SuppressLint("NewApi")
    public BowView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {

        super(context, attrs, defStyleAttr, defStyleRes);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.FishBowlView);
//		int id = ta.getResourceId(
//				R.styleable.FishBowlView_fishbowl_drawable_id,
//				R.drawable.traffic_fishbowl_bg_green);
        int id;
//		mFishBowlBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
        mRefreshTime = ta.getInt(R.styleable.FishBowlView_fishbowl_wavetime, 0);
        mHaveAnimation = mRefreshTime > 0 ? true : false;

        id = ta.getResourceId(R.styleable.FishBowlView_water_color, 0);
        if (id != 0) {
            mWaterColor = context.getResources().getColor(id);
        } else {
            mWaterColor = ta.getColor(R.styleable.FishBowlView_water_color,
                    0xaa65D1FF);
        }
        // mWaterColor = 0xaa65D1FF;
        id = ta.getResourceId(R.styleable.FishBowlView_water_center_color, 0);
        if (id != 0) {
            mWaterCenterColor = context.getResources().getColor(id);
        } else {
            mWaterCenterColor = ta.getColor(
                    R.styleable.FishBowlView_water_center_color, 0xaa65D1FF);
        }
        id = ta.getResourceId(R.styleable.FishBowlView_water_end_color, 0);
        if (id != 0) {
            mWaterEndColor = context.getResources().getColor(id);
        } else {
            mWaterEndColor = ta.getColor(
                    R.styleable.FishBowlView_water_end_color, 0xaa65D1FF);
        }
        mProgress = ta.getInt(R.styleable.FishBowlView_fishbowl_progress, 0);
        mMaxProgress = ta.getInt(R.styleable.FishBowlView_fishbowl_maxprogress,
                100);
        // setProgress(mProgress);
        mWaterPaint = new Paint();
        // mWaterPaint.setColor(mWaterColor);
        mWaterPaint.setStyle(Paint.Style.FILL);
        mWaterPaint.setAntiAlias(true);
        mWaterPath = new Path();
        mRingPath = new Path();

        mRPaint = new Paint();
        mRPaint.setColor(Color.parseColor("#FF2FBEFC"));
        mRPaint.setStyle(Paint.Style.STROKE);
        mRPaint.setAntiAlias(true);
        mRPaint.setStrokeWidth(10f);
        mTopPadding = mBottomPadding = 10;
        SurfaceHolder holder = this.getHolder();
        holder.addCallback(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int measurewidth = measure(widthMeasureSpec, true);
        int measureheight = measure(heightMeasureSpec, false);
        Shader waveShader = new LinearGradient(0, 0, 0, measureheight,
                new int[]{mWaterColor, mWaterCenterColor, mWaterEndColor},
                null, Shader.TileMode.MIRROR);
        mWaterPaint.setShader(waveShader);
        drawpath(measurewidth,measureheight, mRPaint.getStrokeWidth());
        setMeasuredDimension(measurewidth, measureheight);
    }

    void drawpath(int measurewidth, int measureheight, float lineSize) {
        mRingPath.reset();
        RectF rect = new RectF(lineSize, lineSize, measurewidth - lineSize, measureheight - lineSize);
        mRingPath.addRoundRect(rect, rect.height() / 2, rect.height() / 2, Path.Direction.CW);
        mRingPath.close();
    }

    private int measure(int measureSpec, boolean isWidth) {
        int result = 0;
        int padding = isWidth ? getPaddingLeft() + getPaddingRight()
                : getPaddingTop() + getPaddingBottom();
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            result = size;
        } else {
            result = size;
            result += padding;
        }
        return result;
    }

    private RefreshProgressThread mRefreshProgressRunnable;

//    @Override
//    protected void onAttachedToWindow() {
//        super.onAttachedToWindow();
//        startAnimation();
//    }
//
//    @Override
//    protected void onDetachedFromWindow() {
//        super.onDetachedFromWindow();
//        stopAnimation();
//    }

    private void startAnimation() {
        if (mHaveAnimation && mProgress > 0 && mRefreshProgressRunnable == null) {
            mIsStop = false;
            mRefreshProgressRunnable = new RefreshProgressThread();
            mRefreshProgressRunnable.start();
        }
    }

    private void stopAnimation() {
        if (mRefreshProgressRunnable != null) {
            mIsStop = true;
            mRefreshProgressRunnable.interrupt();
            mRefreshProgressRunnable = null;
        }
    }

    boolean ispause = false;


    public void setIspause(boolean ispause) {
        this.ispause = ispause;
    }

    private class RefreshProgressThread extends Thread implements Runnable {
        public void run() {
            synchronized (BowView.this) {
                while (!mIsStop) {
                    // mTopPadding = mBottomPadding =
                    // UItools.dip2px(getContext(), 1)/*getHeight() / 24.0f*/;
                    float rate = mProgress / (mMaxProgress * 1.0f);
                    float verticalpadding = mTopPadding + mBottomPadding;
                    mWaveToTop = (1f - rate) * (getHeight() - verticalpadding);
                    mWaveToTop += mTopPadding;
                    if (mProgress > mMaxProgress / 2) {
                        y_zoom = (int) ((2.0f - rate) * 7);
                        x_zoom = (int) ((2.0f - rate) * 50);
                    } else {
                        y_zoom = (int) ((1.0f + rate) * 7);
                        x_zoom = (int) ((1.0f + rate) * 50);
                    }
                    if (!ispause) {
                        postInvalidate();
                    }
                    try {
                        Thread.sleep(mRefreshTime);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        mIsStop = true;
                    }
                }
            }
        }
    }

    private void calculatePath(int width, int height) {
        mWaterPath.reset();
        if (mHaveAnimation) {
            if (waterOffset > 10000) {
                waterOffset = 0;
            } else {
                waterOffset += animOffset;
            }
            mWaterPath.moveTo(mLeftPadding, height);
            for (float i = mLeftPadding / (x_zoom * 1.0f); x_zoom * i <= width
                    - mRightPadding + max_right; i += offset) {
                mWaterPath.lineTo((x_zoom * i),
                        (float) (y_zoom * Math.cos(i + waterOffset))
                                + mWaveToTop);
            }
            mWaterPath.lineTo(width - mRightPadding, height);
            mWaterPath.close();
        } else {
            float rate = mProgress / (mMaxProgress * 1.0f);
            float verticalpadding = mTopPadding + mBottomPadding;
            mWaveToTop = (1f - rate) * (height - verticalpadding);
            mWaterPath.moveTo(mLeftPadding, height - mBottomPadding);
            mWaterPath.lineTo(mLeftPadding, mWaveToTop);
            mWaterPath.lineTo(width - mRightPadding, mWaveToTop);
            mWaterPath.lineTo(width - mRightPadding, height - mBottomPadding);
            mWaterPath.close();
        }
    }

    Mythread mythread;

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        mIsStop = false;
        if (mythread == null) {
            mythread = new Mythread(surfaceHolder);
        }
        mythread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        mIsStop = true;
        if (mythread != null) {
            mythread.interrupt();
            mythread = null;
        }

    }
    public void runA() {
        synchronized (BowView.this) {
//            while (!mIsStop) {
                // mTopPadding = mBottomPadding =
                // UItools.dip2px(getContext(), 1)/*getHeight() / 24.0f*/;
                float rate = mProgress / (mMaxProgress * 1.0f);
                float verticalpadding = mTopPadding + mBottomPadding;
                mWaveToTop = (1f - rate) * (getHeight() - verticalpadding);
                mWaveToTop += mTopPadding;
                if (mProgress > mMaxProgress / 2) {
                    y_zoom = (int) ((2.0f - rate) * 7);
                    x_zoom = (int) ((2.0f - rate) * 50);
                } else {
                    y_zoom = (int) ((1.0f + rate) * 7);
                    x_zoom = (int) ((1.0f + rate) * 50);
                }
//                if (!ispause) {
//                    postInvalidate();
//                }

//            }
        }
    }
    class Mythread extends Thread {
        SurfaceHolder holder;
        boolean isrun = true;

        Mythread(SurfaceHolder holder) {
            this.holder = holder;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (!mIsStop){
                Canvas canvas = holder.lockCanvas(); // 调用渲染界面的方法 渲染出界面
                runA();
                calculatePath(canvas.getWidth(), canvas.getHeight());
                canvas.save();
                canvas.clipPath(mWaterPath);
                if (mRingPath != null) {
                    canvas.clipPath(mRingPath);
                }
                if (mProgress > 0) {
                    canvas.drawPaint(mWaterPaint);
                }
                canvas.restore();
                if (mRingPath != null) {

                    canvas.drawPath(mRingPath, mRPaint);
                }
//                toDraw(canvas);
                holder.unlockCanvasAndPost(canvas);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    mIsStop = true;
                }
            }

        }

        // 界面渲染
        public void toDraw(Canvas canvas) {

            LogBlue.i("TAG", "canvas=======" + canvas.getWidth());
//			if (!mHaveAnimation)





        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }
}
