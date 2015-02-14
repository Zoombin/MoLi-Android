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
 * 水平ListView适配器
 * 
 * @author lsd
 * 
 */
public class OrderCommHListAdapter extends BaseAdapter {
	OnClickListener listener;
	List<RspUploadimg.Data> list;
	int viewLine = -1;

	public OrderCommHListAdapter() {
		// TODO Auto-generated constructor stub
		list = new ArrayList<RspUploadimg.Data>();
		list.add(null);
	}

	public List<RspUploadimg.Data> getimgs() {
		return list;
	}

	public void setViewLine(int viewLine) {
		this.viewLine = viewLine;
	}

	public void addData(RspUploadimg.Data data) {
		list.add(0, data);
		this.notifyDataSetChanged();
	}

	public void deleteData(int position) {
		if (list != null) {
			list.remove(position);
		}
		this.notifyDataSetChanged();
	}

	public void setOnClickListener(OnClickListener listener) {
		this.listener = listener;
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

		if (position == getCount() - 1) {
			// 最后一个
			tag.order_comment_delete.setVisibility(View.GONE);
			tag.order_comment_delete.setOnClickListener(null);

			tag.order_comment_pic.setImageResource(R.drawable.order_comm_add_pic_selector);
			tag.order_comment_pic.setOnClickListener(listener);
			String itemTag = "";
			if (viewLine != -1) {
				itemTag = viewLine + "#" + "-1";
			} else {
				itemTag = -1 + "";
			}
			tag.order_comment_pic.setTag(itemTag);
		} else {
			tag.order_comment_delete.setVisibility(View.VISIBLE);
			tag.order_comment_delete.setOnClickListener(listener);
			String itemTag = "";
			if (viewLine != -1) {
				itemTag = viewLine + "#" + position;
			} else {
				itemTag = position + "";
			}
			tag.order_comment_delete.setTag(itemTag);

			RspUploadimg.Data data = (RspUploadimg.Data) getItem(position);
			ImageLoader.getInstance().displayImage(data.getImgurl(), tag.order_comment_pic);
			tag.order_comment_pic.setOnClickListener(null);
		}
		return convertView;
	}

	class Tag {
		ImageView order_comment_pic;
		ImageView order_comment_delete;
	}
}
