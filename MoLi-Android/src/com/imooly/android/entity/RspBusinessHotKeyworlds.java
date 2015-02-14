package com.imooly.android.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/***
 * 实体店 - 商家搜索
 * 
 * @author LSD
 * 
 */
public class RspBusinessHotKeyworlds extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {
		List<String> keywords;

		public List<String> getKeywords() {
			return keywords;
		}

		public void setKeywords(List<String> keywords) {
			this.keywords = keywords;
		}

	}
}
