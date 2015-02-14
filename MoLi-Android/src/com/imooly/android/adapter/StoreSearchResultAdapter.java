package com.imooly.android.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.entity.RspBusinessSearch.BusinessEty;
import com.imooly.android.ui.StoreDetailActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 实体店商家搜索结果 listview适配器
 * 
 * @author lsd
 * 
 */
public class StoreSearchResultAdapter extends BaseAdapter {
	private List<BusinessEty> datas;

	public void setData(List<BusinessEty> data) {
		this.datas = data;
		this.notifyDataSetChanged();
	}

	public void addData(List<BusinessEty> list) {
		if (list == null)
			return;
		if (datas == null) {
			datas = new ArrayList<BusinessEty>();
		}
		this.datas.addAll(list);
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return datas == null ? 0 : datas.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return datas == null ? null : datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return datas == null ? 0 : position;
	}

	@Override
	public View getView(int position, View convertView, final ViewGroup parent) {
		// TODO Auto-generated method stub
		Tag tag;
		if (convertView == null) {
			tag = new Tag();
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_store_search_result_item, null);
			tag.item_pic = (ImageView) convertView.findViewById(R.id.item_pic);
			tag.item_name = (TextView) convertView.findViewById(R.id.item_name);
			tag.item_ratingBar = (RatingBar) convertView.findViewById(R.id.item_ratingBar);
			tag.item_type = (TextView) convertView.findViewById(R.id.item_type);
			tag.item_type_icon = (ImageView) convertView.findViewById(R.id.item_type_icon);
			tag.item_distance = (TextView) convertView.findViewById(R.id.item_distance);
			tag.item_location = (TextView) convertView.findViewById(R.id.item_location);
			convertView.setTag(tag);
		} else {
			tag = (Tag) convertView.getTag();
		}

		final BusinessEty entity = (BusinessEty) getItem(position);
		if (entity != null) {
			String icon = entity.getBusinessicon();
			if (null != icon) {
				ImageLoader.getInstance().displayImage(icon, tag.item_pic);
			}

			ImageLoader.getInstance().displayImage(entity.getIndustryicon(), tag.item_type_icon);
			
			String name = entity.getBusinessname();
			tag.item_name.setText(name == null ? "" : name);

			tag.item_ratingBar.setRating(Integer.parseInt(entity.getStarlevel()));

			String industry = entity.getIndustry();
			tag.item_type.setText(industry == null ? "" : industry);

			String distance = entity.getDistance();
			tag.item_distance.setText(distance == null ? "" : distance);

			String address = entity.getAddress();
			tag.item_location.setText(address == null ? "" : address);
		}

		convertView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Context context = parent.getContext();
				context.startActivity(new Intent(context, StoreDetailActivity.class).putExtra(StoreDetailActivity.EXTRA_BUSNESSID,
						entity.getBusinessid()));
			}
		});

		return convertView;
	}

	class Tag {
		ImageView item_pic;
		TextView item_name;
		RatingBar item_ratingBar;
		TextView item_type;
		ImageView item_type_icon;
		TextView item_distance;
		TextView item_location;
	}
}
