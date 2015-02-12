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
import com.imooly.android.ui.StoreSearchResultActivity;

/**
 * 实体店搜索 gridview适配器
 * 
 * @author lsd
 * 
 */
public class StoreSearchHotGridAdapter extends BaseAdapter {

	private Context context;
	private List<String> keywords;

	public StoreSearchHotGridAdapter(Context context, List<String> keywords) {
		this.context = context;
		this.keywords = keywords;
	}

	@Override
	public int getCount() {
		return keywords.size();
	}

	@Override
	public Object getItem(int position) {
		return keywords.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, final ViewGroup parent) {
		final String ety = keywords.get(position);
		ViewHolder holder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.layout_store_search_grid_item, parent, false);
			holder = new ViewHolder();
			holder.textView1 = (TextView) convertView.findViewById(R.id.textView1);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.textView1.setText(ety);
		holder.textView1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Context context = parent.getContext();
				Intent intent = new Intent(context, StoreSearchResultActivity.class);
				intent.putExtra(StoreSearchResultActivity.SEARCH_KEY, ety);
				intent.putExtra(StoreSearchResultActivity.ENTRY_TYPE, "key_search");
				context.startActivity(intent);
			}
		});

		return convertView;
	}

	public class ViewHolder {
		TextView textView1;
	}
}
