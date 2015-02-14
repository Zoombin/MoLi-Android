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
import com.imooly.android.adapter.AddressProvinceListAdapter;
import com.imooly.android.entity.RspAddressCityTown.Town;
import com.imooly.android.widget.CustomDialog;
import com.imooly.android.widget.CustomDialog.CGravity;

/***
 * 
 * @author
 * 
 */
public class AddressSelectTownDialog {
	static List<?> townList;

	public interface AddressSelectTownCallBack {
		void onSelect(Object model);
		void onSelectAid(int aid);
	}

	public void show(Context context, List<Town> list, final AddressSelectTownCallBack callback) {
		townList = list;
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_filter_address_two, null);
		final CustomDialog dialog = CustomDialog.create(context);
		dialog.setCanceledOnTouchOutside(false);

		((TextView) view.findViewById(R.id.title)).setText("选择区域");

		ListView listView = (ListView) view.findViewById(R.id.list);
		
		AddressProvinceListAdapter adapter = new AddressProvinceListAdapter();
		listView.setAdapter(adapter);
		adapter.setData("town", townList);

		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				Object model = parent.getAdapter().getItem(position);
				Town selecTown = (Town)model;
				
				if (callback != null) {
					callback.onSelectAid(selecTown.getAid());
					callback.onSelect(model);
				}
				dialog.dismiss();
			}
		});

		dialog.setContentVw(view).setGravity(CGravity.CENTER).show();
	}
	

}
