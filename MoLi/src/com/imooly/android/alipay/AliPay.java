package com.imooly.android.alipay;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.imooly.android.ui.PaymentActivity.PayCallBack;
import com.imooly.android.widget.Toast;

public class AliPay {

	public static final String PARTNER = "2088311843874216";
	public static final String SELLER = "zhifu@imooly.com";
	public static final String RSA_PRIVATE = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBALGMI7Fkrl802eqATmqbT5GpCqrVHR60sBXSWclm7cT8FHfIkEqMu7oRGQQMAtUoqNeh4byvqG1j8jvxXBV2Z/c1UABC8ZdAXqjXQp+6fqmfMnNzwvJP511Ff7u4T003J557tlVaU3nKnpD/mxVYJlsipC5iMQZ70DuTuMJrZO+JAgMBAAECgYBMhCzNwqozdb+EhI9G+nAsQkHKpdXK6ewJO4JeffFyt4DKrrEgr84nvj6ds990pfU+GRIEE1/u5Of8VWRuC316IMPY9SEdwzFN65+zaZtnqpGmX6h3/SjL3LaKtv/oyP7eUXE7O71lHyOlJYKWtg/8PTDjJCf00Fl/lUFvUL5pQQJBANhY8a1B4kjr4KL4b9dfJJKedzz9vXPt6+o3PXNMELvCi7PmfodDeeuS4qY/3yBL7V8luwXwzm5WrJD79Cet3A0CQQDSFrFBFWCY5wsWU/waPf/BjbRX/ajTaTWOvoKR7EnaIjAuT2qFQKWEi4PrbptBnia6fgx+e/WOqx7Y9kP3YbZtAkEAtfj6LtT/1H4ykGGPEQSB6qFHghGbTOuOR473LQeJ+6QDheoV+wgSgMcnxNZsgunaWvGNgc2ulLhqpfiGwOlH8QJAbNDWJKjG7MuXAYykoo8EXqNgCsdW35G57OKeTKi/o91baVE3Eifm011UCeizP+yDkMri+8yG5suZYbVEhOi2jQJBAKuT4aYsIMw7NZi15R0KZobuoKRsMvbdHbx3u60QIKX6H2a0sYRbhn6PPhAxbSSYPEE7s9vwyVEnMglBIZckTo4=";
	public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCnxj/9qwVfgoUh/y2W89L6BkRAFljhNhgPdyPuBV64bfQNN1PjbCzkIM6qRdKBoLPXmKKMiFYnkd6rAoprih3/PrQEB/VsW8OoM8fxn67UDYuyBTqA23MML9q1+ilIZwBC2AQ2UBVOrFXfFl75p6/B5KsiNG9zpgmLCUYuLkxpLQIDAQAB";

	private static final int SDK_PAY_FLAG = 1;
	private static final int SDK_CHECK_FLAG = 2;

	// private static final int SDK_CHECK_FLAG = 2;

	private Activity mActivity;
	
	private PayCallBack payCallBack;



	public AliPay(Activity mActivity) {
		this.mActivity = mActivity;
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case SDK_PAY_FLAG: {
				Result resultObj = new Result((String) msg.obj);
				String resultStatus = resultObj.resultStatus;
				
				Log.d("xxx", ">>>>>>>>>>>>>>resultStatus = " + resultStatus);

				// 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
				if (TextUtils.equals(resultStatus, "9000")) {
					Toast.show(mActivity, "支付成功");
					payCallBack.onPaySuccess();

				} else {
					// 判断resultStatus 为非“9000”则代表可能支付失败
					// “8000”
					// 代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
					if (TextUtils.equals(resultStatus, "8000")) {
						Toast.show(mActivity, "支付结果确认中");

					} else {
						Toast.show(mActivity, "支付失败");
						payCallBack.onPayFailed();
					}
				}
				
				break;
			}
			case SDK_CHECK_FLAG: {

				if ((Boolean) (msg.obj = true)) {
					Toast.show(mActivity, "检查结果为：" + msg.obj);
				} else {
					Toast.show(mActivity, "检查结果为：" + msg.obj);
				}

				break;
			}
			default:
				break;
			}
		};
	};

	/**
	 * call alipay sdk pay. 调用SDK支付
	 * 
	 */
	public void pay(String productName, String productInfo, String tradeNo, String price, String notifyUrl, PayCallBack payCallBack) {

		// String orderInfo = getOrderInfo("测试的商品", "该测试商品的详细描述", "0.01");
		this.payCallBack = payCallBack;
		String orderInfo = getOrderInfo(productName, productInfo, tradeNo, price, notifyUrl);

		String sign = sign(orderInfo);
		try {
			// 仅需对sign 做URL编码
			sign = URLEncoder.encode(sign, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		final String payInfo = orderInfo + "&sign=\"" + sign + "\"&" + getSignType();

		Runnable payRunnable = new Runnable() {

			@Override
			public void run() {
				// 构造PayTask 对象
				PayTask alipay = new PayTask(mActivity);
				// 调用支付接口
				String result = alipay.pay(payInfo);

				Message msg = new Message();
				msg.what = SDK_PAY_FLAG;
				msg.obj = result;
				mHandler.sendMessage(msg);
			}
		};

		Thread payThread = new Thread(payRunnable);
		payThread.start();
	}

	/**
	 * check whether the device has authentication alipay account.
	 * 查询终端设备是否存在支付宝认证账户
	 * 
	 */
	public void check() {
		Runnable checkRunnable = new Runnable() {

			@Override
			public void run() {
				PayTask payTask = new PayTask(mActivity);
				boolean isExist = payTask.checkAccountIfExist();
				Message msg = new Message();
				msg.what = SDK_CHECK_FLAG;
				msg.obj = isExist;
				mHandler.sendMessage(msg);
			}
		};
	
	 Thread checkThread = new Thread(checkRunnable);
	 checkThread.start();
	
	 }

	/**
	 * get the sdk version. 获取SDK版本号
	 * 
	 */
	public void getSDKVersion() {
		PayTask payTask = new PayTask(mActivity);
		String version = payTask.getVersion();
		Toast.show(mActivity, version);
	}

	/**
	 * create the order info. 创建订单信息
	 * 
	 */
	public String getOrderInfo(String subject, String body, String tradeNo, String price, String notifyUrl) {
		// 合作者身份ID
		String orderInfo = "partner=" + "\"" + PARTNER + "\"";

		// 卖家支付宝账号
		orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

		// 商户网站唯一订单号
		orderInfo += "&out_trade_no=" + "\"" + tradeNo + "\"";

		// 商品名称
		orderInfo += "&subject=" + "\"" + subject + "\"";

		// 商品详情
		orderInfo += "&body=" + "\"" + body + "\"";

		// 商品金额
		orderInfo += "&total_fee=" + "\"" + price + "\"";

		// 服务器异步通知页面路径
		// orderInfo += "&notify_url=" + "\"" + "http://notify.msp.hk/notify.htm" + "\"";
		
		orderInfo += "&notify_url=" + "\"" + notifyUrl + "\"";

		// 接口名称， 固定值
		orderInfo += "&service=\"mobile.securitypay.pay\"";

		// 支付类型， 固定值
		orderInfo += "&payment_type=\"1\"";

		// 参数编码， 固定值
		orderInfo += "&_input_charset=\"utf-8\"";

		// 设置未付款交易的超时时间
		// 默认30分钟，一旦超时，该笔交易就会自动被关闭。
		// 取值范围：1m～15d。
		// m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
		// 该参数数值不接受小数点，如1.5h，可转换为90m。
		orderInfo += "&it_b_pay=\"30m\"";

		// 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
		// orderInfo += "&return_url=\"m.alipay.com\"";

		// 调用银行卡支付，需配置此参数，参与签名， 固定值
		// orderInfo += "&paymethod=\"expressGateway\"";
		
		Log.d("xxx", ">>>>>>>orderInfo = " + orderInfo);

		return orderInfo;
	}

	// /**
	// * get the out_trade_no for an order. 获取外部订单号
	// *
	// */
	// public String getOutTradeNo() {
	// SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
	// Locale.getDefault());
	// Date date = new Date();
	// String key = format.format(date);
	//
	// Random r = new Random();
	// key = key + r.nextInt();
	// key = key.substring(0, 15);
	// return key;
	// }

	/**
	 * sign the order info. 对订单信息进行签名
	 * 
	 * @param content
	 *            待签名订单信息
	 */
	public String sign(String content) {
		return SignUtils.sign(content, RSA_PRIVATE);
	}

	/**
	 * get the sign type we use. 获取签名方式
	 * 
	 */
	public String getSignType() {
		return "sign_type=\"RSA\"";
	}

}
