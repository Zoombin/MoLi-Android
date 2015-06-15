package com.imooly.android.ui;

import java.io.UnsupportedEncodingException;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.db.DB_User;
import com.imooly.android.entity.RspLogin;
import com.imooly.android.entity.RspSuccessCommon;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.widget.CustomProgressDialog;
import com.imooly.android.widget.Toast;

/**
 * 修改昵称画面
 * 
 * @author
 * 
 */
public class NicknameEditActivity extends BaseActivity implements OnClickListener {

	private RelativeLayout rl_title;
	private Button  button_nickname_edit;
	private TextView tv_title;
	private EditText edit_name;

	public static RspLogin.Data loginData;// 临时这么处理

	CustomProgressDialog dialog;

	private String mNickName = null;
	private int nickNamelength = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_nickname_edit);
		
		logActivityName(this);

		mNickName = getIntent().getStringExtra("nick_name");

		if (mNickName != null) {
			nickNamelength = mNickName.length();
		} else {
			nickNamelength = 0;
		}

		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initView() {

		// title
		rl_title = (RelativeLayout) findViewById(R.id.rl_title);

		findViewById(R.id.button_back).setOnClickListener(this);

		tv_title = (TextView) findViewById(R.id.tv_title);

		edit_name = (EditText) findViewById(R.id.edit_name);
		edit_name.setText(mNickName);
		edit_name.setSelection(nickNamelength); // 设置光标位置

		button_nickname_edit = (Button) findViewById(R.id.button_nickname_edit);
		button_nickname_edit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.button_back:
			finish();
			break;
		case R.id.button_nickname_edit:
			final String nickName = edit_name.getText().toString();
			if(nickName.length()>16){
				Toast.show(self, "昵称长度不能超过16位");
				return;
			}
			try {
				mNickName = new String(nickName.getBytes(), "UTF-8");

				Api.updateNickname(self, mNickName, new NetCallBack<ServiceResult>() {

					@Override
					public void success(ServiceResult rspData) {
						RspSuccessCommon rsp = (RspSuccessCommon) rspData;
						int successCode = rsp.data.getSuccess();

						if (successCode == 1) {
							Toast.show(self, "更新昵称成功！");

							// 保存在本地
							DB_User user = new DB_User(self);
							RspLogin.Data data = user.getLoginData();
							data.setNickname(nickName);
							user.setLoginData(data);
							self.finish();
							
						} else {
							Toast.show(self, "更新昵称失败！");
						}
					}

					@Override
					public void failed(String msg) {
						Toast.show(self, msg);
					}
				}, RspSuccessCommon.class);

			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			break;
		default:
			break;
		}
	}
}
