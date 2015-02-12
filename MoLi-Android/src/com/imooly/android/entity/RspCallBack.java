package com.imooly.android.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/***
 * 电子会员卡
 * 
 * @author
 * 
 */
public class RspCallBack extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {

		private String callback;

		public String getCallback() {
			return callback;
		}

		public void setCallback(String callback) {
			this.callback = callback;
		}

	}
}
