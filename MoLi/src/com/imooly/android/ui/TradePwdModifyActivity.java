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
public class TradePwdModifyActivity extends BaseActivity implements OnClickListener {

	private RelativeLayout ll_title;
	private ImageView iv_back;
	private TextView tv_title;
	private EditText edit_old_pwd, edit_new_pwd, edit_twice;

	private Button btn_verifycode;
	private Button btn_save;

	CustomProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trade_pwd_modify);

		logActivityName(this);

		initView();
		initData();
	}

	private void initView() {
		ll_title = (RelativeLayout) findViewById(R.id.ll_title);

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);

		tv_title = (TextView) findViewById(R.id.tv_title);

		edit_old_pwd = (EditText) findViewById(R.id.et_1);

		edit_new_pwd = (EditText) findViewById(R.id.et_2);

		edit_twice = (EditText) findViewById(R.id.et_3);

		// 确认按钮
		btn_save = (Button) findViewById(R.id.btn_save);
		btn_save.setOnClickListener(this);
	}

	private void initData() {

	}

	private void modifyTradePwd() {
		String oldPwd = edit_old_pwd.getText().toString();
		if (TextUtils.isEmpty(oldPwd)) {
			Toast.show(self, "请输入旧交易密码");
			return;
		}
		String newPwd = edit_new_pwd.getText().toString();

		if (TextUtils.isEmpty(newPwd)) {
			Toast.show(self, "请输入新交易密码");
			return;
		}

		String ConfirmPwd = edit_twice.getText().toString();

		if (TextUtils.isEmpty(ConfirmPwd)) {
			Toast.show(self, "请再次输入新交易密码");
			return;
		}

		Api.modifyTradePwd(self, oldPwd, newPwd, ConfirmPwd, new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {

				RspSuccessCommon rsp = (RspSuccessCommon) rspData;
				RspSuccessCommon.Data data = rsp.data;

				if (data.getSuccess() == 1) {
					Toast.show(self, "修改成功");
					finish();
				} else {
					Toast.show(self, "修改失败");
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
			
		case R.id.btn_save:

			modifyTradePwd();

			break;

		default:
			break;
		}
	}
}
