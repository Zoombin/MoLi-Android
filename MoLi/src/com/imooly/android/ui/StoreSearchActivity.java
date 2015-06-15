package com.imooly.android.ui;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.db.DataHelper;
import com.imooly.android.entity.RspBusinessHotKeyworlds;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.widget.LineBreakLayout;
import com.imooly.android.widget.LineBreakLayoutV2;
import com.imooly.android.widget.Toast;

/***
 * 实体店 - 商家搜索
 * 
 * @author lsd
 * 
 */
public class StoreSearchActivity extends BaseActivity implements OnClickListener {
	private RelativeLayout ll_title;
	private ImageView iv_back;
	private TextView tv_right;

	private EditText et_input;
	private ImageView iv_delete;

	private TextView tv_hotsearch, tv_latesearch;
	private LineBreakLayoutV2 hotsearch_grid;
	private LineBreakLayoutV2 latesearch_grid;
	private Button bt_clean;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_search);

		logActivityName(this);

		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();
	}

	private void initView() {
		// TODO Auto-generated method stub
		ll_title = (RelativeLayout) findViewById(R.id.ll_title);

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);

		tv_right = (TextView) findViewById(R.id.tv_right);
		tv_right.setOnClickListener(this);

		et_input = (EditText) findViewById(R.id.et_input);

		iv_delete = (ImageView) findViewById(R.id.iv_delete);
		iv_delete.setOnClickListener(this);

		tv_hotsearch = (TextView) findViewById(R.id.tv_hotsearch);
		tv_latesearch = (TextView) findViewById(R.id.tv_latesearch);

		hotsearch_grid = (LineBreakLayoutV2) findViewById(R.id.hotsearch_grid);
		latesearch_grid = (LineBreakLayoutV2) findViewById(R.id.latesearch_grid);

		bt_clean = (Button) findViewById(R.id.bt_clean);
		bt_clean.setOnClickListener(this);
		et_input.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				// TODO Auto-generated method stub
				switch (actionId) {
				case EditorInfo.IME_ACTION_SEARCH:
					search();
					break;
				}
				return true;
			}
		});
		et_input.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (!TextUtils.isEmpty(s)) {
					iv_delete.setVisibility(View.VISIBLE);
				} else {
					iv_delete.setVisibility(View.INVISIBLE);
				}
			}
		});
	}

	private void initData() {
		// TODO Auto-generated method stub
		// 最近搜索
		List<String> keywords = DataHelper.getInstance().QueryStoreSearch();
		if (keywords != null && keywords.size() > 0) {
			bt_clean.setVisibility(View.VISIBLE);
			latesearch_grid.removeAllViews();

			for (final String keyword : keywords) {
				TextView tView = (TextView) LayoutInflater.from(self).inflate(R.layout.search_item, null);
				tView.setText(keyword);
				tView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						DataHelper.getInstance().SaveStoreSearch(keyword, ((Long) System.currentTimeMillis()).intValue());
						Intent intent = new Intent(self, StoreSearchResultActivity.class);
						intent.putExtra(StoreSearchResultActivity.SEARCH_KEY, keyword);
						intent.putExtra(StoreSearchResultActivity.ENTRY_TYPE, "key_search");
						startActivity(intent);
					}
				});
				latesearch_grid.addView(tView);
			}
		} else {
			bt_clean.setVisibility(View.INVISIBLE);
		}

		// 热门搜索
		Api.businessHotkeywords(self, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				// TODO Auto-generated method stub
				RspBusinessHotKeyworlds rsp = (RspBusinessHotKeyworlds) rspData;
				if (rsp.data != null) {
					List<String> keywords = rsp.data.getKeywords();
					if (keywords != null && keywords.size() > 0) {
						hotsearch_grid.removeAllViews();

						for (final String keyword : keywords) {
							TextView tView = (TextView) LayoutInflater.from(self).inflate(R.layout.search_item, null);
							tView.setText(keyword);
							tView.setOnClickListener(new OnClickListener() {
								@Override
								public void onClick(View v) {
									DataHelper.getInstance().SaveStoreSearch(keyword, ((Long) System.currentTimeMillis()).intValue());
									Intent intent = new Intent(self, StoreSearchResultActivity.class);
									intent.putExtra(StoreSearchResultActivity.SEARCH_KEY, keyword);
									intent.putExtra(StoreSearchResultActivity.ENTRY_TYPE, "key_search");
									startActivity(intent);
								}
							});
							hotsearch_grid.addView(tView);
						}
					}
				}
			}

			@Override
			public void failed(String msg) {
				// TODO Auto-generated method stub
			}
		}, RspBusinessHotKeyworlds.class);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.tv_right:
			search();
			break;
		case R.id.iv_delete:
			et_input.setText("");
			break;
		case R.id.bt_clean:
			DataHelper.getInstance().deleteStoreSearch();
			latesearch_grid.removeAllViews();
			bt_clean.setVisibility(View.INVISIBLE);
			break;
		default:
			break;
		}
	}
	
	private void search() {
		String str = et_input.getText().toString();
		if (TextUtils.isEmpty(str)) {
			Toast.show(self, "请输入搜索内容");
			return;
		}
		DataHelper.getInstance().SaveStoreSearch(str, ((Long) System.currentTimeMillis()).intValue());
		Intent intent = new Intent(self, StoreSearchResultActivity.class);
		intent.putExtra(StoreSearchResultActivity.SEARCH_KEY, str);
		intent.putExtra(StoreSearchResultActivity.ENTRY_TYPE, "key_search");
		startActivity(intent);
	}
}
