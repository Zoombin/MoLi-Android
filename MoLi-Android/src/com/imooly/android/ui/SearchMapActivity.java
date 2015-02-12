package com.imooly.android.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMarkerClickListener;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.imooly.android.R;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.db.DB_Location;
import com.imooly.android.entity.RspBusinessSearch.BusinessEty;
import com.imooly.android.entity.RspDiscountstore;
import com.imooly.android.tool.RoutePlanOP;
import com.imooly.android.widget.Toast;

/**
 * 地图搜索
 * 
 * @author lsd
 * 
 */
public class SearchMapActivity extends BaseActivity implements View.OnClickListener {

	public static String SEARCH_ENTITYS = "search_entitys";
	public static String STORE_NAV = "store_nav";
	private LinearLayout ll_title;
	private ImageView iv_back;
	private TextView tv_title;

	MapView mMapView;
	BaiduMap mBaiduMap;
	ImageButton zoom_in;
	ImageButton zoom_out;
	ImageButton locateBtn;
	LinearLayout ll_transport;
	TextView tv_bus;
	TextView tv_bus_plan;
	TextView tv_car;
	TextView tv_walk;

	BitmapDescriptor icon_mark_food;// 标注
	BitmapDescriptor icon_mark_boby;// 标注
	BitmapDescriptor icon_mark_buid;// 标注
	BitmapDescriptor icon_mark_edu;// 标注
	BitmapDescriptor icon_mark_fun;// 标注
	BitmapDescriptor icon_mark_mei;// 标注
	BitmapDescriptor icon_mark_server;// 标注
	BitmapDescriptor icon_mark_shopping;// 标注

	/**** 定位相关 *******/
	public LocationClient mLocationClient;
	public GeofenceClient mGeofenceClient;
	public MyLocationListenner mMyLocationListener;
	boolean isFirstLoc = true;// 是否首次定位

	BDLocation curLocation;// 定位成功
	private LatLng curLatLng;// 定位成功的位置
	private LatLng markLatLng;// 点击后Mark点的位置

	/***** 搜索相关 *****/
	RoutePlanSearch mSearch = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mapsearch);

		logActivityName(this);

		init();
		locationInfos();
		searchInfos();

		getData();
	}

	private void init() {
		ll_title = (LinearLayout) findViewById(R.id.ll_title);

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);

		tv_title = (TextView) findViewById(R.id.tv_title);

		zoom_in = (ImageButton) findViewById(R.id.zoom_in);
		zoom_out = (ImageButton) findViewById(R.id.zoom_out);
		locateBtn = (ImageButton) findViewById(R.id.locateBtn);
		zoom_in.setOnClickListener(this);
		zoom_out.setOnClickListener(this);
		locateBtn.setOnClickListener(this);

		ll_transport = (LinearLayout) findViewById(R.id.ll_transport);
		tv_bus_plan = (TextView) findViewById(R.id.tv_bus_plan);
		tv_bus_plan.setOnClickListener(this);
		tv_bus = (TextView) findViewById(R.id.tv_bus);
		tv_bus.setOnClickListener(this);
		tv_car = (TextView) findViewById(R.id.tv_car);
		tv_car.setOnClickListener(this);
		tv_walk = (TextView) findViewById(R.id.tv_walk);
		tv_walk.setOnClickListener(this);


		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		// 隐藏缩放控件
		mMapView.showZoomControls(false);
		mBaiduMap = mMapView.getMap();
		// 放大倍数
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
		mBaiduMap.setMapStatus(msu);
		// 标注
		icon_mark_food = BitmapDescriptorFactory.fromResource(R.drawable.icon_mark_food);
		icon_mark_boby = BitmapDescriptorFactory.fromResource(R.drawable.icon_mark_boby);
		icon_mark_buid = BitmapDescriptorFactory.fromResource(R.drawable.icon_mark_buid);
		icon_mark_edu = BitmapDescriptorFactory.fromResource(R.drawable.icon_mark_edu);
		icon_mark_fun = BitmapDescriptorFactory.fromResource(R.drawable.icon_mark_fun);
		icon_mark_mei = BitmapDescriptorFactory.fromResource(R.drawable.icon_mark_mei);
		icon_mark_server = BitmapDescriptorFactory.fromResource(R.drawable.icon_mark_server);
		icon_mark_shopping = BitmapDescriptorFactory.fromResource(R.drawable.icon_mark_shopping);

		// 地图相关事件监听
		mapListener();
	}

	private void getData() {
		DB_Location db_Location = new DB_Location(self);
		double lat = Double.parseDouble(db_Location.getLontitude());
		double lng = Double.parseDouble(db_Location.getLatitude());
		curLatLng = new LatLng(lat, lng);

		Intent intent = getIntent();
		/** 标注点 */
		if (intent.hasExtra(SEARCH_ENTITYS)) {
			@SuppressWarnings("unchecked")
			List<BusinessEty> list = (List<BusinessEty>) intent.getSerializableExtra(SEARCH_ENTITYS);
			if (list != null && list.size() > 0) {
				list = new ArrayList<BusinessEty>();
				BusinessEty businessEty = new BusinessEty();
				businessEty.setAddress("苏州工业园区星湖街180号");
				businessEty.setBusinessname("魔力");
				businessEty.setLat(31.268244);
				businessEty.setLng(120.736099);
				list.add(0, businessEty);

				businessEty = new BusinessEty();
				businessEty.setAddress("苏州大学独墅湖校区");
				businessEty.setBusinessname("苏州大学");
				businessEty.setLat(31.279509);
				businessEty.setLng(120.741341);
				list.add(0, businessEty);
			} else {
				list = new ArrayList<BusinessEty>();
				BusinessEty businessEty = new BusinessEty();
				businessEty.setAddress("苏州工业园区星湖街180号");
				businessEty.setBusinessname("魔力");
				businessEty.setLat(31.268244);
				businessEty.setLng(120.736099);
				list.add(businessEty);

				businessEty = new BusinessEty();
				businessEty.setAddress("苏州大学独墅湖校区");
				businessEty.setBusinessname("苏州大学");
				businessEty.setLat(31.279509);
				businessEty.setLng(120.741341);
				list.add(businessEty);
			}
			
			initOverlay(list);
		}

		/*** 导航 */
		if (intent.hasExtra(STORE_NAV)) {
			RspDiscountstore model = (RspDiscountstore) intent.getSerializableExtra(STORE_NAV);
			RspDiscountstore.Data data = model.data;
			if (data != null) {
				// 标注
				List<BusinessEty> list = new ArrayList<BusinessEty>();
				final BusinessEty ety = new BusinessEty();
				ety.setAddress(data.getAddress());
				ety.setBusinessid(data.getBusinessid());
				ety.setBusinessname(data.getBusinessname());
				ety.setLat(data.getLat());
				ety.setLng(data.getLng());
				list.add(ety);
				initOverlay(list);

				// 移动到位置点
				LatLng ll = new LatLng(data.getLat(), data.getLng());
				MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
				mBaiduMap.animateMapStatus(u);
				
				showTransportTypeView();

				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						// TODO Auto-generated method stub
						View view = initPopView(ety);
						LatLng lal = markLatLng = new LatLng(ety.getLat(), ety.getLng());
						InfoWindow mInfoWindow = new InfoWindow(view, lal, -63);
						mBaiduMap.showInfoWindow(mInfoWindow);
						showTransportTypeView();
						
						//计算公交
						routePlan();
					}
				}, 100);
			}
		}
	}

	/** 添加标注到地图上 */
	public void initOverlay(List<BusinessEty> list) {
		// add marker overlay
		if (list == null || list.size() == 0) {
			return;
		}
		for (BusinessEty ety : list) {
			if (ety.getLat() != 0 && ety.getLng() != 0) {
				LatLng llP = new LatLng(ety.getLat(), ety.getLng());
				BitmapDescriptor icon = getIcon(ety);
				OverlayOptions olo = new MarkerOptions().position(llP).icon(icon).zIndex(9).draggable(true);
				Bundle bundle = new Bundle();
				bundle.putSerializable("ety", ety);
				mBaiduMap.addOverlay(olo).setExtraInfo(bundle);
			}
		}
	}
	
	private BitmapDescriptor getIcon(BusinessEty ety){
		if(ety.getBusinessname().contains("餐饮")){
			return icon_mark_food;
		}
		if(ety.getBusinessname().contains("母婴")){
			return icon_mark_boby;
		}
		if(ety.getBusinessname().contains("住宿")){
			return icon_mark_buid;
		}
		if(ety.getBusinessname().contains("教育")){
			return icon_mark_edu;
		}
		if(ety.getBusinessname().contains("娱乐")){
			return icon_mark_fun;
		}
		if(ety.getBusinessname().contains("美容")){
			return icon_mark_mei;
		}
		if(ety.getBusinessname().contains("服务")){
			return icon_mark_server;
		}
		if(ety.getBusinessname().contains("购物")){
			return icon_mark_shopping;
		}
		return icon_mark_food;
	}

	/***
	 * 地图的相关事件监听
	 */
	public void mapListener() {
		/** Mark点点击事件 */
		mBaiduMap.setOnMarkerClickListener(new OnMarkerClickListener() {
			public boolean onMarkerClick(final Marker marker) {
				// 正常点击
				Bundle bundle = marker.getExtraInfo();
				BusinessEty ety = (BusinessEty) bundle.getSerializable("ety");
				if (ety != null) {
					View view = initPopView(ety);
					LatLng lal = markLatLng = marker.getPosition();
					
					showTransportTypeView();

					InfoWindow mInfoWindow = new InfoWindow(view, lal, -63);
					mBaiduMap.showInfoWindow(mInfoWindow);

					routePlan();
				}
				return true;
			}
		});
	}

	/***
	 * Mark点弹窗
	 * 
	 * @param ety
	 * @return
	 */
	private View initPopView(BusinessEty ety) {
		View view = LayoutInflater.from(self).inflate(R.layout.layout_store_map_pop, null);
		TextView tv_bname = (TextView) view.findViewById(R.id.tv_bname);
		tv_bname.setText(ety.getBusinessname());
		return view;
	}


	/***
	 * 路线计算 （这里只计算公交）
	 */
	private void routePlan() {
		// 设置起终点信息
		PlanNode stNode = PlanNode.withLocation(curLatLng);
		PlanNode enNode = PlanNode.withLocation(markLatLng);

		String city = "";
		if (curLocation != null) {
			city = curLocation.getCity();
		} else {
			city = new DB_Location(self).getCityName();
		}
		mSearch.transitSearch((new TransitRoutePlanOption()).from(stNode).city(city).to(enNode));
	}

	private void showTransportTypeView() {
		if (ll_transport.getVisibility() == View.GONE) {
			ll_transport.startAnimation(AnimationUtils.loadAnimation(self, R.anim.push_in_from_bottom));
		}
		ll_transport.setVisibility(View.VISIBLE);
	}

	private void hideTransportTypeView() {
		if (ll_transport.getVisibility() == View.VISIBLE) {
			ll_transport.setVisibility(View.GONE);
		}
		ll_transport.startAnimation(AnimationUtils.loadAnimation(self, R.anim.push_out_to_bottom));
	}


	/******************** 搜索相关 *******************/

	private void searchInfos() {
		mSearch = RoutePlanSearch.newInstance();
		mSearch.setOnGetRoutePlanResultListener(searchLister);
	}

	OnGetRoutePlanResultListener searchLister = new OnGetRoutePlanResultListener() {
		@Override
		public void onGetWalkingRouteResult(WalkingRouteResult result) {
			// TODO Auto-generated method stub
		}

		@Override
		public void onGetTransitRouteResult(TransitRouteResult result) {
			// TODO Auto-generated method stub
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
				Toast.show(self, "抱歉，未搜索到公交路线");
			}
			if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
				// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
				// result.getSuggestAddrInfo()
				return;
			}
			List<TransitRouteLine> list = result.getRouteLines();
			if (list != null && list.size() > 0) {
				// 底部默认的显示
				TransitRouteLine line = list.get(0);
				int duration = line.getDuration();
				int hour = duration / (60 * 60);
				int min = (duration - (hour * 60 * 60)) / 60;
				String plan = String.format("约%s小时%s分", hour, min);
				tv_bus_plan.setText(plan);
			}
		}

		@Override
		public void onGetDrivingRouteResult(DrivingRouteResult result) {
			// TODO Auto-generated method stub
		}
	};

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
			// if (isFirstLoc) {
			isFirstLoc = false;
			LatLng ll = curLatLng = new LatLng(location.getLatitude(), location.getLongitude());

			// 存储地址(反过来用)
			String nlatitude = Double.toString(location.getLongitude());
			String nlontitude = Double.toString(location.getLatitude());
			DB_Location db_location = new DB_Location(self);
			db_location.setLatitude(nlatitude);
			db_location.setLontitude(nlontitude);

			if (getIntent().hasExtra(STORE_NAV)) {
				// 某个商家导航不要移动到定位位置
				return;
			}
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
			mBaiduMap.animateMapStatus(u);
			mLocationClient.stop();
			// }
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
		case R.id.tv_bus:
		case R.id.tv_bus_plan:
			RoutePlanOP.instance().setRouteType("BUS");
			RoutePlanOP.instance().setMarkLatLng(markLatLng);
			startActivity(new Intent(self,SearchPickActivity.class));
			break;
		case R.id.tv_car:
			RoutePlanOP.instance().setRouteType("CAR");
			RoutePlanOP.instance().setMarkLatLng(markLatLng);
			startActivity(new Intent(self,SearchPickActivity.class));
			break;
		case R.id.tv_walk:
			RoutePlanOP.instance().setRouteType("WALK");
			RoutePlanOP.instance().setMarkLatLng(markLatLng);
			startActivity(new Intent(self,SearchPickActivity.class));
			break;
		default:
			break;
		}
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		icon_mark_food.recycle();
		icon_mark_boby.recycle();
		icon_mark_buid.recycle();
		icon_mark_edu.recycle();
		icon_mark_fun.recycle();
		icon_mark_mei.recycle();
		icon_mark_server.recycle();
		icon_mark_shopping.recycle();
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
