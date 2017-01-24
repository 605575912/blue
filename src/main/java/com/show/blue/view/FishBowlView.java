package com.show.blue.view;

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
import android.widget.RelativeLayout;

import com.show.blue.R;


public class FishBowlView extends RelativeLayout {
    public static String TAG = "FishBowlView";
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


    public FishBowlView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public FishBowlView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
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
        float strokewidth = ta.getDimension(R.styleable.FishBowlView_fishbowl_strokewidth,
                5.0f);
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
        mRPaint.setStrokeWidth(strokewidth);
        mTopPadding = mBottomPadding = strokewidth;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // if(mCopyWaterPath != null && mCopyRingPath != null){
        try {
//			if (!mHaveAnimation)
            calculatePath(getWidth(), getHeight());
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
        } catch (Exception e) {
            // TODO: handle exception
        }
        // }

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
        drawpath(measurewidth, measureheight, mRPaint.getStrokeWidth());
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

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        startAnimation();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopAnimation();
    }

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
            synchronized (FishBowlView.this) {
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

//    AnimationThread animationThread;

//     void setProgress(int progress) {
//        if (progress != mProgress) {
//            if (mHaveAnimation) {
//                if (animationThread == null) {
//                    animationThread = new AnimationThread(progress);
//                    animationThread.start();
//                }
//
//            } else {
//                this.mProgress = progress > mMaxProgress ? mMaxProgress
//                        : progress;
//                calculatePath();
//                postInvalidate();
//            }
//        }
//    }

//    /**
//     * @param progress   进度
//     * @param waterColor 水的颜色，可以填0默认
//     * @param bg         背景图片，可以填0默认
//     */
//    public void setProgress(int progress, int waterColor, int bg) {
//        if (waterColor != 0) {
//            mWaterPaint.setColor(waterColor);
//        }
//        setProgress(progress);
//    }
//
//    private class AnimationThread extends Thread {
//        private int mDesProgress;
//
//        public AnimationThread(int progress) {
//            this.mDesProgress = progress > mMaxProgress ? mMaxProgress
//                    : progress;
//        }
//
//        public void run() {
//            if (mIsStop && mDesProgress > 0)
//                startAnimation();
//            boolean isIncreate = true;
//            if (mDesProgress < mProgress)
//                isIncreate = false;
//            while (mProgress != mDesProgress) {
//                if (isIncreate) {
//                    mProgress += mStep;
//                    if (mProgress > mDesProgress)
//                        mProgress = mDesProgress;
//                } else {
//                    mProgress -= mStep;
//                    if (mProgress < mDesProgress)
//                        mProgress = mDesProgress;
//                }
//                try {
//                    Thread.sleep(40);
//                } catch (InterruptedException e) {
//                    // TODO Auto-generated catch block
//                    e.printStackTrace();
//                }
//            }
//            // if (!mIsStop && mProgress == 0)
//            // stopAnimation();
//        }
//    }

    public void setEmptyPadding(float leftpadding, float rightpadding) {
        this.mLeftPadding = leftpadding;
        this.mRightPadding = rightpadding;
    }


    public void setWaveColor(int startcolorid, int centercolorid, int endcolorid) {
        this.mWaterColor = getResources().getColor(startcolorid);
        this.mWaterCenterColor = getResources().getColor(centercolorid);
        this.mWaterEndColor = getResources().getColor(endcolorid);
    }
}
