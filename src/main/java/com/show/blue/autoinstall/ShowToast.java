package com.show.blue.autoinstall;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

/**
 * 显示统一的Toast created by Bear at 2015-1-7 下午4:03:35 TODO
 */
public class ShowToast {
	static Toast toast;

	/**
	 * 显示统一的Toast created by Bear at 2015-1-7 下午4:03:19 TODO
	 */
	public static void show(Context context, String text) {
		if (TextUtils.isEmpty(text))
		{	return;
		}
		if (toast == null) {
			toast = Toast.makeText(context, text, Toast.LENGTH_LONG);
		}
		toast.setText(text);
		toast.show();
	}

	/**
	 * 显示统一的Toast created by Bear at 2015-1-7 下午4:03:19 TODO
	 */
	public static void cancelTips() {
		if (toast != null) {
			toast.cancel();
		}
	}

	/**
	 * 显示的Toast 2015-1-27 @author lzx
	 * 
	 */

	/**
	 * 显示的Toast 2015-1-27 @author lzx
	 *
	 */

}
