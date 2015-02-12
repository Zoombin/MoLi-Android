package com.imooly.android.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.entity.RspAreaGood.ProvinceEty;
import com.imooly.android.entity.RspAreaGood.CityEty;

/***
 * 商品详情 筛选商品规格 地址适配器
 * 
 * @author lsd
 * 
 */
public class ProdectFilterAddressAdapter extends BaseAdapter {
	String type;
	List<?> list;

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
			ProvinceEty aent = (ProvinceEty) getItem(position);
			name = aent.getName();
		}
		if ("region".equals(type)) {
			CityEty cent = (CityEty) getItem(position);
			name = cent.getName();
		}
		tag.address.setText(name);
		return convertView;
	}

	class Tag {
		TextView address;
	}
}
