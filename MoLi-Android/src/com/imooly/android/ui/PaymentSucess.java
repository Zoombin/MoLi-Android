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
 * 支付成功界面
 * 
 * @author
 * 
 */
public class PaymentSucess extends BaseActivity implements OnClickListener {

	public static final String PAYNO = "payno";// 接收结算单号（String）
	public static final String PAY_MONEY = "pay_money";// 接收付款金额（float）
	public static final String GOOD_NAME = "pay_subject";// 商品名称
	public static final String GOOD_INFO = "pay_body";// 商品详情

	private static final String TYPE_ALIPAY = "alipay";
	private static final String TYPE_WEIXIN = "weixin";

	private RelativeLayout ll_title;
	private ImageView iv_back;
	private TextView tv_title, text_money;

	private float mPrice = 0.0f;
	private String mPayNo;
	private String mGoodName;
	private String mGoodInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment_sucess);

		logActivityName(this);
		Intent intent = getIntent();

		if (intent != null) {
			mPayNo = intent.getStringExtra(PaymentSucess.PAYNO);
			mPrice = intent.getFloatExtra(PaymentSucess.PAY_MONEY, 0.0f);
			mGoodName = intent.getStringExtra(PaymentSucess.GOOD_NAME);
			mGoodInfo = intent.getStringExtra(PaymentSucess.GOOD_INFO);
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

	private void initView() {

	}

	private void initData() {
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		Intent intent;

		switch (v.getId()) {
		case R.id.iv_back:

			finish();
			break;

		case R.id.relative_weixin:

			break;

		case R.id.relative_alipay:

			break;

		default:
			break;
		}
	}

}
