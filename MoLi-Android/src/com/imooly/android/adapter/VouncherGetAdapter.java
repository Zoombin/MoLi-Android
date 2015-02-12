package com.imooly.android.adapter;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.entity.RspVoucherList.Voucher;

/***
 * 水平ListView适配器
 * 
 * @author
 * 
 */
public class VouncherGetAdapter extends BaseAdapter {
	List<Voucher> list;

	public VouncherGetAdapter() {
		list = new ArrayList<Voucher>();
	}
	
	public void addData(List<Voucher> list) {
		if (list !=null && list.size() !=0) {
			this.list.addAll(list);
		}
		this.notifyDataSetChanged();
	}
	
	public Voucher getDataAtPosition(int postion) {
		return list.get(postion);
	}

	public void cleanData() {
		if (list != null) {
			list.clear();
		}
		this.notifyDataSetChanged();
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
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_vouncher_get, null);
			tag.text_store_name = (TextView) convertView.findViewById(R.id.text_store_name);
			tag.text_voucher_account = (TextView) convertView.findViewById(R.id.text_voucher_account);
			tag.text_product_info = (TextView) convertView.findViewById(R.id.text_product_info);
			convertView.setTag(tag);
		} else {
			tag = (Tag) convertView.getTag();
		}        

		tag.text_store_name.setText(list.get(position).getStorename());
		tag.text_voucher_account.setText(String.valueOf(list.get(position).getVoucher()));
		tag.text_product_info.setText(list.get(position).getGoodsname());
		
		return convertView;
	}

	class Tag {
		TextView text_store_name;
		TextView text_voucher_account;
		TextView text_product_info;
	}
}
