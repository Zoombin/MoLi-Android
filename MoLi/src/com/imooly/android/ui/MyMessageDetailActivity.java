package com.imooly.android.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.entity.RspMsginfo;
import com.imooly.android.entity.RspSuccessCommon;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.utils.Utils;
import com.imooly.android.widget.Toast;

/***
 * 我的消息详情
 * 
 * @author
 * 
 */
public class MyMessageDetailActivity extends BaseActivity implements OnClickListener {
	private RelativeLayout ll_title;
	private ImageView iv_back;
	private TextView tv_title, text_info, text_time;
	private Button button_share;
	private String messageID;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mymessage_detail);

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

		text_info = (TextView) findViewById(R.id.text_message);
		text_time = (TextView) findViewById(R.id.mymessage_item_time);

	}

	private void initData() {

		final String messageID = getIntent().getStringExtra("messageid");
		Log.d("xxx", "messageid = " + messageID);
		
		String time = getIntent().getStringExtra("time");
		text_time.setText(Utils.getDatebyTimestamp(time));
		
		Api.msginfo(self, messageID, new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {
				// TODO Auto-generated method stub
				RspMsginfo rsp = (RspMsginfo) rspData;

				tv_title.setText(rsp.data.getTitle());
				text_info.setText(rsp.data.getContent());
				
				if (rsp.data.getIsread() == 0) {
					
					setMsgReaded(messageID);
				}
				
			}

			@Override
			public void failed(String msg) {
				Toast.show(self, msg);
			}
		}, RspMsginfo.class);
	}

	private void setMsgReaded(String messageID) {

		Api.readMsg(self, messageID, new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {

			}

			@Override
			public void failed(String msg) {

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

		default:
			break;
		}
	}
}
