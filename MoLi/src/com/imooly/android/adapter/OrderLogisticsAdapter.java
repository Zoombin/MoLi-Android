package com.imooly.android.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.entity.RspOrderList.OrderEty;

/***
 * 订单物流适配器
 * 
 * @author lsd
 *
 */
public class OrderLogisticsAdapter extends BaseAdapter {
	List<OrderEty> list;

	public void setData(List<OrderEty> list) {
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
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_logistics_item, null);
			tag.order_logistics_tag = (ImageView) convertView.findViewById(R.id.order_logistics_tag);
			tag.order_logistics_info = (TextView) convertView.findViewById(R.id.order_logistics_info);
			tag.order_logistics_time = (TextView) convertView.findViewById(R.id.order_logistics_time);
			convertView.setTag(tag);
		} else {
			tag = (Tag) convertView.getTag();
		}
		

		OrderEty order = (OrderEty) getItem(position);
		if (order != null) {
		}
		return convertView;
	}

	class Tag {
		ImageView order_logistics_tag;
		TextView order_logistics_info;
		TextView order_logistics_time;
	}
}
