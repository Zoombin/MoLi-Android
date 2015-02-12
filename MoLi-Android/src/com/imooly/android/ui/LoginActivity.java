package com.imooly.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.imooly.android.MoLiApplication;
import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.db.DB_Location;
import com.imooly.android.db.DB_User;
import com.imooly.android.entity.RspLogin;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.tool.RegexUtil;
import com.imooly.android.tool.TicketUtil;
import com.imooly.android.utils.Utils;
import com.imooly.android.widget.CustomProgressDialog;
import com.imooly.android.widget.Toast;

/**
 * 登陆画面
 * 
 * @author daiye
 * 
 */
public class LoginActivity extends BaseActivity implements OnClickListener {

	private ImageView iv_back;
	private EditText et_user;
	private EditText et_pwd;
	private Button btn_login;
	private TextView tv_register;
	private TextView tv_findpwd;
	
	CustomProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		logActivityName(this);
		
		initView();
		initData();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		DB_User dbUser = new DB_User(self);
		String userName = dbUser.getUserName();
		String pass = dbUser.getPassWord();
		
		et_user.setText(userName);
		Utils.setEditTextHint(et_user);
		et_pwd.setText(pass);
		Utils.setEditTextHint(et_pwd);
		
		if (TextUtils.isEmpty(pass)) {
			et_pwd.setSelection(pass.length());
		}
	}

	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);

		et_user = (EditText) findViewById(R.id.et_user);

		et_pwd = (EditText) findViewById(R.id.et_pwd);
		
		// login button
		btn_login = (Button) findViewById(R.id.btn_login);
		btn_login.setOnClickListener(this);
		
		// qucikly register
		tv_register = (TextView) findViewById(R.id.tv_register);
		tv_register.setOnClickListener(this);
		tv_register.requestFocus();
		
		// find password
		tv_findpwd = (TextView) findViewById(R.id.tv_findpwd);
		tv_findpwd.setOnClickListener(this);
	}

	private void initData() {
	}
	
	private void login(){
		final String phone = et_user.getText().toString();
		final String pass = et_pwd.getText().toString();
		if(TextUtils.isEmpty(phone)){
			Toast.show(self, "请输入手机号");
			return;
		}
		if(!RegexUtil.isMobileNumber(phone)){
			Toast.show(self, "请输入正确的手机号");
			return;
		}
		if(TextUtils.isEmpty(pass)){
			Toast.show(self, "请输入密码");
			return;
		}
		
		
		if (dialog == null) {
			dialog = CustomProgressDialog.createDialog(self);
			dialog.setMessage("正在登陆");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}
		
		String ipAddress = Utils.getLocalIpAddress();

		// 获得经纬度
		DB_Location db_Location = new DB_Location(self);
        String nlatitude = db_Location.getLatitude();
        String nlontitude = db_Location.getLontitude();
        
		Api.login(self, phone, pass, "", ipAddress, nlatitude, nlontitude, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
					dialog = null;
				}
				
				RspLogin rsp = (RspLogin) rspData;
				if(rsp == null){
					Toast.show(self, "登陆失败");
					return;
				}
				
				if(rsp.error != 0){
					Toast.show(self, rsp.getMsg());
					return;
				}
				
				RspLogin.Data loginData = rsp.data;
				DB_User db_User = new DB_User(self);
				db_User.setUserName(phone);
				//db_User.setPassWord(pass);
				db_User.setUserid(rsp.data.getUserid());
				db_User.setSigntoken(rsp.data.getSigntoken());
				db_User.setLoginData(loginData);
				db_User.setVip(1 == rsp.data.getVipflag() ? true : false);
				
				
				TicketUtil.instance().setSessionid((rsp.data.getSessionid()));
				
				// 设置全局变量isLogin
				MoLiApplication.getInstance().setLogin(true);
				
				finish();
			}
			
			@Override
			public void failed(String msg) {
				if (dialog != null && dialog.isShowing()) {
					dialog.dismiss();
					dialog = null;
				}
				Toast.show(self, msg);
			}
		}, RspLogin.class);
				
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.btn_login:
			login();
			break;
		case R.id.tv_register:
			intent = new Intent(self, RegisterActivity.class);
			startActivity(intent);
			break;
		case R.id.tv_findpwd:
			intent = new Intent(self, FindPwdActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
}
