package com.imooly.android.db;

import android.content.Context;

public class DB_Data extends DB_Base {

	public DB_Data(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		setName("DB_DATA");
		getSettings();
	}

	public void saveTicket(String ticket) {
		setSaveString("ticket", ticket);
	}

	public String getTicket() {
		return getSaveString("ticket", "");
	}

	public void saveSessionid(String sessionid) {
		setSaveString("sessionid", sessionid);
	}

	public String getSessionid() {
		return getSaveString("sessionid", "");
	}

	public void saveRefrashTime(long time) {
		setSaveLong("time", time);
	}

	public long getRefrashTime() {
		return getSaveLong("time", 0);
	}
	
	public void saveSigntokenRefrashTime(long time) {
		setSaveLong("tokentime", time);
	}

	public long getSigntokenRefrashTime() {
		return getSaveLong("tokentime", 0);
	}
}
