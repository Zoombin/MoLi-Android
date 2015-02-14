package com.imooly.android.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * 滑动关闭线性布局
 * @author daiye
 *
 */
@SuppressLint("NewApi")
public class TouchCloseLinerLayout extends LinearLayout {

	TouchCloseDialog dialog;
	
	public TouchCloseLinerLayout(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}

	public TouchCloseLinerLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public TouchCloseLinerLayout(Context context) {
		super(context);
	}

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
	
	public void setDialog(TouchCloseDialog dialog) {
		this.dialog = dialog;
	}
}
