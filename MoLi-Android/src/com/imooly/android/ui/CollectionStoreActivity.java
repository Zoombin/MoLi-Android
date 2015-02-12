package com.imooly.android.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
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
import com.imooly.android.adapter.CollectionStoreFlagshipAdapter;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.entity.RspCellectionStoreList;
import com.imooly.android.entity.RspCellectionStoreList.Business;
import com.imooly.android.entity.RspSuccessCommon;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.widget.Toast;

/***
 * 实体店收藏
 * 
 * @author
 * 
 */
public class CollectionStoreActivity extends BaseActivity implements OnClickListener, OnCheckedChangeListener, OnItemClickListener {
	private RelativeLayout rl_title, rl_delete, rl_none_collection_store;
	private Button  button_edit, button_delete;
	private ImageView iv_back;
	private TextView tv_title;
	private CollectionStoreFlagshipAdapter adapter;
	private ListView mListView;
	private CheckBox checkBox_all;

	private Boolean isEdit = false;
	int curPage = 1;
	private static final String PAGE_SIZE = "15";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collection_store);

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

		mListView = (ListView) findViewById(R.id.list_collection_store);
		adapter = new CollectionStoreFlagshipAdapter(self);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(this);

		rl_delete = (RelativeLayout) findViewById(R.id.rl_delete);

		// 全选checkbox
		checkBox_all = (CheckBox) findViewById(R.id.check_all);
		checkBox_all.setOnCheckedChangeListener(this);

		// 删除按钮
		button_delete = (Button) findViewById(R.id.button_delete);
		button_delete.setOnClickListener(this);

		rl_none_collection_store = (RelativeLayout) findViewById(R.id.rl_none_collection_store);
	}

	private void getData() {
		// 获取收藏列表
		Api.getMyCollectionStores(self, String.valueOf(curPage), PAGE_SIZE, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				RspCellectionStoreList rsp = (RspCellectionStoreList) rspData;
				if (rsp.data != null) {
					List<Business> goodslist = rsp.data.getBusinesslist();
					if (curPage == 1 && goodslist.size() == 0) {
						rl_none_collection_store.setVisibility(View.VISIBLE);
						return;
					}

					if (goodslist != null && goodslist.size() > 0) {
						adapter.addData(goodslist);
					}
				}
			}

			@Override
			public void failed(String msg) {
				// TODO Auto-generated method stub
			}
		}, RspCellectionStoreList.class);

	}

	private void deleteItem(String businessIDs) {
		Api.delteMyCollectionStore(self, businessIDs, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				// TODO Auto-generated method stub
				RspSuccessCommon rsp = (RspSuccessCommon) rspData;
				if (rsp.data != null && rsp.data.success == 1) {
					adapter.deleteItemes();

					// 当内容为空的时候
					if (adapter.getCount() == 0) {
						rl_none_collection_store.setVisibility(View.VISIBLE);
					}
				}
			}

			@Override
			public void failed(String msg) {
				// TODO Auto-generated method stub
				Toast.show(self, "删除失败");
			}
		}, RspSuccessCommon.class);
	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (isChecked == true) {
			adapter.selectAll();
		} else {
			adapter.cleanSelect();
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.iv_back:  // 返回按钮

			finish();
			break;
		case R.id.button_edit: // 编辑

			editButtonClick();
			break;
		case R.id.button_delete: // 删除按钮

			deleteCllections();

			break;
		default:
			break;
		}
	}

	private void editButtonClick() {

		if (isEdit == false) {
			rl_delete.setVisibility(View.VISIBLE);
			adapter.editMode(true);
			isEdit = true;
		} else {
			rl_delete.setVisibility(View.GONE);
			adapter.editMode(false);
			isEdit = false;
		}
	}

	private void deleteCllections() {

		List<Boolean> selects = adapter.getSelect();
		List<String> businessids = null;
		if (selects != null && selects.size() > 0) {
			businessids = new ArrayList<String>();
			for (int i = 0; i < selects.size(); i++) {
				boolean b = selects.get(i);
				if (b) {
					Business goods = (Business) adapter.getItem(i);
					if (goods != null) {
						businessids.add(goods.getBusinessid());
					}
				}
			}

			String businessidsStr = new Gson().toJson(businessids);
			deleteItem(businessidsStr);
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
		} else { // 跳转到某个实体店
			Business business = (Business) adapter.getItem(position);
			Intent intent = new Intent(self, StoreDetailActivity.class);
			intent.putExtra(StoreDetailActivity.EXTRA_BUSNESSID, business.getBusinessid());
			startActivity(intent);
		}
	}

}
