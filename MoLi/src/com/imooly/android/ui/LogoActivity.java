package com.imooly.android.ui;

import org.json.JSONException;

import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.imooly.android.MoLiApplication;
import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.db.DB_Location;
import com.imooly.android.entity.RspAppRegist;
import com.imooly.android.entity.RspRecordDeviceToken;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.tool.Config;
import com.imooly.android.tool.TicketUtil;
import com.imooly.android.tool.TicketUtil.ITicket;
import com.imooly.android.utils.Utils;
import com.imooly.android.view.UpdateDialogLogo;
import com.imooly.android.view.UpdateDialogLogo.onUpdateCallBack;
import com.umeng.message.PushAgent;
import com.umeng.message.proguard.I.e;

/**
 * logo页
 * 
 * @author daiye
 * 
 */
public class LogoActivity extends BaseActivity {

	private MyLocationListener mMyLocationListener;
	private ImageView image_logo;
	PushAgent mPushAgent;
	private Animation animation = null;

	@Override
	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.activity_logo);

		Config.setScreenSize(this);
		logActivityName(this);
		initView();
		
		animation = initLogoAnimation();
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		//自动检查更新
		new UpdateDialogLogo(this, new onUpdateCallBack() {
			@Override
			public void onForceUpdate() {
			}
			
			@Override
			public void onDialogSure() {
				image_logo.startAnimation(animation);
			}
			
			@Override
			public void onDialogCancel() {
				image_logo.startAnimation(animation);
			}
		});
	};

	private void setPushEnable() {
		mPushAgent = PushAgent.getInstance(self);
		if (!Config.getPushEnable()) {
			mPushAgent.disable();
			return;
		}
		mPushAgent.enable();
		
		String device_token = mPushAgent.getRegistrationId();
		Log.i("device_token", "device_token = " + device_token);
		if (TextUtils.isEmpty(Config.getAppID())) {
			//APP没有注册成功
			return;
		}
		
		if(TextUtils.isEmpty(device_token)){
			//推送没有注册成功
			return;
		}
		/**向服务器发送 device_token*/
		Api.recorddevicetoken(self, device_token, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				// TODO Auto-generated method stub
			}
			@Override
			public void failed(String msg) {
				// TODO Auto-generated method stub
			}
		}, null);
		
		/**向友盟addAlias */
		new Thread() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					mPushAgent.addAlias(Config.getAppID(), Config.IMOOLY_APP);
				} catch (e e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();
	}

	private void initView() {
		image_logo = (ImageView) findViewById(R.id.image_logo);
	}

	private void nextPage2() {
		if (self.isFinishing()) {
			return;
		}

		// 判断是否查看过教程
		boolean isTutorial = Config.isTutorial();
		if (!isTutorial) {
			startActivity(new Intent(getApplication(), NavigationActivity.class));
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			createShortcut();
			finish();
		} else {
			startActivity(new Intent(getApplication(), MainActivity.class));
			overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
			finish();
		}

	}

	/**
	 * 实现实位回调监听
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {

			// 系统版本号
			String deviceOSVersion = Utils.getDeviceOSVersion();

			// 设备唯一序列号(IMEI)
			String deviceid = Utils.getDeviceId(self);

			// APP版本号
			String appVer = String.valueOf(Utils.getClientVersionCode(self));

			// IP地址
			String ipAddress = Utils.getLocalIpAddress();

			String nlatitude = "0.0";
			String nlontitude = "0.0";
			String cityName = "";
			String cityId = "";
			if (location != null) {
				// 百度定位命令反了
				nlatitude = Double.toString(location.getLongitude());
				nlontitude = Double.toString(location.getLatitude());
				cityName = location.getCity();
				cityId = location.getCityCode();
				DB_Location db_location = new DB_Location(self);
				db_location.setLatitude(nlatitude);
				db_location.setLontitude(nlontitude);
				db_location.setCityId(cityId);
				db_location.setCityName(cityName);
			}
			
			if (!TextUtils.isEmpty(Config.getAppID())) {
				return;
			}
			
			Api.appRegister(self,"Android", deviceOSVersion, "Android", deviceid, ipAddress, nlatitude, nlontitude, appVer,"",
					new NetCallBack<ServiceResult>() {
						@Override
						public void success(ServiceResult rspData) {
							RspAppRegist rsp = (RspAppRegist) rspData;
							if (rsp.data == null) {
								System.out.println("appregister fail");
								//finish();
								nextPage2();
								return;
							}
							Config.setAppID(rsp.data.getAppid());
							Config.setAppSecret(rsp.data.getAppsecret());

							setPushEnable();
							TicketUtil.instance().firstLoad(new ITicket() {
								@Override
								public void success() {
									// TODO Auto-generated method stub
									nextPage2();
								}

								@Override
								public void failed(String msg) {
									// TODO Auto-generated method stub
									// Toast.show(self, msg);
									nextPage2();
								}
							});
						}

						@Override
						public void failed(String msg) {
							//Toast.show(self, msg);
							nextPage2();
						}
					}, RspAppRegist.class);
		}
	}

	/**
	 * 创建桌面快捷方式
	 */
	private void createShortcut() {
		Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
		// 快捷方式的名称
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, getString(R.string.app_name));
		// 不允许重复创建
		shortcut.putExtra("duplicate", false);
		// 指定快捷方式的启动对象
		Intent shortcutIntent = new Intent(Intent.ACTION_MAIN);
		shortcutIntent.setClassName(this.getPackageName(), this.getClass().getName());
		shortcutIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		// 快捷方式的图标
		ShortcutIconResource iconRes = Intent.ShortcutIconResource.fromContext(this, R.drawable.app_icon);
		shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, iconRes);
		// 发出广播
		sendBroadcast(shortcut);
		// 将第一次启动的标识设置为false
		Config.setFirst(false);
	}

	// 闪屏动画
	private Animation initLogoAnimation() {
		TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF, 0.0f,
				Animation.RELATIVE_TO_SELF, 0.1f, Animation.RELATIVE_TO_SELF, 0.0f);
		animation.setDuration(800); // 设置动画持续时间
		animation.setFillAfter(true);
		animation.setInterpolator(new DecelerateInterpolator());

		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				if (!TextUtils.isEmpty(Config.getAppID())) {
					setPushEnable();
					if (TicketUtil.instance().refrashTicket(new ITicket() {
						@Override
						public void success() {
							// TODO Auto-generated method stub
							nextPage2();
						}

						@Override
						public void failed(String msg) {
							// TODO Auto-generated method stub
							// Toast.show(self, msg);
							nextPage2();
						}
					}))
						return;
					nextPage2();
				}

				mMyLocationListener = new MyLocationListener();
				MoLiApplication.getInstance().mLocationClient.registerLocationListener(mMyLocationListener);
				MoLiApplication.getInstance().mGeofenceClient = new GeofenceClient(getApplicationContext());
				MoLiApplication.getInstance().mLocationClient.start();
			}
		});

		return animation;
	}
}
