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
 * 支付失败界面
 * 
 * @author
 * 
 */
public class PaymentFailed extends BaseActivity implements OnClickListener {


	private ImageView iv_payment_sucess_back;
	private Button btn_shopping, btn_repay;
	private String mPayType = "";
	private TextView mTextView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_payment_failed);

		logActivityName(this);
		
		initView();
		initData();

	}


	private void initView() {
		iv_payment_sucess_back = (ImageView) findViewById(R.id.iv_payment_failed_back);
		iv_payment_sucess_back.setOnClickListener(this);
		
		
		btn_shopping = (Button) findViewById(R.id.btn_go_shopping);
		btn_shopping.setOnClickListener(this);
		
		btn_repay = (Button) findViewById(R.id.btn_repay);
		btn_repay.setOnClickListener(this);
		
		mTextView = (TextView) findViewById(R.id.text_failed_info);

	}

	private void initData() {
		
		if (MoLiApplication.getInstance().isMemeberPay() == true) {
			mTextView.setText("会员充值失败！");
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

		case R.id.btn_go_shopping:
			//随便逛逛  -- 跳转到首页
			
			Intent intent = new Intent(MainActivity.ACTION);
			//发送广播关闭已经打开的页面
			sendBroadcast(intent);

			startActivity(new Intent(self, MainActivity.class));
			finish();
			break;

		case R.id.btn_repay:
			//重新支付
			if(MoLiApplication.getInstance().isMemeberPay()){
				//会员支付
				startActivity(new Intent(this, MemberRechargeActivity.class));
			}else{
				//订单支付
				startActivity(new Intent(this, OrderActivity.class));
			}
			finish();
			break;
		default:
			break;
		}
	}

}
