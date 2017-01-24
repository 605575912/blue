//package com.show.blue.home;
//
//import android.os.Bundle;
//import android.os.Message;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentTransaction;
//import android.view.KeyEvent;
//import android.view.View;
//import android.widget.RelativeLayout;
//
//import com.library.uiframe.BaseFragmentActivity;
//import com.show.blue.BlueApp;
//import com.show.blue.R;
//import com.show.blue.home.discover.DiscoverFragment;
//import com.show.blue.home.myfriend.MyfriendFragment;
//import com.show.blue.version.UpdateManager;
//import com.support.loader.utils.AppManager;
//import com.support.loader.utils.LogBlue;
//
///**
// * Created by liangzhenxiong on 16/4/4.
// */
//public class HomeActivity extends BaseFragmentActivity implements View.OnClickListener {
//    HomeFragment homeFragment;
//    DiscoverFragment discoverFrament;
//    MyfriendFragment myfriendFramgent;
//    RelativeLayout re_0, re_1, re_2, re_3;
//    FragmentTransaction fragmentTransaction;
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (BlueApp.value == -1) {
//
//            LogBlue.i("TAG", "value==-1=" + BlueApp.value);
//            BlueApp.value = 0;
//        } else {
//            LogBlue.i("TAG", "value==0=" + BlueApp.value);
//        }
//        setContentView(R.layout.home_layout);
//        re_0 = (RelativeLayout) findViewById(R.id.re_0);
//        re_1 = (RelativeLayout) findViewById(R.id.re_1);
//        re_2 = (RelativeLayout) findViewById(R.id.re_2);
//        re_3 = (RelativeLayout) findViewById(R.id.re_3);
//        re_0.setOnClickListener(this);
//        re_1.setOnClickListener(this);
//        re_2.setOnClickListener(this);
//        re_3.setOnClickListener(this);
//        setTabSelection(0);
//        update();
//        String mtyb= android.os.Build.BRAND;//手机品牌
//        if ("Huawei".equalsIgnoreCase("huawei")){
//            LogBlue.i("TAG", mtyb+"model");
//        }
//        LogBlue.i("TAG", mtyb+"model==0=");
//    }
//
//
//    void update() {
//
//    }
//
//    void setTabSelection(int index) {
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentTransaction = fragmentManager.beginTransaction();
//        hideFragments(fragmentTransaction);
//        switch (index) {
//            case 0: {
//                if (homeFragment == null) {
//                    homeFragment = new HomeFragment();
//
//                    fragmentTransaction.add(R.id.l_view, homeFragment);
//                } else {
//                    fragmentTransaction.show(homeFragment);
//                }
//            }
//            break;
//            case 1: {
//                if (discoverFrament == null) {
//                    discoverFrament = new DiscoverFragment();
//
//                    fragmentTransaction.add(R.id.l_view, discoverFrament);
//                } else {
//                    fragmentTransaction.show(discoverFrament);
//                }
//            }
//            break;
//            case 2: {
//                if (myfriendFramgent == null) {
//                    myfriendFramgent = new MyfriendFragment();
//
//                    fragmentTransaction.add(R.id.l_view, myfriendFramgent);
//                } else {
//                    fragmentTransaction.show(myfriendFramgent);
//                }
//            }
//            break;
//            case 3: {
//                if (mycenterFragment == null) {
//                    mycenterFragment = new MycenterFragment();
//
//                    fragmentTransaction.add(R.id.l_view, mycenterFragment);
//                } else {
//                    fragmentTransaction.show(mycenterFragment);
//                }
//            }
//            break;
//        }
//        fragmentTransaction.commit();
//    }
//
//    /**
//     * 将所有的Fragment都置为隐藏状态。
//     *
//     * @param transaction 用于对Fragment执行操作的事务
//     */
//    private void hideFragments(FragmentTransaction transaction) {
//        if (homeFragment != null) {
//            transaction.hide(homeFragment);
//        }
//        if (discoverFrament != null) {
//            transaction.hide(discoverFrament);
//        }
//        if (myfriendFramgent != null) {
//            transaction.hide(myfriendFramgent);
//        }
//        if (mycenterFragment != null) {
//            transaction.hide(mycenterFragment);
//        }
//    }
//
//    @Override
//    public void handleMessage(Message msg) {
//        super.handleMessage(msg);
//
//
//    }
//
//    @Override
//    public void onClick(View v) {
//        int id = v.getId();
//        if (id == R.id.re_0) {
//            setTabSelection(0);
//        } else if (id == R.id.re_1) {
//            setTabSelection(1);
//        } else if (id == R.id.re_2) {
//            setTabSelection(2);
//        } else if (id == R.id.re_3) {
//            setTabSelection(3);
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        LogBlue.i("TAG", "Activity===onDestroy");
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            AppManager.getAppManager().AppExit(this);
//            return false;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//
//}
