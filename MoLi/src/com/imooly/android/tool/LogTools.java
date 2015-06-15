package com.imooly.android.tool;

import android.util.Log;

public class LogTools {

	public static boolean isShow = true;// 上线模式

	// public static boolean isShow = false;//开发模式

	// ye工程师打出来的log
	public static void log(String msg) {
		if (isShow) {
			Log.i("activity name = ", msg);
		}
	}

	// li工程师打出来的log
	public static void logTest(String msg) {
		if (isShow) {
			Log.i("Test", msg);
		}
	}

}
