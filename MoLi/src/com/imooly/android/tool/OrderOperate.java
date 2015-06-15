package com.imooly.android.tool;

import java.text.DecimalFormat;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.imooly.android.MoLiApplication;
import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.entity.RspFlagCommon;
import com.imooly.android.entity.RspPay;
import com.imooly.android.entity.RspSuccessCommon;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.ui.PaymentActivity;
import com.imooly.android.ui.TradePwdActivity;
import com.imooly.android.ui.TradePwdSettingActivity;
import com.imooly.android.view.PayConfirmDialog;
import com.imooly.android.view.PayConfirmDialog.PayConfirmCallBack;
import com.imooly.android.widget.CustomDialog;
import com.imooly.android.widget.CustomDialog.CGravity;
import com.imooly.android.widget.Toast;

public class OrderOperate {
	public interface OCallBack {
		void success();

		void failed(String msg);
	}

	/***
	 * 确认收货
	 * 
	  * @param context
	  * @param type
	  * @param orderno
	  * @param walletpwd
	  * @param goodsid
	  * @param tradeid
	  * @param unique
	  * @param callBack
	  */
	public static void take(final Context context, String type, String orderno, String walletpwd, String goodsid, String tradeid,
			String unique, final OCallBack callBack) {
		Api.take(context, type, orderno, walletpwd, goodsid, tradeid, unique, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				// TODO Auto-generated method stub
				RspSuccessCommon rsp = (RspSuccessCommon) rspData;
				if (rsp.data.success == 1) {
					Toast.show(context, "确认成功");
					if (callBack != null)
						callBack.success();
				} else {
					Toast.show(context, rsp.data.message);
					if (callBack != null)
						callBack.failed("");
				}
			}

			@Override
			public void failed(String msg) {
				// TODO Auto-generated method stub
				Toast.show(context, msg);
				if (callBack != null)
					callBack.failed(msg);
			}
		}, RspSuccessCommon.class);
	}

	/***
	 * 取消订单
	 * 
	 * @param context
	 * @param ety
	 */
	public static void cancelOrder(final Context context, String orderno, final OCallBack callBack) {
		Api.cancel(context, orderno, new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {
				// TODO Auto-generated method stub
				RspSuccessCommon rsp = (RspSuccessCommon) rspData;
				if (rsp.data.success == 1) {
					Toast.show(context, "取消成功");
					if (callBack != null)
						callBack.success();
				} else {
					Toast.show(context, "取消失败");
					if (callBack != null)
						callBack.failed("");
				}
			}

			@Override
			public void failed(String msg) {
				// TODO Auto-generated method stub
				if (!TextUtils.isEmpty(msg)) {
					Toast.show(context, msg);
				}
				if (callBack != null)
					callBack.failed(msg);
			}
		}, RspSuccessCommon.class);
	}

   /***
    * 删除订单
    * 
    * @param context
    * @param orderno
    * @param callBack
    */
	public static void deleteOrder(final Context context, String orderno, final OCallBack callBack) {
		Api.delete(context, orderno, new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {
				// TODO Auto-generated method stub
				RspSuccessCommon rsp = (RspSuccessCommon) rspData;
				if (rsp.data.success == 1) {
					Toast.show(context, "删除成功");
					if (callBack != null)
						callBack.success();
				} else {
					Toast.show(context, "删除失败");
					if (callBack != null)
						callBack.failed("");
				}
			}

			@Override
			public void failed(String msg) {
				// TODO Auto-generated method stub
				if (!TextUtils.isEmpty(msg)) {
					Toast.show(context, msg);
				}
				if (callBack != null)
					callBack.failed(msg);
			}
		}, RspSuccessCommon.class);
	}

	/***
	 * 可以合并付款
	 * 
	 * @param context
	 * @param orderno
	 * @param callBack
	 */
	public static void payOrder(final Context context,String orderno, final OCallBack callBack) {
		Api.pay(context, orderno, new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {
				// TODO Auto-generated method stub
				RspPay rsp = (RspPay) rspData;
				if (rsp.data != null) {
					Toast.show(context, "获取信息成功，去付款");
					// 设置会员充值为false
					MoLiApplication.getInstance().setMemeberPay(false);
					
					Intent intent = new Intent();
					intent.setClass(context, PaymentActivity.class);
					intent.putExtra(PaymentActivity.PAYNO, rsp.data.getPayno());
					intent.putExtra(PaymentActivity.PAY_MONEY, rsp.data.getPayamount());
					intent.putExtra(PaymentActivity.GOOD_NAME, rsp.data.getPaysubject());
					intent.putExtra(PaymentActivity.GOOD_INFO, rsp.data.getPaybody());
					context.startActivity(intent);
					
					if (callBack != null)
						callBack.success();
				} else {
					Toast.show(context, "付款失败");
					if (callBack != null)
						callBack.failed("");
				}
			}

			@Override
			public void failed(String msg) {
				// TODO Auto-generated method stub
				if (!TextUtils.isEmpty(msg)) {
					Toast.show(context, msg);
				}
				if (callBack != null)
					callBack.failed(msg);
			}
		}, RspPay.class);
	}

	/***
	 * 提醒卖家发货
	 * 
	 * @param context
	 * @param orderno
	 * @param callBack
	 */
	public static void noticeOrder(final Context context, String orderno, final OCallBack callBack) {
		Api.notice(context, orderno, new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {
				// TODO Auto-generated method stub
				RspSuccessCommon rsp = (RspSuccessCommon) rspData;
				if (rsp.data.success == 1) {
					Toast.show(context, "已提醒卖家尽快发货!");
					if (callBack != null)
						callBack.success();
				} else {
					Toast.show(context, "提醒失败了,再试一次!");
					if (callBack != null)
						callBack.failed("");
				}
			}

			@Override
			public void failed(String msg) {
				// TODO Auto-generated method stub
				if (!TextUtils.isEmpty(msg)) {
					Toast.show(context, msg);
				}
				if (callBack != null)
					callBack.failed(msg);
			}
		}, RspSuccessCommon.class);
	}

	/***
	 * 领取代金券
	 * 
	 * @param context
	 * @param orderno
	 * @param callBack
	 */
	public static void takevoucher(final Context context, String orderno, final OCallBack callBack) {
		Api.takevoucher(context, orderno, "", new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {
				// TODO Auto-generated method stub
				RspSuccessCommon rsp = (RspSuccessCommon) rspData;
				if (rsp.data.success == 1) {
					Toast.show(context, "领取成功");
					if (callBack != null)
						callBack.success();
				} else {
					Toast.show(context, "领取失败");
					if (callBack != null)
						callBack.failed("");
				}
			}

			@Override
			public void failed(String msg) {
				// TODO Auto-generated method stub
				if (!TextUtils.isEmpty(msg)) {
					Toast.show(context, msg);
				}
				if (callBack != null)
					callBack.failed(msg);
			}
		}, RspSuccessCommon.class);
	}
	
	/***
	 * 延迟收货
	 * 
	 * @param context
	 * @param type
	 * @param orderno
	 *            换货的时候需要后面几个参数
	 * @param goodsid
	 * @param tradeid
	 * @param unique
	 * @param callBack
	 */

	public static void takedelay(final Context context, String type, String orderno, String goodsid, String tradeid, String unique,
			final OCallBack callBack) {
		Api.takedelay(context, type, orderno, goodsid, tradeid, unique, new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {
				// TODO Auto-generated method stub
				RspSuccessCommon rsp = (RspSuccessCommon) rspData;
				if (rsp.data.success == 1) {
					Toast.show(context, "延迟收货成功");
					if (callBack != null)
						callBack.success();
				} else {
					Toast.show(context, "延迟收货失败，请重试");
					if (callBack != null)
						callBack.failed("");
				}
			}

			@Override
			public void failed(String msg) {
				// TODO Auto-generated method stub
				if (!TextUtils.isEmpty(msg)) {
					Toast.show(context, msg);
				}
				if (callBack != null)
					callBack.failed(msg);
			}
		}, RspSuccessCommon.class);
	}
	

	/***
	 * 人工服务
	 * 
	 * @param context
	 * @param orderno
	 * @param goodsid
	 * @param tradeid
	 * @param type
	 * @param callBack
	 */
	public static void servicemanual(final Context context, String orderno,String goodsid,String tradeid, String type,final OCallBack callBack){
		Api.servicemanual(context, orderno, goodsid, tradeid,type,new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {
				// TODO Auto-generated method stub
				RspSuccessCommon rsp = (RspSuccessCommon) rspData;
				if (rsp.data.success == 1) {
					Toast.show(context, "申请成功");
					if (callBack != null)
						callBack.success();
				} else {
					Toast.show(context, "申请失败");
					if (callBack != null)
						callBack.failed("");
				}
			}

			@Override
			public void failed(String msg) {
				// TODO Auto-generated method stub
				if (!TextUtils.isEmpty(msg)) {
					Toast.show(context, msg);
				}
				if (callBack != null)
					callBack.failed(msg);
			}
		}, RspSuccessCommon.class);
	}
	
	/***
	 * 售后服务-取消售后服务
	 * 
	 * @param context
	 * @param orderno
	 * @param goodsid
	 * @param tradeid
	 * @param type
	 * @param callBack
	 */
	public static void servicecancel(final Context context, String orderno,String goodsid,String tradeid, String type,final OCallBack callBack){
		Api.servicecancel(context, orderno, goodsid, tradeid,type,new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {
				// TODO Auto-generated method stub
				RspSuccessCommon rsp = (RspSuccessCommon) rspData;
				if (rsp.data.success == 1) {
					Toast.show(context, "取消售后成功");
					if (callBack != null)
						callBack.success();
				} else {
					Toast.show(context, "取消售后失败");
					if (callBack != null)
						callBack.failed("");
				}
			}

			@Override
			public void failed(String msg) {
				// TODO Auto-generated method stub
				if (!TextUtils.isEmpty(msg)) {
					Toast.show(context, msg);
				}
				if (callBack != null)
					callBack.failed(msg);
			}
		}, RspSuccessCommon.class);
	}
	
	/***
	 * 确认收货 先检查是否设置了交易密码
	 * 
	 * @param context
	 * @param name
	 * @param price
	 * @param type
	 * @param orderno
	 * @param goodsid
	 * @param tradeid
	 * @param unique
	 * @param callBack
	 */
	public static void checkIsSettedTraderPassword(final Context context, final String name, final float price, final String type, final String orderno,
			final String goodsid, final String tradeid, final String unique, final OCallBack callBack) {
		// 检查是否设置了交易密码
		Api.checkIsSettedTraderPassword(context, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				RspFlagCommon rsp = (RspFlagCommon) rspData;
				RspFlagCommon.Data data = rsp.data;
				if (data.getFlag() == 1) {
					showConfirmDialog(context, name, price, type, orderno, goodsid, tradeid, unique, callBack);
				} else {
					// 设置交易密码
					Toast.show(context, "请设置交易密码");
					context.startActivity(new Intent(context, TradePwdSettingActivity.class));
				}
			}

			@Override
			public void failed(String msg) {
				// 设置交易密码
				Toast.show(context, "请设置交易密码");
				context.startActivity(new Intent(context, TradePwdSettingActivity.class));
			}
		}, RspFlagCommon.class);
	}
	
	
	private static void showConfirmDialog(final Context context, String name, float price, final String type, final String orderno,
			final String goodsid, final String tradeid, final String unique, final OCallBack callBack) {
		final View view = LayoutInflater.from(context).inflate(R.layout.view_comm_dialog, null);
		final CustomDialog dialog = CustomDialog.create(context);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentVw(view).setGravity(CGravity.CENTER).show();

		((TextView) view.findViewById(R.id.title)).setText(context.getString(R.string.order_confirm_dialog_title));
		DecimalFormat fnum = new DecimalFormat("##0.00");
		((TextView) view.findViewById(R.id.content)).setText(/*name + "\n" + */"价格：" + fnum.format(price)+"元");

		final TextView pass = (TextView) view.findViewById(R.id.pass);

		view.findViewById(R.id.yes).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String passStr = pass.getText().toString();
				if (TextUtils.isEmpty(passStr)) {
					Toast.show(context, "请输入密码");
					return;
				}
				// 正常订单不需要后面的参数
				OrderOperate.take(context, type, orderno, passStr, goodsid, tradeid, unique, new OCallBack() {
					@Override
					public void success() {
						// TODO Auto-generated method stub
						dialog.dismiss();
						if (callBack != null) {
							callBack.success();
						}
					}

					@Override
					public void failed(String msg) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
			}
		});
		view.findViewById(R.id.no).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
	}

}
