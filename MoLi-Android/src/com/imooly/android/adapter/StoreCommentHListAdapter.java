package com.imooly.android.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.imooly.android.R;
import com.nostra13.universalimageloader.core.ImageLoader;

/***
 * 商城 - 评论图片，水平ListView适配器
 * 
 * @author lsd
 * 
 */
public class StoreCommentHListAdapter extends BaseAdapter {
	List<String> list;
	public StoreCommentHListAdapter() {
		// TODO Auto-generated constructor stub
	}

	public void setData(List<String> list) {
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
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_store_comment_hlist_item, null);
			tag.store_comment_pic = (ImageView) convertView.findViewById(R.id.store_comment_pic);
			convertView.setTag(tag);
		} else {
			tag = (Tag) convertView.getTag();
		}
		
		String imgurl = (String) getItem(position);
		ImageLoader.getInstance().displayImage(imgurl, tag.store_comment_pic);
		
		return convertView;
	}

	class Tag {
		ImageView store_comment_pic;
	}
}
