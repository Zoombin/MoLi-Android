package com.imooly.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

public class ObservableScrollView extends ScrollView {
	OnScrollListener onScrollViewListener;

	public interface OnScrollListener {

		void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy);

	}

	public ObservableScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public ObservableScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public void setScrollListener(OnScrollListener onScrollViewListener) {
		this.onScrollViewListener = onScrollViewListener;
	}

	@Override
	protected void onScrollChanged(int x, int y, int oldx, int oldy) {
		super.onScrollChanged(x, y, oldx, oldy);
		if (onScrollViewListener != null) {
			onScrollViewListener.onScrollChanged(this, x, y, oldx, oldy);
		}
	}

}
