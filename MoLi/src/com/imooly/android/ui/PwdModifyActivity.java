package com.imooly.android.ui;

import android.content.Intent;
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
import com.imooly.android.tool.RegexUtil;
import com.imooly.android.widget.CustomProgressDialog;
import com.imooly.android.widget.Toast;

/**
 * 修改密码界面
 * 
 * @author
 * 
 */
public class PwdModifyActivity extends BaseActivity implements OnClickListener {

	private RelativeLayout ll_title;
	private ImageView iv_back;
	private TextView tv_title;
	private EditText et_old_pwd;
	private EditText et_new_pwd;
	private EditText et_confirm_pwd;
	private Button btn_save;
	private RelativeLayout ll_bottom;

	
	CustomProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pwd_modify);
		
		logActivityName(this);
		
		initView();
		initData();
	}
	
	@Override
	protected void onResume() {
		super.onResume();

	}

	private void initView() {
		// title
		ll_title = (RelativeLayout) findViewById(R.id.ll_title);

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);

		tv_title = (TextView) findViewById(R.id.tv_title);

		// 旧密码
		et_old_pwd = (EditText) findViewById(R.id.et_old_pwd);
		// 新密码
		et_new_pwd = (EditText) findViewById(R.id.et_new_pwd);
		// 重复新密码
		et_confirm_pwd = (EditText) findViewById(R.id.et_confirm_pwd);
		
		// 保存按钮
		btn_save = (Button) findViewById(R.id.btn_save);
		btn_save.setOnClickListener(this);

	}

	private void initData() {
		
	}
	
	private void modifyPassword(){
		String oldPwd = et_old_pwd.getText().toString();
		String newPwd = et_new_pwd.getText().toString();
		String confirmPwd = et_confirm_pwd.getText().toString();
		
		if(TextUtils.isEmpty(oldPwd)){
			Toast.show(self, "请输入旧密码");
			return;
		}
		if(!RegexUtil.isMobileNumber(newPwd)){
			Toast.show(self, "请输入新密码");
			return;
		}
		if(TextUtils.isEmpty(confirmPwd)){
			Toast.show(self, "请重复新密码");
			return;
		}
		
		Api.updateUserPwd(self, oldPwd, newPwd, confirmPwd, new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {
				
				RspSuccessCommon rsp = (RspSuccessCommon) rspData;
				RspSuccessCommon.Data data = rsp.data;
				if(data.getSuccess() == 1) {
					Toast.show(self, "修改密码成功！");
				}else {
					Toast.show(self, data.getMessage());
				}
			}

			@Override
			public void failed(String msg) {
				
				Toast.show(self, msg);
			}
		}, RspSuccessCommon.class);
				
	}

	@Override
	public void onClick(View v) {
		Intent intent;
		
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.btn_save:
			modifyPassword();
			break;
		default:
			break;
		}
	}
}
