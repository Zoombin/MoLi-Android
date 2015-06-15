package com.imooly.android.fragment;

import android.os.Bundle;
import android.os.Handler;
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
 * 引导页2
 * 
 * @author daiye
 * 
 */
public class Navigation2Fragment extends BaseFragment {

	private ImageView iv_navigation2_anim_part2;
	private ImageView nav_p1_ji, nav_p1_you, nav_p1_hui, nav_p1_li, nav_p1_gou, nav_p1_gui, nav_p1_dai, nav_p1_da;
	private View ll_navigation2_anim_part1;
	private TextView tv_content;
	private Animation animation;
	private Animation animation_part2;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.navigation2, container, false);
		createView(v);
		return v;
	}

	private void createView(View v) {
		iv_navigation2_anim_part2 = (ImageView) v.findViewById(R.id.iv_navigation2_anim_part2);
		tv_content = (TextView) v.findViewById(R.id.tv_content);
		LayoutParams params1 = (LayoutParams) tv_content.getLayoutParams();
		params1.setMargins(0, 0, 0, Config.getCaleHeight(90));
		tv_content.setLayoutParams(params1);
		tv_content.setTypeface(Config.getTypeface());

		nav_p1_ji = (ImageView) v.findViewById(R.id.nav_p1_ji);
		nav_p1_you = (ImageView) v.findViewById(R.id.nav_p1_you);
		nav_p1_hui = (ImageView) v.findViewById(R.id.nav_p1_hui);
		nav_p1_li = (ImageView) v.findViewById(R.id.nav_p1_li);
		nav_p1_gou = (ImageView) v.findViewById(R.id.nav_p1_gou);
		nav_p1_gui = (ImageView) v.findViewById(R.id.nav_p1_gui);
		nav_p1_dai = (ImageView) v.findViewById(R.id.nav_p1_dai);
		nav_p1_da = (ImageView) v.findViewById(R.id.nav_p1_da);

		ll_navigation2_anim_part1 = v.findViewById(R.id.ll_navigation2_anim_part1);

		animation = AnimationUtils.loadAnimation(mActivity, R.anim.navigation_slide_in_from_bottom);
		animation_part2 = AnimationUtils.loadAnimation(mActivity, R.anim.navigation_translate_scale_in);
	}

	Handler handler;

	public void startAnimation() {
		tv_content.setAnimation(animation);
		tv_content.setVisibility(View.VISIBLE);

		startPart1Anim();
	}

	public void stopAnimation() {
		tv_content.setVisibility(View.GONE);
		iv_navigation2_anim_part2.setVisibility(View.GONE);
		if (tv_content.getAnimation() != null) {
			tv_content.getAnimation().cancel();
		}
		if (iv_navigation2_anim_part2.getAnimation() != null) {
			iv_navigation2_anim_part2.getAnimation().cancel();
		}
	}

	private void startPart1Anim() {
		nav_p1_ji.setVisibility(View.VISIBLE);
		nav_p1_you.setVisibility(View.VISIBLE);
		nav_p1_hui.setVisibility(View.VISIBLE);
		nav_p1_li.setVisibility(View.VISIBLE);
		nav_p1_gou.setVisibility(View.VISIBLE);
		nav_p1_gui.setVisibility(View.VISIBLE);
		nav_p1_dai.setVisibility(View.VISIBLE);
		nav_p1_da.setVisibility(View.VISIBLE);
		ll_navigation2_anim_part1.setVisibility(View.VISIBLE);

		Animation animation1 = AnimationUtils.loadAnimation(mActivity, R.anim.navigation_translate_in_1);
		setAnimationListener(nav_p1_ji, animation1, 50);

		Animation animation2 = AnimationUtils.loadAnimation(mActivity, R.anim.navigation_translate_in_2);
		setAnimationListener(nav_p1_you, animation2, 250);

		Animation animation3 = AnimationUtils.loadAnimation(mActivity, R.anim.navigation_translate_in_3);
		setAnimationListener(nav_p1_hui, animation3, 500);

		Animation animation4 = AnimationUtils.loadAnimation(mActivity, R.anim.navigation_translate_in_4);
		setAnimationListener(nav_p1_li, animation4, 750);

		Animation animation5 = AnimationUtils.loadAnimation(mActivity, R.anim.navigation_translate_in_5);
		setAnimationListener(nav_p1_gou, animation5, 900);

		Animation animation6 = AnimationUtils.loadAnimation(mActivity, R.anim.navigation_translate_in_6);
		setAnimationListener(nav_p1_gui, animation6, 1150);

		Animation animation7 = AnimationUtils.loadAnimation(mActivity, R.anim.navigation_translate_in_7);
		setAnimationListener(nav_p1_dai, animation7, 1300);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				Animation animation8 = AnimationUtils.loadAnimation(mActivity, R.anim.navigation_translate_in_8);
				animation8.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationStart(Animation arg0) {
					}

					@Override
					public void onAnimationRepeat(Animation arg0) {
					}

					@Override
					public void onAnimationEnd(Animation arg0) {
						// TODO Auto-generated method stub
						nav_p1_da.setVisibility(View.GONE);
						ll_navigation2_anim_part1.setVisibility(View.GONE);

						iv_navigation2_anim_part2.setVisibility(View.VISIBLE);
						iv_navigation2_anim_part2.startAnimation(animation_part2);
					}
				});
				nav_p1_da.startAnimation(animation8);
			}
		}, 1600);
	}

	private void setAnimationListener(final ImageView imgView, final Animation anim, int delay) {
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				anim.setAnimationListener(new AnimationListener() {
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
						// TODO Auto-generated method stub
						imgView.setVisibility(View.GONE);
					}
				});
				imgView.setAnimation(anim);
			}
		}, delay);
	}
}
