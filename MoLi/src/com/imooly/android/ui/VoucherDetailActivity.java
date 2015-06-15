package com.imooly.android.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.adapter.VoucherFragmentAdapter;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.fragment.VoucherAllFragment;
import com.imooly.android.fragment.VoucherConsumedFragment;
import com.imooly.android.fragment.VoucherGotFragment;

public class VoucherDetailActivity extends BaseActivity implements OnClickListener {
	private FrameLayout rl_order_optionmenu;
	private ImageView button_back;
	private ViewPager viewpager;
	private TextView text_all, text_had, text_consume;
	private ImageView cursor;
	private int offset;
	private int currIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vouncher_detail);

		logActivityName(this);
		
		initView();
		InitImageCursor();
		initViewPager();
	}

	private void initView() {
		button_back = (ImageView) findViewById(R.id.button_back);
		button_back.setOnClickListener(this);
		
		rl_order_optionmenu = (FrameLayout) findViewById(R.id.rl_order_optionmenu);

		// 全部
		text_all = (TextView) findViewById(R.id.text_all);
		text_all.setOnClickListener(OptionMenuClick);
		text_all.setTag(0);

		// 获得
		text_had = (TextView) findViewById(R.id.text_had);
		text_had.setOnClickListener(OptionMenuClick);
		text_had.setTag(1);

		// 消费
		text_consume = (TextView) findViewById(R.id.text_cosume);
		text_consume.setOnClickListener(OptionMenuClick);
		text_consume.setTag(2);

	}

	private void initViewPager() {
		List<Fragment> fragments = new ArrayList<Fragment>();
		fragments.add(new VoucherAllFragment());
		fragments.add(new VoucherGotFragment());
		fragments.add(new VoucherConsumedFragment());
		
		FragmentManager fm = this.getSupportFragmentManager();

		viewpager = (ViewPager) findViewById(R.id.viewpager_vouncher);
		VoucherFragmentAdapter adapter = new VoucherFragmentAdapter(fm, fragments);
		viewpager.setAdapter(adapter);
		viewpager.setOnPageChangeListener(pageChange);

		viewpager.setAdapter(adapter);
		viewpager.setCurrentItem(0);
		text_all.setSelected(true);
		text_all.setTextColor(getResources().getColor(R.color.main_color));
	}

	private void InitImageCursor() {


		cursor = (ImageView) findViewById(R.id.cursor);
		offset = width / 3;
		
		LayoutParams cursor_Params = cursor.getLayoutParams();
		cursor_Params.width = offset;
		

		cursor.setLayoutParams(cursor_Params);
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);// 设置动画初始位置
		
		
	}

	OnClickListener OptionMenuClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub

			switch (v.getId()) {
			case R.id.text_all:
				currIndex = (Integer) v.getTag();
				viewpager.setCurrentItem(currIndex);
				Log.d("xxx", "text_all click---------");

				break;

			case R.id.text_had:
				currIndex = (Integer) v.getTag();
				viewpager.setCurrentItem(currIndex);
				Log.d("xxx", "text_had click---------");

				break;

			case R.id.text_cosume:
				currIndex = (Integer) v.getTag();
				viewpager.setCurrentItem(currIndex);
				Log.d("xxx", "text_cosume click---------");

				break;
			}
		}
	};

	OnPageChangeListener pageChange = new OnPageChangeListener() {

		@Override
		public void onPageSelected(int position) {
			// TODO Auto-generated method stub
			updateOptionMenuStatus(position);
			updateCurserStatus(position);
			// updataPageStatus(position);

		}

		@Override
		public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onPageScrollStateChanged(int state) {
			// TODO Auto-generated method stub

		}
	};

	private void updateOptionMenuStatus(int position) {
		text_all.setSelected(false);
		text_consume.setSelected(false);
		text_had.setSelected(false);
		text_all.setTextColor(getResources().getColor(R.color.app_text_gray));
		text_had.setTextColor(getResources().getColor(R.color.app_text_gray));
		text_consume.setTextColor(getResources().getColor(R.color.app_text_gray));

		switch (position) {
		case 0:
			text_all.setSelected(true);
			text_all.setTextColor(getResources().getColor(R.color.main_color));
			break;
		case 1:
			text_had.setSelected(true);
			text_had.setTextColor(getResources().getColor(R.color.main_color));
			break;
		case 2:
			text_consume.setSelected(true);
			text_consume.setTextColor(getResources().getColor(R.color.main_color));
			break;
		}
	}

	private void updateCurserStatus(int position) {
		int one = offset;

		Animation animation = null;

		switch (position) {
		case 0:
			animation = new TranslateAnimation(one * currIndex, position * one, 0, 0);
			break;
		case 1:
			animation = new TranslateAnimation(one * currIndex, position * one, 0, 0);
			break;
		case 2:
			animation = new TranslateAnimation(one * currIndex, position * one, 0, 0);
			break;
		case 3:
			animation = new TranslateAnimation(one * currIndex, position * one, 0, 0);
			break;
		case 4:
			animation = new TranslateAnimation(one * currIndex, position * one, 0, 0);
			break;
		}

		currIndex = position;
		animation.setFillAfter(true); // True:图片停在动画结束位置
		animation.setDuration(300);
		cursor.startAnimation(animation);
	}
	
	/**推送进来的特别处理*/
	private void goBack(){
		String pushAction = getIntent().getStringExtra("pushAction");
		if(TextUtils.isEmpty(pushAction)){
			finish();
		}else{
			Intent intent = new Intent(MainActivity.ACTION);
			sendBroadcast(intent);			
			startActivity(new Intent(self,MainActivity.class));
			finish();
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		goBack();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.button_back:
			goBack();
			break;

		default:
			break;
		}

	}
}
