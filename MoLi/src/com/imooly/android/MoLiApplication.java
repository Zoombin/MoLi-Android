package com.imooly.android;

import java.io.File;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.imooly.android.tool.CrashHandler;
import com.imooly.android.tool.MoliMessageHandler;
import com.imooly.android.tool.MoliNotificationClickHandler;
import com.imooly.android.utils.Utils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.umeng.message.PushAgent;

/**
 * 应用入口
 * @author daiye
 *
 */
public class MoLiApplication extends Application {
	private File cacheFile = null;
	private PushAgent mPushAgent;
	public LocationClient mLocationClient;
	public GeofenceClient mGeofenceClient;
	private static Context mContext;
	private static MoLiApplication self;
	
	// 是否登录
	private boolean isAppLogin = false;
	// 是否是会员充值
	private boolean isMemeberPay = false;
	
	
	public boolean isMemeberPay() {
		return isMemeberPay;
	}

	public void setMemeberPay(boolean isMemeberPay) {
		this.isMemeberPay = isMemeberPay;
	}

	// 全局变量支付
	private float mPrice = 0.0f;
	private String mPayNo;
	
	public float getmPrice() {
		return mPrice;
	}

	public void setmPrice(float mPrice) {
		this.mPrice = mPrice;
	}

	public String getmPayNo() {
		return mPayNo;
	}

	public void setmPayNo(String mPayNo) {
		this.mPayNo = mPayNo;
	}

	public boolean isLogin() {
		return isAppLogin;
	}

	public void setLogin(boolean isLogin) {
		this.isAppLogin = isLogin;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		self = this;
		mContext = getApplicationContext();

		cacheFile = Utils.getCacheFile(this);
		
		/**错误收集*/
		 CrashHandler.getInstance().init(mContext);
		
		/**初始化百度地图*/
		SDKInitializer.initialize(this);
		
		initImageLoader();
		
		mLocationClient = new LocationClient(this.getApplicationContext());
		LocationClientOption option = new LocationClientOption();
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
		
		//设置友盟推送处理事件
		mPushAgent = PushAgent.getInstance(this);
		mPushAgent.setMergeNotificaiton(false);//展示多条消息
		mPushAgent.setMessageHandler(new MoliMessageHandler());
		mPushAgent.setNotificationClickHandler(new MoliNotificationClickHandler());
	}

	public static MoLiApplication getInstance() {
		return self;
	}
	
	public static Context getContext() {
		return mContext;
	}

	private void initImageLoader() {
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.ic_loading)
		    .showImageForEmptyUri(R.drawable.ic_error)  // empty URI时显示的图片  
		    .showImageOnFail(R.drawable.ic_error)      // 不是图片文件 显示图片  
			.cacheInMemory(true)
			.bitmapConfig(Bitmap.Config.ARGB_8888)  // 图片压缩质量
			.cacheOnDisc(true)			
			.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
			.build();

		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
				.defaultDisplayImageOptions(defaultOptions)
				.discCache(new UnlimitedDiscCache(cacheFile))
				.discCacheFileCount(10000).threadPoolSize(5)
				.build();
		
		ImageLoader.getInstance().init(config);
	}
	
	@Override
	public void onTerminate() {
		this.isAppLogin = false;
		super.onTerminate();
	}
}
