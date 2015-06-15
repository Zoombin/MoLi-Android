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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.entity.RspGoodsSearch.GoodsEty;
import com.imooly.android.ui.ProductDetailActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 商品列表适配器
 * @author daiye
 *
 */
public class ProductListViewAdapter extends BaseAdapter {
	
	private Context context;
	private List<GoodsEty> goods;

	public ProductListViewAdapter(Context context, List<GoodsEty> goods) {
		this.context = context;
		this.goods = goods;
	}
	
	
	public void setData(List<GoodsEty> goods){
		this.goods = goods;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return goods.size();
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
		final GoodsEty good = goods.get(position);
		ViewHolder holder;
		
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.category_productlist_listview_item, parent, false);
			
			holder = new ViewHolder();
			
			holder.category_productlist_list_item_rl = (RelativeLayout) convertView.findViewById(R.id.category_productlist_list_item_rl);
			holder.category_productlist_list_item_icon = (ImageView) convertView.findViewById(R.id.category_productlist_list_item_icon);
			holder.category_productlist_list_item_title = (TextView) convertView.findViewById(R.id.category_productlist_list_item_title);
			holder.category_productlist_list_item_ad = (TextView) convertView.findViewById(R.id.category_productlist_list_item_ad);
			holder.category_productlist_list_item_price = (TextView) convertView.findViewById(R.id.category_productlist_list_item_price);
			holder.category_productlist_list_item_salesvolume = (TextView) convertView.findViewById(R.id.category_productlist_list_item_salesvolume);
			holder.category_productlist_list_item_highopinion = (TextView) convertView.findViewById(R.id.category_productlist_list_item_highopinion);
			holder.category_productlist_list_item_djq = (ImageView) convertView.findViewById(R.id.category_productlist_list_item_djq);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.category_productlist_list_item_rl.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String goodsid = good.getGoodsid();
				Intent intent = new Intent(context, ProductDetailActivity.class);
				intent.putExtra(ProductDetailActivity.EXTRA_GOODSID, goodsid);
				context.startActivity(intent);
			}
		});
		ImageLoader.getInstance().displayImage(good.getGoodsimage(), holder.category_productlist_list_item_icon);
		holder.category_productlist_list_item_title.setText(good.getGoodsname());
		DecimalFormat fnum = new DecimalFormat("##0.00");
		holder.category_productlist_list_item_price.setText("￥" + fnum.format(good.getPrice()));
		holder.category_productlist_list_item_salesvolume.setText("销量:" + Integer.toString(good.getSalesvolume()));
		holder.category_productlist_list_item_highopinion.setText("好评率:" + Integer.toString(good.getHighopinion()) + "%");
		if (good.getIsvoucher() == 0) {
			holder.category_productlist_list_item_djq.setVisibility(View.GONE);
		} else {
			holder.category_productlist_list_item_djq.setVisibility(View.VISIBLE);
		}
		
		return convertView;
	}
	
	public class ViewHolder {
		RelativeLayout category_productlist_list_item_rl;
		ImageView category_productlist_list_item_icon;
		TextView category_productlist_list_item_title;
		TextView category_productlist_list_item_ad;
		TextView category_productlist_list_item_price;
		TextView category_productlist_list_item_salesvolume;
		TextView category_productlist_list_item_highopinion;
		ImageView category_productlist_list_item_djq;
	}
}
