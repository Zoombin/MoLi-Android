package com.imooly.android.adapter;

import java.util.ArrayList;
import java.util.List;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.entity.RspCommentList.Comment;
import com.imooly.android.widget.HorizontalListView;

/***
 * 商城评价 - ListView适配器
 * 
 * @author lsd
 * 
 */
public class StoreCommentListAdapter extends BaseAdapter {
	List<Comment> list;

	public void setData(List<Comment> list) {
		this.list = list;
		this.notifyDataSetChanged();
	}
	public void addData(List<Comment> list) {
		if(this.list == null){
			this.list = new ArrayList<Comment>();
		}
		if(list != null){
			this.list.addAll(list);
			this.notifyDataSetChanged();
		}
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
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_store_comment_item, null);
			tag.store_comment_content = (TextView) convertView.findViewById(R.id.store_comment_content);
			tag.store_comment_spec = (TextView) convertView.findViewById(R.id.store_comment_spec);
			tag.store_comment_username = (TextView) convertView.findViewById(R.id.store_comment_username);
			tag.store_comment_time = (TextView) convertView.findViewById(R.id.store_comment_time);
			tag.store_comment_excess = (TextView) convertView.findViewById(R.id.store_comment_excess);
			tag.store_comment_hlist = (HorizontalListView) convertView.findViewById(R.id.store_comment_hlist);
			convertView.setTag(tag);
		} else {
			tag = (Tag) convertView.getTag();
		}
		
		Comment data = (Comment) getItem(position);
		if(data != null){
			tag.store_comment_content.setText(data.getContent());
			tag.store_comment_spec.setText(data.getSpec());
			tag.store_comment_username.setText(data.getUsername());
			tag.store_comment_time.setText(data.getSenddate());
			
			String excessContent = data.getAddcontent();
			if(TextUtils.isEmpty(excessContent)){
				tag.store_comment_excess.setVisibility(View.GONE);
			}else{
				tag.store_comment_excess.setVisibility(View.VISIBLE);
				tag.store_comment_excess.setText(excessContent);
			}
			
			List<String> imgs = data.getImages();
			if(imgs != null && imgs.size()>0){
				tag.store_comment_hlist.setVisibility(View.VISIBLE);
				StoreCommentHListAdapter adapter = new StoreCommentHListAdapter();
				tag.store_comment_hlist.setAdapter(adapter);
				adapter.setData(imgs);
			}else{
				tag.store_comment_hlist.setVisibility(View.GONE);
			}
		}

		return convertView;
	}

	class Tag {
		TextView store_comment_content;
		TextView store_comment_spec;
		TextView store_comment_username;
		TextView store_comment_time;
		TextView store_comment_excess;
		HorizontalListView store_comment_hlist;
	}
}
