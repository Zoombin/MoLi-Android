package com.imooly.android.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/***
 * 发货地址
 * 
 * @author LSD
 * 
 */
public class RspGoodsArea extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {
		private List<AreaEty> area;

		public List<AreaEty> getArea() {
			return area;
		}

		public void setArea(List<AreaEty> area) {
			this.area = area;
		}

	}

	public static class AreaEty {
		private String pid;
		private String name;
		private List<CityEty> city;

		public String getPid() {
			return pid;
		}

		public void setPid(String pid) {
			this.pid = pid;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<CityEty> getCity() {
			return city;
		}

		public void setCity(List<CityEty> city) {
			this.city = city;
		}

	}

	public static class CityEty {
		private String cid;
		private String name;

		public String getCid() {
			return cid;
		}

		public void setCid(String cid) {
			this.cid = cid;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}

}
