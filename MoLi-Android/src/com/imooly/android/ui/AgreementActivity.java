package com.imooly.android.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.Net.NetUtils;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.utils.AssetsUtils;

/**
 * 协议画面
 * 
 * @author
 * 
 */
public class AgreementActivity extends BaseActivity implements OnClickListener {

	private RelativeLayout ll_title;
	private ImageView iv_back;
	private TextView tv_title;
	private ScrollView scorll_agree;
	private TextView tv_agreement;
	private String extra;

	private WebView mWebView;

	// 版权信息
	private static final String URL_COPYRIHT = "public/copyright";
	// 软件许可协议
	private static final String URL_PROTOCOL = "public/protocol";
	// 使用帮助
	private static final String URL_HELP = "public/usehelp";
	// 会员服务条款
	private static final String URL_VIP = "public/vipuserterms";
	// 版本说明
	private static final String URL_VERSION_DESC = "public/versiondesc";

	public static final String EXTRA = "extra";
	public static final String EXTRA_COPYRIGHT = "extra_copyrihgt";
	public static final String EXTRA_PROTOCOL = "extra_protocol";
	public static final String EXTRA_HELP = "extra_help";
	public static final String EXTRA_VIP = "extra_vip";
	public static final String EXTRA_VERSION_DESC = "extra_version_desc";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_agreement);

		logActivityName(this);

		extra = getIntent().getStringExtra(EXTRA);

		initView();
		initData();
	}

	private void initView() {
		ll_title = (RelativeLayout) findViewById(R.id.ll_title);

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);

		tv_title = (TextView) findViewById(R.id.tv_title);

		// webview
		mWebView = (WebView) findViewById(R.id.web);
		WebSettings webSettings = mWebView.getSettings();
		webSettings.setJavaScriptEnabled(true);

		/*
		 * scorll_agree = (ScrollView) findViewById(R.id.scorll_agree);
		 * uiAdapter.setPadding(scorll_agree, 18, 25, 18, 25);
		 */

		tv_agreement = (TextView) findViewById(R.id.tv_agreement);
	}

	private void initData() {

		if (TextUtils.equals(extra, EXTRA_COPYRIGHT)) {
			tv_title.setText("版权信息");
			mWebView.loadUrl(NetUtils.BASE_URL + URL_COPYRIHT);
			Log.d("xxx", NetUtils.BASE_URL + URL_COPYRIHT);
			return;
		}

		if (TextUtils.equals(extra, EXTRA_PROTOCOL)) {
			tv_title.setText("软件使用许可协议");
			mWebView.loadUrl(NetUtils.BASE_URL + URL_PROTOCOL);
			Log.d("xxx", NetUtils.BASE_URL + URL_PROTOCOL);
			return;
		}

		if (TextUtils.equals(extra, EXTRA_VERSION_DESC)) {
			tv_title.setText("说明");
			mWebView.loadUrl(NetUtils.BASE_URL + URL_VERSION_DESC);
			Log.d("xxx", NetUtils.BASE_URL + URL_VERSION_DESC);
			return;
		}
		
		if (TextUtils.equals(extra, EXTRA_VIP)) {
			tv_title.setText("魔力网会员服务条款");
			mWebView.loadUrl(NetUtils.BASE_URL + URL_VERSION_DESC);
			Log.d("xxx", NetUtils.BASE_URL + URL_VERSION_DESC);
			return;
		}

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
