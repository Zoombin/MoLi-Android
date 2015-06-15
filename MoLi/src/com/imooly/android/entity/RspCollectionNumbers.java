package com.imooly.android.entity;

import com.google.gson.annotations.SerializedName;

/***
 * 收藏数量
 * 
 * @author
 * 
 */
public class RspCollectionNumbers extends ServiceResult {

	@SerializedName("data")
	public Data data;

	public static class Data {

		public int goods;

		public int store;

		public int business;

		public int getGoods() {
			return goods;
		}

		public void setGoods(int goods) {
			this.goods = goods;
		}

		public int getStore() {
			return store;
		}

		public void setStore(int store) {
			this.store = store;
		}

		public int getBusiness() {
			return business;
		}

		public void setBusiness(int business) {
			this.business = business;
		}


	}

}
