package com.imooly.android.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.entity.RspBusinessClassifyList.SubClassifyEntity;
import com.imooly.android.ui.StoreSearchResultActivity;

/**
 * 实体店分类gridview适配器
 * 
 * @author daiye
 * 
 */
public class SubCategoryGridViewAdapter extends BaseAdapter {

	private String parent_id;
	private Context context;
	private List<SubClassifyEntity> subclassify;

	public SubCategoryGridViewAdapter(Context context, String parent_id,List<SubClassifyEntity> subclassify) {
		this.parent_id = parent_id;
		this.context = context;
		this.subclassify = subclassify;
	}

	@Override
	public int getCount() {
		return subclassify == null ? 0 : subclassify.size();
	}

	@Override
	public Object getItem(int position) {
		return subclassify == null ? null : subclassify.get(position);
	}

	@Override
	public long getItemId(int position) {
		return subclassify == null ? 0 : position;
	}

	public void clearData() {
		subclassify.clear();
	}

	public void addGoodsList(List<SubClassifyEntity> subclassify) {
		this.subclassify.addAll(subclassify);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final SubClassifyEntity subclassifyitem = subclassify.get(position);

		ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.storecategory_gridview_item, parent, false);
			holder.tv_substorecategory = (TextView) convertView.findViewById(R.id.tv_substorecategory);
			holder.item_dir = convertView.findViewById(R.id.item_dir);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		int count = getCount() - 1;
		int sub = count % 3;

		if ((count - position) > sub) {
			holder.item_dir.setVisibility(View.VISIBLE);
		} else {
			holder.item_dir.setVisibility(View.GONE);
		}
		holder.tv_substorecategory.setText(subclassifyitem.getClassifyname());
		holder.tv_substorecategory.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String classifyid = subclassifyitem.getClassifyid();
				Intent intent = new Intent(context, StoreSearchResultActivity.class);
				intent.putExtra(StoreSearchResultActivity.SEARCH_ID, classifyid);
				intent.putExtra(StoreSearchResultActivity.PARENT_ID, parent_id);
				intent.putExtra(StoreSearchResultActivity.ENTRY_TYPE, "category_search");
				context.startActivity(intent);
			}
		});

		return convertView;
	}

	public class ViewHolder {
		TextView tv_substorecategory;
		View item_dir;
	}
}
