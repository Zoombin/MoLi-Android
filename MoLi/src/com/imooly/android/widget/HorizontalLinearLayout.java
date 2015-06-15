package com.imooly.android.widget;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.Interface.OpHubClickListener;
import com.imooly.android.entity.RspOrderList.OrderOpEty;
import com.imooly.android.tool.Config;

public class HorizontalLinearLayout extends LinearLayout {
	Context context;
	OpHubClickListener listener;
	List<OrderOpEty> list;
	Object parentEntity;

	public HorizontalLinearLayout(Context context) {
		this(context, null);
	}

	public HorizontalLinearLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		this.context = context;
	}

	public void setOnclickLister(OpHubClickListener listener) {
		this.listener = listener;
	}

	public void setData(Object parentEntity, List<OrderOpEty> list) {
		this.list = list;
		this.parentEntity = parentEntity;

		removeAllViews();

		for (int i = 0; i < list.size(); i++) {
			OrderOpEty ety = list.get(i);
			View view = getView(context, ety);
			addView(view);
		}
	}

	 
	private View getView(Context context, final OrderOpEty ety) {
		View view = LayoutInflater.from(context).inflate(R.layout.layout_order_op_text, null);
		TextView tv = (TextView) view.findViewById(R.id.tv);
		if (ety != null) {
			tv.setTag(ety);
			tv.setText(ety.getName());

			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
				// 4.0以上可以用
				tv.setTextColor(Color.parseColor("#" + ety.getFontcolor()));
				
				GradientDrawable drawable = new GradientDrawable();
				drawable.setCornerRadius(Config.getCaleValue(5.0f));
				drawable.setColor(Color.parseColor("#" + ety.getBgcolor()));
				drawable.setStroke(1, Color.parseColor("#" + ety.getBordercolor()));
				tv.setBackground(drawable);
			} else {
				String code = ety.getCode();
				if ("OP001".equals(code) || "OP002".equals(code) || "OP005".equals(code) || "OP008".equals(code) || "OP009".equals(code)
						|| "OP012".equals(code) || "OP014".equals(code) || "OP015".equals(code) || "OP017".equals(code)) {
					tv.setTextColor(Color.parseColor("#666666"));
					tv.setBackgroundResource(R.drawable.order_item_corner_bg_v1);//白色
				}
				if ("OP003".equals(code) || "OP004".equals(code) || "OP006".equals(code) || "OP007".equals(code) || "OP011".equals(code)
						|| "OP013".equals(code) || "OP016".equals(code)) {
					tv.setTextColor(Color.parseColor("#FFFFFF"));
					tv.setBackgroundResource(R.drawable.order_item_corner_bg_v2);//橘色
				}
			}

			tv.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					// TODO Auto-generated method stub
					if (listener != null) {
						listener.onClick(parentEntity, ety);
					}
				}
			});
		}
		return view;
	}

}
