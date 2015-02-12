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
import com.imooly.android.entity.RspProfile.GoodsrandEty;
import com.nostra13.universalimageloader.core.ImageLoader;

/***
 * 订单详情 猜你喜欢 GridView适配器
 * 
 * @author lsd
 * 
 */
public class GoodsRandgridAdapter extends BaseAdapter {
	List<GoodsrandEty> list;
	DecimalFormat fnum = new DecimalFormat("##0.00");

	public void setData(List<GoodsrandEty> list) {
		this.list = list;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list == null ? 0 :list.size();
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
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_goodsrand_grid_item, null);
			tag.rand_img = (ImageView) convertView.findViewById(R.id.rand_img);
			tag.rand_flag = (ImageView) convertView.findViewById(R.id.rand_flag);
			tag.rand_name = (TextView) convertView.findViewById(R.id.rand_name);
			tag.rand_price = (TextView) convertView.findViewById(R.id.rand_price);
			convertView.setTag(tag);
		} else {
			tag = (Tag) convertView.getTag();
		}

		GoodsrandEty ety = (GoodsrandEty) getItem(position);
		if (ety != null) {
			tag.rand_name.setText(ety.getGoodsname());
			tag.rand_price.setText("价格：￥" + fnum.format(ety.getPrice()));
			ImageLoader.getInstance().displayImage(ety.getGoodsimage(), tag.rand_img);

			if (1 == ety.isvoucher) {
				tag.rand_flag.setVisibility(View.VISIBLE);
			}else{
				tag.rand_flag.setVisibility(View.INVISIBLE);
			}
		}
		return convertView;
	}

	class Tag {
		ImageView rand_img;
		ImageView rand_flag;
		TextView rand_name;
		TextView rand_price;
	}
}
