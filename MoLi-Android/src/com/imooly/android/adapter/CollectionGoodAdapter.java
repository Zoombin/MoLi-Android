package com.imooly.android.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.entity.RspCellectionGoodsList.CollectionGood;
import com.nostra13.universalimageloader.core.ImageLoader;

/***
 * 收藏商品 水平ListView适配器
 * 
 * @author
 * 
 */
public class CollectionGoodAdapter extends BaseAdapter {
	List<CollectionGood> list;
	List<Boolean> selects;
	private Context mContext;
	boolean edit;

	public CollectionGoodAdapter(Context context) {
		this.mContext = context;
		selects = new ArrayList<Boolean>();
	}

	public void editMode(boolean edit) {
		this.edit = edit;
		this.notifyDataSetChanged();
	}

//	public void setData(List<CollectionGood> list) {
//		if (list == null || list.size() == 0) {
//			return;
//		}
//
//		// 初始化list
//		this.list = list;
//
//		selects = new ArrayList<Boolean>();
//		for (int i = 0; i < list.size(); i++) {
//			selects.add(false);
//		}
//
//		this.notifyDataSetChanged();
//	}

	public void addData(List<CollectionGood> list) {
		if (this.list == null) {
			this.list = new ArrayList<CollectionGood>();
		}
		if (list == null || list.size() == 0) {
			return;
		}
		this.list.addAll(list);

		// 选择标记List
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

	public void deleteItems() {

		int count = 0;

		int len = list.size();

		for (int i = 0; i < len; i++) {

			Log.d("xxx", "selects index i=" + i);

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
		// TODO Auto-generated method stub
		Tag tag;
		if (convertView == null) {
			tag = new Tag();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_list_collection_good, null);
			tag.checkBox = (CheckBox) convertView.findViewById(R.id.check);
			tag.image_left = (ImageView) convertView.findViewById(R.id.image_left);
			tag.textTitle = (TextView) convertView.findViewById(R.id.text_title);
			tag.textPrice = (TextView) convertView.findViewById(R.id.text_price);
			convertView.setTag(tag);
		} else {
			tag = (Tag) convertView.getTag();
		}

		CollectionGood ety = (CollectionGood) getItem(position);
		if (ety != null) {
			ImageLoader.getInstance().displayImage(ety.getGoodsimage(), tag.image_left);

			tag.textTitle.setText(ety.getGoodsname());
			tag.textPrice.setText("价格：" + ety.getPrice());
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
		CheckBox checkBox;
		ImageView image_left;
		TextView textTitle;
		TextView textPrice;
	}

}
