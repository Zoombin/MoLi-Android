package com.imooly.android.entity;

import com.google.gson.annotations.SerializedName;

/***
 * 区域查询邮费
 * 
 * @author LSD
 * 
 */
public class RspGoodsPostage extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {
		public String postage;

		public String getPostage() {
			return postage;
		}

		public void setPostage(String postage) {
			this.postage = postage;
		}

	}
}
