package com.imooly.android.ui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SlidingDrawer;
import android.widget.SlidingDrawer.OnDrawerCloseListener;
import android.widget.SlidingDrawer.OnDrawerOpenListener;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRouteLine.DrivingStep;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.TransitRouteLine.TransitStep;
import com.baidu.mapapi.search.route.WalkingRouteLine.WalkingStep;
import com.imooly.android.R;
import com.imooly.android.adapter.StoreRoutePlanShowAdapter;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.entity.RouteLineEntity;
import com.imooly.android.tool.RoutePlanOP;

/**
 * 地图 - 路径规划
 * 
 * @author lsd
 * 
 */
public class SearchRoutePlanActivity extends BaseActivity implements View.OnClickListener {
	public static String ROUTE_LINE = "route_line";
	private LinearLayout ll_title;
	private ImageView iv_back;
	private ImageView ic_arrow;
	private TextView tv_title;
	SlidingDrawer slidingdrawer;

	TextView route_title;
	TextView route_content;
	ListView listView;
	StoreRoutePlanShowAdapter adapter;

	MapView mMapView;
	BaiduMap mBaiduMap;
	ImageButton zoom_in;
	ImageButton zoom_out;
	ImageButton locateBtn;

	/**** 定位相关 *******/
	public LocationClient mLocationClient;
	public GeofenceClient mGeofenceClient;
	public MyLocationListenner mMyLocationListener;
	boolean isFirstLoc = true;// 是否首次定位

	BDLocation curLocation;// 定位成功
	private LatLng curLatLng;// 定位成功的位置

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_routeplan);

		logActivityName(this);

		init();
		locationInfos();
		showRoute();
	}

	@SuppressWarnings("deprecation")
	private void init() {
		ll_title = (LinearLayout) findViewById(R.id.ll_title);

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
		ic_arrow = (ImageView) findViewById(R.id.ic_arrow);
		tv_title = (TextView) findViewById(R.id.tv_title);

		slidingdrawer = (SlidingDrawer) findViewById(R.id.slidingdrawer);

		route_title = (TextView) findViewById(R.id.route_title);
		route_content = (TextView) findViewById(R.id.route_content);
		listView = (ListView) findViewById(R.id.list);
		adapter = new StoreRoutePlanShowAdapter(self);
		listView.setAdapter(adapter);

		zoom_in = (ImageButton) findViewById(R.id.zoom_in);
		zoom_out = (ImageButton) findViewById(R.id.zoom_out);
		locateBtn = (ImageButton) findViewById(R.id.locateBtn);
		zoom_in.setOnClickListener(this);
		zoom_out.setOnClickListener(this);
		locateBtn.setOnClickListener(this);

		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		// 隐藏缩放控件
		mMapView.showZoomControls(false);
		mBaiduMap = mMapView.getMap();
		// 放大倍数
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(16.0f);
		mBaiduMap.setMapStatus(msu);

		slidingdrawer.setOnDrawerOpenListener(new OnDrawerOpenListener() {
			@Override
			public void onDrawerOpened() {
				// TODO Auto-generated method stub
			    ic_arrow.setImageResource(R.drawable.ic_arrow_down);
			}
		});
		
		slidingdrawer.setOnDrawerCloseListener(new OnDrawerCloseListener() {
			@Override
			public void onDrawerClosed() {
				// TODO Auto-generated method stub
				 ic_arrow.setImageResource(R.drawable.ic_arrow_up);
			}
		});
	}

	private void showRoute() {
		String title = "";
		String content = "";
		List<String> list = new ArrayList<String>();
		String type = RoutePlanOP.instance().getRouteType();
		if ("BUS".equals(type)) {
			TransitRouteLine line = (TransitRouteLine) RoutePlanOP.instance().getRouteLine();
			TransitRouteOverlay overlay = new TransitRouteOverlay(mBaiduMap);
			overlay.setData(line);
			overlay.addToMap();
			overlay.zoomToSpan();
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					mBaiduMap.animateMapStatus(MapStatusUpdateFactory.zoomTo(16.0f));
				}
			}, 800);
			
			List<TransitStep> steps = line.getAllStep();
			StringBuffer sBuffer = new StringBuffer();
			String strDir = " → ";
			for (TransitStep step : steps) {
				if (step.getVehicleInfo() != null) {
					String vehicleInfo = step.getVehicleInfo().getTitle();
					sBuffer.append(vehicleInfo);
					sBuffer.append(strDir);
				}
			}
			title = sBuffer.toString().substring(0, sBuffer.toString().lastIndexOf(strDir));

			int duration = line.getDuration();
			int hour = duration / (60 * 60);
			int min = (duration - (hour * 60 * 60)) / 60;
			int distance = line.getDistance();
			content = String.format("约%s小时%s分  |  %s公里", hour, min, new DecimalFormat("#.0").format(distance / 1000.0f));

			List<TransitStep> allSteps = line.getAllStep();
			for (TransitStep st : allSteps) {
				String s = st.getInstructions();
				if (!TextUtils.isEmpty(s)) {
					list.add(s);
				}
			}
		}
		if ("CAR".equals(type)) {
			DrivingRouteLine line = (DrivingRouteLine) RoutePlanOP.instance().getRouteLine();
			DrivingRouteOverlay overlay = new DrivingRouteOverlay(mBaiduMap);
			overlay.setData(line);
			overlay.addToMap();
			overlay.zoomToSpan();
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					mBaiduMap.animateMapStatus(MapStatusUpdateFactory.zoomTo(16.0f));
				}
			}, 800);

			title = "驾车路线";

			int duration = line.getDuration();
			int hour = duration / (60 * 60);
			int min = (duration - (hour * 60 * 60)) / 60;
			int distance = line.getDistance();
			content = String.format("约%s小时%s分  |  %s公里", hour, min, new DecimalFormat("#.0").format(distance / 1000.0f));

			List<DrivingStep> allSteps = line.getAllStep();
			for (DrivingStep st : allSteps) {
				String s = st.getInstructions();
				if (!TextUtils.isEmpty(s)) {
					list.add(s);
				}
			}
		}
		if ("WALK".equals(type)) {
			WalkingRouteLine line = (WalkingRouteLine) RoutePlanOP.instance().getRouteLine();
			WalkingRouteOverlay overlay = new WalkingRouteOverlay(mBaiduMap);
			overlay.setData(line);
			overlay.addToMap();
			overlay.zoomToSpan();
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					mBaiduMap.animateMapStatus(MapStatusUpdateFactory.zoomTo(16.0f));
				}
			}, 800);

			title = "步行路线";

			int duration = line.getDuration();
			int hour = duration / (60 * 60);
			int min = (duration - (hour * 60 * 60)) / 60;
			int distance = line.getDistance();
			content = String.format("约%s小时%s分  |  %s公里", hour, min, new DecimalFormat("#.0").format(distance / 1000.0f));

			List<WalkingStep> allSteps = line.getAllStep();
			for (WalkingStep st : allSteps) {
				String s = st.getInstructions();
				if (!TextUtils.isEmpty(s)) {
					list.add(s);
				}
			}
		}
		route_title.setText(title);
		route_content.setText(content);
		adapter.setData(list);
	}

	/******************** 定位相关 *******************/

	private void locationInfos() {
		mLocationClient = new LocationClient(this.getApplicationContext());
		mMyLocationListener = new MyLocationListenner();
		mLocationClient.registerLocationListener(mMyLocationListener);
		mGeofenceClient = new GeofenceClient(getApplicationContext());

		initLocation();

		// 开启定位图层
		mBaiduMap.setMyLocationEnabled(true);
		mLocationClient.start();
	}

	private void initLocation() {
		LocationClientOption option = new LocationClientOption();
		option.setOpenGps(true);// 打开gps
		option.setLocationMode(LocationMode.Hight_Accuracy);// 设置定位模式
		option.setCoorType("bd09ll");// 返回的定位结果是百度经纬度,默认值gcj02
		option.setScanSpan(1000);// 设置发起定位请求的间隔时间为5000ms
		option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
		option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
		mLocationClient.setLocOption(option);
	}

	/**
	 * 定位SDK监听函数
	 */
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			// map view 销毁后不在处理新接收的位置
			if (location == null || mMapView == null)
				return;
			curLocation = location;
			MyLocationData locData = new MyLocationData.Builder().accuracy(location.getRadius())
			// 此处设置开发者获取到的方向信息，顺时针0-360
					.direction(100).latitude(location.getLatitude()).longitude(location.getLongitude()).build();
			mBaiduMap.setMyLocationData(locData);
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.zoom_in:
			mBaiduMap.animateMapStatus(MapStatusUpdateFactory.zoomIn());
			break;
		case R.id.zoom_out:
			mBaiduMap.animateMapStatus(MapStatusUpdateFactory.zoomOut());
			break;
		case R.id.locateBtn:
			mLocationClient.start();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mLocationClient.stop();
		mMapView.onDestroy();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mMapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mMapView.onPause();
	}

}
