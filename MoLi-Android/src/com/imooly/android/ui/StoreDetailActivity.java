package com.imooly.android.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.imooly.android.MoLiApplication;
import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.adapter.StoreDetailCommentListAdapter;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.entity.RspDiscountstore;
import com.imooly.android.entity.RspStoreCommentList;
import com.imooly.android.entity.RspSuccessCommon;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.enums.ShareObject;
import com.imooly.android.widget.CannotRollListView;
import com.imooly.android.widget.ObservableScrollView;
import com.imooly.android.widget.ShareDialog;
import com.imooly.android.widget.Toast;
import com.imooly.android.widget.ObservableScrollView.OnScrollListener;
import com.nostra13.universalimageloader.core.ImageLoader;

/***
 * 实体店 - 商家详情
 * 
 * @author lsd
 * 
 */
public class StoreDetailActivity extends BaseActivity implements OnClickListener {
	public static String EXTRA_BUSNESSID = "businessid";
	private RelativeLayout ll_title;
	private ImageView iv_back;
	private TextView tv_title;
	private ImageView share_store;
	private ImageView collect_store;
	private Button btn_rull_up;
	private ObservableScrollView scorllview;

	private LinearLayout ll_level1;
	private ImageView item_pic;
	private ImageView ic_call;
	private TextView item_name;
	private RatingBar item_ratingBar;
	private ImageView item_type_icon;
	private TextView item_type;

	private LinearLayout ll_level2;
	private TextView location_address;
	private TextView level2_txt;
	private TextView level2_describe;

	private LinearLayout ll_level3;
	private TextView level3_txt;
	private ImageView level3_edit;
	private LinearLayout ll_level3_content;
	private CannotRollListView store_content_list;
	StoreDetailCommentListAdapter adapter;

	String businessid;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_detail);

		logActivityName(this);

		initView();
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
		getCommentList();
	}

	private void initView() {
		// TODO Auto-generated method stub
		ll_title = (RelativeLayout) findViewById(R.id.ll_title);

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);

		tv_title = (TextView) findViewById(R.id.tv_title);

		btn_rull_up = (Button) findViewById(R.id.btn_rull_up);
		btn_rull_up.setOnClickListener(this);
		scorllview = (ObservableScrollView) findViewById(R.id.scorllview);

		share_store = (ImageView) findViewById(R.id.share_store);
		share_store.setOnClickListener(this);
		collect_store = (ImageView) findViewById(R.id.collect_store);
		collect_store.setOnClickListener(this);

		location_address = (TextView) findViewById(R.id.location_address);
		location_address.setOnClickListener(this);

		ll_level1 = (LinearLayout) findViewById(R.id.ll_level1);
		ll_level1.setVisibility(View.INVISIBLE);
		item_pic = (ImageView) findViewById(R.id.item_pic);
		ic_call = (ImageView) findViewById(R.id.ic_call);
		ic_call.setOnClickListener(this);
		item_name = (TextView) findViewById(R.id.item_name);
		item_ratingBar = (RatingBar) findViewById(R.id.item_ratingBar);
		item_type = (TextView) findViewById(R.id.item_type);
		item_type_icon = (ImageView) findViewById(R.id.item_type_icon);

		ll_level2 = (LinearLayout) findViewById(R.id.ll_level2);
		ll_level2.setVisibility(View.INVISIBLE);
		level2_txt = (TextView) findViewById(R.id.level2_txt);
		level2_describe = (TextView) findViewById(R.id.level2_describe);

		ll_level3 = (LinearLayout) findViewById(R.id.ll_level3);
		ll_level3.setVisibility(View.INVISIBLE);
		level3_txt = (TextView) findViewById(R.id.level3_txt);
		level3_edit = (ImageView) findViewById(R.id.level3_edit);
		level3_edit.setOnClickListener(this);
		ll_level3_content = (LinearLayout) findViewById(R.id.ll_level3_content);

		store_content_list = (CannotRollListView) findViewById(R.id.store_content_list);
		adapter = new StoreDetailCommentListAdapter();
		store_content_list.setAdapter(adapter);
	}

	private void initData() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		if (!intent.hasExtra(EXTRA_BUSNESSID)) {
			return;
		}
		businessid = intent.getStringExtra(EXTRA_BUSNESSID);

		Api.discountstore(self, businessid, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				// TODO Auto-generated method stub
				RspDiscountstore rsp = (RspDiscountstore) rspData;
				if (rsp.data != null) {
					RspDiscountstore.Data data = rsp.data;
					ll_level1.setVisibility(View.VISIBLE);
					ll_level2.setVisibility(View.VISIBLE);

					level3_edit.setTag(rsp);

					ImageLoader.getInstance().displayImage(data.getBusinessicon(), item_pic);
					if (data.getTel() != null && data.getTel().size() > 0) {
						ic_call.setTag(data.getTel().get(0));
					}

					String name = data.getBusinessname();
					if (!TextUtils.isEmpty(name) && !"null".equals(name)) {
						item_name.setText(name);
					}
					item_ratingBar.setRating(data.getStarleve());
					String industry = data.getIndustry();

					imageLoader.displayImage(data.getIndustryicon(), item_type_icon);

					if (!TextUtils.isEmpty(industry) && !"null".equals(industry)) {
						item_type.setText(industry);
					}
					String dddress = data.getAddress();
					if (!TextUtils.isEmpty(dddress) && !"null".equals(dddress)) {
						location_address.setText(dddress);
						location_address.setTag(rsp);
					}

					String descript = data.getDescription();
					if (!TextUtils.isEmpty(descript) && !"null".equals(descript)) {
						level2_describe.setText(descript);
					}

					int favorites = data.getFavorites();
					if (1 == favorites) {
						collect_store.setSelected(true);
					} else {
						collect_store.setSelected(false);
					}
				}
			}

			@Override
			public void failed(String msg) {
				// TODO Auto-generated method stub
				Toast.show(self, msg);
			}
		}, RspDiscountstore.class);
	}

	private void getCommentList() {
		if (TextUtils.isEmpty(businessid)) {
			return;
		}
		/** 获取评论 */
		Api.storeCommentlist(self, businessid, "", 1, 30, new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {
				// TODO Auto-generated method stub
				ll_level3.setVisibility(View.VISIBLE);
				RspStoreCommentList rsp = (RspStoreCommentList) rspData;
				if (rsp.data != null) {
					adapter.setData(rsp.data.getCommentlist());
				}
			}

			@Override
			public void failed(String msg) {
				// TODO Auto-generated method stub
				ll_level3.setVisibility(View.VISIBLE);
			}
		}, RspStoreCommentList.class);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode != RESULT_OK) {
			return;
		}
		getCommentList();
	}

	/** 推送进来的特别处理 */
	private void goBack() {
		String pushAction = getIntent().getStringExtra("pushAction");
		if (TextUtils.isEmpty(pushAction)) {
			finish();
		} else {
			Intent intent = new Intent(MainActivity.ACTION);
			sendBroadcast(intent);

			startActivity(new Intent(self, MainActivity.class));
			finish();
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		goBack();
	}

	OnScrollListener onScrollListener = new OnScrollListener() {
		@Override
		public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
			// TODO Auto-generated method stub
			if (y > 20) {
				btn_rull_up.setVisibility(View.VISIBLE);
			} else {
				btn_rull_up.setVisibility(View.INVISIBLE);
			}
		}
	};

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.share_store:
			new ShareDialog(self, ShareObject.estore, businessid);
			break;
		case R.id.collect_store:
			if(!MoLiApplication.getInstance().isLogin()){
				startActivity(new Intent(self,LoginActivity.class));
				return;
			}
			final ImageView iv = (ImageView) v;
			if (v.isSelected()) {
				String businessids = "[\"" + businessid + "\"]";
				Api.delteMyCollectionStore(self, businessids, new NetCallBack<ServiceResult>() {
					@Override
					public void success(ServiceResult rspData) {
						// TODO Auto-generated method stub
						RspSuccessCommon rsp = (RspSuccessCommon) rspData;
						if (rsp.data != null && rsp.data.success == 1) {
							iv.setSelected(false);
							Toast.show(self, "已取消收藏");
						} else {
							Toast.show(self, "取消收藏失败");
						}
					}

					@Override
					public void failed(String msg) {
						// TODO Auto-generated method stub
						Toast.show(self, msg);
					}
				}, RspSuccessCommon.class);
			} else {
				Api.addfavbusiness(self, businessid, new NetCallBack<ServiceResult>() {
					@Override
					public void success(ServiceResult rspData) {
						// TODO Auto-generated method stub
						RspSuccessCommon rsp = (RspSuccessCommon) rspData;
						if (rsp.data != null && rsp.data.success == 1) {
							iv.setSelected(true);
							Toast.show(self, "收藏成功");
						} else {
							Toast.show(self, "收藏失败");
						}
					}

					@Override
					public void failed(String msg) {
						// TODO Auto-generated method stub
						Toast.show(self, msg);
					}
				}, RspSuccessCommon.class);
			}
			break;
		case R.id.ic_call:
			String callNum = (String) v.getTag();
			if (TextUtils.isEmpty(callNum))
				callNum = "0512-345678";
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + callNum));
			startActivity(intent);
			break;
		case R.id.location_address:
			RspDiscountstore store = (RspDiscountstore) v.getTag();
			startActivity(new Intent(self, SearchMapActivity.class).putExtra(SearchMapActivity.STORE_NAV, store));
			break;
		case R.id.level3_edit:
			RspDiscountstore tag = (RspDiscountstore) v.getTag();
			startActivityForResult((new Intent(self, StoreCommentActivity.class).putExtra(StoreCommentActivity.MODE, tag)), 100);
			break;
		case R.id.btn_rull_up:
			scorllview.fullScroll(ScrollView.FOCUS_UP);
			btn_rull_up.setVisibility(View.INVISIBLE);
			break;
		default:
			break;
		}
	}
}
