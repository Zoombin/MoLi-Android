package com.imooly.android.adapter;

import java.util.List;

import com.imooly.android.R;
import com.imooly.android.entity.RspOrderProfile.OrderLog;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/***
 * 订单详情ListView适配器
 * 
 * @author lsd
 * 
 */
public class OrderDetailListAdapter extends BaseAdapter {
	List<OrderLog> list;

	public void setData(List<OrderLog> list) {
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
		if (convertView == null) {
			convertView = new TextView(parent.getContext());
		}
		OrderLog log = (OrderLog) getItem(position);
		String s = log.getTitle() + "：" + log.getTime();
		if (!TextUtils.isEmpty(s)) {
			((TextView) convertView).setText(s);
			((TextView) convertView).setTextColor(Color.parseColor("#949494"));
		}
		return convertView;
	}

}
