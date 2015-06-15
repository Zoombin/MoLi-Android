



package com.imooly.android.entity;

import com.google.gson.annotations.SerializedName;

/***
 * 收藏数量
 * 
 * @author XYH
 * 
 */
public class  RspWXPrePay extends ServiceResult {

	@SerializedName("data")
	public Data data;

	public class Data {

		public String appid;

		public String noncestr;

		@SerializedName("package")
		public String strpackage;
		
		public String partnerid;
		
		public String prepayid;
		
		public String sign;
		
		public String timestamp;
		
		public String appkey;
		
		public String getAppkey() {
			return appkey;
		}

		public void setAppkey(String appkey) {
			this.appkey = appkey;
		}

		public String getAppid() {
			return appid;
		}

		public void setAppid(String appid) {
			this.appid = appid;
		}

		public String getNoncestr() {
			return noncestr;
		}

		public void setNoncestr(String noncestr) {
			this.noncestr = noncestr;
		}

		public String getStrpackage() {
			return strpackage;
		}

		public void setStrpackage(String strpackage) {
			this.strpackage = strpackage;
		}

		public String getPartnerid() {
			return partnerid;
		}

		public void setPartnerid(String partnerid) {
			this.partnerid = partnerid;
		}

		public String getPrepayid() {
			return prepayid;
		}

		public void setPrepayid(String prepayid) {
			this.prepayid = prepayid;
		}

		public String getSign() {
			return sign;
		}

		public void setSign(String sign) {
			this.sign = sign;
		}

		public String getTimestamp() {
			return timestamp;
		}

		public void setTimestamp(String timestamp) {
			this.timestamp = timestamp;
		}
	}

}

