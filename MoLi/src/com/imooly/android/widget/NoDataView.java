package com.imooly.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.imooly.android.R;

/**
 * 无数据遮罩层
 * 
 * @author daiye
 * 
 */
public class NoDataView extends RelativeLayout implements OnClickListener {
	private OnClickListener listener;

	private LinearLayout layout_noorder;
	private LinearLayout layout_nocollect;
	private LinearLayout layout_notuihuo;
	private LinearLayout layout_noshopcart;
	private LinearLayout layout_nohuanhuo;
	private LinearLayout layout_nodjq;
	private LinearLayout layout_nologin;
	private LinearLayout layout_noaddress;
	private LinearLayout layout_nomsg;
	private LinearLayout layout_nofootstep;
	private LinearLayout layout_nosearch;
	private LinearLayout layout_network;
	private View v;

	public NoDataView(Context context) {
		super(context);
		init(context);
	}

	public NoDataView(Context context, AttributeSet attributeset) {
		super(context, attributeset);
		init(context);
	}

	private void init(Context context) {
		v = View.inflate(context, R.layout.layout_nodata, this);

		// 界面
		layout_noorder = (LinearLayout) v.findViewById(R.id.layout_noorder);
		layout_nocollect = (LinearLayout) v.findViewById(R.id.layout_nocollect);
		layout_notuihuo = (LinearLayout) v.findViewById(R.id.layout_notuihuo);
		layout_noshopcart = (LinearLayout) v.findViewById(R.id.layout_noshopcart);
		layout_nohuanhuo = (LinearLayout) v.findViewById(R.id.layout_nohuanhuo);
		layout_nodjq = (LinearLayout) v.findViewById(R.id.layout_nodjq);
		layout_nologin = (LinearLayout) v.findViewById(R.id.layout_nologin);
		layout_noaddress = (LinearLayout) v.findViewById(R.id.layout_noaddress);
		layout_nomsg = (LinearLayout) v.findViewById(R.id.layout_nomsg);
		layout_nofootstep = (LinearLayout) v.findViewById(R.id.layout_nofootstep);
		layout_nosearch = (LinearLayout) v.findViewById(R.id.layout_nosearch);
		layout_network = (LinearLayout) v.findViewById(R.id.layout_network);

		v.findViewById(R.id.bt_load_car).setOnClickListener(this);
		v.findViewById(R.id.bt_login).setOnClickListener(this);
		v.findViewById(R.id.bt_add_address).setOnClickListener(this);
		v.findViewById(R.id.bt_reload).setOnClickListener(this);
	}

	public static final int defult = 0;
	public static final int noorder = 1;
	public static final int nocollect = 2;
	public static final int notuihuo = 3;
	public static final int noshopcart = 4;
	public static final int nohuanhuo = 5;
	public static final int nodjq = 6;
	public static final int nologin = 7;
	public static final int noaddress = 8;
	public static final int nomsg = 9;
	public static final int nofootstep = 10;
	public static final int nosearch = 11;
	public static final int nonet = 12;

	public void postHandle(int state) {
		reset();
		v.setVisibility(View.VISIBLE);
		switch (state) {
		case noorder:
			layout_noorder.setVisibility(View.VISIBLE);
			break;
		case nocollect:
			layout_nocollect.setVisibility(View.VISIBLE);
			break;
		case notuihuo:
			layout_notuihuo.setVisibility(View.VISIBLE);
			break;
		case noshopcart:
			layout_noshopcart.setVisibility(View.VISIBLE);
			break;
		case nohuanhuo:
			layout_nohuanhuo.setVisibility(View.VISIBLE);
			break;
		case nodjq:
			layout_nodjq.setVisibility(View.VISIBLE);
			break;
		case nologin:
			layout_nologin.setVisibility(View.VISIBLE);
			break;
		case noaddress:
			layout_noaddress.setVisibility(View.VISIBLE);
			break;
		case nomsg:
			layout_nomsg.setVisibility(View.VISIBLE);
			break;
		case nofootstep:
			layout_nofootstep.setVisibility(View.VISIBLE);
			break;
		case nosearch:
			layout_nosearch.setVisibility(View.VISIBLE);
			break;
		case nonet:
			layout_network.setVisibility(View.VISIBLE);
			break;
		case defult:
			v.setVisibility(View.GONE);
			break;
		default:
			v.setVisibility(View.GONE);
			break;
		}
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		if (this.listener != null) {
			this.listener.onClick(view);
		}
	}

	public void setOnClickListener(OnClickListener listener) {
		this.listener = listener;
	}

	public void reset() {
		layout_noorder.setVisibility(View.GONE);
		layout_nocollect.setVisibility(View.GONE);
		layout_notuihuo.setVisibility(View.GONE);
		layout_noshopcart.setVisibility(View.GONE);
		layout_nohuanhuo.setVisibility(View.GONE);
		layout_nodjq.setVisibility(View.GONE);
		layout_nologin.setVisibility(View.GONE);
		layout_noaddress.setVisibility(View.GONE);
		layout_nomsg.setVisibility(View.GONE);
		layout_nofootstep.setVisibility(View.GONE);
		layout_nosearch.setVisibility(View.GONE);
		layout_network.setVisibility(View.GONE);
	}
}
