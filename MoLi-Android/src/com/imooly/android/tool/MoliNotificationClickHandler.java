package com.imooly.android.tool;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.imooly.android.entity.PushEntity;
import com.imooly.android.entity.PushEntity.ParamsEty;
import com.imooly.android.ui.MainActivity;
import com.imooly.android.ui.WebViewActivity;
import com.imooly.android.widget.Toast;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

/***
 * 推送消息点击处理
 * 
 * @author LSD
 * 
 */
public class MoliNotificationClickHandler extends UmengNotificationClickHandler {
	public String TAG = "NotificationClick";

	@Override
	public void launchApp(Context context, UMessage msg) {
		// TODO Auto-generated method stub
		super.launchApp(context, msg);
	}

	@Override
	public void openActivity(Context context, UMessage msg) {
		// TODO Auto-generated method stub
		super.openActivity(context, msg);
	}

	@Override
	public void openUrl(Context context, UMessage msg) {
		// TODO Auto-generated method stub
		super.openUrl(context, msg);
	}

	@Override
	public void dealWithCustomAction(Context context, UMessage msg) {
		// TODO Auto-generated method stub
		Log.i(TAG, "dealWithCustomAction  " + "custom = " + msg.custom);
		TicketUtil.instance().refrashTicket();
		handlerMsg(context, msg);
	}

	@Override
	public void handleMessage(Context context, UMessage msg) {
		// TODO Auto-generated method stub
		Log.i(TAG, "handleMessage  " + "custom = " + msg.custom);
		TicketUtil.instance().refrashTicket();
		handlerMsg(context, msg);
	}

	/***
	 * 消息分发
	 */
	private void handlerMsg(Context context, UMessage msg) {
		String pushMsg = msg.custom;
		PushEntity ety = null;
		try {
			ety = new Gson().fromJson(pushMsg, PushEntity.class);
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (ety == null) {
			Toast.show(context, "推送消息格式错误");
			return;
		}
		String activity = ety.getActivity();
		if ("page".equals(activity)) {
			String pageCode = ety.getPagecode();
			if(TextUtils.isEmpty(pageCode)){
				Toast.show(context, "推送消息格式错误");
				return;
			}
			ParamsEty paramsEty = ety.getParam();
			Map<String, String> params = null;
			if (paramsEty != null) {
				params = new HashMap<String, String>();
				params.put("goodsid", paramsEty.getGoodsid());
				params.put("fstoreid", paramsEty.getFstoreid());
				params.put("estoreid", paramsEty.getEstoreid());
				params.put("bclassifyid", paramsEty.getBclassifyid());
				params.put("pushAction", "goHomepage");
			}
			ActivityHandoffUtil.startActivity(context, pageCode, params);
		} else if ("app".equals(activity)) {
			Intent intent = new Intent();
			intent.setClass(context, MainActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			context.startActivity(intent);
		} else if ("link".equals(activity)) {
			String url = ety.getUrl();
			Intent intent = new Intent();
			intent.setClass(context, WebViewActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.putExtra("url", url);
			intent.putExtra("pushAction", "goHomepage");
			context.startActivity(intent);
		}
	}
}
