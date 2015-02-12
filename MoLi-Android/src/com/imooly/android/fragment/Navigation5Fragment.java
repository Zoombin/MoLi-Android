package com.imooly.android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.imooly.android.R;
import com.imooly.android.base.BaseFragment;
import com.imooly.android.tool.Config;
import com.imooly.android.ui.MainActivity;
import com.imooly.android.widget.SceneAnimation;

/**
 * 引导页5
 * 
 * @author daiye
 * 
 */
public class Navigation5Fragment extends BaseFragment {

	private ImageView iv_navigation5_anim;
	private ImageView iv_content;
	private SceneAnimation s;
	private Animation animation;
	
	private int[] pFrameRess = {
            R.drawable.navigation_p5_01, R.drawable.navigation_p5_02,
            R.drawable.navigation_p5_03, R.drawable.navigation_p5_04,
            R.drawable.navigation_p5_05, R.drawable.navigation_p5_06
    };
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.navigation5, container, false);
		createView(v);
		return v;
	}

	private void createView(View v) {
		iv_navigation5_anim = (ImageView) v.findViewById(R.id.iv_navigation5_anim);
		iv_content = (ImageView) v .findViewById(R.id.iv_content);
		animation = AnimationUtils.loadAnimation(mActivity,
				R.anim.navigation_slide_in_from_bottom);
		
		Button btn_experience = (Button) v.findViewById(R.id.btn_experience);
		btn_experience.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Config.setTutorial(true);

				Intent intent = new Intent(mActivity, MainActivity.class);
				startActivity(intent);
				mActivity.finish();
			}
		});
	}
	
	public void startAnimation() {
		s = new SceneAnimation(iv_navigation5_anim, pFrameRess, 200);
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
