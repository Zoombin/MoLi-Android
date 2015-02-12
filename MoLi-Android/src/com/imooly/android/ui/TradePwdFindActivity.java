package com.imooly.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
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
import com.imooly.android.tool.RegexUtil;
import com.imooly.android.widget.CountDownButton;
import com.imooly.android.widget.CustomProgressDialog;
import com.imooly.android.widget.Toast;

/**
 * 找回交易密码
 * 
 * @author
 * 
 */
public class TradePwdFindActivity extends BaseActivity implements OnClickListener {

	private RelativeLayout ll_title;
	private ImageView iv_back;
	private TextView tv_title;
	private EditText et_user;
	private EditText et_verifycode;
	private Button btn_verifycode;
	private Button btn_next;
	private CountDownButton countdownbutton;

	CustomProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trade_pwd_find);

		logActivityName(this);

		initView();
		initData();
	}

	private void initView() {
		ll_title = (RelativeLayout) findViewById(R.id.ll_title);

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);

		tv_title = (TextView) findViewById(R.id.tv_title);

		et_user = (EditText) findViewById(R.id.et_user);

		et_verifycode = (EditText) findViewById(R.id.et_verifycode);

		btn_verifycode = (Button) findViewById(R.id.btn_verifycode);
		btn_verifycode.setOnClickListener(this);

		btn_next = (Button) findViewById(R.id.btn_next);
		btn_next.setOnClickListener(this);

	}

	private void initData() {
		countdownbutton = new CountDownButton(self, btn_verifycode);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		countdownbutton.cancel();
	}

	private void getVcode() {
		String phone = et_user.getText().toString();
		if (TextUtils.isEmpty(phone)) {
			Toast.show(self, "请输入手机号");
			return;
		}
		if (!RegexUtil.isMobileNumber(phone)) {
			Toast.show(self, "请输入正确的手机号");
			return;
		}
		countdownbutton.start();

		Api.getVerCode("forgotwalletpwd", phone, self, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				// TODO Auto-generated method stub
				if (rspData == null) {
					Toast.show(self, "获取验证码失败");
					countdownbutton.cancel();
					return;
				}
				RspSuccessCommon rsp = (RspSuccessCommon) rspData;
				if (rsp.data != null && rsp.data.success == 1) {
					btn_next.setBackgroundResource(R.drawable.selector_btn_orange);
					Toast.show(self, "您将2分钟内收到验证码");
				} else {
					countdownbutton.cancel();
					String errMsg = rsp.msg;
					if (TextUtils.isEmpty(errMsg))
						Toast.show(self, "获取验证码失败");
					else
						Toast.show(self, errMsg);
				}
			}

			@Override
			public void failed(String msg) {
				// TODO Auto-generated method stub
				countdownbutton.cancel();
				if (TextUtils.isEmpty(msg))
					Toast.show(self, "获取验证码失败");
				else
					Toast.show(self, msg);
			}
		}, RspSuccessCommon.class);
	}

	private void nextStep() {
		final String phone = et_user.getText().toString();
		final String vcode = et_verifycode.getText().toString();

		if (TextUtils.isEmpty(phone)) {
			Toast.show(self, "请输入手机号");
			return;
		}
		if (!RegexUtil.isMobileNumber(phone)) {
			Toast.show(self, "请输入正确的手机号");
			return;
		}
		if (TextUtils.isEmpty(vcode)) {
			Toast.show(self, "请输入验证码");
			return;
		}

		if (dialog == null) {
			dialog = CustomProgressDialog.createDialog(self);
			dialog.setMessage("正在提交手机号");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		Api.checkTradePhoneAndVcode(self, phone, vcode, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				// TODO Auto-generated method stub
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
					dialog = null;
				}

				if (rspData == null) {
					Toast.show(self, "提交手机号失败");
					return;
				}
				
				RspSuccessCommon rsp = (RspSuccessCommon) rspData;
				if (rsp.data != null && rsp.data.success == 1) {
					Intent intent = new Intent(self, TradePwdFind2Activity.class);
					intent.putExtra("phone", phone);
					intent.putExtra("code", vcode);
					startActivity(intent);
					finish();
				} else {
					String errMsg = rsp.msg;
					if (TextUtils.isEmpty(errMsg))
						Toast.show(self, "提交手机号失败");
					else
						Toast.show(self, errMsg);
				}
			}

			@Override
			public void failed(String msg) {
				// TODO Auto-generated method stub
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
					dialog = null;
				}
				Toast.show(self, msg);
			}
		}, RspSuccessCommon.class);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.btn_verifycode:
			getVcode();
			break;
		case R.id.btn_next:
			nextStep();
			break;
		default:
			break;
		}
	}
}
