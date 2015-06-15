package com.imooly.android.view;

import java.util.List;

import javax.security.auth.PrivateCredentialPermission;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.adapter.AddressProvinceListAdapter;
import com.imooly.android.entity.RspAddressProvince;
import com.imooly.android.entity.RspAddressProvince.ItemProvince;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.widget.CustomDialog;
import com.imooly.android.widget.CustomDialog.CGravity;

/***
 * 
 * @author
 * 
 */
public class AddressSelectProvinceDialog {

	private Context mContext = null;
	private ListView listView = null;
	private List<ItemProvince> provinces;

	public interface AddressProvinceCallBack {
		void onSelect(Object model);
	}

	public void show(Context context, final AddressProvinceCallBack callback) {
		this.mContext = context;
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_filter_address_two, null);
		final CustomDialog dialog = CustomDialog.create(context);
		dialog.setCanceledOnTouchOutside(false);

		((TextView) view.findViewById(R.id.title)).setText("选择省份");
		listView = (ListView) view.findViewById(R.id.list);

		if (provinces == null) {
			getProvinceList();
		} else {
			AddressProvinceListAdapter adapter = new AddressProvinceListAdapter();
			listView.setAdapter(adapter);
			adapter.setData("province", provinces);
		}

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Object model = parent.getAdapter().getItem(position);
				if (callback != null) {
					callback.onSelect(model);
				}
				dialog.dismiss();
			}
		});

		dialog.setContentVw(view).setGravity(CGravity.CENTER).show();
	}

	private void getProvinceList() {

		Api.getAdressProvince(mContext, new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {
				RspAddressProvince rsp = (RspAddressProvince) rspData;
				RspAddressProvince.Data data = rsp.data;

				provinces = data.getAddressProvince();

				AddressProvinceListAdapter adapter = new AddressProvinceListAdapter();
				listView.setAdapter(adapter);

				adapter.setData("province", provinces);

			}

			@Override
			public void failed(String msg) {
				// TODO Auto-generated method stub

			}
		}, RspAddressProvince.class);

	}

}
