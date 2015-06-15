package com.imooly.android.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.adapter.StoreHotListAdapter;
import com.imooly.android.adapter.StoreModulesAdapter;
import com.imooly.android.adapter.StoreRandListAdapter;
import com.imooly.android.base.BaseFragment;
import com.imooly.android.db.DB_Location;
import com.imooly.android.db.DataHelper;
import com.imooly.android.entity.RspAdvertiseIndexads;
import com.imooly.android.entity.RspAdvertiseIndexads.Info;
import com.imooly.android.entity.RspAdvertiseIndexads.Tplcontent;
import com.imooly.android.entity.RspBusinessCirclelist;
import com.imooly.android.entity.RspBusinessCirclelist.Circle;
import com.imooly.android.entity.RspBusinessSearch.BusinessEty;
import com.imooly.android.entity.RspBusinessStoreList;
import com.imooly.android.entity.RspStoreCityList;
import com.imooly.android.entity.RspStoreCityList.City;
import com.imooly.android.entity.RspStoreCityList.CityGroup;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.tool.Config;
import com.imooly.android.ui.SearchMapActivity;
import com.imooly.android.ui.SearchResultActivity;
import com.imooly.android.ui.StoreDetailActivity;
import com.imooly.android.ui.StoreSearchActivity;
import com.imooly.android.ui.SearchActivity.TabFragmentPagerAdapter;
import com.imooly.android.utils.Utils;
import com.imooly.android.view.StoreAddressSelectDialog;
import com.imooly.android.view.StoreAddressSelectDialog.AddressSelectCallBack;
import com.imooly.android.widget.CannotRollGridView;
import com.imooly.android.widget.CannotRollListView;
import com.imooly.android.widget.LoadingView;
import com.imooly.android.widget.NoDataView;
import com.imooly.android.widget.ObservableScrollView;
import com.imooly.android.widget.Toast;
import com.imooly.android.widget.ObservableScrollView.OnScrollListener;

/***
 * 实体店
 * 
 * @author LSD
 * 
 */
public class StoreFragment extends BaseFragment implements OnClickListener {
	private Resources resources;
	private ImageView iv_back;
	private EditText edit_search;
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
	private View view_blank;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_search, container, false);
		resources = getResources();
		initView(view);
		setListener();
		mViewPager.setCurrentItem(0);
		return view;
	}

	private void initView(View v) {
		iv_back = (ImageView) v.findViewById(R.id.iv_back);
//		iv_back.setOnClickListener(this);
		iv_back.setVisibility(View.GONE);

		view_blank = (View) v.findViewById(R.id.view_blank);
		view_blank.setVisibility(View.VISIBLE);
		
		login_code_del_imageView = (TextView) v.findViewById(R.id.login_code_del_imageView);

		edit_search = (EditText) v.findViewById(R.id.home_homepage_et_input);
		edit_search.setOnEditorActionListener(new OnEditorActionListener() {
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
		
		// 获取输入光标
		edit_search.requestFocus();
		
		edit_search.addTextChangedListener(new TextWatcher() {

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

		btn_search = (TextView) v.findViewById(R.id.btn_search);
		btn_search.setOnClickListener(this);

		layout_history = (LinearLayout) v.findViewById(R.id.layout_history);
		layout_history.setOnClickListener(this);
		layout_hot = (LinearLayout) v.findViewById(R.id.layout_hot);
		layout_hot.setOnClickListener(this);
		iv_search_history = (ImageView) v.findViewById(R.id.iv_search_history);
		tv_recently_search_tab = (TextView) v.findViewById(R.id.tv_recently_search_tab);
		iv_search_hot = (ImageView) v.findViewById(R.id.iv_search_hot);
		tv_hot_search_tab = (TextView) v.findViewById(R.id.tv_hot_search_tab);

		iv_nav_indicator = (ImageView) v.findViewById(R.id.iv_nav_indicator);
		mViewPager = (ViewPager) v.findViewById(R.id.mViewPager);

		DisplayMetrics dm = new DisplayMetrics();
		mActivity.getWindowManager().getDefaultDisplay().getMetrics(dm);

		indicatorWidth = dm.widthPixels / 2;

		LayoutParams cursor_Params = iv_nav_indicator.getLayoutParams();
		cursor_Params.width = indicatorWidth;// 初始化滑动下标的宽
		iv_nav_indicator.setLayoutParams(cursor_Params);

		mAdapter = new TabFragmentPagerAdapter(mActivity.getSupportFragmentManager());
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
//		case R.id.iv_back:
//			finish();
//			break;
		case R.id.login_code_del_imageView:
			edit_search.setText("");
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
		Intent intent = new Intent(mActivity, SearchResultActivity.class);
		String keyword = edit_search.getText().toString();
		if (TextUtils.isEmpty(keyword)) {
			Toast.show(mActivity, "请输入商品名！");
			return;
		}
		DataHelper.getInstance().SaveOrUpdateOrder(keyword, ((Long) System.currentTimeMillis()).intValue());
		intent.putExtra(SearchResultActivity.EXTRA_KEYWORD, keyword);
		startActivity(intent);
	}
}
