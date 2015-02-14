package com.imooly.android.ui;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.imooly.android.R;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.db.DataHelper;
import com.imooly.android.fragment.HistoryFragment;
import com.imooly.android.fragment.RecommendFragment;
import com.imooly.android.widget.Toast;

/**
 * 搜索页面
 * 
 * @author daiye
 * 
 */
public class SearchActivity extends BaseActivity implements OnClickListener {

	private Resources resources;
	private ImageView iv_back;
	private EditText home_homepage_et_input;
	private TextView login_code_del_imageView;
	private TextView btn_search;
	private LinearLayout layout_history;
	private LinearLayout layout_hot;
	private ImageView iv_search_history;
	private TextView tv_recently_search_tab;
	private ImageView iv_search_hot;
	private TextView tv_hot_search_tab;
	private ImageView iv_nav_indicator;
	private ViewPager mViewPager;
	private int indicatorWidth;
	private TabFragmentPagerAdapter mAdapter;
	private static final int history_index = 0;
	private static final int recommend_index = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);

		logActivityName(this);

		resources = getResources();
		initView();
		setListener();
		mViewPager.setCurrentItem(0);
	}

	private void initView() {
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);

		login_code_del_imageView = (TextView) findViewById(R.id.login_code_del_imageView);

		home_homepage_et_input = (EditText) findViewById(R.id.home_homepage_et_input);
		home_homepage_et_input.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				switch (actionId) {
				case EditorInfo.IME_ACTION_SEARCH:
					search();
					break;
				}
				return true;
			}
		});
		home_homepage_et_input.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence c, int arg1, int arg2, int arg3) {
				if (TextUtils.isEmpty(c)) {
					login_code_del_imageView.setVisibility(View.GONE);
				} else {
					login_code_del_imageView.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {

			}

			@Override
			public void afterTextChanged(Editable arg0) {

			}
		});

		login_code_del_imageView.setOnClickListener(this);

		btn_search = (TextView) findViewById(R.id.btn_search);
		btn_search.setOnClickListener(this);

		layout_history = (LinearLayout) findViewById(R.id.layout_history);
		layout_history.setOnClickListener(this);
		layout_hot = (LinearLayout) findViewById(R.id.layout_hot);
		layout_hot.setOnClickListener(this);
		iv_search_history = (ImageView) findViewById(R.id.iv_search_history);
		tv_recently_search_tab = (TextView) findViewById(R.id.tv_recently_search_tab);
		iv_search_hot = (ImageView) findViewById(R.id.iv_search_hot);
		tv_hot_search_tab = (TextView) findViewById(R.id.tv_hot_search_tab);

		iv_nav_indicator = (ImageView) findViewById(R.id.iv_nav_indicator);
		mViewPager = (ViewPager) findViewById(R.id.mViewPager);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);

		indicatorWidth = dm.widthPixels / 2;

		LayoutParams cursor_Params = iv_nav_indicator.getLayoutParams();
		cursor_Params.width = indicatorWidth;// 初始化滑动下标的宽
		iv_nav_indicator.setLayoutParams(cursor_Params);

		mAdapter = new TabFragmentPagerAdapter(getSupportFragmentManager());
		mViewPager.setOffscreenPageLimit(2);
		mViewPager.setAdapter(mAdapter);
	}

	private void setListener() {
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {

			float fromXDelta;
			float toXDelta;

			@Override
			public void onPageSelected(int position) {
				if (position == history_index) {
					iv_search_history.setImageResource(R.drawable.search_history_pressed);
					tv_recently_search_tab.setTextColor(resources.getColor(R.color.main_color));
					iv_search_hot.setImageResource(R.drawable.search_hot_normal);
					tv_hot_search_tab.setTextColor(resources.getColor(R.color.app_text_dark_gray));
					fromXDelta = layout_history.getWidth() + 1;
					toXDelta = 0f;
				} else {
					iv_search_history.setImageResource(R.drawable.search_history_normal);
					tv_recently_search_tab.setTextColor(resources.getColor(R.color.app_text_dark_gray));
					iv_search_hot.setImageResource(R.drawable.search_hot_pressed);
					tv_hot_search_tab.setTextColor(resources.getColor(R.color.main_color));
					fromXDelta = 0f;
					toXDelta = layout_history.getWidth() + 1;
				}

				TranslateAnimation animation = new TranslateAnimation(fromXDelta, toXDelta, 0f, 0f);
				animation.setInterpolator(new LinearInterpolator());
				animation.setDuration(100);
				animation.setFillAfter(true);

				// 执行位移动画
				iv_nav_indicator.startAnimation(animation);
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {

			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}
		});
	}

	public static class TabFragmentPagerAdapter extends FragmentPagerAdapter {

		Fragment recommend = null;
		Fragment history = null;

		public TabFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int index) {

			switch (index) {
			case history_index:
				if (history == null) {
					history = new HistoryFragment();
				}
				return history;
			case recommend_index:
				if (recommend == null) {
					recommend = new RecommendFragment();
				}
				return recommend;
			default:

				break;
			}
			return null;
		}

		@Override
		public int getCount() {
			return 2;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.login_code_del_imageView:
			home_homepage_et_input.setText("");
			break;
		case R.id.layout_history:
			mViewPager.setCurrentItem(history_index);
			break;
		case R.id.layout_hot:
			mViewPager.setCurrentItem(recommend_index);
			break;
		case R.id.btn_search:
			search();
			break;
		default:
			break;
		}
	}
	
	private void search(){
		Intent intent = new Intent(self, SearchResultActivity.class);
		String keyword = home_homepage_et_input.getText().toString();
		if (TextUtils.isEmpty(keyword)) {
			Toast.show(self, "请输入商品名！");
			return;
		}
		DataHelper.getInstance().SaveOrUpdateOrder(keyword, ((Long) System.currentTimeMillis()).intValue());
		intent.putExtra(SearchResultActivity.EXTRA_KEYWORD, keyword);
		startActivity(intent);
	}
}
