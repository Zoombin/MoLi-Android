package com.imooly.android.view;

import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.imooly.android.R;
import com.imooly.android.adapter.StoreSearchReslutFilterAdapter;
import com.imooly.android.adapter.StoreSearchReslutFilterAdapterTwo;
import com.imooly.android.entity.RspBusinessCirclelist.Circle;
import com.imooly.android.entity.RspBusinessClassifyList.ClassifyEntity;
import com.imooly.android.entity.RspBusinessClassifyList.SubClassifyEntity;
import com.imooly.android.tool.Config;
import com.imooly.android.ui.StoreSearchResultActivity.FilterType;

/***
 * 
 * 实体店 - 搜索 筛选
 * 
 * @author LSD
 * 
 */
public class StoreSearchResultFilterView extends LinearLayout {
	private Context context;
	private StoreSearchResultFilter callBack;

	private LinearLayout ll_level1;
	private ListView level1_list;
	private LinearLayout ll_level2;
	private ListView level2_list;

	StoreSearchReslutFilterAdapter adapter1;
	StoreSearchReslutFilterAdapterTwo adapter2;

	List<?> list;
	FilterType type;

	public StoreSearchResultFilterView(Context context) {
		// TODO Auto-generated constructor stub
		this(context, null);
		initView(context);
	}

	public StoreSearchResultFilterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		initView(context);
	}

	public interface StoreSearchResultFilter {
		void onSelect(FilterType type, Object data, int PSelect);
	}

	public void setCallBack(StoreSearchResultFilter callBack) {
		this.callBack = callBack;
	}

	/***
	 * 界面初始化
	 * 
	 * @param popView
	 */
	private void initView(Context context) {
		LayoutInflater.from(context).inflate(R.layout.view_store_search_result_dialog, this, true);
		this.context = context;

		ll_level1 = (LinearLayout) findViewById(R.id.ll_level1);
		level1_list = (ListView) findViewById(R.id.level1_list);

		ll_level2 = (LinearLayout) findViewById(R.id.ll_level2);
		level2_list = (ListView) findViewById(R.id.level2_list);

		adapter1 = new StoreSearchReslutFilterAdapter();
		adapter2 = new StoreSearchReslutFilterAdapterTwo();

		level1_list.setAdapter(adapter1);
		level2_list.setAdapter(adapter2);

		level1_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				if (type == FilterType.SORT) {
					if (callBack != null) {
						callBack.onSelect(type, adapter1.getItem(position), position);
					}
				} else {
					if (type == FilterType.CATEGRORY) {
						adapter1.changeSelect(position);
						ClassifyEntity fyEty = (ClassifyEntity) adapter1.getItem(position);
						List<SubClassifyEntity> subList = fyEty.getSubclassify();
						if (subList != null && subList.size() > 0) {
							adapter2.setData(type, subList, position);
						} else {

						}
					}
					if (type == FilterType.CIRCLE) {
						adapter1.changeSelect(position);
						Circle cirEty = (Circle) adapter1.getItem(position);
						List<Circle> subList = cirEty.getSub();
						if (subList != null && subList.size() > 0) {
							adapter2.setData(type, subList, position);
						}else{
							callBack.onSelect(type, cirEty, position);
						}
					}
				}
			}
		});
		level2_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				callBack.onSelect(type, adapter2.getItem(position), adapter2.getPSelect());
			}
		});
	}

	public void show(FilterType type, List<?> list, int PSelect) {
		if (list == null || list.size() == 0) {
			return;
		}

		this.list = list;
		this.type = type;

		adapter1.setData(type, PSelect, list);

		if (type == FilterType.SORT) {
			ll_level2.setVisibility(View.GONE);
		} else {
			android.view.ViewGroup.LayoutParams params = new LayoutParams(Config.width, 450);
			level1_list.setLayoutParams(params);
			level2_list.setLayoutParams(params);
			ll_level2.setVisibility(View.VISIBLE);
			if (type == FilterType.CATEGRORY) {
				ClassifyEntity fyEty = (ClassifyEntity) list.get(PSelect);
				adapter2.setData(type, fyEty.getSubclassify(), PSelect);
			}
			if (type == FilterType.CIRCLE) {
				Circle cirEty = (Circle) list.get(PSelect);
				List<Circle> subs = cirEty.getSub();
				adapter2.setData(type, subs, PSelect);
			}
		}
	}

}
