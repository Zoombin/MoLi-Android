package com.imooly.android.fragment;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.imooly.android.R;
import com.imooly.android.Interface.RequestWebListener;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.adapter.CategoryNewOneAdapter;
import com.imooly.android.adapter.CategoryNewOneAdapter.CategoryOneListener;
import com.imooly.android.adapter.CategoryNewThreeAdapter;
import com.imooly.android.adapter.CategoryNewThreeAdapter.CategoryThreeListener;
import com.imooly.android.adapter.CategoryNewTwoAdapter;
import com.imooly.android.adapter.CategoryNewTwoAdapter.CategoryTwoListener;
import com.imooly.android.base.BaseFragment;
import com.imooly.android.entity.RspGoodsClassList;
import com.imooly.android.entity.RspGoodsClassList.GoodsClassify;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.tool.Config;
import com.imooly.android.ui.MainActivity;
import com.imooly.android.ui.MainActivity.MyTouchListener;
import com.imooly.android.ui.SearchActivity;
import com.imooly.android.ui.SearchResultActivity;
import com.imooly.android.utils.Utils;
import com.imooly.android.widget.LoadingView;

/**
 * 分类
 * 
 * @author daiye
 * 
 */
public class CategoryFragment extends BaseFragment implements OnClickListener {

	private LinearLayout ll_title_category;
	private RelativeLayout layout_category;
	private LoadingView layout_loading;
	private EditText home_homepage_et_input;
	private LinearLayout category_line1;
	private LinearLayout category_line2;
	private LinearLayout category_line3;
	private ListView category_one;
	private ListView category_two;
	private ListView category_three;
	private Animation mShowAction;
	private Animation mHiddenAction;
	private CategoryNewOneAdapter oneadapter;
	private CategoryNewTwoAdapter twoadapter;
	private CategoryNewThreeAdapter threeadapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_category, container, false);
		createView(v);
		initData();
		return v;
	}

	GestureDetector gestureDetector;

	private void createView(View v) {
		ll_title_category = (LinearLayout) v.findViewById(R.id.ll_title_category);
		// fragment重叠bug
		ll_title_category.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// 不做操作
			}
		});
		
		layout_category = (RelativeLayout) v.findViewById(R.id.layout_category);
		layout_loading = (LoadingView) v.findViewById(R.id.layout_loading);

		home_homepage_et_input = (EditText) v
				.findViewById(R.id.home_homepage_et_input);
		home_homepage_et_input.setOnClickListener(this);

		category_line1 = (LinearLayout) v.findViewById(R.id.category_line1);
		category_line2 = (LinearLayout) v.findViewById(R.id.category_line2);
		category_line3 = (LinearLayout) v.findViewById(R.id.category_line3);

		category_one = (ListView) v.findViewById(R.id.category_one);
		category_two = (ListView) v.findViewById(R.id.category_two);
		category_three = (ListView) v.findViewById(R.id.category_three);

		RspGoodsClassList rspgoodsclasslist = Config.getCategoryInfo();
		if (rspgoodsclasslist != null) {
			layout_category.setVisibility(View.VISIBLE);
			layout_loading.setVisibility(View.GONE);
			layout_loading.postHandle(LoadingView.success);
			setCategoryInfo(rspgoodsclasslist);
		}
	}
	
	boolean isfling;
	
	/**
	 * Fragment中，注册 接收MainActivity的Touch回调的对象
	 * 重写其中的onTouchEvent函数，并进行该Fragment的逻辑处理
	 */
	private MainActivity.MyTouchListener mTouchListener = new MyTouchListener() {

		float startX, startY, offsetX, offsetY;
		
		
		@Override
		public void onTouchEvent(MotionEvent event) {
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				isfling = false;
				startX = event.getX();
				startY = event.getY();
				break;
			case MotionEvent.ACTION_UP:
				offsetX = event.getX() - startX;
				offsetY = event.getY() - startY;
				if (Math.abs(offsetX) > Math.abs(offsetY)) {
					if (offsetX > 20) {
						isfling = true;
						mHiddenAction = AnimationUtils.loadAnimation(mActivity,
								R.anim.slide_out_left);
						if (category_line3.getVisibility() == View.VISIBLE) {
							category_line3.setAnimation(mHiddenAction);
							category_line3.setVisibility(View.GONE);
							twoadapter.setSelectedPosition(-1);
							twoadapter.notifyDataSetChanged();
						} else if (category_line2.getVisibility() == View.VISIBLE) {
							category_line2.setAnimation(mHiddenAction);
							category_line2.setVisibility(View.GONE);
							oneadapter.setSelectedPosition(-1);
							oneadapter.notifyDataSetChanged();
						}
					}
				}
				break;
			default:
				break;
			}
		}
	};

	@Override
	public void onResume() { // 在该Fragment的构造函数中注册mTouchListener的回调
		super.onResume();
		((MainActivity) this.getActivity())
				.registerMyTouchListener(mTouchListener);
	};

	@Override
	public void onPause() {
		super.onPause();
		((MainActivity) this.getActivity())
				.unRegisterMyTouchListener(mTouchListener);
		hideListView();
	}
	
	public void hideListView() {
		oneadapter.selectedPosition = -1;
		oneadapter.notifyDataSetChanged();
		category_line2.setVisibility(View.GONE);
		category_line3.setVisibility(View.GONE);
	}

	CategoryOneListener categoryonelistener = new CategoryOneListener() {

		@Override
		public void getCategoryOneId(final List<GoodsClassify> categorylist) {
			if (categorylist == null) {
				return;
			}
			mShowAction = AnimationUtils.loadAnimation(mActivity,
					R.anim.slide_in_right);
			if (category_line2.getVisibility() == View.GONE) {
				mShowAction.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation arg0) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationRepeat(Animation arg0) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationEnd(Animation arg0) {
						oneadapter.notifyDataSetChanged();
					}
				});
				category_line2.setAnimation(mShowAction);
				category_line2.setVisibility(View.VISIBLE);

			} else {
				category_line3.setVisibility(View.GONE);
				twoadapter.setSelectedPosition(-1);
				twoadapter.notifyDataSetChanged();
			}

			twoadapter = new CategoryNewTwoAdapter(mActivity, categorylist);
			twoadapter.setL(categorytwolistener);
			category_two.setAdapter(twoadapter);
			category_two.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					if (isfling) {
						return;
					}
					twoadapter.setSelectedPosition(position);
					if (category_line3.getVisibility() == View.VISIBLE) {
						twoadapter.notifyDataSetChanged();
					}
					twoadapter.getL().getCategoryTwoId(categorylist.get(position).getSubclassify());
				}
			});
		}
	};

	CategoryTwoListener categorytwolistener = new CategoryTwoListener() {

		@Override
		public void getCategoryTwoId(final List<GoodsClassify> categorylist) {
			if (categorylist == null) {
				return;
			}
			mShowAction = AnimationUtils.loadAnimation(mActivity,
					R.anim.slide_in_right);
			if (category_line3.getVisibility() == View.GONE) {
				mShowAction.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation arg0) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationRepeat(Animation arg0) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationEnd(Animation arg0) {
						twoadapter.notifyDataSetChanged();
					}
				});
				category_line3.setAnimation(mShowAction);
				category_line3.setVisibility(View.VISIBLE);
			}

			threeadapter = new CategoryNewThreeAdapter(
					mActivity, categorylist);
			threeadapter.setL(categorythreelistener);
			category_three.setAdapter(threeadapter);
			category_three.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					if (isfling) {
						return;
					}
					threeadapter.getL().getCategoryThreeId(categorylist.get(position).getClassifyid());
				}
			});
		}
	};

	CategoryThreeListener categorythreelistener = new CategoryThreeListener() {

		@Override
		public void getCategoryThreeId(String categorythreeid) {
			Intent intent = new Intent(mActivity, SearchResultActivity.class);
			intent.putExtra(SearchResultActivity.EXTRA_CATEGORYTHREEID,
					categorythreeid);
			startActivity(intent);
		}
	};

	private void initData() {
		String categorylastpulltime = Config.getCategoryLastpulltime();

		Api.getGoodsClassifyList(mActivity, categorylastpulltime, null,
				new NetCallBack<ServiceResult>() {
					@Override
					public void success(ServiceResult rspData) {
						layout_category.setVisibility(View.VISIBLE);
						layout_loading.setVisibility(View.GONE);
						layout_loading.postHandle(LoadingView.success);
						// 保存最后更新时间
						Config.setCategoryLastpulltime(Utils.getNowTime());

						RspGoodsClassList entity = (RspGoodsClassList) rspData;
						Config.setCategoryInfo(entity);

						setCategoryInfo(entity);
					}

					@Override
					public void failed(String msg) {
						layout_loading.postHandle(LoadingView.network_error);
						layout_loading.setL(new RequestWebListener() {

							@Override
							public void requestWeb() {
								initData();
							}
						});
					}
				}, RspGoodsClassList.class);
	}

	private void setCategoryInfo(RspGoodsClassList entity) {
		
		if (category_one.getAdapter() != null) {
			return;
		}
		
		if (entity.data != null) {
			final List<GoodsClassify> categorylist = entity.data.getClassifylist();

			oneadapter = new CategoryNewOneAdapter(mActivity, categorylist);
			oneadapter.setL(categoryonelistener);
			category_one.setAdapter(oneadapter);
			category_one.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					if (isfling) {
						return;
					}
					oneadapter.setSelectedPosition(position);
					if (category_line2.getVisibility() == View.VISIBLE) {
						oneadapter.notifyDataSetChanged();
					}
					oneadapter.getL().getCategoryOneId(categorylist.get(position).getSubclassify());
				}
			});
		}
	}

	
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.home_homepage_et_input:
			Intent intent = new Intent(mActivity, SearchActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	}
}
