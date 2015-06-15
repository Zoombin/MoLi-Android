package com.imooly.android.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.imooly.android.tool.InitUtil;
import com.imooly.android.tool.InitUtil.InitCallBack;
import com.imooly.android.ui.FillOrderActivity;
import com.imooly.android.ui.LoginActivity;
import com.imooly.android.ui.MainActivity;
import com.imooly.android.utils.Utils;
import com.imooly.android.view.MembershipDialog;
import com.imooly.android.view.ShopCartView;
import com.imooly.android.widget.NoDataView;
import com.imooly.android.widget.Toast;
import com.imooly.android.widget.in.srain.cube.views.ptr.PtrClassicFrameLayout;
import com.imooly.android.widget.in.srain.cube.views.ptr.PtrClassicHeader;
import com.imooly.android.widget.in.srain.cube.views.ptr.PtrDefaultHandler;
import com.imooly.android.widget.in.srain.cube.views.ptr.PtrFrameLayout;
import com.imooly.android.widget.in.srain.cube.views.ptr.PtrHandler;

public class ShopCartFragment extends BaseFragment implements OnClickListener {

	public LinearLayout layout_shopcart;
	PtrClassicFrameLayout scroll_shopcart;

	private TextView tv_right;

	private LinearLayout ll_content;

	private LinearLayout calculate_price_layout;
	private LinearLayout check_layout;
	private CheckBox shopcar_checkall;
	private TextView shopcar_all_price;
	public Button shopcar_commit_bt;

	public static boolean isEdit = false;

	public ShopCartView shopcartView;
	public NoDataView layout_nodata_shop;
	private int page = 1;
	private int pagesize = 100;
	boolean loading = false;
	
	public static boolean clean = false;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_shop_cart, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initView();
		
		firstLoad();
	}
	
	private void firstLoad() {
		scroll_shopcart.setLoadingMinTime(1500);
		scroll_shopcart.postDelayed(new Runnable() {
			@Override
			public void run() {
				scroll_shopcart.autoRefresh(true);
			}
		}, 150);
	}
	
	public void refrashData() {
		if (scroll_shopcart != null) {
			Log.i("Tag", "CartSync");
			layout_shopcart.setVisibility(View.VISIBLE);
			layout_nodata_shop.postHandle(NoDataView.defult);

			initData();
		}
	}
	
	private void initView() {
		scroll_shopcart = (PtrClassicFrameLayout) mActivity.findViewById(R.id.scroll_shopcart);
		PtrClassicHeader  header = new PtrClassicHeader(mActivity);
		//scroll_shopcart.setLastUpdateTimeRelateObject(this);//最近更新时间
		scroll_shopcart.setHeaderView(header);
		scroll_shopcart.addPtrUIHandler(header);
		scroll_shopcart.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
            	initData();
            }

			@Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
		scrollViewListerner();
		
		//显示数据层
		layout_shopcart = (LinearLayout) mActivity.findViewById(R.id.layout_shopcart);
		
		//没有数据层
		layout_nodata_shop = (NoDataView) mActivity.findViewById(R.id.layout_nodata_shop);
		layout_nodata_shop.postHandle(NoDataView.defult);//默认不显示
		
		tv_right = (TextView) mActivity.findViewById(R.id.tv_right);
		tv_right.setOnClickListener(this);

		check_layout = (LinearLayout) mActivity.findViewById(R.id.check_layout);
		check_layout.setSelected(false);
		check_layout.setOnClickListener(this);
		shopcar_checkall = (CheckBox) mActivity.findViewById(R.id.shopcar_checkall);

		calculate_price_layout = (LinearLayout) mActivity.findViewById(R.id.calculate_price_layout);
		shopcar_all_price = (TextView) mActivity.findViewById(R.id.shopcar_all_price);
		shopcar_commit_bt = (Button) mActivity.findViewById(R.id.shopcar_commit_bt);
		shopcar_commit_bt.setOnClickListener(this);

		ll_content = (LinearLayout) mActivity.findViewById(R.id.ll_content);
		
		shopcartView = new ShopCartView(mActivity, layout_shopcart, ll_content, layout_nodata_shop, shopcar_all_price,
				shopcar_commit_bt, check_layout,shopcar_checkall);
	}

	public void initData() {
		if(loading){
			return;
		}
		String lastsynctime = Config.getLastsynctime();
		
		// 初始化view
		resetView();

		// 网络
		if (!Utils.isNetworkConnected(mActivity)) {
			layout_shopcart.setVisibility(View.GONE);
			layout_nodata_shop.postHandle(NoDataView.nonet);
			layout_nodata_shop.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (R.id.bt_reload == v.getId()){
						// 没有网络进来的时候APP初始化
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
									layout_nodata_shop.postHandle(NoDataView.nonet);
								}
							});
							return;
						}
						initData();
					}
				}
			});
			return;
		}

		// 登录
		if (!MoLiApplication.getInstance().isLogin()) {
			layout_shopcart.setVisibility(View.GONE);
			layout_nodata_shop.postHandle(NoDataView.nologin);
			layout_nodata_shop.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (R.id.bt_login == v.getId())
						startActivity(new Intent(mActivity, LoginActivity.class));
				}
			});
			return;
		}

		loading = true;
		Api.getCartSync(mActivity, "", page, pagesize, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				loading = false;
				scroll_shopcart.refreshComplete();

				// 保存最后更新时间
				Config.setLastsynctime(Utils.getNowTime());

				RspCartSync entity = (RspCartSync) rspData;
				RspCartSync.Data data = entity.data;
				List<Goods> list = data.getGoodslist();
				if (list != null && list.size() > 0) {
					layout_shopcart.setVisibility(View.VISIBLE);
					layout_nodata_shop.postHandle(NoDataView.defult);
					
					//设置数据
					shopcartView.setData(data.getGoodslist());
					
					//检查是否有失效商品
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							shopcartView.firstLoadCheck();
						}
					}, 200);
				} else {
					// 购物车为空
					layout_shopcart.setVisibility(View.GONE);
					layout_nodata_shop.postHandle(NoDataView.noshopcart);
					layout_nodata_shop.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							if (R.id.bt_load_car == v.getId())
								((MainActivity)mActivity).tabSelect(R.id.button_home);
						}
					});
				}
			}

			@Override
			public void failed(String msg) {
				loading = false;
				scroll_shopcart.refreshComplete();
				
				layout_shopcart.setVisibility(View.GONE);
				layout_nodata_shop.postHandle(NoDataView.nonet);
				layout_nodata_shop.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if (R.id.bt_reload == v.getId()){
							initData();
						}
					}
				});
			}
		}, RspCartSync.class);

	}
	
	
	void scrollViewListerner() {
		// 4.0以上不用处理
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			return;
		}
		// 低系统版本EditText快速滚动会奔溃
		ScrollView scrollView = (ScrollView) mActivity.findViewById(R.id.grid_view);
		scrollView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View view, MotionEvent event) {
				// TODO Auto-generated method stub
				if (!isEdit) {
					return false;
				}
				if (clean) {
					return false;
				}

				for (int i = 0; i < ll_content.getChildCount(); i++) {
					View item = ll_content.getChildAt(i);
					LinearLayout shopcar_subitem_layout = (LinearLayout) item.findViewById(R.id.shopcar_subitem_layout);
					for (int j = 0; j < shopcar_subitem_layout.getChildCount(); j++) {
						View subitem = shopcar_subitem_layout.getChildAt(j);
						EditText shopcar_subitem_et_num = (EditText) subitem.findViewById(R.id.shopcar_subitem_et_num);
						TextView shopcar_subitem_txt_num = (TextView) subitem.findViewById(R.id.shopcar_subitem_txt_num);

						shopcar_subitem_et_num.setVisibility(View.GONE);
						shopcar_subitem_txt_num.setVisibility(View.VISIBLE);
					}
				}

				clean = true;
				return false;
			}
		});
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

	private void orderMake(final List<ReqGoodsInfo> goodsList) {
		final String op = "buycart";

		String ip = Utils.getLocalIpAddress();
		DB_Location db_location = new DB_Location(mActivity);
		String lng = db_location.getLontitude();
		String lat = db_location.getLatitude();

		String goods = new Gson().toJson(goodsList);
		Api.make(mActivity, op, goods, ip, lng, lat, "",new NetCallBack<ServiceResult>() {
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
	

	private void resetView() {
		check_layout.setSelected(false);
		shopcar_checkall.setChecked(false);
		
		shopcar_commit_bt.setBackgroundResource(R.color.app_text_gray);
		shopcar_commit_bt.setText("结算");
		shopcar_commit_bt.setClickable(false);
	}
}
