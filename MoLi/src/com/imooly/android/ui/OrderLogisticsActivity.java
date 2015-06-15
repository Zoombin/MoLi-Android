package com.imooly.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.entity.RspLogistic;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.widget.Toast;

/***
 * 物流详情
 * 
 * @author lsd
 * 
 */
public class OrderLogisticsActivity extends BaseActivity implements OnClickListener {
	public static String ORDER_NO = "orderno";
	public static String TYPE = "type";
	public static String GOODS_ID = "goodsid";
	public static String TRADE_ID = "tradeid";
	public static String UNIQUE = "unique";
	private RelativeLayout ll_title;
	private ImageView iv_back;
	private TextView tv_title;
	private ImageView order_express_icon;
	private TextView order_express_name;
	private TextView order_express_number;
	private TextView order_express_status;

	private WebView webView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_logistics);

		logActivityName(this);
		
		initView();
		getData();
	}

	private void initView() {
		// TODO Auto-generated method stub
		ll_title = (RelativeLayout) findViewById(R.id.ll_title);

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);

		tv_title = (TextView) findViewById(R.id.tv_title);

		order_express_icon = (ImageView) findViewById(R.id.order_express_icon);
		order_express_name = (TextView) findViewById(R.id.order_express_name);
		order_express_number = (TextView) findViewById(R.id.order_express_number);
		order_express_status = (TextView) findViewById(R.id.order_express_status);
		order_express_status.setVisibility(View.INVISIBLE);

		webView = (WebView) findViewById(R.id.webview);
		// webView自适应大小
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
	}

	private void getData() {
		Intent intent = getIntent();
		String orderno = "";
		/** normal/change, */
		String type = "";
		if (intent.hasExtra(ORDER_NO)) {
			orderno = intent.getStringExtra(ORDER_NO);
		}
		if (intent.hasExtra(TYPE)) {
			type = intent.getStringExtra(TYPE);
		}
		if ("change".equals(type)) {
			type = "change";
		} else {
			type = "normal";
		}
		String goodsid="";
		if(intent.hasExtra(GOODS_ID)){
			goodsid = intent.getStringExtra(GOODS_ID);
		}
		String tradeid="";
		if(intent.hasExtra(TRADE_ID)){
			tradeid = intent.getStringExtra(TRADE_ID);
		}
		String unique="";
		if(intent.hasExtra(UNIQUE)){
			unique = intent.getStringExtra(UNIQUE);
		}
		Api.logistic(self, type, orderno,goodsid,tradeid,unique,new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {
				// TODO Auto-generated method stub
				RspLogistic rsp = (RspLogistic) rspData;
				if(rsp.data != null){
					RspLogistic.Data data = rsp.data;
					
					String logo = data.getLogo();
					imageLoader.displayImage(logo, order_express_icon);
					
					order_express_name.setText(data.getName());
					if(!TextUtils.isEmpty(data.getNo())){
						order_express_number.setText("运单编号："+data.getNo());
					}else{
						order_express_number.setText(data.getMsg());
					}
					
					String link = data.getLink();
					Log.i("LSD", "link = "+link);
					webView.loadUrl(link);
				}
			}

			@Override
			public void failed(String msg) {
				// TODO Auto-generated method stub
               if(!TextUtils.isEmpty(msg)){
            	   Toast.show(self, msg);
               }
			}
		}, RspLogistic.class);

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
