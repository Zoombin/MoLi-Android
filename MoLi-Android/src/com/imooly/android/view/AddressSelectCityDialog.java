package com.imooly.android.view;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.adapter.AddressProvinceListAdapter;
import com.imooly.android.adapter.ProdectFilterAddressAdapter;
import com.imooly.android.entity.RspAddressCityTown;
import com.imooly.android.entity.RspAddressCityTown.City;
import com.imooly.android.entity.RspAddressCityTown.Town;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.widget.CustomDialog;
import com.imooly.android.widget.CustomDialog.CGravity;

/***
 * 
 * @author
 * 
 */
public class AddressSelectCityDialog {
	static List<?> cityList;
	private Context mContext;
	private int pid = 0;
	private ListView listView = null;
	private List<City> cities;
	private List<Integer> cids;
	private List<Town> towns;
	
	public interface AddressSelectCityCallBack {
		void onSelect(Object model);
		void onSelectCid(int cid);
		void onSaveTown(List<Town> towns);
	}

	public void show(Context context, int pid, final AddressSelectCityCallBack callback) {
		mContext = context;
		this.pid = pid;
		final View view = LayoutInflater.from(context).inflate(R.layout.dialog_filter_address_two, null);
		final CustomDialog dialog = CustomDialog.create(context);
		dialog.setCanceledOnTouchOutside(false);

		((TextView) view.findViewById(R.id.title)).setText("选择地区");

		listView = (ListView) view.findViewById(R.id.list);
		
		getCityList();

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Object model = parent.getAdapter().getItem(position);
				
				
				int cid = cids.get(position);
				
				
				
				for (City city : cities) {
					if (city.getCid() == cid) {
						towns =city.getAreas();
					}
				}
				
				if (callback != null) {
					callback.onSaveTown(towns);
					callback.onSelectCid(cid);
					callback.onSelect(model);
				}
				dialog.dismiss();
			}
		});

		dialog.setContentVw(view).setGravity(CGravity.CENTER).show();
	}
	
	private void getCityList() {
		
		Api.getAdressCityTown(mContext, pid, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				RspAddressCityTown rsp = (RspAddressCityTown)rspData;
				RspAddressCityTown.Data data = rsp.data;
				cities = data.getCity();
				List<String> cityNames = new ArrayList<String>();
				cids = new ArrayList<Integer>();
				
				for (City city : cities) {
					cityNames.add(city.getName());
					cids.add(city.getCid());
				}
				
				AddressProvinceListAdapter adapter = new AddressProvinceListAdapter();
				listView.setAdapter(adapter);
				adapter.setData("city", cityNames);
				
			}
			
			@Override
			public void failed(String msg) {
				
				
			}
		}, RspAddressCityTown.class);
	}
	
	
}
