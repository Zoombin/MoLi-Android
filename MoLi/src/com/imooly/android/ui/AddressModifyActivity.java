package com.imooly.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.entity.RspAddressInfo;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.view.AddressSelectDialog;
import com.imooly.android.view.AddressSelectDialog.AddressFilterCallBack;
import com.imooly.android.widget.CustomProgressDialog;
import com.imooly.android.widget.Toast;

/**
 * 
 * @author
 * 
 */
public class AddressModifyActivity extends BaseActivity implements OnClickListener {

	private RelativeLayout ll_title;
	private ImageView iv_back;
	private TextView tv_title;

	private Button button_save;

	private EditText edit_name, edit_phone_number, edit_postcode, edit_address, edit_detail_address;

	private CheckBox check_default_address;
	
	private TextView text_set_default;

	private ListView list_address;
	CustomProgressDialog dialog;

	private int mPid, mCid, mAid;
	private int isdefault = 0;

	private String message;
	private String addressid;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address_modify);

		logActivityName(this);

		initView();
		initData();

		Intent intent = getIntent();
		
		addressid = intent.getStringExtra("addressid");
		edit_name.setText(intent.getStringExtra("name"));
		edit_phone_number.setText(intent.getStringExtra("mobile"));
		edit_postcode.setText(intent.getStringExtra("postcode"));
		edit_address.setText(intent.getStringExtra("address"));

		mPid = intent.getIntExtra("pid", 0);
		mCid = intent.getIntExtra("cid", 0);
		mAid = intent.getIntExtra("aid", 0);

		edit_detail_address.setText(intent.getStringExtra("detailAddress"));
		isdefault = intent.getIntExtra("isDefault", 0);
		check_default_address.setChecked(isdefault == 0 ? false : true);
		edit_name.setText(intent.getStringExtra("name"));
		
		// 是默认收货地址，不能设置为非默认收货地址
		if (isdefault ==1 ) {
			text_set_default.setVisibility(View.GONE);
			check_default_address.setVisibility(View.GONE);
		}

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

		list_address = (ListView) findViewById(R.id.list_address);

		// 保存修改
		button_save = (Button) findViewById(R.id.button_save);
		button_save.setOnClickListener(this);

		edit_name = (EditText) findViewById(R.id.edit_name);

		edit_phone_number = (EditText) findViewById(R.id.edit_phone_number);

		edit_postcode = (EditText) findViewById(R.id.edit_postcode);

		edit_address = (EditText) findViewById(R.id.edit_address);
		edit_address.setOnClickListener(this);

		edit_detail_address = (EditText) findViewById(R.id.edit_detail_address);

		check_default_address = (CheckBox) findViewById(R.id.check_default_address);
		
		text_set_default = (TextView) findViewById(R.id.text_set_default);
		
		
		
		check_default_address.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked == true) {
					isdefault = 1;
				} else {
					isdefault = 0;
				}

			}
		});
		
	}

	private void initData() {
		
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.button_save:

			String name = edit_name.getText().toString();
			String mobile = edit_phone_number.getText().toString();
			String code = edit_postcode.getText().toString();
			String street = edit_detail_address.getText().toString();

			// 新增
			if (TextUtils.isEmpty(addressid)) {
				Api.addAddress(self, mPid, mCid, mAid, street, code, name, "", mobile, isdefault, new NetCallBack<ServiceResult>() {
	
					@Override
					public void success(ServiceResult rspData) {
						Toast.show(self, "上传地址成功！");
						setResult(RESULT_OK);
						finish();
					}
	
					@Override
					public void failed(String msg) {
						Toast.show(self, msg);
					}
				}, RspAddressInfo.class);
			} 
			// 修改
			else {
				Api.updateaddress(self, addressid, mPid, mCid, mAid, street, code, name, "", mobile, isdefault, new NetCallBack<ServiceResult>() {
					
					@Override
					public void success(ServiceResult rspData) {
						Toast.show(self, "上传地址成功！");
						setResult(RESULT_OK);
						finish();
					}
	
					@Override
					public void failed(String msg) {
						Toast.show(self, msg);
					}
				}, RspAddressInfo.class);
			}

			break;
		case R.id.edit_address:
			
			new AddressSelectDialog().show(self, new AddressFilterCallBack() {

				@Override
				public void onSelectPid(int pid) {
					mPid = pid;
				}

				@Override
				public void onSelectCid(int cid) {
					mCid = cid;
				}

				@Override
				public void onSelectAid(int aid) {
					mAid = aid;
				}

				@Override
				public void onSelectAddress(String address) {
					edit_address.setText(address);
				}
			});
						
			break;
		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
