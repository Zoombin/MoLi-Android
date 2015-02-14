package com.imooly.android.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.entity.RspNewmsg.Msg;
import com.imooly.android.utils.Utils;

/***
 * 水平ListView适配器
 * 
 * @author
 * 
 */
public class MyMessageAdapter extends BaseAdapter {
	private List<Msg> list;
	private Context mContext;

	public MyMessageAdapter(Context context) {
		mContext = context;
	}

	public void setData(List<Msg> list) {
		this.list = list;
		this.notifyDataSetChanged();
	}

	public void addData(List<Msg> list) {
		if (list == null || list.size() == 0) {
			return;
		}
		if (this.list == null) {
			this.list = new ArrayList<Msg>();
		}
		this.list.addAll(0,list);
		
		this.notifyDataSetChanged();
	}

	public void cleanData() {
		if (list != null) {
			list.clear();
		}
		this.notifyDataSetChanged();
	}

	public List<Msg> getMsgList() {
		return list;
	}

	public void deleteData(int position) {
		if (list != null) {
			list.remove(position);
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
	
	// 设置已读
	public void setReaded(int postion) {
		
		list.get(postion).setIsread(1);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Tag tag;
		if (convertView == null) {
			tag = new Tag();
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_my_message, null);
			tag.mymessage_item_status = (ImageView) convertView.findViewById(R.id.mymessage_item_status);
			tag.mymessage_item_info = (TextView) convertView.findViewById(R.id.mymessage_item_info);
			tag.mymessage_item_time = (TextView) convertView.findViewById(R.id.mymessage_item_time);
			convertView.setTag(tag);
		} else {
			tag = (Tag) convertView.getTag();
		}

		Msg msg = (Msg) getItem(position);
		if (msg != null) {
			tag.mymessage_item_info.setText(msg.getTitle());

			if (1 == msg.getIsread()) { // 已读
				tag.mymessage_item_info.setTextColor(mContext.getResources().getColor(R.color.app_text_gray));
			} else {
				tag.mymessage_item_info.setTextColor(mContext.getResources().getColor(R.color.title));
			}

			tag.mymessage_item_time.setText(Utils.getDatebyTimestamp(msg.getSenddate()));
		}

		return convertView;
	}

	class Tag {
		TextView mymessage_item_info;
		TextView mymessage_item_time;
		ImageView mymessage_item_status;
	}
}
