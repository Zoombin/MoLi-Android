package com.imooly.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.imooly.android.R;
import com.imooly.android.Interface.RequestWebListener;

/**
 * 加载遮罩层
 * @author daiye
 *
 */
public class LoadingView extends RelativeLayout implements OnClickListener {
	
	private RelativeLayout layout_loading_img;
	private ImageView loading_img;
	private LinearLayout error_layout;
	private Button btn_refresh;
	private View v;
	private Animation animation;
	
	RequestWebListener listerner;
	public RequestWebListener getL() {
		return listerner;
	}
	public void setL(RequestWebListener listerner) {
		this.listerner = listerner;
	}

	public LoadingView(Context context) {
		super(context);
		init(context);
	}

	public LoadingView(Context context, AttributeSet attributeset) {
		super(context, attributeset);
		init(context);
	}

	private void init(Context context) {
		v = View.inflate(context, R.layout.layout_loading, this);
		
		// 界面
		layout_loading_img = (RelativeLayout) v.findViewById(R.id.layout_loading_img);
		loading_img = (ImageView) v.findViewById(R.id.loading_img);
		animation = AnimationUtils.loadAnimation(context, R.anim.tip); 
		LinearInterpolator lin = new LinearInterpolator(); 
		animation.setInterpolator(lin);
		loading_img.startAnimation(animation);
		
		// 无网路
		error_layout = ((LinearLayout) v.findViewById(R.id.error_layout));
		error_layout.setOnClickListener(this);
		
		btn_refresh = (Button) v.findViewById(R.id.btn_refresh);
		btn_refresh.setOnClickListener(this);
		
		showLoading();
	}

	private void showLoading() {
		loading_img.startAnimation(animation);
		layout_loading_img.setVisibility(View.VISIBLE);
		
	}

	private void hiddenLoading() {
//		loading_img.setAnimation(null);
		layout_loading_img.setVisibility(View.GONE);
	}

	public static final int network_error = 1;
	public static final int showloading = 2;
	public static final int success = 3;
	public static final int no_shopcart = 4;
	
	public void postHandle(int state) {
		switch (state) {
		case network_error:
			hiddenLoading();
			error_layout.setVisibility(View.VISIBLE);
			break;
		case showloading:
			showLoading();
			error_layout.setVisibility(View.GONE);
			break;
		case success:
			hiddenLoading();
			error_layout.setVisibility(View.GONE);
			break;
		default:
			break;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_refresh:
			if (listerner != null) {
				postHandle(showloading);
				listerner.requestWeb();
			}
			break;
		default:
			break;
		}
	}
}
