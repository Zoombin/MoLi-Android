package com.imooly.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.alipay.AliPay;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.entity.RspCallBack;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.wxpay.*;
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
	
	private static final String TYPE_ALIPAY = "alipay";
	private static final String TYPE_WEIXIN = "weixin";


	private RelativeLayout ll_title;
	private ImageView iv_back;
	private TextView tv_title, text_money;

	private float mPrice = 0.0f;
	private String mPayNo;
	private String mGoodName;
	private String mGoodInfo;
	
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
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	private void initView() {
		ll_title = (RelativeLayout) findViewById(R.id.ll_title);

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);

		tv_title = (TextView) findViewById(R.id.tv_title);

		text_money = (TextView) findViewById(R.id.text_money);
		text_money.setText(String.valueOf(mPrice));

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

			payWithPayNO(TYPE_WEIXIN);
			break;

		case R.id.relative_alipay:

			payWithPayNO(TYPE_ALIPAY);
			break;

		default:
			break;
		}
	}

	private void payWithPayNO(final String type) {

		Api.getNotifyUrl(self, type, mPayNo, new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {
				RspCallBack rsp = (RspCallBack) rspData;
				RspCallBack.Data data = rsp.data;
				
				String url = data.getCallback();
				
				Log.d("xxx", "notify url = " + url);

				if (TextUtils.equals(type, TYPE_ALIPAY)) {
					new AliPay(self).pay(mGoodName, mGoodInfo, mPayNo, String.valueOf(mPrice), url, new PayCallBack() {
						
						@Override
						public void onPaySuccess() {
							Intent intent = new Intent(self, PaymentSuccess.class);
							intent.putExtra(PAYNO, mPayNo);
							intent.putExtra(PAY_MONEY, mPrice);
							intent.putExtra(PAY_TYPE, "支付宝");
							self.startActivity(intent);
						}
						
						@Override
						public void onPayFailed() {
							
							self.startActivity(new Intent(self, PaymentFailed.class));
						}
					});
					
					return;
				}

				if (TextUtils.equals(type, TYPE_WEIXIN)) {
					new WeiXinPay(self).pay(mGoodInfo, mPayNo, String.valueOf(mPrice), url);
					return;
				}

			}

			@Override
			public void failed(String msg) {
				// TODO Auto-generated method stub

			}
		}, RspCallBack.class);
		
	}
}
