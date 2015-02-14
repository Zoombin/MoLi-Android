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
import com.imooly.android.entity.RspBusinessClassifyList.ClassifyEntity;
import com.imooly.android.ui.StoreSearchResultActivity.FilterType;
import com.nostra13.universalimageloader.core.ImageLoader;

/***
 * 实体店 搜索结果 筛选- ListView适配器Two
 * 
 * @author lsd
 * 
 */
public class StoreSearchReslutFilterAdapter extends BaseAdapter {
	FilterType type;
	List<?> list;
	List<Boolean> selects;

	public void setData(FilterType type, int index,List<?> list) {
		this.type = type;
		this.list = list;
		if (list != null && list.size() > 0) {
			selects = new ArrayList<Boolean>();
			for (int i = 0; i < list.size(); i++) {
				selects.add(false);
			}
			selects.set(index, true);
		}
		this.notifyDataSetChanged();
	}

	public void changeSelect(int position) {
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				selects.set(i, false);
			}
			selects.set(position, true);
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
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_store_search_filter_item, null);
			tag.iv_logo = (ImageView) convertView.findViewById(R.id.iv_logo);
			tag.tv_txt = (TextView) convertView.findViewById(R.id.tv_txt);
			tag.iv_right = (ImageView) convertView.findViewById(R.id.iv_right);
			tag.dir = convertView.findViewById(R.id.dir);
			convertView.setTag(tag);
		} else {
			tag = (Tag) convertView.getTag();
		}

		//设置名称
		String txtStr="";
		if(type == FilterType.SORT){
			String s = (String) getItem(position);
			txtStr = s;
		}
		if(type == FilterType.CATEGRORY){
			ClassifyEntity fyEntity = (ClassifyEntity) getItem(position);
			txtStr = fyEntity.getClassifyname();
		}
		if(type == FilterType.CIRCLE){
			Circle ayEntity = (Circle) getItem(position);
			txtStr = ayEntity.getCirclename();
		}
		tag.tv_txt.setText(txtStr);
		
		
		//设置图标
		if(type == FilterType.CATEGRORY){
			tag.iv_logo.setVisibility(View.VISIBLE);
		}else{
			tag.iv_logo.setVisibility(View.GONE);
		}
		
		//选择状态
		boolean b = selects.get(position);
		if (b) {
			tag.tv_txt.setSelected(b);
			if(type == FilterType.CATEGRORY){
				ClassifyEntity fyEntity = (ClassifyEntity) getItem(position);
				ImageLoader.getInstance().displayImage(fyEntity.getOnsmallicon(), tag.iv_logo);
			}
			if(type == FilterType.SORT){
				tag.iv_right.setVisibility(View.INVISIBLE);
			}else{
				tag.iv_right.setVisibility(View.VISIBLE);	
			}
		}else{
			tag.tv_txt.setSelected(false);
			if(type == FilterType.CATEGRORY){
				ClassifyEntity fyEntity = (ClassifyEntity) getItem(position);
				ImageLoader.getInstance().displayImage(fyEntity.getOutsmallicon(), tag.iv_logo);
			}
			tag.iv_right.setVisibility(View.INVISIBLE);
		}
		
		//分隔线
		if(position == getCount() -1){
			tag.dir.setVisibility(View.INVISIBLE);
		}else{
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
