package com.imooly.android.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.adapter.AfterSaleAdapter;
import com.imooly.android.base.BaseActivity;

public class AfterSaleActivity extends BaseActivity implements OnClickListener {

	private RelativeLayout ll_title;
	private ImageView iv_back;
	private TextView tv_title;
	private TextView tv_right;
	private ListView listView;

	private AfterSaleAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_after_sale);
		
		logActivityName(this);
		
		initView();
		initData();
		getData();
	}

	private void initView() {
		// TODO Auto-generated method stub
		ll_title = (RelativeLayout) findViewById(R.id.rl_title);

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);

		tv_title = (TextView) findViewById(R.id.tv_title);

		listView = (ListView) findViewById(R.id.list_after_sale);
	}

	private void initData() {
		// TODO Auto-generated method stub
		if (adapter == null) {
			adapter = new AfterSaleAdapter();
		}
		listView.setAdapter(adapter);
	}
	
	private void getData() {
		// TODO Auto-generated method stub
		List<String> sList = new ArrayList<String>();
		String s = "";
		sList.add(s);
		sList.add(s);
		sList.add(s);
		sList.add(s);
		sList.add(s);
		sList.add(s);
		sList.add(s);
		sList.add(s);

		adapter.setData(sList);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.tv_right:
			adapter.cleanData();
			break;
		default:
			break;
		}
	}

}
