package com.imooly.android.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.adapter.CollectionStoreAdapter;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.entity.RspCellectionFlagStoreList;
import com.imooly.android.entity.RspCellectionFlagStoreList.Store;
import com.imooly.android.entity.RspSuccessCommon;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.pull.PullUpListView;
import com.imooly.android.pull.PullUpListView.OnLoadListener;
import com.imooly.android.widget.Toast;

/***
 * 
 * 旗舰店收藏
 * 
 * @author
 * 
 */
public class CollectionFlagshipActivity extends BaseActivity implements OnClickListener, OnCheckedChangeListener, OnItemClickListener {
	private RelativeLayout rl_title, rl_delete, rl_none_collection;
	private Button button_edit, button_delete;
	private ImageView iv_back;
	private TextView tv_title;
	private CollectionStoreAdapter adapter;
	private PullUpListView mListView;
	private CheckBox checkBox_all;

	private Boolean isEdit = false;
	int curPage = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collection_flagship);

		logActivityName(this);
		initView();
		getData();
	}

	private void initView() {

		// title
		rl_title = (RelativeLayout) findViewById(R.id.rl_title);

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);

		tv_title = (TextView) findViewById(R.id.tv_title);

		button_edit = (Button) findViewById(R.id.button_edit);

		button_edit = (Button) findViewById(R.id.button_edit);
		button_edit.setOnClickListener(this);

		mListView = (PullUpListView) findViewById(R.id.list_collection_flag_ship);
		mListView.setOnLoadListener(new OnLoadListener() {
			
			@Override
			public void onLoad() {
				getData();
				
			}
		});
		
		adapter = new CollectionStoreAdapter(self);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(this);

		rl_delete = (RelativeLayout) findViewById(R.id.rl_delete);

		// 全选checkbox
		checkBox_all = (CheckBox) findViewById(R.id.check_all);
		checkBox_all.setOnCheckedChangeListener(this);

		// 删除按钮
		button_delete = (Button) findViewById(R.id.button_delete);
		button_delete.setOnClickListener(this);

		rl_none_collection = (RelativeLayout) findViewById(R.id.rl_none_collection);

	}

	// 删除某项
	public void deleteItem(String ids) {
		Api.delteMyCollectionFlagStore(self, ids, new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {
				// TODO Auto-generated method stub
				RspSuccessCommon rsp = (RspSuccessCommon) rspData;
				if (rsp.data != null && rsp.data.success == 1) {
					adapter.deleteItemes();
					Toast.show(self, "删除成功");

					if (adapter.getCount() == 0) {
						rl_none_collection.setVisibility(View.VISIBLE);
						button_edit.setVisibility(View.INVISIBLE);
						rl_delete.setVisibility(View.INVISIBLE);
					}

				}
			}

			@Override
			public void failed(String msg) {
				Toast.show(self, msg);
			}
		}, RspSuccessCommon.class);
	}

	private void getData() {
		// 获取收藏列表
		Api.getMyCollectionFlagStores(self, String.valueOf(curPage), 15 + "", new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				RspCellectionFlagStoreList rsp = (RspCellectionFlagStoreList) rspData;
				if (rsp.data != null) {
					List<Store> goodslist = rsp.data.getStorelist();

					// 内容为空的情况
					if (curPage == 1 && goodslist.size() == 0) {
						rl_none_collection.setVisibility(View.VISIBLE);
						button_edit.setVisibility(View.INVISIBLE);
						
						return;
					}

					if (goodslist != null && goodslist.size() > 0) {
						button_edit.setVisibility(View.VISIBLE);
						adapter.addData(goodslist);
					}
				}
				
				mListView.onLoadCompleted();
			}

			@Override
			public void failed(String msg) {
				Toast.show(self, msg);
				mListView.onLoadCompleted();
			}
		}, RspCellectionFlagStoreList.class);

	}

	private void deleteItems() {

		List<Boolean> selects = adapter.getSelect();

		List<String> ids = null;
		if (selects != null && selects.size() > 0) {
			ids = new ArrayList<String>();
			for (int i = 0; i < selects.size(); i++) {
				boolean b = selects.get(i);
				if (b) {
					Store goods = (Store) adapter.getItem(i);
					if (goods != null) {
						ids.add(goods.getStoreid());
					}
				}
			}

			// 没有选择任何删除项
			if (ids.size() == 0) {
				Toast.show(self, "请选择删除项！");
				return;
			}

			String goodsids = new Gson().toJson(ids);
			deleteItem(goodsids);
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.button_edit: // 编辑
			if (isEdit == false) {
				rl_delete.setVisibility(View.VISIBLE);
				adapter.editMode(true);
				isEdit = true;
			} else {
				rl_delete.setVisibility(View.GONE);
				adapter.editMode(false);
				isEdit = false;
			}
			break;
		case R.id.button_delete: // 删除按钮

			deleteItems();

			break;
		default:
			break;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		if (isEdit == true) {
			CheckBox checkBox = (CheckBox) view.findViewById(R.id.check);
			if (checkBox.isChecked()) {
				adapter.changeSelect(position, false);
			} else {
				adapter.changeSelect(position, true);
			}
		} else {

			Store store = (Store) adapter.getItem(position);
			Intent intent = new Intent(self, StoreProActivity.class);
			intent.putExtra(StoreProActivity.EXTRA_BUSINESSID, store.getStoreid());
			startActivity(intent);
		}
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked == true) {
			adapter.selectAll();
		} else {
			adapter.cleanSelect();
		}
	}

}
