package com.imooly.android.adapter;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.imooly.android.R;
import com.imooly.android.entity.RspUploadimg;
import com.nostra13.universalimageloader.core.ImageLoader;

/***
 * 售后详情 图片水平ListView适配器
 * 
 * @author lsd
 * 
 */
public class OrderServiceDetailHListAdapter extends BaseAdapter {
	List<String> list;

	public OrderServiceDetailHListAdapter() {
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
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_comment_hlist_item, null);
			tag.order_comment_pic = (ImageView) convertView.findViewById(R.id.order_comment_pic);
			tag.order_comment_delete = (ImageView) convertView.findViewById(R.id.order_comment_delete);
			convertView.setTag(tag);
		} else {
			tag = (Tag) convertView.getTag();
		}
		
		String img = (String) getItem(position);
		ImageLoader.getInstance().displayImage(img, tag.order_comment_pic);
		tag.order_comment_delete.setVisibility(View.INVISIBLE);

		return convertView;
	}

	class Tag {
		ImageView order_comment_pic;
		ImageView order_comment_delete;
	}
}
