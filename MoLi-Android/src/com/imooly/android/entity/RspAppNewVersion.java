package com.imooly.android.entity;

import com.google.gson.annotations.SerializedName;
/***
 * APP新版
 * 
 * @author LSD
 *
 */
public class RspAppNewVersion extends ServiceResult{
	@SerializedName("data")
	public Data data;

	public static class Data {
		public String hasnewversion;
		public String latestversion;
		public String forceupdate;
		public String downloadaddress;
		public String description;
		public String getHasnewversion() {
			return hasnewversion;
		}
		public void setHasnewversion(String hasnewversion) {
			this.hasnewversion = hasnewversion;
		}
		public String getLatestversion() {
			return latestversion;
		}
		public void setLatestversion(String latestversion) {
			this.latestversion = latestversion;
		}
		public String getForceupdate() {
			return forceupdate;
		}
		public void setForceupdate(String forceupdate) {
			this.forceupdate = forceupdate;
		}
		public String getDownloadaddress() {
			return downloadaddress;
		}
		public void setDownloadaddress(String downloadaddress) {
			this.downloadaddress = downloadaddress;
		}
		public String getDescription() {
			return description;
		}
		public void setDescription(String description) {
			this.description = description;
		}
	}
}
