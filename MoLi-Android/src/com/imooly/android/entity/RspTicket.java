package com.imooly.android.entity;

import com.google.gson.annotations.SerializedName;

/**
 * Ticket
 * 
 * @author LSD
 * 
 */
public class RspTicket extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {
		@SerializedName("ticket")
		public String ticket;

		@SerializedName("sessionid")
		public String sessionid;

		public String getTicket() {
			return ticket;
		}

		public void setTicket(String ticket) {
			this.ticket = ticket;
		}

		public String getSessionid() {
			return sessionid;
		}

		public void setSessionid(String sessionid) {
			this.sessionid = sessionid;
		}

	}
}
