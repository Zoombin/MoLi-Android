package com.imooly.android.ui;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.adapter.SubCategoryGridViewAdapter;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.entity.RspBusinessClassifyList;
import com.imooly.android.entity.RspBusinessClassifyList.ClassifyEntity;
import com.imooly.android.entity.RspBusinessClassifyList.SubClassifyEntity;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.tool.Config;
import com.imooly.android.utils.Utils;
import com.imooly.android.widget.CannotRollGridView;
import com.imooly.android.widget.Toast;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * 实体店分类
 * 
 * @author lsd
 * 
 */
public class StoreCategoryActivity extends BaseActivity implements View.OnClickListener {
	private RelativeLayout ll_title;
	private ImageView iv_back;

	private EditText et_input;
	private LinearLayout gv_storecategory;

	List<ClassifyEntity> tempClassfyList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_storecategory);

		logActivityName(this);

		initView();
		initData();
	}

	private void initView() {
		ll_title = (RelativeLayout) findViewById(R.id.ll_title);

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);

		et_input = (EditText) findViewById(R.id.et_input);
		et_input.setOnClickListener(this);

		gv_storecategory = (LinearLayout) findViewById(R.id.gv_storecategory);
	}

	private void initData() {
		// 获取实体店分类
		List<ClassifyEntity> classifylist = null;
		try {
			classifylist = Config.getBusinessClassList();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (classifylist != null) {
			tempClassfyList = classifylist;
			initView(tempClassfyList);
		}
		// 判断 实体店分类是否有更新
		getBusinessClassifyList();
	}

	private void getBusinessClassifyList() {
		String BusinessClassLastpulltime = Config.getBusinessClassLastpulltime();

		Api.businessClassifyList(this, BusinessClassLastpulltime, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				RspBusinessClassifyList rsp = (RspBusinessClassifyList) rspData;
				if (rsp.data != null && rsp.data.getClassifylist() != null) {
					if (tempClassfyList != null) {
						// tempClassfyList.addAll(rsp.data.getClassifylist());
					} else {
						tempClassfyList = rsp.data.getClassifylist();
					}

					initView(tempClassfyList);

					// 保存最后更新时间
					Config.setBusinessClassLastpulltime(Utils.getNowTime());
					Config.setBusinessClassList(tempClassfyList);
				}
			}

			@Override
			public void failed(String msg) {
				Toast.show(self, msg);
			}
		}, RspBusinessClassifyList.class);
	}

	public void initView(List<ClassifyEntity> classifylist) {
		gv_storecategory.removeAllViews();
		int linesize = 0;
		if ((classifylist.size() % 3) == 0) {
			linesize = classifylist.size() / 3;
		} else {
			linesize = classifylist.size() / 3 + 1;
		}
		for (int i = 0; i < linesize; i++) {
			final View convertView = LayoutInflater.from(self).inflate(R.layout.layout_storecategory_item, null);

			/** 商家分类顶部布局 */
			LinearLayout ll_cateTop = (LinearLayout) convertView.findViewById(R.id.ll_cate);

			/** 商家分类单个布局 */
			final LinearLayout ll_cate1 = (LinearLayout) convertView.findViewById(R.id.ll_cate1);
			ll_cate1.setVisibility(View.INVISIBLE);
			final LinearLayout ll_cate2 = (LinearLayout) convertView.findViewById(R.id.ll_cate2);
			ll_cate2.setVisibility(View.INVISIBLE);
			final LinearLayout ll_cate3 = (LinearLayout) convertView.findViewById(R.id.ll_cate3);
			ll_cate3.setVisibility(View.INVISIBLE);

			/** 商家分类图片、文字的布局 */
			RelativeLayout relativecate_1 = (RelativeLayout) convertView.findViewById(R.id.relativecate_1);
			RelativeLayout relativecate_2 = (RelativeLayout) convertView.findViewById(R.id.relativecate_2);
			RelativeLayout relativecate_3 = (RelativeLayout) convertView.findViewById(R.id.relativecate_3);

			/** 商家分类图片 */
			final ImageView storecategory_1 = (ImageView) convertView.findViewById(R.id.storecategory_1);
			final ImageView storecategory_2 = (ImageView) convertView.findViewById(R.id.storecategory_2);
			final ImageView storecategory_3 = (ImageView) convertView.findViewById(R.id.storecategory_3);
			storecategory_1.setSelected(false);
			storecategory_2.setSelected(false);
			storecategory_3.setSelected(false);

			/** 商家分类白色文字 */
			TextView category_txt_1 = (TextView) convertView.findViewById(R.id.category_txt_1);
			TextView category_txt_2 = (TextView) convertView.findViewById(R.id.category_txt_2);
			TextView category_txt_3 = (TextView) convertView.findViewById(R.id.category_txt_3);

			/** 商家分类向下箭头 */
			final ImageView arrow_1 = (ImageView) convertView.findViewById(R.id.arrow_1);
			arrow_1.setVisibility(View.GONE);
			final ImageView arrow_2 = (ImageView) convertView.findViewById(R.id.arrow_2);
			arrow_2.setVisibility(View.GONE);
			final ImageView arrow_3 = (ImageView) convertView.findViewById(R.id.arrow_3);
			arrow_3.setVisibility(View.GONE);

			/** 商家分类 底部Grid */
			final CannotRollGridView gv_subcategory = (CannotRollGridView) convertView.findViewById(R.id.gv_subcategory);
			gv_subcategory.setVisibility(View.GONE);

			/** 遮罩层 */
			final View up_shade_1 = convertView.findViewById(R.id.up_shade_1);
			up_shade_1.setVisibility(View.INVISIBLE);
			final View up_shade_2 = convertView.findViewById(R.id.up_shade_2);
			up_shade_2.setVisibility(View.INVISIBLE);
			final View up_shade_3 = convertView.findViewById(R.id.up_shade_3);
			up_shade_3.setVisibility(View.INVISIBLE);
			final View down_shade_1 = convertView.findViewById(R.id.down_shade_1);
			down_shade_1.setVisibility(View.INVISIBLE);
			final View down_shade_2 = convertView.findViewById(R.id.down_shade_2);
			down_shade_2.setVisibility(View.INVISIBLE);
			final View down_shade_3 = convertView.findViewById(R.id.down_shade_3);
			down_shade_3.setVisibility(View.INVISIBLE);

			for (int j = 0; j < 3; j++) {
				int index = i * 3 + j;
				if (index > classifylist.size() - 1) {
					continue;
				}
				final ClassifyEntity classify = classifylist.get(index);
				switch (j) {
				case 0:
					ll_cate1.setVisibility(View.VISIBLE);
					ImageLoader.getInstance().displayImage(classify.getClassifyicon(), storecategory_1);
					category_txt_1.setText(classify.getClassifyname());

					storecategory_1.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							String parent_id = classify.getClassifyid();
							List<SubClassifyEntity> subList = classify.getSubclassify();
							if (subList == null || subList.size() == 0) {
								Intent intent = new Intent(self, StoreSearchResultActivity.class);
								intent.putExtra(StoreSearchResultActivity.SEARCH_ID, classify.getClassifyid());
								intent.putExtra(StoreSearchResultActivity.PARENT_ID, classify.getClassifyid());
								intent.putExtra(StoreSearchResultActivity.ENTRY_TYPE, "category_search");
								startActivity(intent);
								return;
							}

							if (v.isSelected() && gv_subcategory.getVisibility() == View.VISIBLE) {
								gv_subcategory.setVisibility(View.GONE);

								storecategory_1.setSelected(false);

								arrow_1.setVisibility(View.GONE);
								arrow_2.setVisibility(View.GONE);
								arrow_3.setVisibility(View.GONE);

								ll_cate1.setBackgroundResource(R.drawable.storecate_bg_normal);
								ll_cate2.setBackgroundResource(R.drawable.storecate_bg_normal);
								ll_cate3.setBackgroundResource(R.drawable.storecate_bg_normal);

								up_shade_1.setVisibility(View.INVISIBLE);
								up_shade_2.setVisibility(View.INVISIBLE);
								up_shade_3.setVisibility(View.INVISIBLE);
								down_shade_1.setVisibility(View.INVISIBLE);
								down_shade_2.setVisibility(View.INVISIBLE);
								down_shade_3.setVisibility(View.INVISIBLE);
							} else {
								resetView();
								
								gv_subcategory.setAdapter(new SubCategoryGridViewAdapter(self, parent_id, subList));
								gv_subcategory.setVisibility(View.VISIBLE);

								storecategory_1.setSelected(true);
								storecategory_2.setSelected(false);
								storecategory_3.setSelected(false);

								// 改变箭头
								arrow_1.setVisibility(View.VISIBLE);
								arrow_2.setVisibility(View.GONE);
								arrow_3.setVisibility(View.GONE);

								// 改变单个布局的背景
								ll_cate1.setBackgroundResource(R.drawable.storecate_bg_pressed_v2);
								ll_cate2.setBackgroundResource(R.drawable.storecate_bg_normal);
								ll_cate3.setBackgroundResource(R.drawable.storecate_bg_normal);

								// 更改遮罩
								up_shade_1.setVisibility(View.VISIBLE);
								up_shade_2.setVisibility(View.INVISIBLE);
								up_shade_3.setVisibility(View.INVISIBLE);
								down_shade_1.setVisibility(View.VISIBLE);
								down_shade_2.setVisibility(View.INVISIBLE);
								down_shade_3.setVisibility(View.INVISIBLE);
							}
						}
					});
					break;
				case 1:
					ll_cate2.setVisibility(View.VISIBLE);
					ImageLoader.getInstance().displayImage(classify.getClassifyicon(), storecategory_2);
					category_txt_2.setText(classify.getClassifyname());

					storecategory_2.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							String parent_id = classify.getClassifyid();
							List<SubClassifyEntity> subList = classify.getSubclassify();
							if (subList == null || subList.size() == 0) {
								Intent intent = new Intent(self, StoreSearchResultActivity.class);
								intent.putExtra(StoreSearchResultActivity.SEARCH_ID, classify.getClassifyid());
								intent.putExtra(StoreSearchResultActivity.PARENT_ID, classify.getClassifyid());
								intent.putExtra(StoreSearchResultActivity.ENTRY_TYPE, "category_search");
								startActivity(intent);
								return;
							}
							if (v.isSelected() && gv_subcategory.getVisibility() == View.VISIBLE) {
								gv_subcategory.setVisibility(View.GONE);

								storecategory_2.setSelected(false);

								arrow_1.setVisibility(View.GONE);
								arrow_2.setVisibility(View.GONE);
								arrow_3.setVisibility(View.GONE);

								ll_cate1.setBackgroundResource(R.drawable.storecate_bg_normal);
								ll_cate2.setBackgroundResource(R.drawable.storecate_bg_normal);
								ll_cate3.setBackgroundResource(R.drawable.storecate_bg_normal);

								up_shade_1.setVisibility(View.INVISIBLE);
								up_shade_2.setVisibility(View.INVISIBLE);
								up_shade_3.setVisibility(View.INVISIBLE);
								down_shade_1.setVisibility(View.INVISIBLE);
								down_shade_2.setVisibility(View.INVISIBLE);
								down_shade_3.setVisibility(View.INVISIBLE);
							} else {
								resetView();
								
								gv_subcategory.setAdapter(new SubCategoryGridViewAdapter(self, parent_id, subList));
								gv_subcategory.setVisibility(View.VISIBLE);

								storecategory_1.setSelected(false);
								storecategory_2.setSelected(true);
								storecategory_3.setSelected(false);

								// 改变箭头
								arrow_1.setVisibility(View.GONE);
								arrow_2.setVisibility(View.VISIBLE);
								arrow_3.setVisibility(View.GONE);

								// 改变单个布局的背景
								ll_cate1.setBackgroundResource(R.drawable.storecate_bg_normal);
								ll_cate2.setBackgroundResource(R.drawable.storecate_bg_pressed_v2);
								ll_cate3.setBackgroundResource(R.drawable.storecate_bg_normal);

								// 更改遮罩
								up_shade_1.setVisibility(View.INVISIBLE);
								up_shade_2.setVisibility(View.VISIBLE);
								up_shade_3.setVisibility(View.INVISIBLE);
								down_shade_1.setVisibility(View.INVISIBLE);
								down_shade_2.setVisibility(View.VISIBLE);
								down_shade_3.setVisibility(View.INVISIBLE);
							}
						}
					});
					break;
				case 2:
					ll_cate3.setVisibility(View.VISIBLE);
					ImageLoader.getInstance().displayImage(classify.getClassifyicon(), storecategory_3);
					category_txt_3.setText(classify.getClassifyname());

					storecategory_3.setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							String parent_id = classify.getClassifyid();
							List<SubClassifyEntity> subList = classify.getSubclassify();
							if (subList == null || subList.size() == 0) {
								Intent intent = new Intent(self, StoreSearchResultActivity.class);
								intent.putExtra(StoreSearchResultActivity.SEARCH_ID, classify.getClassifyid());
								intent.putExtra(StoreSearchResultActivity.PARENT_ID, classify.getClassifyid());
								intent.putExtra(StoreSearchResultActivity.ENTRY_TYPE, "category_search");
								startActivity(intent);
								return;
							}
							
							if (v.isSelected() && gv_subcategory.getVisibility() == View.VISIBLE) {
								gv_subcategory.setVisibility(View.GONE);

								storecategory_3.setSelected(false);

								arrow_1.setVisibility(View.GONE);
								arrow_2.setVisibility(View.GONE);
								arrow_3.setVisibility(View.GONE);

								ll_cate1.setBackgroundResource(R.drawable.storecate_bg_normal);
								ll_cate2.setBackgroundResource(R.drawable.storecate_bg_normal);
								ll_cate3.setBackgroundResource(R.drawable.storecate_bg_normal);

								up_shade_1.setVisibility(View.INVISIBLE);
								up_shade_2.setVisibility(View.INVISIBLE);
								up_shade_3.setVisibility(View.INVISIBLE);
								down_shade_1.setVisibility(View.INVISIBLE);
								down_shade_2.setVisibility(View.INVISIBLE);
								down_shade_3.setVisibility(View.INVISIBLE);
							} else {
								resetView();
								
								gv_subcategory.setAdapter(new SubCategoryGridViewAdapter(self, parent_id, subList));
								gv_subcategory.setVisibility(View.VISIBLE);

								storecategory_1.setSelected(false);
								storecategory_2.setSelected(false);
								storecategory_3.setSelected(true);

								// 改变箭头
								arrow_1.setVisibility(View.GONE);
								arrow_2.setVisibility(View.GONE);
								arrow_3.setVisibility(View.VISIBLE);

								// 改变单个布局的背景
								ll_cate1.setBackgroundResource(R.drawable.storecate_bg_normal);
								ll_cate2.setBackgroundResource(R.drawable.storecate_bg_normal);
								ll_cate3.setBackgroundResource(R.drawable.storecate_bg_pressed_v2);

								// 更改遮罩
								up_shade_1.setVisibility(View.INVISIBLE);
								up_shade_2.setVisibility(View.INVISIBLE);
								up_shade_3.setVisibility(View.VISIBLE);
								down_shade_1.setVisibility(View.INVISIBLE);
								down_shade_2.setVisibility(View.INVISIBLE);
								down_shade_3.setVisibility(View.VISIBLE);
							}
						}
					});
					break;
				default:
					break;
				}
			}

			gv_storecategory.addView(convertView);
		}
	}

	/***
	 * 恢复
	 * 
	 * 可以再优化
	 * 
	 * @param convertView
	 */
	private void resetView() {
		int count = gv_storecategory.getChildCount();
		if (count > 0) {
			for (int i = 0; i < count; i++) {
				View convertView = gv_storecategory.getChildAt(i);

				/** 商家分类单个布局 */
				LinearLayout ll_cate1 = (LinearLayout) convertView.findViewById(R.id.ll_cate1);
				LinearLayout ll_cate2 = (LinearLayout) convertView.findViewById(R.id.ll_cate2);
				LinearLayout ll_cate3 = (LinearLayout) convertView.findViewById(R.id.ll_cate3);
				
				ll_cate1.setBackgroundResource(R.drawable.storecate_bg_normal);
				ll_cate2.setBackgroundResource(R.drawable.storecate_bg_normal);
				ll_cate3.setBackgroundResource(R.drawable.storecate_bg_normal);
				
				/** 商家分类向下箭头 */
				ImageView arrow_1 = (ImageView) convertView.findViewById(R.id.arrow_1);
				arrow_1.setVisibility(View.GONE);
				ImageView arrow_2 = (ImageView) convertView.findViewById(R.id.arrow_2);
				arrow_2.setVisibility(View.GONE);
				ImageView arrow_3 = (ImageView) convertView.findViewById(R.id.arrow_3);
				arrow_3.setVisibility(View.GONE);
				
				/** 商家分类 底部Grid */
				CannotRollGridView gv_subcategory = (CannotRollGridView) convertView.findViewById(R.id.gv_subcategory);
				gv_subcategory.setVisibility(View.GONE);

				/** 遮罩层 */
				View up_shade_1 = convertView.findViewById(R.id.up_shade_1);
				up_shade_1.setVisibility(View.INVISIBLE);
				View up_shade_2 = convertView.findViewById(R.id.up_shade_2);
				up_shade_2.setVisibility(View.INVISIBLE);
				View up_shade_3 = convertView.findViewById(R.id.up_shade_3);
				up_shade_3.setVisibility(View.INVISIBLE);
				View down_shade_1 = convertView.findViewById(R.id.down_shade_1);
				down_shade_1.setVisibility(View.INVISIBLE);
				View down_shade_2 = convertView.findViewById(R.id.down_shade_2);
				down_shade_2.setVisibility(View.INVISIBLE);
				View down_shade_3 = convertView.findViewById(R.id.down_shade_3);
				down_shade_3.setVisibility(View.INVISIBLE);
			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.iv_back:
			finish();
			break;
		case R.id.et_input:
			startActivity(new Intent(self, StoreSearchActivity.class));
			break;
		}
	}
}
