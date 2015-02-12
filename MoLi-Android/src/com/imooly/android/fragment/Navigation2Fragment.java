package com.imooly.android.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.imooly.android.R;
import com.imooly.android.base.BaseFragment;
import com.imooly.android.widget.SceneAnimation;

/**
 * 引导页2
 * 
 * @author daiye
 * 
 */
public class Navigation2Fragment extends BaseFragment {

	private ImageView iv_navigation2_anim;
	private ImageView iv_content;
	private SceneAnimation s;
	private Animation animation;
	
	private int[] pFrameRess = {
            R.drawable.navigation_p2_01, R.drawable.navigation_p2_02,
            R.drawable.navigation_p2_03, R.drawable.navigation_p2_04,
            R.drawable.navigation_p2_05, R.drawable.navigation_p2_06,
            R.drawable.navigation_p2_07, R.drawable.navigation_p2_08,
            R.drawable.navigation_p2_09, R.drawable.navigation_p2_10,
            R.drawable.navigation_p2_11, R.drawable.navigation_p2_12,
            R.drawable.navigation_p2_13, R.drawable.navigation_p2_14,
            R.drawable.navigation_p2_15, R.drawable.navigation_p2_16,
            R.drawable.navigation_p2_17, R.drawable.navigation_p2_18,
            R.drawable.navigation_p2_19, R.drawable.navigation_p2_20,
            R.drawable.navigation_p2_21, R.drawable.navigation_p2_22,
            R.drawable.navigation_p2_23, R.drawable.navigation_p2_24
    };
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.navigation2, container, false);
		createView(v);
		return v;
	}

	private void createView(View v) {
		iv_navigation2_anim = (ImageView) v .findViewById(R.id.iv_navigation2_anim);
		iv_content = (ImageView) v .findViewById(R.id.iv_content);
		animation = AnimationUtils.loadAnimation(mActivity,
				R.anim.navigation_slide_in_from_bottom);
	}
	
	Handler handler;
	public void startAnimation() {
		s = new SceneAnimation(iv_navigation2_anim, pFrameRess, 100);
		s.setLoop(false);
		iv_content.setAnimation(animation);
		iv_content.setVisibility(View.VISIBLE);
	}
	
	public void stopAnimation() {
		iv_content.setVisibility(View.GONE);
		if (s != null) { 
			s.setLoop(false);
		}
	}
}
