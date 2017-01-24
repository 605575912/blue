package com.show.test;

import android.app.Instrumentation;
import android.content.Intent;
import android.test.ActivityTestCase;
import android.test.ActivityUnitTestCase;
import android.test.InstrumentationTestCase;
import android.test.suitebuilder.annotation.MediumTest;
import android.test.suitebuilder.annotation.SmallTest;
import android.util.Log;

import com.show.blue.TestActivity;

/**
 * Created by apple on 2016/10/22.
 */

public class APT extends ActivityTestCase {
    private Intent mLoginIntent;

//    public APT() {
//        super(TestActivity.class);
//    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mLoginIntent = new Intent(getInstrumentation().getTargetContext(), TestActivity.class);
//        test_add();
    }
    @SmallTest
    public void testLoginActivityMoveToIndex() {
//        startActivity(mLoginIntent, null, null);
        Log.i("TAG", "=====");
//        final Button loginButton = (Button) getActivity().findViewById(R.id.btn_login);
//        // 测试Button的点击事件
//        loginButton.performClick();

//        final Intent intent = getStartedActivityIntent();
//
//        // 去判断是否为空,如果为空就说明跳转失败
//        assertNotNull("Intent was null", intent);
//
//        // 这一句是判断你在跳转后有没调finish()
//        assertTrue(isFinishCalled());

    }
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    @MediumTest
    public void test_add() {
        Log.i("TAG", "=====");
    }

    public void sta() {

    }
}
