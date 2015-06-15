/***
This is free and unencumbered software released into the public domain.

Anyone is free to copy, modify, publish, use, compile, sell, or
distribute this software, either in source code form or as a compiled
binary, for any purpose, commercial or non-commercial, and by any
means.

For more information, please refer to <http://unlicense.org/>
 */
package com.imooly.android.tool;

import android.util.Log;

import com.imooly.android.BuildConfig;

/**
 * @date 21.06.2012
 * @author Mustafa Ferhan Akman
 * 
 *         Create a simple and more understandable Android logs.
 * */

public class Logger {

	static String className;
	static String methodName;
	static int lineNumber;

	private Logger() {
		/* Protect from instantiations */
	}

	private static boolean isDebuggable() {
		return BuildConfig.DEBUG;
	}

	private static String createTag() {
		
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append(getSimpleClassName(className))
			.append("$")
			.append(".")
            .append(methodName)
            .append(" ")
            .append("(")
            .append(className)
            .append(":")
            .append(lineNumber)
            .append(")");

		return sBuffer.toString();
	}
	
	private static String createMsg(String msg) {
		
		StringBuffer sBuffer = new StringBuffer();
		sBuffer.append("[")
			.append(getSimpleClassName(className))
			.append("$")
			.append(".")
            .append(methodName)
            .append(":")
            .append(lineNumber)
            .append("]")
            .append("  ")
            .append(msg);

		return sBuffer.toString();
	}
	
	private static void getMethodNames(StackTraceElement[] sElements) {
		className = sElements[1].getFileName();
		methodName = sElements[1].getMethodName();
		lineNumber = sElements[1].getLineNumber();
	}
	
	private static String getSimpleClassName(String name) {
        int lastIndex = name.lastIndexOf(".");
        return name.substring(0,lastIndex);
    }

	public static void e(String msg) {
		e("", msg);
	}
	public static void e(String tag,String msg) {
		if (!isDebuggable())
			return;

		getMethodNames(new Throwable().getStackTrace());
		if("".equals(tag)){
			Log.e(createTag(), msg);
		}else{
			Log.e(tag, createMsg(msg));
		}
	}

	public static void i(String msg) {
		i("", msg);
	}
	public static void i(String tag,String msg) {
		if (!isDebuggable())
			return;

		getMethodNames(new Throwable().getStackTrace());
		if("".equals(tag)){
			Log.i(createTag(), msg);
		}else{
			Log.i(tag, createMsg(msg));
		}
	}

	public static void d(String msg) {
		d("", msg);
	}
	public static void d(String tag,String msg) {
		if (!isDebuggable())
			return;

		getMethodNames(new Throwable().getStackTrace());
		if("".equals(tag)){
			Log.d(createTag(), msg);
		}else{
			Log.d(tag, createMsg(msg));
		}
	}

	public static void v(String msg) {
		v("", msg);
	}
	public static void v(String tag,String msg) {
		if (!isDebuggable())
			return;

		getMethodNames(new Throwable().getStackTrace());
		if("".equals(tag)){
			Log.v(createTag(), msg);
		}else{
			Log.v(tag, createMsg(msg));
		}
	}

	public static void w(String msg) {
		w("", msg);
	}
	public static void w(String tag,String msg) {
		if (!isDebuggable())
			return;

		getMethodNames(new Throwable().getStackTrace());
		if("".equals(tag)){
			Log.w(createTag(), msg);
		}else{
			Log.w(tag, createMsg(msg));
		}
	}

	public static void wtf(String msg) {
		wtf("", msg);
	}
	public static void wtf(String tag,String msg) {
		if (!isDebuggable())
			return;

		getMethodNames(new Throwable().getStackTrace());
		if("".equals(tag)){
			Log.wtf(createTag(), msg);
		}else{
			Log.wtf(tag, createMsg(msg));
		}
	}

}
