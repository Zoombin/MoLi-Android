package com.imooly.android.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.entity.RspAddressList;
import com.imooly.android.entity.RspAddressList.Address;
import com.imooly.android.entity.RspSuccessCommon;
import com.imooly.android.entity.ServiceResult;

/***
 * 水平ListView适配器
 * 
 * @author lsd
 * 
 */
public class AddressAdapter extends BaseAdapter {
	List<RspAddressList.Address> list;

	private Context mContext;

	public AddressAdapter(Context context) {
		this.mContext = context;
	}

	public void addData(List<RspAddressList.Address> list) {
		if (this.list == null) {
			this.list = new ArrayList<RspAddressList.Address>();
		}
		if (list != null) {
			this.list.addAll(list);
			this.notifyDataSetChanged();
		}
	}

	public void setData(List<RspAddressList.Address> list) {
		this.list = list;
		this.notifyDataSetChanged();
	}

	public void cleanData() {
		if (list != null) {
			list.clear();
		}
		this.notifyDataSetChanged();
	}

	public void deleteData(int position) {
		if (list != null) {
			list.remove(position);
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final Tag tag;
		if (convertView == null) {
			tag = new Tag();
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_address, null);
			tag.image_top = (ImageView) convertView.findViewById(R.id.image_top);
			tag.image_bottom = (ImageView) convertView.findViewById(R.id.image_bottom);
			tag.text_check = (TextView) convertView.findViewById(R.id.text_check);
			tag.text_user_name = (TextView) convertView.findViewById(R.id.text_user_name);
			tag.text_user_number = (TextView) convertView.findViewById(R.id.text_user_number);
			tag.text_user_address = (TextView) convertView.findViewById(R.id.text_user_address);

			convertView.setTag(tag);
		} else {
			tag = (Tag) convertView.getTag();
		}

		RspAddressList.Address address = (RspAddressList.Address) getItem(position);
		if (address != null) {
			tag.text_user_name.setText(address.getName());
			tag.text_user_number.setText(address.getMobile());
			String addrs = address.getProvince() + address.getCity() + address.getStreet();
			tag.text_user_address.setText(addrs);

			if(address.getIsdefault() == 1){
				//默认收货地址
			}
			
			
			tag.text_check.setTag(address);
			tag.text_check.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {

					tag.text_check.setText("[已默认]");
					tag.text_check.setTextColor(mContext.getResources().getColor(R.color.main_color));
					tag.image_top.setImageResource(R.drawable.bg_address_line);
					tag.image_top.setVisibility(View.VISIBLE);
					tag.image_bottom.setImageResource(R.drawable.bg_address_line);

					RspAddressList.Address address = (Address) view.getTag();
					Api.setDefaultAddress(mContext, address.getAddressid(), new NetCallBack<ServiceResult>() {

						@Override
						public void success(ServiceResult rspData) {
							// TODO Auto-generated method stub
						}

						@Override
						public void failed(String msg) {
							// TODO Auto-generated method stub
						}
					}, RspSuccessCommon.class);
				}
			});

		}

		return convertView;
	}

	class Tag {
		ImageView image_top;
		ImageView image_bottom;
		TextView text_check;
		TextView text_user_name;
		TextView text_user_number;
		TextView text_user_address;
	}

}
