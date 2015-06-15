package com.imooly.android.adapter;

import java.text.DecimalFormat;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.entity.RspOrderMake;
import com.nostra13.universalimageloader.core.ImageLoader;

/***
 * 填写订单子列表 - ListView适配器
 * 
 * @author lsd
 * 
 */
public class StoreFillorderSubListAdapter extends BaseAdapter {
	List<RspOrderMake.OrderGoodsEty> list;

	public void setData(List<RspOrderMake.OrderGoodsEty> list) {
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
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_store_fillorder_subitem, null);
			tag.fillorder_subitem_pic = (ImageView) convertView.findViewById(R.id.fillorder_subitem_pic);
			tag.fillorder_subitem_conent = (TextView) convertView.findViewById(R.id.fillorder_subitem_conent);
			tag.fillorder_subitem_spec = (TextView) convertView.findViewById(R.id.fillorder_subitem_spec);
			tag.fillorder_subitem_price = (TextView) convertView.findViewById(R.id.fillorder_subitem_price);
			tag.fillorder_subitem_num = (TextView) convertView.findViewById(R.id.fillorder_subitem_num);
			convertView.setTag(tag);
		} else {
			tag = (Tag) convertView.getTag();
		}

		RspOrderMake.OrderGoodsEty subEty = (RspOrderMake.OrderGoodsEty) getItem(position);
		DecimalFormat fnum = new DecimalFormat("##0.00");
		if (subEty != null) {
			ImageLoader.getInstance().displayImage(subEty.goodsimage, tag.fillorder_subitem_pic);
			tag.fillorder_subitem_conent.setText(subEty.getGoodsname());
			tag.fillorder_subitem_spec.setText(subEty.getSpecshow());
			tag.fillorder_subitem_price.setText("￥" + fnum.format(subEty.getPrice()));
			tag.fillorder_subitem_num.setText("数量：" + subEty.getNum());
		}

		return convertView;
	}

	class Tag {
		ImageView fillorder_subitem_pic;
		TextView fillorder_subitem_conent;
		TextView fillorder_subitem_spec;
		TextView fillorder_subitem_price;
		TextView fillorder_subitem_num;
	}
}
