package com.imooly.android.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/***
 * 确认订单信息-立即购买/购物车选择商品结算
 * 
 * 
 * @author LSD
 * 
 */
public class RspOrderSave extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {
		public int success;
		public int vipmember;
		public List<String> ordernos;
		public String payno;
		public float totalprice;
		public float payamount;
		public int payvoucher;
		public String paysubject;
		public String paybody;

		public int getSuccess() {
			return success;
		}

		public void setSuccess(int success) {
			this.success = success;
		}

		public int getVipmember() {
			return vipmember;
		}

		public void setVipmember(int vipmember) {
			this.vipmember = vipmember;
		}

		public List<String> getOrdernos() {
			return ordernos;
		}

		public void setOrdernos(List<String> ordernos) {
			this.ordernos = ordernos;
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

		public void setPayamount(int payamount) {
			this.payamount = payamount;
		}

		public int getPayvoucher() {
			return payvoucher;
		}

		public void setPayvoucher(int payvoucher) {
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
