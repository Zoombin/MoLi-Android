package com.imooly.android.fragment;

import java.util.List;

import android.content.Intent;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.Interface.RequestWebListener;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.adapter.HomeADAdapter;
import com.imooly.android.adapter.HomeBannerAdapter;
import com.imooly.android.adapter.HomeModulesAdapter;
import com.imooly.android.base.BaseFragment;
import com.imooly.android.entity.RspAdvertiseIndexads;
import com.imooly.android.entity.RspAdvertiseIndexads.Info;
import com.imooly.android.entity.RspAdvertiseIndexads.Tplcontent;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.tool.Config;
import com.imooly.android.ui.SearchActivity;
import com.imooly.android.widget.CannotRollGridView;
import com.imooly.android.widget.LoadingView;
import com.imooly.android.widget.autoscrollviewpager.AutoScrollViewPager;
import com.imooly.android.widget.pulltorefresh.PullToRefreshBase;
import com.imooly.android.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.imooly.android.widget.pulltorefresh.PullToRefreshScrollView;
import com.imooly.android.widget.viewpage.CirclePageIndicator;
import com.zbar.lib.CaptureActivity;

/**
 * 首页
 * 
 * @author daiye
 * 
 */
public class HomeFragment extends BaseFragment implements OnClickListener {

	private PullToRefreshScrollView layout_home;
	private LoadingView layout_loading;
	private EditText home_homepage_et_input;
	private Button btn_scan;
	private AutoScrollViewPager viewpager_banner;
	private CirclePageIndicator mIndicator;
	private CannotRollGridView gd_modules;
	private CannotRollGridView gd_ad;
	private TextView tv_vip;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_home, container, false);
		createView(v);
		initData();
		return v;
	}

	public void createView(View v) {
		layout_home = (PullToRefreshScrollView) v
				.findViewById(R.id.layout_home);
		layout_home.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				initData();
			}
		});

		layout_loading = (LoadingView) v.findViewById(R.id.layout_loading);

		home_homepage_et_input = (EditText) v
				.findViewById(R.id.home_homepage_et_input);
		home_homepage_et_input.setOnClickListener(this);

		btn_scan = (Button) v.findViewById(R.id.btn_scan);
		btn_scan.setOnClickListener(this);

		viewpager_banner = (AutoScrollViewPager) v
				.findViewById(R.id.viewpager_banner);

		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				Config.width, (int)(((float)Config.width / 750) * 188));
		viewpager_banner.setLayoutParams(layoutParams);
		mIndicator = (CirclePageIndicator) v.findViewById(R.id.indicator);
		mIndicator.setFillColor(
				Style.FILL,
				getResources().getColor(
						R.color.default_circle_indicator_fill_color));
		mIndicator.setStrokeColor(
				Style.FILL,
				getResources().getColor(
						R.color.default_circle_indicator_stroke_color));

		tv_vip = (TextView) v.findViewById(R.id.tv_vip);
		gd_modules = (CannotRollGridView) v.findViewById(R.id.gd_modules);
		gd_ad = (CannotRollGridView) v.findViewById(R.id.gd_ad);
	}

	private String BANNER_ID = "AD02-001-01-01";
	private String SHORTCUT_ID = "AD02-001-01-03";
	private String AD_ID = "AD02-001-01-02";

	private void initData() {
		Api.getAdvertiseIndexads(mActivity, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				layout_home.onRefreshComplete();

				layout_home.setVisibility(View.VISIBLE);
				layout_loading.setVisibility(View.GONE);
				layout_loading.postHandle(LoadingView.success);

				RspAdvertiseIndexads entity = (RspAdvertiseIndexads) rspData;
				RspAdvertiseIndexads.Data data = entity.data;
				Config.setHomeRspAdvertiseIndexads(entity);

				initView(data);
			}

			@Override
			public void failed(String msg) {
				layout_home.onRefreshComplete();

				RspAdvertiseIndexads rspadvertiseindexads = Config
						.getHomeRspAdvertiseIndexads();
				if (rspadvertiseindexads != null) {
					layout_home.setVisibility(View.VISIBLE);
					layout_loading.setVisibility(View.GONE);
					layout_loading.postHandle(LoadingView.success);

					initView(rspadvertiseindexads.data);
				} else {
					layout_loading.postHandle(LoadingView.network_error);
					layout_loading.setL(new RequestWebListener() {

						@Override
						public void requestWeb() {
							initData();
						}
					});
				}
			}
		}, RspAdvertiseIndexads.class);
	}

	private void initView(RspAdvertiseIndexads.Data data) {
		for (Tplcontent tplcontent : data.getTplcontent()) {
			List<Info> infos = tplcontent.getInfos();
			if (tplcontent.getAdid().equals(BANNER_ID)) {
				viewpager_banner.setAdapter(new HomeBannerAdapter(mActivity,
						infos));

				viewpager_banner.setInterval(4000);
				viewpager_banner.startAutoScroll();

				mIndicator.setViewPager(viewpager_banner, infos.size()
						* CirclePageIndicator.fornum / 2);
			} else if (tplcontent.getAdid().equals(SHORTCUT_ID)) {
				gd_modules.setAdapter(new HomeModulesAdapter(mActivity, infos));
			} else if (tplcontent.getAdid().equals(AD_ID)) {
				// 每行1张平铺
				if (tplcontent.getType().equals("T002")) {
					gd_ad.setNumColumns(1);
					gd_ad.setAdapter(new HomeADAdapter(mActivity, infos, true));
				}
				// 每行2张平铺
				else {
					gd_ad.setNumColumns(2);
					gd_ad.setAdapter(new HomeADAdapter(mActivity, infos, false));
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_scan:
			mActivity
					.startActivity(new Intent(mActivity, CaptureActivity.class));
			break;
		case R.id.home_homepage_et_input:
			Intent intent = new Intent(mActivity, SearchActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
}
