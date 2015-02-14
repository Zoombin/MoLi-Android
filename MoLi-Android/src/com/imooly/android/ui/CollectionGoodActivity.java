package com.imooly.android.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
import com.imooly.android.adapter.CollectionGoodAdapter;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.entity.RspCellectionGoodsList;
import com.imooly.android.entity.RspCellectionGoodsList.CollectionGood;
import com.imooly.android.entity.RspSuccessCommon;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.widget.Toast;

/***
 * 收藏商品
 * 
 * @author
 * 
 */
public class CollectionGoodActivity extends BaseActivity implements OnClickListener, OnCheckedChangeListener, OnItemClickListener {
	private RelativeLayout rl_title, rl_delete, rl_none_collection_good;
	private Button button_edit, button_delete;
	private ImageView iv_back;
	private TextView tv_title;
	private CollectionGoodAdapter adapter;
	private ListView mListView;
	private CheckBox checkBox_all;

	private Boolean isEdit = false;
	int curPage = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_collection_good);

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

		mListView = (ListView) findViewById(R.id.list_collection_good);
		adapter = new CollectionGoodAdapter(self);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(this);

		rl_delete = (RelativeLayout) findViewById(R.id.rl_delete);

		// 全选checkbox
		checkBox_all = (CheckBox) findViewById(R.id.check_all);
		checkBox_all.setOnCheckedChangeListener(this);

		// 删除按钮
		button_delete = (Button) findViewById(R.id.button_delete);
		button_delete.setOnClickListener(this);
		
		rl_none_collection_good = (RelativeLayout) findViewById(R.id.rl_none_collection_good);

	}

	private void getData() {

		// 获取收藏列表
		Api.getMyCollectionGoods(self, String.valueOf(curPage), String.valueOf(15), new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				RspCellectionGoodsList rsp = (RspCellectionGoodsList) rspData;
				if (rsp.data != null) {
					List<CollectionGood> goodslist = rsp.data.getGoodslist();
					
					// 内容为空的情况
					if (curPage ==1 && goodslist.size() == 0) {
						rl_none_collection_good.setVisibility(View.VISIBLE);
						return;
					}
					
					if (goodslist != null && goodslist.size() > 0) {
							adapter.addData(goodslist);
					}
				}
			}

			@Override
			public void failed(String msg) {
				Toast.show(self, msg);
			}
		}, RspCellectionGoodsList.class);

	}

	// 删除某一项
	private void deleteItemes(String goodIDs) {
		Api.delteMyCollectionGood(self, goodIDs, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				RspSuccessCommon rsp = (RspSuccessCommon) rspData;
				if (rsp.data != null && rsp.data.success == 1) {
					adapter.deleteItems();
					Log.d("xxx", ">>>array after delete" + adapter.getSelect().toString());
					
					// 当内容为空的时候
					if (adapter.getCount() == 0) {
						rl_none_collection_good.setVisibility(View.VISIBLE);
					}
				}
			}

			@Override
			public void failed(String msg) {
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
		case R.id.iv_back:
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
		List<String> ids = null;
		if (selects != null && selects.size() > 0) {
			ids = new ArrayList<String>();
			for (int i = 0; i < selects.size(); i++) {
				boolean b = selects.get(i);
				if (b) {
					CollectionGood goods = (CollectionGood) adapter.getItem(i);
					if (goods != null) {
						ids.add(goods.getGoodsid());
					}
				}
			}
			deleteItemes(new Gson().toJson(ids));
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
			CollectionGood collectionGood = (CollectionGood) adapter.getItem(position);
			Intent intent = new Intent(self, ProductDetailActivity.class);
			intent.putExtra(ProductDetailActivity.EXTRA_GOODSID, collectionGood.getGoodsid());
			
			startActivity(intent);
			
		}
	}
}
