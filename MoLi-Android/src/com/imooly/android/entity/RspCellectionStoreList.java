package com.imooly.android.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/***
 * 获取我收藏的实体店列表
 * 
 * @author
 * 
 */
public class RspCellectionStoreList extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {
		private List<Business> businesslist;

		public List<Business> getBusinesslist() {
			return businesslist;
		}

		public void setBusinesslist(List<Business> businesslist) {
			this.businesslist = businesslist;
		}

	}

	public class Business {

		private String businessid;
		private String businessname;
		private String businessimage;

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

		public String getBusinessimage() {
			return businessimage;
		}

		public void setBusinessimage(String businessimage) {
			this.businessimage = businessimage;
		}

	}
}
