package com.imooly.android.entity;

import java.util.List;

import android.R.string;

import com.google.gson.annotations.SerializedName;

/***
 * 地址列表
 * 
 * @author LSD
 * 
 */
public class RspVoucherFlowList extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {

		private List<Flow> voucherflow;

		public List<Flow> getVoucherflow() {
			return voucherflow;
		}

		public void setVoucherflow(List<Flow> voucherflow) {
			this.voucherflow = voucherflow;
		}

	}

	public class Flow {
		private String action;
		private String amount;
		private String logtime;

		public String getAction() {
			return action;
		}

		public void setAction(String action) {
			this.action = action;
		}

		public String getAmount() {
			return amount;
		}

		public void setAmount(String amount) {
			this.amount = amount;
		}

		public String getLogtime() {
			return logtime;
		}

		public void setLogtime(String logtime) {
			this.logtime = logtime;
		}
	}

}
