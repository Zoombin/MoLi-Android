package com.imooly.android.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/***
 * 充值、续费-获取计费信息
 * 
 * @author LSD
 * 
 */
public class RspVoucher extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {
		public int totalvoucher;
		public List<String> useterm;
		public List<FlowEty> flowlist;

		public int getTotalvoucher() {
			return totalvoucher;
		}

		public void setTotalvoucher(int totalvoucher) {
			this.totalvoucher = totalvoucher;
		}

		public List<String> getUseterm() {
			return useterm;
		}

		public void setUseterm(List<String> useterm) {
			this.useterm = useterm;
		}

		public List<FlowEty> getFlowlist() {
			return flowlist;
		}

		public void setFlowlist(List<FlowEty> flowlist) {
			this.flowlist = flowlist;
		}

	}

	public class FlowEty {
		public int amount;
		public String action;
		public String logtime;

		public int getAmount() {
			return amount;
		}

		public void setAmount(int amount) {
			this.amount = amount;
		}

		public String getAction() {
			return action;
		}

		public void setAction(String action) {
			this.action = action;
		}

		public String getLogtime() {
			return logtime;
		}

		public void setLogtime(String logtime) {
			this.logtime = logtime;
		}

	}
}
