package com.imooly.android.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.imooly.android.R;
import com.imooly.android.entity.RspAdvertiseIndexads.Info;
import com.imooly.android.tool.Config;
import com.imooly.android.ui.StoreDetailActivity;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/***
 * 实体店 热推商家 - ListView适配器
 * 
 * @author lsd
 * 
 */
public class StoreHotListAdapter extends BaseAdapter implements OnClickListener {
	Context context;
	List<Info> list;
	private DisplayImageOptions defaultOptions;
	
	public StoreHotListAdapter(Context context) {
		this.context = context;
		defaultOptions = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.ic_loading_304_132)
		    .showImageForEmptyUri(R.drawable.ic_error_304_132)  // empty URI时显示的图片  
		    .showImageOnFail(R.drawable.ic_error_304_132)      // 不是图片文件 显示图片  
			.cacheInMemory(true)
			.bitmapConfig(Bitmap.Config.RGB_565)  // 图片压缩质量
			.cacheOnDisc(true)			
			.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
			.build();
	}

	public void setData(List<Info> list) {
		this.list = list;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		if (list == null) {
			return 0;
		} else {
			return list.size() / 3;
		}
	}

	@Override
	public Object getItem(int position) {
		return list == null ? null : list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return list == null ? 0 : position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Tag tag;
		if (convertView == null) {
			tag = new Tag();
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_store_hot_item, null);
			
			tag.store_hot_pic_left = (ImageView) convertView.findViewById(R.id.store_hot_pic_left);
			tag.store_hot_pic_top = (ImageView) convertView.findViewById(R.id.store_hot_pic_top);
			tag.store_hot_pic_bottom = (ImageView) convertView.findViewById(R.id.store_hot_pic_bottom);
			convertView.setTag(tag);
		} else {
			tag = (Tag) convertView.getTag();
		}

		int width1 = (int)(Config.width / 2 - 25);
		int height1 = width1 * 278 / 282;
		LayoutParams params1 = new LayoutParams(width1, height1);
		params1.setMargins(0, 0, 20, 0);
		tag.store_hot_pic_left.setLayoutParams(params1);
		
		int width2 = (int)(Config.width / 2 );
		int height2 = width2 * 132 / 306;
		LayoutParams params2 = new LayoutParams(width2, height2);
		tag.store_hot_pic_bottom.setLayoutParams(params2);
		params2.setMargins(0, 0, 0, 20);
		tag.store_hot_pic_top.setLayoutParams(params2);
		

		tag.store_hot_pic_left.setVisibility(View.INVISIBLE);
		tag.store_hot_pic_top.setVisibility(View.INVISIBLE);
		tag.store_hot_pic_bottom.setVisibility(View.INVISIBLE);

		tag.store_hot_pic_left.setOnClickListener(null);
		tag.store_hot_pic_top.setOnClickListener(null);
		tag.store_hot_pic_bottom.setOnClickListener(null);
		if (list.size() > 0) {
			Info etyLeft = null;
			Info etyTop = null;
			Info etyBotton = null;

			etyLeft = list.get((position * 3 + 0));
			etyTop = list.get((position * 3 + 1));
			etyBotton = list.get((position * 3 + 2));

			if (etyLeft != null) {
				tag.store_hot_pic_left.setVisibility(View.VISIBLE);
				tag.store_hot_pic_left.setTag(etyLeft);
				ImageLoader.getInstance().displayImage(etyLeft.getImagepath(), tag.store_hot_pic_left);
				tag.store_hot_pic_left.setOnClickListener(this);
			}
			if (etyTop != null) {
				tag.store_hot_pic_top.setVisibility(View.VISIBLE);
				tag.store_hot_pic_top.setTag(etyTop);
				ImageLoader.getInstance().displayImage(etyTop.getImagepath(), tag.store_hot_pic_top, defaultOptions);
				tag.store_hot_pic_top.setOnClickListener(this);
			}
			if (etyBotton != null) {
				tag.store_hot_pic_bottom.setVisibility(View.VISIBLE);
				tag.store_hot_pic_bottom.setTag(etyBotton);
				ImageLoader.getInstance().displayImage(etyBotton.getImagepath(), tag.store_hot_pic_bottom, defaultOptions);
				tag.store_hot_pic_bottom.setOnClickListener(this);
			}
		}

		return convertView;
	}

	class Tag {
		ImageView store_hot_pic_left;
		ImageView store_hot_pic_top;
		ImageView store_hot_pic_bottom;
	}

	@Override
	public void onClick(View v) {
		Info ety = (Info) v.getTag();
		context.startActivity(new Intent(context, StoreDetailActivity.class).putExtra(StoreDetailActivity.EXTRA_BUSNESSID, ety.getParamid()));
	}
}
