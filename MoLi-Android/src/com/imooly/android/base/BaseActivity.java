package com.imooly.android.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import com.imooly.android.tool.ActivityManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.message.PushAgent;

public abstract class BaseActivity extends FragmentActivity {
	protected ActivityManager activityManager;
	protected ImageLoader imageLoader = ImageLoader.getInstance();

	protected BaseActivity self;
	protected int width, height;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		self = this;
		activityManager = ActivityManager.getScreenManager();
		activityManager.pushActivity(this);
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		width = dm.widthPixels;
		height = dm.heightPixels;
		
		//统计应用启动数据
		PushAgent.getInstance(self).onAppStart();
	}

	@Override
	protected void onDestroy() {
		imageLoader.clearMemoryCache();
		activityManager.popActivity(this);
		super.onDestroy();
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public void logActivityName(Activity activity) {
		Log.d("activity name", "this activity name = " + this.getClass().getName());
	}

}
