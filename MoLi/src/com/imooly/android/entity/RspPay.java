package com.imooly.android.entity;

import com.google.gson.annotations.SerializedName;

/***
 * 订单付款
 * 
 * @author LSD
 * 
 */
public class RspPay extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {
		public String payno;
		public float totalprice;
		public float payamount;
		public float payvoucher;
		public String paysubject;
		public String paybody;

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

		public float getPayvoucher() {
			return payvoucher;
		}

		public void setPayvoucher(float payvoucher) {
			this.payvoucher = payvoucher;
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
