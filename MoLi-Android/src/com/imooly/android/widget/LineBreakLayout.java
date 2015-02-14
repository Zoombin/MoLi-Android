package com.imooly.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

public class LineBreakLayout extends ViewGroup {
	Context context;
	private final static int VIEW_MARGIN = 30;
	private int wrap_width = 0;
	private int wrap_hight = 20;

	public LineBreakLayout(Context context) {
		this(context, null);
	}

	public LineBreakLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public LineBreakLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		this.context = context;
		getScreenWidth();
	}

	private void getScreenWidth() {
		wrap_width = getResources().getDisplayMetrics().widthPixels;
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int row = 1;
		int height = 0;
		int lengthX = 0;

		Log.e("view count", "" + getChildCount());

		for (int index = 0; index < getChildCount(); index++) {

			final View child = getChildAt(index);
			child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);

			int width = child.getMeasuredWidth();
			if (width > this.getWidth()) {
				width = this.getWidth() - convertDipOrPx(15);
			}
			height = child.getMeasuredHeight();

			lengthX += width + VIEW_MARGIN;

			if (lengthX > this.getWidth()) {
				lengthX = width + VIEW_MARGIN;
				row++;
			}
		}

		int wrap_hight = row * (height + VIEW_MARGIN) + VIEW_MARGIN;

		setMeasuredDimension(resolveSize(wrap_width, widthMeasureSpec), resolveSize(wrap_hight, heightMeasureSpec));

	}

	public int convertDipOrPx(int dip) {
		float scale = getContext().getResources().getDisplayMetrics().density;
		return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));
	}

	@Override
	protected void onLayout(boolean arg0, int arg1, int arg2, int arg3, int arg4) {
		final int count = getChildCount();
		int row = 0;
		int height = 0;
		int lengthX = arg1;
		int lengthY = arg2;

		for (int i = 0; i < count; i++) {
			final View child = this.getChildAt(i);
			int width = child.getMeasuredWidth();
			if (width > this.getWidth()) {
				width = this.getWidth() - convertDipOrPx(15);
			}
			height = child.getMeasuredHeight();
			lengthX += width + VIEW_MARGIN;
			lengthY = row * (height + VIEW_MARGIN) + VIEW_MARGIN + height + arg2;
			// if it can't drawing on a same line , skip to next line
			if (lengthX > arg3) {
				lengthX = width + VIEW_MARGIN + arg1;
				row++;
				lengthY = row * (height + VIEW_MARGIN) + VIEW_MARGIN + height + arg2;
			}
			child.layout(lengthX - width, lengthY - height, lengthX, lengthY);
		}
		wrap_hight = row * (height + VIEW_MARGIN) + VIEW_MARGIN + height + arg2;
	}
}
