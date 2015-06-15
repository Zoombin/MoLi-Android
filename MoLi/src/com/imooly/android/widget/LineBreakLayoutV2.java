package com.imooly.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class LineBreakLayoutV2 extends RelativeLayout {
	private int dividerHeight = 30;
	private int dividerWidth = 30;

	public LineBreakLayoutV2(Context context) {
		this(context, null);
	}

	public LineBreakLayoutV2(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LineBreakLayoutV2(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	int getDividerHeight() {
		return this.dividerHeight;
	}

	public void setDividerHeight(int dividerHeight) {
		this.dividerHeight = dividerHeight;
	}

	int getDividerWidth() {
		return this.dividerWidth;
	}

	public void setDividerWidth(int dividerWidth) {
		this.dividerWidth = dividerWidth;
	}

	protected void onLayout(boolean arg0, int argLeft, int argTop, int argRight, int argBottom) {
		int count = getChildCount();
		int row = 0;
		int lengthX = 0;
		int lengthY = 0;
		for (int i = 0; i < count; ++i) {
			View child = getChildAt(i);
			int width = child.getMeasuredWidth();
			int height = child.getMeasuredHeight();

			if (lengthX == 0)
				lengthX += width;
			else {
				lengthX += width + getDividerWidth();
			}

			if ((i == 0) && (lengthX <= argRight)) {
				lengthY += height;
			}

			if (lengthX > argRight) {
				lengthX = width;
				lengthY += getDividerHeight() + height;
				++row;
				child.layout(lengthX - width, lengthY - height, lengthX, lengthY);
			} else {
				child.layout(lengthX - width, lengthY - height, lengthX, lengthY);
			}
		}
		ViewGroup.LayoutParams lp = getLayoutParams();
		lp.height = lengthY;
		setLayoutParams(lp);
	}

	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = View.MeasureSpec.getSize(widthMeasureSpec);
		int height = View.MeasureSpec.getSize(heightMeasureSpec);
		setMeasuredDimension(width, height);

		for (int i = 0; i < getChildCount(); ++i) {
			View child = getChildAt(i);
			child.measure(0, 0);
		}
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

}
