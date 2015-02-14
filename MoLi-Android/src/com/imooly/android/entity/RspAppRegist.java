package com.imooly.android.entity;

import com.google.gson.annotations.SerializedName;

/***
 * APP注册
 * 
 * @author LSD
 *
 */
public class RspAppRegist extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {
		public String appid;
		public String appsecret;
		public String getAppid() {
			return appid;
		}
		public void setAppid(String appid) {
			this.appid = appid;
		}
		public String getAppsecret() {
			return appsecret;
		}
		public void setAppsecret(String appsecret) {
			this.appsecret = appsecret;
		}
	}
}
