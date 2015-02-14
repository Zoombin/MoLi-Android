package com.imooly.android.entity;

import com.google.gson.annotations.SerializedName;

/***
 * APP封面
 * 
 * @author LSD
 * 
 */
public class RspAppCover extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {
		int flag;
		String imagepath;

		public int getFlag() {
			return flag;
		}

		public void setFlag(int flag) {
			this.flag = flag;
		}

		public String getImagepath() {
			return imagepath;
		}

		public void setImagepath(String imagepath) {
			this.imagepath = imagepath;
		}

	}
}
