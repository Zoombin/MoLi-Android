package com.imooly.android.ui;

import java.util.List;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.Interface.OpHubClickListener;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.adapter.OrderOPAdapter;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.entity.RspOrderList.OrderOpEty;
import com.imooly.android.entity.RspOrderProfile;
import com.imooly.android.entity.RspOrderProfile.OrderAddrEty;
import com.imooly.android.entity.RspOrderProfile.OrderGoodsEty;
import com.imooly.android.entity.RspOrderProfile.OrderLog;
import com.imooly.android.entity.RspOrderProfile.OrderServiceEty;
import com.imooly.android.entity.RspOrderProfile.OrderStatusEty;
import com.imooly.android.entity.RspOrderProfile.OrderStoEty;
import com.imooly.android.entity.RspOrderProfile.logisticEty;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.tool.OrderOperate;
import com.imooly.android.tool.OrderOperate.OCallBack;
import com.imooly.android.widget.HorizontalListView;
import com.imooly.android.widget.Toast;

/***
 * 订单详情
 * 
 * @author lsd
 * 
 */
public class OrderDetailActivity extends BaseActivity implements OnClickListener {
	public static String ORDER_NO = "order_no";
	private RelativeLayout ll_title;
	private ImageView iv_back;
	private TextView tv_title;

	private LinearLayout ll_content;

	private View order_detail_adsinfo;// 地址
	private TextView order_detail_ads_info;// 地址
	private TextView order_detail_zipcode_info;// 邮编
	private TextView order_detail_receiver_info;// 收件人

	private View order_detail_serinfo;// 客服
	private TextView order_detail_server_name;// 商家名称
	private Button order_detail_server_call;// 商家电话

	private RelativeLayout rl_logistics;
	private ImageView order_detail_status_img;// 状态图标
	private TextView order_detail_status_text;// 状态文字
	private TextView order_detail_status_time;// 时间

	private TextView order_detail_status_txt;
	private TextView order_detail_status;// 交易状态

	private LinearLayout order_detail_goods;// 商品列表
	private LinearLayout order_log_detail;// 交易细节

	// 详情数据
	RspOrderProfile.Data data;

	String orderno;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_detail);

		logActivityName(this);

		initView();
		getData();
	}

	private void initView() {
		// TODO Auto-generated method stub
		ll_title = (RelativeLayout) findViewById(R.id.ll_title);

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);

		tv_title = (TextView) findViewById(R.id.tv_title);

		ll_content = (LinearLayout) findViewById(R.id.ll_content);
		ll_content.setVisibility(View.INVISIBLE);

		order_detail_adsinfo = findViewById(R.id.order_detail_adsinfo);
		rl_logistics = (RelativeLayout) findViewById(R.id.rl_logistics);
		rl_logistics.setOnClickListener(this);
		order_detail_ads_info = (TextView) findViewById(R.id.order_detail_ads_info);
		order_detail_zipcode_info = (TextView) findViewById(R.id.order_detail_zipcode_info);
		order_detail_receiver_info = (TextView) findViewById(R.id.order_detail_receiver_info);
		order_detail_status_img = (ImageView) findViewById(R.id.order_detail_status_img);
		order_detail_status_text = (TextView) findViewById(R.id.order_detail_status_text);
		order_detail_status_time = (TextView) findViewById(R.id.order_detail_status_time);

		order_detail_serinfo = findViewById(R.id.order_detail_serinfo);
		order_detail_serinfo.setOnClickListener(this);
		order_detail_serinfo.setVisibility(View.GONE);
		order_detail_server_name = (TextView) findViewById(R.id.order_detail_server_name);
		order_detail_server_name.setOnClickListener(this);

		order_detail_server_call = (Button) findViewById(R.id.order_detail_server_call);
		order_detail_server_call.setOnClickListener(this);

		order_detail_status_txt = (TextView) findViewById(R.id.order_detail_status_txt);
		order_detail_status = (TextView) findViewById(R.id.order_detail_status);

		order_detail_goods = (LinearLayout) findViewById(R.id.order_detail_goods);
		order_log_detail = (LinearLayout) findViewById(R.id.order_log_detail);
	}

	private void getData() {
		String orderno = this.orderno = getIntent().getStringExtra(ORDER_NO);
		Api.orderProfile(self, orderno, new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {
				// TODO Auto-generated method stub
				RspOrderProfile rsp = (RspOrderProfile) rspData;
				if (rsp.data != null) {
					initData(rsp);
				}
			}

			@Override
			public void failed(String msg) {
				// TODO Auto-generated method stub
				if (!TextUtils.isEmpty(msg)) {
					Toast.show(self, msg);
				}

			}
		}, RspOrderProfile.class);
	}

	private void initData(RspOrderProfile rsp) {
		// TODO Auto-generated method stub
		ll_content.setVisibility(View.VISIBLE);
		data = rsp.data;
		OrderAddrEty addrEty = data.getAddress();
		if (addrEty != null) {
			order_detail_ads_info.setText(addrEty.getAddress());
			order_detail_zipcode_info.setText(addrEty.getPostcode());
			order_detail_receiver_info.setText(addrEty.getName() + "          " + addrEty.getTelephone());
		}

		OrderStoEty stoEty = data.getStore();
		if (stoEty != null) {
			order_detail_serinfo.setVisibility(View.VISIBLE);
			order_detail_server_name.setText(stoEty.getStorename());
			order_detail_server_call.setTag(stoEty.getTelephone());
			order_detail_serinfo.setTag(stoEty.getStoreid());
		}

		logisticEty logisticEty = data.getLogistic();
		if (logisticEty != null) {
			rl_logistics.setTag(logisticEty);
			order_detail_status_text.setText(logisticEty.getMsg());
			order_detail_status_time.setText(logisticEty.getTime());
		}

		OrderStatusEty statusEty = data.getStatus();
		order_log_detail.removeAllViews();
		if (statusEty != null) {
			order_detail_status.setText(statusEty.getCurrent());

			List<OrderLog> logs = statusEty.getLog();
			if (logs != null && logs.size() > 0) {
				OrderLog log0 = new OrderLog();
				log0.setTitle("订单编号");
				log0.setTime(orderno);
				logs.add(0, log0);
				for (OrderLog log : logs) {
					View listLayout = LayoutInflater.from(self).inflate(R.layout.layout_order_detail_logs_item, null);
					TextView tView = (TextView) listLayout.findViewById(R.id.tv);
					tView.setText(log.getTitle() + "：" + log.getTime());
					order_log_detail.addView(listLayout);
				}
			}
		}

		List<OrderGoodsEty> orderGdetys = data.getGoods();
		order_detail_goods.removeAllViews();
		if (orderGdetys != null && orderGdetys.size() > 0) {
			for (int i = 0; i < orderGdetys.size(); i++) {
				OrderGoodsEty gEty = orderGdetys.get(i);
				View itemLayout = LayoutInflater.from(self).inflate(R.layout.layout_order_detail_goods_item, null);
				ImageView order_detail_descr_pic = (ImageView) itemLayout.findViewById(R.id.order_detail_descr_pic);
				TextView order_detail_descr_name = (TextView) itemLayout.findViewById(R.id.order_detail_descr_name);
				TextView order_detail_descr_price = (TextView) itemLayout.findViewById(R.id.order_detail_descr_price);
				TextView order_detail_descr_num = (TextView) itemLayout.findViewById(R.id.order_detail_descr_num);
				TextView order_detail_descr_status = (TextView) itemLayout.findViewById(R.id.order_detail_descr_status);

				imageLoader.displayImage(gEty.getImage(), order_detail_descr_pic);
				order_detail_descr_name.setText(gEty.getName());
				order_detail_descr_price.setText("价格：" + gEty.getPrice());
				order_detail_descr_num.setText("数量：" + gEty.getNum());

				LinearLayout op_layout = (LinearLayout) itemLayout.findViewById(R.id.op_layout);
				HorizontalListView hlist = (HorizontalListView) itemLayout.findViewById(R.id.hlistview);
				OrderServiceEty serEty = gEty.getService();
				order_detail_descr_status.setText(serEty.getStatus());

				List<OrderOpEty> opList = serEty.getOplist();
				if (opList != null && opList.size() > 0) {
					op_layout.setVisibility(View.VISIBLE);
					OrderOPAdapter opAdapter = new OrderOPAdapter(self);
					hlist.setAdapter(opAdapter);
					opAdapter.setOnclickLister(opHubClickListener);
					opAdapter.setData(gEty, opList);
				} else {
					op_layout.setVisibility(View.GONE);
				}

				order_detail_goods.addView(itemLayout);
			}
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode != RESULT_OK) {
			return;
		}
		getData();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.rl_logistics:
			startActivity(new Intent(self, OrderLogisticsActivity.class).putExtra(OrderLogisticsActivity.ORDER_NO, orderno));
			break;
		case R.id.order_detail_server_call:
			String tel = (String) v.getTag();
			if (!TextUtils.isEmpty(tel)) {
				Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + tel));
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
			}
			break;
		case R.id.order_detail_serinfo:
			String businsid = (String) v.getTag();
			if (TextUtils.isEmpty(businsid)) {
				return;
			}
			startActivity(new Intent(self, StoreProActivity.class).putExtra(StoreProActivity.EXTRA_BUSINESSID, businsid));
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
			
			OrderGoodsEty ety = (OrderGoodsEty) parentEntity;
			String orderno = data.getOrderno();
			String goodsid = ety.getGoodsid();
			String tradeid = ety.getTradeid();
			String unique = ety.getUnique();

			// 售后类型(退货/换货)
			OrderServiceEty serviceEty = ety.getService();
			String type = serviceEty.getType();

			Intent intent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putString("orderno", orderno);
			bundle.putString("goodsid", goodsid);
			bundle.putString("tradeid", tradeid);
			bundle.putString("unique", unique);

			String code = tag.getCode();
			if ("OP011".equals(code)) {
				// OP011 申请售后
				intent.setClass(self, OrderServiceActivity.class);
				intent.putExtras(bundle);
				startActivityForResult(intent, 100);
			}
			if ("OP012".equals(code)) {
				// OP012 填写物流信息
				intent.setClass(self, OrderServiceBacktrackActivity.class);
				bundle.putString("type", type);
				intent.putExtras(bundle);
				startActivityForResult(intent, 101);
			}
			if ("OP013".equals(code)) {
				// OP013 取消售后
				OrderOperate.servicecancel(self, orderno, goodsid, tradeid, type, oCallBack);
			}
			if ("OP014".equals(code)) {
				// OP014 查看物流
				intent.setClass(self, OrderLogisticsActivity.class);
				bundle.putString("type", "nomal");
				intent.putExtras(bundle);
				startActivity(intent);
			}
			if ("OP015".equals(code)) {
				// OP015 延迟收货
				OrderOperate.takedelay(self, "nomal", orderno, goodsid, tradeid, unique, oCallBack);
			}
			if ("OP016".equals(code)) {
				// OP016 确认收货
				OrderOperate.showConfirmDialog(self, ety.getName(), ety.getPrice(), type, orderno, goodsid, tradeid, unique, oCallBack);
			}
			if ("OP017".equals(code)) {
				// OP017 人工服务
				OrderOperate.servicemanual(self, orderno, goodsid, tradeid, type, oCallBack);
			}
		}
	};

	/***
	 * 订单选项点击回调
	 */
	OCallBack oCallBack = new OCallBack() {
		@Override
		public void success() {
			// TODO Auto-generated method stub
			getData();
		}

		@Override
		public void failed(String msg) {
			// TODO Auto-generated method stub
		}
	};
}
