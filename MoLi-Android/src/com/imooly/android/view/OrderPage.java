package com.imooly.android.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.adapter.OrderAdapter;
import com.imooly.android.entity.RspOrderList;
import com.imooly.android.entity.RspOrderList.OrderEty;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.tool.OrderOperate.OCallBack;
import com.imooly.android.widget.NoDataView;
import com.imooly.android.widget.PullToRefreshListView;
import com.imooly.android.widget.PullToRefreshListView.OnLoadMoreListener;
import com.imooly.android.widget.PullToRefreshListView.OnRefreshListener;

public class OrderPage extends LinearLayout implements View.OnClickListener {
	Context context;
	PullToRefreshListView lv_order_all;
	NoDataView ll_nodata;

	OrderAdapter adapter;
	List<OrderEty> orderList = new ArrayList<RspOrderList.OrderEty>();
	int curPage = 1;
	String status;

	public OrderPage(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public OrderPage(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init(context);
	}

	public void init(final Context context) {
		// TODO Auto-generated method stub
		this.context = context;
		LayoutInflater.from(context).inflate(R.layout.view_order_pageall, this);
		lv_order_all = (PullToRefreshListView) findViewById(R.id.lv_order_all);
		ll_nodata = (NoDataView) findViewById(R.id.ll_nodata);

		adapter = new OrderAdapter(context);
		adapter.setOCallback(oCallBack);

		lv_order_all.setAdapter(adapter);
		lv_order_all.setOnLoadListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				curPage++;
				getData(true);
			}
		});
		lv_order_all.setOnRefreshListener(new OnRefreshListener() {
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				curPage = 1;
				getData(false);
			}
		});
	}

	public void setData(String status) {
		// TODO Auto-generated method stub
		this.status = status;
		if (orderList != null && orderList.size() > 0) {
			adapter.setData(orderList);
		} else {
			curPage = 1;
			getData(false);
		}
	}

	public void getData(final boolean add) {
		Api.orderlist(context, status, curPage, 10, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				// TODO Auto-generated method stub
				lv_order_all.onLoadMoreComplete();
				lv_order_all.onRefreshComplete();
				RspOrderList rsp = (RspOrderList) rspData;
				List<OrderEty> list = rsp.data.getOrderlist();
				if (list == null || list.size() == 0) {
					if (!add) {
						ll_nodata.setVisibility(View.VISIBLE);
						ll_nodata.postHandle(NoDataView.noorder);
						lv_order_all.setVisibility(View.GONE);
					}
					return;
				}
				if (add) {
					orderList.addAll(list);
				} else {
					orderList = list;
				}
				adapter.setData(orderList);
			}

			@Override
			public void failed(String msg) {
				// TODO Auto-generated method stub
				lv_order_all.onLoadMoreComplete();
				lv_order_all.onRefreshComplete();
			}
		}, RspOrderList.class);
	}

	/***
	 * 订单选项点击回调
	 */
	OCallBack oCallBack = new OCallBack() {
		@Override
		public void success() {
			// TODO Auto-generated method stub
			curPage = 1;
			getData(false);
		}

		@Override
		public void failed(String msg) {
			// TODO Auto-generated method stub
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		default:
			break;
		}
	}
}
