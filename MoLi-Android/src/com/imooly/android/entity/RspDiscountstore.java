package com.imooly.android.entity;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/***
 * 获取实体店 商家详情
 * 
 * @author LSD
 * 
 */
public class RspDiscountstore extends ServiceResult implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SerializedName("data")
	public Data data;

	public static class Data implements Serializable {
		private String businessid;
		private String businessname;
		private String businessicon;
		private String industryicon;
		private String industry;
		private int starleve;
		private String address;
		private double lng;
		private double lat;
		private List<String> tel;
		private String description;
		private int totalcomment;
		private int favorites;

		public String getBusinessid() {
			return businessid;
		}

		public void setBusinessid(String businessid) {
			this.businessid = businessid;
		}

		public String getIndustryicon() {
			return industryicon;
		}

		public void setIndustryicon(String industryicon) {
			this.industryicon = industryicon;
		}

		public String getBusinessname() {
			return businessname;
		}

		public void setBusinessname(String businessname) {
			this.businessname = businessname;
		}

		public String getBusinessicon() {
			return businessicon;
		}

		public void setBusinessicon(String businessicon) {
			this.businessicon = businessicon;
		}

		public String getIndustry() {
			return industry;
		}

		public void setIndustry(String industry) {
			this.industry = industry;
		}

		public int getStarleve() {
			return starleve;
		}

		public void setStarleve(int starleve) {
			this.starleve = starleve;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public double getLng() {
			return lng;
		}

		public void setLng(double lng) {
			this.lng = lng;
		}

		public double getLat() {
			return lat;
		}

		public void setLat(double lat) {
			this.lat = lat;
		}

		public List<String> getTel() {
			return tel;
		}

		public void setTel(List<String> tel) {
			this.tel = tel;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

		public int getTotalcomment() {
			return totalcomment;
		}

		public void setTotalcomment(int totalcomment) {
			this.totalcomment = totalcomment;
		}

		public int getFavorites() {
			return favorites;
		}

		public void setFavorites(int favorites) {
			this.favorites = favorites;
		}

	}
}
