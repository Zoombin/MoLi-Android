package com.imooly.android.entity;

import com.google.gson.annotations.SerializedName;

/***
 * 
 * 请求结果
 * 
 * @author lsd
 *
 */

public abstract class ServiceResult {
	@SerializedName("error")
	public int error;

	@SerializedName("msg")
	public String msg;

	public int getError() {
		return error;
	}

	public void setError(int error) {
		this.error = error;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
