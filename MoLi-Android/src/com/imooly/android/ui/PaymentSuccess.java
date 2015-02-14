package com.imooly.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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
public class PaymentSuccess extends BaseActivity implements OnClickListener {

	public static final String PAYNO = "payno";// 接收结算单号（String）
	public static final String PAY_MONEY = "pay_money";// 接收付款金额（float）
	public static final String GOOD_NAME = "pay_subject";// 商品名称
	public static final String GOOD_INFO = "pay_body";// 商品详情

	private ImageView iv_payment_sucess_back;
	private TextView txt_payno, txt_price, txt_pay_type;
	private Button btn_shopping, btn_my_orders;
	private float mPrice = 0.0f;
	private String mPayNo = "";
	private String mPayType = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment_sucess);

		logActivityName(this);
		Intent intent = getIntent();

		if (intent != null) {
			mPayNo = intent.getStringExtra(PaymentActivity.PAYNO);
			mPrice = intent.getFloatExtra(PaymentActivity.PAY_MONEY, 0.0f);
			mPayType = intent.getStringExtra(PaymentActivity.PAY_TYPE);
		}

		initView();
		initData();

	}


	private void initView() {
		iv_payment_sucess_back = (ImageView) findViewById(R.id.iv_payment_sucess_back);
		iv_payment_sucess_back.setOnClickListener(this);
		
		txt_payno = (TextView) findViewById(R.id.txt_payno);
		txt_price = (TextView) findViewById(R.id.txt_price);
		txt_pay_type = (TextView) findViewById(R.id.txt_pay_type);
		
		btn_shopping = (Button) findViewById(R.id.btn_shopping);
		btn_shopping.setOnClickListener(this);
		
		btn_my_orders = (Button) findViewById(R.id.btn_my_orders);
		btn_my_orders.setOnClickListener(this);
	}

	private void initData() {
		
		txt_payno.setText(mPayNo);
		txt_price.setText(String.valueOf(mPrice));
		txt_pay_type.setText(mPayType);
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

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.iv_back:

			finish();
			break;

		case R.id.btn_shopping:

			startActivity(new Intent(this, MainActivity.class));
			break;

		case R.id.btn_my_orders:

			startActivity(new Intent(this, OrderActivity.class));
			break;

		default:
			break;
		}
	}

}
