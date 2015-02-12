package com.imooly.android.adapter;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.entity.RspGoodsClassList.GoodsClassify;

/**
 * 第二分类适配器
 * @author 烨
 *
 */
public class CategoryNewTwoAdapter extends BaseAdapter {

	public interface CategoryTwoListener {
		public void getCategoryTwoId(List<GoodsClassify> categorylist);
	}
	CategoryTwoListener l;
	public CategoryTwoListener getL() {
		return l;
	}
	public void setL(CategoryTwoListener l) {
		this.l = l;
	}
	
	private Context context;
	private List<GoodsClassify> categorylist;
	private int selectedPosition = -1;// 选中的位置  
	private Resources resources;
	
	public CategoryNewTwoAdapter(Context context, List<GoodsClassify> categorylist) {
		resources = context.getResources();
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
	
	public void setSelectedPosition(int position) {  
        selectedPosition = position;  
    } 
	
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final GoodsClassify category = categorylist.get(position);
		ViewHolder holder;
		
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(
					R.layout.category_new_2, parent, false);
			
			holder = new ViewHolder();

			holder.cate_tv_two = (TextView) convertView.findViewById(R.id.cate_tv_two);
			holder.cate_iv_delta = (ImageView) convertView.findViewById(R.id.cate_iv_delta);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		if (selectedPosition == position) {  
			holder.cate_tv_two.setTextColor(resources.getColor(R.color.main_color));
			holder.cate_iv_delta.setVisibility(View.VISIBLE);
        } else {
        	holder.cate_tv_two.setTextColor(resources.getColor(R.color.weak_text_color));
        	holder.cate_iv_delta.setVisibility(View.GONE);
        }
		
		holder.cate_tv_two.setText(category.getClassifyname());
		
		return convertView;
	}
	
	public class ViewHolder {
		TextView cate_tv_two;
		ImageView cate_iv_delta;
	}
}
