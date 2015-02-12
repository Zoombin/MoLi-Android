package com.imooly.android.entity;

import com.google.gson.annotations.SerializedName;

/***
 * 旗舰店商品详情
 * 
 * @author LSD
 * 
 */
public class RspStoreproFile extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {
		public String businessid;
		public String businessname;
		public String businessicon;
		public String businessbanner;
		public int isfavorite;

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

		public String getBusinessicon() {
			return businessicon;
		}

		public void setBusinessicon(String businessicon) {
			this.businessicon = businessicon;
		}

		public String getBusinessbanner() {
			return businessbanner;
		}

		public void setBusinessbanner(String businessbanner) {
			this.businessbanner = businessbanner;
		}

		public int getIsfavorite() {
			return isfavorite;
		}

		public void setIsfavorite(int isfavorite) {
			this.isfavorite = isfavorite;
		}

	}
}
