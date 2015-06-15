package com.imooly.android.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.entity.RspAdvertiseIndexads.Info;
import com.imooly.android.entity.RspOrderProfile.logisticEty;
import com.imooly.android.ui.CollectionActivity;
import com.imooly.android.ui.MemberCardActivity;
import com.imooly.android.ui.MemberRightActivity;
import com.imooly.android.ui.VoucherActivity;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 首页模块适配器
 * 
 * @author daiye
 * 
 */
public class HomeModulesAdapter extends BaseAdapter {

	private Context context;
	private List<Info> infos;
	// 电子会员卡
	String CD01 = "CD01";
	// 代金卷
	String CD02 = "CD02";
	//会员特权
	String MP01 = "MP01";
	//我的收藏
	String MC01 = "MC01";
	
	public HomeModulesAdapter(Context context, List<Info> infos) {
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
			convertView = LayoutInflater.from(context).inflate(
					R.layout.home_module_item, parent, false);

			holder = new ViewHolder();

			holder.layout_module = (RelativeLayout)  convertView
					.findViewById(R.id.layout_module);
			
			holder.iv_module = (ImageView) convertView
					.findViewById(R.id.iv_module);
			
			holder.tv_module = (TextView) convertView
					.findViewById(R.id.tv_module);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final String apppagecode = info.getApppagecode();
		holder.layout_module.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				boolean skip = false;
				if (apppagecode.equals(CD01)) {
					// 电子会员卡
					skip = true;
					intent.setClass(context, MemberCardActivity.class);
				} else if (apppagecode.equals(CD02)) {
					// 代金卷
					skip = true;
					intent.setClass(context, VoucherActivity.class);
				} else if (apppagecode.equals(MP01) || apppagecode.equals("MP0")) {
					// 会员特权
					skip = true;
					intent.setClass(context, MemberRightActivity.class);
				} else if (apppagecode.equals(MC01)) {
					// 我的收藏
					skip = true;
					intent.setClass(context, CollectionActivity.class);
				}
				if(skip){
					context.startActivity(intent);
				}
			}
		});
		
		Log.d("xxx", "info.getImagepath() = " + info.getImagepath());
		
		ImageLoader.getInstance().displayImage(info.getImagepath(), holder.iv_module);
		holder.tv_module.setText(info.getTitle());
		
		return convertView;
	}

	public class ViewHolder {
		RelativeLayout layout_module;
		ImageView iv_module;
		TextView tv_module;
	}
}
