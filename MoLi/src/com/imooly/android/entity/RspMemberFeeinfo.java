package com.imooly.android.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/***
 * 充值、续费-获取计费信息
 * 
 * @author LSD
 * 
 */
public class RspMemberFeeinfo extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {
		String termlink;

		List<FeeinfoEty> feelist;

		public List<FeeinfoEty> getFeelist() {
			return feelist;
		}

		public void setFeelist(List<FeeinfoEty> feelist) {
			this.feelist = feelist;
		}

		public String getTermlink() {
			return termlink;
		}

		public void setTermlink(String termlink) {
			this.termlink = termlink;
		}

	}

	public class FeeinfoEty {

		public float fee;
		public String type;
		public int status;
		public String name;

		public float getFee() {
			return fee;
		}

		public void setFee(float fee) {
			this.fee = fee;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public int getStatus() {
			return status;
		}

		public void setStatus(int status) {
			this.status = status;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}
}
