package com.imooly.android.entity;

import com.google.gson.annotations.SerializedName;
/***
 * 人群角色列表
 * 
 * @author LSD
 *
 */
public class RspUserInfo extends ServiceResult{
	@SerializedName("data")
	public Data data;

	public static class Data {
		public String icon;
		public String nickname;
		public String voucher;
		public String usergroup;
		public String name;
		public String sex;
		public String telephone;
		
		public String getIcon() {
			return icon;
		}
		public void setIcon(String icon) {
			this.icon = icon;
		}
		public String getNickname() {
			return nickname;
		}
		public void setNickname(String nickname) {
			this.nickname = nickname;
		}
		public String getVoucher() {
			return voucher;
		}
		public void setVoucher(String voucher) {
			this.voucher = voucher;
		}
		public String getUsergroup() {
			return usergroup;
		}
		public void setUsergroup(String usergroup) {
			this.usergroup = usergroup;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getSex() {
			return sex;
		}
		public void setSex(String sex) {
			this.sex = sex;
		}
		public String getTelephone() {
			return telephone;
		}
		public void setTelephone(String telephone) {
			this.telephone = telephone;
		}

	}
}
