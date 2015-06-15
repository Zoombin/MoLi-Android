package com.imooly.android.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.entity.RspCellectionStoreList.Business;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/***
 * 实体店收藏 水平ListView适配器
 * 
 * @author
 * 
 */
public class CollectionStoreFlagshipAdapter extends BaseAdapter {

	List<Business> list;
	List<Boolean> selects;
	private Context mContext;
	boolean edit;
	private DisplayImageOptions defaultOptions;
	
	public CollectionStoreFlagshipAdapter(Context context) {
		this.mContext = context;
		
		defaultOptions = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.ic_loading_158_86)
		    .showImageForEmptyUri(R.drawable.ic_error_158_86)  // empty URI时显示的图片  
		    .showImageOnFail(R.drawable.ic_error_158_86)      // 不是图片文件 显示图片  
			.cacheInMemory(true)
			.bitmapConfig(Bitmap.Config.RGB_565)  // 图片压缩质量
			.cacheOnDisc(true)			
			.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
			.build();
	}

	public void editMode(boolean edit) {
		this.edit = edit;
		this.notifyDataSetChanged();
	}

	public void setData(List<Business> list) {
		if (list == null || list.size() == 0) {
			return;
		}
		this.list = list;

		selects = new ArrayList<Boolean>();
		for (int i = 0; i < list.size(); i++) {
			selects.add(false);
		}
		this.notifyDataSetChanged();
	}

	public void addData(List<Business> list) {
		if (this.list == null) {
			this.list = new ArrayList<Business>();
		}
		if (list == null || list.size() == 0) {
			return;
		}
		this.list.addAll(list);
		selects = new ArrayList<Boolean>();
		for (int i = 0; i < list.size(); i++) {
			selects.add(false);
		}
		this.notifyDataSetChanged();
	}

	public void cleanSelect() {
		if (list == null || list.size() == 0) {
			return;
		}
		selects = new ArrayList<Boolean>();
		for (int i = 0; i < list.size(); i++) {
			selects.add(false);
		}
		this.notifyDataSetChanged();
	}

	public void selectAll() {
		if (list == null || list.size() == 0) {
			return;
		}
		selects = new ArrayList<Boolean>();
		for (int i = 0; i < list.size(); i++) {
			selects.add(true);
		}
		this.notifyDataSetChanged();
	}

	public void changeSelect(int position, boolean select) {
		if (list == null || list.size() == 0) {
			return;
		}
		selects.set(position, select);
		this.notifyDataSetChanged();
	}

	public List<Boolean> getSelect() {
		return selects;
	}

	public void deleteItemes() {
		int count = 0;
		int len = list.size();
		for (int i = 0; i < len; i++) {
			boolean b = selects.get(i);
			if (b) {

				list.remove(i);
				selects.remove(i);

				count++;
				i--;
				len--;

			}
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
	public View getView(int position, View convertView, ViewGroup parent) {

		Tag tag;
		final int pos = position;

		if (convertView == null) {

			tag = new Tag();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_collection_store_flagship, null);
			tag.image_left = (ImageView) convertView.findViewById(R.id.image_left);
			tag.checkBox = (CheckBox) convertView.findViewById(R.id.check);
			tag.textTitle = (TextView) convertView.findViewById(R.id.text_title);

			convertView.setTag(tag);

		} else {
			tag = (Tag) convertView.getTag();
		}

		Business ety = (Business) getItem(position);
		if (ety != null) {
			ImageLoader.getInstance().displayImage(ety.getBusinessimage(), tag.image_left, defaultOptions);
			tag.textTitle.setText(ety.getBusinessname());
		}
		if (edit) {
			tag.checkBox.setVisibility(View.VISIBLE);
		} else {
			tag.checkBox.setVisibility(View.GONE);
		}
		if (selects.get(position)) {
			tag.checkBox.setChecked(true);
		} else {
			tag.checkBox.setChecked(false);
		}

		return convertView;
	}

	class Tag {
		ImageView image_left;
		CheckBox checkBox;
		TextView textTitle;
	}

}
