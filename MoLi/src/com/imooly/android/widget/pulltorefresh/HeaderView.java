package com.imooly.android.widget.pulltorefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.imooly.android.R;

/**
 * 
 * @since July 4, 2013
 * @author kzhang
 */
public class HeaderView extends LinearLayout {

	private boolean isArrowsUp = true;

	private TextView mTipsTextView;
	private TextView mLastUpdatedTextView;
	private ImageView mArrowImageView;
	private ProgressBar mProgressBar;

	public HeaderView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public HeaderView(Context context) {
		super(context);
		LayoutInflater.from(context).inflate(R.layout.listview_head, this);

		init(context);
	}

	private void init(Context context) {
		mArrowImageView = (ImageView) findViewById(R.id.head_arrowImageView);
		mArrowImageView.setMinimumWidth(70);
		mArrowImageView.setMinimumHeight(50);
		mProgressBar = (ProgressBar) findViewById(R.id.head_progressBar);
		mTipsTextView = (TextView) findViewById(R.id.head_tipsTextView);
		mLastUpdatedTextView = (TextView) findViewById(R.id.head_lastUpdatedTextView);
	}

	/**
	 * 下拉刷新
	 */
	public int setStartRefresh() {
		mArrowImageView.setVisibility(View.VISIBLE);
		mTipsTextView.setVisibility(View.VISIBLE);
		mLastUpdatedTextView.setVisibility(View.VISIBLE);
		mProgressBar.setVisibility(View.GONE);
		mTipsTextView.setText("下拉刷新");

		if (!isArrowsUp) {
			RotateAnimation mReverseFlipAnimation = new RotateAnimation(-180,
					0, RotateAnimation.RELATIVE_TO_SELF, 0.5f,
					RotateAnimation.RELATIVE_TO_SELF, 0.5f);
			mReverseFlipAnimation.setInterpolator(new LinearInterpolator());
			mReverseFlipAnimation.setDuration(250);
			mReverseFlipAnimation.setFillAfter(true);

			mArrowImageView.clearAnimation();
			mArrowImageView.setAnimation(mReverseFlipAnimation);
		}

		isArrowsUp = true;
		return PullScrollView.PULL_DOWN_STATE;
	}

	/**
	 * 松开手刷新
	 */
	public int releaseFreshing() {
		mArrowImageView.setVisibility(View.VISIBLE);
		mTipsTextView.setVisibility(View.VISIBLE);
		mLastUpdatedTextView.setVisibility(View.VISIBLE);
		mProgressBar.setVisibility(View.GONE);
		mTipsTextView.setText("松开手刷新");

		if (isArrowsUp) {
			RotateAnimation animationUp = new RotateAnimation(0, -180,
					RotateAnimation.RELATIVE_TO_SELF, 0.5f,
					RotateAnimation.RELATIVE_TO_SELF, 0.5f);
			animationUp.setInterpolator(new LinearInterpolator());
			animationUp.setDuration(250);
			animationUp.setFillAfter(true);

			mArrowImageView.clearAnimation();
			mArrowImageView.setAnimation(animationUp);
		}

		isArrowsUp = false;
		return PullScrollView.RELEASE_TO_REFRESH;
	}

	/**
	 * 正在刷新
	 */
	public int setRefreshing() {
		mArrowImageView.clearAnimation();
		mArrowImageView.setVisibility(View.GONE);
		mTipsTextView.setVisibility(View.VISIBLE);
		mLastUpdatedTextView.setVisibility(View.VISIBLE);
		mProgressBar.setVisibility(View.VISIBLE);
		mTipsTextView.setText("正在刷新...");
		return PullScrollView.REFRESHING;
	}

	/**
	 * 设置 View 高度
	 * 
	 * @param presetHeight
	 *            原始高度
	 * @param currentHeight
	 *            当前高度
	 */
	public int setPadding(int presetHeight, int currentHeight) {
		this.setPadding(0, currentHeight, 0, 0);

		// 初始化箭头状态向下
		if (currentHeight <= presetHeight) {
			return setStartRefresh();
		} else { // 改变按钮状态向上
			return releaseFreshing();
		}
	}

	/**
	 * 初始化 HeadView PaddingTop
	 */
	public void setPaddingTop(int paddingTop) {
		this.setPadding(0, paddingTop, 0, 0);
	}
}
