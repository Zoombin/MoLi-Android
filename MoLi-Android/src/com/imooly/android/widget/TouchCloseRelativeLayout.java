package com.imooly.android.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

/**
 * 滑动关闭相对布局
 * @author daiye
 *
 */
@SuppressLint("NewApi")
public class TouchCloseRelativeLayout extends RelativeLayout {

	public TouchCloseRelativeLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public TouchCloseRelativeLayout(Context context) {
		super(context);

	}

	public TouchCloseRelativeLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);

	}

	CustomDialog dialog;

	float startX, startY, offsetX, offsetY;

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			startX = event.getX();
			startY = event.getY();
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			offsetX = event.getX() - startX;
			offsetY = event.getY() - startY;
			if (Math.abs(offsetX) > Math.abs(offsetY)) {
				if (offsetX > 20) {
					dialog.dismiss();
				}
			}
		}
		return super.dispatchTouchEvent(event);
	}

	public void setDialog(CustomDialog dialog) {
		this.dialog = dialog;
	}
}
