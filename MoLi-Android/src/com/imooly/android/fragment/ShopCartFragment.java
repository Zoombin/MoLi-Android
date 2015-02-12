package com.imooly.android.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.imooly.android.MoLiApplication;
import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.base.BaseFragment;
import com.imooly.android.db.DB_Location;
import com.imooly.android.entity.ReqGoodsInfo;
import com.imooly.android.entity.RspCartSync;
import com.imooly.android.entity.RspCartSync.Good;
import com.imooly.android.entity.RspCartSync.Goods;
import com.imooly.android.entity.RspOrderMake;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.tool.Config;
import com.imooly.android.ui.FillOrderActivity;
import com.imooly.android.ui.LoginActivity;
import com.imooly.android.ui.MainActivity;
import com.imooly.android.utils.Utils;
import com.imooly.android.view.MembershipDialog;
import com.imooly.android.view.ShopCartView;
import com.imooly.android.widget.LoadingView;
import com.imooly.android.widget.NoDataView;
import com.imooly.android.widget.Toast;
import com.imooly.android.widget.pulltorefresh.PullToRefreshBase;
import com.imooly.android.widget.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.imooly.android.widget.pulltorefresh.PullToRefreshScrollView;

public class ShopCartFragment extends BaseFragment implements OnClickListener {

	public LinearLayout layout_shopcart;
	private PullToRefreshScrollView scroll_shopcart;
	private LoadingView layout_loading;

	private RelativeLayout ll_title_mycenter;
	private TextView tv_title;
	private TextView tv_right;

	private LinearLayout ll_content;

	private LinearLayout calculate_price_layout;
	private LinearLayout check_layout;
	private CheckBox shopcar_checkall;
	private TextView shopcar_all_price;
	public Button shopcar_commit_bt;

	public static boolean isEdit = false;

	public ShopCartView shopcartView;
	public NoDataView noDataView;
	private int page = 1;
	private int pagesize = 30;
	private OnRefreshListener<ScrollView> onrefreshlistener;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_shop_cart, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
		initData();
	}

	public void refrashData() {
		scroll_shopcart.setRefreshing();
		onrefreshlistener.onRefresh(scroll_shopcart);
	}

	private void initView() {
		scroll_shopcart = (PullToRefreshScrollView) mActivity.findViewById(R.id.scroll_shopcart);
		onrefreshlistener = new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				initData();
			}
		};
		scroll_shopcart.setOnRefreshListener(onrefreshlistener);
		noDataView = (NoDataView) mActivity.findViewById(R.id.layout_nodata);
		noDataView.setOnClickListener(listener);
		layout_shopcart = (LinearLayout) mActivity.findViewById(R.id.layout_shopcart);
		layout_loading = (LoadingView) mActivity.findViewById(R.id.layout_loading);

		ll_title_mycenter = (RelativeLayout) mActivity.findViewById(R.id.ll_title_mycenter);
		// fragment重叠bug
		ll_title_mycenter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 不做操作
			}
		});

		tv_right = (TextView) mActivity.findViewById(R.id.tv_right);
		tv_right.setOnClickListener(this);

		check_layout = (LinearLayout) mActivity.findViewById(R.id.check_layout);
		check_layout.setSelected(false);
		check_layout.setOnClickListener(this);
		shopcar_checkall = (CheckBox) mActivity.findViewById(R.id.shopcar_checkall);
		//shopcar_checkall.setOnCheckedChangeListener(checkChanged);

		calculate_price_layout = (LinearLayout) mActivity.findViewById(R.id.calculate_price_layout);
		shopcar_all_price = (TextView) mActivity.findViewById(R.id.shopcar_all_price);
		shopcar_commit_bt = (Button) mActivity.findViewById(R.id.shopcar_commit_bt);
		shopcar_commit_bt.setOnClickListener(this);

		ll_content = (LinearLayout) mActivity.findViewById(R.id.ll_content);
	}

	public void initData() {
		String lastsynctime = Config.getLastsynctime();
		// 初始化数据
		reset();

		// 网络
		if (!Utils.isNetworkConnected(mActivity)) {
			layout_shopcart.setVisibility(View.GONE);
			//这种方式有bug
			//layout_loading.setVisibility(View.VISIBLE);
			//layout_loading.postHandle(LoadingView.network_error);
			layout_loading.setVisibility(View.GONE);
			layout_loading.postHandle(LoadingView.success);
			noDataView.setVisibility(View.VISIBLE);
			noDataView.postHandle(NoDataView.nonet);
			return;
		}
		// 登录
		if (!MoLiApplication.getInstance().isLogin()) {
			layout_shopcart.setVisibility(View.GONE);
			layout_loading.setVisibility(View.GONE);
			layout_loading.postHandle(LoadingView.success);
			noDataView.setVisibility(View.VISIBLE);
			noDataView.postHandle(NoDataView.nologin);
			return;
		}

		Api.getCartSync(mActivity, "", page, pagesize, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				scroll_shopcart.onRefreshComplete();
				layout_loading.setVisibility(View.GONE);
				layout_loading.postHandle(LoadingView.success);

				// 保存最后更新时间
				Config.setLastsynctime(Utils.getNowTime());

				RspCartSync entity = (RspCartSync) rspData;
				RspCartSync.Data data = entity.data;
				List<Goods> list = data.getGoodslist();
				if (list != null && list.size() > 0) {
					layout_shopcart.setVisibility(View.VISIBLE);
					noDataView.setVisibility(View.GONE);
					shopcartView = new ShopCartView(mActivity, layout_shopcart, ll_content, noDataView, shopcar_all_price,
							shopcar_commit_bt, check_layout,shopcar_checkall, data.getGoodslist());
				} else {
					// 购物车为空
					layout_shopcart.setVisibility(View.GONE);
					noDataView.setVisibility(View.VISIBLE);
					noDataView.postHandle(NoDataView.noshopcart);
				}
			}

			@Override
			public void failed(String msg) {
				if (msg != null) {
					if ("网络异常!".equals(msg)) {
						scroll_shopcart.onRefreshComplete();
						layout_shopcart.setVisibility(View.GONE);
						layout_loading.setVisibility(View.VISIBLE);
						layout_loading.postHandle(LoadingView.network_error);
						noDataView.setVisibility(View.GONE);
					} else {
						scroll_shopcart.onRefreshComplete();
						layout_shopcart.setVisibility(View.GONE);
						layout_loading.setVisibility(View.GONE);
						layout_loading.postHandle(LoadingView.success);
						noDataView.setVisibility(View.GONE);

						Toast.show(mActivity, msg);
					}
				}
			}
		}, RspCartSync.class);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_right:
			if (isEdit) {
				tv_right.setText("编辑");
				shopcar_commit_bt.setText("结算");
				isEdit = false;
				calculate_price_layout.setVisibility(View.VISIBLE);
			} else {
				tv_right.setText("完成");
				shopcar_commit_bt.setText("删除");
				isEdit = true;
				calculate_price_layout.setVisibility(View.INVISIBLE);
			}
			if(shopcartView != null){
				shopcartView.edit();
				shopcartView.setCommitBtnStatus();
			}
			//状态改变时，不勾选
			shopcar_checkall.setChecked(false);
			break;
		case R.id.shopcar_commit_bt:
			if (shopcar_commit_bt.getText().toString().contains("结算")) {
				// 未登陆
				if (!MoLiApplication.getInstance().isLogin()) {
					Toast.show(mActivity, "请先登录");
					startActivity(new Intent(mActivity, LoginActivity.class));
					return;
				}
				
				// 购车你执行一次 order/make 判断了 可以跳到填写订单 再把这个参数传过来
				if (!shopcartView.checkShopCar()) {
					return;
				}
				List<Good> goodlist = shopcartView.getGoodlist();
				if (goodlist == null || goodlist.size() == 0) {
					Toast.show(mActivity, "请选择商品!");
					return;
				}

				List<ReqGoodsInfo> goodsList = new ArrayList<ReqGoodsInfo>();
				for (Good good : goodlist) {
					ReqGoodsInfo reqGoodsInfo = new ReqGoodsInfo();
					reqGoodsInfo.goodsid = good.getGoodsid();
					reqGoodsInfo.num = good.getNum();
					reqGoodsInfo.spec = good.getSpec();
					goodsList.add(reqGoodsInfo);
				}

				orderMake(goodsList);
			} else if (shopcar_commit_bt.getText().toString().equals("删除")) {
				if (shopcartView != null) {
					shopcartView.deleteShopCart();
				}
			}
			break;
		case R.id.check_layout:
			if(v.isSelected()){
				check_layout.setSelected(false);
				shopcartView.check(false);
				shopcar_checkall.setChecked(false);
			}else{
				check_layout.setSelected(true);
				shopcartView.check(true);
				shopcar_checkall.setChecked(true);
			}
			break;
		default:
			break;
		}
	}

	OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			switch (view.getId()) {
			case R.id.bt_login:
				startActivity(new Intent(mActivity, LoginActivity.class));
				break;
			case R.id.bt_load_car:
				((MainActivity)mActivity).tabSelect(R.id.button_home);
				break;
			case R.id.bt_reload:
				initData();
				break;
			default:
				break;
			}
		}
	};

	private void orderMake(final List<ReqGoodsInfo> goodsList) {
		final String op = "buycart";

		String ip = Utils.getLocalIpAddress();
		DB_Location db_location = new DB_Location(mActivity);
		String lng = db_location.getLontitude();
		String lat = db_location.getLatitude();

		String goods = new Gson().toJson(goodsList);
		Api.make(mActivity, op, goods, ip, lng, lat, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				RspOrderMake rsp = (RspOrderMake) rspData;
				if (rsp.data != null) {
					if (rsp.data.vipmember == 0) {
						MembershipDialog.show(mActivity);
						return;
					}
					Intent intent = new Intent(mActivity, FillOrderActivity.class);
					intent.putExtra(FillOrderActivity.ORDER_OP, op);
					intent.putExtra(FillOrderActivity.ORDER_GOODS_LIST, (Serializable) goodsList);
					mActivity.startActivity(intent);
				}
			}

			@Override
			public void failed(String msg) {
				if (!TextUtils.isEmpty(msg)) {
					Toast.show(mActivity, msg);
				}
			}
		}, RspOrderMake.class);
	}

	/*OnCheckedChangeListener checkChanged = new OnCheckedChangeListener() {
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if (isChecked) {
				shopcartView.check(true);
			} else {
				shopcartView.check(false);
			}
		}
	};*/

	private void reset() {
		tv_right.setText("编辑");
		check_layout.setSelected(false);
		shopcar_checkall.setChecked(false);
	}
	
	
}
