package com.imooly.android.adapter;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.imooly.android.R;

/***
 * 商品搜索 筛选商品 HListView适配器
 * 
 * @author lsd
 * 
 */
public class ProdectSelectFilterAdapter extends BaseAdapter {
	public interface ItemClick {
		void onClick(int Position);
	}

	List<String> list;
	List<Boolean> select = new ArrayList<Boolean>();
	ItemClick itemClick;

	public void setOnItemClick(ItemClick itemClick) {
		this.itemClick = itemClick;
	}

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

	public void cleanSelect() {
		for (int i = 0; i < list.size(); i++) {
			select.set(i, false);
		}
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Tag tag;
		if (convertView == null) {
			tag = new Tag();
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_prodect_filter_txt, null);
			tag.txt = (TextView) convertView.findViewById(R.id.txt);
			convertView.setTag(tag);
		} else {
			tag = (Tag) convertView.getTag();
		}

		String ety = (String) getItem(position);
		tag.txt.setText(ety);
		if (select.get(position)) {
			tag.txt.setTextColor(parent.getResources().getColor(R.color.white));
			tag.txt.setBackgroundResource(R.drawable.layout_selector_all_dash_pressed_v2);
		} else {
			tag.txt.setTextColor(parent.getResources().getColor(R.color.app_text_gray));
			tag.txt.setBackgroundResource(R.drawable.layout_selector_all_dash_nomol);
		}
		if (itemClick != null) {
			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					// 这样写为了屏蔽外出滑动的问题
					itemClick.onClick(position);
				}
			});
		}
		return convertView;
	}

	class Tag {
		TextView txt;
	}
}
