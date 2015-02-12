package com.imooly.android.entity;

import com.google.gson.annotations.SerializedName;

/***
 * 充值、续费-获取计费信息
 * 
 * @author LSD
 * 
 */
public class RspVoucherNumber extends ServiceResult {

	@SerializedName("data")
	public Data data;

	public static class Data {

		public int totalvoucher;

		public int getTotalvoucher() {
			return totalvoucher;
		}

		public void setTotalvoucher(int totalvoucher) {
			this.totalvoucher = totalvoucher;
		}

	}

}
