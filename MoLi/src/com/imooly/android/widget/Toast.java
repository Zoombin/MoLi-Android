package com.imooly.android.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.imooly.android.R;

public class Toast {
	private static long lastShowTime = 0;
	
	public static void show(Context context, String msg) {
		if (TextUtils.isEmpty(msg)) {
			return;
		}
		long time = System.currentTimeMillis();
		long timeD = time - lastShowTime;
		if (0 < timeD && timeD < 3000) {
			return;
		}
		lastShowTime = time;
		
		TextView txt = new TextView(context);
		txt.setLayoutParams(new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
		txt.setCompoundDrawablePadding((int) context.getResources().getDimension(R.dimen.dp_5));
		Drawable drawable = context.getResources().getDrawable(R.drawable.ic_toast);
		drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
		txt.setCompoundDrawables(drawable, null, null, null);
		txt.setText(msg);

		// txt.setTextColor(0xf0666666);
		txt.setTextColor(context.getResources().getColor(R.color.app_text_black));
		txt.setBackgroundResource(R.drawable.ic_toast_bg);
		float paddingLeft = context.getResources().getDimension(R.dimen.dp_12);
		float paddingTop = context.getResources().getDimension(R.dimen.dp_10);

		txt.setPadding((int) paddingLeft, (int) paddingTop, (int) paddingLeft, (int) paddingTop);

		android.widget.Toast toast = new android.widget.Toast(context);
		toast.setView(txt);
		toast.setDuration(android.widget.Toast.LENGTH_SHORT);
		// toast.setGravity(Gravity.CENTER, 0, 0);
		toast.show();
	}
}
