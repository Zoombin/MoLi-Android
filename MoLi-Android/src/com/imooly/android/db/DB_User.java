package com.imooly.android.db;

import android.content.Context;

import com.imooly.android.entity.RspLogin;

public class DB_User extends DB_Base {

	public DB_User(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		setName("DB_USER");
		getSettings();
	}

	public void setUserName(String userName) {
		setSaveString("userName", userName);
	}

	public String getUserName() {
		return getSaveString("userName", "");
	}

	public void setPassWord(String passWord) {
		setSaveString("passWord", passWord);
	}

	public String getPassWord() {
		return getSaveString("passWord", "");
	}

	public void setUserid(String userid) {
		setSaveString("userid", userid);
	}

	public String getUserid() {
		return getSaveString("userid", "");
	}
	public void setSigntoken(String Signtoken) {
		setSaveString("Signtoken", Signtoken);
	}

	public String getSigntoken() {
		return getSaveString("Signtoken", "");
	}

	public void setLoginData(RspLogin.Data data) {
		setSaveMode("loginData", data);
	}

	public RspLogin.Data getLoginData() {
		return (RspLogin.Data) getSaveMode("loginData", null);
	}
	
	public void setVip(boolean vip){
		setSaveBoolean("vip", vip);
	}
	
	public boolean isVip(){
		return getSaveBoolean("vip", false);
	}
	

}
