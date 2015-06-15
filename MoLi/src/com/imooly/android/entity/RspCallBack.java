package com.imooly.android.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/***
 * 支付的回调地址
 * 
 * @author
 * 
 */
public class RspCallBack extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {

		private String notifyurl;

		public String getNotifyurl() {
			return notifyurl;
		}

		public void setNotifyurl(String notifyurl) {
			this.notifyurl = notifyurl;
		}

	}
}
