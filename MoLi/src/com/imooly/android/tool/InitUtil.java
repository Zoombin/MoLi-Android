package com.imooly.android.tool;

import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.db.DB_Location;
import com.imooly.android.entity.RspAppRegist;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.utils.Utils;

public class InitUtil {
	public interface InitCallBack{
		void success();
		void failed(String msg);
	}
	
	private Context context;
	private InitCallBack callBack;
	private LocationClient mLocationClient;

	public InitUtil(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
		
	     mLocationClient = new LocationClient(context);
		LocationClientOption option = new LocationClientOption();
		option.setIsNeedAddress(true);
		mLocationClient.setLocOption(option);
		
		MyLocationListener mMyLocationListener = new MyLocationListener();
		mLocationClient.registerLocationListener(mMyLocationListener);
	}
	
	public void start(InitCallBack callBack){
		this.callBack = callBack;
		mLocationClient.start();
	}

	/**
	 * 实现实位回调监听
	 */
	public class MyLocationListener implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
			mLocationClient.stop();

			// 系统版本号
			String deviceOSVersion = Utils.getDeviceOSVersion();

			// 设备唯一序列号(IMEI)
			String deviceid = Utils.getDeviceId(context);

			// APP版本号
			String appVer = String.valueOf(Utils.getClientVersionCode(context));

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
				DB_Location db_location = new DB_Location(context);
				db_location.setLatitude(nlatitude);
				db_location.setLontitude(nlontitude);
				db_location.setCityId(cityId);
				db_location.setCityName(cityName);
			}

			Api.appRegister(context,"Android", deviceOSVersion, "Android", deviceid, ipAddress, nlatitude, nlontitude, appVer,"",
					new NetCallBack<ServiceResult>() {
						@Override
						public void success(ServiceResult rspData) {
							RspAppRegist rsp = (RspAppRegist) rspData;
							if (rsp.data == null) {
								callBack.failed("");
								return;
							}
							Config.setAppID(rsp.data.getAppid());
							Config.setAppSecret(rsp.data.getAppsecret());
							
							callBack.success();
						}

						@Override
						public void failed(String msg) {
							callBack.failed(msg);
						}
					}, RspAppRegist.class);
		}
	}
}
