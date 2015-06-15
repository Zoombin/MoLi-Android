package com.imooly.android.adapter;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.imooly.android.R;
import com.imooly.android.Interface.OpHubClickListener;
import com.imooly.android.entity.RspOrderList.OrderEty;
import com.imooly.android.entity.RspOrderList.OrderOpEty;
import com.imooly.android.tool.OrderOperate;
import com.imooly.android.tool.OrderOperate.OCallBack;
import com.imooly.android.ui.OrderCommentActivity;
import com.imooly.android.ui.OrderDetailActivity;
import com.imooly.android.ui.OrderLogisticsActivity;
import com.imooly.android.ui.VoucherHadGotActivity;
import com.imooly.android.view.CommonConfirmDialog;
import com.imooly.android.view.CommonConfirmDialog.CommonAlertCallBack;
import com.imooly.android.widget.CannotRollListView;
import com.imooly.android.widget.HorizontalLinearLayout;
import com.imooly.android.widget.HorizontalListView;

/***
 * 所有订单适配器
 * 
 * @author lsd
 * 
 */
public class OrderAdapter extends BaseAdapter implements OnClickListener {
	Context context;
	List<OrderEty> list;
	OCallBack callBack;

	public OrderAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public void setOCallback(OCallBack callBack) {
		this.callBack = callBack;
	}

	public void setData(List<OrderEty> list) {
		this.list = list;
		this.notifyDataSetChanged();
	}

	public void addData(List<OrderEty> list) {
		if (list == null || list.size() == 0) {
			return;
		}
		if (this.list == null) {
			this.list = new ArrayList<OrderEty>();
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
			convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, null);
			tag.order_item_layout = (LinearLayout) convertView.findViewById(R.id.order_item_layout);
			tag.order_item_icon = (ImageView) convertView.findViewById(R.id.order_item_icon);
			tag.order_item_name = (TextView) convertView.findViewById(R.id.order_item_name);
			tag.order_item_top_price = (TextView) convertView.findViewById(R.id.order_item_top_price);
			tag.subList = (CannotRollListView) convertView.findViewById(R.id.comm_list);
			tag.op_layout = (LinearLayout) convertView.findViewById(R.id.op_layout);
			tag.hlistview = (HorizontalLinearLayout) convertView.findViewById(R.id.hlistview);
			convertView.setTag(tag);
		} else {
			tag = (Tag) convertView.getTag();
		}

		OrderEty ety = (OrderEty) getItem(position);
		if (ety != null) {
			tag.order_item_layout.setTag(ety);
			tag.order_item_layout.setOnClickListener(this);

			tag.order_item_name.setText(ety.getStorename());
			DecimalFormat fnum = new DecimalFormat("##0.00");
			tag.order_item_top_price.setText("￥" + fnum.format(ety.getTotalprice()));

			// 商品列表
			OrderSubAdapter subAdapter = new OrderSubAdapter(context);
			tag.subList.setAdapter(subAdapter);
			tag.subList.setTag(ety);
			tag.subList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					// TODO Auto-generated method stub
					OrderEty ety = (OrderEty) parent.getTag();
					context.startActivity(new Intent(context, OrderDetailActivity.class).putExtra(OrderDetailActivity.ORDER_NO,
							ety.getOrderno()));
				}
			});
			subAdapter.setData(ety.getGoods());

			// 按钮列表
			List<OrderOpEty> opList = ety.getOplist();
			if (opList != null && opList.size() > 0) {
				tag.op_layout.setVisibility(View.VISIBLE);
				//OrderOPAdapter opAdapter = new OrderOPAdapter(context);
				//tag.hlistview.setAdapter(opAdapter);
				//opAdapter.setOnclickLister(opClickListener);
				//opAdapter.setData(ety, opList);
				tag.hlistview.setOnclickLister(opClickListener);
				tag.hlistview.setData(ety, opList);
			} else {
				tag.op_layout.setVisibility(View.GONE);
			}

		}
		return convertView;
	}

	class Tag {
		LinearLayout order_item_layout;
		ImageView order_item_icon;
		TextView order_item_name;
		TextView order_item_top_price;
		CannotRollListView subList;
		LinearLayout op_layout;
		HorizontalLinearLayout hlistview;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.order_item_layout:
			OrderEty etyt = (OrderEty) v.getTag();
			context.startActivity(new Intent(context, OrderDetailActivity.class).putExtra(OrderDetailActivity.ORDER_NO, etyt.getOrderno()));
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
	OpHubClickListener opClickListener = new OpHubClickListener() {
		@Override
		public void onClick(Object parentEntity, Object Entity) {
			// TODO Auto-generated method stub
			final OrderEty ety = (OrderEty) parentEntity;
			
			OrderOpEty tag = (OrderOpEty) Entity;
			String code = tag.getCode();
			if ("OP001".equals(code)) {
				// OP001 取消订单
				CommonConfirmDialog.show(context, "是否取消该订单?", "","",new CommonAlertCallBack() {
					@Override
					public void onConfirm() {
						// TODO Auto-generated method stub
						OrderOperate.cancelOrder(context, ety.getOrderno(), callBack);
					}

					@Override
					public void onCancel() {
						// TODO Auto-generated method stub
					}
				});
			}
			if ("OP002".equals(code)) {
				// OP002 删除订单
				OrderOperate.deleteOrder(context, ety.getOrderno(), callBack);
			}
			if ("OP003".equals(code)) {
				// OP003 付款
				List<String> list = new ArrayList<String>();
				list.add(ety.getOrderno());
				String orderNo = new Gson().toJson(list);
				OrderOperate.payOrder(context,orderNo, null);
			}
			if ("OP004".equals(code)) {
				// OP004 提醒卖家发货
				OrderOperate.noticeOrder(context, ety.getOrderno(), callBack);
			}
			if ("OP005".equals(code)) {
				// OP005 查看物流
				Intent intent = new Intent(context, OrderLogisticsActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("orderno", ety.getOrderno());
				bundle.putString("type", "normal");
				intent.putExtras(bundle);
				context.startActivity(intent);
			}
			if ("OP006".equals(code)) {
				// OP006 确认收货
				/*OrderOperate.showConfirmDialog(context, ety.getStorename(), ety.getTotalprice(), "normal", ety.getOrderno(), "", "", "",
						callBack);*/
				OrderOperate.checkIsSettedTraderPassword(context, ety.getStorename(), ety.getTotalprice(), "normal", ety.getOrderno(), "", "", "",
						callBack);
			}
			if ("OP007".equals(code)) {
				// OP007 领代金券
				((Activity) context).startActivityForResult(new Intent(context, VoucherHadGotActivity.class), 100);
			}
			if ("OP008".equals(code)) {
				// OP008 评价
				((Activity) context).startActivityForResult(
						new Intent(context, OrderCommentActivity.class).putExtra(OrderCommentActivity.ORDER_NO, ety.getOrderno()), 100);
			}
			if ("OP009".equals(code)) {
				// OP009 延迟收货 (正常订单 不需要后面几个参数)
				OrderOperate.takedelay(context, "normal", ety.getOrderno(), "", "", "", callBack);
			}
		}
	};
}
