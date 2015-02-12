package com.imooly.android.adapter;

import java.util.List;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.entity.RspBusinessSearch.BusinessEty;
import com.nostra13.universalimageloader.core.ImageLoader;

/***
 * 实体店 猜你喜欢 - ListView适配器
 * 
 * @author lsd
 * 
 */
public class StoreRandListAdapter extends BaseAdapter {
	List<BusinessEty> list;

	public void setData(List<BusinessEty> list) {
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
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_store_rand_item, null);
			tag.store_rand_name = (TextView) convertView.findViewById(R.id.store_rand_name);
			tag.store_rand_type_icon= (ImageView) convertView.findViewById(R.id.store_rand_type_icon);
			tag.store_rand_type = (TextView) convertView.findViewById(R.id.store_rand_type);
			tag.store_rand_address = (TextView) convertView.findViewById(R.id.store_rand_address);
			tag.store_rand_pic = (ImageView) convertView.findViewById(R.id.store_rand_pic);
			convertView.setTag(tag);
		} else {
			tag = (Tag) convertView.getTag();
		}

		BusinessEty ety = (BusinessEty) getItem(position);
		if (ety != null) {
			ImageLoader.getInstance().displayImage(ety.getBusinessicon(), tag.store_rand_pic);
			ImageLoader.getInstance().displayImage(ety.getIndustryicon(), tag.store_rand_type_icon);
			String name = ety.getBusinessname();
			if (!TextUtils.isEmpty(name) && !"null".equals(name)) {
				tag.store_rand_name.setText(name);
			}
			String industry = ety.getIndustry();
			if (!TextUtils.isEmpty(industry) && !"null".equals(industry)) {
				tag.store_rand_type.setText(industry);
			}
			String adrs = ety.getAddress();
			if (!TextUtils.isEmpty(adrs) && !"null".equals(adrs)) {
				tag.store_rand_address.setText(adrs);
			}
		}

		return convertView;
	}

	class Tag {
		TextView store_rand_name;
		ImageView store_rand_type_icon;
		TextView store_rand_type;
		TextView store_rand_address;
		ImageView store_rand_pic;
	}
}
