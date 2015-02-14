package com.imooly.android.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.imooly.android.R;

/***
 * 水平ListView适配器
 * 
 * @author lsd
 * 
 */
public class AfterSaleAdapter extends BaseAdapter {
	List<String> list;

	public AfterSaleAdapter() {
		// TODO Auto-generated constructor stub
	}

	public void setData(List<String> list) {
		this.list = list;
		this.notifyDataSetChanged();
	}

	public void cleanData() {
		if (list != null) {
			list.clear();
		}
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		//return list == null ? 0 : list.size();
		
		return 20;
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
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_after_sale, null);
			tag.image_pic = (ImageView) convertView.findViewById(R.id.image_after_sale);
			tag.text_after_sale = (TextView) convertView.findViewById(R.id.text_after_sale);
			tag.text_number = (TextView) convertView.findViewById(R.id.text_number);
			tag.text_state = (TextView) convertView.findViewById(R.id.text_state);
			convertView.setTag(tag);
		} else {
			tag = (Tag) convertView.getTag();
		}

		return convertView;
	}

	class Tag {
		ImageView image_pic;
		TextView text_after_sale;
		TextView text_number;
		TextView text_state;
	}
}
