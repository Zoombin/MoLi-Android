package com.imooly.android.entity;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/***
 * 消息详情
 * 
 * @author LSD
 * 
 */
public class RspNewmsg extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {
		private int flag;
		private int num;

		private List<Msg> msglist;

		public int getFlag() {
			return flag;
		}

		public void setFlag(int flag) {
			this.flag = flag;
		}

		public int getNum() {
			return num;
		}

		public void setNum(int num) {
			this.num = num;
		}

		public List<Msg> getMsglist() {
			return msglist;
		}

		public void setMsglist(List<Msg> msglist) {
			this.msglist = msglist;
		}

	}

	public static class Msg implements Serializable{
		private String messageid;
		private String type;
		private String title;
		private String link;
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

		public String getLink() {
			return link;
		}

		public void setLink(String link) {
			this.link = link;
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
