package com.imooly.android.widget;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;

public class TouchCloseDialog extends Dialog {

	public TouchCloseDialog(Context context, int theme) {
		super(context, theme);
	}

	protected TouchCloseDialog(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}

	public TouchCloseDialog(Context context) {
		super(context);
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
