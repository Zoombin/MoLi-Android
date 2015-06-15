package com.imooly.android.adapter;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.entity.RspGoodsClassList.GoodsClassify;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * 第一分类适配器
 * 
 * @author 烨
 * 
 */
public class CategoryNewOneAdapter extends BaseAdapter {

	public interface CategoryOneListener {
		public void getCategoryOneId(List<GoodsClassify> categorylist);
	}

	CategoryOneListener l;

	public CategoryOneListener getL() {
		return l;
	}

	public void setL(CategoryOneListener l) {
		this.l = l;
	}

	private Context context;
	private List<GoodsClassify> categorylist;
	public int selectedPosition = -1;// 选中的位置
	private DisplayImageOptions defaultOptions;
	private Resources resources;
	private Animation mShowAction;
	
	public CategoryNewOneAdapter(Context context,
			List<GoodsClassify> categorylist) {
		resources = context.getResources();
		this.context = context;
		this.categorylist = categorylist;
		defaultOptions = new DisplayImageOptions.Builder()
				.displayer(new RoundedBitmapDisplayer(100)).cacheInMemory(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();
		mShowAction = AnimationUtils.loadAnimation(context,
				R.anim.slide_in_right);
	}
	
	public void refrashData(List<GoodsClassify> categorylist){
		this.categorylist = categorylist;
		notifyDataSetChanged();
	}
	
	public List<GoodsClassify> getData(){
		return this.categorylist;
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
					R.layout.category_new_1, parent, false);

			holder = new ViewHolder();

			holder.layout_category1 = (RelativeLayout) convertView
					.findViewById(R.id.layout_category1);
			holder.cate_iv_delta = (ImageView) convertView
					.findViewById(R.id.cate_iv_delta);
			holder.cate_iv_logo = (ImageView) convertView
					.findViewById(R.id.cate_iv_logo);
			holder.cate_iv_main = (TextView) convertView
					.findViewById(R.id.cate_iv_main);
			holder.cate_iv_sub = (TextView) convertView
					.findViewById(R.id.cate_iv_sub);
			holder.cate_iv_line = (ImageView) convertView
					.findViewById(R.id.cate_iv_line);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		ImageLoader.getInstance().displayImage(category.getClassifyicon(),
				holder.cate_iv_logo, defaultOptions);

		if (selectedPosition == position) {
			holder.cate_iv_delta
					.setVisibility(View.VISIBLE);
		} else {
			holder.cate_iv_delta
					.setVisibility(View.GONE);
		}

		holder.cate_iv_main.setText(category.getClassifyname());
		holder.cate_iv_sub.setText(category.getCaption());

		if (position == categorylist.size() - 1) {
			holder.cate_iv_line.setVisibility(View.GONE);
		} else {
			holder.cate_iv_line.setVisibility(View.VISIBLE);
		}
		
		return convertView;
	}

	public class ViewHolder {
		RelativeLayout layout_category1;
		ImageView cate_iv_delta;
		ImageView cate_iv_logo;
		TextView cate_iv_main;
		TextView cate_iv_sub;
		ImageView cate_iv_line;
	}
}
