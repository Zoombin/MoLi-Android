package com.imooly.android.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.adapter.StoreHotListAdapter;
import com.imooly.android.adapter.StoreModulesAdapter;
import com.imooly.android.adapter.StoreRandListAdapter;
import com.imooly.android.base.BaseFragment;
import com.imooly.android.db.DB_Location;
import com.imooly.android.entity.RspAdvertiseIndexads;
import com.imooly.android.entity.RspAdvertiseIndexads.Info;
import com.imooly.android.entity.RspAdvertiseIndexads.Tplcontent;
import com.imooly.android.entity.RspBusinessCirclelist;
import com.imooly.android.entity.RspBusinessCirclelist.Circle;
import com.imooly.android.entity.RspBusinessSearch.BusinessEty;
import com.imooly.android.entity.RspBusinessStoreList;
import com.imooly.android.entity.RspStoreCityList;
import com.imooly.android.entity.RspStoreCityList.City;
import com.imooly.android.entity.RspStoreCityList.CityGroup;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.tool.Config;
import com.imooly.android.ui.SearchMapActivity;
import com.imooly.android.ui.StoreDetailActivity;
import com.imooly.android.ui.StoreSearchActivity;
import com.imooly.android.utils.Utils;
import com.imooly.android.view.StoreAddressSelectDialog;
import com.imooly.android.view.StoreAddressSelectDialog.AddressSelectCallBack;
import com.imooly.android.widget.CannotRollGridView;
import com.imooly.android.widget.CannotRollListView;
import com.imooly.android.widget.LoadingView;
import com.imooly.android.widget.NoDataView;
import com.imooly.android.widget.ObservableScrollView;
import com.imooly.android.widget.ObservableScrollView.OnScrollListener;

/***
 * 实体店
 * 
 * @author LSD
 * 
 */
public class StoreFragment extends BaseFragment implements OnClickListener {
	public static int SELECT_CITY = 100;
	private ObservableScrollView layout_store;
	private LoadingView layout_loading;
	private View shadow_view;
	public NoDataView noDataView;

	RelativeLayout store_parent;
	LinearLayout ll_title_store;
	ImageView location_mark;
	CannotRollGridView store_modules;

	LinearLayout ll_level2, ll_level3;
	LinearLayout ll_level2_content, ll_level3_content;
	CannotRollListView store_ad_list;

	TextView level2_txt, level3_txt, location_city;
	EditText et_input;
	Button btn_rull_up;

	StoreAddressSelectDialog cDialog;// 选择城市Dialog

	// 城市数据
	List<CityGroup> cityList;
	City curCity;

	// 附近商家数据
	List<BusinessEty> tempBusinessList;

	// 商圈数据
	List<Circle> tempCircleList;
	int circleIndex = -1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_store, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		initView();
		getData();
	}

	private void initView() {
		layout_store = (ObservableScrollView) mActivity.findViewById(R.id.layout_store);
		layout_store.setScrollListener(onScrollListener);
		layout_loading = (LoadingView) mActivity.findViewById(R.id.layout_storeloading);
		noDataView = (NoDataView) mActivity.findViewById(R.id.layout_storenodata);
		noDataView.setOnClickListener(listener);

		shadow_view = new View(mActivity);
		shadow_view.setBackgroundColor(Color.parseColor("#40000000"));
		shadow_view.setVisibility(View.GONE);
		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mActivity.addContentView(shadow_view, params);

		store_parent = (RelativeLayout) mActivity.findViewById(R.id.store_parent);
		// top View
		ll_title_store = (LinearLayout) mActivity.findViewById(R.id.ll_title_store);

		location_city = (TextView) mActivity.findViewById(R.id.location_city);
		location_city.setOnClickListener(this);

		location_mark = (ImageView) mActivity.findViewById(R.id.location_mark);
		location_mark.setOnClickListener(this);

		store_modules = (CannotRollGridView) mActivity.findViewById(R.id.store_modules);
		store_modules.setVisibility(View.GONE);

		ll_level2 = (LinearLayout) mActivity.findViewById(R.id.ll_level2);
		ll_level3 = (LinearLayout) mActivity.findViewById(R.id.ll_level3);
		ll_level2.setVisibility(View.GONE);
		ll_level3.setVisibility(View.GONE);
		// content view
		ll_level2_content = (LinearLayout) mActivity.findViewById(R.id.ll_level2_content);
		ll_level3_content = (LinearLayout) mActivity.findViewById(R.id.ll_level3_content);
		store_ad_list = (CannotRollListView) mActivity.findViewById(R.id.store_ad_list);

		level2_txt = (TextView) mActivity.findViewById(R.id.level2_txt);
		level3_txt = (TextView) mActivity.findViewById(R.id.level3_txt);

		btn_rull_up = (Button) mActivity.findViewById(R.id.btn_rull_up);
		btn_rull_up.setOnClickListener(this);

		et_input = (EditText) mActivity.findViewById(R.id.et_input);
		et_input.setOnClickListener(this);
	}

	private void getData() {
		getAdData();
		getCityInfo();
	}

	//切换城市 刷新数据
	public void refrashData() {
		DB_Location db_Location = new DB_Location(mActivity);
		String cityid = db_Location.getCurCityId();
		String cityName = db_Location.getCurCityName();

		curCity = new City();
		curCity.setCityid(Integer.parseInt(cityid));
		curCity.setName(cityName);
		// curCity.setName(cityName);
		
		circleIndex  = -1;
		getCirclelist(cityid, true);

	}

	private void getCityInfo() {
		/** IP地址 */
		String ip = Utils.getLocalIpAddress();

		/** 定位信息 */
		DB_Location db_location = new DB_Location(mActivity);
		String lng = db_location.getLontitude();
		String lat = db_location.getLatitude();

		/** 城市列表 */
		Api.storeCityList(mActivity, false, ip, lng, lat, new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {
				RspStoreCityList rsp = (RspStoreCityList) rspData;
				Config.setStoreCityList(rsp);
				
				initCityList(rsp);
			}

			@Override
			public void failed(String msg) {
				RspStoreCityList rsp = Config.getStoreCityList();
				if (rsp != null) {
					initCityList(rsp);
				}
			}
		}, RspStoreCityList.class);
	}

	private void initCityList(RspStoreCityList rsp) {
		cityList = rsp.data.getCitylist();
		curCity = rsp.data.getCurrent();

		String cityName = curCity.getName();
		int cityid = curCity.getCityid();

		location_city.setText(cityName);
		new DB_Location(mActivity).setCurCityId(cityid + "");// 服务器返回的当前城市
		new DB_Location(mActivity).setCurCityName(cityName);

		getNetData(cityid + "");
	}
	
	/** 实体店广告 */
	private void getAdData() {
		Api.getAdvertiseShopads(mActivity, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				RspAdvertiseIndexads rsp = (RspAdvertiseIndexads) rspData;
				if (rsp.data != null) {
					layout_store.setVisibility(View.VISIBLE);
					layout_loading.setVisibility(View.GONE);
					layout_loading.postHandle(LoadingView.success);
					noDataView.setVisibility(View.GONE);

					store_modules.setVisibility(View.VISIBLE);
					ll_level2.setVisibility(View.VISIBLE);

					Config.setStoreRspAdvertiseIndexads(rsp);
					
					initAdvertiseShopads(rsp);
				}
			}

			@Override
			public void failed(String msg) {
				RspAdvertiseIndexads rspadvertiseindexads = Config.getStoreRspAdvertiseIndexads();
				if (rspadvertiseindexads != null) {
					layout_store.setVisibility(View.VISIBLE);
					layout_loading.setVisibility(View.GONE);
					layout_loading.postHandle(LoadingView.success);
					noDataView.setVisibility(View.GONE);
					store_modules.setVisibility(View.VISIBLE);
					ll_level2.setVisibility(View.VISIBLE);
					
					initAdvertiseShopads(rspadvertiseindexads);
				} else {
					layout_store.setVisibility(View.GONE);
					layout_loading.setVisibility(View.GONE);
					layout_loading.postHandle(LoadingView.success);
					noDataView.setVisibility(View.VISIBLE);
					noDataView.postHandle(NoDataView.nonet);
				}

			}
		}, RspAdvertiseIndexads.class);
	}

	private void initAdvertiseShopads(RspAdvertiseIndexads rsp) {
		List<Tplcontent> tplcontents = rsp.data.getTplcontent();
		if (tplcontents != null && tplcontents.size() > 0) {
			List<Info> adInfos = new ArrayList<RspAdvertiseIndexads.Info>();
			List<Info> shortcutInfos = new ArrayList<RspAdvertiseIndexads.Info>();
			for (Tplcontent tplcontent : tplcontents) {
				if ("T004".equals(tplcontent.getType())) {
					// 广告数据
					List<Info> list = tplcontent.getInfos();
					if (list != null && list.size() > 0) {
						adInfos.addAll(list);
					}
				}
				if ("T005".equals(tplcontent.getType())) {
					// 快捷方式数据
					List<Info> list = tplcontent.getInfos();
					if (list != null && list.size() > 0) {
						shortcutInfos.addAll(list);
					}
				}

			}
			// 设置快捷方式数据
			store_modules.setAdapter(new StoreModulesAdapter(mActivity, shortcutInfos));

			// 设置广告数据(热推商家)
			StoreHotListAdapter adapter = new StoreHotListAdapter(mActivity);
			store_ad_list.setAdapter(adapter);
			adapter.setData(adInfos);
		}
	}
	
	private void getNetData(final String cityid) {
		cDialog = new StoreAddressSelectDialog(mActivity, callBack);
		cDialog.setShadow_view(shadow_view);

		String ip = Utils.getLocalIpAddress();

		DB_Location db_location = new DB_Location(mActivity);
		String lng = db_location.getLontitude();
		String lat = db_location.getLatitude();

		/** 附近搜索 */
		Api.storeNear(mActivity, cityid + "", ip, lng, lat, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				layout_store.setVisibility(View.VISIBLE);
				layout_loading.setVisibility(View.GONE);
				layout_loading.postHandle(LoadingView.success);
				noDataView.setVisibility(View.GONE);
				
				RspBusinessStoreList rsp = (RspBusinessStoreList) rspData;
				tempBusinessList = rsp.data.getStorelist();
				if (tempBusinessList != null && tempBusinessList.size() > 0) {
					location_mark.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void failed(String msg) {
			}
		}, RspBusinessStoreList.class);

		/** 猜你喜欢 */
		ll_level3_content.removeAllViews();
		Api.storeRand(mActivity, cityid, ip, lng, lat, new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {
				RspBusinessStoreList rsp = (RspBusinessStoreList) rspData;
				Config.setBusinessStoreList(rsp);
				initBusinessStoreList(rsp);
			}

			@Override
			public void failed(String msg) {
				RspBusinessStoreList rsp = Config.getBusinessStoreList();
				if (rsp != null) {
					initBusinessStoreList(rsp);
				}
			}
		}, RspBusinessStoreList.class);

		// 获取当前城市下的商圈
		getCirclelist(cityid, false);
	}

	private void initBusinessStoreList(RspBusinessStoreList rsp) {
		if (rsp.data != null) {
			ll_level3.setVisibility(View.VISIBLE);
			View level2ListLayout = LayoutInflater.from(mActivity).inflate(R.layout.layout_store_content_item, null);
			CannotRollListView listView = (CannotRollListView) level2ListLayout.findViewById(R.id.store_content_list);
			StoreRandListAdapter adapter = new StoreRandListAdapter();
			listView.setDivider(getResources().getDrawable(R.drawable.comm_dash_line));
			listView.setAdapter(adapter);
			listView.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					// TODO Auto-generated method stub
					BusinessEty ety = (BusinessEty) parent.getAdapter().getItem(position);
					startActivity(new Intent(mActivity, StoreDetailActivity.class).putExtra(StoreDetailActivity.EXTRA_BUSNESSID,
							ety.getBusinessid()));
				}
			});
			adapter.setData(rsp.data.getStorelist());
			ll_level3_content.addView(level2ListLayout);
		}
	}
	
	/** 获取商圈 */
	private void getCirclelist(String cityid, final boolean needRefrash) {
		String ip = Utils.getLocalIpAddress();
		DB_Location db_location = new DB_Location(mActivity);
		String lng = db_location.getLontitude();
		String lat = db_location.getLatitude();

		Api.businessCirclelist(mActivity, cityid, ip, lng, lat, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				RspBusinessCirclelist rsp = (RspBusinessCirclelist) rspData;
				Config.setBusinessCirclelist(rsp);
				
				initBusinessCirclelist(rsp, needRefrash);
			}

			@Override
			public void failed(String msg) {
				RspBusinessCirclelist rsp = Config.getBusinessCirclelist();
				if (rsp != null) {
					initBusinessCirclelist(rsp, needRefrash);
				}
			}
		}, RspBusinessCirclelist.class);
	}

	private void initBusinessCirclelist(RspBusinessCirclelist rsp, boolean needRefrash) {
		if (rsp.data != null) {
			List<Circle> circleList = rsp.data.getCirclelist();// 商圈
			boolean b = false;
			for(Circle circle : circleList){
				if("全城".equals(circle.getCirclename())){
					b = true;
					break;
				}
			}
			if(!b){
				Circle circle = new Circle();
				circle.setCid("0");
				circle.setCirclename("全城");
				circleList.add(0, circle);
			}
			
			tempCircleList = circleList;
			
			if (needRefrash) {
				cDialog.refrashView(tempCircleList, curCity, circleIndex);
			}
		}
	}
	
	AddressSelectCallBack callBack = new AddressSelectCallBack() {
		@Override
		public void onSelect(Circle mode, int index) {
			// TODO Auto-generated method stub
			location_city.setText(mode.getCirclename());
			circleIndex = index;
		}
	};

	OnScrollListener onScrollListener = new OnScrollListener() {
		@Override
		public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
			// TODO Auto-generated method stub
			if (y > 150) {
				btn_rull_up.setVisibility(View.VISIBLE);
			} else {
				btn_rull_up.setVisibility(View.INVISIBLE);
			}
		}
	};
	
	OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			switch (view.getId()) {
			case R.id.bt_reload:
				getData();
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.location_city:
			// 城市切换
			cDialog.showPopWin(ll_title_store, tempCircleList, curCity, circleIndex);
			break;
		case R.id.location_mark:
			startActivity(new Intent(mActivity, SearchMapActivity.class).putExtra(SearchMapActivity.SEARCH_ENTITYS,
					(Serializable) tempBusinessList));
			break;
		case R.id.et_input:
			startActivity(new Intent(mActivity, StoreSearchActivity.class));
			break;

		case R.id.btn_rull_up:
			layout_store.fullScroll(ScrollView.FOCUS_UP);
			btn_rull_up.setVisibility(View.INVISIBLE);
			break;

		default:
			break;
		}
	}
}
