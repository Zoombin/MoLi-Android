package com.imooly.android.entity;

import com.google.gson.annotations.SerializedName;

/***
 * 充值、续费-获取计费信息
 * 
 * @author 订单信息概况-不同状态的订单数目等
 * 
 */
public class RspOrderNumbers extends ServiceResult {

	@SerializedName("data")
	public Data data;

	public static class Data {

		private OrderNumber forpay;

		private OrderNumber forsend;

		private OrderNumber fortake;

		private OrderNumber forcomment;

		public OrderNumber getForpay() {
			return forpay;
		}

		public void setForpay(OrderNumber forpay) {
			this.forpay = forpay;
		}

		public OrderNumber getForsend() {
			return forsend;
		}

		public void setForsend(OrderNumber forsend) {
			this.forsend = forsend;
		}

		public OrderNumber getFortake() {
			return fortake;
		}

		public void setFortake(OrderNumber fortake) {
			this.fortake = fortake;
		}

		public OrderNumber getForcomment() {
			return forcomment;
		}

		public void setForcomment(OrderNumber forcomment) {
			this.forcomment = forcomment;
		}
	}

	public class OrderNumber {
		private int hasnew;
		private int num;

		public int getHasnew() {
			return hasnew;
		}

		public void setHasnew(int hasnew) {
			this.hasnew = hasnew;
		}

		public int getNum() {
			return num;
		}

		public void setNum(int num) {
			this.num = num;
		}

	}

}
