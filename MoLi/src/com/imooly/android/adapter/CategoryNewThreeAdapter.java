package com.imooly.android.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.entity.RspGoodsClassList.GoodsClassify;

/**
 * 第三分类适配器
 * @author 烨
 *
 */
public class CategoryNewThreeAdapter extends BaseAdapter {

	public interface CategoryThreeListener {
		public void getCategoryThreeId(String categorythreeid);
	}
	CategoryThreeListener l;
	public CategoryThreeListener getL() {
		return l;
	}
	public void setL(CategoryThreeListener l) {
		this.l = l;
	}
	
	private Context context;
	private List<GoodsClassify> categorylist;

	public CategoryNewThreeAdapter(Context context, List<GoodsClassify> categorylist) {
		this.context = context;
		this.categorylist = categorylist;
	}

	@Override
	public int getCount() {
		return categorylist.size();
	}

	@Override
	public Object getItem(int position) {
		return categorylist.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final GoodsClassify category = categorylist.get(position);
		ViewHolder holder;
		
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.category_new_3, parent, false);
			
			holder = new ViewHolder();

			holder.cate_tv_three = (TextView) convertView.findViewById(R.id.cate_tv_three);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.cate_tv_three.setText(category.getClassifyname());

		return convertView;
	}
	
	public class ViewHolder {
		TextView cate_tv_three;
	}
}
