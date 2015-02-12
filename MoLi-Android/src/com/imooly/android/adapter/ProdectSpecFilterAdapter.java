package com.imooly.android.adapter;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.imooly.android.R;

/***
 * 商品详情 筛选商品规格 GridView适配器
 * 
 * @author lsd
 * 
 */
public class ProdectSpecFilterAdapter extends BaseAdapter {
	List<String> list;
	List<Boolean> select = new ArrayList<Boolean>();

	public void setData(List<String> list) {
		this.list = list;
		for (int i = 0; i < list.size(); i++) {
			select.add(false);
		}
		this.notifyDataSetChanged();
	}

	public void changeSelect(int position) {
		for (int i = 0; i < list.size(); i++) {
			select.set(i, false);
		}
		select.set(position, true);
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
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_button, null);
			tag.bt = (TextView) convertView.findViewById(R.id.bt);
			convertView.setTag(tag);
		} else {
			tag = (Tag) convertView.getTag();
		}

		String ety = (String) getItem(position);
		tag.bt.setText(ety);
		if (select.get(position)) {
			tag.bt.setTextColor(parent.getResources().getColor(R.color.white));
			tag.bt.setBackgroundResource(R.drawable.layout_selector_all_dash_pressed_v2);
		} else {
			tag.bt.setTextColor(parent.getResources().getColor(R.color.app_text_gray));
			tag.bt.setBackgroundResource(R.drawable.layout_selector_all_dash_nomol);
		}
		return convertView;
	}

	class Tag {
		TextView bt;
	}
}
