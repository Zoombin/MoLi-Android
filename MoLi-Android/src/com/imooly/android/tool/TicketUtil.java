package com.imooly.android.tool;

import android.content.Context;
import android.text.TextUtils;

import com.imooly.android.MoLiApplication;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.db.DB_Data;
import com.imooly.android.db.DB_Location;
import com.imooly.android.db.DB_User;
import com.imooly.android.entity.RspLogin;
import com.imooly.android.entity.RspTicket;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.utils.Utils;

public class TicketUtil {
	private static TicketUtil tUtil;
	private static DB_Data data;

	private TicketUtil() {
		// TODO Auto-generated constructor stub
	}

	synchronized public static TicketUtil instance() {
		if (tUtil == null) {
			tUtil = new TicketUtil();
			data = new DB_Data(MoLiApplication.getContext());
		}
		return tUtil;
	}
	
	public void setSessionid(String sessionid){
		data.saveSessionid(sessionid);
	}

	public String getTicket() {
		if (needRefrash()) {
			refrashTicket();
		}
		if (isValid()) {
			return data.getTicket();
		}
		return "";
	}

	public String getSessionid() {
		if (needRefrash()) {
			refrashTicket();
		}
		if (isValid()) {
			return data.getSessionid();
		}
		return "";
	}

	public boolean isValid() {
		long curTime = System.currentTimeMillis();
		if ((curTime - data.getRefrashTime()) < 20 * 60 * 1000) {
			return true;
		}
		return false;
	}
	
	private boolean needRefrash() {
		long curTime = System.currentTimeMillis();
		if ((curTime - data.getRefrashTime()) > 18 * 60 * 1000) {
			return true;
		}
		return false;
	}

	public void refrashTicket() {
		//获取Ticket
		Api.getTicket(MoLiApplication.getContext(), new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				// TODO Auto-generated method stub
				RspTicket entity = (RspTicket) rspData;
				if (entity.data != null) {
					String ticket = entity.data.getTicket();
					String sessionid = entity.data.getSessionid();
					long time = System.currentTimeMillis();
					data.saveTicket(ticket);
					data.saveSessionid(sessionid);
					data.saveRefrashTime(time);
					
					//同时登陆一次
					login();
				}
			}

			@Override
			public void failed(String msg) {
				// TODO Auto-generated method stub
			}
		}, RspTicket.class);
		
	}
	
	/***
	 * 持久化登录
	 * 
	 * 这里和手动登录不一样（这里是用signtoken登录的）
	 */
	private void login() {
		final Context context = MoLiApplication.getContext();
		final DB_User db_User = new DB_User(context);
		String signtoken = db_User.getSigntoken();
		if (!TextUtils.isEmpty(signtoken)) {
			String userName = db_User.getUserName();
			
			String ip = Utils.getLocalIpAddress();
			DB_Location db_location = new DB_Location(context);
			String lng = db_location.getLontitude();
			String lat = db_location.getLatitude();
			
			Api.login(context, userName, "", signtoken , ip, lng, lat, new NetCallBack<ServiceResult>() {
				@Override
				public void success(ServiceResult rspData) {
					// TODO Auto-generated method stub
					RspLogin rsp = (RspLogin) rspData;
					if (rsp.data != null) {
						// 设置全局变量isLogin
						MoLiApplication.getInstance().setLogin(true);
						long time = System.currentTimeMillis();
						data.saveSigntokenRefrashTime(time);
						data.saveSessionid(rsp.data.getSessionid());
						
						db_User.setSigntoken(rsp.data.getSigntoken());
					}
				}
				
				@Override
				public void failed(String msg) {
					
				}
			}, RspLogin.class);
		}
	}

}
