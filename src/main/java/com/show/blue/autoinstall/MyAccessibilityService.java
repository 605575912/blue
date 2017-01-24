package com.show.blue.autoinstall;


import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.accessibility.AccessibilityNodeInfoCompat;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.support.loader.utils.ReflectHelper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

/**
 * 2015.12.4 三星A8000机的经验教训：MyAccessibilityService为辅助服务，客户端传参数给该服务器，不能通过startService传，否则在系统设置界面关闭
 * 该功能时，在unBind之后，整个客户端的所有进程会被杀掉。解决这个问题的办法只好通过广播来解决。
 * @author lhy
 *
 */
@SuppressLint("NewApi")
public class MyAccessibilityService extends AccessibilityService implements Runnable {
	public static final String TAG = "MyAccessibilityService";
    	private final static String ACTION_INSTALLING = "action.installing.packages"; //开始安装
    	private final static String ACTION_INSTALL_DONE = "action.install.done"; //结束安装
    	
    	
    	private final static String ACTION_INSTALL_PACKAGE = "action.install.apackage";
    	private final static String ACTION_INSTALL_PACKAGES = "action.install.packages";
    	private final static String KEY_APK_PATH = "key.apk.path"; //待安装的应用文件路径
    	private final static String KEY_APK_PATHS = "key.apk.paths"; //批量安装时的应用文件路径
    	private final static int	INSTALL_EXPIRED_TIME = 300*1000; //安装超时时间，毫秒
    	private final static int	WAIT_NEXT_OP_EXPIRED_TIME = 60*1000; //安装完成之后，等下一操作的操时时间
    	
	public static final int TYPE_KILL_APP = 1;
	public static final int TYPE_INSTALL_APP = 2;
	public static final int TYPE_UNINSTALL_APP = 3;
	public static int INVOKE_TYPE = TYPE_INSTALL_APP;
	private final String UNINSTALLERACTIVITY = "com.android.packageinstaller.UninstallerActivity";
	private final String UNINSTALLAPPPROGRESS = "com.android.packageinstaller.UninstallAppProgress";
	private Set<Integer> UNINSTALL_WINDOWIDS = new HashSet<Integer>();
	private Handler mHandler ;
	private boolean			mServiceConnected = false;
	private boolean			mUnbound = false;
	private Vector<InstallTask> mInstallTasks = new Vector<InstallTask>(); //记录任务活动时间
	private boolean			mAllowToIntall = false; //是否可以自动装
	private long				mInstallDoneTime = 0 ;//记录安装完成时的时间，在安装完成后的一段时间内，允许操作诸如权限管理界面
	private BroadcastReceiver mReceiver ;
	private final static String TEXT_OK = "确定";
	private final static String TEXT_INSTALL = "安装";
	private final static String TEXT_CANCEL = "取消";
	private final static String TEXT_DELETE = "删除";
	private final static String TEXT_DELFILE = "删除安装包";
	private final static String TEXT_NEXT = "下一步";
	private final static String TEXT_END = "完成";
	private final static String TEXT_OPEN = "打开";
	private final static String TEXT_ACCEPT = "应用程序许可";
	private final static String TEXT_CONFIRM = "确认";
	private final static String TEXT_FORCESTOP = "强行停止";
	private final static int mServiceTag = 3;

	
	static class InstallTask{
	    String apkpath; //安装包
	    long	activetime ; //安装活动时间
	    InstallTask(String path, long time){
		apkpath = path ;
		activetime = time ;
	    }
	}
	
	static enum OperCode{
	    AND  , OR
	}
	
	private static String[]	PACKAGE_INSTALLER = {"com.android.packageinstaller" //系统安装包
	};
	private static String[]	GRANT_PERM_PACKAGES = {"com.sonymobile.cta",//Sony的权限管理包，安装完成后会调出来
	    	"com.sonymobile.cta",//Sony的权限管理包，安装完成后会调出来
		"com.lenovo.safecenter", //联想安全中心
		"com.lenovo.security", //联想安全
		"com.xiaomi.gamecenter" ,//小米游戏中心
		"com.sec.android.app.capabilitymanager", //2015.12.3 Mic:增加三星 A8000机的应用程序权限管理界面
		"com.lewa.permmanager" //TCL 么么达的权限管理界面 -->"完成” "打开"
	}; 
	
	private static String[] SETTING_PACKAGES = {"com.android.settings"};
	
	/**
	 * 判断事件是否为安装器的
	 * @param packageName
	 * @return
	 */
	private static boolean isInstallerEvent(CharSequence packageName){
	    for (String s : PACKAGE_INSTALLER){
		if (s.equals(packageName))
		    return true;
	    }
	    return false;
	}
	
	/**
	 * 判断事件是否为权限管理的
	 * @param packageName
	 * @return
	 */
	private static boolean isGrantPermEvent(CharSequence packageName){
	    for (String s:GRANT_PERM_PACKAGES){
		if (s.equals(packageName)){
		    return true;
		}
	    }
	    return false;
	}
	
	/**
	 * 判断事件是否为设置的
	 * @param packageName
	 * @return
	 */
	private static boolean isSettingPackageEvent(CharSequence packageName){
	    for (String s: SETTING_PACKAGES){
		if (s.equals(packageName)){
		    return true;
		}
	    }
	    return false;
	}
	
	public static void reset(){
		INVOKE_TYPE = TYPE_INSTALL_APP;
	}


	@Override
	public void onCreate() {
	    super.onCreate();
//		keepAlive();
	    mHandler = new Handler(getMainLooper());
	    IntentFilter filter = new IntentFilter(ACTION_INSTALL_DONE);
	    filter.addAction(ACTION_INSTALLING);
	    filter.addAction(ACTION_INSTALL_PACKAGE);
	    filter.addAction(ACTION_INSTALL_PACKAGES);
	    mReceiver = new MyReceiver(this) ;
	    registerReceiver(mReceiver, filter);
	}

//	/**
//	 * 保持辅助功能
//	 */
//	private void keepAlive() {
//		int version = MobileAdapter.getInstance().getVersion();
//		Notification nf = new Notification();
//		nf.flags |= Notification.FLAG_NO_CLEAR;
//		nf.flags |= Notification.FLAG_ONGOING_EVENT;
//		if (version < 18 && version >= 5) {
//			startForeground(
//					NotificationIDCreator.generateNotificationID(NotificationIDCreator.NTCLS_SERVICE, mServiceTag), nf);
//		} else if (version >= 18) {
//			startForeground(0, nf); // 设置为前台服务避免kill，Android4.3及以上需要设置id为0时通知栏才不显示该通知
//			//		Notification nf = new Notification();
////		ApplicationInfo appInfo = getApplicationInfo();
////		nf.icon = appInfo.icon;
////		nf.flags |= Notification.FLAG_AUTO_CANCEL;
////		CharSequence appName = appName = appInfo.packageName;
////		Intent runningIntent = AppBrowserLauncher.getHomePageLaunchIntent(this);
////		PendingIntent pi = PendingIntent.getActivity(this, 0, runningIntent, PendingIntent.FLAG_UPDATE_CURRENT);
////		nf.setLatestEventInfo(this, getString(R.string.app_name), "点击进入" + getString(R.string.app_name), pi);
////		startForeground(
////			NotificationIDCreator.generateNotificationID(NotificationIDCreator.NTCLS_SERVICE, mServiceTag),
////			nf);
//		}
//	}

	/**
 * 三星A8000：不通过startService传参数，否则会在关闭该服务器，引起整个应用被杀！	
 * @param intent
 */
//	@Override
//	public int onStartCommand(Intent intent, int flags, int startId) {
////	    handleIntent(intent);
//	    return super.onStartCommand(intent, flags, startId);
//	}
	
	private void handleIntent(Intent intent){
	     	String action = intent != null ? intent.getAction() : null;
    	    	if (ACTION_INSTALLING.equals(action)){
    	    	    mAllowToIntall = true;
    	    	}else if (ACTION_INSTALL_DONE.equals(action)){
    	    	    mInstallDoneTime = System.currentTimeMillis();
    	    	    mAllowToIntall = false;
    	    	    mInstallTasks.clear();
    	    	}else if (mServiceConnected) {
        	    if (ACTION_INSTALL_PACKAGE.equals(action)) { // 安装一个应用s
        		String str_val = intent.getStringExtra(KEY_APK_PATH);
        		if (!TextUtils.isEmpty(str_val)) {
        		    addInstallTask(str_val, System.currentTimeMillis());
        		}
        	    } else if (ACTION_INSTALL_PACKAGES.equals(action)) { // 批量安装应用
        		String[] paths = intent.getStringArrayExtra(KEY_APK_PATHS);
        		if (paths != null) {
        		    for (String s : paths) {
        			if (!TextUtils.isEmpty(s)) {
        			    addInstallTask(s, System.currentTimeMillis());
        			}
        		    }
        		}
        	    }
        	}
	}

	private void addInstallTask(String apkpath, long time){
	    mAllowToIntall = true;
	    for (InstallTask t: mInstallTasks){
		if (t.apkpath.equals(apkpath)){
		    t.activetime = time ;
		    return ;
		}
	    }
	    mInstallTasks.add(new InstallTask(apkpath, time));
	}
	//删除安装超时时间
	private void removeExpiredInstallTasks(){
	    final int N = mInstallTasks.size() ;
	    if (N == 0){
		return ;
	    }
	    InstallTask t ;
	    long now = System.currentTimeMillis() ;
	    for (int k = N-1; k >= 0; k -- ){
		t = mInstallTasks.get(k );
		if(now - t.activetime > INSTALL_EXPIRED_TIME){
		    mInstallTasks.remove(k);
		}
	    }
	}
	
	private void removeFirstInstallTask(){
	    final int N = mInstallTasks.size() ;
	    if (N == 0){
		return ;
	    }
	    mInstallTasks.remove(0);
	}
	
	private void updateFirstInstallTask(int expiretime){
	    final int N = mInstallTasks.size() ;
	    if (N == 0){
		return ;
	    }
	    InstallTask t = mInstallTasks.get(0);
	    t.activetime = System.currentTimeMillis() - INSTALL_EXPIRED_TIME + expiretime;
	}
	
	private void updateInstallTasks(){
	    final int N = mInstallTasks.size() ;
	    if (N == 0){
		return ;
	    }
	    InstallTask t = null ;
	    for (int k = 0; k < N; k ++){
		t= mInstallTasks.get(k);
		t.activetime = System.currentTimeMillis() ;
	    }
	}
	
	/**
	 * Android 4.0及以前版本没有getServiceInfo方法，只能通过反射获取
	 * @return
	 */
	AccessibilityServiceInfo getServiceInfoCompat(){
	    if (Build.VERSION.SDK_INT < 16){
		Object obj = ReflectHelper.getDeclaredFieldValue(this, AccessibilityService.class.getName(), "mInfo");
		if (obj instanceof AccessibilityServiceInfo)
		    return (AccessibilityServiceInfo) obj;
		else{
		    return null ;
		}
	    }else{
		return (AccessibilityServiceInfo) ReflectHelper.callMethod(this, "getServiceInfo", null, null);
	    }
	}
	
	@Override
	protected void onServiceConnected() {
	    mHandler.removeCallbacks(this);
//	    setAccessibilityOpened(this, true);
	    mServiceConnected = true;
	    mUnbound = false;
	    super.onServiceConnected();
	    AccessibilityServiceInfo svInfo = getServiceInfoCompat();
	    if (svInfo == null){
		svInfo = new AccessibilityServiceInfo() ;
		svInfo.eventTypes = AccessibilityEvent.TYPES_ALL_MASK;
		svInfo.feedbackType = AccessibilityServiceInfo.FEEDBACK_GENERIC;
		svInfo.notificationTimeout = 100;  
	    }
	    svInfo.packageNames = new String[PACKAGE_INSTALLER.length + GRANT_PERM_PACKAGES.length +SETTING_PACKAGES.length ];
	    int k = 0;
	    String[] pkgs = svInfo.packageNames;
	    for (String s: PACKAGE_INSTALLER){
		pkgs[k++] = s ;
	    }
	    for (String s: SETTING_PACKAGES){
		pkgs[k++] = s ;
	    }
	    for (String s: GRANT_PERM_PACKAGES){
		pkgs[k++] = s ;
	    }
	    setServiceInfo(svInfo);
	}

	@Override
	public void onAccessibilityEvent(AccessibilityEvent event) {
	    	if (mAllowToIntall){
	    	    this.processAccessibilityEnvent(event);
	    	}else{ 
//	    	    if (AspLog.isPrintLog){
//	    		AspLog.d(TAG, "PackageName="+event.getPackageName() +",EventType="+
//	    			AccessibilityEvent.eventTypeToString(event.getEventType()) +",Event="+event.toString());
//	    	    }
	    	    if (event.getSource() == null){
//	    		AspLog.d(TAG, "the source = null");
	    		return ;
	    	    }
	    	    long installed_time = System.currentTimeMillis() - mInstallDoneTime;
	    	    if (isGrantPermEvent(event.getPackageName()) && installed_time < 30000 ){ //权限管理通常在安装完成之后隔一段时间才出来，因此需要判断时间戳，不能无限制的允许点击
	    		processGrantPermEvent(event);
	    	    }
	    	}
	}

	private void processAccessibilityEnvent(AccessibilityEvent event) {
//		AspLog.d(TAG, "PackageName="+event.getPackageName() +",EventType="+ AccessibilityEvent.eventTypeToString(event.getEventType()) +",Event="+event.toString());
		if (event.getSource() == null) {
//			AspLog.d(TAG, "the source = null");
		} else {
//			printAllTextNode(event.getSource());
//			AspLog.i(TAG, "INVOKE_TYPE="+INVOKE_TYPE +"--------------------------------------------------------------------------------");
			switch (INVOKE_TYPE) {
			case TYPE_KILL_APP:
				processKillApplication(event);
				break;
			case TYPE_INSTALL_APP:
				processinstallApplication(event);
				break;
			case TYPE_UNINSTALL_APP:
				processUninstallApplication(event);
				break;				
			default:
				break;
			}						
		}
	}

	@Override
	protected boolean onKeyEvent(KeyEvent event) {
		return true;
	}

	@Override
	public void onInterrupt() {
//	    AspLog.d(TAG, "call onInterrupt!");
	}

	@Override
	public boolean onUnbind(Intent intent) {
//	    AspLog.d(TAG, "call onUnbind!");
	    mUnbound = true;
	    return super.onUnbind(intent);
	}

	@Override
	public void onDestroy() {
//	    AspLog.d(TAG, "call onDestroy, ServiceConnected="+mServiceConnected +",unbound="+mUnbound);
	    mServiceConnected = false;
	    unregisterReceiver(mReceiver);
	    super.onDestroy();
	    mHandler.postDelayed(this, 2000); //如果清理进程的话，run()应该不会被调用，利用这点来判断异常关闭“自动装”
	}
	
	@Override
	public void run(){
//	    AspLog.d(TAG, "call run, ServiceConnected=" + mServiceConnected + ",unbound=" + mUnbound);
	    if (mUnbound ){ //只有正常调用onUnbind的才算正常关闭，其他当作异常关闭。
//		setAccessibilityOpened(this, false);
	    }
	}
	
	private void processUninstallApplication(AccessibilityEvent event) {
		
		if (event.getSource() != null) {
			if (isInstallerEvent(event.getPackageName())) {
				List<AccessibilityNodeInfo> ok_nodes = event.getSource().findAccessibilityNodeInfosByText(TEXT_OK);
				performClick(ok_nodes, 0);
				recycleNodes(ok_nodes);
			}
		}
	}

	private void processinstallApplication(AccessibilityEvent event) {
		if (event.getSource() != null/*&&isMMApp(event)==null*/) {
//			if(appname == null)
//				appname=isMMApp(event);
			List<AccessibilityNodeInfo> matched_nodes = null ;
			List<List<AccessibilityNodeInfo>> nodesList = null ;

			boolean perform_click = false;
			removeExpiredInstallTasks() ;
			if (mInstallTasks.size() == 0){ //无安装任务了
//			    AspLog.i(TAG, "processinstallApplication installTasks count = 0");
			    return ;
			}
			if (isInstallerEvent(event.getPackageName())/*event.getPackageName().equals("com.android.packageinstaller")*/
					&& isUninstallWindow(event)) {
				//				List<AccessibilityNodeInfo> appname_nodes = event.getSource().findAccessibilityNodeInfosByText("MM商场");
				AccessibilityNodeInfo rootnodeinfo = getRootAccessibilityNodeInfo(event.getSource());
				int	sleep_time = 200 ;
                		nodesList = getAccessibilityNodeInfosByText(rootnodeinfo, new String[] { TEXT_INSTALL }, OperCode.OR);
                		if (nodesList != null && nodesList.size() > 0) {
							ShowToast.show(getApplication(),"============"+nodesList.size());
                		    perform_click = performClick(nodesList.get(0), sleep_time);
                		    matched_nodes = findNode(nodesList, new String[] { TEXT_INSTALL });
                		    if (perform_click) {
                			if (matched_nodes != null && matched_nodes.size() > 0) {
//                			    AspLog.v(TAG, "auto click install");
//                			    AccessibilityInstallingWindowManager.showInstallingWindow(this);
//                			    saveAppUpdateCount(this);
                			    updateInstallTasks();
                			}
                			recycleNodesList(nodesList);
                			return;
                		    }
                		    recycleNodesList(nodesList);
                		}
				//对话框："删除安装包-->取消、删除"
				nodesList = getAccessibilityNodeInfosByText(rootnodeinfo, new String[]{TEXT_CANCEL, TEXT_DELETE, TEXT_DELFILE}, OperCode.AND);
				if (nodesList != null && nodesList.size() > 0){
				    matched_nodes = findNode(nodesList, new String[]{TEXT_CANCEL});
				    if (matched_nodes != null && matched_nodes.size() > 0){
//					AccessibilityInstallingWindowManager.removeInstallingView(this); //2015.10.12 Mic:撤掉安装中的界面，防止干扰点击
					perform_click = performClick(matched_nodes, sleep_time);
					if (perform_click ){
//					    AspLog.v(TAG, "auto click cancel");
					    recycleNodesList(nodesList);
					    updateInstallTasks();
					    return ;
					}
				    }
				    recycleNodesList(nodesList);
				}

				nodesList = getAccessibilityNodeInfosByText(rootnodeinfo, new String[]{TEXT_NEXT, TEXT_OK, TEXT_END}, OperCode.OR);
				if (nodesList != null && nodesList.size() > 0){
				    matched_nodes = findNode(nodesList, new String[]{TEXT_NEXT});
				    if (matched_nodes != null && matched_nodes.size() > 0){
//					AspLog.v(TAG, "auto click next");
//					AccessibilityInstallingWindowManager.showInstallingWindow(this); //显示安装中的界面
					perform_click = performClick(matched_nodes, sleep_time);
					if (perform_click ){
//					    saveAppUpdateCount(this);
					    updateInstallTasks();
					}
				    }else{
					recycleNodesList(nodesList);
					nodesList = getAccessibilityNodeInfosByText(rootnodeinfo, new String[]{TEXT_END, TEXT_OPEN}, OperCode.AND);
					if (nodesList != null && nodesList.size() > 0){
//					    AccessibilityInstallingWindowManager.removeInstallingView(this);
					    sleep_time = 500 ;
                			    matched_nodes = findNode(nodesList, new String[] { TEXT_END });
                			    perform_click = performClick(matched_nodes, sleep_time);
                			    if (perform_click && matched_nodes != null && matched_nodes.size() > 0) {
//                				saveAppUpdateCount(this);
                				if (mInstallTasks.size() == 1){ //三星SM-G9008W等手机安装完成时，会切出“应用程序许可 确定”界面，需要延时点击关闭它
                				    updateFirstInstallTask(WAIT_NEXT_OP_EXPIRED_TIME); // 设定WAIT_NEXT_OP_EXPIRED_TIME/1000秒后自动任务超时
                				}else{
                				    removeFirstInstallTask();
                				}
//                				isclickinstall = false;
                			    }
					}else {
					    nodesList = getAccessibilityNodeInfosByText(rootnodeinfo, new String[]{TEXT_OK}, OperCode.OR);
					    if (nodesList != null && nodesList.size() > 0){
						perform_click = performClicks(nodesList, sleep_time);
						if (perform_click){
//						    saveAppUpdateCount(this);
						    updateFirstInstallTask(WAIT_NEXT_OP_EXPIRED_TIME); // 设定WAIT_NEXT_OP_EXPIRED_TIME/1000秒后自动任务超时
						}
					    }else{
						nodesList = getAccessibilityNodeInfosByText(rootnodeinfo, new String[]{TEXT_END}, OperCode.OR);
						 if (nodesList != null && nodesList.size() > 0){ //应对安装失败的情形
							perform_click = performClicks(nodesList, sleep_time);
							if (perform_click){
//							    saveAppUpdateCount(this);
							    if (mInstallTasks.size() == 1){//三星SM-G9008W等手机安装完成时，会切出“应用程序许可 确定”界面，需要延时点击关闭它
								updateFirstInstallTask(WAIT_NEXT_OP_EXPIRED_TIME/1000); // 设定3秒后自动任务超时
							    }else{
								removeFirstInstallTask();
							    }
							}
						 }
					    }
					}
				    }
				    recycleNodesList(nodesList);
				    if(perform_click){
					return ;
				    }
				}
			}else if (isGrantPermEvent(event.getPackageName())){
			    processGrantPermEvent(event);
			} else if (isSettingPackageEvent(event.getPackageName())){ //设置界面
			    AccessibilityNodeInfo rootnodeinfo = getRootAccessibilityNodeInfo(event.getSource());
			    nodesList = getAccessibilityNodeInfosByText(rootnodeinfo, new String[]{TEXT_ACCEPT, TEXT_CONFIRM, TEXT_OK}, OperCode.OR);
			    if (nodesList != null && nodesList.size() > 0){
				perform_click = performClicks(nodesList, 0);
				if (perform_click){
				    updateFirstInstallTask(WAIT_NEXT_OP_EXPIRED_TIME); //设定WAIT_NEXT_OP_EXPIRED_TIME/1000秒后自动任务超时
//				    AspLog.i(TAG, "perform [TEXT_CONFIRM]");
				}
			    }
			}
			if (nodesList != null){
			    recycleNodesList(nodesList);
			}
		}
	}

	private void processGrantPermEvent(AccessibilityEvent event){
	    List<List<AccessibilityNodeInfo>> nodesList = null;
	    boolean perform_click = false;
	    AccessibilityNodeInfo rootnodeinfo = getRootAccessibilityNodeInfo(event.getSource());
	    nodesList = getAccessibilityNodeInfosByText(rootnodeinfo, new String[]{TEXT_OK, TEXT_CONFIRM,TEXT_END}, OperCode.OR);
	    //大部分为"确定"
	    //TC的么么达按钮为“完成”“打开"
	    if (nodesList != null && nodesList.size() > 0){ //三星的应用程序权限管理界面的按钮为“确认"
		perform_click = performClick(nodesList.get(0), 0);
		if (perform_click){
		    updateFirstInstallTask(WAIT_NEXT_OP_EXPIRED_TIME); //设定WAIT_NEXT_OP_EXPIRED_TIME/1000秒后自动任务超时
//		    AspLog.i(TAG, "perform [TEXT_OK|TEXT_CONFIRM]");
		}
	    }
	}
	
	private void processKillApplication(AccessibilityEvent event) {
		
		if (event.getSource() != null) {
			if (isSettingPackageEvent(event.getPackageName())/*event.getPackageName().equals("com.android.settings")*/) {
				List<AccessibilityNodeInfo> nodes = event.getSource().findAccessibilityNodeInfosByText(TEXT_FORCESTOP);
				boolean perform_click = false;
				perform_click = performClick(nodes, 0);
				recycleNodes(nodes);
				if (!perform_click){
				    nodes = event.getSource().findAccessibilityNodeInfosByText(TEXT_OK);
				    perform_click = performClick(nodes, 0);
				    recycleNodes(nodes);
				    if (perform_click){
//					AspLog.i(TAG, "processKillApplication performClick TEXT_OK");
				    }
				}else{
//				    AspLog.i(TAG, "processKillApplication performClick TEXT_FORCESTOP");
				}
				
//				List<AccessibilityNodeInfo> stop_nodes = event.getSource().findAccessibilityNodeInfosByText("强行停止");
//				if (stop_nodes!=null && !stop_nodes.isEmpty()) {
//					AccessibilityNodeInfo node;
//					for(int i=0; i<stop_nodes.size(); i++){
//						node = stop_nodes.get(i);
//						if (node.getClassName().equals("android.widget.Button")) {
//							if(node.isEnabled()){
//							   node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//							}
//						}
//					}
//				}
//
//				List<AccessibilityNodeInfo> ok_nodes = event.getSource().findAccessibilityNodeInfosByText("确定");
//				if (ok_nodes!=null && !ok_nodes.isEmpty()) {
//					AccessibilityNodeInfo node;
//					for(int i=0; i<ok_nodes.size(); i++){
//						node = ok_nodes.get(i);
//						if (node.getClassName().equals("android.widget.Button")) {
//							node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
//							Log.d("action", "click ok");
//						}
//					}
//
//				}
			}
		}
	}

    private static boolean performClicks(final List<List<AccessibilityNodeInfo>> nodesList, int sleep_time){
	if (nodesList == null){
	    return false;
	}
	for (List<AccessibilityNodeInfo> n: nodesList){
	    if (performClick(n, sleep_time)){
		return true;
	    }
	}
	return false;
    }

	private static boolean performClick(final List<AccessibilityNodeInfo> nodes, int sleep_time) {
		boolean performed = false;
		if (nodes != null && !nodes.isEmpty()) {
			AccessibilityNodeInfo node;
			for (int i = 0; i < nodes.size(); i++) {
				node = nodes.get(i);
				performed = getClickableAction(node,sleep_time);
				if (performed){
					break;
				}
			}
		} else {
//			AspLog.i(TAG, "performClick no find");
		}
		return performed;
	}

	/**
	 * 查找可点击控件
	 * @param node
	 * @param sleep_time
	 * @return
	 */
	static boolean getClickableAction(AccessibilityNodeInfo node,int sleep_time) {
		if (node == null) {
			return false;
		}
		if (node.isClickable() && node.isEnabled()) {
			if (sleep_time > 0) {
				try {
					Thread.sleep(sleep_time);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			node.performAction(AccessibilityNodeInfoCompat.ACTION_ACCESSIBILITY_FOCUS);
			return node.performAction(AccessibilityNodeInfoCompat.ACTION_CLICK);

		} else {
			node = node.getParent();
			return getClickableAction(node,sleep_time);
		}
	}


	private static void recycleNodes(final List<AccessibilityNodeInfo> nodes ) {
	if (nodes == null){
	    return ;
	}
	for (AccessibilityNodeInfo n: nodes){
	    n.recycle();
	}
	nodes.clear();
    }
    
    private static void recycleNodesList(final List<List<AccessibilityNodeInfo>> nodesList){
	if (nodesList == null){
	    return ;
	}
	for (List<AccessibilityNodeInfo > l : nodesList){
	    recycleNodes(l);
	}
	nodesList.clear();
    }
    
    /**
     * 启动安装，从此开始响应自动装
     * @param context
     */
    public static void startIntalling(Context context){
//	Intent intent = new Intent(ACTION_INSTALLING);
//	intent.setClass(context, MyAccessibilityService.class);
//	context.startService(intent);
//三星A8000不通过startService传参数	
	Intent intent = new Intent(ACTION_INSTALLING);
	intent.setPackage(context.getPackageName());
	context.sendBroadcast(intent);
    }
    
    /**
     * 结束安装，从此不再响应自动装
     * @param context
     */
    public static void installDone(Context context){
//	Intent intent = new Intent(ACTION_INSTALL_DONE);
//	intent.setClass(context, MyAccessibilityService.class);
//	context.startService(intent);
//	三星A8000不通过startService传参数	
	Intent intent = new Intent(ACTION_INSTALL_DONE);
	intent.setPackage(context.getPackageName());
	context.sendBroadcast(intent);
    }
    /**
     * 告诉我要安装了
     * @param context
     * @param apkpath
     */
    public static void tellMe2InstallApp(Context context, String apkpath){
//	Intent intent = new Intent(ACTION_INSTALL_PACKAGE);
//	intent.setClass(context, MyAccessibilityService.class);
//	intent.putExtra(KEY_APK_PATH, apkpath);
//	context.startService(intent);
//	三星A8000不通过startService传参数	
	Intent intent = new Intent(ACTION_INSTALL_PACKAGE);
	intent.setPackage(context.getPackageName());
	intent.putExtra(KEY_APK_PATH, apkpath);
	context.sendBroadcast(intent);	
    }
    
    public static void tellMe2InstallApps(Context context, List<Intent> install_intents){
	if (install_intents == null || install_intents.size() == 0){
	    return ;
	}
	String[] apkpaths = new String[install_intents.size()];
	Uri uri ;
	int k = 0; 
	for (Intent it: install_intents){
	    uri = it.getData() ;
	    apkpaths[k++] = uri.getPath() ;
	}
	tellMe2InstallApps(context, apkpaths);
    }
    
    public static void tellMe2InstallApps(Context context, String[]apkpaths){
//	Intent intent = new Intent(ACTION_INSTALL_PACKAGES);
//	intent.setClass(context, MyAccessibilityService.class);
//	intent.putExtra(KEY_APK_PATHS, apkpaths);
//	context.startService(intent);
//	三星A8000不通过startService传参数	
	Intent intent = new Intent(ACTION_INSTALL_PACKAGES);
	intent.setPackage(context.getPackageName());
	intent.putExtra(KEY_APK_PATHS, apkpaths);
	context.sendBroadcast(intent);
    }
	
//	public static void addMMInstallCount(Context context,int count){
//		int mode = AspireUtils.getMODE_MULTI_PROCESS();
//		count += getMMInstallCount(context);
//		SharedPreferences preferences = CryptSharedPreferences.getSharedPreferences(context,MMProviderField.PREF_NAME, mode);
//	    Editor editor = preferences.edit();
//	    editor.putInt("mminstallcount", count);
//	    AspLog.v(TAG, "addcount mminstallcount = "+count);
//	    editor.commit();
//	} 
//	
//	public static void setMMInstallCount(Context context,int count){
//		int mode = AspireUtils.getMODE_MULTI_PROCESS();
//		SharedPreferences preferences = CryptSharedPreferences.getSharedPreferences(context,MMProviderField.PREF_NAME, mode);
//	    Editor editor = preferences.edit();
//	    editor.putInt("mminstallcount", count);
//	    AspLog.v(TAG, "setcount mminstallcount = "+count);
//	    editor.commit();
//	} 
//	
//	public static int getMMInstallCount(Context context){
//		int mode = AspireUtils.getMODE_MULTI_PROCESS();
//	    SharedPreferences preferences = CryptSharedPreferences.getSharedPreferences(context,MMProviderField.PREF_NAME, mode);
//	    int count = preferences.getInt("mminstallcount", 0);
//	    AspLog.v(TAG, "getcount mminstallcount = "+count);
//	    return count;
//	}
//	
//	public static void decreaseInstallCount(Context context){
//		int count = getMMInstallCount(context);
//		setMMInstallCount(context, count-1);
//	}
		
//	public static void setIsMMInstall(Context context,boolean flag){
//		int mode = AspireUtils.getMODE_MULTI_PROCESS();
//	    SharedPreferences preferences = CryptSharedPreferences.getSharedPreferences(context,MMProviderField.PREF_NAME, mode);
//	    Editor editor = preferences.edit();
//	    editor.putBoolean("ismminstall", flag);
//	    editor.commit();
//	    AspLog.v(TAG, "ismminstall = "+flag);
//	}
//
//	public static boolean getIsMMInstall(Context context){
//    	int mode = AspireUtils.getMODE_MULTI_PROCESS();
//	    SharedPreferences preferences = CryptSharedPreferences.getSharedPreferences(context,MMProviderField.PREF_NAME, mode);
//	    return preferences.getBoolean("ismminstall", false);
//	}
//
//	public static void saveAppUpdateCount(Context context){
//    	int mode = AspireUtils.getMODE_MULTI_PROCESS();
//	    SharedPreferences preferences = CryptSharedPreferences.getSharedPreferences(context,MMProviderField.PREF_NAME, mode);
//	    Editor editor = preferences.edit();
//	    int clickcount = getAppUpdateCount(context)+1;
//	    editor.putInt("clickcount", clickcount);
//	    editor.commit();
//	    AspLog.v(TAG, "clickcount = "+clickcount);
//    }
//
//    public static int getAppUpdateCount(Context context){
//    	int mode = AspireUtils.getMODE_MULTI_PROCESS();
//	    SharedPreferences preferences = CryptSharedPreferences.getSharedPreferences(context,MMProviderField.PREF_NAME, mode);
//	    return preferences.getInt("clickcount", 0);
//    }
//
//    public static void setIsShowAccessibilityDialog(Context context,boolean flag){
//		int mode = AspireUtils.getMODE_MULTI_PROCESS();
//	    SharedPreferences preferences = CryptSharedPreferences.getSharedPreferences(context,MMProviderField.PREF_NAME, mode);
//	    Editor editor = preferences.edit();
//	    editor.putBoolean("accessibilitydialog.isshow", flag);
//	    editor.commit();
//    }
//
//    public static boolean getIsShowAccessibilityDialog(Context context){
//    	int mode = AspireUtils.getMODE_MULTI_PROCESS();
//	    SharedPreferences preferences = CryptSharedPreferences.getSharedPreferences(context,MMProviderField.PREF_NAME, mode);
//	    return preferences.getBoolean("accessibilitydialog.isshow", false);
//    }
    
    static class AccInfo{ //允许被混淆，故不需要实现IProguard接口
	int	pid ;
    }
    
//    public static boolean accessibilityHaveOpened(Context context){
////    	int mode = AspireUtils.getMODE_MULTI_PROCESS();
////	    SharedPreferences preferences = CryptSharedPreferences.getSharedPreferences(context,MMProviderField.PREF_NAME, mode);
////	    return preferences.getBoolean("accessibility.open", false);
//	String path = StorageSelector.getInstance().getCacheDirectory() + File.separator + "accessibility.open";
//	File file = new File(path);
//	return file.exists();
//    }
    
//    /**
//     * 改用直接写文件来替代SharedPreference，原因是SharedPreference用线程调度来写文件，可能会造成更新滞后
//     * @param context
//     * @param opened
//     */
//    public static void setAccessibilityOpened(Context context, boolean opened){
//	String path = StorageSelector.getInstance().getCacheDirectory() + File.separator + "accessibility.open";
//	File file = new File(path);
//	if (opened){
//	    AccInfo ai = new AccInfo();
//	    ai.pid = android.os.Process.myPid();
//	    AspireUtils.saveJsonToFile(ai, file);
//	}else{
//	    file.delete();
//	}
////    	int mode = AspireUtils.getMODE_MULTI_PROCESS();
////	    SharedPreferences preferences = CryptSharedPreferences.getSharedPreferences(context,MMProviderField.PREF_NAME, mode);
////	    Editor editor = preferences.edit();
////	    editor.putBoolean("accessibility.open", opened);
////	    editor.commit();
//    }

	public static String isMMApp(AccessibilityEvent event) {
//		for (String appname : PackageUtil.AUTOINSTALL_APPNAMES) {
		String appname = "MM商场";
			if (event != null && event.getSource() != null && appname != null && event.getSource().findAccessibilityNodeInfosByText(appname) != null
					&& event.getSource()
							.findAccessibilityNodeInfosByText(appname).size() > 0)
				return appname;
//		}
		return null;
	}
	
	private static void printAllTextNode(final AccessibilityNodeInfo node){
	    if (node == null){
		return ;
	    }
	    CharSequence label = node.getText() ;
	    if (!TextUtils.isEmpty(label)){
//		AspLog.i(TAG, "pkgname="+node.getPackageName() +",label deep="+getNodeDeep(node, null) +",text="+label);
	    }
	    int count = node.getChildCount() ;
	    AccessibilityNodeInfo childNode = null ;
	    for(int k = 0; k < count; k ++){
		childNode = node.getChild(k);
		printAllTextNode(childNode);
	    }
	}
	
	public static AccessibilityNodeInfo getRootAccessibilityNodeInfo(
			AccessibilityNodeInfo paramAccessibilityNodeInfo) {
		while (true) {
			AccessibilityNodeInfo localAccessibilityNodeInfo = paramAccessibilityNodeInfo != null ? paramAccessibilityNodeInfo.getParent() : null;
			if ((localAccessibilityNodeInfo == null)
					|| (localAccessibilityNodeInfo == paramAccessibilityNodeInfo))
				return paramAccessibilityNodeInfo;
			paramAccessibilityNodeInfo = localAccessibilityNodeInfo;
		}
	}
	
	private static int getNodeDeep(final AccessibilityNodeInfo fromNode, final AccessibilityNodeInfo toNode){
	    if (fromNode == null || fromNode == toNode){
		return 0 ;
	    }
	    int deep = 0 ;
	    AccessibilityNodeInfo parent = fromNode.getParent() ;
	    while(parent != null){
		deep ++ ;
		if (parent == toNode){
		    return deep ;
		}
		parent = parent.getParent() ;
	    }
	    return deep; 
	}
	
//	public static List<AccessibilityNodeInfo> findAccessibilityNodeInfoByText(AccessibilityNodeInfo fromNodeInfo, String nodeLabel)  {
//	    if (fromNodeInfo == null){
//		return null ;
//	    }
//	    List<AccessibilityNodeInfo> nodeList = getAccessibilityNodeInfoByText(fromNodeInfo, nodeLabel);
//	    if (nodeList == null || nodeList.size() == 0){
//		int child_count = fromNodeInfo.getChildCount() ;
//		AccessibilityNodeInfo child_node = null ;
//		for (int k = 0; k < child_count; k ++){
//		    child_node = fromNodeInfo.getChild(k);
//		    nodeList = findAccessibilityNodeInfoByText(child_node, nodeLabel);
//		    if (nodeList != null && nodeList.size() > 0){
//			return nodeList ;
//		    }
//		}
//	    }
//	    return nodeList ;
//	}
	
	private static List<AccessibilityNodeInfo> findNode(List<List<AccessibilityNodeInfo>> nodeLists, String[] node_labels) {
	    if (nodeLists == null){
		return null ;
	    }
	    CharSequence label ;
	    for (List<AccessibilityNodeInfo> l: nodeLists){
		for (AccessibilityNodeInfo n: l){
		    label = n.getText() ;
		    if (!TextUtils.isEmpty(label)){
			for (String c: node_labels){
			    if (c.equals(label.toString())){
				return l ;
			    }
			}
		    }
		}
	    }
	    return null ;
	}
	
	private static List<List<AccessibilityNodeInfo>> getAccessibilityNodeInfosByText(AccessibilityNodeInfo fromNodeInfo, String[] node_labels, OperCode opcode){
	    List<AccessibilityNodeInfo> localList = null ;
	    List<List<AccessibilityNodeInfo>> lists = null ;
	    if (opcode == OperCode.AND || opcode == OperCode.OR){
		lists = new ArrayList<List<AccessibilityNodeInfo>>();
		for (String s: node_labels){
		   localList = getAccessibilityNodeInfoByText(fromNodeInfo, s);
		   if (localList != null && localList.size() > 0){
		       lists.add(localList);
		   }
		}
		if (opcode == OperCode.AND && lists.size() != node_labels.length ){ //未完全匹配，释放已添加的数据
		    for(List<AccessibilityNodeInfo> n: lists){
			recycleNodes(n);
		    }
		    lists.clear() ;
		}
	    }
	    return lists;
	}
	
	private static List<AccessibilityNodeInfo> getAccessibilityNodeInfoByText(AccessibilityNodeInfo paramAccessibilityNodeInfo, String paramString)  {
	    CharSequence label = null ;
	    List<AccessibilityNodeInfo> localList = paramAccessibilityNodeInfo.findAccessibilityNodeInfosByText(paramString);
	    ArrayList<AccessibilityNodeInfo> localArrayList = new ArrayList<AccessibilityNodeInfo>();
	    label = paramAccessibilityNodeInfo.getText() ;
	    if (!TextUtils.isEmpty(label) && label.toString().equals(paramString)){
		localArrayList.add(AccessibilityNodeInfo.obtain(paramAccessibilityNodeInfo));
	    }
	    Iterator<AccessibilityNodeInfo> localIterator = localList.iterator();
	    while (localIterator.hasNext())	    {
	      AccessibilityNodeInfo localAccessibilityNodeInfo = (AccessibilityNodeInfo)localIterator.next();
	      label = localAccessibilityNodeInfo.getText();
	      if ((!TextUtils.isEmpty(label)) && (label.toString().equals(paramString))){
	        localArrayList.add(localAccessibilityNodeInfo);
	      }else{
		  localAccessibilityNodeInfo.recycle() ;
	      }
	    }
	    return localArrayList;
	}
	
	public final boolean isUninstallWindow(AccessibilityEvent paramAccessibilityEvent) {
		CharSequence localCharSequence = paramAccessibilityEvent.getClassName();
		if ((UNINSTALLERACTIVITY.equals(localCharSequence))
				|| (UNINSTALLAPPPROGRESS.equals(localCharSequence)))
			UNINSTALL_WINDOWIDS.add(Integer.valueOf(paramAccessibilityEvent
					.getWindowId()));
		while (UNINSTALL_WINDOWIDS.contains(Integer
				.valueOf(paramAccessibilityEvent.getWindowId())))
			return false;
		return true;
	}
	
	static class MyReceiver extends BroadcastReceiver {
	    MyAccessibilityService	mService ;
	    MyReceiver(MyAccessibilityService service){
		mService = service ;
	    }
	    @Override
	    public void onReceive(Context context, final Intent intent) {
//		intent.setClass(context, MyAccessibilityService.class);
//		context.startService(intent);
//		三星A8000不通过startService传参数		
		Handler handler = new Handler(mService.getMainLooper());
		handler.post(new Runnable(){
		    @Override
		    public void run(){
			mService.handleIntent(intent);
		    }
		});
	    }
	}
}
