package com.imooly.android.entity;

import com.google.gson.annotations.SerializedName;

/***
 * 收藏数量
 * 
 * @author XYH
 * 
 */
public class RspCollectionNumbers extends ServiceResult {

	@SerializedName("data")
	public Data data;

	public static class Data {

		public int goodNumber;

		public int flagStoreNumber;

		public int storeNumber;

		public int getGoodNumber() {
			return goodNumber;
		}

		public void setGoodNumber(int goodNumber) {
			this.goodNumber = goodNumber;
		}

		public int getFlagStoreNumber() {
			return flagStoreNumber;
		}

		public void setFlagStoreNumber(int flagStoreNumber) {
			this.flagStoreNumber = flagStoreNumber;
		}

		public int getStoreNumber() {
			return storeNumber;
		}

		public void setStoreNumber(int storeNumber) {
			this.storeNumber = storeNumber;
		}

	}

}
