package com.imooly.android.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.base.BaseFragment;
import com.imooly.android.tool.Config;

/**
 * 引导页1
 * 
 * @author daiye
 * 
 */
public class Navigation1Fragment extends BaseFragment {

	private ImageView iv_eye1;
	private ImageView iv_eye2;
	private TextView tv_content;
	private ImageView iv_icon;
	private Animation animation_content;
	private Animation animation_eye;
	private Animation animation_icon;
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.navigation1, container, false);
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
		LayoutParams params2 = new LayoutParams(Config.getCaleWidth(410), Config.getCaleHeight(395));
		params2.setMargins(Config.getCaleWidth(30), Config.getCaleHeight(95), 0, 0);
		iv_icon.setLayoutParams(params2);
		
		iv_eye1 = (ImageView) v.findViewById(R.id.iv_eye1);
		LayoutParams params3 = new LayoutParams(Config.getCaleWidth(32), Config.getCaleHeight(32));
		params3.setMargins(Config.getCaleWidth(430), Config.getCaleHeight(596), 0, 0);
		iv_eye1.setLayoutParams(params3);
		
		iv_eye2 = (ImageView) v.findViewById(R.id.iv_eye2);
		LayoutParams params4 = new LayoutParams(Config.getCaleWidth(32), Config.getCaleHeight(32));
		params4.setMargins(Config.getCaleWidth(484), Config.getCaleHeight(610), 0, 0);
		iv_eye2.setLayoutParams(params4);
		
		animation_content = AnimationUtils.loadAnimation(mActivity,
				R.anim.navigation_slide_in_from_bottom);
		animation_eye = AnimationUtils.loadAnimation(mActivity,
				R.anim.tutorail_rotate);
		animation_icon = AnimationUtils.loadAnimation(mActivity, R.anim.tutorail_scalate);
		
		startAnimation();
	}

	public void startAnimation() {
		iv_icon.setVisibility(View.VISIBLE);

		iv_icon.startAnimation(animation_icon);
		animation_icon.setAnimationListener(new AnimationListener() {
			
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
				tv_content.setVisibility(View.VISIBLE);
				tv_content.startAnimation(animation_content);
			}
		});
		
		iv_eye1.startAnimation(animation_eye);
		iv_eye2.startAnimation(animation_eye);
	}

	public void stopAnimation() {
		if (tv_content.getAnimation() != null) {
			tv_content.getAnimation().cancel();
		}
		if (iv_icon.getAnimation() != null) {
			iv_icon.getAnimation().cancel();
		}
		if (iv_eye1.getAnimation() != null) {
			iv_eye1.getAnimation().cancel();
		}
		if (iv_eye2.getAnimation() != null) {
			iv_eye2.getAnimation().cancel();
		}

		tv_content.setVisibility(View.GONE);
		iv_icon.setVisibility(View.GONE);
	}
}
