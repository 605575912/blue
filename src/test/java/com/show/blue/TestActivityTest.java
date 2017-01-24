package com.show.blue;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class)
public class TestActivityTest {
    @Rule
    public ShowTestRule showTestRule = new ShowTestRule();

    @Before
    public void setUps() {
//        activity = Robolectric.setupActivity(TestActivity.class);

    }


//    @Test
//    public void loginSuccess() {
////        emailView.setText("zhangzhan35@gmail.com");
////        passwordView.setText("123");
////        button.performClick();
//
////        ShadowApplication application = ShadowApplication.getInstance();
////        assertThat("Next activity has started", application.getNextStartedActivity(), is(notNullValue()));
//    }

    @Test
    public void loginWithEmptyUsernameAndPassword() {

//        button.performClick();
//        double density = activity.getdensity(activity);
//        System.out.println("density 大小：" + density);
//        ShadowApplication application = ShadowApplication.getInstance();
////        assertThat("Next activity should not started", application.getNextStartedActivity(), is(nullValue()));
////        MatcherAssert.assertThat(emailView.getError(), eq);
////        assertThat("error", activity.getdensity(activity), is(6.0f));
////        assertThat("Show error for Password field ", passwordView.getError(), is(notNullValue()));
//
//        assertEquals("高波错了", textView.getText().toString(), "");

        UserManager mockUserManager = Mockito.spy(new UserManager());//  默认不干活
        LoginPresenter loginPresenter = new LoginPresenter();
        loginPresenter.setUserManager(mockUserManager);  //<==
//先创建一个mock对象
        PasswordValidator mockValidator = Mockito.spy(new PasswordValidator());//真实方法
        loginPresenter.setPasswordValidator(mockValidator);

//当调用mockValidator的verifyPassword方法，同时传入"xiaochuang_is_handsome"时，返回true
        Mockito.when(mockValidator.verifyPassword(Mockito.anyString())).thenReturn(false);

        loginPresenter.login("xiaochuang", "");


//        mockUserManager.performLogin("xiaochuang", "xiaochuang password");
        Mockito.verify(mockUserManager, Mockito.times(1)).performLogin("xiaochuang", "");
//        assertEquals(emailView.getError().toString(), application.getString(R.string.error_field_required));
    }

//    @Test
//    public void loginFailure() {
//        emailView.setText("invalid@email");
//        passwordView.setText("invalidpassword");
//        button.performClick();
//
//        ShadowApplication application = ShadowApplication.getInstance();
//        assertThat("Next activity should not started", application.getNextStartedActivity(), is(nullValue()));
//        assertThat("Show error for Email field ", emailView.getError(), is(notNullValue()));
//        assertThat("Show error for Password field ", passwordView.getError(), is(notNullValue()));
//    }
}
