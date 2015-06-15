package com.imooly.android.entity;

import com.google.gson.annotations.SerializedName;

/***
 * 向服务器发送 device_token
 * 
 * @author LSD
 *
 */
public class RspRecordDeviceToken extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {
	}
}
