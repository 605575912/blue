package com.show.blue;

import android.os.Build;
import android.view.WindowManager;

public class FloatLayoutParams {
    public static WindowManager.LayoutParams getFloatLayoutParams(int flag, int pixformat){
	WindowManager.LayoutParams lp = new WindowManager.LayoutParams(WindowManager.LayoutParams.TYPE_TOAST,
    	    flag, 
    	    pixformat);
	
	int sdk = Integer.parseInt(Build.VERSION.SDK);
	if (sdk < 19){ //Android 4.3之前的版本的TYPE_TOAST不支持响应TOUCH事件，会导致浮窗不能点击、拖动
	    lp.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
	}
	return lp ;
    }
}
