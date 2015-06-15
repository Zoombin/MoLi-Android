package com.imooly.android.entity;

import java.util.Map;

import com.google.gson.Gson;

/***
 * 请求参数
 * 
 * @author lsd
 * 
 */
public abstract class ServiceRequest {
	public String appid;
	public String timestamp;
	public String sessionid;
	public String signature;

	public String genReqStr() {
		Gson gson = new Gson();
		String jstr = gson.toJson(this);
		return jstr;
	}

	public void addPublic(Map<String, String> params) {
		appid = params.get("appid");
		timestamp = params.get("timestamp");
		sessionid = params.get("sessionid");
		signature = params.get("signature");
	}
}
