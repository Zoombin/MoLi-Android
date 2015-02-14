package com.imooly.android.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/***
 * 获取我收藏的旗舰店
 * 
 * @author
 * 
 */
public class RspCellectionFlagStoreList extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {
		private List<Store> storelist;

		public List<Store> getStorelist() {
			return storelist;
		}

		public void setStorelist(List<Store> storelist) {
			this.storelist = storelist;
		}

	}

	public class Store {

		private String storeid;
		private String storename;
		private String storeimage;

		public String getStoreid() {
			return storeid;
		}

		public void setStoreid(String storeid) {
			this.storeid = storeid;
		}

		public String getStorename() {
			return storename;
		}

		public void setStorename(String storename) {
			this.storename = storename;
		}

		public String getStoreimage() {
			return storeimage;
		}

		public void setStoreimage(String storeimage) {
			this.storeimage = storeimage;
		}

	}
}
