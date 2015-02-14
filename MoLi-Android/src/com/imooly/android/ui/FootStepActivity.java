package com.imooly.android.ui;

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
import com.imooly.android.adapter.FootStepAdapter;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.db.DataHelper;
import com.imooly.android.entity.FootStepEntity;
import com.imooly.android.view.CommonConfirmDialog;
import com.imooly.android.view.CommonConfirmDialog.CommonAlertCallBack;

/***
 * 我的足迹
 * 
 * @author lsd
 * 
 */
public class FootStepActivity extends BaseActivity implements OnClickListener {
	private RelativeLayout ll_title, rl_none_footstep;
	private ImageView iv_back;
	private TextView tv_title;
	private TextView tv_right;
	private ListView listView;

	private FootStepAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_footstep);

		logActivityName(this);

		initView();
		initData();
		getData();
	}

	private void initView() {
		// TODO Auto-generated method stub
		ll_title = (RelativeLayout) findViewById(R.id.rl_title_footstep);

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);

		tv_title = (TextView) findViewById(R.id.tv_title);

		tv_right = (TextView) findViewById(R.id.tv_right);
		tv_right.setOnClickListener(this);

		listView = (ListView) findViewById(R.id.footstep_list);
		
		rl_none_footstep = (RelativeLayout) findViewById(R.id.rl_none_footstep);
	}

	private void initData() {
		// TODO Auto-generated method stub
		if (adapter == null) {
			adapter = new FootStepAdapter();
		}
		listView.setAdapter(adapter);
	}

	private void getData() {
		
		// TODO Auto-generated method stub
		List<FootStepEntity> list = DataHelper.getInstance().QueryFootstep();
		
		if (list.size() == 0) {
			rl_none_footstep.setVisibility(View.VISIBLE);
		}else {
			adapter.setData(list);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.tv_right:
			CommonConfirmDialog.show(self, "是否清除您的足迹？", "","",new CommonAlertCallBack() {
				@Override
				public void onConfirm() {
					// TODO Auto-generated method stub
					DataHelper.getInstance().deleteFootstep();
					adapter.cleanData();
					rl_none_footstep.setVisibility(View.VISIBLE);
				}

				@Override
				public void onCancel() {
					// TODO Auto-generated method stub
				}
			});
			break;
		default:
			break;
		}
	}
}
