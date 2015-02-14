package com.imooly.android.db;

import android.content.Context;

public class DB_Location extends DB_Base {

	public DB_Location(Context context) {
		super(context);
		setName("DB_LOCATION");
		getSettings();
	}
	
	/*****百度地图定位成功的数据 **/
	public void setLatitude(String nlatitude) {
		setSaveString("nlatitude", nlatitude);
	}

	public String getLatitude() {
		return getSaveString("nlatitude", "");
	}

	public void setLontitude(String nlontitude) {
		setSaveString("nlontitude", nlontitude);
	}

	public String getLontitude() {
		return getSaveString("nlontitude", "");
	}
	
	public void setCityName(String cityName) {
		setSaveString("cityname", cityName);
	}

	public String getCityName() {
		return getSaveString("cityname", "");
	}
	
	public void setCityId(String cityId) {
		setSaveString("cityid", cityId);
	}

	public String getCityId() {
		return getSaveString("cityid", "");
	}
	
	
	
	
	/******服务器返回的城市数据**/
	public void setCurCityId(String cityId) {
		setSaveString("curcityid", cityId);
	}

	public String getCurCityId() {
		return getSaveString("curcityid", "");
	}
	
	public void setCurCityName(String cityname) {
		setSaveString("curcityname", cityname);
	}

	public String getCurCityName() {
		return getSaveString("curcityname", "");
	}
	
}
