package com.imooly.android.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.entity.RspSuccessCommon;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.widget.CustomProgressDialog;
import com.imooly.android.widget.Toast;

/**
 * 设置交易密码
 * 
 * @author
 * 
 */
public class TradePwdSettingActivity extends BaseActivity implements OnClickListener {

	private RelativeLayout ll_title;
	private ImageView iv_back;
	private TextView tv_title;
	private EditText edit_setting, edit_sure;

	private Button btn_verifycode;
	private Button btn_sure;

	CustomProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trade_pwd_settings);

		logActivityName(this);

		initView();
		initData();
	}

	private void initView() {
		ll_title = (RelativeLayout) findViewById(R.id.ll_title);

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);

		tv_title = (TextView) findViewById(R.id.tv_title);

		edit_setting = (EditText) findViewById(R.id.et_1);

		edit_sure = (EditText) findViewById(R.id.et_2);

		// 确认按钮
		btn_sure = (Button) findViewById(R.id.btn_sure);
		btn_sure.setOnClickListener(this);
	}

	private void initData() {

	}

	private void setTradPwd() {
		
		if (TextUtils.isEmpty(edit_setting.getText().toString())) {
			Toast.show(self, "请输入交易密码");
			return;
		}

		if (TextUtils.isEmpty(edit_sure.getText().toString())) {
			Toast.show(self, "请再次输入交易密码");
			return;
		}
		
		Api.setTradePwd(self, edit_setting.getText().toString(), edit_sure.getText().toString(), new NetCallBack<ServiceResult>() {
			
			@Override
			public void success(ServiceResult rspData) {
				
				RspSuccessCommon rsp = (RspSuccessCommon)rspData;
				RspSuccessCommon.Data data = rsp.data;
				
				if (data.getSuccess() == 1) {
					Toast.show(self, "设置成功");
					self.finish();
				} else {
					Toast.show(self, "设置失败");
				}
			}
			
			@Override
			public void failed(String msg) {
				Toast.show(self, msg);
				
			}
		}, RspSuccessCommon.class);
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.btn_sure:
			setTradPwd();
			break;

		default:
			break;
		}
	}
}
