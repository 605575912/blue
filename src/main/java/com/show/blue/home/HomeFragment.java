//package com.show.blue.home;
//
//import android.os.Bundle;
//import android.os.Message;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.WindowManager;
//import android.widget.AbsListView;
//import android.widget.AdapterView;
//import android.widget.ListView;
//
//import com.example.gaussianblur.blur.BlurActivity;
//import com.library.uiframe.BaseFragment;
//import com.show.blue.R;
//import com.show.blue.home.flippable.FlippableActivity;
//import com.show.blue.home.stackview.StackActivity;
//import com.support.loader.ServiceLoader;
//import com.support.loader.adapter.ItemData;
//import com.support.loader.adapter.UIListAdapter;
//import com.support.loader.packet.LoaderType;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by liangzhenxiong on 16/4/4.
// */
//public class HomeFragment extends BaseFragment {
//    ListView list_view;
//    UIListAdapter adapter;
//    List<ItemData> list;
//    private View rootView;
//    WindowManager.LayoutParams param;
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        if (null != rootView) {
//            ViewGroup parent = (ViewGroup) rootView.getParent();
//            if (null != parent) {
//                parent.removeView(rootView);
//            }
//        } else {
//            rootView = inflater.inflate(R.layout.home_tab1_layout, null);
//            list_view = (ListView) rootView.findViewById(R.id.list_view);
//            list = new ArrayList<ItemData>();
//            adapter = new UIListAdapter(getActivity(), list);
//            list_view.setAdapter(adapter);
//            list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    ItemData itemData = (ItemData) parent.getAdapter().getItem(position);
//                    itemData.onItemClick(parent, view, position, getActivity());
////                finish();
////                Intent intent = getPackageManager().getLaunchIntentForPackage("com.aspire.mm");
////                intent.setAction(Intent.ACTION_VIEW);
//////                intent.putExtra("targeturl", "mm://index");
////                intent.putExtra("webadverurl", "http://www.baidu.com");
////                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
////                startActivity(intent);
//
//                }
//            });
//            list_view.setOnScrollListener(new AbsListView.OnScrollListener() {
//                @Override
//                public void onScrollStateChanged(AbsListView absListView, int i) {
//
//                    for (ItemData itemData : list) {
//                        itemData.onScrollStateChanged(i);
//                    }
//                    adapter.notifyDataSetChanged();
//                }
//
//                @Override
//                public void onScroll(AbsListView absListView, int i, int i1, int i2) {
//                }
//            });
//            checkUpdate();
//
////            WindowManager windowManager = (WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE);
////            //设置LayoutParams(全局变量）相关参数
////            param = new WindowManager.LayoutParams();
////
////            param.type = WindowManager.LayoutParams.TYPE_TOAST;     // 系统提示类型,重要
////            param.format = 1;
////            param.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE; // 不能抢占聚焦点
////            param.flags = param.flags | WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH;
////            param.flags = param.flags | WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS; // 排版不受限制
////
////            param.alpha = 1.0f;
////
////            param.gravity = Gravity.LEFT | Gravity.TOP;   //调整悬浮窗口至左上角
////            //以屏幕左上角为原点，设置x、y初始值
////            param.x = 0;
////            param.y = 0;
////
//////            Display display = windowManager.getDefaultDisplay();
////            //设置悬浮窗口长宽数据
////            param.width = WindowManager.LayoutParams.MATCH_PARENT;
////            param.height = WindowManager.LayoutParams.MATCH_PARENT;
////
////            View view = LayoutInflater.from(getActivity()).inflate(R.layout.window_layout, null);
////            Button button8 = (Button) view.findViewById(R.id.button8);
////            button8.setOnClickListener(new View.OnClickListener() {
////                @Override
////                public void onClick(View view) {
////                    ShowToast.showTips(getActivity(),"1");
////                }
////            });
////            windowManager.addView(view, param);
//        }
//        return rootView;
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//    }
//
//    public void checkUpdate() {
//        Object o = ServiceLoader.getInstance().getListenerLoader(LoaderType.LOAD_HOMEDATA);
//        if (o == null) {
//            ServiceLoader.getInstance().setListenerLoader(LoaderType.LOAD_HOMEDATA, new HomeDataListener() {
//                @Override
//                public void update(MainItem mainItem) {
////                    VersionHandler(version);
//                }
//            });
//        } else if (o instanceof MainItem) {
//            Message msg = handler.obtainMessage(1);
//            msg.obj = o;
//            handler.sendMessage(msg);
//        }
//    }
//
//    ItemData getItem(Item item) {
//        if (item == null) {
//            return null;
//        }
//        switch (item.getType()) {
//            case 0: {
//                BannerItemData bannerItemData = new BannerItemData();
//                bannerItemData.setItem(item);
//                bannerItemData.setAclass(BlurActivity.class);
//                return bannerItemData;
//            }
//            case 1: {
//                RecommendItemData recommendItemData = new RecommendItemData();
//                recommendItemData.item = item;
//                recommendItemData.setAclass(FlippableActivity.class);
//                return recommendItemData;
//            }
//            case 2: {
//                RecommendItemData recommendItemData = new RecommendItemData();
//                recommendItemData.item = item;
//                recommendItemData.setAclass(StackActivity.class);
//                return recommendItemData;
//            }
//            case 3: {
//                RecommendItemData recommendItemData = new RecommendItemData();
//                recommendItemData.item = item;
//                recommendItemData.setAclass(CardViewActivity.class);
//                return recommendItemData;
//            }
//        }
//
//        return null;
//    }
//
//    @Override
//    public void handleMessage(Message msg) {
//        super.handleMessage(msg);
//        if (msg.what == 1) {
//            MainItem version = (MainItem) msg.obj;
//            if (version == null) {
//                return;
//            }
//            if (version.arraydata == null) {
//                return;
//            }
//            for (Item item : version.arraydata) {
//                ItemData mainItemData = getItem(item);
//                if (mainItemData != null) {
//                    list.add(mainItemData);
//                }
//            }
//
////            list.add(getItem("flyme应用中心 详情页", BlurActivity.class));
////            list.add(getItem("矢量文字，字体", IconfontActivity.class));
////            list.add(getItem("SVG", SVGMainActivity.class));
////            list.add(getItem("动态SVG", SecondActivity.class));
////            list.add(getItem("APK包注释", FishActivity.class));
////            list.add(getItem("VIEWG", SVGMainActivity.class));
//            adapter.notifyDataSetChanged();
//        }
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
//}
