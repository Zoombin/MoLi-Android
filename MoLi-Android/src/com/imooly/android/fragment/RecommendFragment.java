package com.imooly.android.fragment;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.base.BaseFragment;
import com.imooly.android.db.DataHelper;
import com.imooly.android.entity.RspHotKeyWords;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.ui.SearchResultActivity;
import com.imooly.android.widget.LineBreakLayout;
import com.imooly.android.widget.Toast;

/**
 * 热门热门fragment
 * 
 * @author daiye
 * 
 */
public class RecommendFragment extends BaseFragment {

	private View view;
	private LineBreakLayout linebreaklayout;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater
				.inflate(R.layout.fragment_search_recommend, container, false);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		linebreaklayout = (LineBreakLayout) view.findViewById(R.id.linebreaklayout);
		Api.hotKeyWords(mActivity, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				RspHotKeyWords rsp = (RspHotKeyWords) rspData;
				List<String> keywords = rsp.data.getKeywords();
				for (final String keyword : keywords) {
					TextView tView = (TextView) LayoutInflater.from(mActivity).inflate(
							R.layout.search_item, null);
					tView.setText(keyword);
					tView.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							DataHelper.getInstance().SaveOrUpdateOrder(keyword, ((Long)System.currentTimeMillis()).intValue());
							Intent intent = new Intent(mActivity, SearchResultActivity.class);
							intent.putExtra(SearchResultActivity.EXTRA_KEYWORD, keyword);
							mActivity.startActivity(intent);
						}
					});
					
					linebreaklayout.addView(tView); 
				}
			}

			@Override
			public void failed(String msg) {
				Toast.show(mActivity, msg);
			}
		}, RspHotKeyWords.class);
	}
}
