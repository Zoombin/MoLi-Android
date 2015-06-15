package com.imooly.android.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/***
 * 获取省数据列表
 * 
 * @author
 * 
 */
public class RspAddressProvince extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {

		private List<ItemProvince> province;

		public List<ItemProvince> getAddressProvince() {
			return province;
		}

		public void setAddressProvince(List<ItemProvince> addressProvince) {
			this.province = addressProvince;
		}

	}

	public class ItemProvince {

		private int pid;
		private String name;

		public int getPid() {
			return pid;
		}

		public void setPid(int pid) {
			this.pid = pid;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

	}
}
