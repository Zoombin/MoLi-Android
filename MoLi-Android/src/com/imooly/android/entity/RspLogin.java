package com.imooly.android.entity;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;

/***
 * 登陆
 * 
 * @author LSD
 * 
 */
public class RspLogin extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data implements Serializable {
		public String userid;
		public String phone;
		public String nickname;
		public String email;
		public String bindingbusinessid;
		public String bindingbusiness;
		public String avatar;
		public String userrole;
		public String status;
		public String signtoken;
		public String sessionid;
		public int vipflag;

		public String getUserid() {
			return userid;
		}

		public void setUserid(String userid) {
			this.userid = userid;
		}

		public String getPhone() {
			return phone;
		}

		public void setPhone(String phone) {
			this.phone = phone;
		}

		public String getNickname() {
			return nickname;
		}

		public void setNickname(String nickname) {
			this.nickname = nickname;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getBindingbusinessid() {
			return bindingbusinessid;
		}

		public void setBindingbusinessid(String bindingbusinessid) {
			this.bindingbusinessid = bindingbusinessid;
		}

		public String getBindingbusiness() {
			return bindingbusiness;
		}

		public void setBindingbusiness(String bindingbusiness) {
			this.bindingbusiness = bindingbusiness;
		}

		public String getAvatar() {
			return avatar;
		}

		public void setAvatar(String avatar) {
			this.avatar = avatar;
		}

		public String getUserrole() {
			return userrole;
		}

		public void setUserrole(String userrole) {
			this.userrole = userrole;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getSigntoken() {
			return signtoken;
		}

		public void setSigntoken(String signtoken) {
			this.signtoken = signtoken;
		}

		public String getSessionid() {
			return sessionid;
		}

		public void setSessionid(String sessionid) {
			this.sessionid = sessionid;
		}

		public int getVipflag() {
			return vipflag;
		}

		public void setVipflag(int vipflag) {
			this.vipflag = vipflag;
		}

	}
}
