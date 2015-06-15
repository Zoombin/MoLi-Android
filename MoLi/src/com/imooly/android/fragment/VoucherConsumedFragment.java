package com.imooly.android.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.adapter.VoucherAllAdapter;
import com.imooly.android.base.BaseFragment;
import com.imooly.android.entity.RspVoucherFlowList;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.pull.PullUpListView;
import com.imooly.android.pull.PullUpListView.OnLoadListener;
import com.imooly.android.widget.Toast;

/**
 * 代金券已使用
 * 
 * @author
 * 
 */
public class VoucherConsumedFragment extends BaseFragment implements OnClickListener {

	private View view;
	private VoucherAllAdapter mAdapter;
	private RelativeLayout rl_no_data = null;
	private int curPage = 1;
	private PullUpListView mListView = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		
		if (view != null) {
			
			ViewGroup parent = (ViewGroup) view.getParent();
            if (null != parent) {
                parent.removeView(view);
            }
            
		} else {
			
			
			view = inflater.inflate(R.layout.fragment_voucher_all, container, false);
			mListView = (PullUpListView) view.findViewById(R.id.list_voucher_all);
			View headerView = inflater.inflate(R.layout.head_view_5, null);
			mListView.addHeaderView(headerView, null, false);
			
			mListView.setOnLoadListener(new OnLoadListener() {

				@Override
				public void onLoad() {
					getVoucherFlowList();

				}
			});

			// 添加头部view


			rl_no_data = (RelativeLayout) view.findViewById(R.id.rl_none_voucher);
			mAdapter = new VoucherAllAdapter();
			mListView.setAdapter(mAdapter);

			Log.e("xxx", "voucher got fragment");
			
		}
		


		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getVoucherFlowList();

	}

	private void getVoucherFlowList() {
		Api.getVoucherFlow(getActivity(), "use", curPage, 20 + "", new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {
				RspVoucherFlowList rsp = (RspVoucherFlowList) rspData;
				RspVoucherFlowList.Data data = rsp.data;

				// 显示没有数据的界面
				if (data.getVoucherflow().size() == 0 && curPage == 1) {
					rl_no_data.setVisibility(View.VISIBLE);
					return;
				}

				// 有数据的情况
				if (data.getVoucherflow() != null && data.getVoucherflow().size() > 0) {
					mAdapter.addData(data.getVoucherflow());
					curPage++;
					Log.d("xxx", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>curpage = " + curPage);
				}

				// 必须放在最后，为了显示全部加载
				mListView.onLoadCompleted();
			}

			@Override
			public void failed(String msg) {
				Toast.show(getActivity(), msg);
			}
		}, RspVoucherFlowList.class);
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cleardata:

			break;
		default:
			break;
		}
	}
}
