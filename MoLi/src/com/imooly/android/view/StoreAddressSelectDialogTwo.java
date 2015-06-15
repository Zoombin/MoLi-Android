package com.imooly.android.view;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.adapter.StoreAddressSelectAdapter;
import com.imooly.android.entity.RspStoreCityList.City;
import com.imooly.android.entity.RspStoreCityList.CityGroup;
import com.imooly.android.widget.CustomDialog;
import com.imooly.android.widget.CustomDialog.CGravity;

/***
 * 
 * 实体店城市选择 （省）
 * 
 * 废弃了
 * 
 * @author LSD
 * 
 */

@Deprecated
public class StoreAddressSelectDialogTwo implements OnClickListener {
	private Context context;
	private AddressSelectCallBackTwo callback;
	private RelativeLayout ll_title;
	private ImageView iv_back;
	private TextView tv_title;

	CustomDialog dialog;

	StoreAddressSelectAdapter adapter;

	public interface AddressSelectCallBackTwo {
		void onSelect(CityGroup mode);
	}

	public StoreAddressSelectDialogTwo(Context context, AddressSelectCallBackTwo callback) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.callback = callback;

		adapter = new StoreAddressSelectAdapter();
	}

	public void show(List<CityGroup> cityList, City curCity) {
		final View view = LayoutInflater.from(context).inflate(R.layout.activity_storechangecity, null);
		dialog = CustomDialog.create(context, R.style.FilterDialogStyleBottom);

		ll_title = (RelativeLayout) view.findViewById(R.id.ll_title);
		iv_back = (ImageView) view.findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);

		tv_title = (TextView) view.findViewById(R.id.tv_title);

		ListView listView = (ListView) view.findViewById(R.id.list);
		adapter = new StoreAddressSelectAdapter();
		listView.setAdapter(adapter);

		String cityName = curCity.getName();
		tv_title.setText("当前城市：" + cityName);

		adapter.setData(cityList.get(0).getCities());

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				CityGroup ctG = (CityGroup) adapter.getItem(position);
				callback.onSelect(ctG);
				dialog.dismiss();
			}
		});

		dialog.setContentVw(view).setGravity(CGravity.CENTER).loadAnimation(R.style.citychangeAnim).show();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_back:
			dialog.dismiss();
			break;

		default:
			break;
		}
	}

}
