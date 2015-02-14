package com.imooly.android.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;

import com.imooly.android.R;
import com.imooly.android.widget.CustomDialog;
import com.imooly.android.widget.CustomDialog.CGravity;

public class RoutePlanPickStartPoint {
	 public interface PickCallback{
		 void onLocation();
		 void onPick();
	 }

	public static void showPickDialog(Context context,final PickCallback callback) {
		final View view = LayoutInflater.from(context).inflate(R.layout.layout_route_plan_pick_start, null);
		final CustomDialog dialog = CustomDialog.create(context);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentVw(view).setGravity(CGravity.CENTER).show();

		view.findViewById(R.id.close).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		view.findViewById(R.id.my_location).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if(callback != null){
					callback.onLocation();
				}
			}
		});

		view.findViewById(R.id.pick_form_map).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if(callback != null){
					callback.onPick();
				}
			}
		});
	}
}
