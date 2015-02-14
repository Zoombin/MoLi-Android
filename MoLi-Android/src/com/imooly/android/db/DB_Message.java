package com.imooly.android.db;

import java.util.List;

import android.content.Context;

import com.imooly.android.entity.RspNewmsg.Msg;

/***
 * 我的消息存储
 * 
 * @author lsd
 * 
 */
public class DB_Message extends DB_Base {

	public DB_Message(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		setName("DB_Message");
		getSettings();
	}

	public void setLastPullTime() {
		long curTime = System.currentTimeMillis() / 1000;
		String pullTime = Long.toString(curTime);
		setSaveString("lastPullTime", pullTime);
	}

	public String getLastPullTime() {
		return getSaveString("lastPullTime", "0");
	}

	public void setOldMessage(List<Msg> oldMsgs) {
		setSaveMode("oldMsgs", oldMsgs);
	}

	public List<Msg> getOldMessage() {
		return (List<Msg>) getSaveMode("oldMsgs", null);
	}

}
