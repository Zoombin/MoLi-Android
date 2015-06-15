package com.imooly.android.entity;

import com.google.gson.annotations.SerializedName;

/***
 * 充值、续费-获取计费信息
 * 
 * @author LSD
 * 
 */
public class RspMyMessageNumber extends ServiceResult {
	
	@SerializedName("data")
	
	public Data data;

	public static class Data {
		
		public int num;

		public int getNum() {
			return num;
		}

		public void setNum(int num) {
			this.num = num;
		}
	}

}
