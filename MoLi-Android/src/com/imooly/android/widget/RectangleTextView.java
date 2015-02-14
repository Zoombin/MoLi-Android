package com.imooly.android.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

public class RectangleTextView extends TextView {
	public RectangleTextView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public RectangleTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public RectangleTextView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	int strokeColor;

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// 圆角背景
		// Paint p = new Paint();
		// p.setStyle(Paint.Style.FILL);// 充满
		// p.setColor(color);
		// p.setAntiAlias(true);// 设置画笔的锯齿效果
		// RectF oval3 = new RectF(0, 0, getWidth(), getHeight());// 设置个新的长方形
		// canvas.drawRoundRect(oval3, 5, 5, p);// 第二个参数是x半径，第三个参数是y半径

		// 描边
		Paint paint = new Paint();
		paint.setColor(strokeColor);
		// 画TextView的4个边.
		canvas.drawLine(0, 0, this.getWidth() - 1, 0, paint);
		canvas.drawLine(0, 0, 0, this.getHeight() - 1, paint);
		canvas.drawLine(this.getWidth() - 1, 0, this.getWidth() - 1, this.getHeight() - 1, paint);
		canvas.drawLine(0, this.getHeight() - 1, this.getWidth() - 1, this.getHeight() - 1, paint);

	}

	public int getStrokeColor() {
		return strokeColor;
	}

	public void SetStrokeColor(int strokeColor) {
		this.strokeColor = strokeColor;
		postInvalidate();
	}
}
