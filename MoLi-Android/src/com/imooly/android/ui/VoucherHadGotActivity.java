package com.imooly.android.ui;

import java.util.List;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.adapter.VouncherGetAdapter;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.entity.RspSuccessCommon;
import com.imooly.android.entity.RspVoucherAmount;
import com.imooly.android.entity.RspVoucherList;
import com.imooly.android.entity.RspVoucherList.Voucher;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.widget.CustomDialog;
import com.imooly.android.widget.CustomDialog.CGravity;
import com.imooly.android.widget.Toast;

public class VoucherHadGotActivity extends BaseActivity implements OnItemClickListener, View.OnClickListener {
	private RelativeLayout rl_title;
	private ListView mListView;
	private VouncherGetAdapter adapter;
	private ImageView button_back;
	private RelativeLayout rl_none_voucher;
	private static final String PAGE_SIZE = "8";
	private int mCurPager = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vouncher_get);

		logActivityName(this);

		initView();
		initData();
	}

	private void initView() {

		rl_title = (RelativeLayout) findViewById(R.id.rl_title);

		mListView = (ListView) findViewById(R.id.list_vouncher_get);
		mListView.setOnItemClickListener(this);

		button_back = (ImageView) findViewById(R.id.button_back_voucher);
		button_back.setOnClickListener(this);

		LayoutInflater inflater = (LayoutInflater) self.getSystemService(LAYOUT_INFLATER_SERVICE);
		View head_view = inflater.inflate(R.layout.head_view, null);

		mListView.addHeaderView(head_view);
		mListView.addFooterView(head_view);
		
		rl_none_voucher = (RelativeLayout) findViewById(R.id.rl_none_voucher);
		
	}

	private void initData() {

		if (adapter == null) {
			adapter = new VouncherGetAdapter();
		}

		mListView.setAdapter(adapter);

		getVoucherListData();

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.button_back_voucher:

			finish();

			break;

		default:
			break;
		}

	}

	// 获取可领取的代金券列表
	private void getVoucherListData() {

		Api.getVoucherList(self, mCurPager, PAGE_SIZE, new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {
				RspVoucherList rsp = (RspVoucherList) rspData;
				RspVoucherList.Data data = rsp.data;
				
				List<Voucher> list= data.getVoucherlist();
				
				if (mCurPager == 1 && list.size() == 0) {
					rl_none_voucher.setVisibility(View.VISIBLE);
				} else {
					adapter.addData(list);
				}

			}

			@Override
			public void failed(String msg) {
				Toast.show(self, msg);
			}

		}, RspVoucherList.class);
	}

	// 领取代金券
	private void getVoucherByOrderNO(String orderNumber, int postion) {
		final int pos = postion;
		Api.takeVoucher(self, orderNumber, new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {
				RspSuccessCommon rsp = (RspSuccessCommon) rspData;
				RspSuccessCommon.Data data = rsp.data;
				if (data.getSuccess() == 1) {
					Toast.show(self, "获取代金券成功！");
					
					adapter.deleteData(pos);
					
					// 内容为空的时候
					if (adapter.getCount() == 0) {
						rl_none_voucher.setVisibility(View.VISIBLE);
					}
					
				} else {
					Toast.show(self, "获取代金券失败！");
				}
			}

			@Override
			public void failed(String msg) {
				Toast.show(self, msg);

			}
		}, RspSuccessCommon.class);
	}

	// 订单可领取代金券金额
	private void getVoucherAmountByOrderNumber(final String orderNumber, final int voucherNumClient, final int position) {

		Api.getVoucherAmount(self, orderNumber, new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {
				RspVoucherAmount rsp = (RspVoucherAmount) rspData;
				RspVoucherAmount.Data data = rsp.data;
				int voucherNumber = data.getVoucher();

				Log.d("xxx", "voucher number = " + voucherNumber);

				if (voucherNumber == 0) {
					showInvalidSelectDialog(self);
				} else {

					String title = "可领取 " + voucherNumber;

					if (voucherNumber < voucherNumClient) {
						title = "可领取的金额已变为 " + voucherNumber;
					}

					showSelectDialog(self, title, new DialogCallBack() {

						@Override
						public void onSure() {
							getVoucherByOrderNO(orderNumber, position);
						}

						@Override
						public void onCancel() {

						}
					});
				}
			}

			@Override
			public void failed(String msg) {

			}
		}, RspVoucherAmount.class);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		Voucher voucher = (Voucher) adapter.getItem(position);
		String orderNo = voucher.getOrderno();
		int voucherClient = voucher.getVoucher();
		getVoucherAmountByOrderNumber(orderNo, voucherClient, position-1);
	}

	public interface DialogCallBack {
		void onCancel();

		void onSure();
	}

	public void showSelectDialog(Context context, String title, final DialogCallBack callback) {

		final View view = LayoutInflater.from(context).inflate(R.layout.dialog_voucher_select, null);
		final CustomDialog dialog = CustomDialog.create(context);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentVw(view).setGravity(CGravity.CENTER).show();

		TextView textView = (TextView) view.findViewById(R.id.text_dialog_title);
		textView.setText(title);

		view.findViewById(R.id.button_cancel).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if (callback != null) {
					callback.onCancel();
				}
			}
		});

		view.findViewById(R.id.button_sure).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if (callback != null) {
					callback.onSure();
				}
			}
		});
	}

	public void showInvalidSelectDialog(Context context) {

		final View view = LayoutInflater.from(context).inflate(R.layout.dialog_voucher_invalid_select, null);
		final CustomDialog dialog = CustomDialog.create(context);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentVw(view).setGravity(CGravity.CENTER).show();

		view.findViewById(R.id.button_sure).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
	}

}
