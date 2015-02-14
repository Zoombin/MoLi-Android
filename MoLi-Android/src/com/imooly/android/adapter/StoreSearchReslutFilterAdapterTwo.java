package com.imooly.android.adapter;

import java.util.ArrayList;
import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.entity.RspBusinessCirclelist.Circle;
import com.imooly.android.entity.RspBusinessClassifyList.SubClassifyEntity;
import com.imooly.android.ui.StoreSearchResultActivity.FilterType;

/***
 * 实体店 搜索结果 筛选- ListView适配器
 * 
 * @author lsd
 * 
 */
public class StoreSearchReslutFilterAdapterTwo extends BaseAdapter {
	int PSelect = 0;
	FilterType type;
	List<?> list;
	List<Boolean> select;

	public void setData(FilterType type, List<?> list,int PSelect) {
		this.PSelect = PSelect;
		this.type = type;
		this.list = list;
		if (list != null && list.size() > 0) {
			select = new ArrayList<Boolean>();
			for (int i = 0; i < list.size(); i++) {
				select.add(false);
			}
		}
		this.notifyDataSetChanged();
	}
	
	public int getPSelect(){
		return PSelect;
	}

	public void changeSelect(int position) {
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				select.set(i, false);
			}
			select.set(position, true);
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
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_store_search_filter_item2, null);
			tag.iv_logo = (ImageView) convertView.findViewById(R.id.iv_logo);
			tag.tv_txt = (TextView) convertView.findViewById(R.id.tv_txt);
			tag.iv_right = (ImageView) convertView.findViewById(R.id.iv_right);
			tag.dir = convertView.findViewById(R.id.dir);
			convertView.setTag(tag);
		} else {
			tag = (Tag) convertView.getTag();
		}

		tag.iv_logo.setVisibility(View.GONE);
		tag.iv_right.setVisibility(View.GONE);

		String txtStr = "";
		if (type == FilterType.CATEGRORY) {
			SubClassifyEntity fyEntity = (SubClassifyEntity) getItem(position);
			txtStr = fyEntity.getClassifyname();
		}
		if (type == FilterType.CIRCLE) {
			Circle airEntity = (Circle) getItem(position);
			txtStr = airEntity.getCirclename();
		}
		tag.tv_txt.setText(txtStr);

		if (position == getCount() - 1) {
			tag.dir.setVisibility(View.INVISIBLE);
		} else {
			tag.dir.setVisibility(View.VISIBLE);
		}

		return convertView;
	}

	class Tag {
		ImageView iv_logo;
		TextView tv_txt;
		ImageView iv_right;
		View dir;
	}
}
