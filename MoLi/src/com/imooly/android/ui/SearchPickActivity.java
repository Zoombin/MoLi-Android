package com.imooly.android.ui;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMap.OnMapClickListener;
import com.baidu.mapapi.map.InfoWindow;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.imooly.android.R;
import com.imooly.android.adapter.StoreRoutePlanAdapter;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.db.DB_Location;
import com.imooly.android.entity.RspBusinessSearch.BusinessEty;
import com.imooly.android.tool.RoutePlanOP;
import com.imooly.android.view.RoutePlanPickStartPoint;
import com.imooly.android.view.RoutePlanPickStartPoint.PickCallback;
import com.imooly.android.widget.Toast;

/**
 * 地图选点
 * 
 * @author lsd
 * 
 */
public class SearchPickActivity extends BaseActivity implements View.OnClickListener, OnItemClickListener {
	/**
	 * 路线计划
	 * 
	 * @author lsd
	 * 
	 */
     enum RoutePlanModel {
		BUS, CAR, WALK;
	}

    ImageView iv_back;
	MapView mMapView;
	BaiduMap mBaiduMap;
	ImageButton zoom_in;
	ImageButton zoom_out;
	ImageButton locateBtn;

	/*** 路线规划 ******/
	private RoutePlanModel rModel = RoutePlanModel.BUS;
	View ll_routeplay;
	private RelativeLayout ll_title1;
	private ImageView iv_back1;
	private LinearLayout ll_select_position;
	private TextView tv_mylocation;
	private TextView tv_mydestination;
	private ImageView iv_bus_level3;
	private ImageView iv_car_level3;
	private ImageView iv_walk_level3;
	private View line_bus;
	private View line_walk;
	private View line_car;
	ProgressBar progress;
	ListView listView;
	StoreRoutePlanAdapter adapter;

	BusinessEty tempRoutPlayEty;// 导航是起点的商家信息

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
		setContentView(R.layout.activity_mappick);

		logActivityName(this);

		init();
		locationInfos();
		searchInfos();

		getData();
	}

	private void init() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
		zoom_in = (ImageButton) findViewById(R.id.zoom_in);
		zoom_out = (ImageButton) findViewById(R.id.zoom_out);
		locateBtn = (ImageButton) findViewById(R.id.locateBtn);
		zoom_in.setOnClickListener(this);
		zoom_out.setOnClickListener(this);
		locateBtn.setOnClickListener(this);

		// 路径规划
		ll_routeplay = findViewById(R.id.ll_routeplay);
		ll_title1 = (RelativeLayout) findViewById(R.id.ll_title1);
		ll_title1.setOnClickListener(this);
		iv_back1 = (ImageView) findViewById(R.id.iv_back1);
		iv_back1.setOnClickListener(this);
		ll_select_position = (LinearLayout) findViewById(R.id.ll_select_position);
		ll_select_position.setOnClickListener(this);
		tv_mylocation = (TextView) findViewById(R.id.tv_mylocation);
		tv_mydestination = (TextView) findViewById(R.id.tv_mydestination);

		iv_bus_level3 = (ImageView) findViewById(R.id.iv_bus_level3);
		iv_car_level3 = (ImageView) findViewById(R.id.iv_car_level3);
		iv_walk_level3 = (ImageView) findViewById(R.id.iv_walk_level3);
		line_bus = findViewById(R.id.line_bus);
		line_car = findViewById(R.id.line_car);
		line_walk = findViewById(R.id.line_walk);
		iv_bus_level3.setOnClickListener(this);
		iv_car_level3.setOnClickListener(this);
		iv_walk_level3.setOnClickListener(this);

		listView = (ListView) findViewById(R.id.list);
		listView.setAdapter(adapter = new StoreRoutePlanAdapter());
		listView.setOnItemClickListener(this);
		
		progress = (ProgressBar) findViewById(R.id.progress);

		// 地图初始化
		mMapView = (MapView) findViewById(R.id.bmapView);
		// 隐藏缩放控件
		mMapView.showZoomControls(false);
		mBaiduMap = mMapView.getMap();
		// 放大倍数
		MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(14.0f);
		mBaiduMap.setMapStatus(msu);

		// 地图相关事件监听
		mapListener();
	}

	private void getData() {
		DB_Location db_Location = new DB_Location(self);
		double lat = Double.parseDouble(db_Location.getLontitude());
		double lng = Double.parseDouble(db_Location.getLatitude());
		curLatLng = new LatLng(lat, lng);

		markLatLng = RoutePlanOP.instance().getMarkLatLng();
		String type = RoutePlanOP.instance().getRouteType();
		if("BUS".equals(type)){
			rModel = RoutePlanModel.BUS;
		}
		if("CAR".equals(type)){
			rModel = RoutePlanModel.CAR;
		}
		if("WALK".equals(type)){
			rModel = RoutePlanModel.WALK;
		}
		
		trafficTypeChange();
	}

	/***
	 * 地图的相关事件监听
	 */
	public void mapListener() {
		/** 地图点击事件 */
		mBaiduMap.setOnMapClickListener(new OnMapClickListener() {
			@Override
			public boolean onMapPoiClick(MapPoi arg0) {
				// TODO Auto-generated method stub
				LatLng latlng = arg0.getPosition();
				View view = initSelectPointView(arg0.getPosition());

				InfoWindow mInfoWindow = new InfoWindow(view, latlng, 0);
				mBaiduMap.showInfoWindow(mInfoWindow);

				return false;
			}

			@Override
			public void onMapClick(LatLng arg0) {
				// TODO Auto-generated method stub
			}
		});
	}

	/***
	 * 地图选点
	 * 
	 * @param latLng
	 * @return
	 */
	private View initSelectPointView(final LatLng latLng) {
		View view = LayoutInflater.from(self).inflate(R.layout.layout_store_map_pop, null);
		TextView tv_bname = (TextView) view.findViewById(R.id.tv_bname);
		tv_bname.setText("设该点为起点");
		view.setTag(latLng);
		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mBaiduMap.hideInfoWindow();
				showRoutePlanView();

				tv_mylocation.setText("地图上的点");
				curLatLng = latLng;

				routePlan(rModel);
			}
		});
		return view;
	}

	/***
	 * 路线计算
	 */
	private void routePlan(RoutePlanModel model) {
		progress.setVisibility(View.VISIBLE);
		listView.setVisibility(View.GONE);
		
		// 设置起终点信息
		PlanNode stNode = PlanNode.withLocation(curLatLng);
		PlanNode enNode = PlanNode.withLocation(markLatLng);

		switch (model) {
		case WALK:
			mSearch.walkingSearch((new WalkingRoutePlanOption()).from(stNode).to(enNode));
			break;
		case BUS:
			String city = "";
			if (curLocation != null) {
				city = curLocation.getCity();
			} else {
				city = new DB_Location(self).getCityName();
			}
			mSearch.transitSearch((new TransitRoutePlanOption()).from(stNode).city(city).to(enNode));
			break;
		case CAR:
			mSearch.drivingSearch((new DrivingRoutePlanOption()).from(stNode).to(enNode));
			break;
		default:
			break;
		}
	}

	private void showRoutePlanView() {
		if (ll_routeplay.getVisibility() == View.GONE) {
			ll_routeplay.startAnimation(AnimationUtils.loadAnimation(self, R.anim.alpha_in));
		}
		ll_routeplay.setVisibility(View.VISIBLE);

		if (tempRoutPlayEty != null) {
			tv_mydestination.setText(tempRoutPlayEty.getBusinessname());
		}

		trafficTypeChange();

		mBaiduMap.hideInfoWindow();
	}

	private void hideRoutePlanView() {
		if (ll_routeplay.getVisibility() == View.VISIBLE) {
			ll_routeplay.setVisibility(View.GONE);
		}
		ll_routeplay.startAnimation(AnimationUtils.loadAnimation(self, R.anim.alpha_out));
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
			progress.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
				Toast.show(self, "抱歉，未搜索到步行路线");
			}
			if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
				// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
				// result.getSuggestAddrInfo()
				return;
			}
			List<WalkingRouteLine> list = result.getRouteLines();
			adapter.setData(list);
		}

		@Override
		public void onGetTransitRouteResult(TransitRouteResult result) {
			// TODO Auto-generated method stub
			progress.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
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
			}
			adapter.setData(list);
		}

		@Override
		public void onGetDrivingRouteResult(DrivingRouteResult result) {
			// TODO Auto-generated method stub
			progress.setVisibility(View.GONE);
			listView.setVisibility(View.VISIBLE);
			if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
				Toast.show(self, "抱歉，未搜索到驾车路线");
			}
			if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
				// 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
				// result.getSuggestAddrInfo()
				return;
			}
			List<DrivingRouteLine> list = result.getRouteLines();
			adapter.setData(list);
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

			// 移到当前位置
			MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll);
			mBaiduMap.animateMapStatus(u);
			mLocationClient.stop();
			// }
		}

		public void onReceivePoi(BDLocation poiLocation) {
		}
	}

	private void trafficTypeChange() {
		iv_car_level3.setSelected(false);
		iv_bus_level3.setSelected(false);
		iv_walk_level3.setSelected(false);
		line_bus.setVisibility(View.INVISIBLE);
		line_car.setVisibility(View.INVISIBLE);
		line_walk.setVisibility(View.INVISIBLE);
		switch (rModel) {
		case BUS:
			iv_bus_level3.setSelected(true);
			line_bus.setVisibility(View.VISIBLE);
			break;
		case CAR:
			iv_car_level3.setSelected(true);
			line_car.setVisibility(View.VISIBLE);
			break;
		case WALK:
			iv_walk_level3.setSelected(true);
			line_walk.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}

		routePlan(rModel);
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(ll_routeplay.getVisibility() == View.GONE){
			showRoutePlanView();
		}else{
			finish();
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_back:
			showRoutePlanView();
			break;
		case R.id.iv_back1:
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
		case R.id.iv_car_level3:
			rModel = RoutePlanModel.CAR;
			trafficTypeChange();
			break;
		case R.id.iv_bus_level3:
			rModel = RoutePlanModel.BUS;
			trafficTypeChange();
			break;
		case R.id.iv_walk_level3:
			rModel = RoutePlanModel.WALK;
			trafficTypeChange();
			break;
		case R.id.ll_select_position:
			// 地图选点
			RoutePlanPickStartPoint.showPickDialog(self, new PickCallback() {
				@Override
				public void onPick() {
					// TODO Auto-generated method stub
					Toast.show(self, "在地图上选择起点");
					hideRoutePlanView();
				}

				@Override
				public void onLocation() {
					// TODO Auto-generated method stub
					tv_mylocation.setText("我的位置");

					DB_Location db_Location = new DB_Location(self);
					double lat = Double.parseDouble(db_Location.getLontitude());
					double lng = Double.parseDouble(db_Location.getLatitude());
					curLatLng = new LatLng(lat, lng);

					routePlan(rModel);
				}
			});
			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		Object object = adapter.getItem(position);
		if (object instanceof TransitRouteLine) {
			RoutePlanOP.instance().setRouteType("BUS");
			RoutePlanOP.instance().setRouteLine(object);
		}
		if (object instanceof DrivingRouteLine) {
			RoutePlanOP.instance().setRouteType("CAR");
			RoutePlanOP.instance().setRouteLine(object);
		}
		if (object instanceof WalkingRouteLine) {
			RoutePlanOP.instance().setRouteType("WALK");
			RoutePlanOP.instance().setRouteLine(object);
		}

		startActivity(new Intent(self, SearchRoutePlanActivity.class));
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
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
