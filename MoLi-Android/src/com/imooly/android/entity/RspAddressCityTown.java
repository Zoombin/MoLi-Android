package com.imooly.android.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/***
 * 获取省数据列表
 * 
 * @author 
 * 
 */
public class RspAddressCityTown extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {

		private List<City> city;

		public List<City> getCity() {
			return city;
		}

		public void setCity(List<City> city) {
			this.city = city;
		}
	}

	public class City {

		private int cid;
		private String name;
		private List<Town> areas;
		
		public int getCid() {
			return cid;
		}
		public void setCid(int cid) {
			this.cid = cid;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public List<Town> getAreas() {
			return areas;
		}
		public void setAreas(List<Town> areas) {
			this.areas = areas;
		}


	}
	
	public class Town {
		private int aid;
		private String name;
		
		public int getAid() {
			return aid;
		}
		public void setAid(int aid) {
			this.aid = aid;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
	}
}
