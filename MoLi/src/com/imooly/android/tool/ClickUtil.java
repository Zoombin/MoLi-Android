package com.imooly.android.tool;

public class ClickUtil {
	private static long lastClickTime = 0;

	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 1000) {
			return true;
		}
	
		lastClickTime = time;
		return false;
	}
}
