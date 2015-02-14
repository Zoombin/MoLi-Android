package com.imooly.android.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.entity.RspGoodsSearch.GoodsEty;
import com.imooly.android.ui.ProductDetailActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 商品gridview适配器
 * 
 * @author daiye
 * 
 */
public class StoreProGridViewAdapter extends BaseAdapter {

	private Context context;
	private List<GoodsEty> goods;

	public StoreProGridViewAdapter(Context context, List<GoodsEty> goods) {
		this.context = context;
		this.goods = goods;
	}

	@Override
	public int getCount() {
		if (goods == null) {
			return 0;
		} else {
			return goods.size() / 2 + goods.size() % 2;
		}
	}

	@Override
	public Object getItem(int position) {
		return goods.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void clearData() {
		goods.clear();
	}

	public void addGoodsList(List<GoodsEty> goods) {
		this.goods.addAll(goods);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final GoodsEty good1 = goods.get(position * 2);
		GoodsEty goodtemp = null;
		if (position * 2 + 1 < goods.size()) {
			goodtemp = goods.get(position * 2 + 1);
		}
		final GoodsEty good2 = goodtemp;
		ViewHolder holder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.storepro_productlist_gridview_item, parent, false);

			holder = new ViewHolder();

			holder.category_productlist_grid_item_frame = (RelativeLayout) convertView
					.findViewById(R.id.category_productlist_grid_item_frame);
			holder.category_productlist_grid_item_icon = (ImageView) convertView
					.findViewById(R.id.category_productlist_grid_item_icon);
			holder.category_productlist_grid_item_title = (TextView) convertView
					.findViewById(R.id.category_productlist_grid_item_title);
			holder.category_productlist_grid_item_ad = (TextView) convertView
					.findViewById(R.id.category_productlist_grid_item_ad);
			holder.category_productlist_grid_item_price = (TextView) convertView
					.findViewById(R.id.category_productlist_grid_item_price);
			holder.category_productlist_gird_item_djq = (ImageView) convertView
					.findViewById(R.id.category_productlist_gird_item_djq);

			holder.category_productlist_grid_item_frame2 = (RelativeLayout) convertView
					.findViewById(R.id.category_productlist_grid_item_frame2);
			holder.category_productlist_grid_item_icon2 = (ImageView) convertView
					.findViewById(R.id.category_productlist_grid_item_icon2);
			holder.category_productlist_grid_item_title2 = (TextView) convertView
					.findViewById(R.id.category_productlist_grid_item_title2);
			holder.category_productlist_grid_item_ad2 = (TextView) convertView
					.findViewById(R.id.category_productlist_grid_item_ad2);
			holder.category_productlist_grid_item_price2 = (TextView) convertView
					.findViewById(R.id.category_productlist_grid_item_price2);
			holder.category_productlist_gird_item_djq2 = (ImageView) convertView
					.findViewById(R.id.category_productlist_gird_item_djq2);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.category_productlist_grid_item_frame
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						String goodsid = good1.getGoodsid();
						Intent intent = new Intent(context,
								ProductDetailActivity.class);
						intent.putExtra(ProductDetailActivity.EXTRA_GOODSID,
								goodsid);
						context.startActivity(intent);
					}
				});
		ImageLoader.getInstance().displayImage(good1.getGoodsimage(),
				holder.category_productlist_grid_item_icon);
		holder.category_productlist_grid_item_title.setText(good1
				.getGoodsname());
		holder.category_productlist_grid_item_price.setText("￥"
				+ Float.toString(good1.getPrice()));
		if (good1.getIsvoucher() == 0) {
			holder.category_productlist_gird_item_djq.setVisibility(View.GONE);
		} else {
			holder.category_productlist_gird_item_djq
					.setVisibility(View.VISIBLE);
		}

		if (good2 == null) {
			holder.category_productlist_grid_item_frame2
					.setVisibility(View.INVISIBLE);
		} else {
			holder.category_productlist_grid_item_frame2
					.setVisibility(View.VISIBLE);
			holder.category_productlist_grid_item_frame2
					.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							String goodsid = good2.getGoodsid();
							Intent intent = new Intent(context,
									ProductDetailActivity.class);
							intent.putExtra(
									ProductDetailActivity.EXTRA_GOODSID,
									goodsid);
							context.startActivity(intent);
						}
					});
			ImageLoader.getInstance().displayImage(good2.getGoodsimage(),
					holder.category_productlist_grid_item_icon2);
			holder.category_productlist_grid_item_title2.setText(good2
					.getGoodsname());
			holder.category_productlist_grid_item_price2.setText("￥"
					+ Float.toString(good2.getPrice()));
			if (good2.getIsvoucher() == 0) {
				holder.category_productlist_gird_item_djq2
						.setVisibility(View.GONE);
			} else {
				holder.category_productlist_gird_item_djq2
						.setVisibility(View.VISIBLE);
			}
		}

		return convertView;
	}

	public class ViewHolder {
		RelativeLayout category_productlist_grid_item_frame;
		ImageView category_productlist_grid_item_icon;
		TextView category_productlist_grid_item_title;
		TextView category_productlist_grid_item_ad;
		TextView category_productlist_grid_item_price;
		ImageView category_productlist_gird_item_djq;

		RelativeLayout category_productlist_grid_item_frame2;
		ImageView category_productlist_grid_item_icon2;
		TextView category_productlist_grid_item_title2;
		TextView category_productlist_grid_item_ad2;
		TextView category_productlist_grid_item_price2;
		ImageView category_productlist_gird_item_djq2;
	}
}
