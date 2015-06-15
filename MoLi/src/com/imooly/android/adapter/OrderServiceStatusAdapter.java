package com.imooly.android.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.Interface.OpHubClickListener;
import com.imooly.android.entity.RspOrderList.OrderOpEty;
import com.imooly.android.entity.RspSgoodslist.SGoodsEty;
import com.imooly.android.tool.OrderOperate;
import com.imooly.android.tool.OrderOperate.OCallBack;
import com.imooly.android.ui.OrderLogisticsActivity;
import com.imooly.android.ui.OrderServiceActivity;
import com.imooly.android.ui.OrderServiceBacktrackActivity;
import com.imooly.android.ui.OrderServiceDetail;
import com.imooly.android.widget.HorizontalLinearLayout;
import com.imooly.android.widget.HorizontalListView;
import com.nostra13.universalimageloader.core.ImageLoader;

/***
 * 售后服务-退换货商品列表 ListView适配器
 * 
 * @author lsd
 * 
 */
public class OrderServiceStatusAdapter extends BaseAdapter implements OnClickListener {
	Context context;
	List<SGoodsEty> list;
	String type;
	OCallBack callBack;

	public OrderServiceStatusAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public void setOCallback(OCallBack callBack) {
		this.callBack = callBack;
	}

	public void setData(String type, List<SGoodsEty> list) {
		this.list = list;
		this.type = type;
		this.notifyDataSetChanged();
	}

	public void addData(List<SGoodsEty> list) {
		if (list == null || list.size() == 0) {
			return;
		}
		if (this.list == null) {
			this.list = new ArrayList<SGoodsEty>();
		}
		this.list.addAll(list);
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
		// TODO Auto-generated method stub
		Tag tag = null;
		if (convertView == null) {
			tag = new Tag();
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_order_server_status_item, null);
			tag.item_content = (LinearLayout) convertView.findViewById(R.id.item_content);
			tag.item_pic = (ImageView) convertView.findViewById(R.id.item_pic);
			tag.item_name = (TextView) convertView.findViewById(R.id.item_name);
			tag.item_price = (TextView) convertView.findViewById(R.id.item_price);
			tag.item_num = (TextView) convertView.findViewById(R.id.item_num);
			tag.item_status = (TextView) convertView.findViewById(R.id.item_status);
			tag.item_status_info = (TextView) convertView.findViewById(R.id.item_status_info);
			tag.op_layout = (LinearLayout) convertView.findViewById(R.id.op_layout);
			tag.hlistview = (HorizontalLinearLayout) convertView.findViewById(R.id.hlistview);
			convertView.setTag(tag);
		} else {
			tag = (Tag) convertView.getTag();
		}

		SGoodsEty ety = (SGoodsEty) getItem(position);
		if (ety != null) {
			tag.item_content.setTag(position);
			tag.item_content.setOnClickListener(this);

			ImageLoader.getInstance().displayImage(ety.getImage(), tag.item_pic);

			tag.item_name.setText(ety.getName());
			DecimalFormat fnum = new DecimalFormat("##0.00");
			tag.item_price.setText("价格：" + "￥" + fnum.format(ety.getPrice()));
			tag.item_num.setText("数量：" + ety.getNum());
			tag.item_status.setText("状态：");
			tag.item_status_info.setText(ety.getStatusname());

			// 按钮列表
			List<OrderOpEty> opList = ety.getOplist();
			if (opList == null || opList.size() == 0) {
				tag.op_layout.setVisibility(View.GONE);
			} else {
				tag.op_layout.setVisibility(View.VISIBLE);

				//OrderOPAdapter opAdapter = new OrderOPAdapter(context);
				//tag.hlistview.setAdapter(opAdapter);
				//opAdapter.setOnclickLister(opHubClickListener);
				//opAdapter.setData(ety, opList);
				tag.hlistview.setOnclickLister(opHubClickListener);
				tag.hlistview.setData(ety, opList);
			}
		}
		return convertView;
	}

	class Tag {
		LinearLayout item_content;
		ImageView item_pic;
		TextView item_name;
		TextView item_price;
		TextView item_num;
		TextView item_status;
		TextView item_status_info;
		LinearLayout op_layout;
		HorizontalLinearLayout hlistview;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.item_content:
			int position = (Integer) v.getTag();
			SGoodsEty ety = (SGoodsEty) getItem(position);
			Intent intents = new Intent(context, OrderServiceDetail.class);
			intents.putExtra(OrderServiceDetail.ORDER_NO, ety.getOrderno());
			intents.putExtra(OrderServiceDetail.GOOD_ID, ety.getGoodsid());
			intents.putExtra(OrderServiceDetail.UNIQUE, ety.getUnique());
			context.startActivity(intents);
			break;
		default:
			break;
		}
	}
	
	/***
	 * 
	 * 操作集点击
	 * 
	 */
	OpHubClickListener opHubClickListener = new OpHubClickListener() {
		@Override
		public void onClick(Object parentEntity, Object Entity) {
			// TODO Auto-generated method stub
			OrderOpEty tag = (OrderOpEty) Entity;

			SGoodsEty sgodEty = (SGoodsEty) parentEntity;
			String orderno = sgodEty.getOrderno();
			String tradeid = sgodEty.getTradeid();
			String goodsid = sgodEty.getGoodsid();
			String unique = sgodEty.getUnique();

			// 售后类型(退货/换货)
			String type = OrderServiceStatusAdapter.this.type;

			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("orderno", orderno);
			bundle.putString("goodsid", goodsid);
			bundle.putString("tradeid", tradeid);
			bundle.putString("unique", unique);

			String code = tag.getCode();
			if ("OP011".equals(code)) {
				// OP011 申请售后
				intent.setClass(context, OrderServiceActivity.class);
				intent.putExtras(bundle);
				context.startActivity(intent);
			}
			if ("OP012".equals(code)) {
				// OP012 填写物流信息

				// 这里的type是 退货、换货（return/change）
				intent.setClass(context, OrderServiceBacktrackActivity.class);
				bundle.putString("type", type);
				intent.putExtras(bundle);
				context.startActivity(intent);
			}
			if ("OP013".equals(code)) {
				// OP013 取消售后
				OrderOperate.servicecancel(context, orderno, goodsid, tradeid, type, callBack);
			}
			if ("OP014".equals(code)) {
				// OP014 查看物流
				// 这里的type是 换货/正常（change/nomal）
				intent.setClass(context, OrderLogisticsActivity.class);
				bundle.putString("type", "change");
				intent.putExtras(bundle);
				context.startActivity(intent);
			}
			if ("OP015".equals(code)) {
				// OP015 延迟收货
				// 这里的type是 换货/正常（change/nomal）
				OrderOperate.takedelay(context, "change", orderno, goodsid, tradeid, unique, callBack);
			}
			if ("OP016".equals(code)) {
				// OP016 确认收货 (售后不需要输入密码)
				// 这里的type是 换货/正常（change/nomal）
				/*OrderOperate.showConfirmDialog(context, sgodEty.getName(), sgodEty.getPrice(), "change", orderno, goodsid, tradeid, unique,
						null);*/
				OrderOperate.take(context, "change", orderno, "", goodsid, tradeid, unique, callBack);
			}
			if ("OP017".equals(code)) {
				// OP017 人工服务
				OrderOperate.servicemanual(context, orderno, goodsid, tradeid, type, callBack);
			}
		}
	};
}
