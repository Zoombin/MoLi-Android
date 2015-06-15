package com.imooly.android.ui;

import java.text.DecimalFormat;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alipay.sdk.util.LogUtils;
import com.imooly.android.MoLiApplication;
import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.adapter.GoodsRandgridAdapter;
import com.imooly.android.adapter.PagerviewImgAdapter;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.db.DB_Location;
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
import com.imooly.android.tool.Config;
import com.imooly.android.utils.Utils;
import com.imooly.android.view.MembershipDialog;
import com.imooly.android.view.ProductSpecFilterDialog;
import com.imooly.android.view.ProductSpecFilterDialog.ProdectSpecFilterCallBack;
import com.imooly.android.widget.CannotRollGridView;
import com.imooly.android.widget.ScrollViewExtend;
import com.imooly.android.widget.ShareDialog;
import com.imooly.android.widget.Toast;
import com.imooly.android.widget.autoscrollviewpager.AutoScrollViewPager;
import com.imooly.android.widget.viewpage.CirclePageIndicator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

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
	private RelativeLayout product_detail_layout;
	private RelativeLayout product_content_layout;
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
	private Button add_to_shopcar;
	private LinearLayout layout_comment;
	private LinearLayout rand_layout;
	private LinearLayout grid_layout;
	private Button buynow;
	private ImageView rollup;

	private RspGoodsSpec goodsSpec;
	private String goodsid;

	private LinearLayout bottom_layout;
	private LinearLayout shopcar_layout;
	private View main_menu;
	private ImageView iv_mark;// 单独处理了不写在TabView
	private TextView text_num;
	boolean VISIBLE;
	int screenDisplayHight = 0;
	int bottomHight=0;
	int menuHight = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_product_detail);
		logActivityName(this);

		initView();
		initData();
	}
	
	
	@Override
	protected void onStart() {
		super.onStart();
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				//首次处理
				initDisplay();
				
				VISIBLE = false;
				bottom_layout.setVisibility(View.VISIBLE);
				setBottomLocation();
			}
		}, 1200);
	}
	
	private void initDisplay(){
		screenDisplayHight = Config.displayheight;
		bottomHight = bottom_layout.getHeight();
		menuHight = main_menu.getHeight();
	}
	
	
	private void setBottomLocation(){
		//底部
		setMargin(bottom_layout,screenDisplayHight-menuHight,-menuHight);
		//内容
		setMargin(product_content_layout, 0, menuHight);
	}
	

	private void initView() {
		mInflater = (LayoutInflater) self.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

		int screenWidth = Config.width;
		pager = (AutoScrollViewPager) findViewById(R.id.pager);
		pager.getLayoutParams().height = screenWidth;
		pager.getLayoutParams().width = screenWidth;
		pager.setLayoutParams(pager.getLayoutParams());

		product_detail_layout = (RelativeLayout) findViewById(R.id.product_detail_layout);
		product_content_layout = (RelativeLayout) findViewById(R.id.product_content_layout);
		findViewById(R.id.btn_back).setOnClickListener(this);
		btn_share = (Button) findViewById(R.id.btn_share);
		btn_share.setOnClickListener(this);

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

		bottom_layout = (LinearLayout) findViewById(R.id.bottom_layout);
		bottom_layout.setVisibility(View.INVISIBLE);
		main_menu = findViewById(R.id.main_menu);
		shopcar_layout = (LinearLayout) findViewById(R.id.shopcar_layout);
		shopcar_layout.setOnClickListener(this);// 屏蔽向下点击
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
					mIndicator.setFillColor(Style.FILL, getResources().getColor(R.color.default_circle_indicator_fill_color));
					mIndicator.setStrokeColor(Style.FILL, getResources().getColor(R.color.default_circle_indicator_stroke_color));
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
				if (!TextUtils.isEmpty(goodsfrom) && !TextUtils.isEmpty(goodsto) && !"null".equals(goodsfrom) && !"null".equals(goodsto)) {
					tv_goodsto.setText(goodsfrom + " 至 " + goodsto);
				}

				// // 保存足迹 ////
				String img = data.getLogo();
				if(MoLiApplication.getInstance().isLogin()){
					DataHelper.getInstance().SaveOrUpdateFootstep(goodsid, goodsname, img, fnum.format(goodsprice));
				}

				// 商品发货方式 快递/EMS/平邮等
				String postageway = data.getPostageway();
				// 快递费用
				String postage = data.getPostage();
				if (!TextUtils.isEmpty(postageway) && !TextUtils.isEmpty(postage) && !"null".equals(postageway) && !"null".equals(postage)) {
					tv_postage.setText(postageway + "：" + postage);
				}

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
						String str = subEty.getName() + "：" + subEty.getValue() + "\n";
						value.append(str);
					}
					introduce_value += introduceety.getTitle() + "\n" + value + "\n";
				}
				SpannableString styledText = new SpannableString(introduce_value);
				
				int textsize = 0;
				for (IntroduceEty introduceety : introduce) {
					List<ListEty> listEties = introduceety.getList();
					
					String title = introduceety.getTitle() + "\n";
					int titlelength = title.length();
					styledText.setSpan(new TextAppearanceSpan(ProductDetailActivity.this, R.style.IntroduceTitle), textsize, textsize + titlelength - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					textsize += titlelength;
					
					for (ListEty subEty : listEties) {
						String str = subEty.getName() + "：" + subEty.getValue() + "\n";
						styledText.setSpan(new TextAppearanceSpan(ProductDetailActivity.this, R.style.IntroduceContent), textsize - 1, textsize + str.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
						textsize += str.length();
					}
					
					int entersize = "\n".length();
					styledText.setSpan(new TextAppearanceSpan(ProductDetailActivity.this, R.style.IntroduceEnter), textsize - 1, textsize + entersize - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
					textsize += entersize;
				}
				
				tv_introduce_info.setText(styledText);
				
				// 显示或隐藏 规格
				layout_introduce.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (VISIBLE) {
							hideMenu();
						}
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
				if (store.businessid != null) {
					layout_store.setVisibility(View.VISIBLE);
					layout_store.setTag(store.businessid);
					String bssid = store.getBusinessid();// 旗舰店id
					String bname = store.getBusinessname();// 旗舰店名字
					String bicon = store.getBusinessicon();// 旗舰店图标

					DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
							.showImageOnLoading(R.drawable.ic_loading_580_276).showImageForEmptyUri(R.drawable.ic_error_580_276) // empty
																																	// URI时显示的图片
							.showImageOnFail(R.drawable.ic_error_580_276) // 不是图片文件
																			// 显示图片
							.cacheInMemory(true).bitmapConfig(Bitmap.Config.RGB_565) // 图片压缩质量
							.cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();
					ImageLoader.getInstance().displayImage(bicon, storeIcon, defaultOptions);
					storeName.setText(bname);
				}

				// 代金券
				List<String> vouValue = data.getVoucher();// 金额
				String vouImg = data.getVoucherimage();//
				if (vouValue != null && vouValue.size() == 2) {
					layout_voucher.setVisibility(View.VISIBLE);
					String value0 = vouValue.get(0);
					String value1 = vouValue.get(1);
					if (value0.equals(value1)) {
						voucher_value.setText(value0);
						voucher_tip.setText(String.format(getString(R.string.fillorder_voucher_format), value0));
					} else {
						voucher_value.setText(value0 + " ~ " + value1);
						voucher_tip.setText(String.format(getString(R.string.fillorder_voucher_format), value0 + " ~ " + value1));
					}
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
							if (VISIBLE) {
								hideMenu();
							}
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
					//刷新底部菜单栏
					setBottomLocation();
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
		if (Config.curShopCarNum > 0) {
			iv_mark.setVisibility(View.VISIBLE);
			text_num.setVisibility(View.VISIBLE);
			text_num.setText(Config.curShopCarNum + "");
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
			if (VISIBLE) {
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
		float[] f = new float[2];
		f[0] = menuHight;
		f[1] =  0;
		ObjectAnimator animator = ObjectAnimator.ofFloat(bottom_layout, "translationY", f);
		animator.setDuration(180);
		animator.setInterpolator(new AccelerateDecelerateInterpolator());
		animator.start();
		VISIBLE = true;
		//底部
		setMargin(bottom_layout,screenDisplayHight-bottomHight,0);
	}

	private void hideMenu() {
		float[] f = new float[2];
		f[0] = -menuHight;
		f[1] = 0;
		ObjectAnimator animator = ObjectAnimator.ofFloat(bottom_layout, "translationY", f);
		animator.setDuration(180);
		animator.setInterpolator(new AccelerateDecelerateInterpolator());
		animator.start();
		VISIBLE = false;
		//底部
		setMargin(bottom_layout,screenDisplayHight-menuHight,-menuHight);
	}

	@Override
	public void onClick(View v) {
		// 小魔女点击
		if (v.getId() == R.id.rollup) {
			if (VISIBLE) {
				hideMenu();
			} else {
				showMenu();
			}
			return;
		}
				
		// 防止重复点击
		if (ClickUtil.isFastDoubleClick()) {
			return;
		}

		// 隐藏菜单栏
		if (VISIBLE) {
			hideMenu();
		} 
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
							// Toast.show(self, "已取消收藏");
						} else {
							Toast.show(self, rsp.data.message);
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
							// Toast.show(self, "收藏成功");
						} else {
							Toast.show(self, rsp.data.message);
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
		case R.id.add_to_shopcar: // 加入购物车

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
		case R.id.buynow: // 立即购买
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

	ProductSpecFilterDialog filterDialog;

	private void showPickDialog(int from) {
		if (goodsSpec == null) {
			Toast.show(self, "获取商品规格失败");
			return;
		}
		if (filterDialog == null) {
			filterDialog = new ProductSpecFilterDialog();
		}
		filterDialog.show(self, from, goodsid, goodsSpec, new ProdectSpecFilterCallBack() {
			@Override
			public void onDismiss() {
				// TODO Auto-generated method stub
			}

			@Override
			public void onSuccess() {
				Config.curShopCarNum += 1;
				showCurShopCarStatus();
			}
		});
	}
	
	
	public void setMargin(View view ,int top,int bottom) {
		RelativeLayout.LayoutParams layoutParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutParam.setMargins(0, top, 0, bottom);
		view.setLayoutParams(layoutParam);
		view.invalidate();
		product_detail_layout.invalidate();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		ImageLoader.getInstance().clearDiscCache();
		ImageLoader.getInstance().clearMemoryCache();
		super.onDestroy();
	}
}
