package com.imooly.android.adapter;

import java.text.DecimalFormat;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.entity.RspOrderList.OrderGdsEty;
import com.nostra13.universalimageloader.core.ImageLoader;

/***
 * 所有订单 z子适配器
 * 
 * @author lsd
 * 
 */
public class OrderSubAdapter extends BaseAdapter {
	Context context;
	List<OrderGdsEty> list;

	public OrderSubAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public void setData(List<OrderGdsEty> list) {
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
		Tag tag = null;
		if (convertView == null) {
			tag = new Tag();
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_sub_item, null);
			tag.order_item_pic = (ImageView) convertView.findViewById(R.id.order_item_pic);
			tag.order_item_detail = (TextView) convertView.findViewById(R.id.order_item_detail);
			tag.order_item_price = (TextView) convertView.findViewById(R.id.order_item_price);
			tag.order_item_num = (TextView) convertView.findViewById(R.id.order_item_num);
			tag.order_item_spec = (TextView) convertView.findViewById(R.id.order_item_spec);
			convertView.setTag(tag);
		} else {
			tag = (Tag) convertView.getTag();
		}

		OrderGdsEty ety = (OrderGdsEty) getItem(position);
		if (ety != null) {
			ImageLoader.getInstance().displayImage(ety.getImage(), tag.order_item_pic);
			tag.order_item_detail.setText(ety.getName());
			DecimalFormat fnum = new DecimalFormat("##0.00");
			tag.order_item_price.setText("价格：￥"+fnum.format(ety.getPrice()));
			tag.order_item_num.setText("数量："+ety.getNum() + "");
			tag.order_item_spec.setText(ety.getSpec());
		}
		return convertView;
	}

	class Tag {
		ImageView order_item_pic;
		TextView order_item_detail;
		TextView order_item_price;
		TextView order_item_num;
		TextView order_item_spec;
	}
}
