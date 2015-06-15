package com.imooly.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.entity.RspFlagCommon;
import com.imooly.android.entity.ServiceResult;

public class TradePwdActivity extends BaseActivity implements OnClickListener {

	private RelativeLayout rl_title;

	private RelativeLayout rl_trade_setting, rl_trade_modify, rl_trade_find;

	private ImageView iv_back;
	private TextView tv_title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trade_password);

		logActivityName(this);

		initView();

		checkIsSettedTraderPwd();
	}

	private void initView() {

		// title
		rl_title = (RelativeLayout) findViewById(R.id.rl_title);

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);

		tv_title = (TextView) findViewById(R.id.tv_title);

		// 设置交易密码
		rl_trade_setting = (RelativeLayout) findViewById(R.id.rl_trade_setting);
		rl_trade_setting.setOnClickListener(this);

		// 修改交易密码
		rl_trade_modify = (RelativeLayout) findViewById(R.id.rl_trade_modify);
		rl_trade_modify.setOnClickListener(this);

		// 找回交易密码
		rl_trade_find = (RelativeLayout) findViewById(R.id.rl_trade_find);
		rl_trade_find.setOnClickListener(this);

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
		case R.id.rl_trade_setting:

			TradePwdActivity.this.startActivity(new Intent(TradePwdActivity.this, TradePwdSettingActivity.class));
			this.finish();
			break;

		case R.id.rl_trade_modify:
			Intent intent2 = new Intent(TradePwdActivity.this, TradePwdModifyActivity.class);
			TradePwdActivity.this.startActivity(intent2);
			break;

		case R.id.rl_trade_find:

			Intent intent3 = new Intent(TradePwdActivity.this, TradePwdFindActivity.class);
			TradePwdActivity.this.startActivity(intent3);
			break;

		default:
			break;

		}

	}

	private void checkIsSettedTraderPwd() {

		Api.checkIsSettedTraderPassword(self, new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {
				RspFlagCommon rsp = (RspFlagCommon) rspData;
				RspFlagCommon.Data data = rsp.data;
				if (data.getFlag() == 1) {

					rl_trade_setting.setVisibility(View.GONE);
					rl_trade_modify.setVisibility(View.VISIBLE);
					rl_trade_find.setVisibility(View.VISIBLE);
				} else {
					rl_trade_setting.setVisibility(View.VISIBLE);
					rl_trade_modify.setVisibility(View.GONE);
					rl_trade_find.setVisibility(View.GONE);

				}
			}

			@Override
			public void failed(String msg) {
				rl_trade_setting.setVisibility(View.VISIBLE);
				rl_trade_modify.setVisibility(View.VISIBLE);
				rl_trade_find.setVisibility(View.VISIBLE);
			}
		}, RspFlagCommon.class);
	}
}
