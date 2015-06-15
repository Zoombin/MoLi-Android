package com.imooly.android.view;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.adapter.ProdectFilterAddressAdapter;
import com.imooly.android.widget.CustomDialog;
import com.imooly.android.widget.CustomDialog.CGravity;

/***
 * 
 * @author LSD
 * 
 */
public class AddressFilterDialogTwo {
	static List<?> cityList;

	public interface AddressTwoFilterCallBack {
		void onSelect(Object model);
	}

	public void show(Context context, final String type, List<?> list, final AddressTwoFilterCallBack callback) {
		cityList = list;
		final View view = LayoutInflater.from(context).inflate(R.layout.dialog_filter_address_two, null);
		final CustomDialog dialog = CustomDialog.create(context);
		dialog.setCanceledOnTouchOutside(false);

		if ("province".equals(type)) {
			((TextView) view.findViewById(R.id.title)).setText("选择省份");
		}
		if ("region".equals(type)) {
			((TextView) view.findViewById(R.id.title)).setText("选择区域");
		}
		ListView listView = (ListView) view.findViewById(R.id.list);
		ProdectFilterAddressAdapter adapter = new ProdectFilterAddressAdapter();
		listView.setAdapter(adapter);
		adapter.setData(type, cityList);

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
}
