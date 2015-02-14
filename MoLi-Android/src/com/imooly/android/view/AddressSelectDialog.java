package com.imooly.android.view;

import java.util.List;

import android.content.Context;

import com.imooly.android.entity.RspAddressCityTown.Town;
import com.imooly.android.entity.RspAddressProvince.ItemProvince;
import com.imooly.android.entity.RspAreaGood.CityEty;
import com.imooly.android.entity.RspAreaGood.ProvinceEty;
import com.imooly.android.view.AddressSelectCityDialog.AddressSelectCityCallBack;
import com.imooly.android.view.AddressSelectProvinceDialog.AddressProvinceCallBack;
import com.imooly.android.view.AddressSelectTownDialog.AddressSelectTownCallBack;

/***
 * 
 * @author
 * 
 */
public class AddressSelectDialog {
	private Context context;
	private List<ProvinceEty> cityList;
	private AddressFilterCallBack addressSelectSallBack;

	private int mPid = 0;
	private int mCid = 0;
	private int mAid = 0;

	private ProvinceEty tempProvince;
	private CityEty tempRegion;

	private List<Town> mTowns = null;
	
	private StringBuffer addressBuffer;

	public interface AddressFilterCallBack {
		void onSelectAid(int aid);

		void onSelectCid(int cid);

		void onSelectPid(int pid);
		
		void onSelectAddress(String address);
	}

	public void show(final Context context, final AddressFilterCallBack callback) {
		this.context = context;

		addressSelectSallBack = callback;

		new AddressSelectProvinceDialog().show(context, new AddressProvinceCallBack() {

			@Override
			public void onSelect(Object model) {
				ItemProvince itemProvince = (ItemProvince) model;
				String provinceName = itemProvince.getName();
				mPid = itemProvince.getPid();
				addressBuffer = new StringBuffer(provinceName);
				
				new AddressSelectCityDialog().show(context, mPid, new AddressSelectCityCallBack() {

					public void onSaveTown(List<Town> towns) {
						mTowns = towns;

					};

					public void onSelectCid(int cid) {
						mCid = cid;
					};
					
					@Override
					public void onSelect(Object model) {
						if(!addressBuffer.toString().equals((String)model)) {
							addressBuffer.append((String) model);
						}
						
						new AddressSelectTownDialog().show(context, mTowns, new AddressSelectTownCallBack() {

							public void onSelectAid(int aid) {
								mAid = aid;
							};
							
							@Override
							public void onSelect(Object model) {
								Town town = (Town) model;
								addressBuffer.append(town.getName());
								callback.onSelectAid(mAid);
								callback.onSelectCid(mCid);
								callback.onSelectPid(mPid);
								callback.onSelectAddress(addressBuffer.toString());
							}
						});
					}
				});

			}
		});
	}
}
