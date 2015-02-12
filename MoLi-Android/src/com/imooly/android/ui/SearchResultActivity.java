package com.imooly.android.ui;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.adapter.ProductGridViewAdapter;
import com.imooly.android.adapter.ProductListViewAdapter;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.entity.RspGoodsSearch;
import com.imooly.android.entity.RspGoodsSearch.GoodsEty;
import com.imooly.android.entity.RspGoodsSearch.SpecEty;
import com.imooly.android.entity.RspGoodsSearch.StoreEty;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.view.ProductFilterDialog;
import com.imooly.android.view.ProductFilterDialog.ProductFilterCallBack;
import com.imooly.android.view.ProductFilterDialog.SelectModel;
import com.imooly.android.widget.NoDataView;
import com.imooly.android.widget.PullToRefreshListView;
import com.imooly.android.widget.PullToRefreshListView.OnLoadMoreListener;
import com.imooly.android.widget.Toast;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * 搜索结果画面
 * 
 * @author daiye
 * 
 */
public class SearchResultActivity extends BaseActivity implements OnClickListener {

	public static final String EXTRA_CATEGORYTHREEID = "categorythreeid";
	public static final String EXTRA_KEYWORD = "keyword";
	private final String ORDERBY_TIME = "time";
	private final String ORDERBY_PRICE = "price";
	private final String ORDERBY_SALESVOLUME = "salesvolume";
	private final String ORDERBY_HIGNOPINION = "hignopinion";
	private String orderby = ORDERBY_TIME;
	private String orderway = "";
	private final String ORDERWAY_ASC = "0";
	private final String ORDERWAY_DESC = "1";

	private LinearLayout ll_slide_view;//需要显示 隐藏的
	private LinearLayout home_homepage_top_search_layout;
	private ImageView iv_back;
	private EditText home_homepage_et_input;
	private TextView login_code_del_imageView;
	private Button btn_switch;
	private TextView btn_filter;
	private LinearLayout layout_item_time;
	private LinearLayout layout_item_order;
	private LinearLayout layout_item_salesvolume;
	private LinearLayout layout_item_hignopinion;
	private TextView product_list_tab_item_time;
	private ImageView iv_price_order;
	private TextView product_list_tab_item_price;
	private TextView product_list_tab_item_salesvolume;
	private TextView product_list_tab_item_hignopinion;
	private ImageView iv_businessicon;
	private PullToRefreshListView category_lv_product_list;
	private ProductGridViewAdapter productgridviewadapter;
	private ProductListViewAdapter productlistviewadapter;
	private int page = 1;
	private int pagesize = 20;
	private ProductFilterDialog dialog;
	List<GoodsEty> goods;
	List<SpecEty> specs;
	List<StoreEty> stores;
	String categorythreeid;
	String keyword;
	private Resources resources;
	private String price = null;
	private int stockflag = 1;
	private int voucherflag = 1;
	private String spec = "";
	private Button btn_lv_page;
	private Button btn_lv_back;
	private NoDataView ll_nodata;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category_product_list);

		logActivityName(this);
		
		resources = getResources();
		
		categorythreeid = getIntent().getStringExtra(EXTRA_CATEGORYTHREEID);
		keyword = getIntent().getStringExtra(EXTRA_KEYWORD);
//		categorythreeid = "53eac7c87556f4ed5ba51026";
//		categorythreeid = "";
//		keyword = "手机";
		if (TextUtils.isEmpty(categorythreeid)) {
			categorythreeid = "";
		}
		if (TextUtils.isEmpty(keyword)) {
			keyword = "";
		}
		
		try {
			keyword = URLEncoder.encode(keyword, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		ll_slide_view = (LinearLayout) findViewById(R.id.ll_slide_view);
		home_homepage_top_search_layout = (LinearLayout) findViewById(R.id.home_homepage_top_search_layout);

		login_code_del_imageView = (TextView) findViewById(R.id.login_code_del_imageView);
		
		home_homepage_et_input = (EditText) findViewById(R.id.home_homepage_et_input);
		home_homepage_et_input.setOnClickListener(this);

		login_code_del_imageView.setOnClickListener(this);
		
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
		
		btn_switch = (Button) findViewById(R.id.btn_switch);
		btn_switch.setOnClickListener(this);
		
		btn_filter = (TextView) findViewById(R.id.btn_filter);
		btn_filter.setOnClickListener(this);
		
		layout_item_time = (LinearLayout) findViewById(R.id.layout_item_time);
		layout_item_time.setOnClickListener(this);
		layout_item_order = (LinearLayout) findViewById(R.id.layout_item_order);
		layout_item_order.setOnClickListener(this);
		layout_item_salesvolume = (LinearLayout) findViewById(R.id.layout_item_salesvolume);
		layout_item_salesvolume.setOnClickListener(this);
		layout_item_hignopinion = (LinearLayout) findViewById(R.id.layout_item_hignopinion);
		layout_item_hignopinion.setOnClickListener(this);
		
		product_list_tab_item_time = (TextView) findViewById(R.id.product_list_tab_item_time);
		iv_price_order = (ImageView) findViewById(R.id.iv_price_order);
		product_list_tab_item_price = (TextView) findViewById(R.id.product_list_tab_item_price);
		product_list_tab_item_salesvolume = (TextView) findViewById(R.id.product_list_tab_item_salesvolume);
		product_list_tab_item_hignopinion = (TextView) findViewById(R.id.product_list_tab_item_hignopinion);
		
		category_lv_product_list = (PullToRefreshListView) findViewById(R.id.category_lv_product_list);
		iv_businessicon = (ImageView) findViewById(R.id.iv_businessicon);
		
		btn_lv_page = (Button) findViewById(R.id.btn_lv_page);
		btn_lv_back = (Button) findViewById(R.id.btn_lv_back);
		btn_lv_back.setOnClickListener(this);
		
		category_lv_product_list.setButton(btn_lv_page, btn_lv_back);
		category_lv_product_list.setStateView(ll_slide_view);//头部滑动的view
		category_lv_product_list.setOnLoadListener(new OnLoadMoreListener() {
			
			@Override
			public void onLoadMore() {
				page++;
				getData();
			}
		});
		
		ll_nodata = (NoDataView) findViewById(R.id.ll_nodata);

		goods = new ArrayList<GoodsEty>();
		stores = new ArrayList<StoreEty>();
		
		// 选中最新tab
		getFilterData(ORDERBY_TIME, orderway, layout_item_time, product_list_tab_item_time);
	}
	
	private void getData() {
		Api.goodsSearch(this, categorythreeid, keyword, price, spec, stockflag, voucherflag, orderby, orderway, page, pagesize, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				category_lv_product_list.onLoadMoreComplete();
				btn_switch.setVisibility(View.VISIBLE);
				btn_filter.setVisibility(View.VISIBLE);

				RspGoodsSearch rsp = (RspGoodsSearch) rspData;
				int totalpage = rsp.data.totalpage;
			    category_lv_product_list.setNum(totalpage, pagesize);
			    category_lv_product_list.setGridType(!view_listview_state);
				
				List<GoodsEty>  rspgoods = rsp.data.goodslist;
				specs = rsp.data.speclist;
				final List<StoreEty> rspstores = rsp.data.storelist;
				
				if (rspgoods == null || rspgoods.size() == 0) {
					if(page == 1){
						//没有搜索到数据
						ll_nodata.setVisibility(View.VISIBLE);
						ll_nodata.postHandle(NoDataView.nosearch);
					}else{
						//Toast.makeText(self, "无更多商品信息！", Toast.LENGTH_SHORT).show();
						return;
					}
				}
				// 重新搜索时，清空数据
				if (page == 1) {
					goods.clear();
					productlistviewadapter = null;
					productgridviewadapter = null;
				}
				goods.addAll(rspgoods);
				
				if (rspstores != null && rspstores.size() != 0) {
					iv_businessicon.setVisibility(View.VISIBLE);
					DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
						.showImageOnLoading(R.drawable.ic_loading_640_160)
					    .showImageForEmptyUri(R.drawable.ic_error_640_160)  // empty URI时显示的图片  
					    .showImageOnFail(R.drawable.ic_error_640_160)      // 不是图片文件 显示图片  
						.cacheInMemory(true)
						.bitmapConfig(Bitmap.Config.RGB_565)  // 图片压缩质量
						.cacheOnDisc(true)			
						.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
						.build();
					ImageLoader.getInstance().displayImage(rspstores.get(0).businessimage, iv_businessicon, defaultOptions);
					iv_businessicon.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View arg0) {
							Intent intent = new Intent(self, StoreProActivity.class);
							intent.putExtra(StoreProActivity.EXTRA_BUSINESSID, rspstores.get(0).businessid);
							startActivity(intent);
						}
					});
				} else {
					iv_businessicon.setVisibility(View.GONE);
				}
				
				if (view_listview_state) {
					if (productlistviewadapter == null) {
						productlistviewadapter = new ProductListViewAdapter(self, goods);
						category_lv_product_list.setAdapter(productlistviewadapter);
					} else {
//						productlistviewadapter.addGoodsList(rspgoods);
						productlistviewadapter.notifyDataSetChanged();
					}
				} else {
					if (productgridviewadapter == null) {
						productgridviewadapter = new ProductGridViewAdapter(self, goods); 
						category_lv_product_list.setAdapter(productgridviewadapter);
					} else {
//						productgridviewadapter.addGoodsList(rspgoods);
						productgridviewadapter.notifyDataSetChanged();
					}
				}
			}
			
			@Override
			public void failed(String msg) {
				category_lv_product_list.onLoadMoreComplete();
				Toast.show(self, msg);
			}
		}, RspGoodsSearch.class);
	}

	boolean view_listview_state = true;
	
	@Override
	public void onClick(View v) {
		ll_nodata.setVisibility(View.GONE);
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.login_code_del_imageView:
			home_homepage_et_input.setText("");
			break;
		case R.id.btn_switch:
			if (!view_listview_state) {
				btn_switch.setBackgroundResource(R.drawable.btn_title_selector_list);
				productlistviewadapter = new ProductListViewAdapter(self, goods);
				category_lv_product_list.setAdapter(productlistviewadapter);
				view_listview_state = true;
			} else {
				btn_switch.setBackgroundResource(R.drawable.btn_title_selector_grid);
				productgridviewadapter = new ProductGridViewAdapter(self, goods);
				category_lv_product_list.setAdapter(productgridviewadapter);
				view_listview_state = false;
			}
			category_lv_product_list.setGridType(!view_listview_state);
			btn_lv_back.setVisibility(View.GONE);
			break;
		case R.id.btn_filter:
			if (dialog == null) {
				dialog = new ProductFilterDialog(self, specs,new ProductFilterCallBack() {
					@Override
					public void onSelect(SelectModel model) {
						spec = model.spec;
						price = model.price;
						stockflag = model.stockflag;
						voucherflag = model.voucherflag;
						getData();
					}
				});
			}
			dialog.dialog.show();
			break;
		case R.id.layout_item_time:
			page = 1;
			getFilterData(ORDERBY_TIME, "", layout_item_time, product_list_tab_item_time);
			
			break;
		case R.id.layout_item_order:
			page = 1;
			if (orderway.equals("") || orderway.equals(ORDERWAY_DESC)) {
				orderway = ORDERWAY_ASC;
			} else {
				orderway = ORDERWAY_DESC;
			}
			getFilterData(ORDERBY_PRICE, orderway, layout_item_order, product_list_tab_item_price);

			break;
		case R.id.layout_item_salesvolume:
			page = 1;
			getFilterData(ORDERBY_SALESVOLUME, "", layout_item_salesvolume, product_list_tab_item_salesvolume);
			
			break;
		case R.id.layout_item_hignopinion:
			page = 1;
			getFilterData(ORDERBY_HIGNOPINION, "", layout_item_hignopinion, product_list_tab_item_hignopinion);

			break;
		case R.id.btn_lv_back:
			// 回到顶部
			category_lv_product_list.requestFocusFromTouch();
			category_lv_product_list.setSelection(0);
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					ll_slide_view.setVisibility(View.VISIBLE);
				}
			}, 500);
			break;
		case R.id.home_homepage_et_input: 
			Intent intent = new Intent();
			intent.setClass(self, SearchActivity.class);
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
	}
	
	private void getFilterData(String orderby, String orderway, LinearLayout l, TextView v) {
		this.orderby = orderby;
		this.orderway = orderway;
		
		getData();

		if (l != null) {
			layout_item_time.setBackgroundResource(android.R.color.transparent);
			product_list_tab_item_time.setTextColor(resources.getColor(R.color.weak_text_color));
			layout_item_order.setBackgroundResource(android.R.color.transparent);
			if (orderway.equals("") || orderway.equals(ORDERWAY_DESC)) {
				iv_price_order.setImageResource(R.drawable.product_price_up);
			} else {
				iv_price_order.setImageResource(R.drawable.product_price_down);
			}
			product_list_tab_item_price.setTextColor(resources.getColor(R.color.weak_text_color));
			layout_item_salesvolume.setBackgroundResource(android.R.color.transparent);
			product_list_tab_item_salesvolume.setTextColor(resources.getColor(R.color.weak_text_color));
			layout_item_hignopinion.setBackgroundResource(android.R.color.transparent);
			product_list_tab_item_hignopinion.setTextColor(resources.getColor(R.color.weak_text_color));
			
			l.setBackgroundResource(R.drawable.tab_selected);
			v.setTextColor(resources.getColor(R.color.main_color));
		}
	}
}
