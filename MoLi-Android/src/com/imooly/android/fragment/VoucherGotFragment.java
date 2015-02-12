package com.imooly.android.fragment;

import android.os.Bundle;
import android.util.Log;
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
 * 代金券获得
 * 
 * @author
 * 
 */
public class VoucherGotFragment extends BaseFragment implements OnClickListener {

	private View view;
	private VoucherAllAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_voucher_all, container, false);
		ListView listView = (ListView) view.findViewById(R.id.list_voucher_all);
		adapter = new VoucherAllAdapter();
		listView.setAdapter(adapter);
		
		Log.e("xxx", "voucher got fragment");

		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getVoucherFlowList();
	}

	private void getVoucherFlowList() {
		Api.getVoucherFlow(getActivity(), "get", 1 + "", 20 + "", new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {
				RspVoucherFlowList rsp = (RspVoucherFlowList) rspData;
				RspVoucherFlowList.Data data = rsp.data;
				adapter.setData(data.getVoucherflow());
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
