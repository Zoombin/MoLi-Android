package com.imooly.android.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Switch;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.entity.RspAddressCityTown.Town;
import com.imooly.android.entity.RspAddressProvince.ItemProvince;
import com.imooly.android.entity.RspAreaGood.CityEty;
import com.imooly.android.entity.RspAreaGood.ProvinceEty;

/***
 * 地址列表 包括省、市、镇
 * 
 * @author
 * 
 */
public class AddressProvinceListAdapter extends BaseAdapter {
	List<?> list;
	String type;
	
	public void setData(String type, List<?> list) {
		this.type = type;
		this.list = list;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list == null ? 0 : list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list == null ? null : list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return list == null ? 0 : position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Tag tag;
		if (convertView == null) {
			tag = new Tag();
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_address, null);
			tag.address = (TextView) convertView.findViewById(R.id.address);
			convertView.setTag(tag);
		} else {
			tag = (Tag) convertView.getTag();
		}
		
		String name = "";
		
		
		if ("province".equals(type)) {
			ItemProvince item = (ItemProvince) getItem(position);
			name = item.getName();
		}
		if ("city".equals(type)) {
			String cityName = (String) getItem(position);
			name = cityName;
		}
		
		if ("town".equals(type)) {
			Town town = (Town) getItem(position);
			name = town.getName();
		}
		
		tag.address.setText(name);
		

		return convertView;
	}

	class Tag {
		TextView address;
	}
}
