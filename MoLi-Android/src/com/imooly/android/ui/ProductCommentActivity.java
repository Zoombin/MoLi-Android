package com.imooly.android.ui;

import android.content.Intent;
import android.graphics.Matrix;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.adapter.StoreCommentListAdapter;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.entity.RspCommentList;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.widget.PullToRefreshListView;
import com.imooly.android.widget.PullToRefreshListView.OnLoadMoreListener;
import com.imooly.android.widget.Toast;

/***
 * 商城 - 用户评价
 * 
 * @author lsd
 * 
 */
public class ProductCommentActivity extends BaseActivity implements OnClickListener {
	public static final String EXTRA_GOODSID = "goodsid";
	private RelativeLayout ll_title;
	private ImageView iv_back;
	private TextView tv_title;

	private RelativeLayout rl_optionmenu;
	private LinearLayout ll_comment_result;
	private ImageView cursor;

	private TextView tv_store_comment_better;
	private TextView tv_store_comment_medium;
	private TextView tv_store_comment_worst;

	private TextView tv_comm_key1;
	private TextView tv_comm_value1;
	private TextView tv_comm_key2;
	private TextView tv_comm_value2;

	private PullToRefreshListView store_comment_list;

	int cursorW;
	int offset = 0;
	int currIndex = 0;

	StoreCommentListAdapter adapter;

	RspCommentList.Data goodData;
	RspCommentList.Data midData;
	RspCommentList.Data badData;

	String goodsid;
	String flag = "good";
	int curPage = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_storepro_comment);

		logActivityName(this);
		
		initView();
		InitImageCursor();

		getIntentExtras();

		initData();
		curPage = 1;
		getData(false);
	}

	private void initView() {
		// TODO Auto-generated method stub
		ll_title = (RelativeLayout) findViewById(R.id.ll_title);

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);

		tv_title = (TextView) findViewById(R.id.tv_title);

		ll_comment_result = (LinearLayout) findViewById(R.id.ll_comment_result);
		tv_comm_key1 = (TextView) findViewById(R.id.tv_comm_key1);
		tv_comm_value1 = (TextView) findViewById(R.id.tv_comm_value1);
		tv_comm_key2 = (TextView) findViewById(R.id.tv_comm_key2);
		tv_comm_value2 = (TextView) findViewById(R.id.tv_comm_value2);

		rl_optionmenu = (RelativeLayout) findViewById(R.id.rl_optionmenu);
		cursor = (ImageView) findViewById(R.id.cursor);

		tv_store_comment_better = (TextView) findViewById(R.id.tv_store_comment_better);
		tv_store_comment_better.setOnClickListener(OptionMenuClick);
		tv_store_comment_better.setTag(0);

		tv_store_comment_medium = (TextView) findViewById(R.id.tv_store_comment_medium);
		tv_store_comment_medium.setOnClickListener(OptionMenuClick);
		tv_store_comment_medium.setTag(1);

		tv_store_comment_worst = (TextView) findViewById(R.id.tv_store_comment_worst);
		tv_store_comment_worst.setOnClickListener(OptionMenuClick);
		tv_store_comment_worst.setTag(2);

		store_comment_list = (PullToRefreshListView) findViewById(R.id.store_comment_list);
	}

	private void InitImageCursor() {
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		int screenW = dm.widthPixels;// 屏幕宽度

		offset = 0;// 偏移量

		LayoutParams cursor_Params = cursor.getLayoutParams();
		cursorW = screenW / 3;// 游标宽度
		cursor_Params.width = cursorW;// 初始化滑动下标的宽
		cursor.setLayoutParams(cursor_Params);

		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		cursor.setImageMatrix(matrix);// 设置动画初始位置
		tv_store_comment_better.setSelected(true);
	}

	private void getIntentExtras() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		if (intent.hasExtra(EXTRA_GOODSID)) {
			goodsid = intent.getStringExtra(EXTRA_GOODSID);
		}
	}

	private void initData() {
		// TODO Auto-generated method stub
		adapter = new StoreCommentListAdapter();
		store_comment_list.setAdapter(adapter);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		default:
			break;
		}
	}

	OnClickListener OptionMenuClick = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.tv_store_comment_better:
				currIndex = (Integer) v.getTag();
				break;
			case R.id.tv_store_comment_medium:
				currIndex = (Integer) v.getTag();
				break;
			case R.id.tv_store_comment_worst:
				currIndex = (Integer) v.getTag();
				break;
			}
			optionChange(currIndex);
			updateCurserStatus(currIndex);
			curPage = 1;
			getData(false);
		}
	};

	void optionChange(int position) {
		tv_store_comment_better.setSelected(false);
		tv_store_comment_medium.setSelected(false);
		tv_store_comment_worst.setSelected(false);

		switch (position) {
		case 0:
			flag = "good";
			tv_store_comment_better.setSelected(true);
			break;
		case 1:
			flag = "mid";
			tv_store_comment_medium.setSelected(true);
			break;
		case 2:
			flag = "bad";
			tv_store_comment_worst.setSelected(true);
			break;
		}
	}

	void updateCurserStatus(int position) {
		int one = offset * 2 + cursorW;
		Animation animation = null;
		animation = new TranslateAnimation(one * currIndex, position * one, 0, 0);
		currIndex = position;
		animation.setFillAfter(true);// True:图片停在动画结束位置
		animation.setDuration(300);
		cursor.startAnimation(animation);
	}

	private void getData(final boolean addData) {
		// good（好）/mid（中）/bad（差
		Api.goodsCommentlist(self, goodsid, flag, curPage, 10, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				// TODO Auto-generated method stub
				store_comment_list.onLoadMoreComplete();
				RspCommentList rsp = (RspCommentList) rspData;
				if (rsp.data != null) {
					tv_comm_value1.setText(rsp.data.getHighopinion() + "%");
					tv_comm_value2.setText(rsp.data.getTotalcomment() + "");
					if (addData) {
						adapter.addData(rsp.data.getCommentlist());
					} else {
						adapter.setData(rsp.data.getCommentlist());
					}
				}
			}

			@Override
			public void failed(String msg) {
				// TODO Auto-generated method stub
				store_comment_list.onLoadMoreComplete();
				if (!TextUtils.isEmpty(msg)) {
					Toast.show(self, msg);
				}
			}
		}, RspCommentList.class);
	}
	
	OnLoadMoreListener loadmore = new OnLoadMoreListener() {
		@Override
		public void onLoadMore() {
			// TODO Auto-generated method stub
			curPage ++;
			getData(true);
		}
	};

}
