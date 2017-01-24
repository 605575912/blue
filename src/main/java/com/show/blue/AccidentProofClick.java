package com.show.blue;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;


/**
 * 防止意外点击和快速连续点击
 * 用法是调用View的setOnTouchListener();
 *
 * @author lhy
 */
public class AccidentProofClick implements OnTouchListener {
    //final int	MIN_CLICK_DURATION = 20 ; //最小点击时长
    final int MAX_CLICK_DURATION = 350; //最大点击时长
    final int MIN_CLICK_INTERVAL_TIME = 1000; //两次点击时间间隔
    String TAG;
    //long	mLastDownTime ; //记录上次按下时间
    long mLastClickTime; //记录上次击发点击事件时间
    float mLastX, mLastY;
    boolean mIsPressed; //标记是否被按下
    View mPressedView; //标记按下的View
    long mPressedTime;

    public AccidentProofClick() {
        TAG = getClass().getSimpleName();
        mLastX = 0.0f;
        mLastY = 0.0f;
        //mLastDownTime = System.currentTimeMillis() ;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        float deltaX = 0.0f, deltaY = 0.0f;
//	long duration = 0 ;
        long clickinterval = 0;
        long currenttime = System.currentTimeMillis();
        boolean handle = false;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                v.setPressed(true);
                //mLastDownTime = currenttime;
                mPressedTime = currenttime;
                mLastX = event.getX();
                mLastY = event.getY();
                mIsPressed = true;
                mPressedView = v;
                break;
            case MotionEvent.ACTION_MOVE:
                deltaX = event.getX() - mLastX;
                deltaY = event.getY() - mLastY;
                //duration = currenttime - mLastDownTime;
                break;
            case MotionEvent.ACTION_UP:
                v.setPressed(false);
                deltaX = event.getX() - mLastX;
                deltaY = event.getY() - mLastY;
                //duration = currenttime - mLastDownTime;
                clickinterval = currenttime - mLastClickTime;
                if (clickinterval < 0) { //2015.4.10 Mic: 将时间往前调之后，会导致点击无效，因此需要重置下mLastClickTime
                    mLastClickTime = currenttime;
                }
                if (hasOnClickListeners(v)) {
//		v.setPressed(false);
                    long pressedtime = currenttime - mPressedTime;
                    final int X = (int) event.getX(), Y = (int) event.getY();
                    float distance = (float) Math.sqrt(deltaX * deltaX + deltaY * deltaY);
                    final boolean pointerInView = X >= 0 && Y >= 0 && X <= v.getMeasuredWidth() && Y <= v.getMeasuredHeight();
                    if (pointerInView && mPressedView == v //&& duration >= MIN_CLICK_DURATION /*&& duration <= MAX_CLICK_DURATION*/ //2013.11.28不判断上限时间
                            && clickinterval > MIN_CLICK_INTERVAL_TIME
                            && distance < 30
                            && pressedtime < 150) {
                        mLastClickTime = currenttime;
                        // v.callOnClick()
                        v.performClick();
                    } else {
                    }
                    event.setAction(MotionEvent.ACTION_CANCEL); // 不击发原先的点击事件
                }
                mIsPressed = false;
                mPressedView = null;
                break;
            default:
                v.setPressed(false);
                mIsPressed = false;
                mPressedView = null;
                break;
        }
        return handle;
    }

    boolean hasOnClickListeners(View v) {
        return v.hasOnClickListeners();
    }
}
