package com.imooly.android.pull;

import com.imooly.android.R;
import com.imooly.android.entity.RspOrderProfile.logisticEty;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class PullUpListView extends ListView implements OnScrollListener {

	private OnLoadListener mOnLoadListener = null;
	private LayoutInflater inflater = null;
	private Context mContext;
	private View mFooterView = null;
	private boolean mIsLoading = false;

	@SuppressWarnings("unused")
	private boolean mIsFooterShowing = false;
	private TextView mTextView = null;
	private ProgressBar mProgressBar = null;
	private LinearLayout ll_footview;

	private int preCount = 0;
	
	
	private int mFirstVisibleItem = 0;
	private int mVisibleItemCount = 0;
	private int mTotalItemCount = 0;
	
	private int mPageCount = 0;
	
	@SuppressLint("NewApi")
	public PullUpListView(Context context) {
		super(context);
		mContext = context;
		initView();
//		// 去掉顶部和底部的阴影
//		if (Integer.parseInt(Build.VERSION.SDK) >= 9) {
//			this.setOverScrollMode(View.OVER_SCROLL_NEVER);
//		}

		Log.d("xxx", "PullUpListView(Context context)");
	}

	@SuppressLint("NewApi")
	public PullUpListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initView();
//		// 去掉顶部和底部的阴影
//		if (Integer.parseInt(Build.VERSION.SDK) >= 9) {
//			this.setOverScrollMode(View.OVER_SCROLL_NEVER);
//		}

		//Log.d("xxx", "PullUpListView(Context context, AttributeSet attrs)");
	}

	public PullUpListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initView();

		//Log.d("xxx", "PullUpListView(Context context, AttributeSet attrs, int defStyle)");
	}

	private void initView() {
		this.setOnScrollListener(this);
		inflater = LayoutInflater.from(mContext);
		mFooterView = inflater.inflate(R.layout.pulluplist_footer, null);
		mFooterView.setDrawingCacheEnabled(false);
		
		// foot view的最外层
		ll_footview = (LinearLayout) mFooterView.findViewById(R.id.ll_footview);
		mProgressBar = (ProgressBar) mFooterView.findViewById(R.id.loading);
		mTextView = (TextView) mFooterView.findViewById(R.id.text_loading);
		
		// 加入foot view但是吧可见
		//this.addFooterView(mFooterView);
		this.addFooterView(mFooterView, null, false);  // 为了不可点击
		// 去掉foot view下面的那条横线
		this.setFooterDividersEnabled(false);
		
		//设置背景透明
		setCacheColorHint(mContext.getResources().getColor(android.R.color.transparent));
		
		// set foot view state
		this.setFooterViewState(0);
		
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

		Log.d("xxx", "mScrollState = ");
		
		mFirstVisibleItem = firstVisibleItem;
		mVisibleItemCount = visibleItemCount;
		mTotalItemCount = totalItemCount;
		
//		Log.d("xxx", "firstVisibleItem = " + firstVisibleItem);
//		Log.d("xxx", "visibleItemCount = " + visibleItemCount);
//		Log.d("xxx", "totalItemCount = " + totalItemCount);
		
		
	}

	/**
	 * @param state
	 */
	public void setFooterViewState(int state) {

		switch (state) {
		case 0:
			mProgressBar.setVisibility(View.GONE);
			mTextView.setText("已加载全部");
			break;
		case 1:
			mProgressBar.setVisibility(View.VISIBLE);
			mTextView.setText("正在加载");
		default:
			break;
		}

	}

	// ListView列表加载完成，重置标记为,设置foot view的状态
	public void onLoadCompleted() {
		
		mPageCount++;
		
		// 第一次加载完成后，设置footview可以显示
		ll_footview.setVisibility(View.VISIBLE); 
		
		if (preCount == this.getCount() || mPageCount == 1 ) {
			
			setFooterViewState(0);
			
		}else {
			//ll_footview.setVisibility(View.INVISIBLE);
			setFooterViewState(1);
		}
		
		preCount = this.getCount();

		mIsLoading = false;
		
		
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		
		// 加载的条件
		// mIsLoading == false 防止反复加载
		if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && mFirstVisibleItem + mVisibleItemCount == mTotalItemCount && mIsLoading == false) {
			
			//ll_footview.setVisibility(View.VISIBLE);
			onLoad();
			mIsLoading = true;
		}
		
		if (AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL == scrollState) {
			setFooterViewState(1);
		}

	}

	// 回调
	public void onLoad() {
		if (mOnLoadListener != null) {
			mOnLoadListener.onLoad();
		}
	}

	// 加载更多监听
	public void setOnLoadListener(OnLoadListener onLoadListener) {
		this.mOnLoadListener = onLoadListener;
	}

	/*
	 * 定义加载更多接口
	 */
	public interface OnLoadListener {
		public void onLoad();
	}

}
