package com.imooly.android.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

import com.imooly.android.R;
import com.imooly.android.base.BaseFragment;
import com.imooly.android.tool.Config;

/**
 * 引导页4
 * 
 * @author daiye
 * 
 */
public class Navigation4Fragment extends BaseFragment {

	private ImageView iv_xin;
	private ImageView iv_icon;
	private TextView tv_content;
	private Animation animation_content;
	private Animation animationxin;
	private Animation animationicon;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.navigation4, container, false);
		createView(v);
		return v;
	}

	private void createView(View v) {
		tv_content = (TextView) v.findViewById(R.id.tv_content);
		LayoutParams params1 = (LayoutParams) tv_content.getLayoutParams();
		params1.setMargins(0, 0, 0, Config.getCaleHeight(90));
		tv_content.setLayoutParams(params1);
		tv_content.setTypeface(Config.getTypeface());
		
		iv_icon = (ImageView) v.findViewById(R.id.iv_icon);
		LayoutParams params2 = new LayoutParams(Config.getCaleWidth(346), Config.getCaleHeight(212));
		params2.setMargins(Config.getCaleWidth(42), Config.getCaleHeight(90), 0, 0);
		iv_icon.setLayoutParams(params2);
		
		iv_xin = (ImageView) v.findViewById(R.id.iv_xin);
		LayoutParams params3 = new LayoutParams(Config.getCaleWidth(64), Config.getCaleHeight(56));
		params3.setMargins(Config.getCaleWidth(330), Config.getCaleHeight(434), 0, 0);
		iv_xin.setLayoutParams(params3);
		
		animation_content = AnimationUtils.loadAnimation(mActivity, R.anim.navigation_slide_in_from_bottom);
		animationxin = AnimationUtils.loadAnimation(mActivity, R.anim.tutorail_xin);
		animationicon = AnimationUtils.loadAnimation(mActivity, R.anim.tutorail_scalate);
	}

	public void startAnimation() {
		iv_xin.startAnimation(animationxin);
		// 动画结束后停留在最后1帧
		animationxin.setFillAfter(true);
		animationxin.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				iv_icon.setVisibility(View.VISIBLE);
				iv_icon.startAnimation(animationicon);
				animationicon.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation arg0) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationRepeat(Animation arg0) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationEnd(Animation arg0) {
						tv_content.startAnimation(animation_content);
						tv_content.setVisibility(View.VISIBLE);
					}
				});
			}
		});
	}

	public void stopAnimation() {
		tv_content.setVisibility(View.GONE);
		iv_icon.setVisibility(View.GONE);

		if (tv_content.getAnimation() != null) {
			tv_content.getAnimation().cancel();
		}
		if (iv_icon.getAnimation() != null) {
			iv_icon.getAnimation().cancel();
		}
		if (iv_xin.getAnimation() != null) {
			iv_xin.getAnimation().cancel();
		}
	}
}
