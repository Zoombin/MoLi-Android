package com.imooly.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.db.DB_Location;
import com.imooly.android.db.DB_User;
import com.imooly.android.entity.RspSetPass;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.utils.Utils;
import com.imooly.android.widget.CustomProgressDialog;
import com.imooly.android.widget.Toast;

/**
 * 注册确认画面
 * 
 * @author daiye
 * 
 */
public class RegisterConfirmActivity extends BaseActivity implements OnClickListener {

	private ImageView iv_back;
	private TextView tv_phone;
	private EditText et_pwd;
	private EditText et_pwdconfirm;
	private Button btn_confirm;
	private CheckBox chk_agree;
	private TextView tv_agreement;

	private String phone;
	private String code;
	
	CustomProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register_confirm);
		
		logActivityName(this);
		
		getIntentData();
		initView();
		initData();
	}

	private void getIntentData() {
		Intent intent = getIntent();
		phone = intent.getStringExtra("phone");
		code = intent.getStringExtra("code");
	}

	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);

		tv_phone = (TextView) findViewById(R.id.tv_phone);

		et_pwd = (EditText) findViewById(R.id.et_pwd);

		et_pwdconfirm = (EditText) findViewById(R.id.et_pwdconfirm);

		btn_confirm = (Button) findViewById(R.id.btn_confirm);
		btn_confirm.setOnClickListener(this);

		chk_agree = (CheckBox) findViewById(R.id.chk_agree);

		tv_agreement = (TextView) findViewById(R.id.tv_agreement);
		tv_agreement.setOnClickListener(this);
	}

	private void initData() {
		tv_phone.setText(phone);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.btn_confirm:
			final String pass = et_pwd.getText().toString();
			String cpass = et_pwdconfirm.getText().toString();
			if (!chk_agree.isChecked()) {
				Toast.show(self, "请同意用户协议");
				return;
			}
			if (TextUtils.isEmpty(pass)) {
				Toast.show(self, "请输入密码");
				return;
			}
			if (TextUtils.isEmpty(cpass)) {
				Toast.show(self, "请再次输入密码");
				return;
			}
			if (!pass.equals(cpass)) {
				Toast.show(self, "两次输入密码不一致");
				return;
			}
			
			if (dialog == null) {
				dialog = CustomProgressDialog.createDialog(self);
				dialog.setMessage("正在注册");
				dialog.setCanceledOnTouchOutside(false);
				dialog.show();
			}
			
			String ipAddress = Utils.getLocalIpAddress();
			
        	DB_Location db_location = new DB_Location(self);
        	String nlatitude = db_location.getLatitude();
        	String nlontitude = db_location.getLontitude();
	        
			Api.registSetPass(self, phone, code, pass, cpass, ipAddress, nlatitude, nlontitude, new NetCallBack<ServiceResult>() {
				@Override
				public void success(ServiceResult rspData) {
					if (dialog != null && dialog.isShowing()) {
						dialog.dismiss();
						dialog = null;
					}
					
					if(rspData == null){
						Toast.show(self, "密码设置失败");
						return;
					}
					RspSetPass rsp = (RspSetPass) rspData;
					if(rsp.data != null){
						DB_User dUser = new DB_User(self);
						dUser.setUserName(phone);
						dUser.setPassWord(pass);
						
						Toast.show(self, "注册成功，请登录");
						finish();
					}else{
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
			}, RspSetPass.class);
			break;
		case R.id.tv_agreement:
			startActivity(new Intent(self, AgreementActivity.class));
			break;
		default:
			break;
		}
	}
}
