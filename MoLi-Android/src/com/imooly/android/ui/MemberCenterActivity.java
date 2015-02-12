package com.imooly.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.base.BaseActivity;

public class MemberCenterActivity extends BaseActivity implements OnClickListener {

	private RelativeLayout rl_title;

	private LinearLayout linear_member_card;
	private LinearLayout linear_member_recharge;
	private LinearLayout linear_member_right;

	private TextView tv_title;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member_center);
		
		logActivityName(this);

		initView();
	}

	private void initView() {

		// title
		rl_title = (RelativeLayout) findViewById(R.id.rl_title);

		findViewById(R.id.button_back).setOnClickListener(this);

		tv_title = (TextView) findViewById(R.id.tv_title);
		
		
		linear_member_card = (LinearLayout) findViewById(R.id.linear_member_card);
		linear_member_recharge = (LinearLayout) findViewById(R.id.linear_member_recharge);
		linear_member_right = (LinearLayout) findViewById(R.id.linear_member_right);

		linear_member_card.setOnClickListener(this);
		linear_member_recharge.setOnClickListener(this);
		linear_member_right.setOnClickListener(this);



	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	/**推送进来的特别处理*/
	private void goBack(){
		String pushAction = getIntent().getStringExtra("pushAction");
		if(TextUtils.isEmpty(pushAction)){
			finish();
		}else{
			Intent intent = new Intent(MainActivity.ACTION);
			sendBroadcast(intent);
			
			startActivity(new Intent(self,MainActivity.class));
			finish();
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		goBack();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		
		case R.id.button_back:
			
			goBack();
			break;
			
		case R.id.linear_member_card:

			MemberCenterActivity.this.startActivity(new Intent(MemberCenterActivity.this, MemberCardActivity.class));
			
			break;

		case R.id.linear_member_recharge:

			MemberCenterActivity.this.startActivity(new Intent(MemberCenterActivity.this, MemberRechargeActivity.class));
			
			break;

		case R.id.linear_member_right:
			
			MemberCenterActivity.this.startActivity(new Intent(MemberCenterActivity.this, MemberRightActivity.class));
			
			break;

		default:
			break;

		}

	}

}
