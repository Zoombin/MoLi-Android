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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.imooly.android.widget.AutoLoadListView.OnLoadMoreListener;
import com.imooly.android.widget.NoDataView;
import com.imooly.android.widget.PageListView;
import com.imooly.android.widget.PageListView.OnViewScrollListener;
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

	//listHeader占位用
	private View listHeader;
	private View holderView;
	
	private LinearLayout ll_slide_view;//需要显示 隐藏的
	private RelativeLayout rl_result_content;
	private ImageView iv_back;
	private EditText mEditView;
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
	private ImageView holder_iv_businessicon;
	private PageListView category_lv_product_list;
	private ProductGridViewAdapter productgridviewadapter;
	private ProductListViewAdapter productlistviewadapter;
	private int page = 1;
	private int pagesize = 20;
	private ProductFilterDialog dialog;
	List<GoodsEty> goods;
	List<SpecEty> specs;
	List<String> pricelist;
	List<StoreEty> stores;
	String categorythreeid;
	String keyword_show, keyword;
	private Resources resources;
	private String price = "";
	private int stockflag = 1;
	private int voucherflag = 0;
	private String spec = "";
	private Button btn_lv_page;
	private Button btn_lv_back;
	private NoDataView ll_nodata;
	boolean loading = false;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category_product_list);

		logActivityName(this);
		resources = getResources();
		
		categorythreeid = getIntent().getStringExtra(EXTRA_CATEGORYTHREEID);
		keyword_show = keyword = getIntent().getStringExtra(EXTRA_KEYWORD);
		
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
		
		initView();
		
		initData();
		
		// 选中最新tab
		getFilterData(ORDERBY_TIME, orderway, layout_item_time, product_list_tab_item_time);
	}
	
	
	private void initView() {
		// TODO Auto-generated method stub
		rl_result_content = (RelativeLayout) findViewById(R.id.rl_result_content);
		login_code_del_imageView = (TextView) findViewById(R.id.login_code_del_imageView);
		
		mEditView = (EditText) findViewById(R.id.home_homepage_et_input);
		mEditView.setOnClickListener(this);

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
		
		category_lv_product_list = (PageListView) findViewById(R.id.category_lv_product_list);
		// 需要影藏的头部
		ll_slide_view = (LinearLayout) findViewById(R.id.ll_slide_view);
		//list的头部 占位用
		listHeader = LayoutInflater.from(self).inflate(R.layout.category_product_holder, null);
		holderView = listHeader.findViewById(R.id.holder_view);
		category_lv_product_list.addHeaderView(listHeader);
		
		iv_businessicon = (ImageView) findViewById(R.id.iv_businessicon);
		
		btn_lv_page = (Button) findViewById(R.id.btn_lv_page);
		btn_lv_back = (Button) findViewById(R.id.btn_lv_back);
		btn_lv_back.setOnClickListener(this);
		
		category_lv_product_list.setButton(btn_lv_page, btn_lv_back);
		
		//category_lv_product_list.setStateView(ll_slide_view);//头部滑动的view
		//隐藏头部在外面处理
		category_lv_product_list.setOnViewScrollListener(new OnViewScrollListener() {
			@Override
			public void onScrollUp() {
				// TODO Auto-generated method stub
				if (ll_slide_view.getVisibility() == View.VISIBLE) {
					//处理不满一屏时不隐藏头部
					int firstVisible = category_lv_product_list.getFirstVisiblePosition();
					if(firstVisible != 0){
						//超过一屏了
						ll_slide_view.setVisibility(View.GONE);
					}
				}
			}
			@Override
			public void onScrollDown() {
				// TODO Auto-generated method stub
				if (ll_slide_view.getVisibility() == View.GONE) {
					ll_slide_view.setVisibility(View.VISIBLE);
				}
			}
		});
		category_lv_product_list.setOnLoadListener(new OnLoadMoreListener() {
			
			@Override
			public void onLoadMore() {
				if (loading) {
					return;
				}
				
				page++;
				getData();
			}
		});
		
		ll_nodata = (NoDataView) findViewById(R.id.ll_nodata);

		goods = new ArrayList<GoodsEty>();
		stores = new ArrayList<StoreEty>();
	}
	
	
	private void initData() {
		mEditView.setText(keyword_show);
	}
	
	private void getData() {
		loading = true;
		Api.goodsSearch(this, categorythreeid, keyword, price, spec, stockflag, voucherflag, orderby, orderway, page, pagesize, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				loading = false;
				category_lv_product_list.onLoadMoreComplete();
				btn_switch.setVisibility(View.VISIBLE);
				btn_filter.setVisibility(View.VISIBLE);
				category_lv_product_list.setVisibility(View.VISIBLE);
				ll_nodata.postHandle(NoDataView.defult);
				
				// 设置占位的高度
				LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) holderView.getLayoutParams();
				params.height = ll_slide_view.getHeight();
				holderView.setLayoutParams(params);
				

				RspGoodsSearch rsp = (RspGoodsSearch) rspData;
				int totalpage = rsp.data.totalpage;
			    category_lv_product_list.setNum(totalpage, pagesize);
			    category_lv_product_list.setGridType(!view_listview_state);
				
				List<GoodsEty>  rspgoods = rsp.data.goodslist;
				specs = rsp.data.speclist;
				pricelist = rsp.data.pricelist;
				final List<StoreEty> rspstores = rsp.data.storelist;
				
				if (rspgoods == null || rspgoods.size() == 0) {
					if(page == 1){
						//没有搜索到数据
						category_lv_product_list.setVisibility(View.GONE);
						ll_nodata.postHandle(NoDataView.nosearch);
					}else{
						//Toast.makeText(self, "无更多商品信息！", Toast.LENGTH_SHORT).show();
						category_lv_product_list.setNoMoreData(true);
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
				
				/**旗舰店图片*/
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
						productlistviewadapter.notifyDataSetChanged();
					}
				} else {
					if (productgridviewadapter == null) {
						productgridviewadapter = new ProductGridViewAdapter(self, goods); 
						category_lv_product_list.setAdapter(productgridviewadapter);
					} else {
						productgridviewadapter.notifyDataSetChanged();
					}
				}
			}
			
			@Override
			public void failed(String msg) {
				loading = false;
				category_lv_product_list.onLoadMoreComplete();
				Toast.show(self, msg);
				if("网络异常!".equals(msg)){
					category_lv_product_list.setVisibility(View.GONE);
					ll_nodata.postHandle(NoDataView.nonet);
					ll_nodata.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							getData();
						}
					});
				}
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
			mEditView.setText("");
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
				dialog = new ProductFilterDialog(self, specs,pricelist,new ProductFilterCallBack() {
					@Override
					public void onSelect(SelectModel model) {
						spec = "";
						if (!TextUtils.isEmpty(model.spec)) {
							try {
								spec = URLEncoder.encode(model.spec, "UTF-8");
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}
						}
						price = "";
						if (!TextUtils.isEmpty(model.price)) {
							try {
								price = URLEncoder.encode(model.price, "UTF-8");
							} catch (UnsupportedEncodingException e) {
								e.printStackTrace();
							}
						}
						
						stockflag = model.stockflag;
						voucherflag = model.voucherflag;
						
						if(loading){
							return;
						}
						page = 1;
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
			intent.putExtra("keyword_show", keyword_show);
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
		if (l != null) { 
			layout_item_time.setBackgroundResource(android.R.color.transparent);
			product_list_tab_item_time.setTextColor(resources.getColor(R.color.app_text_dark_gray));
			layout_item_order.setBackgroundResource(android.R.color.transparent);
			if (orderway.equals("") || orderway.equals(ORDERWAY_DESC)) {
				iv_price_order.setImageResource(R.drawable.product_price_down);
			} else {
				iv_price_order.setImageResource(R.drawable.product_price_up);
			}
			product_list_tab_item_price.setTextColor(resources.getColor(R.color.app_text_dark_gray));
			layout_item_salesvolume.setBackgroundResource(android.R.color.transparent);
			product_list_tab_item_salesvolume.setTextColor(resources.getColor(R.color.app_text_dark_gray));
			layout_item_hignopinion.setBackgroundResource(android.R.color.transparent);
			product_list_tab_item_hignopinion.setTextColor(resources.getColor(R.color.app_text_dark_gray));
			
			l.setBackgroundResource(R.drawable.tab_selected);
			v.setTextColor(resources.getColor(R.color.main_color));
		}
		
		if(loading){
			return;
		}
		page = 1;
		getData();
	}
}
