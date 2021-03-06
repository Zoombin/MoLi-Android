package com.imooly.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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
import com.imooly.android.utils.Utils;
import com.imooly.android.widget.CountDownButton;
import com.imooly.android.widget.CustomProgressDialog;
import com.imooly.android.widget.Toast;

/**
 * 找回密码
 * 
 * @author daiye
 * 
 */
public class FindPwdActivity extends BaseActivity implements OnClickListener {

	private ImageView iv_back;
	private EditText et_user;
	private EditText et_verifycode;
	private Button btn_verifycode;
	private Button btn_next;
	private CountDownButton countdownbutton;
	CustomProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_findpwd);
		
		logActivityName(this);
		
		initView();
		initData();
	}

	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);

		et_user = (EditText) findViewById(R.id.et_user);
		Utils.setEditTextHint(et_user);
		et_verifycode = (EditText) findViewById(R.id.et_verifycode);
		Utils.setEditTextHint(et_verifycode);
		et_user.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int count, int after) {
				if (TextUtils.isEmpty(s) || TextUtils.isEmpty(et_verifycode.getText())) {
					btn_next.setBackgroundColor(getResources().getColor(R.color.btn_verifycode_pressed));
				} else {
					btn_next.setBackgroundResource(R.drawable.selector_btn_orange);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				
			}
		});
		et_verifycode.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int count, int after) {
				if (TextUtils.isEmpty(s) || TextUtils.isEmpty(et_user.getText())) {
					btn_next.setBackgroundColor(getResources().getColor(R.color.btn_verifycode_pressed));
				} else {
					btn_next.setBackgroundResource(R.drawable.selector_btn_orange);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		btn_verifycode = (Button) findViewById(R.id.btn_verifycode);
		btn_verifycode.setOnClickListener(this);

		btn_next = (Button) findViewById(R.id.btn_next);
		btn_next.setOnClickListener(this);
	}

	private void initData() {
		countdownbutton = new CountDownButton(self, btn_verifycode);
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
		
		Api.getVerCode("forgot", phone, self, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				if (rspData == null) {
					Toast.show(self, "获取验证码失败");
					countdownbutton.cancel();
					return;
				}
				RspSuccessCommon rsp = (RspSuccessCommon) rspData;
				if (rsp.data != null && rsp.data.success == 1) {
					countdownbutton.start();
					Toast.show(self, "您将2分钟内收到验证码");
				} else {
					countdownbutton.cancel();
					Toast.show(self, rsp.msg);
				}
			}

			@Override
			public void failed(String msg) {
				countdownbutton.cancel();
				Toast.show(self, msg);
			}
		}, RspSuccessCommon.class);
	}

	private void checkVerifyPhone() {
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
			dialog.setMessage("正在验证");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		Api.forgotPassCkPhone(self, phone, vcode, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
					dialog = null;
				}

				if (rspData == null) {
					Toast.show(self, "验证失败");
					return;
				}
				RspSuccessCommon rsp = (RspSuccessCommon) rspData;
				if (rsp.data != null && rsp.data.success == 1) {
					Intent intent = new Intent(self, ChangePwdActivity.class);
					intent.putExtra("phone", phone);
					intent.putExtra("code", vcode);
					startActivity(intent);
					finish();
				} else {
					Toast.show(self, rsp.msg);
				}
			}

			@Override
			public void failed(String msg) {
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
					dialog = null;
				}

				Toast.show(self, msg);
			}
		}, RspSuccessCommon.class);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		countdownbutton.cancel();
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
			checkVerifyPhone();
			break;
		default:
			break;
		}
	}
}
