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
import com.imooly.android.base.BaseActivity;
import com.imooly.android.widget.CustomProgressDialog;
import com.imooly.android.widget.Toast;

/**
 * 初始化密码修改界面
 * 
 * @author
 * 
 */
public class PasswordInitActivity extends BaseActivity implements OnClickListener {

	private RelativeLayout ll_title;
	private ImageView iv_back;
	private TextView tv_title;
	private EditText edit_init_pwd, edit_new_pwd, edit_confirm_pwd;

	private Button button_sure;
	private RelativeLayout ll_bottom;

	private TextView text_enter;

	CustomProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_password_init);

		logActivityName(this);

		initView();
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();

	}

	private void initView() {
		ll_title = (RelativeLayout) findViewById(R.id.ll_title);

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);

		tv_title = (TextView) findViewById(R.id.tv_title);

		edit_init_pwd = (EditText) findViewById(R.id.edit_init_pwd);

		edit_new_pwd = (EditText) findViewById(R.id.edit_new_pwd);

		edit_confirm_pwd = (EditText) findViewById(R.id.edit_confirm_pwd);

		text_enter = (TextView) findViewById(R.id.text_enter);

		// login button
		button_sure = (Button) findViewById(R.id.button_sure);
		button_sure.setOnClickListener(this);

	}

	private void initData() {
	}

	private void commitNewPassword() {
		String initPwd = edit_init_pwd.getText().toString();
		String newPwd = edit_new_pwd.getText().toString();
		String confirmPwd = edit_confirm_pwd.getText().toString();
		if (TextUtils.isEmpty(initPwd)) {
			Toast.show(self, "请输入初始密码");
			return;
		}
		if (TextUtils.isEmpty(newPwd)) {
			Toast.show(self, "请输入新密码");
			return;
		}
		if (TextUtils.isEmpty(confirmPwd)) {
			Toast.show(self, "请再次输入新密码");
			return;
		}

		if (dialog == null) {
			dialog = CustomProgressDialog.createDialog(self);
			dialog.setMessage("正在登陆");
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
		}

		/*
		 * Api.login(self, phone, pass, "", ipAddress, nlatitude, nlontitude,
		 * new NetCallBack<ServiceResult>() {
		 * 
		 * @Override public void success(ServiceResult rspData) { // TODO
		 * Auto-generated method stub if (dialog != null && dialog.isShowing())
		 * { dialog.dismiss(); dialog = null; }
		 * 
		 * RspLogin rsp = (RspLogin) rspData; if(rsp == null){
		 * Toast.makeText(self, "登陆失败", Toast.LENGTH_SHORT).show(); return; }
		 * 
		 * if(rsp.error != 0){ Toast.makeText(self, rsp.getMsg(),
		 * Toast.LENGTH_SHORT).show(); return; }
		 * 
		 * Toast.makeText(self, "登陆成功", Toast.LENGTH_SHORT).show();
		 * RspLogin.Data loginData = rsp.data; DB_User db_User = new
		 * DB_User(self); db_User.setUserName(phone); db_User.setPassWord(pass);
		 * db_User.setUserid(rsp.data.getUserid());
		 * db_User.setSigntoken(rsp.data.getSigntoken());
		 * db_User.setLoginData(loginData);
		 * 
		 * TicketUtil.instance().setSessionid((rsp.data.getSessionid()));
		 * 
		 * // 设置全局变量isLogin MoLiApplication.getInstance().setLogin(true);
		 * 
		 * finish(); }
		 * 
		 * @Override public void failed(String msg) { // TODO Auto-generated
		 * method stub if (dialog != null && dialog.isShowing()) {
		 * dialog.dismiss(); dialog = null; } Toast.makeText(self, msg,
		 * Toast.LENGTH_SHORT).show(); } }, RspLogin.class);
		 */

	}

	@Override
	public void onClick(View v) {
		Intent intent;

		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.text_enter:
			
			// 已经登录
			intent = new Intent(self, FindPwdActivity.class);
			startActivity(intent);

			break;
		case R.id.button_sure:
			commitNewPassword();
			break;
		default:
			break;
		}
	}
}
