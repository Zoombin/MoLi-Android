package com.imooly.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooly.android.MoLiApplication;
import com.imooly.android.R;
import com.imooly.android.base.BaseActivity;

public class MemberRightActivity extends BaseActivity implements OnClickListener {

	private RelativeLayout rl_title;

	private ImageView iv_back;
	private TextView tv_title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member_right);
		
		logActivityName(this);
		
		initView();

	}

	private void initView() {

		// title
		rl_title = (RelativeLayout) findViewById(R.id.rl_title);

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);

		tv_title = (TextView) findViewById(R.id.tv_title);

		// 试用时间

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.iv_back:

			finish();
			break;

		default:
			break;

		}

	}

}
