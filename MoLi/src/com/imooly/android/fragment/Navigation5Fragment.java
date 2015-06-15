package com.imooly.android.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;

import com.imooly.android.R;
import com.imooly.android.base.BaseFragment;
import com.imooly.android.tool.Config;
import com.imooly.android.ui.MainActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 引导页5
 * 
 * @author daiye
 * 
 */
public class Navigation5Fragment extends BaseFragment {

	private ImageView iv_yun1;
	private ImageView iv_yun2;
	private ImageView iv_yun3;
	private TextView tv_content;
	Button btn_experience;
	// private SceneAnimation s;
	private Animation animation1;
	private Animation animationyun1;
	private Animation animationyun2;
	private Animation animationyun3;

	// private int[] pFrameRess = {
	// R.drawable.navigation_p5_01, R.drawable.navigation_p5_02,
	// R.drawable.navigation_p5_03, R.drawable.navigation_p5_04,
	// R.drawable.navigation_p5_05, R.drawable.navigation_p5_06
	// };

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.navigation5, container, false);
		createView(v);
		return v;
	}

	private void createView(View v) {
		tv_content = (TextView) v.findViewById(R.id.tv_content);
		LayoutParams params1 = (LayoutParams) tv_content.getLayoutParams();
		params1.setMargins(0, 0, 0, Config.getCaleHeight(68));
		tv_content.setLayoutParams(params1);
		tv_content.setTypeface(Config.getTypeface());
		
		iv_yun1 = (ImageView) v.findViewById(R.id.iv_yun1);
		LayoutParams params2 = new LayoutParams(Config.getCaleWidth(148), Config.getCaleHeight(94));
		params2.setMargins(0, Config.getCaleHeight(290), 0, 0);
		iv_yun1.setLayoutParams(params2);
		
		iv_yun2 = (ImageView) v.findViewById(R.id.iv_yun2);
		LayoutParams params3 = new LayoutParams(Config.getCaleWidth(169), Config.getCaleHeight(110));
		params3.setMargins(0, Config.getCaleHeight(480), 0, 0);
		iv_yun2.setLayoutParams(params3);
		
		iv_yun3 = (ImageView) v.findViewById(R.id.iv_yun3);
		LayoutParams params4 = new LayoutParams(Config.getCaleWidth(169), Config.getCaleHeight(110));
		params4.setMargins(0, Config.getCaleHeight(623), 0, 0);
		iv_yun3.setLayoutParams(params4);
		
		animation1 = AnimationUtils.loadAnimation(mActivity, R.anim.navigation_slide_in_from_bottom);

        btn_experience = (Button) v.findViewById(R.id.btn_experience);
		btn_experience.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Config.setTutorial(true);
				
				Intent intent = new Intent(mActivity, MainActivity.class);
				startActivity(intent);
				ImageLoader.getInstance().clearDiscCache();
				ImageLoader.getInstance().clearMemoryCache();
				System.gc();
				mActivity.finish();
			}
		});

		animationyun1 = AnimationUtils.loadAnimation(mActivity, R.anim.tutorail_yun);
		animationyun1.setDuration(5000);

		animationyun2 = AnimationUtils.loadAnimation(mActivity, R.anim.tutorail_yun);
		animationyun2.setDuration(4000);

		animationyun3 = AnimationUtils.loadAnimation(mActivity, R.anim.tutorail_yun);
		animationyun3.setDuration(3000);
	}

	public void startAnimation() {
		iv_yun1.startAnimation(animationyun1);
		iv_yun2.startAnimation(animationyun2);
		iv_yun3.startAnimation(animationyun3);

		tv_content.startAnimation(animation1);
		tv_content.setVisibility(View.VISIBLE);
	}

	public void stopAnimation() {
		iv_yun1.getAnimation().cancel();
		iv_yun2.getAnimation().cancel();
		iv_yun3.getAnimation().cancel();
		if (tv_content.getAnimation() != null) {
			tv_content.getAnimation().cancel();
		}
		
		tv_content.setVisibility(View.GONE);
	}
}
