package com.imooly.android.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/***
 * 实体店 - 分类搜索项
 * 
 * @author LSD
 * 
 */
public class RspCatelement extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {

		List<Catelement> element;

		public List<Catelement> getElement() {
			return element;
		}

		public void setElement(List<Catelement> element) {
			this.element = element;
		}

	}

	public static class Catelement {
		private String id;
		private String name;
		private String icon;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getIcon() {
			return icon;
		}

		public void setIcon(String icon) {
			this.icon = icon;
		}
	}
}
