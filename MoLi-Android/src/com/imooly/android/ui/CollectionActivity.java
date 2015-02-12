package com.imooly.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooly.android.MoLiApplication;
import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.entity.RspCollectionNumbers;
import com.imooly.android.entity.ServiceResult;

public class CollectionActivity extends BaseActivity implements OnClickListener {

	private LinearLayout linear_good;
	private LinearLayout linear_store;
	private LinearLayout linear_flagship;

	private ImageView iv_back;
	
	private TextView text_good_number, text_flag_store_number, text_store_number;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collection);
		
		if (MoLiApplication.getInstance().isLogin() == false) {
			self.startActivity(new Intent(self, LoginActivity.class));
		}
		
		logActivityName(this);

		initView();
		initData();
	}
	
	@Override
	protected void onStart() {
		if (MoLiApplication.getInstance().isLogin() == true) {
			initData();
		}
		super.onStart();
	}
	
	private void getCollectionNumber() {
		Api.myCollectionNumbers(self, new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {
				RspCollectionNumbers rsp = (RspCollectionNumbers) rspData;
				RspCollectionNumbers.Data data = rsp.data;
				
				text_good_number.setText(String.format("%d件商品", data.goodNumber));
				text_flag_store_number.setText(String.format("%d个店铺", data.flagStoreNumber));
				text_store_number.setText(String.format("%d个实体店", data.storeNumber));
				
			}

			@Override
			public void failed(String msg) {
				// TODO Auto-generated method stub

			}
		}, RspCollectionNumbers.class);
	}

	private void initView() {

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);

		linear_good = (LinearLayout) findViewById(R.id.linear_collection_good);
		linear_store = (LinearLayout) findViewById(R.id.linear_collection_store);
		linear_flagship = (LinearLayout) findViewById(R.id.linear_collection_flagship);

		linear_good.setOnClickListener(this);
		linear_store.setOnClickListener(this);
		linear_flagship.setOnClickListener(this);
		
		// 28件商品
		text_good_number = (TextView) findViewById(R.id.text_good_number);
		text_flag_store_number = (TextView) findViewById(R.id.text_flag_store_number);
		text_store_number = (TextView) findViewById(R.id.text_store_number);

	}
	
	private void initData() {
		
		getCollectionNumber();

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
		case R.id.iv_back:
			goBack();
			break;

		case R.id.linear_collection_flagship:

			CollectionActivity.this.startActivity(new Intent(CollectionActivity.this, CollectionFlagshipActivity.class));
			break;

		case R.id.linear_collection_good:

			CollectionActivity.this.startActivity(new Intent(CollectionActivity.this, CollectionGoodActivity.class));
			break;

		case R.id.linear_collection_store:
			CollectionActivity.this.startActivity(new Intent(CollectionActivity.this, CollectionStoreActivity.class));
			break;

		default:
			break;

		}

	}

}
