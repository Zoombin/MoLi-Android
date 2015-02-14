package com.imooly.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.imooly.android.R;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.utils.Utils;
import com.imooly.android.view.UpdateDialog;

public class AboutUsActivity extends BaseActivity implements OnClickListener {

	private RelativeLayout rl_title;
	private RelativeLayout rl_copyright_infomation;
	private RelativeLayout rl_sofeware_licence;
	private RelativeLayout rl_declare;

	private ImageView iv_back;
	private TextView tv_title, text_version_info;
	
	private Button btn_aboutus_update;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_about_us);
		
		logActivityName(this);
		
		initView();
		initDate();
	}

	private void initView() {

		// title
		rl_title = (RelativeLayout) findViewById(R.id.rl_title);

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);

		tv_title = (TextView) findViewById(R.id.tv_title);

		rl_copyright_infomation = (RelativeLayout) findViewById(R.id.rl_copyright_infomation);
		rl_copyright_infomation.setOnClickListener(this);
		rl_sofeware_licence = (RelativeLayout) findViewById(R.id.rl_sofeware_licence);
		rl_sofeware_licence.setOnClickListener(this);
		
		rl_declare = (RelativeLayout) findViewById(R.id.rl_declare);
		rl_declare.setOnClickListener(this);
		
		// 更新
		btn_aboutus_update = (Button) findViewById(R.id.btn_aboutus_update);
		btn_aboutus_update.setOnClickListener(this);
		text_version_info = (TextView) findViewById(R.id.text_version_info);
		
	}
	
	private void initDate() {
		text_version_info.setText(Utils.getClientVersionName(self));
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
		case R.id.rl_copyright_infomation:
			
			Intent intent = new Intent(AboutUsActivity.this, AgreementActivity.class);
			intent.putExtra(AgreementActivity.EXTRA, AgreementActivity.EXTRA_COPYRIGHT);
			AboutUsActivity.this.startActivity(intent);
			
			break;

		case R.id.rl_sofeware_licence:
			Intent intent2 = new Intent(AboutUsActivity.this, AgreementActivity.class);
			intent2.putExtra(AgreementActivity.EXTRA, AgreementActivity.EXTRA_PROTOCOL);
			AboutUsActivity.this.startActivity(intent2);
			break;

		case R.id.rl_declare:
			Intent intent3 = new Intent(AboutUsActivity.this, AgreementActivity.class);
			intent3.putExtra(AgreementActivity.EXTRA, AgreementActivity.EXTRA_VERSION_DESC);
			AboutUsActivity.this.startActivity(intent3);
			break;

		case R.id.btn_aboutus_update:
			new UpdateDialog(this).checkUpdate();			
			break;			
		default:
			break;

		}

	}

}
