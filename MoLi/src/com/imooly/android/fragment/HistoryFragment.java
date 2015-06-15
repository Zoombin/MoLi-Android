package com.imooly.android.fragment;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.base.BaseFragment;
import com.imooly.android.db.DataHelper;
import com.imooly.android.ui.SearchActivity;
import com.imooly.android.ui.SearchResultActivity;
import com.imooly.android.widget.LineBreakLayout;

/**
 * 搜索历史fragment
 * @author daiye
 *
 */
public class HistoryFragment extends BaseFragment implements OnClickListener {
	
	private View view;
	private LineBreakLayout linebreaklayout;
	private Button btn_cleardata;
	private List<String> keywords;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_search_history, container, false);
		return view;
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		linebreaklayout = (LineBreakLayout) view.findViewById(R.id.linebreaklayout);
		
		btn_cleardata = (Button) view.findViewById(R.id.btn_cleardata);
		btn_cleardata.setOnClickListener(this);
	}
	
	
	@Override
	public void onResume() {
		super.onResume();
		
		linebreaklayout.removeAllViews();
		
		keywords = DataHelper.getInstance().QueryKeyWords();
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
					if (getActivity() instanceof SearchActivity) {
						mActivity.finish();
					}
				}
			});
			linebreaklayout.addView(tView); 
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cleardata:
			DataHelper.getInstance().deleteHistory();
			linebreaklayout.removeAllViews();
			break;
		default:
			break;
		}
	}
}
