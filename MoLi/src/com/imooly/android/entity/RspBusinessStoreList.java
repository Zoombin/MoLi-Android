package com.imooly.android.entity;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import com.imooly.android.entity.RspBusinessSearch.BusinessEty;

/***
 * 实体店 - 商家列表(热推/喜欢/附近)
 * 
 * @author LSD
 * 
 */
public class RspBusinessStoreList extends ServiceResult implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@SerializedName("data")
	public Data data;

	public static class Data implements Serializable {
		
		private static final long serialVersionUID = 1L;
		
		@SerializedName("storelist")
		public List<BusinessEty> storelist;

		public List<BusinessEty> getStorelist() {
			return storelist;
		}

		public void setStorelist(List<BusinessEty> storelist) {
			this.storelist = storelist;
		}
	}
}
