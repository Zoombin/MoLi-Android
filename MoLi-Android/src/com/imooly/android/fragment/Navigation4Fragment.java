package com.imooly.android.fragment;

import android.graphics.drawable.AnimationDrawable;
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
 * 引导页4
 * 
 * @author daiye
 * 
 */
public class Navigation4Fragment extends BaseFragment {

	private ImageView iv_navigation4_anim;
	private ImageView iv_content;
	private SceneAnimation s;
	private Animation animation;
	
	private int[] pFrameRess = {
            R.drawable.navigation_p4_04, R.drawable.navigation_p4_05,
            R.drawable.navigation_p4_06
    };
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.navigation4, container, false);
		createView(v);
		return v;
	}

	private void createView(View v) {
		iv_navigation4_anim = (ImageView) v .findViewById(R.id.iv_navigation4_anim);
		iv_content = (ImageView) v .findViewById(R.id.iv_content);
		animation = AnimationUtils.loadAnimation(mActivity,
				R.anim.navigation_slide_in_from_bottom);
	}
	
	public void startAnimation() {
		s = new SceneAnimation(iv_navigation4_anim, pFrameRess, 200);
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
