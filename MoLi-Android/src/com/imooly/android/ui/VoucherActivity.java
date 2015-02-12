package com.imooly.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooly.android.MoLiApplication;
import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.animation.RotateFlipCardAnimation;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.entity.RspVoucherNumber;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.widget.Toast;

public class VoucherActivity extends BaseActivity implements OnClickListener {
	private RelativeLayout relative_vouncher_detail, relative_get_vouncher;
	private LinearLayout linear_containier;
	private LayoutInflater inflater;
	private TextView text_flip_card;
	private boolean isShowingNumber = true; // 开始显示代金券金额

	private View view_vouncher_number;
	private View view_vouncher_role;
	private View view_vouncher_bottom;

	private TextView text_voucher_number;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vouncher);
		Log.e("xxx", "onCreate...");

		if (MoLiApplication.getInstance().isLogin() == false) {
			self.startActivity(new Intent(self, LoginActivity.class));
		}

		logActivityName(this);

		inflater = self.getLayoutInflater();

		initView();

	}

	@Override
	protected void onStart() {
		Log.e("xxx", "onStart...");

		if (MoLiApplication.getInstance().isLogin() == true) {

			Api.myVoucherNumber(self, new NetCallBack<ServiceResult>() {

				@Override
				public void success(ServiceResult rspData) {
					// TODO Auto-generated method stub

					RspVoucherNumber rsp = (RspVoucherNumber) rspData;
					RspVoucherNumber.Data VoucherData = rsp.data;
					text_voucher_number.setText(VoucherData.getTotalvoucher() + "");
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
		Log.e("xxx", "onResume...");
		super.onResume();
	}

	@Override
	protected void onPause() {
		Log.e("xxx", "onPause...");
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		Log.e("xxx", "onDestroy...");
		super.onDestroy();
	}

	private void initView() {

		findViewById(R.id.iv_back).setOnClickListener(this);

		linear_containier = (LinearLayout) findViewById(R.id.linear_container);
		relative_vouncher_detail = (RelativeLayout) findViewById(R.id.relative_vouncher_detail);
		relative_vouncher_detail.setOnClickListener(this);

		relative_get_vouncher = (RelativeLayout) findViewById(R.id.relative_get_vouncher);
		relative_get_vouncher.setOnClickListener(this);

		// 代金券余额
		view_vouncher_number = inflater.inflate(R.layout.view_vouncher_number, null);
		text_voucher_number = (TextView) view_vouncher_number.findViewById(R.id.text_voucher_number);
		// 代金券使用细则
		view_vouncher_role = inflater.inflate(R.layout.view_vouncher_role, null);
		// 底部点击
		view_vouncher_bottom = inflater.inflate(R.layout.view_vouncher_bottom, null);
		text_flip_card = (TextView) view_vouncher_bottom.findViewById(R.id.text_flip_card);
		text_flip_card.setOnClickListener(this);

		linear_containier.addView(view_vouncher_number);
		linear_containier.addView(view_vouncher_bottom);

	}

	private void applyRotation(float start, float end) {
		// 计算中心点
		float centerX = linear_containier.getWidth() / 2.0f;
		float centerY = linear_containier.getHeight() / 2.0f;

		RotateFlipCardAnimation rotation = new RotateFlipCardAnimation(start, end, centerX, centerY, 0.0f, true);
		rotation.setDuration(500);
		rotation.setFillAfter(true);
		rotation.setInterpolator(new LinearInterpolator());
		// 设置监听
		rotation.setAnimationListener(new DisplayNextView());

		linear_containier.startAnimation(rotation);
	}

	private class DisplayNextView implements Animation.AnimationListener {

		public void onAnimationStart(Animation animation) {
		}

		// 动画结束
		public void onAnimationEnd(Animation animation) {

			linear_containier.removeAllViews();

			if (isShowingNumber == true) {
				linear_containier.addView(view_vouncher_role);
				linear_containier.addView(view_vouncher_bottom);
				text_flip_card.setText("点击翻开查看代金券余额");
				isShowingNumber = false;
			} else {
				text_flip_card.setText("点击翻开查看使用细则");
				linear_containier.addView(view_vouncher_number);
				linear_containier.addView(view_vouncher_bottom);
				isShowingNumber = true;
			}

		}

		public void onAnimationRepeat(Animation animation) {
		}
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

		case R.id.text_flip_card:

			applyRotation(0.0f, 180.0f);

			break;

		default:
			break;
		}

	}
}
