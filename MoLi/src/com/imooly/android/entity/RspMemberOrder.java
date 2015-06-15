package com.imooly.android.entity;

import com.google.gson.annotations.SerializedName;

/***
 * 会员充值、续费-获取付款信息
 * 
 * @author
 * 
 */
public class RspMemberOrder extends ServiceResult {

	@SerializedName("data")
	public Data data;

	public static class Data {

		private String orderno;
		private String payno;
		private float totalprice;
		private float payamount;
		private String paysubject;
		private String paybody;

		public String getOrderno() {
			return orderno;
		}

		public void setOrderno(String orderno) {
			this.orderno = orderno;
		}

		public String getPayno() {
			return payno;
		}

		public void setPayno(String payno) {
			this.payno = payno;
		}

		public float getTotalprice() {
			return totalprice;
		}

		public void setTotalprice(float totalprice) {
			this.totalprice = totalprice;
		}

		public float getPayamount() {
			return payamount;
		}

		public void setPayamount(float payamount) {
			this.payamount = payamount;
		}

		public String getPaysubject() {
			return paysubject;
		}

		public void setPaysubject(String paysubject) {
			this.paysubject = paysubject;
		}

		public String getPaybody() {
			return paybody;
		}

		public void setPaybody(String paybody) {
			this.paybody = paybody;
		}

	}

}
