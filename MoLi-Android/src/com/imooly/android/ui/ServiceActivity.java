package com.imooly.android.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.entity.RspTelNumber;
import com.imooly.android.entity.ServiceResult;

public class ServiceActivity extends BaseActivity implements OnClickListener {

	private RelativeLayout rl_title;

	private RelativeLayout rl_service;
	private RelativeLayout rl_call;

	private ImageView iv_back;
	private TextView tv_title;

	private TextView tv_service;
	private TextView tv_call;
	private TextView tv_phone_number;
	private String telNumber;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_service);

		logActivityName(this);

		initView();

		initData();
	}

	private void initData() {
		Api.getServiceTelNumber(self, new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {
				RspTelNumber rsp = (RspTelNumber) rspData;
				RspTelNumber.Data data = rsp.data;
				telNumber = data.getTelephone();

			}

			@Override
			public void failed(String msg) {

			}
		}, RspTelNumber.class);

	}

	private void initView() {

		// title
		rl_title = (RelativeLayout) findViewById(R.id.rl_title);

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);

		tv_title = (TextView) findViewById(R.id.tv_title);

		// service
		rl_service = (RelativeLayout) findViewById(R.id.rl_service);

		tv_service = (TextView) findViewById(R.id.text_service);

		// call
		rl_call = (RelativeLayout) findViewById(R.id.rl_call);
		tv_call = (TextView) findViewById(R.id.text_service);
		tv_phone_number = (TextView) findViewById(R.id.text_phone_number);

		tv_phone_number = (TextView) findViewById(R.id.text_phone_number);
		rl_service.setOnClickListener(this);
		rl_call.setOnClickListener(this);

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
		case R.id.rl_service:

			ServiceActivity.this.startActivity(new Intent(ServiceActivity.this, OrderServiceStatusActivity.class));

			break;

		case R.id.rl_call:
			
			if (telNumber != null & telNumber != "") {				
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + telNumber));
				startActivity(intent);			
			} else {
				Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "4008280220"));
				startActivity(intent);
			}

			break;

		default:
			break;

		}

	}

}
