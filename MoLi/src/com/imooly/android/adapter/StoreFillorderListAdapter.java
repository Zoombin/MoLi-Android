package com.imooly.android.adapter;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.entity.RspOrderMake;
import com.imooly.android.widget.CannotRollListView;

/***
 * 商城评价 - ListView适配器
 * 
 * @author lsd
 * 
 */
public class StoreFillorderListAdapter extends BaseAdapter {
	List<RspOrderMake.OrderStoreProEty> list;
	Map<String, String> markMap;
	
	public StoreFillorderListAdapter() {
		// TODO Auto-generated method stub
		markMap = new HashMap<String, String>();
	}

	public void setData(List<RspOrderMake.OrderStoreProEty> list) {
		this.list = list;
		this.notifyDataSetChanged();
	}
	
	public Map<String, String> getMarkMap(){
		return markMap;
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
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_store_fillorder_item, null);
			tag.fillorder_item_sub_layout = (LinearLayout) convertView.findViewById(R.id.fillorder_item_sub_layout);
			tag.fillorder_item_sublist = (CannotRollListView) convertView.findViewById(R.id.fillorder_item_sublist);
			tag.fillorder_item_express = (TextView) convertView.findViewById(R.id.fillorder_item_express);
			tag.fillorder_item_store_name = (TextView) convertView.findViewById(R.id.fillorder_item_store_name);
			tag.fillorder_item_message = (EditText) convertView.findViewById(R.id.fillorder_item_message);
			tag.fillorder_item_num = (TextView) convertView.findViewById(R.id.fillorder_item_num);
			tag.fillorder_item_price = (TextView) convertView.findViewById(R.id.fillorder_item_price);
			convertView.setTag(tag);
		} else {
			tag = (Tag) convertView.getTag();
		}
		
		final RspOrderMake.OrderStoreProEty orderEty = (RspOrderMake.OrderStoreProEty) getItem(position);
		
		tag.fillorder_item_message.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
			}
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				String mark = s.toString();
				String storeId = orderEty.getStoreid();
				
				markMap.put(storeId, mark);
			}
		});
		
		if (orderEty != null) {
			DecimalFormat fnum = new DecimalFormat("##0.00");
			String postway = orderEty.getPostageway();
			String numString = fnum.format(orderEty.getPostage());
			if(!TextUtils.isEmpty(postway) && !"null".equals(postway)){
				tag.fillorder_item_express.setText(postway + "  " + numString);
			}
			tag.fillorder_item_store_name.setText(orderEty.getStorename());
			tag.fillorder_item_num.setText("数量：" + orderEty.getNum() + "");
			tag.fillorder_item_price.setText("￥" + fnum.format(orderEty.getTotalprice()));
			
			String storeId = orderEty.getStoreid();
			if(markMap.containsKey(storeId)){
				tag.fillorder_item_message.setText(markMap.get(storeId));
			}

			StoreFillorderSubListAdapter adapter = new StoreFillorderSubListAdapter();
			tag.fillorder_item_sublist.setAdapter(adapter);
			adapter.setData(orderEty.getGoods());
		}
		return convertView;
	}

	class Tag {
		LinearLayout fillorder_item_sub_layout;
		CannotRollListView fillorder_item_sublist;
		TextView fillorder_item_express;
		EditText fillorder_item_message;
		TextView fillorder_item_store_name;
		TextView fillorder_item_num;
		TextView fillorder_item_price;
	}
}
