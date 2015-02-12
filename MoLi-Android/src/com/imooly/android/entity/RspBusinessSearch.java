package com.imooly.android.entity;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/***
 * 实体店 - 商家搜索
 * 
 * @author LSD
 * 
 */
public class RspBusinessSearch extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {
		int totalpage;

		@SerializedName("businesslist")
		public List<BusinessEty> storelist;

		public int getTotalpage() {
			return totalpage;
		}

		public void setTotalpage(int totalpage) {
			this.totalpage = totalpage;
		}

		public List<BusinessEty> getStorelist() {
			return storelist;
		}

		public void setStorelist(List<BusinessEty> storelist) {
			this.storelist = storelist;
		}

	}

	public static class BusinessEty implements Serializable {
		public String businessid;
		public String businessname;
		public List<String> industryid;
		public String industry;
		public String industryicon;
		public String businessicon;
		public String address;
		public String starlevel;
		public String distance;
		public List<String> telephone;
		public double lng;
		public double lat;

		public String getBusinessid() {
			return businessid;
		}

		public void setBusinessid(String businessid) {
			this.businessid = businessid;
		}

		public String getBusinessname() {
			return businessname;
		}

		public void setBusinessname(String businessname) {
			this.businessname = businessname;
		}

		public List<String> getIndustryid() {
			return industryid;
		}

		public void setIndustryid(List<String> industryid) {
			this.industryid = industryid;
		}

		public String getIndustry() {
			return industry;
		}

		public String getIndustryicon() {
			return industryicon;
		}

		public void setIndustryicon(String industryicon) {
			this.industryicon = industryicon;
		}

		public void setIndustry(String industry) {
			this.industry = industry;
		}

		public String getBusinessicon() {
			return businessicon;
		}

		public void setBusinessicon(String businessicon) {
			this.businessicon = businessicon;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getStarlevel() {
			return starlevel;
		}

		public void setStarlevel(String starlevel) {
			this.starlevel = starlevel;
		}

		public String getDistance() {
			return distance;
		}

		public void setDistance(String distance) {
			this.distance = distance;
		}

		public List<String> getTelephone() {
			return telephone;
		}

		public void setTelephone(List<String> telephone) {
			this.telephone = telephone;
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

	}
}
