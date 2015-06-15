package com.imooly.android.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;

import com.imooly.android.R;

public class CustomDialog extends Dialog {
	private static CustomDialog customDialog = null;

	public CustomDialog(Context context) {
		super(context);
	}

	public CustomDialog(Context context, int theme) {
		super(context, theme);
	}

	public static CustomDialog create(Context context,int theme) {
		customDialog = new CustomDialog(context, theme);
		customDialog.setCanceledOnTouchOutside(true);
		return customDialog;
	}
	
	public static CustomDialog create(Context context) {
		customDialog = new CustomDialog(context, R.style.customDialog);
		customDialog.setCanceledOnTouchOutside(true);
		return customDialog;
	}

	public enum CGravity {
		TOP(), BOTTOM(), CENTER();
		private CGravity() {
		}
	}
	
	/**
	 * setGravity
	 * 
	 * @param cGraity
	 */
	public CustomDialog setGravity(CGravity cGraity) {
		switch (cGraity) {
		case TOP:
			customDialog.getWindow().getAttributes().gravity = Gravity.TOP;
			break;
		case BOTTOM:
			customDialog.getWindow().getAttributes().gravity = Gravity.BOTTOM;
			break;
		case CENTER:
			customDialog.getWindow().getAttributes().gravity = Gravity.CENTER;
			break;
		default:
			customDialog.getWindow().getAttributes().gravity = Gravity.TOP;
			break;
		}
		return customDialog;
	}

	/**
	 * 
	 * @param view
	 * @return
	 */
	public CustomDialog setContentVw(int id) {
		customDialog.setContentView(id);
		return customDialog;
	}

	public CustomDialog setContentVw(View view) {
		customDialog.setContentView(view);
		return customDialog;
	}
	
	
	/**
	 * Animation
	 * 
	 * @return
	 */
	public CustomDialog loadAnimation() {
		customDialog.getWindow().setWindowAnimations(R.style.dialogAnim);
		return customDialog;
	}
	
	public CustomDialog loadAnimation(int style) {
		customDialog.getWindow().setWindowAnimations(style);
		return customDialog;
	}
	
	float startX, startY, offsetX, offsetY;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			startX = event.getX();
			startY = event.getY();
			return false;
		case MotionEvent.ACTION_UP:
			offsetX = event.getX() - startX;
			offsetY = event.getY() - startY;
			if (Math.abs(offsetX) > Math.abs(offsetY)) {
				if (offsetX > 20) {
					dismiss();
				}
			}
			return false;
		default:
			return true;
		}
	}
}
