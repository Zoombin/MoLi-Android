package com.imooly.android.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
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
import com.imooly.android.adapter.ViewPaperAdapter;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.view.OrderPage;

/***
 * 订单
 * 
 * @author lsd
 * 
 */
public class OrderActivity extends BaseActivity implements OnClickListener {
	public static String PAGE_ALL = "pageAll";
	public static String PAGE_NONPAY = "pageNonpay";
	public static String PAGE_NONDELIVER = "pageNondeliver";
	public static String PAGE_NONHOLD = "pageNonhold";
	public static String PAGE_NONCOMMENT = "pageNoncomment";
	private RelativeLayout ll_title;
	private ImageView iv_back;
	private TextView tv_title;

	private RelativeLayout ll_order_optionmenu;
	private ViewPager viewpager;
	private ImageView cursor;

	private TextView tv_orderall, tv_order_nonpay, tv_order_nondeliver, tv_order_nonhold, tv_order_noncomment;

	OrderPage pageAll;// 所有订单
	OrderPage pageNonPay;// 待付款
	OrderPage pageNonDeliver;// 待发货
	OrderPage pageNonReceiver;// 待收货
	OrderPage pageNonComment;// 待评论

	int cursorW;
	int offset = 0;
	int currIndex = 0;
	boolean tabChange = false;//只有切换才显示移动动画

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order);

		logActivityName(this);

		initView();
		InitImageCursor();
		initViewPager();
		getIntentData();
	}

	private void getIntentData() {
		Intent intent = getIntent();
		if (intent.hasExtra("page")) {
			String page = intent.getStringExtra("page");
			if (pageAll.equals(page)) {
				setCurPage(0);
			}
			if (PAGE_NONPAY.equals(page)) {
				setCurPage(1);
			}
			if (PAGE_NONDELIVER.equals(page)) {
				setCurPage(2);
			}
			if (PAGE_NONHOLD.equals(page)) {
				setCurPage(3);
			}
			if (PAGE_NONCOMMENT.equals(page)) {
				setCurPage(4);
			}
		}
	}

	public void setCurPage(int page) {
		viewpager.setCurrentItem(page);
	}

	private void initView() {
		// app title
		ll_title = (RelativeLayout) findViewById(R.id.ll_title);

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);

		tv_title = (TextView) findViewById(R.id.tv_title);

		//
		ll_order_optionmenu = (RelativeLayout) findViewById(R.id.ll_order_optionmenu);

		viewpager = (ViewPager) findViewById(R.id.viewpager);
		cursor = (ImageView) findViewById(R.id.cursor);

		tv_orderall = (TextView) findViewById(R.id.tv_orderall);
		tv_orderall.setOnClickListener(OptionMenuClick);
		tv_orderall.setTag(0);

		tv_order_nonpay = (TextView) findViewById(R.id.tv_order_nonpay);
		tv_order_nonpay.setOnClickListener(OptionMenuClick);
		tv_order_nonpay.setTag(1);

		tv_order_nondeliver = (TextView) findViewById(R.id.tv_order_nondeliver);
		tv_order_nondeliver.setOnClickListener(OptionMenuClick);
		tv_order_nondeliver.setTag(2);

		tv_order_nonhold = (TextView) findViewById(R.id.tv_order_nonhold);
		tv_order_nonhold.setOnClickListener(OptionMenuClick);
		tv_order_nonhold.setTag(3);

		tv_order_noncomment = (TextView) findViewById(R.id.tv_order_noncomment);
		tv_order_noncomment.setOnClickListener(OptionMenuClick);
		tv_order_noncomment.setTag(4);
	}

	private void initViewPager() {
		List<View> views = new ArrayList<View>();

		pageAll = new OrderPage(self);
		pageNonPay = new OrderPage(self);
		pageNonDeliver = new OrderPage(self);
		pageNonReceiver = new OrderPage(self);
		pageNonComment = new OrderPage(self);

		views.add(pageAll);
		views.add(pageNonPay);
		views.add(pageNonDeliver);
		views.add(pageNonReceiver);
		views.add(pageNonComment);
		ViewPaperAdapter adapter = new ViewPaperAdapter(views);
		viewpager.setAdapter(adapter);
		viewpager.setOnPageChangeListener(pageChange);

		viewpager.setCurrentItem(0);
		tv_orderall.setSelected(true);
		pageAll.setData("");
	}

	private void InitImageCursor() {
		
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 屏幕宽度
		offset = 0;// 偏移量
		
		LayoutParams cursor_Params = cursor.getLayoutParams();
		cursorW = screenW / 5;// 游标宽度
		cursor_Params.width = cursorW;// 初始化滑动下标的宽
		cursor.setLayoutParams(cursor_Params);
		
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);// 设置动画初始位置
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
			tabChange = true;
			switch (v.getId()) {
			case R.id.tv_orderall:
				currIndex = (Integer) v.getTag();
				viewpager.setCurrentItem(currIndex);
				break;
			case R.id.tv_order_nonpay:
				currIndex = (Integer) v.getTag();
				viewpager.setCurrentItem(currIndex);
				break;
			case R.id.tv_order_nondeliver:
				currIndex = (Integer) v.getTag();
				viewpager.setCurrentItem(currIndex);
				break;
			case R.id.tv_order_nonhold:
				currIndex = (Integer) v.getTag();
				viewpager.setCurrentItem(currIndex);
				break;
			case R.id.tv_order_noncomment:
				currIndex = (Integer) v.getTag();
				viewpager.setCurrentItem(currIndex);
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
			updataPageStatus(position);
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
		tv_orderall.setSelected(false);
		tv_order_noncomment.setSelected(false);
		tv_order_nondeliver.setSelected(false);
		tv_order_nonhold.setSelected(false);
		tv_order_nonpay.setSelected(false);

		switch (position) {
		case 0:
			tv_orderall.setSelected(true);
			break;
		case 1:
			tv_order_nonpay.setSelected(true);
			break;
		case 2:
			tv_order_nondeliver.setSelected(true);
			break;
		case 3:
			tv_order_nonhold.setSelected(true);
			break;
		case 4:
			tv_order_noncomment.setSelected(true);
			break;
		}
	}

	private void updateCurserStatus(int position) {
		int one = offset * 2 + cursorW;
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
		animation.setFillAfter(true);// True:图片停在动画结束位置
		if(tabChange){
			//只有tab切换才有动画
			animation.setDuration(500);
		}else{
			animation.setDuration(1);
		}
		cursor.startAnimation(animation);
	}

	private void updataPageStatus(int position) {
		switch (position) {
		case 0:
			pageAll.setData("");
			break;
		case 1:
			pageNonPay.setData("forpay");
			break;
		case 2:
			pageNonDeliver.setData("forsend");
			break;
		case 3:
			pageNonReceiver.setData("fortake");
			break;
		case 4:
			pageNonComment.setData("forcomment");
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode != RESULT_OK) {
			return;
		}
		int cur = viewpager.getCurrentItem();
		if (4 == cur) {
			//评价后，待评价页面刷新
			pageNonComment.getData(false);
		}
		if (0 == cur) {
			//评价后，待评价页面刷新
			pageAll.getData(false);
		}
	}

}
