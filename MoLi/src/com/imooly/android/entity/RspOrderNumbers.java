package com.imooly.android.entity;

import com.google.gson.annotations.SerializedName;

/***
 * 
 * @author 订单信息概况-不同状态的订单数目等
 * 
 */
public class RspOrderNumbers extends ServiceResult {

	@SerializedName("data")
	public Data data;

	public static class Data {
		
		private int forpay;

		private int forsend;

		private int fortake;

		private int forcomment;
		

		public int getForpay() {
			return forpay;
		}

		public void setForpay(int forpay) {
			this.forpay = forpay;
		}

		public int getForsend() {
			return forsend;
		}

		public void setForsend(int forsend) {
			this.forsend = forsend;
		}

		public int getFortake() {
			return fortake;
		}

		public void setFortake(int fortake) {
			this.fortake = fortake;
		}

		public int getForcomment() {
			return forcomment;
		}

		public void setForcomment(int forcomment) {
			this.forcomment = forcomment;
		}


	
	}

}
