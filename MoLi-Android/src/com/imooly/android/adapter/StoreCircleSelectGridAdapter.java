package com.imooly.android.adapter;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.entity.RspBusinessCirclelist.Circle;
import com.imooly.android.entity.RspStoreCityList.City;

/***
 * 实体店 - 地址切换
 * 
 * @author lsd
 * 
 */
public class StoreCircleSelectGridAdapter extends BaseAdapter {
	List<Circle> list;
	List<Boolean> select;

	public void setData(int index, List<Circle> list) {
		if (list == null || list.size() == 0) {
			return;
		}
		select = new ArrayList<Boolean>();
		this.list = list;
		for (int i = 0; i < list.size(); i++) {
			select.add(false);
		}
		if(index >=0){
			select.set(index, true);
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
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_text_store, null);
			tag.bt = (TextView) convertView.findViewById(R.id.bt);
			convertView.setTag(tag);
		} else {
			tag = (Tag) convertView.getTag();
		}

		Circle ety = (Circle) getItem(position);
		tag.bt.setText(ety.getCirclename());
		if (select.get(position)) {
			tag.bt.setTextColor(parent.getResources().getColor(R.color.main_color));
			convertView.setBackgroundResource(R.drawable.layout_selector_all_line_pressed);
		} else {
			tag.bt.setTextColor(parent.getResources().getColor(R.color.app_text_gray));
			convertView.setBackgroundResource(R.drawable.layout_selector_all_line_nomol);
		}
		return convertView;
	}

	class Tag {
		TextView bt;
	}
}
