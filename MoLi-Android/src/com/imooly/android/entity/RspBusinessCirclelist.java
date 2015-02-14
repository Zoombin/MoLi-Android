package com.imooly.android.entity;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/***
 * 城市下的区和商圈
 * 
 * @author daiye
 * 
 */
public class RspBusinessCirclelist extends ServiceResult implements Serializable {

	private static final long serialVersionUID = 1L;

	@SerializedName("data")
	public Data data;

	public static class Data implements Serializable {

		private static final long serialVersionUID = 1L;

		List<String> near;
		List<Circle> circlelist;

		public List<String> getNear() {
			return near;
		}

		public void setNear(List<String> near) {
			this.near = near;
		}

		public List<Circle> getCirclelist() {
			return circlelist;
		}

		public void setCirclelist(List<Circle> circlelist) {
			this.circlelist = circlelist;
		}

	}

	public static class Circle implements Serializable {
		private static final long serialVersionUID = 1L;
		private String cid;
		private String circlename;

		private List<Circle> sub;// 自己定义的方便处理

		public String getCid() {
			return cid;
		}

		public void setCid(String cid) {
			this.cid = cid;
		}

		public String getCirclename() {
			return circlename;
		}

		public void setCirclename(String circlename) {
			this.circlename = circlename;
		}

		public List<Circle> getSub() {
			return sub;
		}

		public void setSub(List<Circle> sub) {
			this.sub = sub;
		}

	}
}
