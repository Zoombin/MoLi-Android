package com.imooly.android.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.adapter.OrderAdapter;
import com.imooly.android.entity.RspOrderList;
import com.imooly.android.entity.RspOrderList.OrderEty;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.tool.OrderOperate.OCallBack;
import com.imooly.android.widget.NoDataView;

public class OrderPage extends LinearLayout implements View.OnClickListener {
	Context context;
	ListView lv_order_all;
	NoDataView ll_nodata;

	OrderAdapter adapter;
	List<OrderEty> orderList = new ArrayList<RspOrderList.OrderEty>();
	int curPage = 1;
	String status="";
	boolean mCanScrollDown;

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
		lv_order_all = (ListView) findViewById(R.id.lv_order_all);
		ll_nodata = (NoDataView) findViewById(R.id.ll_nodata);
		adapter = new OrderAdapter(context);
		adapter.setOCallback(oCallBack);

		lv_order_all.setAdapter(adapter);
		lv_order_all.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// TODO Auto-generated method stub
				if (!mCanScrollDown && scrollState == SCROLL_STATE_IDLE) {
					// SCROLL_STATE_IDLE=0，滑动停止
					curPage++;
					getData(true);
				}
			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub
				if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
					mCanScrollDown = false;
				} else {
					mCanScrollDown = true;
				}
			}
		});
	}

	public void setData(String status) {
		// TODO Auto-generated method stub
		this.status = status;
		curPage = 1;
		getData(false);
	}

	public void getData(final boolean add) {
		Api.orderlist(context, status, curPage, 10, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				// TODO Auto-generated method stub
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
