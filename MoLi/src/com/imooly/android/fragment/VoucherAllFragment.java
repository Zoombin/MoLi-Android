package com.imooly.android.fragment;

import java.util.Currency;

import android.app.Activity;
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
 * 代金券全部
 * 
 * @author
 * 
 */
public class VoucherAllFragment extends BaseFragment implements OnClickListener {

	private View view;
	private VoucherAllAdapter mAdapter;
	private int curPage = 1;
	private RelativeLayout rl_no_data = null;
	private PullUpListView mListView = null;

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub

		super.onAttach(activity);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Log.d("xxx", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>onCreate");
		super.onCreate(savedInstanceState);
	}
	
	
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
			
			mListView.setOnLoadListener(new OnLoadListener() {
				
				@Override
				public void onLoad() {
					getVoucherFlowList();
					
				}
			});
			
			rl_no_data = (RelativeLayout) view.findViewById(R.id.rl_none_voucher);
			View headerView = inflater.inflate(R.layout.head_view_5, null);
			
			mListView.addHeaderView(headerView, null, false);
			mAdapter = new VoucherAllAdapter();
			
			mListView.setAdapter(mAdapter);
		}
		

		return view;
	}
	

	
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub

		super.onDestroy();
	}
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub

		super.onDestroyView();
	}
	
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub

		super.onDetach();
	}

	@Override
	public void onStart() {
		
		// TODO Auto-generated method stub

		super.onStart();
	
	}
	
	@Override
	public void onStop() {
		
		// TODO Auto-generated method stub
		super.onStop();
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}
	
	
	@Override
	public void onResume() {
		
		super.onResume();

	}
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		
		super.onActivityCreated(savedInstanceState);
		
		// 处理viewerpager的tab之前的切换
		if (curPage > 1) {
			return;
		}
		
		getVoucherFlowList();
	}



	private void getVoucherFlowList() {
		Api.getVoucherFlow(mActivity, "all", curPage , 20 + "", new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {
				RspVoucherFlowList rsp = (RspVoucherFlowList) rspData;
				RspVoucherFlowList.Data data = rsp.data;
				
				// 没有数据的情况
				if(data.getVoucherflow().size() == 0 && curPage == 1) {
					// 显示没有的界面
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
