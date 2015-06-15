package com.imooly.android.adapter;

import java.text.DecimalFormat;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.entity.FootStepEntity;
import com.imooly.android.ui.ProductDetailActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

/***
 * 水平ListView适配器
 * 
 * @author lsd
 * 
 */
public class FootStepAdapter extends BaseAdapter {
	List<FootStepEntity> list;

	public FootStepAdapter() {
		// TODO Auto-generated constructor stub
	}

	public void setData(List<FootStepEntity> list) {
		this.list = list;
		this.notifyDataSetChanged();
	}

	public void cleanData() {
		if (list != null) {
			list.clear();
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
	public View getView(int position, View convertView, final ViewGroup parent) {
		// TODO Auto-generated method stub
		Tag tag;
		if (convertView == null) {
			tag = new Tag();
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_footstep, null);
			tag.footstep_item_pic = (ImageView) convertView.findViewById(R.id.footstep_item_pic);
			tag.footstep_item_info = (TextView) convertView.findViewById(R.id.footstep_item_info);
			tag.footstep_item_price = (TextView) convertView.findViewById(R.id.footstep_item_price);
			convertView.setTag(tag);
		} else {
			tag = (Tag) convertView.getTag();
		}

		final FootStepEntity ety = (FootStepEntity) getItem(position);
		if (ety != null) {
			ImageLoader.getInstance().displayImage(ety.getGoods_img(), tag.footstep_item_pic);
			tag.footstep_item_info.setText(ety.getGoods_name());
			DecimalFormat fnum = new DecimalFormat("##0.00");
			float price = 0;
			try {
				price = Float.parseFloat(ety.getGoods_price());
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			tag.footstep_item_price.setText("价格：" + fnum.format(price));

			convertView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					Context context = parent.getContext();
					String goods_id = ety.getGoods_id();
					context.startActivity(new Intent(context, ProductDetailActivity.class).putExtra(ProductDetailActivity.EXTRA_GOODSID,
							goods_id));
				}
			});
		}

		return convertView;
	}

	class Tag {
		ImageView footstep_item_pic;
		TextView footstep_item_info;
		TextView footstep_item_price;
	}
}
