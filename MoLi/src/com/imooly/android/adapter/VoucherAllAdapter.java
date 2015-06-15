package com.imooly.android.adapter;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.entity.RspNewmsg.Msg;
import com.imooly.android.entity.RspVoucherFlowList.Flow;

/***
 * 水平ListView适配器
 * 
 * @author
 * 
 */
public class VoucherAllAdapter extends BaseAdapter {
	List<Flow> list;

	public VoucherAllAdapter() {
		// TODO Auto-generated constructor stub
	}

	public void setData(List<Flow> list) {
		this.list = list;
		this.notifyDataSetChanged();
	}

	public void addData(List<Flow> list) {
		
		if (this.list == null) {
			this.list = new ArrayList<Flow>();
		}
		
		if (list == null || list.size() == 0) {
			return;
		}

		this.list.addAll(list);
		this.notifyDataSetChanged();
	}

	public void cleanData() {
		if (list != null) {
			list.clear();
		}
		this.notifyDataSetChanged();
	}

	public List<Flow> getMsgList() {
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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Tag tag;
		if (convertView == null) {
			tag = new Tag();
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_voucher_all, null);
			tag.text_info = (TextView) convertView.findViewById(R.id.text_info);
			tag.text_amount = (TextView) convertView.findViewById(R.id.text_amount);
			
			convertView.setTag(tag);
		} else {
			tag = (Tag) convertView.getTag();
		}

		// 必须放在最外面，防止错位
		tag.text_info.setText(list.get(position).getAction());
		tag.text_amount.setText(list.get(position).getAmount());


		return convertView;
	}

	class Tag {
		TextView text_info;
		TextView text_amount;
	}
}
