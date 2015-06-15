package com.imooly.android.ui;

import java.util.List;

import javax.crypto.spec.PSource;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.adapter.AddressReceiverAdapter;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.entity.RspAddressList;
import com.imooly.android.entity.RspAddressList.Address;
import com.imooly.android.entity.RspSuccessCommon;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.widget.CustomDialog;
import com.imooly.android.widget.CustomDialog.CGravity;
import com.imooly.android.widget.Toast;

/**
 * 
 * @author
 * 
 */
public class AddressActivity extends BaseActivity implements OnClickListener, OnItemClickListener ,OnItemLongClickListener{

	private RelativeLayout rl_title_address;
	private LinearLayout rl_none_address;
	private ImageView iv_back;
	private TextView tv_title;

	private Button button_add_address;

	private AddressReceiverAdapter adapter;

	private ListView list_address;
	// private CustomProgressDialog dialog;

	private CustomDialog dialog;

	private List<Address> mList;

	private int myPosition;

	private int mCurPager = 1;

	private static final int PAGER_SIZE = 1000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_address);

		logActivityName(this);

		initView();
		initData();

	}

	@Override
	protected void onStart() {
		if (adapter != null) {
			adapter.cleanData();
		}

		getData();
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initView() {

		rl_title_address = (RelativeLayout) findViewById(R.id.rl_title_address);
		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);
		tv_title = (TextView) findViewById(R.id.tv_title);

		list_address = (ListView) findViewById(R.id.list_address);
		list_address.setOnItemClickListener(this);
		list_address.setOnItemLongClickListener(this);

		 LayoutInflater inflater = LayoutInflater.from(this);
		 View headerView = inflater.inflate(R.layout.head_view_5, null);
		 list_address.addHeaderView(headerView);

		button_add_address = (Button) findViewById(R.id.button_add_address);
		button_add_address.setOnClickListener(this);

		rl_none_address = (LinearLayout) findViewById(R.id.rl_none_address);

	}

	private void initData() {
		if (adapter == null) {
			adapter = new AddressReceiverAdapter(AddressActivity.this);
		}

		list_address.setAdapter(adapter);
		
		// 表示是跳转到收货地址界面，获取地址的，隐藏，设置默认
		Intent intent = getIntent();
		if(intent.hasExtra("action")){
			adapter.setSelectAddress(true);
		}
	}

	private void getData() {

		// lastpulltime 时间戳 Utils.getUnixTime()

		Api.addresslist(self, 0 + "", mCurPager, PAGER_SIZE, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {

				RspAddressList rsp = (RspAddressList) rspData;
				if (rsp.data != null) {

					mList = rsp.data.getAddresslist();

					if (mList == null || mList.size() == 0) {
						rl_none_address.setVisibility(View.VISIBLE);
						return;
					} else {
						rl_none_address.setVisibility(View.GONE);
						adapter.addData(mList);
					}

				}

			}

			@Override
			public void failed(String msg) {
				// TODO Auto-generated method stub
			}
		}, RspAddressList.class);
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.iv_back:
			setResult(RESULT_OK);
			finish();
			break;
		case R.id.button_add_address:

			startActivity(new Intent(self, AddressModifyActivity.class));
			break;

		default:
			break;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {  // postion 从1开始
		Log.d("xxx", ">>>>>>>>>>>>>>>>>>>>>>position = " + position);
		myPosition = position -1;
		
		Intent intent = getIntent();
		if(intent.hasExtra("action")){  // 从订单跳转过来
			
			RspAddressList.Address address = (Address) adapter.getItem(myPosition);
			
			intent.putExtra("selAddr", address);
			setResult(RESULT_OK, intent);
			finish();
		} else {
			
			// 设置为默认地址
			TextView text_check = (TextView) view.findViewById(R.id.text_check);
			
			if (adapter.getList().get(myPosition).getIsdefault() == 0 ) {
				setDefaultAddress(self, adapter.getList().get(myPosition).getAddressid(), myPosition, text_check);
			}
			
		}
		


	}
	
	
	private void setDefaultAddress(Context context, String addressId, int pos, final TextView tv) {

		final int position = pos;

		Api.setDefaultAddress(context, addressId, new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {
				RspSuccessCommon rsp = (RspSuccessCommon) rspData;
				RspSuccessCommon.Data data = rsp.data;
				if (data.getSuccess() == 1) {
					Toast.show(self, "设置成功");
					

					List<Address> list = adapter.getList();
					// 设置默认收货地址
					for (int i = 0; i < list.size(); i++) {
						list.get(i).setIsdefault(0);
					}

					list.get(position).setIsdefault(1);

					adapter.notifyDataSetChanged();
					
					tv.setText("已默认");
				}
			}

			@Override
			public void failed(String msg) {
				Toast.show(self, "设置失败");
			}
		}, RspSuccessCommon.class);
	}
	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
		// TODO Auto-generated method stub
		Log.d("xxx", "onItemLongClick position = " + position);
		

		myPosition = position -1;

		showPickDialog(self, new DialogCallBack() {

			@Override
			public void onFirstClick() { // 编辑
				Intent intent = new Intent(self, AddressModifyActivity.class);

				RspAddressList.Address address = (Address) adapter.getItem(myPosition);

				String addressid = address.getAddressid();
				String name = address.getName();
				String mobile = address.getMobile();
				String postcode = address.getPostcode();

				int pid = address.getProvinceid();
				int cid = address.getCityid();
				int aid = address.getAreaid();

				String province = address.getProvince();
				String city = address.getCity();
				String town = address.getArea();

				String detailAddress = address.getStreet();

				int isDefault = address.getIsdefault();

				StringBuilder builder = new StringBuilder(province);
				if (!province.equals(city)) {
					builder.append(city).append(town);
				} else {
					builder.append(town);
				}

				intent.putExtra("addressid", addressid);
				intent.putExtra("name", name);
				intent.putExtra("mobile", mobile);
				intent.putExtra("postcode", postcode);
				intent.putExtra("address", builder.toString());
				intent.putExtra("detailAddress", detailAddress);
				intent.putExtra("isDefault", isDefault);
				intent.putExtra("pid", pid);
				intent.putExtra("cid", cid);
				intent.putExtra("aid", aid);
				startActivity(intent);
			}

			@Override
			public void onSecondClick() { // 删除
				RspAddressList.Address address = (Address) adapter.getItem(myPosition);
				deleteAddressItem(address.getAddressid(), myPosition);
			}
		});
		return true;
	}

	private void deleteAddressItem(String addressId, final int position) {

		Api.deleteAddressItem(self, addressId, new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {

				RspSuccessCommon rsp = (RspSuccessCommon) rspData;
				RspSuccessCommon.Data data = rsp.data;
				
				if (data.getSuccess() == 1) {
					Toast.show(self, "删除成功");
					adapter.deleteData(position);

					if (adapter.getCount() == 0) {
						rl_none_address.setVisibility(View.VISIBLE);
					}

				} else {
					Toast.show(self, "删除失败");
				}

			}

			@Override
			public void failed(String msg) {
				Toast.show(self, msg);
			}
		}, RspSuccessCommon.class);
	}

	public interface DialogCallBack {
		void onFirstClick();

		void onSecondClick();
	}

	public void showPickDialog(Context context, final DialogCallBack callback) {

		final View view = LayoutInflater.from(context).inflate(R.layout.dialog_address_edit, null);
		final CustomDialog dialog = CustomDialog.create(context);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentVw(view).setGravity(CGravity.CENTER).show();

		view.findViewById(R.id.close).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		view.findViewById(R.id.text_first).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if (callback != null) {
					callback.onFirstClick();
				}
			}
		});

		view.findViewById(R.id.text_second).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if (callback != null) {
					callback.onSecondClick();
				}
			}
		});
	}
}
