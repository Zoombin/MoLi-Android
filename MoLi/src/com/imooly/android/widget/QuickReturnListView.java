/*
 * Copyright 2013 Lars Werkman
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.imooly.android.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;

public class QuickReturnListView extends AutoLoadListView implements OnGlobalLayoutListener {
	private int mItemCount;
	private int mItemOffsetY[];
	private boolean scrollIsComputed = false;
	private int mHeight;

	private View mQuickReturnView;
	private View mPlaceHolder;
	private int mCachedVerticalScrollRange;
	private int mQuickReturnHeight;

	private static final int STATE_ONSCREEN = 0;
	private static final int STATE_OFFSCREEN = 1;
	private static final int STATE_RETURNING = 2;
	private int mState = STATE_ONSCREEN;
	private int mScrollY;
	private int mMinRawY = 0;
	private TranslateAnimation anim;
	
	public QuickReturnListView(Context context) {
		super(context);
		init(context);
	}

	public QuickReturnListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	/***
	 * 设置占位的View
	 * 
	 * @param mPlaceHolder
	 */
	public void setPlaceHolderView(View mPlaceHolder) {
		this.mPlaceHolder = mPlaceHolder;
	}

	/***
	 * 设置需要隐藏/显示的View
	 * 
	 * @param mQuickReturnView
	 */
	public void setQuickReturnViewView(View mQuickReturnView) {
		this.mQuickReturnView = mQuickReturnView;
	}
	

	/***
	 * 初始化
	 * 
	 * @param context
	 */
	private void init(Context context) {
		getViewTreeObserver().addOnGlobalLayoutListener(this);
	}

	private int getListHeight() {
		return mHeight;
	}

	/***
	 * 计算高度
	 */
	private void computeScrollY() {
		if (getAdapter() != null) {
			mHeight = 0;
			mItemCount = getAdapter().getCount();
			mItemOffsetY = new int[mItemCount];
			for (int i = 0; i < mItemCount; ++i) {
				View view = getAdapter().getView(i, null, this);
				view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
						MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
				mItemOffsetY[i] = mHeight;
				mHeight += view.getMeasuredHeight();
			}
			scrollIsComputed = true;
		}
	}

	public boolean scrollYIsComputed() {
		return scrollIsComputed;
	}

	/***
	 * 获取计算的高度
	 * 
	 * @return
	 */
	public int getComputedScrollY() {
		int pos, nScrollY, nItemY;
		View view = null;
		pos = getFirstVisiblePosition();
		view = getChildAt(0);
		nItemY = view.getTop();
		nScrollY = mItemOffsetY[pos] - nItemY;
		return nScrollY;
	}

	@Override
	public void onGlobalLayout() {
		if (mQuickReturnView != null) {
			mQuickReturnHeight = mQuickReturnView.getHeight();
			computeScrollY();
			mCachedVerticalScrollRange = getListHeight();
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		super.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		/**** 处理显示影藏头部的问题 ****/
		if (mPlaceHolder != null && mQuickReturnView != null) {
			mScrollY = 0;
			int translationY = 0;

			if (scrollYIsComputed()) {
				mScrollY = getComputedScrollY();
			}

			int rawY = 0;
			if (mPlaceHolder != null) {
				rawY = mPlaceHolder.getTop() - Math.min(mCachedVerticalScrollRange - getHeight(), mScrollY);
			}

			switch (mState) {
			case STATE_OFFSCREEN:
				if (rawY <= mMinRawY) {
					mMinRawY = rawY;
				} else {
					mState = STATE_RETURNING;
				}
				translationY = rawY;
				break;

			case STATE_ONSCREEN:
				if (rawY < -mQuickReturnHeight) {
					mState = STATE_OFFSCREEN;
					mMinRawY = rawY;
				}
				translationY = rawY;
				break;

			case STATE_RETURNING:
				translationY = (rawY - mMinRawY) - mQuickReturnHeight;
				if (translationY > 0) {
					translationY = 0;
					mMinRawY = rawY - mQuickReturnHeight;
				}

				if (rawY > 0) {
					mState = STATE_ONSCREEN;
					translationY = rawY;
				}

				if (translationY < -mQuickReturnHeight) {
					mState = STATE_OFFSCREEN;
					mMinRawY = rawY;
				}
				break;
			}
			
			/** this can be used if the build is below honeycomb **/
			if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.HONEYCOMB) {
				anim = new TranslateAnimation(0, 0, translationY, translationY);
				anim.setFillAfter(true);
				anim.setDuration(0);
				mQuickReturnView.startAnimation(anim);
			} else {
				mQuickReturnView.setTranslationY(translationY);
			}
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		super.onScrollStateChanged(view, scrollState);
	}
}