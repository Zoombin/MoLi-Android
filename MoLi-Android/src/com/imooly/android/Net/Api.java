package com.imooly.android.Net;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;

import com.baidu.platform.comapi.map.s;
import com.imooly.android.MoLiApplication;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.enums.RequestType;
import com.imooly.android.tool.Config;
import com.imooly.android.tool.MD5;
import com.imooly.android.tool.MD5Util;
import com.imooly.android.tool.TicketUtil;
import com.imooly.android.utils.NetWorkUtils;

public class Api {
	/***
	 * App注册
	 * 
	 * 说明：不需要appid，signature，timestamp，sessionid
	 * 
	 * 
	 * @param deviceos
	 *            系统类型 IOS/Android
	 * @param deviceosversion
	 *            系统版本号
	 * @param devicetype
	 *            设备类型 Android
	 * @param deviceid
	 *            设备码/设备id号
	 * @param ip
	 * @param lng
	 * @param lat
	 * @param appversion
	 *            APP版本号
	 * @param context
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void appRegister(String deviceos, String deviceosversion, String devicetype, String deviceid, String ip, String lng,
			String lat, String appversion, Context context, final NetCallBack<ServiceResult> netCallBack, final Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("deviceos", "Android");
		params.put("deviceosversion", deviceosversion);
		params.put("devicetype", "Android");
		params.put("deviceid", deviceid);
		params.put("ip", ip);
		params.put("lng", lng);
		params.put("lat", lat);
		params.put("appversion", appversion);

		NetUtils.post(context, RequestType.App_Register.getConstValue(), params, null, netCallBack, rspCls);
	}
	
	/***
	 * 获取ticket
	 * 
	 * 有效期20分钟
	 * 
	 * @param context
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void getTicket(Context context, final NetCallBack<ServiceResult> netCallBack, final Class<?> rspCls) {
		String appid = Config.getAppID();
		NetUtils.getValue(context, RequestType.App_GetTicket.getConstValue() + "?appid=" + appid, null, netCallBack, rspCls);
	}

	/***
	 * 获取APP最新版本
	 * 
	 * @param context
	 * @param version
	 *            APP版本号
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void getAppnewversion(Context context, String version, final NetCallBack<ServiceResult> netCallBack, final Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("deviceos", "Android,");
		params.put("devicetype", "Android");
		params.put("version", version);
		NetUtils.getValue(context, RequestType.App_GetAppNewVersion.getConstValue() + "?" + getReqPama(params), "正在检测", netCallBack, rspCls);
	}

	/***
	 * 获取APP封面
	 * 
	 * @param context
	 * @param imagepath
	 *            封面图地址校验码 md5(imagepath) 服务器用于比较是否更新封面图
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void getAppCover(Context context, String imagepath, final NetCallBack<ServiceResult> netCallBack, final Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("deviceos", "Android,");
		params.put("devicetype", "Android");
		params.put("scode", MD5Util.MD5(imagepath));
		NetUtils.getValue(context, RequestType.App_GetAppCover.getConstValue() + "?" + getReqPama(params), null, netCallBack, rspCls);
	}

	/**
	 * 分享内容获取
	 * @param context
	 * @param object
	 * @param platform
	 * @param param
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void getShareSInfo(Context context, String object, String platform, String param, final NetCallBack<ServiceResult> netCallBack, final Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("object", object);
		params.put("platform", platform);
		params.put("params", param);
		NetUtils.getValue(context, RequestType.share_SInfo.getConstValue() + "?" + getReqPama(params), null, netCallBack, rspCls);
	}
	
	/***
	 * 获取验证码
	 * 
	 * @param action
	 *            发送短信的动作[regist:注册/forgot:忘记密码]
	 * @param phone
	 * @param context
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void getVerCode(String action, String phone, Context context, final NetCallBack<ServiceResult> netCallBack,
			final Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("action", action);
		params.put("phone", phone);

		NetUtils.post(context, RequestType.User_GetVerCode.getConstValue(), params, null, netCallBack, rspCls);
	}

	/***
	 * 检查验证码是否有效
	 * 
	 * @param context
	 * @param action
	 *            发送短信的动作[regist:注册/forgot:忘记密码]
	 * @param phone
	 * @param vcode
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void checkVerCode(Context context, String action, String phone, String vcode,
			final NetCallBack<ServiceResult> netCallBack, final Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("action", action);
		params.put("phone", phone);
		params.put("vcode", vcode);
		NetUtils.getValue(context, RequestType.User_CheckVerCode.getConstValue() + "?" + getReqPama(params), null, netCallBack, rspCls);
	}

	/***
	 * 用户注册
	 * 
	 * 注册协议地址 APP客户端用webview方式访问此地址
	 * 
	 * @param context
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void registterms(Context context, final NetCallBack<ServiceResult> netCallBack, final Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		NetUtils.getValue(context, RequestType.User_Registterms.getConstValue() + "?" + getReqPama(params), null, netCallBack, rspCls);
	}

	/***
	 * 用户注册 - 提交手机号
	 * 
	 * 0失败 1成功
	 * 
	 * @param context
	 * @param phone
	 * @param vcode
	 *            短信验证码
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void checkPhoneAndVcode(Context context, String phone, String vcode, final NetCallBack<ServiceResult> netCallBack,
			final Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("phone", phone);
		params.put("vcode", vcode);
		NetUtils.post(context, RequestType.User_RegistPhone.getConstValue(), params, null, netCallBack, rspCls);
	}

	/***
	 * 用户注册-设置密码
	 * 
	 * @param context
	 * @param phone
	 * @param vcode
	 *            短信验证码
	 * @param password
	 * @param passwordc
	 * @param ip
	 * @param lng
	 * @param lat
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void registSetPass(Context context, String phone, String vcode, String password, String passwordc, String ip, String lng,
			String lat, final NetCallBack<ServiceResult> netCallBack, final Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("phone", phone);
		params.put("vcode", vcode);
		params.put("password", password);
		params.put("passwordc", passwordc);
		params.put("ip", ip);
		params.put("lng", lng);
		params.put("lat", lat);
		NetUtils.post(context, RequestType.User_RegistSetPass.getConstValue(), params, null, netCallBack, rspCls);
	}

	/***
	 * 用户登录(登陆成功后要存储 signid/signtoken)
	 * 
	 * 
	 * 
	 * @param context
	 * @param phone
	 * @param password
	 *            登录密码(API中完成加密)
	 * @param signtoken
	 *            持久化登录token 该参数与password二选一必传
	 * @param ip
	 * @param lng
	 * @param lat
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void login(Context context, String phone, String password, String signtoken, String ip, String lng, String lat,
			NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("phone", phone);
		if (!"".equals(password)) {
			String securityPass = MD5.encryptMD5(MD5.encryptMD5(password) + TicketUtil.instance().getTicket());
			params.put("password", securityPass);
		}
		params.put("signtoken", signtoken);
		params.put("ip", ip);
		params.put("lng", lng);
		params.put("lat", lat);
		NetUtils.post(context, RequestType.User_Login.getConstValue(), params, null, netCallBack, rspCls);
	}

	/***
	 * 忘记密码-验证手机
	 * 
	 * @param context
	 * @param phone
	 * @param vcode
	 *            短信验证码
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void forgotPassCkPhone(Context context, String phone, String vcode, NetCallBack<ServiceResult> netCallBack,
			Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("phone", phone);
		params.put("vcode", vcode);
		NetUtils.post(context, RequestType.User_ForgotPassCkPhone.getConstValue(), params, null, netCallBack, rspCls);
	}

	/***
	 * 忘记密码-设置新密码
	 * 
	 * @param context
	 * @param phone
	 * @param vcode
	 *            短信验证码
	 * @param password
	 * @param passwordc
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void forgotPassSetNew(Context context, String phone, String vcode, String password, String passwordc,
			NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("phone", phone);
		params.put("vcode", vcode);
		params.put("password", password);
		params.put("passwordc", passwordc);
		NetUtils.post(context, RequestType.User_ForgotPassSetNew.getConstValue(), params, null, netCallBack, rspCls);
	}

	/***
	 * 修改密码
	 * 
	 * @param context
	 * @param phone
	 * @param vcode
	 *            短信验证码
	 * @param password
	 * @param passwordc
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void changePw(Context context, String phone, String vcode, String password, String passwordc,
			NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("phone", phone);
		params.put("vcode", vcode);
		params.put("password", password);
		params.put("passwordc", passwordc);
		NetUtils.post(context, RequestType.User_ChangePass.getConstValue(), params, null, netCallBack, rspCls);
	}

	/***
	 * 设置或修改交易密码
	 * 
	 * @param context
	 * @param action
	 *            设置/修改
	 * @param password
	 * @param passwordc
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void traderpw(Context context, String action, String password, String passwordc, NetCallBack<ServiceResult> netCallBack,
			Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("action", action);
		params.put("password", password);
		params.put("passwordc", passwordc);
		NetUtils.post(context, RequestType.User_TraderPass.getConstValue(), params, null, netCallBack, rspCls);
	}

	/***
	 * 获取用户组
	 * 
	 * @param context
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void userGroupList(Context context, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		NetUtils.getValue(context, RequestType.User_UserGroupList.getConstValue() + "?" + getReqPama(params), null, netCallBack, rspCls);
	}

	/***
	 * 设置用户组
	 * 
	 * @param context
	 * @param userGroup
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void setUserGroup(Context context, String userGroup, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("usergroup", userGroup);
		NetUtils.post(context, RequestType.User_SetUserGroup.getConstValue(), params, null, netCallBack, rspCls);
	}

	/***
	 * 获取用户信息
	 * 
	 * @param context
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void userInfo(Context context, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		NetUtils.getValue(context, RequestType.User_UserInfo.getConstValue() + "?" + getReqPama(params), null, netCallBack, rspCls);
	}

	/***
	 * 更新用户信息
	 * 
	 * @param context
	 * @param icon
	 * @param nickname
	 * @param sex
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void updateUserInfo(Context context, String icon, String nickname, String sex, NetCallBack<ServiceResult> netCallBack,
			Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("icon", icon);
		params.put("nickname", nickname);
		params.put("sex", sex);
		NetUtils.post(context, RequestType.User_UpdateUserInfo.getConstValue(), params, null, netCallBack, rspCls);
	}

	/***
	 * 获取电子会员卡
	 * 
	 * @param context
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void memberCard(Context context, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		NetUtils.getValue(context, RequestType.User_MemberCard.getConstValue() + "?" + getReqPama(params), null, netCallBack, rspCls);

	}

	/***
	 * 充值、续费-获取计费信息
	 * 
	 * @param context
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void memberfeeinfo(Context context, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		NetUtils.getValue(context, RequestType.User_MemberFeeinfo.getConstValue() + "?" + getReqPama(params), null, netCallBack, rspCls);
	}

	/****
	 * 获取新消息
	 * 
	 * @param context
	 * @param lastpulltime
	 * @param page
	 * @param pagesize
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void getNewMsgList(Context context, String lastpulltime, int page, int pagesize, NetCallBack<ServiceResult> netCallBack,
			Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("lastpulltime", lastpulltime);
		params.put("page", page + "");
		params.put("pagesize", pagesize + "");
		NetUtils.getValue(context, RequestType.Message_Newmsg.getConstValue() + "?" + getReqPama(params), "正在获取", netCallBack, rspCls);
	}

	/***
	 * 删除某条消息
	 * 
	 * @param context
	 * @param messageid
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void deleteMsg(Context context, String messageid, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("messageid", messageid);
		NetUtils.post(context, RequestType.Message_Deletemsg.getConstValue(), params, "正在删除", netCallBack, rspCls);
	}
	
	/***
	 * 已读某条消息
	 * 
	 * @param context
	 * @param messageid
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void readMsg(Context context, String messageid, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("messageid", "["+messageid+"]");
		NetUtils.post(context, RequestType.Message_ReadMsg.getConstValue(), params, null, netCallBack, rspCls);
	}
	
	

	/***
	 * 获取消息详情
	 * 
	 * @param context
	 * @param messageid
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void msginfo(Context context, String messageid, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("messageid", messageid);
		NetUtils.getValue(context, RequestType.Message_Msginfo.getConstValue() + "?" + getReqPama(params), null, netCallBack, rspCls);
	}

	/***
	 * 获取收货地址列表
	 * 
	 * @param context
	 * @param lastpulltime
	 * @param page
	 * @param pagesize
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void addresslist(Context context, String lastpulltime, int page, int pagesize, NetCallBack<ServiceResult> netCallBack,
			Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("lastpulltime", lastpulltime);
		params.put("page", String.valueOf(page));
		params.put("pagesize", String.valueOf(pagesize));
		NetUtils.getValue(context, RequestType.User_Addresslist.getConstValue() + "?" + getReqPama(params), "正在获取", netCallBack, rspCls);
	}

	/***
	 * 获取某个收货地址详情
	 * 
	 * @param context
	 * @param addressid
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void addressinfo(Context context, String addressid, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("addressid", addressid);
		NetUtils.getValue(context, RequestType.User_Addressinfo.getConstValue() + "?" + getReqPama(params), null, netCallBack, rspCls);
	}

	/***
	 * 新增收货地址
	 * 
	 * @param context
	 * @param provinceid
	 * @param cityid
	 * @param areaid
	 * @param street
	 * @param code
	 * @param name
	 * @param tel
	 * @param mobile
	 * @param isdefault
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void addAddress(Context context, int provinceid, int cityid, int areaid, String street, String code, String name,
			String tel, String mobile, int isdefault, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("provinceid", provinceid + "");
		params.put("cityid", cityid + "");
		params.put("areaid", areaid + "");
		params.put("street", street);
		params.put("code", code);
		params.put("name", name);
		params.put("tel", tel);
		params.put("mobile", mobile);
		params.put("isdefault", isdefault + "");
		NetUtils.post(context, RequestType.User_Addaddress.getConstValue(), params, "正在上传", netCallBack, rspCls);
	}

	/***
	 * 编辑收货地址
	 * 
	 * @param context
	 * @param addressid
	 * @param provinceid
	 * @param cityid
	 * @param areaid
	 * @param street
	 * @param code
	 * @param name
	 * @param tel
	 * @param mobile
	 * @param isdefault
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void updateaddress(Context context, String addressid, int provinceid, int cityid, int areaid, String street,
			String code, String name, String tel, String mobile, int isdefault, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("addressid", addressid);
		params.put("provinceid", provinceid + "");
		params.put("cityid", cityid + "");
		params.put("areaid", areaid + "");
		params.put("street", street);
		params.put("code", code);
		params.put("name", name);
		params.put("tel", tel);
		params.put("mobile", mobile);
		params.put("isdefault", isdefault + "");
		NetUtils.post(context, RequestType.User_UpdateAddress.getConstValue(), params, "正在上传", netCallBack, rspCls);
	}

	/***
	 * 删除收货地址
	 * 
	 * @param context
	 * @param addressid
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void deleteAddressItem(Context context, String addressid, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("addressid", addressid);
		NetUtils.post(context, RequestType.User_DeleteAddress.getConstValue(), params, "正在删除", netCallBack, rspCls);
	}

	/***
	 * 加入 购物车
	 * 
	 * @param context
	 * @param goodsid
	 * @param spec
	 * @param num
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void add(Context context, String goodsid, String spec, int num, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("goodsid", goodsid);
		params.put("spec", spec);
		params.put("num", num + "");
		NetUtils.post(context, RequestType.Cart_Add.getConstValue(), params, "正在添加商品到购物车", netCallBack, rspCls);
	}

	/**
	 * 购物车删除商品
	 * 
	 * @param context
	 * @param goodslist
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void cartDelete(Context context, String goodslist, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("goodslist", goodslist);
		NetUtils.post(context, RequestType.Cart_Delete.getConstValue(), params, "正在删除商品", netCallBack, rspCls);
	}
	
	/**
	 * 修改购物车商品数量
	 * @param context
	 * @param goodslist
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void cartUpdate(Context context, String goodslist, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("goodslist", goodslist);
		NetUtils.post(context, RequestType.Cart_Update.getConstValue(), params, null, netCallBack, rspCls);
	}
	
	/**
	 * 购物车同步
	 * 
	 * @param context
	 * @param lastsynctime
	 * @param page
	 * @param pagesize
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void getCartSync(Context context, String lastsynctime, int page, int pagesize, NetCallBack<ServiceResult> netCallBack,
			Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("lastsynctime", lastsynctime);
		params.put("page", page + "");
		params.put("pagesize", pagesize + "");
		NetUtils.getValue(context, RequestType.Cart_Sync.getConstValue() + "?" + getReqPama(params), null, netCallBack, rspCls);
	}

	/***
	 * 实体店 - 获取城市列表
	 * 
	 * @param context
	 * @param ip
	 * @param lng
	 * @param lat
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void storeCityList(Context context, boolean loading,String ip, String lng, String lat, NetCallBack<ServiceResult> netCallBack,
			Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("ip", ip);
		params.put("lng", lng);
		params.put("lat", lat);
		String string = null;
		if(loading){
			string = "正在获取数据";
		}
		NetUtils.getValue(context, RequestType.Business_StoreCityList.getConstValue() + "?" + getReqPama(params), string, netCallBack, rspCls);
	}

	/***
	 * 分类搜索项
	 * 
	 * @param context
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void catelement(Context context, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		NetUtils.getValue(context, RequestType.Business_Catelement.getConstValue() + "?" + getReqPama(params), null, netCallBack, rspCls);
	}

	/***
	 * 获取推荐商家
	 * 
	 * @param context
	 * @param cityid
	 *            用户位置所在城市id
	 * @param ip
	 * @param lng
	 * @param lat
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void storeHot(Context context, String cityid, String ip, String lng, String lat, NetCallBack<ServiceResult> netCallBack,
			Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("cityid", cityid);
		params.put("ip", ip);
		params.put("lng", lng);
		params.put("lat", lat);
		NetUtils.getValue(context, RequestType.Business_StoreHot.getConstValue() + "?" + getReqPama(params), null, netCallBack, rspCls);
	}

	/***
	 * 喜欢的商家
	 * 
	 * @param context
	 * @param cityid
	 *            用户位置所在城市id
	 * @param ip
	 * @param lng
	 * @param lat
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void storeRand(Context context, String cityid, String ip, String lng, String lat, NetCallBack<ServiceResult> netCallBack,
			Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("cityid", cityid);
		params.put("ip", ip);
		params.put("lng", lng);
		params.put("lat", lat);
		NetUtils.getValue(context, RequestType.Business_StoreRand.getConstValue() + "?" + getReqPama(params), null, netCallBack, rspCls);
	}

	/***
	 * 附近商家
	 * 
	 * @param context
	 * @param cityid
	 *            用户位置所在城市id
	 * @param ip
	 * @param lng
	 * @param lat
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void storeNear(Context context, String cityid, String ip, String lng, String lat, NetCallBack<ServiceResult> netCallBack,
			Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("cityid", cityid);
		params.put("ip", ip);
		params.put("lng", lng);
		params.put("lat", lat);
		NetUtils.getValue(context, RequestType.Business_StoreNear.getConstValue() + "?" + getReqPama(params), null, netCallBack, rspCls);
	}

	/***
	 * 实体店 热门搜索关键字
	 * 
	 * @param context
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void businessHotkeywords(Context context, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		NetUtils.getValue(context, RequestType.Business_Hotkeywords.getConstValue() + "?" + getReqPama(params), null, netCallBack, rspCls);
	}

	/***
	 * 获取商家搜索
	 * 
	 * @param context
	 * @param cityid
	 *            用户所选城市 （如苏州 66）
	 * @param classifyid
	 *            分类id 
	 * @param circleid
	 *            商圈id
	 * @param distance
	 *            距离（km）
	 * @param keywords
	 *            商家名，分类或商圈
	 * @param lng
	 * @param lat
	 * @param page
	 * @param pagesize
	 *            1-距离（默认），2-时间倒序
	 * @param netCallBack
	 * @param rspCls
	 */

	public static void businessSearch(Context context, String cityid, String classifyid, String circleid, String distance, String keywords,
			String lng, String lat, String sort, int page, int pagesize, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("cityid", cityid);
		params.put("classifyid", classifyid);
		params.put("circleid", circleid);
		params.put("distance", distance);
		params.put("keywords", keywords);
		params.put("lng", lng);
		params.put("lat", lat);
		params.put("sort", sort);
		params.put("page", page + "");
		params.put("pagesize", pagesize + "");
		NetUtils.getValue(context, RequestType.Business_Businesssearch.getConstValue() + "?" + getReqPama(params), "正在获取数据", netCallBack,
				rspCls);
	}

	/***
	 * 获取商家分类
	 * 
	 * @param context
	 * @param lastpulltime
	 *            上次更新本地分类缓存日期
	 * @param netCallBack
	 * @param rspCls
	 */

	public static void businessClassifyList(Context context, String lastpulltime, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("lastpulltime", lastpulltime);
		NetUtils.getValue(context, RequestType.Business_BusinessClassifyList.getConstValue() + "?" + getReqPama(params), null, netCallBack,
				rspCls);
	}

	/***
	 * 城市下的区和商圈
	 * 
	 * @param context
	 * @param cityid
	 * @param ip
	 * @param lng
	 * @param lat
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void businessCirclelist(Context context, String cityid, String ip, String lng, String lat,
			NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("cityid", cityid);
		params.put("ip", ip);
		params.put("lng", lng);
		params.put("citlatyid", lat);
		NetUtils.getValue(context, RequestType.Business_BusinessCirclelist.getConstValue() + "?" + getReqPama(params), null, netCallBack,
				rspCls);
	}

	/***
	 * 获取实体店商家详情
	 * 
	 * @param context
	 * @param businessid
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void discountstore(Context context, String businessid, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("businessid", businessid);
		NetUtils.getValue(context, RequestType.Business_Discountstore.getConstValue() + "?" + getReqPama(params), "正在获取数据", netCallBack, rspCls);
	}

	/***
	 * 实体折扣店评价
	 * 
	 * @param context
	 * @param businessid
	 * @param commentflag
	 *            评价等级，可选值为：good（好）/mid（中）/bad（差）
	 * @param page
	 * @param pagesize
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void storeCommentlist(Context context, String businessid, String commentflag, int page, int pagesize,
			NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("businessid", businessid);
		params.put("commentflag", commentflag);
		params.put("page", page + "");
		params.put("pagesize", pagesize + "");
		NetUtils.getValue(context, RequestType.Business_Commentlist.getConstValue() + "?" + getReqPama(params), null, netCallBack, rspCls);
	}

	/***
	 * 提交实体店评论
	 * 
	 * @param context
	 * @param businessid
	 * @param uid
	 * @param star
	 * @param content
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void storeComment(Context context, String businessid, String uid, String star, String content,
			NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("businessid", businessid);
		params.put("uid", uid);
		params.put("star", star);
		params.put("content", content);
		NetUtils.post(context, RequestType.Business_Comment.getConstValue(), params, "正在上传评论", netCallBack, rspCls);
	}

	/***
	 * 获取商品分类
	 * 
	 * @param context
	 * @param lastpulltime
	 *            上次更新本地分类缓存日期
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void getGoodsClassifyList(Context context, String lastpulltime, String msg, NetCallBack<ServiceResult> netCallBack,
			Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("lastpulltime", lastpulltime);
		NetUtils.getValue(context, RequestType.Goods_GoodsClassifyList.getConstValue() + "?" + getReqPama(params), msg, netCallBack, rspCls);
	}

	/***
	 * 热门搜索关键字
	 * 
	 * @param context
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void hotKeyWords(Context context, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		NetUtils.getValue(context, RequestType.Goods_HotKeyWords.getConstValue() + "?" + getReqPama(params), null, netCallBack, rspCls);
	}

	/***
	 * 搜索商品
	 * 
	 * @param context
	 * @param classifyid
	 *            商品分类id
	 * @param keywords
	 *            关键字
	 * @param price
	 *            价格附近
	 * @param spec
	 *            规格参数（规格:值;规格:值;…）例：”品牌:雅诗兰黛;适用人群:女士”
	 * @param orderby
	 *            排序字段，可选[time/price/salesvolume/hignopinion]，对应[最新/价格/销量/好评度]
	 * @param orderway
	 *            排序方式，只有orderby参数=price时才用到，可选[0/1]，对应[升序/降序]
	 * @param page
	 *            页码
	 * @param pagesize
	 *            需要返回的数据数量
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void goodsSearch(Context context, String classifyid, String keywords, String price, String spec, int stockflag, int voucherflag, String orderby,
			String orderway, int page, int pagesize, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("classifyid", classifyid);
		params.put("keywords", keywords);
		params.put("price", price);
		params.put("spec", spec);
		params.put("stockflag", Integer.toString(stockflag));
		params.put("voucherflag", Integer.toString(voucherflag));
		params.put("orderby", orderby);
		params.put("orderway", orderway);
		params.put("page", page + "");
		params.put("pagesize", pagesize + "");
		NetUtils.getValue(context, RequestType.Goods_Search.getConstValue() + "?" + getReqPama(params), "正在获取数据", netCallBack, rspCls);
	}

	/***
	 * 
	 * 
	 * @param context
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void goodsarea(Context context, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		NetUtils.getValue(context, RequestType.Goods_Goodsarea.getConstValue() + "?" + getReqPama(params), null, netCallBack, rspCls);
	}

	/***
	 * 获取旗舰店详情
	 * 
	 * @param context
	 * @param businessid
	 *            旗舰店id,
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void storeproFile(Context context, String businessid, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("businessid", businessid);
		NetUtils.getValue(context, RequestType.Business_StoreproFile.getConstValue() + "?" + getReqPama(params), null, netCallBack, rspCls);
	}

	/***
	 * 获取旗舰店商品列表
	 * 
	 * @param context
	 * @param businessid
	 *            旗舰店id,
	 * @param page
	 * @param pagesize
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void storeGoodsList(Context context, String businessid, int page, int pagesize, NetCallBack<ServiceResult> netCallBack,
			Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("businessid", businessid);
		params.put("page", page + "");
		params.put("pagesize", pagesize + "");
		NetUtils.getValue(context, RequestType.Goods_StoreGoodsList.getConstValue() + "?" + getReqPama(params), null, netCallBack, rspCls);
	}

	/***
	 * 获取商品详情
	 * 
	 * @param context
	 * @param goodsid
	 *            商品id,
	 * @param ip
	 * @param lng
	 * @param lat
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void profile(Context context, String goodsid, String ip, String lng, String lat, NetCallBack<ServiceResult> netCallBack,
			Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("goodsid", goodsid);
		params.put("ip", ip);
		params.put("lng", lng);
		params.put("lat", lat);
		NetUtils.getValue(context, RequestType.Goods_Profile.getConstValue() + "?" + getReqPama(params), "正在获取数据", netCallBack, rspCls);
	}

	/***
	 * 收藏商品
	 * 
	 * @param context
	 * @param goodsid
	 *            商品id
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void collectGoods(Context context, String goodsid, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("goodsid", goodsid);
		NetUtils.post(context, RequestType.Goods_CollectGoods.getConstValue(), params, null, netCallBack, rspCls);
	}

	/***
	 * 获取商品规格
	 * 
	 * @param context
	 * @param goodsid
	 *            商品id
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void goodsSpec(Context context, String goodsid, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("goodsid", goodsid);
		NetUtils.getValue(context, RequestType.Goods_GoodsSpec.getConstValue() + "?" + getReqPama(params), null, netCallBack, rspCls);
	}

	/***
	 * 获取商品某组规格所对应价格
	 * 
	 * @param context
	 * @param goodsid
	 *            商品id
	 * @param goodsspec
	 *            商品某组规格, 该参数传递每个规格在组里的下标并-隔开（例：1-0-2对应的规格组为“天空蓝、中国移动、规格值3”）
	 *            相应，如果用户选择“土豪金，中国联通，规格值2”这组规格，那么该参数应该传值：0-1-1 服务器会根据规格坐标，定位商品价格
	 * 
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void goodsprice(Context context, String goodsid, String goodsspec, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("goodsid", goodsid);
		params.put("goodsspec", goodsspec);
		NetUtils.getValue(context, RequestType.Goods_GoodsPrice.getConstValue() + "?" + getReqPama(params), null, netCallBack, rspCls);
	}

	/***
	 * 获取商品图文详情
	 * 
	 * @param context
	 * @param goodsid
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void goodsContent(Context context, String goodsid, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("goodsid", goodsid);
		NetUtils.getValue(context, RequestType.Goods_GoodsContent.getConstValue() + "?" + getReqPama(params), null, netCallBack, rspCls);
	}

	/***
	 * 获取商品评价列表
	 * 
	 * @param context
	 * @param goodsid
	 *            商品id
	 * @param commentflag
	 *            评价等级，可选值为：good（好）/mid（中）/bad（差）
	 * @param page
	 * @param pagesize
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void goodsCommentlist(Context context, String goodsid, String commentflag, int page, int pagesize,
			NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("goodsid", goodsid);
		params.put("commentflag", commentflag);
		params.put("page", page + "");
		params.put("pagesize", pagesize + "");
		NetUtils.getValue(context, RequestType.Goods_CommentList.getConstValue() + "?" + getReqPama(params), "正在获取数据", netCallBack, rspCls);
	}

	/***
	 * 选择区域查询邮费
	 * 
	 * @param context
	 * @param goodsid
	 *            商品id
	 * @param areaid
	 *            选择的区域id，对应商品详情接口返回的servicelist
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void goodsPostage(Context context, String goodsid, String areaid, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("goodsid", goodsid);
		params.put("areaid", areaid);
		NetUtils.getValue(context, RequestType.Goods_GoodsPostage.getConstValue() + "?" + getReqPama(params), null, netCallBack, rspCls);
	}

	/***
	 * 确认订单信息-立即购买/购物车选择商品结算
	 * 
	 * 
	 * @param context
	 * @param reqMsg
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void make(Context context, String op, String goods, String ip, String lng, String lat,
			NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("op", op);
		params.put("goods", goods);
		params.put("ip", ip);
		params.put("lng", lng);
		params.put("lat", lat);
		NetUtils.post(context, RequestType.Order_Make.getConstValue(), params, "正在获取数据", netCallBack, rspCls);
	}

	/***
	 * 提交生成订单
	 * 
	 * @param context
	 * @param reqMsg
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void save(Context context, String op, String addressid, String goods, int voucher,String walletpwd,
			NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("op", op);
		params.put("goods", goods);
		params.put("addressid", addressid);
		params.put("voucher", voucher + "");
		String encryptPass = "";
		if(!TextUtils.isEmpty(walletpwd)){
			encryptPass = MD5.encryptMD5(walletpwd);
		}
		params.put("walletpwd", encryptPass);
		NetUtils.post(context, RequestType.Order_Save.getConstValue(), params, "正在提交订单", netCallBack, rspCls);
	}

	/***
	 * 获取订单列表
	 * 
	 * @param context
	 * @param status
	 *            forpay待支付 forsend待发货 fortake待收货 forcomment待评价
	 * @param page
	 * @param pagesize
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void orderlist(Context context, String status, int page, int pagesize, NetCallBack<ServiceResult> netCallBack,
			Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("status", status);
		params.put("page", page + "");
		params.put("pagesize", pagesize + "");
		NetUtils.getValue(context, RequestType.Order_Orderlist.getConstValue() + "?" + getReqPama(params), "正在获取数据", netCallBack, rspCls);
	}

	/***
	 * 评价-某个订单待评价的商品
	 * 
	 * @param context
	 * @param orderno
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void orderComment(Context context, String orderno, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("orderno", orderno);
		NetUtils.getValue(context, RequestType.Order_Comment.getConstValue() + "?" + getReqPama(params), null, netCallBack, rspCls);
	}

	/**
	 * 发布评论
	 * 
	 * @param context
	 * @param orderno
	 * @param commentlist
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void sendcomment(Context context, String orderno, String commentlist, NetCallBack<ServiceResult> netCallBack,
			Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("orderno", orderno);
		params.put("commentlist", commentlist);
		NetUtils.post(context, RequestType.Order_Sendcomment.getConstValue(), params, null, netCallBack, rspCls);
	}

	/***
	 * 确认收货
	 * 
	 * @param context
	 * @param type
	 *            normal : 正常订单 change:换货订单 默认值normal
	 *            如果是换货商品列表中的确认收货，则type=change
	 * 
	 * @param orderno
	 * @param walletpwd
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void take(Context context, String type, String orderno, String walletpwd, String goodsid, String tradeid, String unique,
			NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("type", type);
		params.put("orderno", orderno);
		params.put("walletpwd", MD5.encryptMD5(walletpwd));
		params.put("goodsid", goodsid);
		params.put("tradeid", tradeid);
		params.put("unique", unique);
		NetUtils.post(context, RequestType.Order_Take.getConstValue(), params, null, netCallBack, rspCls);
	}

	/***
	 * 取消订单
	 * 
	 * @param context
	 * @param orderno
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void cancel(Context context, String orderno, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("orderno", orderno);
		NetUtils.post(context, RequestType.Order_Cancel.getConstValue(), params, "正在取消订单", netCallBack, rspCls);
	}

	/***
	 * 删除订单
	 * 
	 * @param context
	 * @param orderno
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void delete(Context context, String orderno, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("orderno", orderno);
		NetUtils.post(context, RequestType.Order_Delete.getConstValue(), params, "正在删除订单", netCallBack, rspCls);
	}

	/***
	 * 订单付款
	 * 
	 * @param context
	 * @param orderno
	 *            订单号 json数组 支持多个订单合并支付
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void pay(Context context, String orderno, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("orderno", orderno);
		NetUtils.post(context, RequestType.Order_Pay.getConstValue(), params, "正在获取付款信息", netCallBack, rspCls);
	}

	/***
	 * 提醒卖家发货
	 * 
	 * @param context
	 * @param orderno
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void notice(Context context, String orderno, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("orderno", orderno);
		NetUtils.post(context, RequestType.Order_Notice.getConstValue(), params, "提醒卖家发货", netCallBack, rspCls);
	}

	/***
	 * 申请售后服务-获取商品售后信息
	 * 
	 * @param context
	 * @param orderno
	 * @param goodsid
	 * @param tradeid
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void service(Context context, String orderno, String goodsid, String tradeid, NetCallBack<ServiceResult> netCallBack,
			Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("orderno", orderno);
		params.put("goodsid", goodsid);
		params.put("tradeid", tradeid);
		NetUtils.post(context, RequestType.Order_Service.getConstValue(), params, null, netCallBack, rspCls);
	}

	/**
	 * 申请售后服务-保存申请信息
	 * 
	 * @param context
	 * @param orderno
	 * @param goodsid
	 * @param tradeid
	 * @param type
	 *            return/change
	 * @param remark
	 * @param images
	 * @param addressid
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void serviceadd(Context context, String orderno, String goodsid, String tradeid, String type, String remark,
			String images, String addressid, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("orderno", orderno);
		params.put("goodsid", goodsid);
		params.put("tradeid", tradeid);
		params.put("type", type);
		params.put("remark", remark);
		params.put("images", images);
		params.put("addressid", addressid);
		NetUtils.post(context, RequestType.Order_Serviceadd.getConstValue(), params, null, netCallBack, rspCls);
	}

	/***
	 * 保存物流信息 (给商家用的)
	 * 
	 * @param context
	 * @param orderno
	 * @param goodsid
	 * @param tradeid
	 * @param type
	 * @param name
	 * @param phone
	 * @param logisticname
	 * @param logisticno
	 * @param remark
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void servicelogistic(Context context, String orderno, String goodsid, String tradeid, String type, String name,
			String phone, String logisticname, String logisticno, String remark, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("orderno", orderno);
		params.put("goodsid", goodsid);
		params.put("tradeid", tradeid);
		params.put("type", type);
		params.put("name", name);
		params.put("phone", phone);
		params.put("logisticname", logisticname);
		params.put("logisticno", logisticno);
		params.put("remark", remark);
		NetUtils.post(context, RequestType.Order_Servicelogistic.getConstValue(), params, null, netCallBack, rspCls);
	}

	/***
	 * 售货服务-获取商家收货地址
	 * 
	 * @param context
	 * @param orderno
	 * @param goodsid
	 * @param tradeid
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void servicebusiness(Context context, String orderno, String goodsid, String tradeid,String unique,
			NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("orderno", orderno);
		params.put("goodsid", goodsid);
		params.put("tradeid", tradeid);
		params.put("unique", unique);
		NetUtils.getValue(context, RequestType.Order_Servicebusiness.getConstValue() + "?" + getReqPama(params), "正在提交…", netCallBack, rspCls);
	}

	/***
	 * 查看物流
	 * 
	 * @param context
	 * @param type
	 *            normal/change,
	 * @param orderno
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void logistic(Context context, String type, String orderno, String goodsid, String tradeid, String unique,
			NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("type", type);
		params.put("orderno", orderno);
		params.put("goodsid", goodsid);
		params.put("tradeid", tradeid);
		params.put("unique", unique);
		NetUtils.getValue(context, RequestType.Order_Logistic.getConstValue() + "?" + getReqPama(params), null, netCallBack, rspCls);
	}

	/***
	 * 订单详情
	 * 
	 * @param context
	 * @param type
	 * @param orderno
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void orderProfile(Context context, String orderno, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("orderno", orderno);
		NetUtils.getValue(context, RequestType.Order_Profile.getConstValue() + "?" + getReqPama(params), "正在获取数据", netCallBack, rspCls);
	}

	/***
	 * 图片上传通用接口
	 * 
	 * @param context
	 * @param file
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void uploadimg(Context context, byte[] fileBytes, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("imgfile", Base64.encodeToString(fileBytes, Base64.DEFAULT));
		NetUtils.post(context, RequestType.Org_Uploadimg.getConstValue(), params, "正在上传", netCallBack, rspCls);
	}

	/***
	 * 更新个人信息--avatar
	 * 
	 * @param context
	 * @param file
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void updateAvatar(Context context, byte[] fileBytes, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("avatar", Base64.encodeToString(fileBytes, Base64.DEFAULT));
		NetUtils.post(context, RequestType.User_SetInfo.getConstValue(), params, "正在上传", netCallBack, rspCls);
	}

	/***
	 * 领取代金券
	 * 
	 * @param context
	 * @param orderno
	 * @param walletpwd
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void takevoucher(Context context, String orderno, String walletpwd, NetCallBack<ServiceResult> netCallBack,
			Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("walletpwd", walletpwd);
		params.put("orderno", orderno);
		NetUtils.post(context, RequestType.Order_Takevoucher.getConstValue(), params, null, netCallBack, rspCls);
	}

	/***
	 * 延迟收货
	 * 
	 * @param context
	 * @param type
	 * @param orderno
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void takedelay(Context context, String type, String orderno, String goodsid, String tradeid, String unique,
			NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("type", type);
		params.put("orderno", orderno);
		params.put("goodsid", goodsid);
		params.put("tradeid", tradeid);
		params.put("unique", unique);
		NetUtils.post(context, RequestType.Order_Takedelay.getConstValue(), params, "正在申请延迟收货…", netCallBack, rspCls);
	}

	/***
	 * 人工服务
	 * 
	 * @param context
	 * @param orderno
	 * @param goodsid
	 * @param tradeid
	 * @param type
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void servicemanual(Context context, String orderno, String goodsid, String tradeid, String type,
			NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("orderno", orderno);
		params.put("goodsid", goodsid);
		params.put("tradeid", tradeid);
		params.put("type", type);
		NetUtils.getValue(context, RequestType.Order_Servicemanual.getConstValue() + "?" + getReqPama(params), null, netCallBack, rspCls);
	}

	/***
	 * 售后服务-取消售后服务
	 * 
	 * @param context
	 * @param orderno
	 * @param goodsid
	 * @param tradeid
	 * @param type
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void servicecancel(Context context, String orderno, String goodsid, String tradeid, String type,
			NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("orderno", orderno);
		params.put("goodsid", goodsid);
		params.put("tradeid", tradeid);
		params.put("type", type);
		NetUtils.getValue(context, RequestType.Order_Servicecancel.getConstValue() + "?" + getReqPama(params), null, netCallBack, rspCls);
	}

	/***
	 * 售后服务-退换货商品列表
	 * 
	 * @param context
	 * @param type
	 * @param page
	 * @param pagesize
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void sgoodslist(Context context, String type, int page, int pagesize, NetCallBack<ServiceResult> netCallBack,
			Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("type", type);
		params.put("page", page + "");
		params.put("pagesize", pagesize + "");
		NetUtils.getValue(context, RequestType.Order_Sgoodslist.getConstValue() + "?" + getReqPama(params), null, netCallBack, rspCls);
	}

	/***
	 * 售后服务-商品售后详情
	 * 
	 * @param context
	 * @param goodsid
	 * @param unique
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void sgoodsprofile(Context context, String orderno,String goodsid, String unique, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("orderno", orderno);
		params.put("goodsid", goodsid);
		params.put("unique", unique);
		NetUtils.getValue(context, RequestType.Order_Sgoodsprofile.getConstValue() + "?" + getReqPama(params), "正在获取数据", netCallBack,
				rspCls);
	}

	/***
	 * 我的代金券-余额、流水
	 * 
	 * @param context
	 * @param page
	 * @param pagesize
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void myVoucher(Context context, int page, int pagesize, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("page", page + "");
		params.put("pagesize", pagesize + "");
		NetUtils.getValue(context, RequestType.Wallet_MyVoucher.getConstValue() + "?" + getReqPama(params), null, netCallBack, rspCls);
	}

	/***
	 * 我的代金券余额
	 * 
	 * @param context
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void myVoucherNumber(Context context, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		NetUtils.getValue(context, RequestType.Wallet_MyVoucher.getConstValue() + "?" + getReqPama(params), null, netCallBack, rspCls);
	}

	/***
	 * 检查是否有新的消息
	 * 
	 * @param context
	 * @param lastpulltime
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void myMessageNumber(Context context, String lastpulltime, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("lastpulltime", lastpulltime);
		NetUtils.getValue(context, RequestType.Message_Number.getConstValue() + "?" + getReqPama(params), null, netCallBack, rspCls);
	}

	/***
	 * 更新个人信息--修改昵称
	 * 
	 * @param context
	 * @param nickname
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void updateNickname(Context context, String nickname, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("nickname", nickname);
		NetUtils.post(context, RequestType.User_SetInfo.getConstValue(), params, null, netCallBack, rspCls);
	}

	/***
	 * 用户收藏信息概况-收藏数量
	 * 
	 * @param context
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void myCollectionNumbers(Context context, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		NetUtils.getValue(context, RequestType.Collection_Numbers.getConstValue() + "?" + getReqPama(params), null, netCallBack, rspCls);
	}

	/***
	 * 获取省数据列表
	 * 
	 * @param context
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void getAdressProvince(Context context, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		NetUtils.getValue(context, RequestType.Address_Province.getConstValue() + "?" + getReqPama(params), null, netCallBack, rspCls);
	}

	/***
	 * 获取某个省的市、区列表数据
	 * 
	 * @param context
	 * @param pid
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void getAdressCityTown(Context context, int pid, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("pid", pid + "");
		addPublic(params);
		NetUtils.getValue(context, RequestType.Address_City_Town.getConstValue() + "?" + getReqPama(params), null, netCallBack, rspCls);
	}

	/***
	 * 可领取的代金券列表
	 * 
	 * @param context
	 * @param page
	 * @param pageSize
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void getVoucherList(Context context, int page, String pageSize, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("page", String.valueOf(page));
		params.put("pagesize", pageSize);
		addPublic(params);
		NetUtils.getValue(context, RequestType.Wallet_NewVoucher.getConstValue() + "?" + getReqPama(params), "正在获取", netCallBack, rspCls);
	}

	/***
	 * 领取代金券
	 * 
	 * @param context
	 * @param orderNumber
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void takeVoucher(Context context, String orderNumber, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("orderno", orderNumber);
		addPublic(params);
		NetUtils.post(context, RequestType.Wallet_TakeVoucher.getConstValue(), params, "正在领取", netCallBack, rspCls);
	}

	/***
	 * 订单可领取代金券金额
	 * 
	 * @param context
	 * @param orderNumber
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void getVoucherAmount(Context context, String orderNumber, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("orderno", orderNumber);
		addPublic(params);
		NetUtils.getValue(context, RequestType.Wallet_VoucherAmount.getConstValue() + "?" + getReqPama(params), null, netCallBack, rspCls);
	}
	
	

	/**
	 * 代金券流水明细--all/get/use
	 */

	public static void getVoucherFlow(Context context, String type, String page, String pageSize, NetCallBack<ServiceResult> netCallBack,
			Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", type);
		params.put("page", page);
		params.put("pagesize", pageSize);
		addPublic(params);
		NetUtils.getValue(context, RequestType.Wallet_VoucherFlow.getConstValue() + "?" + getReqPama(params), null, netCallBack, rspCls);
	}
	
    /***
     * 收藏商品
     * 
     * @param context
     * @param goodsid
     * @param netCallBack
     * @param rspCls
     */
	public static void addfavgoods(Context context, String goodsid, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("goodsid", goodsid);
		addPublic(params);
		NetUtils.post(context, RequestType.User_Addfavgoods.getConstValue(), params, null, netCallBack, rspCls);
	}

	/**
	 * 收藏旗舰店
	 * @param context
	 * @param storeid
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void addfavstore(Context context, String storeid, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("storeid", storeid);
		addPublic(params);
		NetUtils.post(context, RequestType.User_Addfavstore.getConstValue(), params, null, netCallBack, rspCls);
	}
	
	/**
	 * 获取我收藏的商品
	 */

	public static void getMyCollectionGoods(Context context, String page, String pageSize, NetCallBack<ServiceResult> netCallBack,
			Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("page", page);
		params.put("pagesize", pageSize);
		addPublic(params);
		NetUtils.getValue(context, RequestType.User_FavGoodsList.getConstValue() + "?" + getReqPama(params), "正在获取数据", netCallBack, rspCls);
	}

	/**
	 * 删除我收藏的某个商品
	 */
	public static void delteMyCollectionGood(Context context, String goodIDs, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("goodsids", goodIDs);
		addPublic(params);
		NetUtils.post(context, RequestType.User_DelFavGoods.getConstValue(), params, null, netCallBack, rspCls);
	}

	/**
	 * 获取我收藏的旗舰店
	 */
	public static void getMyCollectionFlagStores(Context context, String page, String pageSize, NetCallBack<ServiceResult> netCallBack,
			Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("page", page);
		params.put("pagesize", pageSize);
		addPublic(params);
		NetUtils.getValue(context, RequestType.User_FavStoreList.getConstValue() + "?" + getReqPama(params), "正在获取数据", netCallBack, rspCls);
	}

	/**
	 * 首页广告
	 * 
	 * @param context
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void getAdvertiseIndexads(Context context, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		NetUtils.getValue(context, RequestType.Advertise_Indexads.getConstValue() + "?" + getReqPama(params), null, netCallBack, rspCls);
	}
	
	/***
	 * 实体店广告
	 * 
	 * @param context
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void getAdvertiseShopads(Context context, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		NetUtils.getValue(context, RequestType.Advertise_Shopads.getConstValue() + "?" + getReqPama(params), null, netCallBack, rspCls);
	}

	/**
	 * 删除我收藏的某个旗舰店
	 */
	public static void delteMyCollectionFlagStore(Context context, String storeID, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("storeids", storeID);
		addPublic(params);
		NetUtils.post(context, RequestType.User_DelFavStore.getConstValue(), params, null, netCallBack, rspCls);
	}

	/**
	 * 获取我收藏的实体店
	 */

	public static void getMyCollectionStores(Context context, String page, String pageSize, NetCallBack<ServiceResult> netCallBack,
			Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("page", page);
		params.put("pagesize", pageSize);
		addPublic(params);
		NetUtils.getValue(context, RequestType.User_FavBusinessStore.getConstValue() + "?" + getReqPama(params), "正在获取数据", netCallBack, rspCls);
	}
	
	/***
	 * 收藏实体店
	 * 
	 * @param context
	 * @param businessid
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void addfavbusiness(Context context, String businessid, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("businessid", businessid);
		NetUtils.post(context, RequestType.User_Addfavbusiness.getConstValue(), params, null, netCallBack, rspCls);
	}

	/**
	 * 删除我收藏的某个实体店
	 */
	public static void delteMyCollectionStore(Context context, String businessids, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		params.put("businessids", businessids);
		NetUtils.post(context, RequestType.User_DelFavBusiness.getConstValue(), params, null, netCallBack, rspCls);
	}

	/**
	 * 用户退出
	 */
	public static void logout(Context context, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		NetUtils.getValue(context, RequestType.User_Logout.getConstValue() + "?" + getReqPama(params), "正在退出", netCallBack, rspCls);
	}

	/**
	 * 订单信息概况-不同状态的订单数目等
	 */
	public static void getOrderNumbers(Context context, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		NetUtils.getValue(context, RequestType.Order_MyOrder.getConstValue() + "?" + getReqPama(params), null, netCallBack, rspCls);
	}

	/**
	 * 修改密码
	 */
	public static void updateUserPwd(Context context, String oldPwd, String newPwd, String confirmPwd,
			NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("oldpassword", oldPwd);
		params.put("password", newPwd);
		params.put("passwordc", confirmPwd);
		addPublic(params);
		NetUtils.post(context, RequestType.User_SetUserPwd.getConstValue(), params, null, netCallBack, rspCls);
	}

	/**
	 * 用户退出
	 */
	public static void checkIsSettedTraderPassword(Context context, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		NetUtils.getValue(context, RequestType.Wallet_CkUserWalletPwd.getConstValue() + "?" + getReqPama(params), null, netCallBack, rspCls);
	}

	/**
	 * 设置交易密码
	 */
	public static void setTradePwd(Context context, String newPwd, String confirmPwd, NetCallBack<ServiceResult> netCallBack,
			Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("password", newPwd);
		params.put("passwordc", confirmPwd);
		addPublic(params);
		NetUtils.post(context, RequestType.Wallet_SetUserWalletPwd.getConstValue(), params, null, netCallBack, rspCls);
	}

	/**
	 * 修改交易密码
	 */
	public static void modifyTradePwd(Context context, String oldPwd, String newPwd, String confirmPwd,
			NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("oldpassword", oldPwd);
		params.put("password", newPwd);
		params.put("passwordc", confirmPwd);
		addPublic(params);
		NetUtils.post(context, RequestType.Wallet_SetUserWalletPwd.getConstValue(), params, null, netCallBack, rspCls);
	}

	/**
	 * 忘记交易密码-验证手机和注册码
	 */
	public static void checkTradePhoneAndVcode(Context context, String phone, String vcode, NetCallBack<ServiceResult> netCallBack,
			Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("phone", phone);
		params.put("vcode", vcode);
		addPublic(params);
		NetUtils.post(context, RequestType.Wallet_ForgotUserWalletPwdPhone.getConstValue(), params, null, netCallBack, rspCls);
	}

	/**
	 * 忘记交易密码-设置新密码
	 */
	public static void setNewTradePwd(Context context, String phone, String vcode, String pwd, String pwdc,
			NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("phone", phone);
		params.put("vcode", vcode);
		params.put("password", pwd);
		params.put("passwordc", pwdc);
		addPublic(params);
		NetUtils.post(context, RequestType.Wallet_ForgotUserWalletPwdNew.getConstValue(), params, null, netCallBack, rspCls);
	}

	/***
	 * 设置某个收货地址为默认地址
	 * 
	 * @param context
	 * @param addressid
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void setDefaultAddress(Context context, String id, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("addressid", id);
		addPublic(params);
		NetUtils.post(context, RequestType.User_SetDefAddress.getConstValue(), params, "正在设置...", netCallBack, rspCls);
	}
	
	/**
	 * 客服电话
	 */
	public static void getServiceTelNumber(Context context, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		addPublic(params);
		NetUtils.getValue(context, RequestType.Public_ServiceTel.getConstValue() + "?" + getReqPama(params), null, netCallBack, rspCls);
	}
	
	
	/***
	 * 会员充值、续费-获取付款信息
	 * 
	 * @param context
	 * @param type
	 * @param netCallBack
	 * @param rspCls
	 */
	public static void membeCharge(Context context, String type, int times, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("type", type);
		params.put("times", String.valueOf(times));
		addPublic(params);
		NetUtils.post(context, RequestType.User_VPay.getConstValue(), params, null, netCallBack, rspCls);
	}
	
	/**
	 * 移动支付-获取支付相关信息
	 */
	public static void getNotifyUrl(Context context, String payWay, String payNo, NetCallBack<ServiceResult> netCallBack, Class<?> rspCls) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("payway", payWay);
		params.put("payno", payNo);
		addPublic(params);
		NetUtils.getValue(context, RequestType.Pay_PayInfo.getConstValue() + "?" + getReqPama(params), null, netCallBack, rspCls);
	}

	/***
	 * 获取请求参数
	 * 
	 * @param params
	 * @return
	 */
	public static String getReqPama(Map<String, String> params) {
		StringBuffer sBuffer = new StringBuffer();
		Iterator<?> iterator = params.keySet().iterator();
		while (iterator.hasNext()) {
			String key = (String) iterator.next();
			sBuffer.append(key + "=" + params.get(key) + "&");
		}
		String reqString = sBuffer.toString();
		return reqString.substring(0, reqString.length() - 1);
	}

	/***
	 * API请求公共参数
	 * 
	 * APP唯一标识appid，校验签名signature，时间戳timestamp，会话sessionid（cookie中）
	 * 
	 * @param params
	 * @return
	 */
	public static Map<String, String> addPublic(Map<String, String> params) {
		String appid = Config.getAppID();
		String appsecret = Config.getAppSecret();
		String picType = Config.getPicQuality();
		String ticket = TicketUtil.instance().getTicket();
		String timestamp = (System.currentTimeMillis() / 1000) + "";
		String sessionid = TicketUtil.instance().getSessionid();
		params.put("appid", appid);
		params.put("timestamp", timestamp);
		params.put("sessionid", sessionid);
		String signature = signature(appid, appsecret, ticket, timestamp);
		params.put("signature", signature);
		
		// 设置图片质量
		if (TextUtils.equals(picType,"auto")) {
			params.put("network", NetWorkUtils.getNetWorkType(MoLiApplication.getContext()));
		}else {
			params.put("network", picType);
		}
		
		return params;
	}

	/***
	 * API加密校验
	 * 
	 * API请求需要附带加密校验参数，服务器会校验该参数的值是否有效，判断该请求是否是合法的API请求，如果校验参数值不正确，则服务器拒绝响应
	 * 校验参数名：signature 校验参数生成规则：
	 * appid，appsecret，ticket，timestamp(unix时间戳)四个参数的参数值按字母顺序排序，做字符串连接之后，md5加密生成
	 * 
	 * @param appid
	 * @param appsecret
	 * @param ticket
	 * @param timestamp
	 * @return
	 */
	public static String signature(String appid, String appsecret, String ticket, String timestamp) {
		String[] sign = new String[4];
		sign[0] = appid;
		sign[1] = appsecret;
		sign[2] = ticket;
		sign[3] = timestamp;
		Arrays.sort(sign);
		String s = sign[0] + sign[1] + sign[2] + sign[3];
		return MD5.encryptMD5(s);
	}
}
