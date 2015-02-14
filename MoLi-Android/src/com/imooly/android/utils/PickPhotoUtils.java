package com.imooly.android.utils;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.widget.CustomDialog;
import com.imooly.android.widget.CustomDialog.CGravity;

public class PickPhotoUtils {
	 public interface PickCallback{
		 void onPhoto();
		 void onCamera();
	 }

	public static void showPickDialog(Context context,String title,final PickCallback callback) {
		final View view = LayoutInflater.from(context).inflate(R.layout.dialog_view_pick_photo, null);
		final CustomDialog dialog = CustomDialog.create(context);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentVw(view).setGravity(CGravity.CENTER).show();

		if(TextUtils.isEmpty(title)){
			title = "选取图片";
		}
		((TextView) view.findViewById(R.id.title)).setText(title);

		view.findViewById(R.id.close).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		view.findViewById(R.id.pick_photo).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if(callback != null){
					callback.onPhoto();
				}
			}
		});

		view.findViewById(R.id.pick_camera).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if(callback != null){
					callback.onCamera();
				}
			}
		});
	}
}
