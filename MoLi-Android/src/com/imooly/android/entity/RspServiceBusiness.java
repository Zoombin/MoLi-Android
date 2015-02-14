package com.imooly.android.entity;

import com.google.gson.annotations.SerializedName;

/***
 * 售货服务-获取商家收货地址
 * 
 * 
 * 
 * @author LSD
 * 
 */
public class RspServiceBusiness extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {
		String name;
		String tel;
		String address;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getTel() {
			return tel;
		}

		public void setTel(String tel) {
			this.tel = tel;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

	}

}
