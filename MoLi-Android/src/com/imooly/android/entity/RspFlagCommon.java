package com.imooly.android.entity;

import com.google.gson.annotations.SerializedName;

/***
 * 检查是否已经设置过交易密码
 * 
 * @author
 * 
 */
public class RspFlagCommon extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {
		private int flag;

		public int getFlag() {
			return flag;
		}

		public void setFlag(int flag) {
			this.flag = flag;
		}
	}
}
