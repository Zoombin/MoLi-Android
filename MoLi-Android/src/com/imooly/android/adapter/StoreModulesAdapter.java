package com.imooly.android.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.entity.RspAdvertiseIndexads.Info;
import com.imooly.android.ui.StoreCategoryActivity;
import com.imooly.android.ui.StoreSearchResultActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 实体店模块适配器
 * 
 * @author daiye
 * 
 */
public class StoreModulesAdapter extends BaseAdapter {

	private Context context;
	private List<Info> infos;
	// 电子会员卡
	String CD01 = "CD01";
	// 代金卷
	String CD02 = "CD02";
	// 会员特权
	String MP01 = "MP01";
	// 我的收藏
	String MC01 = "MC01";
	// 实体店详情
	String PH01 = "PH01";
	// 商家分类搜索
	String SS01 = "SS01";

	public StoreModulesAdapter(Context context, List<Info> infos) {
		this.context = context;
		this.infos = infos;
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
			convertView = LayoutInflater.from(context).inflate(R.layout.home_module_item, null);
			holder = new ViewHolder();
			holder.layout_module = (RelativeLayout) convertView.findViewById(R.id.layout_module);
			holder.iv_module = (ImageView) convertView.findViewById(R.id.iv_module);
			holder.tv_module = (TextView) convertView.findViewById(R.id.tv_module);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final String id = info.getParamid();
		holder.layout_module.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if ("".equals(id)) {
					context.startActivity(new Intent(context, StoreCategoryActivity.class));
				} else {
					Intent intent = new Intent(context, StoreSearchResultActivity.class);
					intent.putExtra(StoreSearchResultActivity.SEARCH_ID, id);
					intent.putExtra(StoreSearchResultActivity.PARENT_ID, id);
					intent.putExtra(StoreSearchResultActivity.ENTRY_TYPE, "category_search");
					context.startActivity(intent);
				}
			}
		});
		if (!TextUtils.isEmpty(info.getImagepath())) {
			ImageLoader.getInstance().displayImage(info.getImagepath(), holder.iv_module);
		} else {
			holder.iv_module.setImageResource(R.drawable.ic_error);
		}
		holder.tv_module.setText(info.getTitle());

		return convertView;
	}

	public class ViewHolder {
		RelativeLayout layout_module;
		ImageView iv_module;
		TextView tv_module;
	}
}
