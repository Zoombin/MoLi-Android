package com.imooly.android.fragment;

import java.util.List;

import android.content.Intent;
import android.graphics.Paint.Style;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.imooly.android.R;
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
import com.imooly.android.tool.InitUtil;
import com.imooly.android.tool.InitUtil.InitCallBack;
import com.imooly.android.ui.SearchActivity;
import com.imooly.android.widget.CannotRollGridView;
import com.imooly.android.widget.NoDataView;
import com.imooly.android.widget.Toast;
import com.imooly.android.widget.autoscrollviewpager.AutoScrollViewPager;
import com.imooly.android.widget.in.srain.cube.views.ptr.PtrClassicFrameLayout;
import com.imooly.android.widget.in.srain.cube.views.ptr.PtrClassicHeader;
import com.imooly.android.widget.in.srain.cube.views.ptr.PtrDefaultHandler;
import com.imooly.android.widget.in.srain.cube.views.ptr.PtrFrameLayout;
import com.imooly.android.widget.in.srain.cube.views.ptr.PtrHandler;
import com.imooly.android.widget.viewpage.CirclePageIndicator;
import com.zbar.lib.CaptureActivity;


/**
 * 首页
 * 
 * @author daiye
 * 
 */
public class HomeFragment extends BaseFragment implements OnClickListener {

	private PtrClassicFrameLayout layout_home;
	private NoDataView layout_nodata_home;
	private EditText mEditText;
	private Button btn_scan;
	private AutoScrollViewPager mViewPager;
	private CirclePageIndicator mIndicator;
	private CannotRollGridView gd_modules;
	private CannotRollGridView gd_ad;
	private TextView tv_vip;
	private LinearLayout contentLayout;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_home, container, false);
		createView(v);
		
		if (Config.getHomeRspAdvertiseIndexads() != null) {
			initView(Config.getHomeRspAdvertiseIndexads().data);
		}
		
		firstLoad();

		return v;
	}
	
	private void firstLoad(){
		layout_home.setLoadingMinTime(1500);
		layout_home.postDelayed(new Runnable() {
			@Override
			public void run() {
				layout_home.autoRefresh(true);
			}
		}, 150);
	}

	public void createView(View v) {
		layout_home = (PtrClassicFrameLayout) v.findViewById(R.id.layout_home);
		layout_nodata_home = (NoDataView) v.findViewById(R.id.layout_nodata_home);
		layout_nodata_home.postHandle(NoDataView.defult);
		
		PtrClassicHeader  header = new PtrClassicHeader(mActivity);
		//scroll_shopcart.setLastUpdateTimeRelateObject(this);//最近更新时间
		layout_home.setHeaderView(header);
		layout_home.addPtrUIHandler(header);
		layout_home.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
            	initData();
            }

			@Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });

		mEditText = (EditText) v.findViewById(R.id.home_homepage_et_input);
		mEditText.setOnClickListener(this); 

		btn_scan = (Button) v.findViewById(R.id.btn_scan);
		btn_scan.setOnClickListener(this);

		mViewPager = (AutoScrollViewPager) v.findViewById(R.id.viewpager_banner);

		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
				Config.width, (int)(((float)Config.width / 750) * 188));
		mViewPager.setLayoutParams(layoutParams);
		mIndicator = (CirclePageIndicator) v.findViewById(R.id.home_page_indicator);
		
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
				layout_home.refreshComplete();
				layout_home.setVisibility(View.VISIBLE);
				layout_nodata_home.postHandle(NoDataView.defult);

				RspAdvertiseIndexads entity = (RspAdvertiseIndexads) rspData;
				RspAdvertiseIndexads.Data data = entity.data;
				Config.setHomeRspAdvertiseIndexads(entity);

				initView(data);
			}

			@Override
			public void failed(String msg) {
				layout_home.refreshComplete();

				RspAdvertiseIndexads rspadvertiseindexads = Config
						.getHomeRspAdvertiseIndexads();
				if (rspadvertiseindexads != null) {
					layout_home.setVisibility(View.VISIBLE);
					layout_nodata_home.postHandle(NoDataView.defult);
					initView(rspadvertiseindexads.data);
				} else {
					layout_home.setVisibility(View.GONE);
					layout_nodata_home.postHandle(NoDataView.nonet);
					layout_nodata_home.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							// 没有网络进来的时候初始化
							if (TextUtils.isEmpty(Config.getAppID())) {
								new InitUtil(mActivity).start(new InitCallBack() {
									@Override
									public void success() {
										// TODO Auto-generated method stub
										initData();
									}

									@Override
									public void failed(String msg) {
										// TODO Auto-generated method stub
										Toast.show(mActivity, "网络连接异常");
										layout_nodata_home.postHandle(NoDataView.nonet);
									}
								});
								return;
							}
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
				mViewPager.setAdapter(new HomeBannerAdapter(mActivity,
						infos));

				mViewPager.setInterval(4000);
				mViewPager.startAutoScroll();

				mIndicator.setViewPager(mViewPager, infos.size()
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
			mActivity.startActivity(new Intent(mActivity, CaptureActivity.class));
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
