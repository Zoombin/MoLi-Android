package com.imooly.android.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/***
 * 获取省数据列表
 * 
 * @author 
 * 
 */
public class RspVoucherTerm extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {
		
		private int flag;
		private List<String> content;
		

		public List<String> getContent() {
			return content;
		}

		public void setContent(List<String> content) {
			this.content = content;
		}

		public int getFlag() {
			return flag;
		}
		
		public void setFlag(int flag) {
			this.flag = flag;
		}
		

	}
	
}
