package com.imooly.android.entity;

import com.google.gson.annotations.SerializedName;

/***
 * 人群角色列表
 * 
 * @author LSD
 * 
 */
public class RspUserInfo extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {
		public String userid;
		public String avatar;
		public String nickname;
		public String totalvolume;
		public String usergroup;
		public String truename;
		public String gender;
		public String phone;

	}
}
