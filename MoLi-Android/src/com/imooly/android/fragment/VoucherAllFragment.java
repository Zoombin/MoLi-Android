package com.imooly.android.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;

import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.adapter.VoucherAllAdapter;
import com.imooly.android.base.BaseFragment;
import com.imooly.android.entity.RspVoucherFlowList;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.widget.Toast;

/**
 * 代金券全部
 * 
 * @author
 * 
 */
public class VoucherAllFragment extends BaseFragment implements OnClickListener {

	private View view;
	private VoucherAllAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_voucher_all, container, false);
		
		
		ListView listView = (ListView) view.findViewById(R.id.list_voucher_all);
		
		View headerView = inflater.inflate(R.layout.head_view_5, null);
		listView.addHeaderView(headerView);

		adapter = new VoucherAllAdapter();
		listView.setAdapter(adapter);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getVoucherFlowList();
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	private void getVoucherFlowList() {
		Api.getVoucherFlow(mActivity, "all", 1 + "", 20 + "", new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {
				RspVoucherFlowList rsp = (RspVoucherFlowList) rspData;
				RspVoucherFlowList.Data data = rsp.data;
				adapter.setData(data.getVoucherflow());
			}

			@Override
			public void failed(String msg) {
				Toast.show(mActivity, msg);
			}
		}, RspVoucherFlowList.class);
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
