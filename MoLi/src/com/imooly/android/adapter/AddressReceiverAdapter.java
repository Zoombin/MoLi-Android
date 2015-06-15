package com.imooly.android.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.entity.RspAddressList;

/***
 * 收货地址
 * 
 * @author
 * 
 */
public class AddressReceiverAdapter extends BaseAdapter {
	private List<RspAddressList.Address> list;
	private Context mContext;
	private boolean isSelectAddress = false;

	public boolean isSelectAddress() {
		return isSelectAddress;
	}

	public void setSelectAddress(boolean isSelectAddress) {
		this.isSelectAddress = isSelectAddress;
	}

	public List<RspAddressList.Address> getList() {
		return list;
	}

	public void setList(List<RspAddressList.Address> list) {
		this.list = list;
	}

	public AddressReceiverAdapter(Context context) {
		this.mContext = context;
	}

	public void addData(List<RspAddressList.Address> list) {
		
		if (this.list == null) {
			this.list = new ArrayList<RspAddressList.Address>();
		}
		
		
		
		this.list.addAll(list);
		this.notifyDataSetChanged();

	}
	
	
	

	// public void setData(List<RspAddressList.Address> list) {
	// this.list = list;
	// this.notifyDataSetChanged();
	// }

	public void cleanData() {
		if (list != null) {
			list.clear();
		}
		this.notifyDataSetChanged();
	}

	public void deleteData(int position) {
		
		Log.d("xxx", ">>>>>>>>>>>>>>>>deleteDate position = " + position);
		
		Log.d("xxx", ">>>>>>>>>>list.size() 1= " + list.size());
		if (list != null) {
			list.remove(position);
		}
		
		Log.d("xxx", ">>>>>>>>>>list.size() 2= " + list.size());
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
	public View getView(int position, View convertView, ViewGroup parent) {
		
		Log.d("xxx", "position = " + position);
		
		final int myPosition = position;
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
			tag.text_index_title = (TextView) convertView.findViewById(R.id.text_index_title);
			
			// 隐藏 ，设置默认字符
			if (isSelectAddress) {
				tag.text_check.setVisibility(View.INVISIBLE);
			}

			convertView.setTag(tag);

		} else {
			tag = (Tag) convertView.getTag();
		}

		RspAddressList.Address address = (RspAddressList.Address) getItem(myPosition);

		if (address != null) {
			tag.text_user_name.setText(address.getName());
			tag.text_user_number.setText(address.getMobile());
//			String addrs = address.getProvince() + address.getCity() + address.getStreet();
			tag.text_user_address.setText(address.getStreet());
			tag.text_index_title.setText(address.getProvince() + address.getCity() + address.getArea());

			Log.d("address", "isdefault = " + address.getIsdefault());

			if (address.getIsdefault() == 1) { // 1为默认
				tag.text_check.setText("[已默认]");
				tag.text_check.setTextColor(mContext.getResources().getColor(R.color.main_color));
				tag.image_top.setImageResource(R.drawable.bg_address_line);
				tag.image_top.setVisibility(View.VISIBLE);
				tag.image_bottom.setImageResource(R.drawable.bg_address_line);
				tag.text_check.setClickable(false);
			} else {
				tag.text_check.setText("[设为默认]");
				tag.text_check.setClickable(true);
				tag.text_check.setTextColor(mContext.getResources().getColor(R.color.app_text_dark_gray));
				tag.image_top.setVisibility(View.GONE);
				tag.image_bottom.setImageResource(R.drawable.shape_dash_line);
			}

			tag.text_check.setTag(address);


		}

		return convertView;
	}



	class Tag {
		ImageView image_top;
		ImageView image_bottom;
		TextView text_index_title;
		TextView text_check;
		TextView text_user_name;
		TextView text_user_number;
		TextView text_user_address;
	}

}
