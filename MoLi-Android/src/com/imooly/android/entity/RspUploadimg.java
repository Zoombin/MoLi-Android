package com.imooly.android.entity;

import com.google.gson.annotations.SerializedName;

/***
 * APP注册
 * 
 * @author LSD
 *
 */
public class RspUploadimg extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {
		int success;
		String imgpath;
		String imgurl;

		public int getSuccess() {
			return success;
		}

		public void setSuccess(int success) {
			this.success = success;
		}

		public String getImgpath() {
			return imgpath;
		}

		public void setImgpath(String imgpath) {
			this.imgpath = imgpath;
		}

		public String getImgurl() {
			return imgurl;
		}

		public void setImgurl(String imgurl) {
			this.imgurl = imgurl;
		}

	}
}
