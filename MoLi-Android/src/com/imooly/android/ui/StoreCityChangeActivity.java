package com.imooly.android.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.adapter.StoreAddressSelectAdapter;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.db.DB_Location;
import com.imooly.android.entity.RspStoreCityList;
import com.imooly.android.entity.RspStoreCityList.City;
import com.imooly.android.entity.RspStoreCityList.CityGroup;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.utils.Utils;

/**
 * 实体店 - 选择城市（省）
 * 
 * @author lsd
 * 
 */
public class StoreCityChangeActivity extends BaseActivity implements View.OnClickListener {
	private RelativeLayout ll_title;
	private ImageView iv_back;
	private TextView tv_title;
	private TextView tv_location_city;

	StoreAddressSelectAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_storechangecity);

		logActivityName(this);

		initView();
		initData();
	}

	private void initView() {
		ll_title = (RelativeLayout) findViewById(R.id.ll_title);

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);

		tv_title = (TextView) findViewById(R.id.tv_title);
		tv_location_city = (TextView) findViewById(R.id.tv_location_city);

		GridView gridView = (GridView) findViewById(R.id.city_glist);
		gridView.setAdapter(adapter = new StoreAddressSelectAdapter());
		gridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				City city = (City) adapter.getItem(position);
				int cityid = city.getCityid();
				new DB_Location(self).setCurCityId(cityid + "");// 当前城市是服务器返回的
				new DB_Location(self).setCurCityName(city.getName());

				setResult(RESULT_OK);
				finish();
			}
		});
	}

	private void initData() {
		/** IP地址 */
		String ip = Utils.getLocalIpAddress();

		DB_Location db_location = new DB_Location(self);
		String lng = db_location.getLontitude();
		String lat = db_location.getLatitude();

		Api.storeCityList(self, true, ip, lng, lat, new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {
				// TODO Auto-generated method stub
				RspStoreCityList rsp = (RspStoreCityList) rspData;
				City curCity = rsp.data.getCurrent();
				String cityName = curCity.getName();
				tv_location_city.setText(cityName);

				List<City> cityList = new ArrayList<City>();
				List<CityGroup> cityGroups = rsp.data.getCitylist();
				if (cityGroups != null && cityGroups.size() > 0) {
					for (CityGroup cityGroup : cityGroups) {
						List<City> citys = cityGroup.getCities();
						if (citys != null && citys.size() > 0) {
							cityList.addAll(citys);
						}
					}
				}
				adapter.setData(cityList);
			}

			@Override
			public void failed(String msg) {
				// TODO Auto-generated method stub

			}
		}, RspStoreCityList.class);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.iv_back:
			finish();
			break;
		}
	}
}
