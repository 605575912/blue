package com.show.blue.icon;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.Region;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.show.blue.R;


public class FishBowlView extends LinearLayout {
	public static String TAG = "FishBowlView";
	public static final Double PI = 3.14159265358979323846;
	private float mWaveToTop;
	private int mWaterColor;
	private int mWaterCenterColor;
	private int mWaterEndColor;
	private Paint mWaterPaint;
	private int mProgress;
	private int mMaxProgress;
	// private Drawable mFishBowlDrawable;
	private Bitmap mFishBowlBitmap;
	private Path mWaterPath;
	private Path mRingPath;
	private int x_zoom = 100;
	// private Shader mWaveShader;

	/** wave crest */
	private int y_zoom = 16;// 高度
	/** offset of X */
	private final float offset = 0.1f;
	private final float max_right = x_zoom * offset;

	// wave animation
	private float waterOffset = 0.0f;
	/** offset of Y */
	private float animOffset = 0.15f;
	int width;
	int height;
	private int mRefreshTime = 60;
	private boolean mIsStop;
	private boolean mHaveAnimation;
	private float mTopPadding = 3.0f;
	private float mBottomPadding = 3.0f;
	private float mStep = 2.0f;
	private float mLeftPadding;
	private float mRightPadding;

	// private Path mCopyWaterPath;
	// private Path mCopyRingPath;

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
		int id = ta.getResourceId(
				R.styleable.FishBowlView_fishbowl_drawable_id,
				R.drawable.traffic_manager_head_bg_orange);
		mFishBowlBitmap = BitmapFactory.decodeResource(getResources(), id);
		mRefreshTime = ta.getInt(R.styleable.FishBowlView_fishbowl_wavetime, 0);
		if (mRefreshTime > 0)
			mHaveAnimation = true;
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
		mTopPadding = mBottomPadding = 6;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		// if(mCopyWaterPath != null && mCopyRingPath != null){
		try {
//			if (!mHaveAnimation)
				calculatePath();
			canvas.drawBitmap(mFishBowlBitmap, 0, 0, new Paint());
			canvas.save();
			canvas.clipPath(mWaterPath);
			canvas.clipPath(mRingPath, Region.Op.INTERSECT);
			if (mProgress > 0){
//				canvas.drawPath(mWaterPath, mWaterPaint);
				canvas.drawPaint(mWaterPaint);
			}
			canvas.restore();
		} catch (Exception e) {
			// TODO: handle exception
//			AspLog.e(TAG, "onDraw error is" + e.toString());
		}
		// }

	}

	private int measure(int measureSpec) {

		int specMode = View.MeasureSpec.getMode(measureSpec);
		int specSize = View.MeasureSpec.getSize(measureSpec);

		// Default size if no limits are specified.

		int result = measureSpec;
		if (specMode == View.MeasureSpec.AT_MOST) {

			// Calculate the ideal size of your
			// control within this maximum size.
			// If your control fills the available
			// space return the outer bound.
			result = specSize;
		} else if (specMode == View.MeasureSpec.EXACTLY) {

			// If your control can fit within these bounds return that
			// value.
			result = specSize;
		}else {

		}
		return result;
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int measurewidth = measure(widthMeasureSpec);
		int measureheight = measure(heightMeasureSpec);
//		if (measurewidth != mFishBowlBitmap.getWidth()) {
//			mFishBowlBitmap = AspireUtils.zoomBitmap(mFishBowlBitmap,
//					measurewidth);
//			measureheight = mFishBowlBitmap.getHeight();
//		}
		Shader waveShader = new LinearGradient(0, 0, 0, measureheight,
				new int[] { mWaterColor, mWaterCenterColor, mWaterEndColor },
				null, Shader.TileMode.MIRROR);
		mWaterPaint.setShader(waveShader);
		float r = (measureheight - mTopPadding - mBottomPadding) / 2.0f;
		float x, y;
		x = measureheight / 2.0f;
		y = x;
		mRingPath.addCircle(x, y, r, Direction.CCW);
		mRingPath.close();
		width = measurewidth;
		height = measureheight;
		Log.i("TAG","h====="+measureheight);
		Log.i("TAG", "===w==" + measurewidth);
		setMeasuredDimension(measurewidth, measureheight);
	}
//	private int measure(int measureSpec, boolean isWidth) {
//		int result;
//		int padding = isWidth ? getPaddingLeft() + getPaddingRight()
//				: getPaddingTop() + getPaddingBottom();
//		int mode = MeasureSpec.getMode(measureSpec);
//		int size = MeasureSpec.getSize(measureSpec);
//		if (mode == MeasureSpec.EXACTLY) {
//			result = size;
//		} else {
//			// if (mode == MeasureSpec.AT_MOST) {
//			// result = isWidth ? getSuggestedMinimumWidth()
//			// : getSuggestedMinimumHeight();
//			// result += padding;
//			// if (isWidth) {
//			// result = Math.max(result, size);
//			// } else {
//			// result = Math.min(result, size);
//			// }
//			// } else
//			result = isWidth ? mFishBowlBitmap.getWidth() : mFishBowlBitmap
//					.getHeight();
//			result += padding;
//		}
//		// result = isWidth ? mFishBowlDrawable.getIntrinsicWidth()
//		// : mFishBowlDrawable.getIntrinsicHeight();
//		// result += padding;
//		return result;
//	}

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

	private class RefreshProgressThread extends Thread implements Runnable {
		public void run() {
			synchronized (FishBowlView.this) {
				while (!mIsStop) {
					// mTopPadding = mBottomPadding =
					// UItools.dip2px(getContext(), 1)/*getHeight() / 24.0f*/;
					float rate = mProgress / (mMaxProgress * 1.0f);
					float verticalpadding = mTopPadding + mBottomPadding;
					mWaveToTop = (1f - rate) * (height - verticalpadding);
					mWaveToTop += mTopPadding;
					if (mProgress > mMaxProgress / 2) {
						y_zoom = (int) ((2.0f - rate) * 7);
						x_zoom = (int) ((2.0f - rate) * 50);
					} else {
						y_zoom = (int) ((1.0f + rate) * 7);
						x_zoom = (int) ((1.0f + rate) * 50);
					}
//					calculatePath();
					postInvalidate();
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

	private void calculatePath() {
		mWaterPath.reset();
		// mRingPath.reset();
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
			// float r = (getHeight() - mTopPadding - mBottomPadding) / 2.0f;
			// float x, y;
			// x = getHeight() / 2.0f;
			// y = x;
			// for (float j = 0; j <= 360; j++) {
			// float x = (float) (r * (Math.cos(j / 180 * PI) + 1));
			// float y = (float) (r * (Math.sin(j / 180 * PI) + 1));
			// mRingPath.lineTo(x, y);
			// mRingPath.addCircle(x, y, radius, dir)
			// }
			// mRingPath.addCircle(x, y, r - 10, Direction.CW);
			mWaterPath.lineTo(width - mRightPadding, height);
//			mWaterPath.op(path, op)
			mWaterPath.close();
			// mWaterPath.op(mRingPath, Path.Op.INTERSECT);
		} else {
			// mTopPadding = mBottomPadding = getHeight() / 13f;
			float rate = mProgress / (mMaxProgress * 1.0f);
			float verticalpadding = mTopPadding + mBottomPadding;
			mWaveToTop = (1f - rate) * (height - verticalpadding);
			mWaterPath.moveTo(mLeftPadding, height-mBottomPadding);
			mWaterPath.lineTo(mLeftPadding, mWaveToTop);
			mWaterPath.lineTo(width - mRightPadding, mWaveToTop);
			mWaterPath.lineTo(width - mRightPadding, height-mBottomPadding);
			mWaterPath.close();
		}
		// final Path tempwaterpath = new Path(mWaterPath);
		// final Path tempringpath = new Path(mRingPath);
		// post(new Runnable() {
		//
		// @Override
		// public void run() {
		// // TODO Auto-generated method stub
		// mCopyWaterPath = tempwaterpath;
		// mCopyRingPath = tempringpath;
		// }
		// });
	}

	public void setProgress(int progress) {
		if (progress != mProgress) {
			if (mHaveAnimation) {
				new AnimationThread(progress).start();
			} else {
				this.mProgress = progress > mMaxProgress ? mMaxProgress
						: progress;
				calculatePath();
				postInvalidate();
			}
		}
	}

	/**
	 * @param progress
	 *            进度
	 * @param waterColor
	 *            水的颜色，可以填0默认
	 * @param bg
	 *            背景图片，可以填0默认
	 */
	public void setProgress(int progress, int waterColor, int bg) {
		if (waterColor != 0) {
			mWaterPaint.setColor(waterColor);
		}
		if (bg != 0) {
			mFishBowlBitmap = BitmapFactory.decodeResource(getResources(), bg);
		}
		setProgress(progress);
	}

	private class AnimationThread extends Thread {
		private int mDesProgress;

		public AnimationThread(int progress) {
			this.mDesProgress = progress > mMaxProgress ? mMaxProgress
					: progress;
		}

		public void run() {
			if (mIsStop && mDesProgress > 0)
				startAnimation();
			boolean isIncreate = true;
			if (mDesProgress < mProgress)
				isIncreate = false;
			while (mProgress != mDesProgress) {
				if (isIncreate) {
					mProgress += mStep;
					if (mProgress > mDesProgress)
						mProgress = mDesProgress;
				} else {
					mProgress -= mStep;
					if (mProgress < mDesProgress)
						mProgress = mDesProgress;
				}
				try {
					Thread.sleep(30);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// if (!mIsStop && mProgress == 0)
			// stopAnimation();
		}
	}

	public void setEmptyPadding(float leftpadding, float rightpadding) {
		this.mLeftPadding = leftpadding;
		this.mRightPadding = rightpadding;
	}

	public void setFishBowlImage(int resid) {
		mFishBowlBitmap = BitmapFactory.decodeResource(getResources(), resid);
	}

	public void setWaveColor(int startcolorid, int centercolorid, int endcolorid) {
		this.mWaterColor = getResources().getColor(startcolorid);
		this.mWaterCenterColor = getResources().getColor(centercolorid);
		this.mWaterEndColor = getResources().getColor(endcolorid);
	}
}
