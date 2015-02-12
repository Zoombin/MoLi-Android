package com.imooly.android.fragment;

import android.os.Bundle;
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
 * 引导页1
 * 
 * @author daiye
 * 
 */
public class Navigation1Fragment extends BaseFragment {

	private ImageView iv_navigation1_anim;
	private ImageView iv_content;
	private SceneAnimation s;
	private Animation animation;
	
	private int[] pFrameRess = {
            R.drawable.navigation_p1_06, R.drawable.navigation_p1_07,
            R.drawable.navigation_p1_08, R.drawable.navigation_p1_09,
            R.drawable.navigation_p1_10, R.drawable.navigation_p1_11
    };
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.navigation1, container, false);
		createView(v);
		return v;
	}

	private void createView(View v) {
		iv_navigation1_anim = (ImageView) v .findViewById(R.id.iv_navigation1_anim);
		iv_content = (ImageView) v .findViewById(R.id.iv_content);
		animation = AnimationUtils.loadAnimation(mActivity,
				R.anim.navigation_slide_in_from_bottom);
		startAnimation();
	}
	
	public void startAnimation() {
		s = new SceneAnimation(iv_navigation1_anim, pFrameRess, 100);
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
