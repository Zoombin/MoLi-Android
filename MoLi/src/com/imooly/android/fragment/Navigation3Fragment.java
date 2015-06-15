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
 * 引导页3
 * 
 * @author daiye
 * 
 */
public class Navigation3Fragment extends BaseFragment {

	private ImageView iv_head;
	private TextView tv_content;
	private Animation animation_content;
	private Animation animation_left;
	private Animation animation_bottom;
	private Animation animation_top;
	private Animation tutorail_rotate_reverse;
	private ImageView iv_tv1;
	private ImageView iv_tv2;
	private ImageView iv_tv3;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.navigation3, container, false);
		createView(v);
		return v;
	}

	private void createView(View v) {
		tv_content = (TextView) v.findViewById(R.id.tv_content);
		LayoutParams params1 = (LayoutParams) tv_content.getLayoutParams();
		params1.setMargins(0, 0, 0, 90 * Config.displayheight / 1136);
		tv_content.setLayoutParams(params1);
		tv_content.setTypeface(Config.getTypeface());
		
		iv_head = (ImageView) v.findViewById(R.id.iv_head);
		LayoutParams params2 = new LayoutParams(Config.getCaleWidth(267), Config.getCaleHeight(276));
		params2.setMargins(Config.getCaleWidth(352), Config.getCaleHeight(242), 0, 0);
		iv_head.setLayoutParams(params2);
		
		iv_tv1 = (ImageView) v.findViewById(R.id.iv_tv1);
		LayoutParams params3 = new LayoutParams(Config.getCaleWidth(178), Config.getCaleHeight(93));
		params3.setMargins(Config.getCaleWidth(138), Config.getCaleHeight(378), 0, 0);
		iv_tv1.setLayoutParams(params3);
		
		iv_tv2 = (ImageView) v.findViewById(R.id.iv_tv2);
		LayoutParams params4 = new LayoutParams(Config.getCaleWidth(178), Config.getCaleHeight(93));
		params4.setMargins(Config.getCaleWidth(40), Config.getCaleHeight(460), 0, 0);
		iv_tv2.setLayoutParams(params4);
		
		iv_tv3 = (ImageView) v.findViewById(R.id.iv_tv3);
		LayoutParams params5 = new LayoutParams(Config.getCaleWidth(172), Config.getCaleHeight(58));
		params5.setMargins(Config.getCaleWidth(120), Config.getCaleHeight(532), 0, 0);
		iv_tv3.setLayoutParams(params5);
		
		animation_content = AnimationUtils.loadAnimation(mActivity, R.anim.navigation_slide_in_from_bottom);
		tutorail_rotate_reverse = AnimationUtils.loadAnimation(mActivity, R.anim.tutorail_rotate_reverse);
		animation_left = AnimationUtils.loadAnimation(mActivity, R.anim.navigation_slide_in_from_left);
		animation_top = AnimationUtils.loadAnimation(mActivity, R.anim.navigation_slide_in_from_top);
		animation_bottom = AnimationUtils.loadAnimation(mActivity, R.anim.navigation_slide_in_from_bottom);
	}

	public void startAnimation() {
		iv_head.startAnimation(tutorail_rotate_reverse);
		
		animation_top.setAnimationListener(new AnimationListener() {
			
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
				animation_left.setAnimationListener(new AnimationListener() {
					
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
						animation_bottom.setAnimationListener(new AnimationListener() {
							
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
						iv_tv3.setVisibility(View.VISIBLE);
						iv_tv3.startAnimation(animation_bottom);
					}
				});
				iv_tv2.setVisibility(View.VISIBLE);
				iv_tv2.startAnimation(animation_left);
			}
		});
		iv_tv1.setVisibility(View.VISIBLE);
		iv_tv1.startAnimation(animation_top);
	}

	public void stopAnimation() {
		tv_content.setVisibility(View.GONE);
		iv_tv1.setVisibility(View.GONE);
		iv_tv2.setVisibility(View.GONE);
		iv_tv3.setVisibility(View.GONE);
		
		if (tv_content.getAnimation() != null) {
			tv_content.getAnimation().cancel();
		}
		if (iv_tv1.getAnimation() != null) {
			iv_tv1.getAnimation().cancel();
		}
		if (iv_tv2.getAnimation() != null) {
			iv_tv2.getAnimation().cancel();
		}
		if (iv_tv3.getAnimation() != null) {
			iv_tv3.getAnimation().cancel();
		}
		iv_head.getAnimation().cancel();
	}
}
