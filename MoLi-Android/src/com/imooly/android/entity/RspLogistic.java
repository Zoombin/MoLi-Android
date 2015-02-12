package com.imooly.android.entity;

import com.google.gson.annotations.SerializedName;

/***
 * 订单 物流
 * 
 * @author LSD
 * 
 */
public class RspLogistic extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {
		public String logo;
		public String name;
		public String no;
		public String link;

		public String getLogo() {
			return logo;
		}

		public void setLogo(String logo) {
			this.logo = logo;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getNo() {
			return no;
		}

		public void setNo(String no) {
			this.no = no;
		}

		public String getLink() {
			return link;
		}

		public void setLink(String link) {
			this.link = link;
		}

	}
}
