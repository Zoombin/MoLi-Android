package com.imooly.android.ui;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.adapter.OrderServiceDetailHListAdapter;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.entity.RspSgoodsProfile;
import com.imooly.android.entity.RspSgoodsProfile.SgoodsAddressEty;
import com.imooly.android.entity.RspSgoodsProfile.SgoodsGodsEty;
import com.imooly.android.entity.RspSgoodsProfile.SgoodsServiceEty;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.widget.HorizontalListView;
import com.imooly.android.widget.Toast;

/***
 * 售后详情
 * 
 * @author lsd
 * 
 */
public class OrderServiceDetail extends BaseActivity implements OnClickListener {
	public static String ORDER_NO = "order_no";
	public static String GOOD_ID = "goods_id";
	public static String UNIQUE = "unique";

	private RelativeLayout ll_title;
	private ImageView iv_back;
	private TextView tv_title;

	LinearLayout ll_level1;
	TextView tv_goods_name;
	TextView tv_type;
	TextView tv_status;
	TextView tv_time;
	LinearLayout ll_payback;
	TextView tv_payback;

	LinearLayout ll_level2;
	TextView tv_reason;
	LinearLayout ll_mark;
	TextView tv_mark;
	HorizontalListView order_server_hlistview;

	LinearLayout ll_level3;
	TextView address_content;
	TextView zipcode;
	TextView name;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_sevice_detail);
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

		ll_level1 = (LinearLayout) findViewById(R.id.ll_level1);
		tv_goods_name = (TextView) findViewById(R.id.tv_goods_name);
		tv_type = (TextView) findViewById(R.id.tv_type);
		tv_status = (TextView) findViewById(R.id.tv_status);
		tv_time = (TextView) findViewById(R.id.tv_time);
		ll_payback = (LinearLayout) findViewById(R.id.ll_payback);
		ll_payback.setVisibility(View.GONE);
		tv_payback = (TextView) findViewById(R.id.tv_payback);

		ll_level2 = (LinearLayout) findViewById(R.id.ll_level2);
		tv_reason = (TextView) findViewById(R.id.tv_reason);
		ll_mark = (LinearLayout) findViewById(R.id.ll_mark);
		tv_mark = (TextView) findViewById(R.id.tv_mark);
		order_server_hlistview = (HorizontalListView) findViewById(R.id.order_server_hlistview);

		ll_level3 = (LinearLayout) findViewById(R.id.ll_level3);
		ll_level3.setVisibility(View.GONE);
		address_content = (TextView) findViewById(R.id.address_content);
		zipcode = (TextView) findViewById(R.id.zipcode);
		name = (TextView) findViewById(R.id.name);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		default:
			break;
		}
	}

	private void getData() {
		String orderno="";
		String goodsid = "";
		String unique = "";

		Intent intent = getIntent();
		if (intent.hasExtra(ORDER_NO)) {
			orderno = intent.getStringExtra(ORDER_NO);
		}
		if (intent.hasExtra(GOOD_ID)) {
			goodsid = intent.getStringExtra(GOOD_ID);
		}
		if (intent.hasExtra(UNIQUE)) {
			unique = intent.getStringExtra(UNIQUE);
		}
		Api.sgoodsprofile(self, orderno,goodsid, unique, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				// TODO Auto-generated method stub
				RspSgoodsProfile rsp = (RspSgoodsProfile) rspData;
				if (rsp.data != null) {
					RspSgoodsProfile.Data data = rsp.data;

					SgoodsGodsEty goods = data.getGoods();
					if (goods != null) {
						tv_goods_name.setText(goods.getName());
					}

					// //
					SgoodsServiceEty service = data.getService();
					if (service != null) {
						String type = service.getType();
						if ("return".equals(type)) {
							tv_title.setText("售后详情  退货");
						} else {
							tv_title.setText("售后详情  换货");
						}
						tv_type.setText(service.getTypename());
						tv_status.setText(service.getStatusname());
						tv_time.setText(service.getCreatetime());

						String refundamount = service.getRefundamount();
						if (TextUtils.isEmpty(refundamount)) {
							ll_payback.setVisibility(View.GONE);
						} else {
							ll_payback.setVisibility(View.VISIBLE);
							tv_payback.setText(service.getRefundamount());
						}

						tv_reason.setText(service.getUremark());

						String bremark = service.getBremark();
						if (TextUtils.isEmpty(bremark)) {
							ll_mark.setVisibility(View.GONE);
						} else {
							ll_mark.setVisibility(View.VISIBLE);
							tv_mark.setText(bremark);
						}

						List<String> imgs = service.getImages();
						if (imgs != null && imgs.size() > 0) {
							OrderServiceDetailHListAdapter adapter = new OrderServiceDetailHListAdapter();
							order_server_hlistview.setAdapter(adapter);
							adapter.setData(imgs);
						}
					}

					// //
					SgoodsAddressEty address = data.getAddress();
					if (address == null) {
						ll_level3.setVisibility(View.GONE);
					} else {
						ll_level3.setVisibility(View.VISIBLE);
						address_content.setText(address.getAddress());
						zipcode.setText(address.getPostcode());
						name.setText(address.getName() + "     " + address.getTel());
					}
				}
			}

			@Override
			public void failed(String msg) {
				// TODO Auto-generated method stub
				Toast.show(self, msg);
			}
		}, RspSgoodsProfile.class);
	}
}
