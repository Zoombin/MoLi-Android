package com.imooly.android.ui;

import java.security.MessageDigest;
import java.text.DecimalFormat;
import java.util.LinkedList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.R.bool;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooly.android.MoLiApplication;
import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.alipay.AliPay;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.entity.RspCallBack;
import com.imooly.android.entity.RspWXPrePay;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.utils.Utils;
import com.imooly.android.widget.Toast;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * 付款页面
 * 
 * @author
 * 
 */
public class PaymentActivity extends BaseActivity implements OnClickListener {

	public static final String PAYNO = "payno";// 接收结算单号（String）
	public static final String PAY_MONEY = "pay_money";// 接收付款金额（float）
	public static final String GOOD_NAME = "pay_subject";// 商品名称
	public static final String GOOD_INFO = "pay_body";// 商品详情
	public static final String PAY_TYPE = "pay_type";
	public static final String INT_TYPE = "0";

	private static final String TYPE_ALIPAY = "alipay";
	private static final String TYPE_WEIXIN = "weixin";

	private ImageView iv_back;
	private TextView tv_title, text_money;

	private float mPrice = 0.0f;
	private String mPayNo;
	private String mGoodName;
	private String mGoodInfo;

	private IWXAPI api;

	private RelativeLayout rl_weixin, rl_alipay;

	public interface PayCallBack {

		public void onPaySuccess();

		public void onPayFailed();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment);

		logActivityName(this);
		Intent intent = getIntent();

		if (intent != null) {
			mPayNo = intent.getStringExtra(PaymentActivity.PAYNO);
			mPrice = intent.getFloatExtra(PaymentActivity.PAY_MONEY, 0.0f);
			mGoodName = intent.getStringExtra(PaymentActivity.GOOD_NAME);
			mGoodInfo = intent.getStringExtra(PaymentActivity.GOOD_INFO);
			
			MoLiApplication app = (MoLiApplication) this.getApplication();
			app.setmPayNo(mPayNo);
			app.setmPrice(mPrice);
		}

		Log.d("xxx", "mPayNo = " + mPayNo);
		Log.d("xxx", "mPrice = " + mPrice);

		initView();
		initData();

	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	private void initView() {

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);

		tv_title = (TextView) findViewById(R.id.tv_title);

		text_money = (TextView) findViewById(R.id.text_money);
		DecimalFormat fnum = new DecimalFormat("##0.00");
		text_money.setText("￥" + fnum.format(mPrice));

		rl_alipay = (RelativeLayout) findViewById(R.id.relative_alipay);
		rl_alipay.setOnClickListener(this);

		rl_weixin = (RelativeLayout) findViewById(R.id.relative_weixin);
		rl_weixin.setOnClickListener(this);
	}

	private void initData() {
	}

	@Override
	public void onClick(View v) {
		Intent intent;

		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;

		case R.id.relative_weixin:
			doWeiXinPay();

			break;

		case R.id.relative_alipay:
			doAlipayWithPayNO();

			break;

		default:
			break;
		}
	}

	private void doAlipayWithPayNO() {

		Api.getNotifyUrl(self, mPayNo, new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {
				RspCallBack rsp = (RspCallBack) rspData;
				RspCallBack.Data data = rsp.data;

				String url = data.getNotifyurl();

				Log.d("xxx", "notify url = " + url);
				
				//new AliPay(self).check();  // 检测支付宝账户是否存在

				new AliPay(self).pay(mGoodName, mGoodInfo, mPayNo, String.valueOf(mPrice), url, new PayCallBack() {
					
					@Override
					public void onPaySuccess() {
						Log.d("xxx", "onPaySuccess......");

						Intent intent = new Intent(self, PaymentSuccess.class);
						intent.putExtra(PAYNO, mPayNo);
						intent.putExtra(PAY_MONEY, mPrice);
						intent.putExtra(PAY_TYPE, "支付宝");
						self.startActivity(intent);
						PaymentActivity.this.finish();  //结束支付界面
					}

					@Override
					public void onPayFailed() {
						Log.d("xxx", "onPayFailed......");
						self.startActivity(new Intent(self, PaymentFailed.class));
						PaymentActivity.this.finish();  //结束支付界面
					}
				});

				return;

			}

			@Override
			public void failed(String msg) {
				// TODO Auto-generated method stub

			}
		}, RspCallBack.class);

	}

	private void doWeiXinPay() {
		

		
		if (!Utils.checkIsInstallWeiXin(this)) {
			Toast.show(self, "抱歉，您还没有安装微信！");
			return;
		}

		String ip = Utils.getLocalIpAddress();

		api = WXAPIFactory.createWXAPI(self, null);
		api.registerApp("wx501bd7cea77cc83a");

		Api.doWeiXinPay(self, mPayNo, ip, new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {
				RspWXPrePay rsp = (RspWXPrePay) rspData;
				RspWXPrePay.Data data = rsp.data;

				sendPayReq(data);

				PaymentActivity.this.finish();  //结束支付界面
			}

			@Override
			public void failed(String msg) {
				Toast.show(self, msg);
			}

		}, RspWXPrePay.class);
	}

	private void sendPayReq(RspWXPrePay.Data data) {

		// Log.d("xxx", "appid = " + data.getAppid());
		// Log.d("xxx", "noncestr = " + data.getNoncestr());
		// Log.d("xxx", "partnerid = " + data.getPartnerid());
		// Log.d("xxx", "prepayid = " + data.getPrepayid());
		// Log.d("xxx", "sign = " + data.getSign());
		// Log.d("xxx", "package = " + data.getStrpackage());
		// Log.d("xxx", "timestamp = " + data.getTimestamp());

		PayReq req = new PayReq();
		req.appId = data.getAppid();
		req.partnerId = data.getPartnerid();
		req.prepayId = data.getPrepayid();

		req.nonceStr = data.getNoncestr();

		req.timeStamp = data.getTimestamp();

		req.packageValue = "Sign=Wxpay";// "Sign=" + packageValue;

		List<NameValuePair> signParams = new LinkedList<NameValuePair>();
		signParams.add(new BasicNameValuePair("appid", req.appId));
		signParams.add(new BasicNameValuePair("appkey", data.getAppkey()));
		signParams.add(new BasicNameValuePair("noncestr", req.nonceStr));
		signParams.add(new BasicNameValuePair("package", req.packageValue));
		signParams.add(new BasicNameValuePair("partnerid", req.partnerId));
		signParams.add(new BasicNameValuePair("prepayid", req.prepayId));
		signParams.add(new BasicNameValuePair("timestamp", req.timeStamp));
		req.sign = genSign(signParams);

		Log.d("xxx", "req.sign = " + req.sign);

		// 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
		api.sendReq(req);
	}

	private String genSign(List<NameValuePair> params) {
		StringBuilder sb = new StringBuilder();

		int i = 0;
		for (; i < params.size() - 1; i++) {
			sb.append(params.get(i).getName());
			sb.append('=');
			sb.append(params.get(i).getValue());
			sb.append('&');
		}
		sb.append(params.get(i).getName());
		sb.append('=');
		sb.append(params.get(i).getValue());

		String sha1 = sha1(sb.toString());
		
		Log.d("xxx", "sb.toString" + sb.toString());

		return sha1;
	}
	
	
	public static String sha1(String str) {
		if (str == null || str.length() == 0) {
			return null;
		}

		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

		try {
			MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
			mdTemp.update(str.getBytes());

			byte[] md = mdTemp.digest();
			int j = md.length;
			char buf[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
				buf[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(buf);
		} catch (Exception e) {
			return null;
		}
	}
}
