package com.imooly.android.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.imooly.android.R;

/**
 * 路线计划 展示适配器
 * 
 * @author lsd
 * 
 */
public class StoreRoutePlanShowAdapter extends BaseAdapter {

	private Context context;
	private List<String> list;

	public StoreRoutePlanShowAdapter(Context context) {
		this.context = context;
	}

	public void setData(List<String> list) {
		this.list = list;
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
	public View getView(final int position, View convertView, final ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.view_text, parent, false);
			holder = new ViewHolder();
			holder.textView1 = (TextView) convertView.findViewById(R.id.tv);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String ety = (String) getItem(position);
		holder.textView1.setText(ety);
		return convertView;
	}

	public class ViewHolder {
		TextView textView1;
	}
}
