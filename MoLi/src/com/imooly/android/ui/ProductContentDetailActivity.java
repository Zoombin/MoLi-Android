package com.imooly.android.ui;

import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.entity.RspGoodsContent;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.enums.ShareObject;
import com.imooly.android.tool.Config;
import com.imooly.android.widget.ShareDialog;
import com.imooly.android.widget.Toast;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 图文详情页
 * 
 * @author lsd
 * 
 */
public class ProductContentDetailActivity extends BaseActivity implements OnClickListener {
	public static final String EXTRA_GOODSID = "goodsid";
	private LayoutInflater mInflater;
	private Button btn_back;
	private Button btn_share;
	private TextView tv_product_name;
	private LinearLayout ll_content;
	private String goodsid;
	DisplayImageOptions defaultOptions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_content_detail);
		logActivityName(this);

		defaultOptions = new DisplayImageOptions.Builder().cacheInMemory(true).bitmapConfig(Bitmap.Config.ARGB_8888) // 图片压缩质量
				.cacheOnDisc(true).imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2).build();

		initView();
		initData();
	}

	private void initView() {
		mInflater = (LayoutInflater) self.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		findViewById(R.id.btn_back).setOnClickListener(this);
		btn_share = (Button) findViewById(R.id.btn_share);
		btn_share.setOnClickListener(this);
		ll_content = (LinearLayout) findViewById(R.id.ll_content);
	}

	private void initData() {
		goodsid = getIntent().getStringExtra(EXTRA_GOODSID);
		Api.goodsContent(self, goodsid, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				RspGoodsContent rsp = (RspGoodsContent) rspData;
				RspGoodsContent.Data data = rsp.data;
				if (data != null) {
					List<String> contentImgs = data.getGoodscontent();
					if (contentImgs != null && contentImgs.size() > 0) {
						for (int i = 0; i < contentImgs.size(); i++) {
							String url = contentImgs.get(i);
							
							// 用webView来显示
							//View view = mInflater.inflate(R.layout.view_webview, null);
							//WebView webView = (WebView) view.findViewById(R.id.webview);

							// 根据屏幕宽度来缩放
							//webView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
							//webView.setInitialScale(Config.width / 750 * 100);
							// 缩放模式
							//webView.getSettings().setUseWideViewPort(true);
							//webView.getSettings().setLoadWithOverviewMode(true);
							//webView.loadUrl(url);
							

							View view = mInflater.inflate(R.layout.view_image, null);
							ImageView imageView = (ImageView) view.findViewById(R.id.image);

							ImageLoader.getInstance().displayImage(url, imageView, defaultOptions, new ImageLoadingListener() {
								@Override
								public void onLoadingStarted(String arg0, View arg1) {
									// TODO Auto-generated method stub
								}

								@Override
								public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
									// TODO Auto-generated method stub
								}

								@Override
								public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
									if(arg2 != null){
										int bpwidth = arg2.getWidth();
										int bpheight = arg2.getHeight();
										int width = Config.width;
										int height = Config.width * bpheight / bpwidth;
										RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(width, height);
										arg1.setLayoutParams(params);
									}
								}

								@Override
								public void onLoadingCancelled(String arg0, View arg1) {
									// TODO Auto-generated method stub
								}
							});

							ll_content.addView(view);
						}
					} else {
						Toast.show(self, "无图文详情！");
						finish();
					}
				}
			}

			@Override
			public void failed(String msg) {
				Toast.show(self, msg);
				finish();
			}
		}, RspGoodsContent.class);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.btn_share:
			// 分享
			new ShareDialog(self, ShareObject.goods, goodsid);
			break;
		default:
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		ImageLoader.getInstance().clearDiscCache();
		ImageLoader.getInstance().clearMemoryCache();
		super.onDestroy();
	}
}
