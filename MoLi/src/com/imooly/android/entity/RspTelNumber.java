package com.imooly.android.entity;

import com.google.gson.annotations.SerializedName;

/***
 * 客服电话
 * 
 * @author
 * 
 */
public class RspTelNumber extends ServiceResult {
	
	@SerializedName("data")
	
	public Data data;

	public static class Data {
		
		public String telephone;

		public String getTelephone() {
			return telephone;
		}

		public void setTelephone(String telephone) {
			this.telephone = telephone;
		}
		

		
	}

}
