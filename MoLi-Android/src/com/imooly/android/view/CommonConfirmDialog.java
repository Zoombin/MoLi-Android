package com.imooly.android.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.widget.CustomDialog;
import com.imooly.android.widget.CustomDialog.CGravity;

/***
 * 
 * @author LSD
 * 
 *         通用的提醒
 * 
 */
public class CommonConfirmDialog {
	public interface CommonAlertCallBack {
		void onCancel();

		void onConfirm();
	}

	public static void show(final Context context, String content, String btnoText,String btyesText,final CommonAlertCallBack callback) {
		final View view = LayoutInflater.from(context).inflate(R.layout.dialog_confirm_common, null);
		final CustomDialog dialog = CustomDialog.create(context);
		dialog.setCanceledOnTouchOutside(true);

		TextView title = (TextView) view.findViewById(R.id.title);
		TextView no = (TextView) view.findViewById(R.id.no);
		TextView yes = (TextView) view.findViewById(R.id.yes);

		if (!TextUtils.isEmpty(content)) {
			title.setText(content);
		}
		if (!TextUtils.isEmpty(btnoText)) {
			no.setText(btnoText);
		}
		if (!TextUtils.isEmpty(btyesText)) {
			yes.setText(btyesText);
		}

		no.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if (callback != null) {
					callback.onCancel();
				}
			}
		});
		yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if (callback != null) {
					callback.onConfirm();
				}
			}
		});

		dialog.setContentVw(view).setGravity(CGravity.CENTER).show();
	}
}
