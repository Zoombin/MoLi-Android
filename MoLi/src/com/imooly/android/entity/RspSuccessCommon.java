package com.imooly.android.entity;

import com.google.gson.annotations.SerializedName;

/***
 * 通用的 请求成功返回
 * 
 * 
 * @author lsd
 * 
 */
public class RspSuccessCommon extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {
		public int success;
		public String message;

		public int getSuccess() {
			return success;
		}

		public void setSuccess(int success) {
			this.success = success;
		}

		public String getMessage() {
			return message;
		}

		public void setMessage(String message) {
			this.message = message;
		}

	}
}
