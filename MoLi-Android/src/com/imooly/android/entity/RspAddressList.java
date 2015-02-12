package com.imooly.android.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/***
 * 地址列表
 * 
 * @author LSD
 * 
 */
public class RspAddressList extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {
		private int flag;
		private List<Address> addresslist;

		public int getFlag() {
			return flag;
		}

		public void setFlag(int flag) {
			this.flag = flag;
		}

		public List<Address> getAddresslist() {
			return addresslist;
		}

		public void setAddresslist(List<Address> addresslist) {
			this.addresslist = addresslist;
		}

	}

	public static class Address {
		private String addressid;
		private int provinceid;
		private int cityid;
		private int areaid;
		private String province;
		private String city;
		private String area;
		private String street;
		private String postcode;
		private String name;
		private String tel;
		private String mobile;
		private int isdefault;

		public String getAddressid() {
			return addressid;
		}

		public void setAddressid(String addressid) {
			this.addressid = addressid;
		}

		public int getProvinceid() {
			return provinceid;
		}

		public void setProvinceid(int provinceid) {
			this.provinceid = provinceid;
		}

		public int getCityid() {
			return cityid;
		}

		public void setCityid(int cityid) {
			this.cityid = cityid;
		}

		public int getAreaid() {
			return areaid;
		}

		public void setAreaid(int areaid) {
			this.areaid = areaid;
		}

		public String getProvince() {
			return province;
		}

		public void setProvince(String province) {
			this.province = province;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getArea() {
			return area;
		}

		public void setArea(String area) {
			this.area = area;
		}

		public String getStreet() {
			return street;
		}

		public void setStreet(String street) {
			this.street = street;
		}

		public String getPostcode() {
			return postcode;
		}

		public void setPostcode(String postcode) {
			this.postcode = postcode;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getTel() {
			return tel;
		}

		public void setTel(String tel) {
			this.tel = tel;
		}

		public String getMobile() {
			return mobile;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}

		public int getIsdefault() {
			return isdefault;
		}

		public void setIsdefault(int isdefault) {
			this.isdefault = isdefault;
		}

	}
}
