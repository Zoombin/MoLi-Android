package com.imooly.android.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.imooly.android.R;
import com.imooly.android.widget.CustomDialog;
import com.imooly.android.widget.CustomDialog.CGravity;
import com.nostra13.universalimageloader.core.ImageLoader;


/***
 * 全屏显示
 * 
 * @author lsd
 *
 */
public class FullScreenImageDialog {

	public static void show(final Context context,String imgUrl) {
		final View view = LayoutInflater.from(context).inflate(R.layout.dialog_fullscreen_image, null);
		final CustomDialog dialog = CustomDialog.create(context);
		dialog.setCanceledOnTouchOutside(true);

		ImageView img= (ImageView) view.findViewById(R.id.img);
		ImageLoader.getInstance().displayImage(imgUrl, img);

		view.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		dialog.setContentVw(view).setGravity(CGravity.CENTER).show();
	}
}
