package com.imooly.android.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/***
 * 消息详情
 * 
 * @author LSD
 * 
 */
public class RspMsginfo extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {
		private String messageid;
		private String type;
		private String title;
		private String content;
		private List<String> images;
		private String senddate;
		private int isread;

		public String getMessageid() {
			return messageid;
		}

		public void setMessageid(String messageid) {
			this.messageid = messageid;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getTitle() {
			return title;
		}

		public void setTitle(String title) {
			this.title = title;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public List<String> getImages() {
			return images;
		}

		public void setImages(List<String> images) {
			this.images = images;
		}

		public String getSenddate() {
			return senddate;
		}

		public void setSenddate(String senddate) {
			this.senddate = senddate;
		}

		public int getIsread() {
			return isread;
		}

		public void setIsread(int isread) {
			this.isread = isread;
		}

	}
}
