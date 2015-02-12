package com.imooly.android.ui;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.adapter.StoreSearchResultAdapter;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.db.DB_Location;
import com.imooly.android.entity.RspBusinessCirclelist;
import com.imooly.android.entity.RspBusinessCirclelist.Circle;
import com.imooly.android.entity.RspBusinessClassifyList;
import com.imooly.android.entity.RspBusinessClassifyList.ClassifyEntity;
import com.imooly.android.entity.RspBusinessClassifyList.SubClassifyEntity;
import com.imooly.android.entity.RspBusinessSearch;
import com.imooly.android.entity.RspBusinessSearch.BusinessEty;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.tool.Config;
import com.imooly.android.utils.Utils;
import com.imooly.android.view.StoreSearchResultFilterView;
import com.imooly.android.view.StoreSearchResultFilterView.StoreSearchResultFilter;
import com.imooly.android.widget.NoDataView;
import com.imooly.android.widget.PullToRefreshListView;
import com.imooly.android.widget.PullToRefreshListView.OnLoadMoreListener;
import com.imooly.android.widget.Toast;

/***
 * 实体店 - 商家搜索结果页
 * 
 * @author lsd
 * 
 */
public class StoreSearchResultActivity extends BaseActivity implements OnClickListener {
	public static enum FilterType {
		CATEGRORY, CIRCLE, SORT;
	}

	public static String SEARCH_KEY = "searchkey";
	public static String SEARCH_ID = "search_id";
	public static String PARENT_ID = "parent_id";
	public static String ENTRY_TYPE = "entry_type";
	private RelativeLayout ll_title;
	private ImageView iv_back;
	private TextView tv_title;
	private ImageView location_mark;
	private ImageView search_mark;

	private RelativeLayout layout_businessclassify;
	private TextView tv_businessclassify;
	private ImageView iv_businessclassify;

	private RelativeLayout layout_circlelist;
	private TextView tv_circlelist;
	private ImageView iv_circlelist;

	private RelativeLayout layout_sort;
	private TextView tv_sort;
	private ImageView iv_sort;

	private LinearLayout ll_searchtip;
	private TextView search_tip;
	private PullToRefreshListView listView;
	StoreSearchResultFilterView filterView;
	private Button btn_lv_page;
	private Button btn_lv_back;
	private NoDataView ll_nodata;

	List<ClassifyEntity> tempClassfyList;
	List<Circle> tempCirclelist;

	private int curPage = 1;
	private int pagesize = 20;
	private StoreSearchResultAdapter adapter;

	private String entryType = "";
	private String searchKey = "";
	private String classifyid = "";
	private String circleid = "";
	private String distance = "";
	private String sort = "1";

	private int clafyPSelect = 0;
	private int circPSelect = 0;
	private int sortPSelect = 0;

	private List<BusinessEty> searchResult;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_search_result);

		logActivityName(this);

		initView();
		initData();
	}

	private void initView() {
		ll_title = (RelativeLayout) findViewById(R.id.ll_title);

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);

		tv_title = (TextView) findViewById(R.id.tv_title);

		location_mark = (ImageView) findViewById(R.id.location_mark);
		location_mark.setOnClickListener(this);

		search_mark = (ImageView) findViewById(R.id.search_mark);
		search_mark.setOnClickListener(this);

		layout_businessclassify = (RelativeLayout) findViewById(R.id.layout_businessclassify);
		layout_businessclassify.setOnClickListener(this);
		tv_businessclassify = (TextView) findViewById(R.id.tv_businessclassify);
		iv_businessclassify = (ImageView) findViewById(R.id.iv_businessclassify);

		layout_circlelist = (RelativeLayout) findViewById(R.id.layout_circlelist);
		layout_circlelist.setOnClickListener(this);
		tv_circlelist = (TextView) findViewById(R.id.tv_circlelist);
		iv_circlelist = (ImageView) findViewById(R.id.iv_circlelist);

		layout_sort = (RelativeLayout) findViewById(R.id.layout_sort);
		layout_sort.setOnClickListener(this);
		tv_sort = (TextView) findViewById(R.id.tv_sort);
		iv_sort = (ImageView) findViewById(R.id.iv_sort);

		filterView = (StoreSearchResultFilterView) findViewById(R.id.ll_filter_view);
		filterView.setCallBack(filterCallBack);
		filterView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// filterView.startAnimation(shrinkUpAnim);
				cleanStatus();
				filterView.setVisibility(View.GONE);
			}
		});

		ll_searchtip = (LinearLayout) findViewById(R.id.ll_searchtip);
		search_tip = (TextView) findViewById(R.id.search_tip);
		listView = (PullToRefreshListView) findViewById(R.id.list);
		listView.setAdapter(adapter = new StoreSearchResultAdapter());
		listView.setOnLoadListener(new OnLoadMoreListener() {
			@Override
			public void onLoadMore() {
				curPage++;
				search();
			}
		});

		btn_lv_page = (Button) findViewById(R.id.btn_lv_page);
		btn_lv_back = (Button) findViewById(R.id.btn_lv_back);
		btn_lv_back.setOnClickListener(this);
		listView.setButton(btn_lv_page, btn_lv_back);

		ll_nodata = (NoDataView) findViewById(R.id.ll_nodata);
	}

	private void initData() {
		// 初始化数据
		searchResult = new ArrayList<RspBusinessSearch.BusinessEty>();

		// 获取实体店分类
		List<ClassifyEntity> classifylist = null;
		try {
			classifylist = Config.getBusinessClassList();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (classifylist != null) {
			tempClassfyList = classifylist;
		}
		// 判断 实体店分类是否有更新
		getBusinessClassifyList();

		// 获取城市下的区和商圈
		getCirclelist();

		// 从关键字进来的(关键字)
		if (getIntent().hasExtra(SEARCH_KEY)) {
			searchKey = getIntent().getStringExtra(SEARCH_KEY);
			search_tip.setText(searchKey);
		}
		try {
			searchKey = URLEncoder.encode(searchKey, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		// 从分类进来的(分类id)
		if (getIntent().hasExtra(SEARCH_ID)) {
			classifyid = getIntent().getStringExtra(SEARCH_ID);
		}

		// 父分类
		if (getIntent().hasExtra(PARENT_ID)) {
			String parent_id = getIntent().getStringExtra(PARENT_ID);
			if (tempClassfyList != null && tempClassfyList.size() > 0) {
				for (int i = 0; i < tempClassfyList.size(); i++) {
					ClassifyEntity cEty = tempClassfyList.get(i);
					if (parent_id.equals(cEty.getClassifyid())) {
						clafyPSelect = i;
						tv_businessclassify.setText(cEty.getClassifyname());
					}
				}
			}
		}

		// 入口
		if (getIntent().hasExtra(ENTRY_TYPE)) {
			entryType = getIntent().getStringExtra(ENTRY_TYPE);
		}
		if ("category_search".equals(entryType)) {
			ll_searchtip.setVisibility(View.GONE);
			search_mark.setVisibility(View.VISIBLE);
		} else if ("key_search".equals(entryType)) {
			ll_searchtip.setVisibility(View.VISIBLE);
			search_mark.setVisibility(View.GONE);
		}

		search();
	}

	private void getBusinessClassifyList() {
		String BusinessClassLastpulltime = Config.getBusinessClassLastpulltime();

		Api.businessClassifyList(this, BusinessClassLastpulltime, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				RspBusinessClassifyList rsp = (RspBusinessClassifyList) rspData;
				if (rsp.data != null && rsp.data.getClassifylist() != null) {
					if (tempClassfyList != null) {
						// tempClassfyList.addAll(rsp.data.getClassifylist());
					} else {
						tempClassfyList = rsp.data.getClassifylist();
					}

					// 保存最后更新时间
					Config.setBusinessClassLastpulltime(Utils.getNowTime());
					Config.setBusinessClassList(tempClassfyList);
				}
			}

			@Override
			public void failed(String msg) {
				Toast.show(self, msg);
			}
		}, RspBusinessClassifyList.class);
	}

	private void getCirclelist() {
		// IP地址
		String ipAddress = Utils.getLocalIpAddress();
		DB_Location db_location = new DB_Location(self);
		String cityid = db_location.getCurCityId();

		Api.businessCirclelist(this, cityid, ipAddress, db_location.getLatitude(), db_location.getLontitude(),
				new NetCallBack<ServiceResult>() {
					@Override
					public void success(ServiceResult rspData) {
						RspBusinessCirclelist rsp = (RspBusinessCirclelist) rspData;
						if (rsp.data != null) {
							List<Circle> cirList = rsp.data.getCirclelist();// 商圈
							List<String> neList = rsp.data.getNear();// 附近

							if (neList != null && neList.size() > 0) {
								List<Circle> subs = new ArrayList<Circle>();
								for (String string : neList) {
									Circle sub = new Circle();
									sub.setCid("000");
									sub.setCirclename(string);
									subs.add(sub);
								}
								Circle circle = new Circle();
								circle.setCid("000");
								circle.setCirclename("附近");
								circle.setSub(subs);

								if (cirList != null) {
									cirList.add(0, circle);
								}
							}

							tempCirclelist = cirList;
						}

					}

					@Override
					public void failed(String msg) {
						Toast.show(self, msg);
					}
				}, RspBusinessCirclelist.class);
	}

	private void search() {
		DB_Location db_location = new DB_Location(self);
		String lng = db_location.getLontitude();
		String lat = db_location.getLatitude();
		String cityid = db_location.getCurCityId();
		Api.businessSearch(self, cityid, classifyid, circleid, distance, searchKey, lng, lat, sort, curPage, pagesize,
				new NetCallBack<ServiceResult>() {
					@Override
					public void success(ServiceResult rspData) {
						listView.onLoadMoreComplete();
						listView.onRefreshComplete();
						RspBusinessSearch rsp = (RspBusinessSearch) rspData;
						if (rsp.data != null) {
							int totalPage = rsp.data.getTotalpage();
							listView.setNum(totalPage, pagesize);

							if (curPage == 1) {
								// 重新加载时 清空原有数据
								searchResult.clear();
								adapter.notifyDataSetChanged();
							}

							List<BusinessEty> list = rsp.data.getStorelist();
							if (list != null && list.size() > 0) {
								searchResult.addAll(list);
								adapter.setData(searchResult);
							} else {
								if (curPage == 1) {
									// 没有搜索到数据
									ll_nodata.setVisibility(View.VISIBLE);
									ll_nodata.postHandle(NoDataView.nosearch);
								} else {
									// 没有更多数据
								}
							}
						}
					}

					@Override
					public void failed(String msg) {
						if (!TextUtils.isEmpty(msg)) {
							Toast.show(self, msg);
						}
						listView.onLoadMoreComplete();
						listView.onRefreshComplete();
					}
				}, RspBusinessSearch.class);
	}

	private void cleanStatus() {
		tv_businessclassify.setSelected(false);
		iv_businessclassify.setSelected(false);
		layout_businessclassify.setSelected(false);

		tv_sort.setSelected(false);
		iv_sort.setSelected(false);
		layout_sort.setSelected(false);

		tv_circlelist.setSelected(false);
		iv_circlelist.setSelected(false);
		layout_circlelist.setSelected(false);
	}

	private void setSelect(int id) {
		cleanStatus();

		switch (id) {
		case R.id.layout_businessclassify:
			tv_businessclassify.setSelected(true);
			iv_businessclassify.setSelected(true);
			layout_businessclassify.setSelected(true);
			break;
		case R.id.layout_circlelist:
			tv_circlelist.setSelected(true);
			iv_circlelist.setSelected(true);
			layout_circlelist.setSelected(true);
			break;
		case R.id.layout_sort:
			tv_sort.setSelected(true);
			iv_sort.setSelected(true);
			layout_sort.setSelected(true);
			break;
		default:
			break;
		}

	}

	/***
	 * 筛选回调
	 */
	StoreSearchResultFilter filterCallBack = new StoreSearchResultFilter() {
		@Override
		public void onSelect(FilterType type, Object data, int PSelect) {
			curPage = 1;
			filterView.setVisibility(View.GONE);
			if (type == FilterType.SORT) {
				String string = (String) data;
				if ("最新发布".equals(string)) {
					sort = "2";
					tv_sort.setText("最新发布");
				} else {
					sort = "1";
					tv_sort.setText("距离优先");
				}
				tv_sort.setSelected(false);
				iv_sort.setSelected(false);
				sortPSelect = PSelect;
			}
			if (type == FilterType.CATEGRORY) {
				SubClassifyEntity fyEntity = (SubClassifyEntity) data;
				classifyid = fyEntity.getClassifyid();
				tv_businessclassify.setText(fyEntity.getClassifyname());
				tv_businessclassify.setSelected(false);
				iv_businessclassify.setSelected(false);
				clafyPSelect = PSelect;
			}
			if (type == FilterType.CIRCLE) {
				Circle airEntity = (Circle) data;
				String id = airEntity.getCid();
				if ("000".equals(id)) {
					distance = airEntity.getCirclename();
					circleid = "";
				} else {
					circleid = id;
					distance = "0";
					tv_circlelist.setText(airEntity.getCirclename());
					tv_circlelist.setSelected(false);
					iv_circlelist.setSelected(false);
				}
				circPSelect = PSelect;
			}

			search();
		}
	};

	@Override
	public void onClick(View v) {
		ll_nodata.setVisibility(View.GONE);
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.location_mark:
			startActivity(new Intent(self, SearchMapActivity.class).putExtra(SearchMapActivity.SEARCH_ENTITYS, (Serializable) searchResult));
			break;
		case R.id.search_mark:
			startActivity(new Intent(self, StoreSearchActivity.class));
			finish();
			break;
		case R.id.layout_businessclassify:
			if (v.isSelected() && filterView.getVisibility() == View.VISIBLE) {
				filterView.setVisibility(View.GONE);
				cleanStatus();
			} else {
				if (tempClassfyList == null) {
					return;
				}
				setSelect(R.id.layout_businessclassify);
				filterView.setVisibility(View.VISIBLE);
				filterView.show(FilterType.CATEGRORY, tempClassfyList, clafyPSelect);
			}
			break;
		case R.id.layout_circlelist:
			if (v.isSelected() && filterView.getVisibility() == View.VISIBLE) {
				filterView.setVisibility(View.GONE);
				cleanStatus();
			} else {
				if (tempCirclelist == null) {
					return;
				}
				setSelect(R.id.layout_circlelist);
				filterView.setVisibility(View.VISIBLE);
				filterView.show(FilterType.CIRCLE, tempCirclelist, circPSelect);
			}
			break;
		case R.id.layout_sort:
			if (v.isSelected() && filterView.getVisibility() == View.VISIBLE) {
				filterView.setVisibility(View.GONE);
				cleanStatus();
			} else {
				setSelect(R.id.layout_sort);

				List<String> sortList = new ArrayList<String>();
				sortList.add("距离优先");
				sortList.add("最新发布");

				filterView.setVisibility(View.VISIBLE);
				filterView.show(FilterType.SORT, sortList, sortPSelect);
			}
			break;
		case R.id.btn_lv_back:
			// 回到顶部
			listView.requestFocusFromTouch();
			listView.setSelection(0);
			break;
		default:
			break;
		}
	}
}
