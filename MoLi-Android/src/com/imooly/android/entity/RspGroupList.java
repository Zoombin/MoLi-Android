package com.imooly.android.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;
/***
 * 用户组
 * 
 * @author LSD
 *
 */
public class RspGroupList extends ServiceResult{
	@SerializedName("data")
	public Data data;

	public static class Data {
		public List<String> usergrouplist;

		public List<String> getUsergrouplist() {
			return usergrouplist;
		}

		public void setUsergrouplist(List<String> usergrouplist) {
			this.usergrouplist = usergrouplist;
		}
	}
}
