package com.imooly.android.fragment;

import java.util.List;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.location.f;
import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.adapter.OrderServiceStatusAdapter;
import com.imooly.android.base.BaseFragment;
import com.imooly.android.entity.RspSgoodslist;
import com.imooly.android.entity.RspSgoodslist.SGoodsEty;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.tool.OrderOperate.OCallBack;
import com.imooly.android.widget.NoDataView;
import com.imooly.android.widget.PullToRefreshListView;
import com.imooly.android.widget.PullToRefreshListView.OnLoadMoreListener;
import com.imooly.android.widget.Toast;

/**
 * 售后状态Fragment
 * 
 * @author LSD
 * 
 */
public class OrderServiceStatusFragment extends BaseFragment {
	PullToRefreshListView listView;
	OrderServiceStatusAdapter adapter;

	NoDataView noDataView;

	int curPage = 1;
	String type = "";

	public OrderServiceStatusFragment(String type) {
		// TODO Auto-generated constructor stub
		this.type = type;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_order_service_status, container, false);
		initView(view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		initData();
		//getData(false);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getData(false);
	}

	private void initView(View view) {
		listView = (PullToRefreshListView) view.findViewById(R.id.listview);
		noDataView = (NoDataView) view.findViewById(R.id.nodata);
	}

	private void initData() {
		listView.setOnLoadListener(loadmore);

		adapter = new OrderServiceStatusAdapter(mActivity);
		adapter.setOCallback(oBack);
		listView.setAdapter(adapter);
	}

	public void getData(final boolean addData) {
		Api.sgoodslist(mActivity, type, curPage, 10, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				// TODO Auto-generated method stub
				RspSgoodslist rsp = (RspSgoodslist) rspData;
				if (rsp.data != null) {
					List<SGoodsEty> list = rsp.data.getGoodslist();
					if (list != null && list.size() > 0) {
						if (addData) {
							adapter.addData(rsp.data.getGoodslist());
						} else {
							adapter.setData(type, rsp.data.getGoodslist());
						}

						if (rsp.data.getTotal() == adapter.getCount()) {
							listView.setCanLoadMore(false);
						}
					} else {
						if (!addData) {
							listView.setVisibility(View.GONE);
							noDataView.setVisibility(View.VISIBLE);
							if ("return".equals(type)) {
								noDataView.postHandle(NoDataView.notuihuo);
							} else if ("change".equals(type)) {
								noDataView.postHandle(NoDataView.nohuanhuo);
							}
						}
					}
				}
			}

			@Override
			public void failed(String msg) {
				// TODO Auto-generated method stub
				if (!TextUtils.isEmpty(msg)) {
					Toast.show(mActivity, msg);
				}
			}
		}, RspSgoodslist.class);
	}

	OnLoadMoreListener loadmore = new OnLoadMoreListener() {
		@Override
		public void onLoadMore() {
			// TODO Auto-generated method stub
			listView.setCanLoadMore(true);
			curPage++;
			getData(true);

		}
	};
	
	OCallBack oBack = new OCallBack() {
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
}
