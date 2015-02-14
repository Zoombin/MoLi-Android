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
 * 支付失败界面
 * 
 * @author
 * 
 */
public class PaymentFailed extends BaseActivity implements OnClickListener {


	private ImageView iv_payment_sucess_back;
	private Button btn_shopping, btn_repay;
	private String mPayType = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment_failed);

		logActivityName(this);
		
		initView();
		initData();

	}


	private void initView() {
		iv_payment_sucess_back = (ImageView) findViewById(R.id.iv_payment_sucess_back);
		iv_payment_sucess_back.setOnClickListener(this);
		
		
		btn_shopping = (Button) findViewById(R.id.btn_go_shopping);
		btn_shopping.setOnClickListener(this);
		
		btn_repay = (Button) findViewById(R.id.btn_repay);
		btn_repay.setOnClickListener(this);
	}

	private void initData() {
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

		case R.id.btn_go_shopping:

			startActivity(new Intent(this, MainActivity.class));
			
			break;

		case R.id.btn_repay:

			startActivity(new Intent(this, OrderActivity.class));
			
			break;

		default:
			break;
		}
	}

}
