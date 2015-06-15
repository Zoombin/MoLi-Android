package com.imooly.android.adapter;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.Interface.OpHubClickListener;
import com.imooly.android.entity.RspOrderList.OrderOpEty;
import com.imooly.android.widget.RectangleTextView;

/***
 * 所有订单 按钮适配器
 * 
 * @author lsd
 * 
 */
public class OrderOPAdapter extends BaseAdapter {
	Context context;
	List<OrderOpEty> list;
	Object parentEntity;
	OpHubClickListener listener;

	public OrderOPAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public void setOnclickLister(OpHubClickListener listener) {
		this.listener = listener;
	}

	public void setData(Object parentEntity, List<OrderOpEty> list) {
		this.list = list;
		this.parentEntity = parentEntity;
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
		Tag tag = null;
		if (convertView == null) {
			tag = new Tag();
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_order_op_item, null);
			tag.tv = (RectangleTextView) convertView.findViewById(R.id.tv);
			convertView.setTag(tag);
		} else {
			tag = (Tag) convertView.getTag();
		}

		final OrderOpEty ety = (OrderOpEty) getItem(position);
		if (ety != null) {
			tag.tv.setTag(ety);
			tag.tv.setText(ety.getName());
			tag.tv.setTextColor(Color.parseColor("#" + ety.getFontcolor()));
			tag.tv.setBackgroundColor(Color.parseColor("#" + ety.getBgcolor()));
			String borderColor = ety.getBordercolor();
			if(TextUtils.isEmpty(borderColor)){
				borderColor = "949494";
			}
			tag.tv.SetStrokeColor(Color.parseColor("#"+borderColor));
			tag.tv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					if (listener != null) {
						listener.onClick(parentEntity, ety);
					}
				}
			});
		}
		return convertView;
	}

	class Tag {
		RectangleTextView tv;
	}
}
