package com.imooly.android.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.Button;

import com.imooly.android.R;

/***
 * 页码显示listview
 * 
 * @author lsd
 * 
 */
public class PageListView extends AutoLoadListView {
	Button btnPage, btnback;

	int totalpage, pagesize;
	boolean isGrid = false;

	Animation alpshow;
	Animation alpgone;

	float startY = 0;
	boolean move_action = false;
	boolean up_action = true;
	
	private OnViewScrollListener mScrollListener;

	public PageListView(Context pContext, AttributeSet pAttrs) {
		super(pContext, pAttrs);
		init(pContext);
	}

	public PageListView(Context pContext) {
		super(pContext);
		init(pContext);
	}

	void init(Context context) {
		alpshow = AnimationUtils.loadAnimation(context, R.anim.alphashow);
		alpgone = AnimationUtils.loadAnimation(context, R.anim.alphagone);
	}
	
	/**
	 * 自定义的滚动监听
	 * 
	 */
	public interface OnViewScrollListener {
		public void onScrollUp();

		public void onScrollDown();
	}
	
	
	public void setOnViewScrollListener(OnViewScrollListener mScrollListener){
		this.mScrollListener = mScrollListener;
	}
	
	/**
	 * 回到顶部/页码
	 * 
	 * @param btnPage
	 * @param btnback
	 */
	public void setButton(Button btnPage, Button btnback) {
		this.btnPage = btnPage;
		this.btnback = btnback;
	}

	/**
	 * 显示的页码
	 * 
	 * @param totalpage
	 * @param pagesize
	 */
	public void setNum(int totalpage, int pagesize) {
		this.totalpage = totalpage;
		this.pagesize = pagesize;
	}

	/***
	 * 设置网格模式
	 * 
	 * @param isGrid
	 */
	public void setGridType(boolean isGrid) {
		this.isGrid = isGrid;
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		int mLastItemIndex = firstVisibleItem + visibleItemCount;
		// 设置页码
		int curPage = 0;
		if (pagesize != 0) {
			int curItem = mLastItemIndex;
			if (isGrid) {
				curItem = mLastItemIndex * 2;
			}
			curPage = curItem / pagesize + 1;
			if (curPage > totalpage) {
				curPage = totalpage;
			}
			if (btnPage != null) {
				btnPage.setText(curPage + " / " + totalpage);
			}
		}

		// 设置回到顶部按钮
		if (btnback != null) {
			btnback.setVisibility(View.GONE);
			if (curPage > 1) {
				btnback.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		super.onScrollStateChanged(view, scrollState);
		switch (scrollState) {
		// 滚动
		case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
			if (btnPage != null) {
				btnPage.startAnimation(alpshow);
				btnPage.setVisibility(View.VISIBLE);
			}
			break;
		case OnScrollListener.SCROLL_STATE_IDLE:
			if (btnPage != null) {
				btnPage.startAnimation(alpgone);
				btnPage.setVisibility(View.GONE);
			}
			break;
		}
	}
	
	
	@Override
	public boolean onInterceptTouchEvent(final MotionEvent ev) {
		// TODO Auto-generated method stub
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			//Log.i("d", "ACTION_DOWN,  StartY = "+ev.getY());
			move_action = false;
			up_action = false;
			startY = ev.getY();
			break;
		}
		return super.onInterceptTouchEvent(ev);
	}

	/**
	 * 原作者的，我没改动，请读者自行优化。
	 */
	public boolean onTouchEvent(final MotionEvent event) {
		/***这处理隐藏头部的问题（只有需要处理的时候才操作）*/
		if (mScrollListener != null)
			switch (event.getAction()) {
			case MotionEvent.ACTION_MOVE:
				if (!move_action) {
					// Log.i("d", "ACTION_MOVE");
					move_action = true;
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							if (move_action == false) {
								// up的时候处理了
								return;
							}
							float curY = event.getY();
						    //Log.i("LSD", "curY = "+curY);
							if (curY < startY+2) {
								mScrollListener.onScrollUp();
							} else if (curY > startY+2){
								mScrollListener.onScrollDown();
							}
							startY = curY;
							move_action = false;
							up_action = true;
						}
					}, 60);
				}
				break;
			case MotionEvent.ACTION_UP:
				if (!up_action) {
					//move的时候没处理
					move_action = false;
					float curY = event.getY();
					// Log.i("d", "ACTION_UP,  curY = "+curY);
					if (curY < startY+2) {
						mScrollListener.onScrollUp();
					} else if (curY > startY+2){
						mScrollListener.onScrollDown();
					}
				}
				break;
		}

		return super.onTouchEvent(event);
	}

}
