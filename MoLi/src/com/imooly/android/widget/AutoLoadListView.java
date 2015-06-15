package com.imooly.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.imooly.android.R;

/**
 * ListView 自动加载更多
 * 
 * @author lsd
 * 
 */
public class AutoLoadListView extends ListView implements OnScrollListener {
	/** 加载中 */
	private final static int ENDINT_LOADING = 1;
	/** 自动完成刷新 */
	private final static int ENDINT_AUTO_LOAD_DONE = 2;
	/** 自动加载的状态 */
	private int mEndState;
	/** 可以加载更多？ */
	private boolean mCanLoadMore = false;
	/**是否已经滚动到了底部*/
	private boolean mCanScrollDown = true;
	
	
	private LayoutInflater mInflater;
	private View mEndRootView;
	private ProgressBar mEndLoadProgressBar;
	private TextView mEndLoadTipsTextView;
	private ImageView mEndLoadTipsImagView;

	private boolean noMoreData;
	private OnLoadMoreListener mLoadMoreListener;

	public AutoLoadListView(Context pContext, AttributeSet pAttrs) {
		super(pContext, pAttrs);
		init(pContext);
	}

	public AutoLoadListView(Context pContext) {
		super(pContext);
		init(pContext);
	}

	public AutoLoadListView(Context pContext, AttributeSet pAttrs, int pDefStyle) {
		super(pContext, pAttrs, pDefStyle);
		init(pContext);
	}
	

	/**
	 * 初始化操作
	 */
	private void init(Context pContext) {
		mInflater = LayoutInflater.from(pContext);
		setOnScrollListener(this);
		
		addFooterView();
	}

	/**
	 * 添加加载更多FootView
	 */
	private void addFooterView() {
		mEndRootView = mInflater.inflate(R.layout.view_autoload_footer, null);
		this.addFooterView(mEndRootView);
		
		mEndLoadProgressBar = (ProgressBar) mEndRootView.findViewById(R.id.load_more_progress);
		mEndLoadTipsTextView = (TextView) mEndRootView.findViewById(R.id.load_more_tip);
		mEndLoadTipsImagView = (ImageView) mEndRootView.findViewById(R.id.load_more_img);
		mEndRootView.setVisibility(View.INVISIBLE);// 默认不显示（加载中 才显示）
	}


	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if((firstVisibleItem+visibleItemCount) == totalItemCount){
			mCanScrollDown = false;
		}else{
			mCanScrollDown = true;
		}
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (mCanLoadMore) {// 存在加载更多功能
			if (!mCanScrollDown && scrollState == SCROLL_STATE_IDLE) {
				// SCROLL_STATE_IDLE=0，滑动停止
				if (mEndState != ENDINT_LOADING) {
					// FootView显示 : 更 多 ---> 加载中...
					mEndState = ENDINT_LOADING;
					changeEndViewByState();
					if(noMoreData){
						setNoMoreData(true);
						return;
					}
					onLoadMore();
				}
			}
		}
	}

	/**
	 * 改变加载更多状态
	 */
	private void changeEndViewByState() {
		if (mCanLoadMore) {
			// 允许加载更多
			switch (mEndState) {
			case ENDINT_LOADING:// 刷新中
				mEndRootView.setVisibility(View.VISIBLE);
				mEndLoadTipsTextView.setText(R.string.p2refresh_doing_end_refresh);
				mEndLoadTipsTextView.setVisibility(View.VISIBLE);
				mEndLoadProgressBar.setVisibility(View.VISIBLE);
				mEndLoadTipsImagView.setVisibility(View.INVISIBLE);
				break;
			case ENDINT_AUTO_LOAD_DONE:// 自动刷新完成
				mEndLoadTipsTextView.setText(R.string.p2refresh_end_load_more);
				mEndLoadTipsTextView.setVisibility(View.VISIBLE);
				mEndLoadProgressBar.setVisibility(View.GONE);

				mEndRootView.setVisibility(View.INVISIBLE);

				//没有更多数据了
				if (noMoreData) {
					mEndRootView.setVisibility(View.VISIBLE);
					mEndLoadTipsImagView.setVisibility(View.VISIBLE);
					mEndLoadTipsTextView.setText(R.string.p2refresh_end_no_more_data);
				}
				break;
			default:
				break;
			}
		}
	}

	/**
	 * 加载更多监听接口
	 */
	public interface OnLoadMoreListener {
		public void onLoadMore();
	}
	
	/***
	 * 
	 * @return
	 */
	public boolean isCanLoadMore() {
		return mCanLoadMore;
	}

	/***
	 * 
	 * @param pCanLoadMore
	 */
	public void setCanLoadMore(boolean pCanLoadMore) {
		mCanLoadMore = pCanLoadMore;
	}
	

	/***
	 * 设置加载更多
	 * 
	 * @param pLoadMoreListener
	 */
	public void setOnLoadListener(OnLoadMoreListener pLoadMoreListener) {
		if (pLoadMoreListener != null) {
			mLoadMoreListener = pLoadMoreListener;
			mCanLoadMore = true;
		}
	}

	/**
	 * 正在加载更多，FootView显示 ： 加载中...
	 */
	private void onLoadMore() {
		if (mLoadMoreListener != null) {
			// 加载中...
			mLoadMoreListener.onLoadMore();
		}
	}

	/**
	 * 加载更多完成
	 */
	public void onLoadMoreComplete() {
		mEndState = ENDINT_AUTO_LOAD_DONE;

		changeEndViewByState();
	}
	
	/***
	 * 设置没有更多数据
	 */
	public void setNoMoreData(boolean noMoreData){
		this.noMoreData = noMoreData;
		
		onLoadMoreComplete();
	}
}
