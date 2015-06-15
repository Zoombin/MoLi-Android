package com.imooly.android.ui;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.adapter.OrderServiceStatusFragmentAdapter;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.fragment.OrderServiceStatusFragment;

/***
 * 售后状态 退货或列表
 * 
 * @author lsd
 * 
 */
public class OrderServiceStatusActivity extends BaseActivity implements OnClickListener {
	private RelativeLayout ll_title;
	private ImageView iv_back;
	private TextView tv_title;

	private RelativeLayout rl_optionmenu;
	private ImageView cursor;

	private TextView tv_return;
	private TextView tv_change;
	
	private ViewPager viewpager;

	int cursorW;
	int offset = 0;
	int currIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_sevice_status);

		logActivityName(this);

		initView();
		InitImageCursor();
		initViewPager();
	}

	private void initView() {
		// TODO Auto-generated method stub
		ll_title = (RelativeLayout) findViewById(R.id.ll_title);

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);

		tv_title = (TextView) findViewById(R.id.tv_title);

		// index选项
		rl_optionmenu = (RelativeLayout) findViewById(R.id.rl_optionmenu);
		cursor = (ImageView) findViewById(R.id.cursor);

		tv_return = (TextView) findViewById(R.id.tv_return);
		tv_return.setOnClickListener(OptionMenuClick);
		tv_return.setTag(0);

		tv_change = (TextView) findViewById(R.id.tv_change);
		tv_change.setOnClickListener(OptionMenuClick);
		tv_change.setTag(1);
		
		viewpager = (ViewPager) findViewById(R.id.viewpager);
	}

	private void InitImageCursor() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 屏幕宽度

		offset = 0;// 偏移量

		LayoutParams cursor_Params = cursor.getLayoutParams();
		cursorW = screenW / 2;// 游标宽度
		cursor_Params.width = cursorW;// 初始化滑动下标的宽
		cursor.setLayoutParams(cursor_Params);

		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);// 设置动画初始位置
	}
	
	private void initViewPager() {
		List<Fragment> list = new ArrayList<Fragment>();
		OrderServiceStatusFragment returnFragment = new OrderServiceStatusFragment("return");
		OrderServiceStatusFragment changeFragment = new OrderServiceStatusFragment("change");
		list.add(returnFragment);
		list.add(changeFragment);
		
		OrderServiceStatusFragmentAdapter adapter = new OrderServiceStatusFragmentAdapter(getSupportFragmentManager());
		viewpager.setAdapter(adapter);
		viewpager.setOnPageChangeListener(pageChange);
		adapter.setData(list);
		
		viewpager.setCurrentItem(0);
		tv_return.setSelected(true);
	}
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		default:
			break;
		}
	}
	
	
	OnClickListener OptionMenuClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int postion;
			switch (v.getId()) {
			case R.id.tv_return:
				postion = (Integer) v.getTag();
				viewpager.setCurrentItem(postion);
				break;
			case R.id.tv_change:
				postion = (Integer) v.getTag();
				viewpager.setCurrentItem(postion);
				break;
			}
		}
	};
	
	
	OnPageChangeListener pageChange = new OnPageChangeListener() {
		@Override
		public void onPageSelected(int position) {
			// TODO Auto-generated method stub
			optionChange(position);
			updateCurserStatus(position);
			refrashData(position);
			
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


	void optionChange(int position) {
		tv_return.setSelected(false);
		tv_change.setSelected(false);

		switch (position) {
		case 0:
			tv_return.setSelected(true);
			break;
		case 1:
			tv_change.setSelected(true);
			break;
		}
	}

	void updateCurserStatus(int position) {
		int one = offset * 2 + cursorW;
		Animation animation = null;
		animation = new TranslateAnimation(one * currIndex, position * one, 0, 0);
		currIndex = position;
		animation.setFillAfter(true);// True:图片停在动画结束位置
		animation.setDuration(300);
		cursor.startAnimation(animation);
	}
	
	
	void refrashData(int position) {
		OrderServiceStatusFragment fragment = (OrderServiceStatusFragment) ((OrderServiceStatusFragmentAdapter) viewpager.getAdapter())
				.getItem(position);
		fragment.getData(false);
	}

}
