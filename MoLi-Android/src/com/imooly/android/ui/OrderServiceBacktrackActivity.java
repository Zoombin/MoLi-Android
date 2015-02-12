package com.imooly.android.ui;

import android.content.Intent;
import android.os.Bundle;
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
import com.imooly.android.entity.RspServiceBusiness;
import com.imooly.android.entity.RspSuccessCommon;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.widget.Toast;

/***
 * 售后服务 - 换货物流 (这个填写后是给商家用的)
 * 
 * @author lsd
 * 
 */
public class OrderServiceBacktrackActivity extends BaseActivity implements OnClickListener {
	public static String ORDER_NO = "orderno";
	public static String GOODS_ID = "goodsid";
	public static String TRADE_ID = "tradeid";
	public static String UNIQUE = "unique";
	public static String TYPE = "type";

	private RelativeLayout ll_title;
	private ImageView iv_back;
	private TextView tv_title;

	private TextView order_backtrack_addr;
	private EditText order_backtrack_name, order_backtrack_phone, order_backtrack_logistics, order_backtrack_num, order_backtrack_message;
	private Button bt_commit;

	// //-----公共参数
	String orderno = "";// 传递过来的订单编号
	String goodsid = "";
	String tradeid = "";
	String unique = "";
	String type = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_service_backtrack);

		logActivityName(this);
		
		initView();
		initData();
	}

	private void initView() {
		// TODO Auto-generated method stub
		ll_title = (RelativeLayout) findViewById(R.id.ll_title);

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);

		tv_title = (TextView) findViewById(R.id.tv_title);

		order_backtrack_addr = (TextView) findViewById(R.id.order_backtrack_addr);
		order_backtrack_name = (EditText) findViewById(R.id.order_backtrack_name);
		order_backtrack_phone = (EditText) findViewById(R.id.order_backtrack_phone);
		order_backtrack_logistics = (EditText) findViewById(R.id.order_backtrack_logistics);
		order_backtrack_num = (EditText) findViewById(R.id.order_backtrack_num);
		order_backtrack_message = (EditText) findViewById(R.id.order_backtrack_message);
		bt_commit = (Button) findViewById(R.id.bt_commit);
		bt_commit.setOnClickListener(this);

	}

	private void initData() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		if(intent.hasExtra(ORDER_NO)){
			orderno = intent.getStringExtra(ORDER_NO);
		}
		if(intent.hasExtra(GOODS_ID)){
			goodsid = intent.getStringExtra(GOODS_ID);
		}
		if(intent.hasExtra(TRADE_ID)){
			tradeid = intent.getStringExtra(TRADE_ID);
		}
		if(intent.hasExtra(UNIQUE)){
			unique = intent.getStringExtra(UNIQUE);
		}
		if(intent.hasExtra(TYPE)){
			type = intent.getStringExtra(TYPE);
		}
		
		Api.servicebusiness(self, orderno, goodsid, tradeid,unique, new NetCallBack<ServiceResult>() {
			
			@Override
			public void success(ServiceResult rspData) {
				// TODO Auto-generated method stub
				RspServiceBusiness rsp = (RspServiceBusiness) rspData;
				if(rsp.data != null){
					order_backtrack_addr.setText(rsp.data.getAddress());
				}
			}
			
			@Override
			public void failed(String msg) {
				// TODO Auto-generated method stub
				Toast.show(self, msg);
			}
		}, RspServiceBusiness.class);

	}

	private void commit() {
		String name = order_backtrack_name.getText().toString();
		String phone = order_backtrack_phone.getText().toString();
		String logisticname = order_backtrack_logistics.getText().toString();
		String logisticno = order_backtrack_num.getText().toString();
		String remark  = order_backtrack_message.getText().toString();
		
		Api.servicelogistic(self, orderno, goodsid, tradeid, type, name, phone, logisticname, logisticno, remark, new NetCallBack<ServiceResult>() {
			
			@Override
			public void success(ServiceResult rspData) {
				// TODO Auto-generated method stub
				RspSuccessCommon rsp = (RspSuccessCommon) rspData;
				if(rsp.data != null && rsp.data.getSuccess() ==1){
					Toast.show(self, "提交成功");
					setResult(RESULT_OK);
					finish();
				}else{
					Toast.show(self, rsp.data.message);
				}
			}
			
			@Override
			public void failed(String msg) {
				// TODO Auto-generated method stub
				Toast.show(self, msg);
			}
		}, RspSuccessCommon.class);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.bt_commit:
			commit();
			break;
		default:
			break;
		}
	}

}
