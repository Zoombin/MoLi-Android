package com.imooly.android.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/***
 * 热门关键字
 * 
 * @author LSD
 * 
 */
public class RspHotKeyWords extends ServiceResult{
	@SerializedName("data")
	public Data data;

	public static class Data {
		public List<String> keywords;

		public List<String> getKeywords() {
			return keywords;
		}

		public void setKeywords(List<String> keywords) {
			this.keywords = keywords;
		}
	}
}
