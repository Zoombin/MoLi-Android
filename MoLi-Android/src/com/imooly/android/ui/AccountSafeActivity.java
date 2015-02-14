package com.imooly.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.base.BaseActivity;

public class AccountSafeActivity extends BaseActivity implements OnClickListener {

	private RelativeLayout rl_title;

	private RelativeLayout rl_address;
	private RelativeLayout rl_modify_pwd;

	private RelativeLayout rl_transaction_pwd;

	private ImageView iv_accountsafe_back;
	private TextView tv_title;

	private TextView tv_address;
	private TextView tv_modify_pwd;
	private TextView tv_pay_pwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_account_safe);
		logActivityName(this);
		initView();
	}

	private void initView() {

		// title
		rl_title = (RelativeLayout) findViewById(R.id.rl_title);

		iv_accountsafe_back = (ImageView) findViewById(R.id.iv_accountsafe_back);
		iv_accountsafe_back.setOnClickListener(this);

		tv_title = (TextView) findViewById(R.id.tv_title);

		// address
		rl_address = (RelativeLayout) findViewById(R.id.rl_address);
		tv_address = (TextView) findViewById(R.id.text_address);
		rl_address.setOnClickListener(this);

		// modify password
		rl_modify_pwd = (RelativeLayout) findViewById(R.id.rl_modify_pwd);
		tv_modify_pwd = (TextView) findViewById(R.id.text_modify_pwd);
		rl_modify_pwd.setOnClickListener(this);
		// pay password
		rl_transaction_pwd = (RelativeLayout) findViewById(R.id.rl_transaction_pwd);
		tv_pay_pwd = (TextView) findViewById(R.id.text_pay_pwd);
		rl_transaction_pwd.setOnClickListener(this);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.rl_address:
			AccountSafeActivity.this.startActivity(new Intent(AccountSafeActivity.this, AddressActivity.class));
			break;

		case R.id.rl_modify_pwd:
			Intent intent2 = new Intent(AccountSafeActivity.this, PasswordModifyActivity.class);
			AccountSafeActivity.this.startActivity(intent2);
			break;

		case R.id.rl_transaction_pwd:
			Intent intent3 = new Intent(AccountSafeActivity.this, TradePwdActivity.class);
			AccountSafeActivity.this.startActivity(intent3);
			break;
			
		case R.id.iv_accountsafe_back:
			finish();

		default:
			break;

		}

	}

}
