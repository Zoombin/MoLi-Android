package com.imooly.android.tool;

import java.util.ArrayList;
import java.util.List;

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
	boolean isLoadingTicket = false;
	List<ITicket> callBackList;

	public interface ITicket {
		void success();

		void failed(String msg);
	}

	private static TicketUtil tUtil;
	private static DB_Data data;

	private TicketUtil() {
		// TODO Auto-generated constructor stubs
	}

	synchronized public static TicketUtil instance() {
		if (tUtil == null) {
			tUtil = new TicketUtil();
			data = new DB_Data(MoLiApplication.getContext());
		}
		return tUtil;
	}

	public void setSessionid(String sessionid) {
		data.saveSessionid(sessionid);
	}

	public String getTicket() {
		// 每次API请求，会延长登录失效时间
		long time = System.currentTimeMillis();
		data.saveLoginTime(time);

		return data.getTicket();
	}

	public String getSessionid() {
		return data.getSessionid();
	}

	public void firstLoad(final ITicket callBack) {
		getTicketFromNet(false, callBack);
	}

	public boolean refrashTicket(final ITicket callBack) {
		getTicketFromNet(true, callBack);
		return true;
	}

	public boolean isTicketValid(final ITicket callBack) {
		long curTime = System.currentTimeMillis();
		if ((curTime - data.getRefrashTime()) < Config.ClientTimeOut * 60 * 1000) {
			return true;
		}

		if (isLoadingTicket) {
			// 一个界面会调用多个接口，只获取一次统一返回ticke
			pushCallBack(callBack);
		} else {
			isLoadingTicket = true;
			getTicketFromNet(true, callBack);
		}
		return false;
	}

	private void pushCallBack(ITicket callBack) {
		if (callBackList == null) {
			callBackList = new ArrayList<ITicket>();
		}
		callBackList.add(callBack);
	}

	private void popCallBack(boolean success) {
		if (callBackList != null) {
			for (ITicket callback : callBackList) {
				if (success) {
					callback.success();
				} else {
					callback.failed("");
				}
			}
			callBackList.clear();
		}
	}

	// 是否能自动登录
	@Deprecated
	public boolean canLogin() {
		Context context = MoLiApplication.getContext();
		DB_User db_User = new DB_User(context);
		String signtoken = db_User.getSigntoken();

		if (!TextUtils.isEmpty(signtoken)) {
			return true;
		}
		return false;
	}

	@Deprecated
	public boolean needAutoLogin() {
		if (canLogin()) {
			long curTime = System.currentTimeMillis();
			if ((curTime - data.getLoginTime()) >= Config.ClientTimeOut * 60 * 1000) {
				return true;
			} else {
				// 不需要自动登录（此时还保留着登录状态）
				MoLiApplication.getInstance().setLogin(true);
			}
		}
		return false;
	}

	private void getTicketFromNet(final boolean needLogin, final ITicket callBack) {
		Api.getTicket(MoLiApplication.getContext(), new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				// TODO Auto-generated method stub
				RspTicket entity = (RspTicket) rspData;
				if (entity.data != null) {
					String ticket = entity.data.getTicket();
					long time = System.currentTimeMillis();
					data.saveTicket(ticket);
					data.saveRefrashTime(time);

					if (needLogin) {
						login(callBack);
					} else {
						if (callBack != null) {
							callBack.success();
						}
						isLoadingTicket = false;
						popCallBack(false);
					}
				}
			}

			@Override
			public void failed(String msg) {
				// TODO Auto-generated method stub
				if (callBack != null) {
					callBack.failed(msg);
				}

				isLoadingTicket = false;
				popCallBack(false);

			}
		}, RspTicket.class);
	}

	/***
	 * 持久化登录
	 * 
	 * 这里和手动登录不一样（这里是用phonsigntoken登录的）
	 */
	public void login(final ITicket callBack) {
		Context context = MoLiApplication.getContext();
		final DB_User db_User = new DB_User(context);
		String signtoken = db_User.getSigntoken();
		String userName = db_User.getUserName();

		String ip = Utils.getLocalIpAddress();
		DB_Location db_location = new DB_Location(context);
		String lng = db_location.getLontitude();
		String lat = db_location.getLatitude();

		Api.login(context, userName, "", signtoken, ip, lng, lat, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				// TODO Auto-generated method stub
				RspLogin rsp = (RspLogin) rspData;
				if (rsp.data != null) {
					isLoadingTicket = false;
					// 设置全局变量isLogin
					MoLiApplication.getInstance().setLogin(true);
					data.saveSessionid(rsp.data.getSessionid());
					long time = System.currentTimeMillis();
					data.saveLoginTime(time);

					db_User.setSigntoken(rsp.data.getSigntoken());
					if (callBack != null) {
						callBack.success();
					}
					isLoadingTicket = false;
					popCallBack(true);
				}
			}

			@Override
			public void failed(String msg) {
				if (callBack != null) {
					callBack.failed("");
				}
				isLoadingTicket = false;
				popCallBack(false);
			}
		}, RspLogin.class);

	}

}
