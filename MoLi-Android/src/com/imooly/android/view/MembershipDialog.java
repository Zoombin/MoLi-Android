package com.imooly.android.view;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.ui.MemberRechargeActivity;
import com.imooly.android.ui.MemberRightActivity;
import com.imooly.android.widget.CustomDialog;
import com.imooly.android.widget.CustomDialog.CGravity;

/***
 * 
 * @author LSD
 * 
 *         成为会员
 * 
 */
public class MembershipDialog {
	public interface MembershipCallBack {
		void onLeft();

		void onRight();
	}

	public static void show(final Context context) {
		final View view = LayoutInflater.from(context).inflate(R.layout.dialog_membership, null);
		final CustomDialog dialog = CustomDialog.create(context);
		dialog.setCanceledOnTouchOutside(true);

		TextView tiltle = (TextView) view.findViewById(R.id.title);
		TextView tv1 = (TextView) view.findViewById(R.id.tv1);
		TextView tv2 = (TextView) view.findViewById(R.id.tv2);

		tv1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				context.startActivity(new Intent(context,MemberRightActivity.class));
			}
		});
		tv2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				context.startActivity(new Intent(context,MemberRechargeActivity.class));
			}
		});

		dialog.setContentVw(view).setGravity(CGravity.CENTER).show();
	}
}
