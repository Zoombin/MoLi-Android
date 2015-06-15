package com.imooly.android.ui;

import java.text.DecimalFormat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooly.android.MoLiApplication;
import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.entity.RspVoucherNumber;
import com.imooly.android.entity.RspVoucherTerm;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.tool.Rotation3D;
import com.imooly.android.tool.Rotation3D.RotationListener;
import com.imooly.android.utils.Utils;
import com.imooly.android.widget.Toast;

public class VoucherActivity extends BaseActivity implements OnClickListener {
	private RelativeLayout relative_vouncher_detail, relative_get_vouncher;
	private LinearLayout linear_containier, ll_positive,ll_negative;
	private TextView text_flip_card;
	private boolean isShowingNumber = true; // 开始显示代金券金额

	private TextView text_voucher_number;

	private boolean isShowing = false;
	private TextView mTextView1, mTextView2, mTextView3, mTextView4;
	
	DecimalFormat fnum = new DecimalFormat("##0.00");
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vouncher);
		Log.d("xxx", "onCreate...");

		if (MoLiApplication.getInstance().isLogin() == false) {
			self.startActivityForResult(new Intent(self, LoginActivity.class), 0);
		}

		logActivityName(this);

		initView();
		
		getData();

	}
	
	private void getData() {
		
		Api.getVoucherTerm(self, Utils.getUnixTime()+"", new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {
				RspVoucherTerm rsp =  (RspVoucherTerm)rspData;
				RspVoucherTerm.Data data = rsp.data;
				
				
				if (data.getFlag() ==1) { // 需要更新
					mTextView1.setText(data.getContent().get(0));
					mTextView2.setText(data.getContent().get(1));
					mTextView3.setText(data.getContent().get(2));
					mTextView4.setText(data.getContent().get(3));
				}
			}

			@Override
			public void failed(String msg) {
				Toast.show(self, msg);
				
			}
		}, RspVoucherTerm.class);
		
	}

	private void initView() {

		findViewById(R.id.iv_back).setOnClickListener(this);

		linear_containier = (LinearLayout) findViewById(R.id.linear_container);
		linear_containier.setOnClickListener(this);

		relative_vouncher_detail = (RelativeLayout) findViewById(R.id.relative_vouncher_detail);
		relative_vouncher_detail.setOnClickListener(this);

		relative_get_vouncher = (RelativeLayout) findViewById(R.id.relative_get_vouncher);
		relative_get_vouncher.setOnClickListener(this);

		text_voucher_number = (TextView) findViewById(R.id.text_voucher_number);
		text_flip_card = (TextView) findViewById(R.id.text_flip_card);

		ll_positive = (LinearLayout) findViewById(R.id.ll_positive);
		ll_negative = (LinearLayout) findViewById(R.id.ll_negative);
		
		mTextView1 = (TextView) findViewById(R.id.text_one);
		mTextView2 = (TextView) findViewById(R.id.text_two);
		mTextView3 = (TextView) findViewById(R.id.text_three);
		mTextView4 = (TextView) findViewById(R.id.text_four);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			finish();
		}
	}
	
	@Override
	protected void onStart() {
		Log.d("xxx", "onStart...");

		if (MoLiApplication.getInstance().isLogin() == true) {

			Api.myVoucherNumber(self, new NetCallBack<ServiceResult>() {

				@Override
				public void success(ServiceResult rspData) {
					// TODO Auto-generated method stub

					RspVoucherNumber rsp = (RspVoucherNumber) rspData;
					RspVoucherNumber.Data VoucherData = rsp.data;
					text_voucher_number.setText(fnum.format(VoucherData.getVoucher()) + "");
				}

				@Override
				public void failed(String msg) {
					Toast.show(self, "获取代金券余额失败");
				}
			}, RspVoucherNumber.class);

		}

		super.onStart();
	}

	@Override
	protected void onResume() {
		Log.d("xxx", "onResume...");
		super.onResume();
	}

	@Override
	protected void onPause() {
		Log.d("xxx", "onPause...");
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		Log.d("xxx", "onDestroy...");
		super.onDestroy();
	}



	/** 推送进来的特别处理 */
	private void goBack() {
		String pushAction = getIntent().getStringExtra("pushAction");
		if (TextUtils.isEmpty(pushAction)) {
			finish();
		} else {
			Intent intent = new Intent(MainActivity.ACTION);
			sendBroadcast(intent);

			startActivity(new Intent(self, MainActivity.class));
			finish();
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		goBack();
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {
		case R.id.iv_back:
			goBack();
			break;

		case R.id.relative_vouncher_detail:
			startActivity(new Intent(self, VoucherDetailActivity.class));
			break;

		case R.id.relative_get_vouncher:
			startActivity(new Intent(self, VoucherHadGotActivity.class));
			break;

		case R.id.linear_container:

			// applyRotation(0.0f, 180.0f);

			new Rotation3D(0.0f, 180.0f, linear_containier, new RotationListener() {

				@Override
				public void onEndRotation() {

					if (isShowing == false) {

						text_flip_card.setText("点击翻开查看代金卷金额");
						isShowing = true;
						ll_negative.setVisibility(View.VISIBLE);
						ll_positive.setVisibility(View.GONE);
					} else {
						text_flip_card.setText("点击翻开查看使用细则");
						isShowing = false;
						ll_positive.setVisibility(View.VISIBLE);
						ll_negative.setVisibility(View.GONE);
					}
				}
			});
			break;

		default:
			break;
		}

	}
}
