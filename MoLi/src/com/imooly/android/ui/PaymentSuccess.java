package com.imooly.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.imooly.android.MoLiApplication;
import com.imooly.android.R;
import com.imooly.android.base.BaseActivity;

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
	private TextView mTextView;
	

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
		
		mTextView = (TextView) findViewById(R.id.text_success_info);
	}

	private void initData() {
		
		txt_payno.setText("订单编号：" + mPayNo);
		txt_price.setText("已付金额：" + String.valueOf(mPrice));
		txt_pay_type.setText("支付方式："+ mPayType);
		
		if (MoLiApplication.getInstance().isMemeberPay() == true) {
			mTextView.setText("会员充值成功！");
		}
		
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
		// 设置会员充值为false
		MoLiApplication.getInstance().setMemeberPay(false);
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		
		switch (v.getId()) {
		case R.id.iv_payment_sucess_back:

			finish();
			break;

		case R.id.btn_shopping:
			//继续购物  -- 跳转到首页
			
			Intent intent = new Intent(MainActivity.ACTION);
			//发送广播关闭已经打开的页面
			sendBroadcast(intent);

			startActivity(new Intent(self, MainActivity.class));
			finish();
			break;

		case R.id.btn_my_orders:
			//我的订单  -- 跳转到订单页面

			startActivity(new Intent(this, OrderActivity.class));
			PaymentSuccess.this.finish();
			break;

		default:
			break;
		}
	}

}
