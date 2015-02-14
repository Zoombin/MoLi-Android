package com.imooly.android.entity;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/***
 * 实体店 - 城市列表
 * 
 * @author LSD
 * 
 */
public class RspStoreCityList extends ServiceResult implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@SerializedName("data")
	public Data data;

	public static class Data implements Serializable{
		
		private static final long serialVersionUID = 1L;
		
		@SerializedName("current")
		public City current;

		@SerializedName("citylist")
		public List<CityGroup> citylist;

		public City getCurrent() {
			return current;
		}

		public void setCurrent(City current) {
			this.current = current;
		}

		public List<CityGroup> getCitylist() {
			return citylist;
		}

		public void setCitylist(List<CityGroup> citylist) {
			this.citylist = citylist;
		}

	}

	public static class CityGroup implements Serializable{
		
		private static final long serialVersionUID = 1L;
		
		String pname;
		List<City> cities;

		public String getPname() {
			return pname;
		}

		public void setPname(String pname) {
			this.pname = pname;
		}

		public List<City> getCities() {
			return cities;
		}

		public void setCities(List<City> cities) {
			this.cities = cities;
		}

	}

	public static class City implements Serializable{
		
		private static final long serialVersionUID = 1L;
		
		private int cityid;
		private String name;

		public int getCityid() {
			return cityid;
		}

		public void setCityid(int cityid) {
			this.cityid = cityid;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}
}
