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
import com.imooly.android.widget.Toast;

/***
 * 
 * @author LSD
 * 
 *         支付
 * 
 */
public class PayConfirmDialog {
	public interface PayConfirmCallBack {
		void onLeft();

		void onRight(Object obj);
	}

	public static void showConfirmDialog(final Context context, String title, String content, final PayConfirmCallBack callBack) {
		final View view = LayoutInflater.from(context).inflate(R.layout.view_comm_dialog, null);
		final CustomDialog dialog = CustomDialog.create(context);
		dialog.setCanceledOnTouchOutside(false);
		dialog.setContentVw(view).setGravity(CGravity.CENTER).show();

		((TextView) view.findViewById(R.id.title)).setText(title);
		((TextView) view.findViewById(R.id.content)).setText(content);

		final TextView pass = (TextView) view.findViewById(R.id.pass);

		view.findViewById(R.id.yes).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String passStr = pass.getText().toString();
				if (TextUtils.isEmpty(passStr)) {
					Toast.show(context, "请输入密码");
					return;
				}
				dialog.dismiss();
				if (callBack != null) {
					callBack.onRight(passStr);
				}
			}
		});
		view.findViewById(R.id.no).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if (callBack != null) {
					callBack.onLeft();
				}
			}
		});
	}

}
