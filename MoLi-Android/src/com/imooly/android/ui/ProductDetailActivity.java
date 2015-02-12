package com.imooly.android.ui;

import java.text.DecimalFormat;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.imooly.android.MoLiApplication;
import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.adapter.GoodsRandgridAdapter;
import com.imooly.android.adapter.PagerviewImgAdapter;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.db.DB_Location;
import com.imooly.android.db.DB_User;
import com.imooly.android.db.DataHelper;
import com.imooly.android.entity.RspGoodsSpec;
import com.imooly.android.entity.RspMemberCard;
import com.imooly.android.entity.RspProfile;
import com.imooly.android.entity.RspProfile.AreaEty;
import com.imooly.android.entity.RspProfile.GoodsrandEty;
import com.imooly.android.entity.RspProfile.IntroduceEty;
import com.imooly.android.entity.RspProfile.IntroduceEty.ListEty;
import com.imooly.android.entity.RspProfile.StoreEty;
import com.imooly.android.entity.RspSuccessCommon;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.enums.ShareObject;
import com.imooly.android.tool.ClickUtil;
import com.imooly.android.tool.Constants;
import com.imooly.android.utils.OnSingleClickListener;
import com.imooly.android.utils.Utils;
import com.imooly.android.view.MembershipDialog;
import com.imooly.android.view.ProdectSpecFilterDialog;
import com.imooly.android.view.ProdectSpecFilterDialog.ProdectSpecFilterCallBack;
import com.imooly.android.widget.CannotRollGridView;
import com.imooly.android.widget.ScrollViewExtend;
import com.imooly.android.widget.ShareDialog;
import com.imooly.android.widget.Toast;
import com.imooly.android.widget.autoscrollviewpager.AutoScrollViewPager;
import com.imooly.android.widget.viewpage.CirclePageIndicator;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 商品详情页面
 * 
 * @author daiye
 * 
 */
public class ProductDetailActivity extends BaseActivity implements OnClickListener {
	DecimalFormat fnum = new DecimalFormat("##0.00");
	public static final String EXTRA_GOODSID = "goodsid";
	private AutoScrollViewPager pager;
	private Button btn_share;

	ScrollViewExtend layout_scroll;

	// topLayout
	private TextView tv_product_name;
	private Button btn_collect;
	private TextView tv_price_net;
	private TextView tv_price_market;
	private CirclePageIndicator mIndicator;
	private TextView tv_goodsto;
	private TextView tv_postage;
	private TextView tv_salesvolume;

	// contentLayout
	private LinearLayout content_layout;
	private LayoutInflater mInflater;
	private LinearLayout layout_choose;
	private TextView tv_choose;
	private LinearLayout layout_graphic;
	private LinearLayout layout_introduce;
	private TextView tv_introduce_info;
	private ImageView iv_introduce_arrow_down;
	private ImageView iv_arrow_introdude;
	private TextView tv_comment;
	private LinearLayout layout_store;
	private TextView storeName;
	private ImageView storeIcon;
	private LinearLayout layout_voucher;
	private TextView voucher_value;
	private TextView voucher_tip;
	private ImageView voucher_img;
	private LinearLayout shopcar_layout;
	private Button add_to_shopcar;
	private LinearLayout layout_comment;
	private LinearLayout rand_layout;
	private LinearLayout grid_layout;
	private Button buynow;
	private ImageView rollup;

	private RspGoodsSpec goodsSpec;
	private String goodsid;

	private LinearLayout main_bottom;
	private ImageView iv_mark;// 单独处理了不写在TabView
	private TextView text_num;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_detail);
		logActivityName(this);

		initView();
		initData();
	}

	private void initView() {
		mInflater = (LayoutInflater) self.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenWidth = dm.widthPixels;
		pager = (AutoScrollViewPager) findViewById(R.id.pager);
		pager.getLayoutParams().height = screenWidth;
		pager.getLayoutParams().width = screenWidth;
		pager.setLayoutParams(pager.getLayoutParams());

		findViewById(R.id.btn_back).setOnClickListener(this);
		btn_share = (Button) findViewById(R.id.btn_share);
		btn_share.setOnClickListener(this);

		main_bottom = (LinearLayout) findViewById(R.id.main_bottom);

		layout_scroll = (ScrollViewExtend) findViewById(R.id.layout_scroll);
		layout_scroll.setOnTouchListener(touchListener);

		mIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
		tv_product_name = (TextView) findViewById(R.id.tv_product_name);
		btn_collect = (Button) findViewById(R.id.btn_collect);
		btn_collect.setOnClickListener(this);
		tv_price_net = (TextView) findViewById(R.id.tv_price_net);
		tv_price_market = (TextView) findViewById(R.id.tv_price_market);
		tv_goodsto = (TextView) findViewById(R.id.tv_goodsto);
		tv_postage = (TextView) findViewById(R.id.tv_postage);
		tv_salesvolume = (TextView) findViewById(R.id.tv_salesvolume);

		content_layout = (LinearLayout) findViewById(R.id.content_layout);
		content_layout.setVisibility(View.INVISIBLE);
		layout_choose = (LinearLayout) findViewById(R.id.layout_choose);
		layout_choose.setOnClickListener(this);
		tv_choose = (TextView) findViewById(R.id.tv_choose);
		layout_graphic = (LinearLayout) findViewById(R.id.layout_graphic);
		layout_graphic.setOnClickListener(this);
		layout_introduce = (LinearLayout) findViewById(R.id.layout_introduce);
		tv_introduce_info = (TextView) findViewById(R.id.tv_introduce_info);
		iv_introduce_arrow_down = (ImageView) findViewById(R.id.iv_introduce_arrow_down);
		iv_arrow_introdude = (ImageView) findViewById(R.id.iv_arrow_introdude);
		tv_comment = (TextView) findViewById(R.id.tv_comment);
		layout_store = (LinearLayout) findViewById(R.id.layout_store);
		layout_store.setOnClickListener(this);
		storeName = (TextView) findViewById(R.id.tv_store);
		storeIcon = (ImageView) findViewById(R.id.iv_store);
		layout_voucher = (LinearLayout) findViewById(R.id.layout_voucher);
		layout_voucher.setVisibility(View.GONE);
		voucher_value = (TextView) findViewById(R.id.voucher_value);
		voucher_tip = (TextView) findViewById(R.id.voucher_tip);
		voucher_img = (ImageView) findViewById(R.id.voucher_img);

		shopcar_layout = (LinearLayout) findViewById(R.id.shopcar_layout);
		shopcar_layout.setOnClickListener(this);// 屏蔽向下点击
		shopcar_layout.setVisibility(View.INVISIBLE);
		add_to_shopcar = (Button) findViewById(R.id.add_to_shopcar);
		add_to_shopcar.setOnClickListener(this);
		layout_comment = (LinearLayout) findViewById(R.id.layout_comment);
		layout_comment.setOnClickListener(this);
		rand_layout = (LinearLayout) findViewById(R.id.rand_layout);
		grid_layout = (LinearLayout) findViewById(R.id.grid_layout);
		buynow = (Button) findViewById(R.id.buynow);
		buynow.setOnClickListener(this);
		rollup = (ImageView) findViewById(R.id.rollup);
		rollup.setOnClickListener(this);

		iv_mark = (ImageView) findViewById(R.id.iv_mark);
		text_num = (TextView) findViewById(R.id.text_num);
	}

	private void initData() {
		goodsid = getIntent().getStringExtra(EXTRA_GOODSID);
		// IP地址
		String ipAddress = Utils.getLocalIpAddress();

		DB_Location db_location = new DB_Location(self);

		/** 商品详情 */
		Api.profile(self, goodsid, ipAddress, db_location.getLatitude(), db_location.getLontitude(), new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				RspProfile rsp = (RspProfile) rspData;
				RspProfile.Data data = rsp.data;
				if (data == null) {
					return;
				}
				shopcar_layout.setVisibility(View.VISIBLE);
				content_layout.setVisibility(View.VISIBLE);

				List<AreaEty> arealist = data.getArealist();
				// 商品id
				String goodsid = data.getGoodsid();
				// 商品图片
				List<String> goodsimage = data.getGoodsimages();
				if (goodsimage != null && goodsimage.size() > 1) {
					pager.setAdapter(new PagerviewImgAdapter(self, goodsimage, true));
					mIndicator.setVisibility(View.VISIBLE);
					mIndicator.setPageColor(getResources().getColor(R.color.white));
					mIndicator.setViewPager(pager, goodsimage.size() * CirclePageIndicator.fornum / 2);
				} else {
					pager.setAdapter(new PagerviewImgAdapter(self, goodsimage, false));
					mIndicator.setVisibility(View.INVISIBLE);
				}

				// 商品名称
				String goodsname = data.getGoodsname();
				tv_product_name.setText(goodsname);

				// 商品价格
				float goodsprice = data.getGoodsprice().getViprmb();//
				tv_price_net.setText("￥" + fnum.format(goodsprice));
				float marketprice = data.getGoodsprice().getMarket();//
				tv_price_market.setText("￥" + fnum.format(marketprice));
				tv_price_market.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);

				int favorite = data.getIsfavorite();
				if (1 == favorite) {
					btn_collect.setText("已收藏");
					btn_collect.setSelected(true);
				} else {
					btn_collect.setText("收藏");
					btn_collect.setSelected(false);
				}

				int isStock = data.getIsstock();
				layout_choose.setTag(isStock);
				add_to_shopcar.setTag(isStock);
				buynow.setTag(isStock);
				if (0 == isStock) {
					// 没有库存
					add_to_shopcar.setBackgroundColor(getResources().getColor(R.color.white_transluce_invalid));
					buynow.setBackgroundColor(getResources().getColor(R.color.red_transluce_invalid));
				}

				// 快递 从哪出发 例：浙江丽水 至 江苏 苏州
				String goodsfrom = data.getGoodsfrom();
				// 快递 送到哪 例：浙江丽水 至 江苏 苏州
				String goodsto = data.getGoodsto();
				if (TextUtils.isEmpty(goodsfrom) && TextUtils.isEmpty(goodsto)) {
					tv_goodsto.setText(goodsfrom + " 至 " + goodsto);
				}

				// // 保存足迹 ////
				String img = "";
				if (goodsimage != null && goodsimage.size() > 0) {
					img = goodsimage.get(0);
				}
				DataHelper.getInstance().SaveOrUpdateFootstep(goodsid, goodsname, img, fnum.format(goodsprice));

				// 商品发货方式 快递/EMS/平邮等
				String postageway = data.getPostageway();
				// 快递费用
				float postage = data.getPostage();
				tv_postage.setText(postageway + "：" + fnum.format(postage));

				// 其他说明 例：全场包邮/满100包邮等
				String goodscaption = data.getGoodscaption();
				// 总销量
				int salesvolume = data.getSalesvolume();
				tv_salesvolume.setText(salesvolume + "");

				// 选择规格标题 例：尺寸、机身颜色
				String choose = data.getChoose();
				if (TextUtils.isEmpty(choose)) {
					choose = "规格";
				}
				tv_choose.setText("选择：" + choose);

				// 图文详情
				// …… 页面跳转

				// 规格参数介绍
				List<IntroduceEty> introduce = data.getIntroduce();
				String introduce_value = "";
				for (IntroduceEty introduceety : introduce) {
					List<ListEty> listEties = introduceety.getList();
					StringBuffer value = new StringBuffer();
					for (ListEty subEty : listEties) {
						String str = subEty.getName() + "(" + subEty.getValue() + "),";
						value.append(str);
					}
					introduce_value += introduceety.getTitle() + "：" + value + "\n";
				}
				tv_introduce_info.setText(introduce_value);
				// 显示或隐藏 规格
				layout_introduce.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						hideMenu();
						if (tv_introduce_info.getVisibility() == View.GONE) {
							tv_introduce_info.setVisibility(View.VISIBLE);
							iv_arrow_introdude.setImageResource(R.drawable.ic_arrow_down);
							iv_introduce_arrow_down.setVisibility(View.VISIBLE);
						} else {
							tv_introduce_info.setVisibility(View.GONE);
							iv_arrow_introdude.setImageResource(R.drawable.ic_arrow_right);
							iv_introduce_arrow_down.setVisibility(View.INVISIBLE);
						}
					}
				});

				// 累计评价
				int totalcomment = data.getTotalcomment();
				String source = "累计评价<font color='#ea4f22'>(" + totalcomment + ")</font>";
				tv_comment.setText(Html.fromHtml(source));

				// 旗舰店详情
				StoreEty store = data.getStore();
				layout_store.setVisibility(View.GONE);
				if (store != null) {
					layout_store.setVisibility(View.VISIBLE);
					layout_store.setTag(store.businessid);
					String bssid = store.getBusinessid();// 旗舰店id
					String bname = store.getBusinessname();// 旗舰店名字
					String bicon = store.getBusinessicon();// 旗舰店图标
					ImageLoader.getInstance().displayImage(bicon, storeIcon);
					storeName.setText(bname);
				}

				// 代金券
				List<String> vouValue = data.getVoucher();// 金额
				String vouImg = data.getVoucherimage();//
				if (vouValue != null && vouValue.size() == 2) {
					layout_voucher.setVisibility(View.VISIBLE);
					voucher_value.setText(vouValue.get(0) + " ~ " + vouValue.get(1));
					voucher_tip.setText(String.format(getString(R.string.fillorder_voucher_format),
							vouValue.get(0) + " ~ " + vouValue.get(1)));
				}

				// 猜你喜欢
				List<GoodsrandEty> goodsrands = data.getGoodsrand();
				if (goodsrands != null && goodsrands.size() > 0) {
					rand_layout.setVisibility(View.VISIBLE);
					CannotRollGridView gridList = new CannotRollGridView(self);
					gridList.setNumColumns(2);
					gridList.setHorizontalSpacing(20);
					gridList.setVerticalSpacing(20);
					gridList.setCacheColorHint(Color.parseColor("#00000000"));
					gridList.setSelector(new ColorDrawable(Color.TRANSPARENT));
					gridList.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							hideMenu();
							GoodsrandEty enEty = (GoodsrandEty) parent.getAdapter().getItem(position);
							startActivity(new Intent(self, ProductDetailActivity.class).putExtra(ProductDetailActivity.EXTRA_GOODSID,
									enEty.getGoodsid()));
						}
					});
					GoodsRandgridAdapter gAdapter = new GoodsRandgridAdapter();
					gridList.setAdapter(gAdapter);
					gAdapter.setData(goodsrands);

					grid_layout.addView(gridList);

					// 底部菜单的状态
					showCurShopCarStatus();
				}
			}

			@Override
			public void failed(String msg) {
				Toast.show(self, msg);
			}
		}, RspProfile.class);

		/** 商品规格 */
		Api.goodsSpec(self, goodsid, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				RspGoodsSpec rsp = (RspGoodsSpec) rspData;
				if (rsp.data != null) {
					goodsSpec = rsp;
				}
			}

			@Override
			public void failed(String msg) {

			}
		}, RspGoodsSpec.class);
	}

	private void showCurShopCarStatus() {
		if (Constants.curShopCarNum > 0) {
			iv_mark.setVisibility(View.VISIBLE);
			text_num.setVisibility(View.VISIBLE);
			text_num.setText(Constants.curShopCarNum + "");
		} else {
			iv_mark.setVisibility(View.GONE);
			text_num.setVisibility(View.GONE);
		}
	}

	/** 推送进来的特别处理 */
	private void goBack() {
		String pushAction = getIntent().getStringExtra("pushAction");
		if (TextUtils.isEmpty(pushAction)) {
			finish();
		} else {
			Intent intent = new Intent(MainActivity.ACTION);
			sendBroadcast(intent);

			startActivity(new Intent(self, MainActivity.class));
			finish();
		}
	}

	@Override
	public void onBackPressed() {
		goBack();
	}

	OnTouchListener touchListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (main_bottom.getVisibility() == View.VISIBLE) {
				hideMenu();
			}
			return false;
		}
	};

	public void onTabClicked(View v) {
		startMainActivity(v.getId());
	}

	private void startMainActivity(int viewid) {
		Intent intent = new Intent(MainActivity.ACTION);
		intent.putExtra("viewid", viewid);
		sendBroadcast(intent);

		startActivity(new Intent(self, MainActivity.class));
		finish();
	}

	private void showMenu() {
		main_bottom.setVisibility(View.VISIBLE);
		main_bottom.startAnimation(AnimationUtils.loadAnimation(self, R.anim.push_in_from_bottom));
	}

	private void hideMenu() {
		main_bottom.startAnimation(AnimationUtils.loadAnimation(self, R.anim.push_out_to_bottom));
		main_bottom.setVisibility(View.GONE);
	}

	@Override
	public void onClick(View v) {
		// 防止重复点击
		if (ClickUtil.isFastDoubleClick()) {
			return;
		}

		// 小魔女点击
		if (v.getId() == R.id.rollup) {
			if (main_bottom.getVisibility() == View.VISIBLE) {
				hideMenu();
			} else {
				showMenu();
			}
			return;
		}

		// 隐藏菜单栏
		hideMenu();
		switch (v.getId()) {
		case R.id.btn_back:
			goBack();
			break;
		case R.id.btn_share:
			// 分享
			new ShareDialog(self, ShareObject.goods, goodsid);
			break;
		case R.id.btn_collect:
			if (!MoLiApplication.getInstance().isLogin()) {
				Toast.show(self, "请先登录");
				startActivity(new Intent(self, LoginActivity.class));
				return;
			}
			final Button collect = (Button) v;
			if (collect.isSelected()) {
				String goodsids = "[\"" + goodsid + "\"]";
				Api.delteMyCollectionGood(self, goodsids, new NetCallBack<ServiceResult>() {
					@Override
					public void success(ServiceResult rspData) {
						RspSuccessCommon rsp = (RspSuccessCommon) rspData;
						if (rsp.data != null && rsp.data.success == 1) {
							collect.setSelected(false);
							collect.setText("收藏");
							Toast.show(self, "已取消收藏");
						} else {
							Toast.show(self, "取消收藏失败");
						}
					}

					@Override
					public void failed(String msg) {
						Toast.show(self, msg);
					}
				}, RspSuccessCommon.class);
			} else {
				Api.addfavgoods(self, goodsid, new NetCallBack<ServiceResult>() {
					@Override
					public void success(ServiceResult rspData) {
						RspSuccessCommon rsp = (RspSuccessCommon) rspData;
						if (rsp.data != null && rsp.data.success == 1) {
							collect.setSelected(true);
							collect.setText("已收藏");
							Toast.show(self, "收藏成功");
						} else {
							Toast.show(self, "收藏失败");
						}
					}

					@Override
					public void failed(String msg) {
						Toast.show(self, msg);
					};
				}, RspSuccessCommon.class);
			}
			break;
		case R.id.layout_choose:
			// 选择商品规格
			int isStock = (Integer) v.getTag();
			if (0 == isStock) {
				Toast.show(self, "该商品库存不足");
				return;
			}
			showPickDialog(0);
			break;
		case R.id.layout_graphic:
			// 图文详情
			startActivity(new Intent(self, ProductContentDetailActivity.class)
					.putExtra(ProductContentDetailActivity.EXTRA_GOODSID, goodsid));
			break;
		case R.id.layout_comment:
			// 评价
			startActivity(new Intent(self, ProductCommentActivity.class).putExtra(ProductCommentActivity.EXTRA_GOODSID, goodsid));
			break;
		case R.id.layout_store:
			// 旗舰店
			String businsid = (String) v.getTag();
			startActivity(new Intent(self, StoreProActivity.class).putExtra(StoreProActivity.EXTRA_BUSINESSID, businsid));
			break;
		case R.id.add_to_shopcar:
			// 加入购物车
			// 未登陆
			if (!MoLiApplication.getInstance().isLogin()) {
				Toast.show(self, "请先登录");
				startActivity(new Intent(self, LoginActivity.class));
				return;
			}

			int isStock1 = (Integer) v.getTag();
			if (0 == isStock1) {
				Toast.show(self, "该商品库存不足");
				return;
			}
			showPickDialog(1);
			break;
		case R.id.voucher_guide:
			startActivity(new Intent(self, VoucherDetailActivity.class));
			break;
		case R.id.buynow:
			// 立即购买
			// 未登陆
			if (!MoLiApplication.getInstance().isLogin()) {
				Toast.show(self, "请先登录");
				startActivity(new Intent(self, LoginActivity.class));
				return;
			}

			int isStock2 = (Integer) v.getTag();
			if (0 == isStock2) {
				Toast.show(self, "该商品库存不足");
				return;
			}
			Api.memberCard(self, new NetCallBack<ServiceResult>() {

				@Override
				public void success(ServiceResult rspData) {
					RspMemberCard rsp = (RspMemberCard) rspData;
					RspMemberCard.Data data = rsp.data;
					int vipFlag = data.getVipflag();
					if (vipFlag == 0) {
						MembershipDialog.show(self);
					} else {
						showPickDialog(2);
					}
				}

				@Override
				public void failed(String msg) {
					Toast.show(self, msg);
				}
			}, RspMemberCard.class);

			break;
		default:
			break;
		}
	}

	ProdectSpecFilterDialog filterDialog;
	private void showPickDialog(int from) {
		if (goodsSpec == null) {
			Toast.show(self, "获取商品规格失败");
			return;
		}
		if (filterDialog == null) {
			filterDialog = new ProdectSpecFilterDialog();
		}
		filterDialog.show(self, from, goodsid, goodsSpec, new ProdectSpecFilterCallBack() {
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess() {
				Constants.curShopCarNum += 1;
				showCurShopCarStatus();
			}
		});
	}
}
