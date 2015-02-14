package com.imooly.android.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.base.BaseActivity;

/**
 * 通用webView页面
 * 
 * @author lsd
 * 
 */
public class WebViewActivity extends BaseActivity implements OnClickListener {
	private RelativeLayout ll_title;
	private ImageView iv_back;
	private TextView tv_title;
	private WebView webView;
	private ProgressBar proBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_webview);

		logActivityName(this);

		initView();
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initView() {
		ll_title = (RelativeLayout) findViewById(R.id.ll_title);

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);

		tv_title = (TextView) findViewById(R.id.tv_title);

		proBar = (ProgressBar) findViewById(R.id.progress);
		webView = (WebView) findViewById(R.id.webview);
		// webView自适应大小
		webView.getSettings().setUseWideViewPort(true);
		webView.getSettings().setLoadWithOverviewMode(true);
		WebSettings webSettings = webView.getSettings();
		webSettings.setJavaScriptEnabled(true);
		WebViewClient webViewClient = new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				// TODO Auto-generated method stub
				proBar.setVisibility(View.VISIBLE);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				proBar.setVisibility(View.INVISIBLE);
			}
		};
		webView.setWebViewClient(webViewClient);
	}

	private void initData() {
		String url = getIntent().getStringExtra("url");
		webView.loadUrl(url);
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			goBack();
			break;
		default:
			break;
		}
	}
}
