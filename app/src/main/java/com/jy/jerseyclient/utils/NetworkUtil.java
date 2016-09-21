package com.jy.jerseyclient.utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * 网络相关工具类
 * @author xy
 * 
 */
public class NetworkUtil {
	/**
	 * 判断是否有可用网络
	 * @param context
	 * @return
	 */
	public static boolean isNetWorkOpened(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connManager.getActiveNetworkInfo() != null) {
			return connManager.getActiveNetworkInfo().isAvailable();
		}
		return false;
	}
}