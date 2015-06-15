package com.imooly.android.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.entity.RspStoreCommentList.StoreComment;

/***
 * 实体店 评价 - ListView适配器
 * 
 * @author lsd
 * 
 */
public class StoreDetailCommentListAdapter extends BaseAdapter {
	List<StoreComment> list;

	public void setData(List<StoreComment> list) {
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
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_store_detail_item, null);
			tag.item_name = (TextView) convertView.findViewById(R.id.item_name);
			tag.item_time = (TextView) convertView.findViewById(R.id.item_time);
			tag.item_comment = (TextView) convertView.findViewById(R.id.item_comment);
			tag.item_ratingBar = (RatingBar) convertView.findViewById(R.id.item_ratingBar);
			convertView.setTag(tag);
		} else {
			tag = (Tag) convertView.getTag();
		}

		StoreComment enty = (StoreComment) getItem(position);
		if (enty != null) {
			tag.item_name.setText(enty.getUsername());
			tag.item_time.setText(enty.getSenddate());
			tag.item_comment.setText(enty.getContent());
			tag.item_ratingBar.setRating(enty.getStar());
		}
		return convertView;
	}

	class Tag {
		TextView item_name;
		TextView item_time;
		TextView item_comment;
		RatingBar item_ratingBar;
	}
}
