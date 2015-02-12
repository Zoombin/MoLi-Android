package com.imooly.android.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.imooly.android.R;
import com.imooly.android.entity.RspAdvertiseIndexads.Info;
import com.imooly.android.tool.Config;
import com.imooly.android.ui.ProductDetailActivity;
import com.imooly.android.ui.StoreDetailActivity;
import com.imooly.android.ui.StoreProActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * 首页广告适配器
 * 
 * @author daiye
 * 
 */
public class HomeADAdapter extends BaseAdapter {

	private Context context;
	private List<Info> infos;
	private boolean oneline;
	
	// 商品详情
	String BN01 = "BN01";
	// 店铺
	String SH01 = "SH01";
	// 实体店详情
	String PH01 = "PH01";
	private DisplayImageOptions defaultOptions;
	
	public HomeADAdapter(Context context, List<Info> infos, boolean oneline) {
		this.context = context;
		this.oneline = oneline;
		this.infos = infos;
		
		if (oneline) {
			defaultOptions = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.ic_loading_580_276)
		    .showImageForEmptyUri(R.drawable.ic_error_580_276)  // empty URI时显示的图片  
		    .showImageOnFail(R.drawable.ic_error_580_276)      // 不是图片文件 显示图片  
			.cacheInMemory(true)
			.bitmapConfig(Bitmap.Config.RGB_565)  // 图片压缩质量
			.cacheOnDisc(true)			
			.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
			.build();
		} else {
			defaultOptions = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.ic_loading_280_156)
		    .showImageForEmptyUri(R.drawable.ic_error_280_156)  // empty URI时显示的图片  
		    .showImageOnFail(R.drawable.ic_error_280_156)      // 不是图片文件 显示图片  
			.cacheInMemory(true)
			.bitmapConfig(Bitmap.Config.RGB_565)  // 图片压缩质量
			.cacheOnDisc(true)			
			.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
			.build();
		}
	}

	@Override
	public int getCount() {
		return infos.size();
	}

	@Override
	public Object getItem(int position) {
		return infos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public void clearData() {
		infos.clear();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Info info = infos.get(position);
		ViewHolder holder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.home_ad_item, parent, false);

			holder = new ViewHolder();
			
			holder.iv_ad = (ImageView) convertView
					.findViewById(R.id.iv_ad);
//			if (oneline) {
//				int width = (int)(Config.width - (10*Config.density));
//				int height = width / 690 * 328;
//				LayoutParams params = new LayoutParams(width, height);
//				holder.iv_ad.setLayoutParams(params);
//			} else {
//				int width = (int)(Config.width / 2 - (10*Config.density));
//				int height = width / 420 * 234;
//				LayoutParams params = new LayoutParams(width, height);
//				holder.iv_ad.setLayoutParams(params);
//			}

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final String apppagecode = info.getApppagecode();
		ImageLoader.getInstance().displayImage(info.getImagepath(), holder.iv_ad);
		holder.iv_ad.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				if (apppagecode.equals(BN01)) {
					// 商品详情
					intent.putExtra(ProductDetailActivity.EXTRA_GOODSID, info.getParamid());
					intent.setClass(context, ProductDetailActivity.class);
				} else if (apppagecode.equals(SH01)) {
					// 店铺
					intent.putExtra(StoreProActivity.EXTRA_BUSINESSID, info.getParamid());
					intent.setClass(context, StoreProActivity.class);
				} else if (apppagecode.equals(PH01)) {
					// 实体店详情
					intent.putExtra(StoreDetailActivity.EXTRA_BUSNESSID, info.getParamid());
					intent.setClass(context, StoreDetailActivity.class);
				}
				context.startActivity(intent);
			}
		});
		return convertView;
	}

	public class ViewHolder {
		ImageView iv_ad;
	}
}
