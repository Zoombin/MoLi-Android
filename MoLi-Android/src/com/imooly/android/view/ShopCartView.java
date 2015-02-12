package com.imooly.android.view;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.entity.ReqCartDeleteGoods;
import com.imooly.android.entity.ReqGoodsInfo;
import com.imooly.android.entity.RspCartSync.Good;
import com.imooly.android.entity.RspCartSync.Goods;
import com.imooly.android.entity.RspSuccessCommon;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.fragment.ShopCartFragment;
import com.imooly.android.ui.ProductDetailActivity;
import com.imooly.android.view.CommonConfirmDialog.CommonAlertCallBack;
import com.imooly.android.widget.NoDataView;
import com.imooly.android.widget.Toast;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.socialize.net.r;

public class ShopCartView {
	Context context;
	LinearLayout rootListContentView;
	TextView rootAllPrice;
	Button rootCommitBtn;
	LinearLayout rootShopCarLayout;
	NoDataView rootNoDataView;
	LinearLayout rootCheckLayout;
	CheckBox rootCheckAll;

	List<Goods> goodslist;

	/***
	 * 
	 * @param context
	 * @param rootShopCarLayout
	 *            列表内容布局
	 * @param rootListContentView
	 *            加载列表的View
	 * @param rootNoDataView
	 *            没有数据的view
	 * @param rootAllPrice
	 *            底部所有价格
	 * @param rootCommitBtn
	 *            底部提交按钮
	 * @param rootCheckAll
	 *            底部全选按钮
	 * @param goodslist
	 */
	public ShopCartView(Context context, LinearLayout rootShopCarLayout, LinearLayout rootListContentView, NoDataView rootNoDataView,
			TextView rootAllPrice, Button rootCommitBtn, LinearLayout rootCheckLayout, CheckBox rootCheckAll, List<Goods> goodslist) {
		this.context = context;
		this.rootShopCarLayout = rootShopCarLayout;
		this.rootListContentView = rootListContentView;
		this.rootNoDataView = rootNoDataView;
		this.rootAllPrice = rootAllPrice;
		this.rootCommitBtn = rootCommitBtn;
		this.rootCheckLayout = rootCheckLayout;
		this.rootCheckAll = rootCheckAll;

		this.goodslist = goodslist;
		setData(goodslist);
	}

	public void setData(List<Goods> datas) {
		if (rootListContentView.getChildCount() > 0) {
			rootListContentView.removeAllViews();
		}
		for (int i = 0; i < datas.size(); i++) {
			Goods ety = datas.get(i);
			View view = getItem(ety);
			rootListContentView.addView(view);
		}
	}

	// 编辑
	public void edit() {
		for (int i = 0; i < rootListContentView.getChildCount(); i++) {
			View item = rootListContentView.getChildAt(i);
			LinearLayout shopcar_subitem_layout = (LinearLayout) item.findViewById(R.id.shopcar_subitem_layout);
			for (int j = 0; j < shopcar_subitem_layout.getChildCount(); j++) {
				View view = shopcar_subitem_layout.getChildAt(j);
				TextView shopcar_subitem_content = (TextView) view.findViewById(R.id.shopcar_subitem_content);
				LinearLayout shopcar_subitem_mlayout = (LinearLayout) view.findViewById(R.id.shopcar_subitem_mlayout);
				CheckBox shopcar_subitem_check = (CheckBox) view.findViewById(R.id.shopcar_subitem_check);
				ImageView shopcar_subitem_invalid = (ImageView) view.findViewById(R.id.shopcar_subitem_invalid);

				// 初始化勾选的数据
				shopcar_subitem_check.setChecked(false);

				Good good = (Good) view.getTag();
				if (ShopCartFragment.isEdit) {
					shopcar_subitem_content.setVisibility(View.GONE);
					shopcar_subitem_mlayout.setVisibility(View.VISIBLE);
					if (good.getOnsale() != 1) {
						// 失效商品
						shopcar_subitem_check.setVisibility(View.VISIBLE);
						shopcar_subitem_invalid.setVisibility(View.GONE);
					}
				} else {
					shopcar_subitem_content.setVisibility(View.VISIBLE);
					shopcar_subitem_mlayout.setVisibility(View.GONE);
					if (good.getOnsale() != 1) {
						// 失效商品
						shopcar_subitem_check.setVisibility(View.GONE);
						shopcar_subitem_invalid.setVisibility(View.VISIBLE);
					}
				}
			}
		}
	}

	// 全选
	public void check(boolean check) {
		for (int i = 0; i < rootListContentView.getChildCount(); i++) {
			View item = rootListContentView.getChildAt(i);
			CheckBox shopcar_item_check = (CheckBox) item.findViewById(R.id.shopcar_item_check);
			shopcar_item_check.setChecked(check);
			
			LinearLayout shop_item_title_layout = (LinearLayout) item.findViewById(R.id.shop_item_title_layout);
			shop_item_title_layout.setSelected(check);
			
			LinearLayout shopcar_subitem_layout = (LinearLayout) item.findViewById(R.id.shopcar_subitem_layout);
			for (int j = 0; j < shopcar_subitem_layout.getChildCount(); j++) {
				View subView = shopcar_subitem_layout.getChildAt(j);
				CheckBox shopcar_subitem_check = (CheckBox) subView.findViewById(R.id.shopcar_subitem_check);
				shopcar_subitem_check.setChecked(check);
			}
		}
	}

	private View getItem(Goods ety) {
		View item = LayoutInflater.from(context).inflate(R.layout.layout_shopcart_item, null);
		final LinearLayout shop_item_title_layout = (LinearLayout) item.findViewById(R.id.shop_item_title_layout);
		final LinearLayout shopcar_subitem_layout = (LinearLayout) item.findViewById(R.id.shopcar_subitem_layout);
		TextView shopcar_item_name = (TextView) item.findViewById(R.id.shopcar_item_name);
		CheckBox shopcar_item_check = (CheckBox) item.findViewById(R.id.shopcar_item_check);

		// 旗舰店名称
		shopcar_item_name.setText(ety.getStorename());
		
		// 商品列表
		List<Good> subEties = ety.getGoods();
		for (int j = 0; j < subEties.size(); j++) {
			Good subEty = subEties.get(j);
			View view = getSubItem(shop_item_title_layout,shopcar_item_check, shopcar_subitem_layout, subEty);
			shopcar_subitem_layout.addView(view);
		}
		
		//用点击事件来改变状态
		shop_item_title_layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(v.isSelected()){
					shop_item_title_layout.setSelected(false);
					for (int i = 0; i < shopcar_subitem_layout.getChildCount(); i++) {
						View subView = shopcar_subitem_layout.getChildAt(i);
						CheckBox shopcar_subitem_check = (CheckBox) subView.findViewById(R.id.shopcar_subitem_check);
						shopcar_subitem_check.setChecked(false);
					}
				}else{
					shop_item_title_layout.setSelected(true);
					for (int i = 0; i < shopcar_subitem_layout.getChildCount(); i++) {
						View subView = shopcar_subitem_layout.getChildAt(i);
						CheckBox shopcar_subitem_check = (CheckBox) subView.findViewById(R.id.shopcar_subitem_check);
						shopcar_subitem_check.setChecked(true);
					}
				}
			}
		});

		return item;
	}

	@SuppressLint("NewApi")
	private View getSubItem(final LinearLayout shop_item_title_layout,final CheckBox shopcar_item_check, final LinearLayout shopcar_subitem_layout, final Good subEty) {
		final View subitem = LayoutInflater.from(context).inflate(R.layout.layout_shopcart_subitem, null);
		LinearLayout layout_subitem = (LinearLayout) subitem.findViewById(R.id.layout_subitem);
		layout_subitem.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!ShopCartFragment.isEdit) {
					// 商品详情
					Intent intent = new Intent();
					intent.putExtra(ProductDetailActivity.EXTRA_GOODSID, subEty.getGoodsid());
					intent.setClass(context, ProductDetailActivity.class);
					context.startActivity(intent);
				}
			}
		});
		CheckBox shopcar_subitem_check = (CheckBox) subitem.findViewById(R.id.shopcar_subitem_check);
		ImageView shopcar_subitem_invalid = (ImageView) subitem.findViewById(R.id.shopcar_subitem_invalid);
		ImageView shopcar_subitem_pic = (ImageView) subitem.findViewById(R.id.shopcar_subitem_pic);
		TextView shopcar_subitem_content = (TextView) subitem.findViewById(R.id.shopcar_subitem_content);
		LinearLayout shopcar_subitem_mlayout = (LinearLayout) subitem.findViewById(R.id.shopcar_subitem_mlayout);
		LinearLayout layout_sub_content = (LinearLayout) subitem.findViewById(R.id.layout_sub_content);
		ImageView shopcar_subitem_minus = (ImageView) subitem.findViewById(R.id.shopcar_subitem_minus);
		final EditText shopcar_subitem_et_num = (EditText) subitem.findViewById(R.id.shopcar_subitem_et_num);
		ImageView shopcar_subitem_plus = (ImageView) subitem.findViewById(R.id.shopcar_subitem_plus);
		TextView shopcar_subitem_spec = (TextView) subitem.findViewById(R.id.shopcar_subitem_spec);
		TextView shopcar_subitem_price = (TextView) subitem.findViewById(R.id.shopcar_subitem_price);
		final TextView shopcar_subitem_num = (TextView) subitem.findViewById(R.id.shopcar_subitem_num);
		TextView shopcar_subitem_stock = (TextView) subitem.findViewById(R.id.shopcar_subitem_stock);

		shopcar_subitem_content.setText(subEty.getGoodsname());
		ImageLoader.getInstance().displayImage(subEty.goodsimage, shopcar_subitem_pic);
		shopcar_subitem_spec.setText(subEty.getSpecshow());
		shopcar_subitem_price.setText(Float.toString(subEty.getPrice()));
		// 失效
		int onsale = subEty.getOnsale();
		if (1 == onsale) {
			layout_sub_content.setBackground(null);
			shopcar_subitem_invalid.setVisibility(View.GONE);
			shopcar_subitem_check.setVisibility(View.VISIBLE);
		} else {
			layout_sub_content.setBackgroundColor(context.getResources().getColor(R.color.app_bg_color));
			shopcar_subitem_invalid.setVisibility(View.VISIBLE);
			shopcar_subitem_check.setVisibility(View.GONE);
		}

		// 库存
		if (subEty.getStock() == 1) {
			shopcar_subitem_stock.setVisibility(View.GONE);
		} else {
			shopcar_subitem_stock.setVisibility(View.VISIBLE);
			shopcar_subitem_stock.setText("缺货");
		}

		// 设置数目
		SetSubNum(null, subEty.getNum(), shopcar_subitem_et_num, shopcar_subitem_num);

		shopcar_subitem_check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				boolean check = true;
				for (int j = 0; j < shopcar_subitem_layout.getChildCount(); j++) {
					View view = shopcar_subitem_layout.getChildAt(j);
					CheckBox shopcar_subitem_check = (CheckBox) view.findViewById(R.id.shopcar_subitem_check);
					if (!shopcar_subitem_check.isChecked()) {
						check = false;
					}
				}
				shop_item_title_layout.setSelected(check);
				shopcar_item_check.setChecked(check);
				
				
				// 底部总价格
				setTotalPrice();
				// 底部提交按钮的状态
				setCommitBtnStatus();
				// 底部全选状态
				setCheckAllStatus();
			}
		});
		shopcar_subitem_et_num.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				int num = 1;
				String numStr = shopcar_subitem_et_num.getText().toString();
				if (!TextUtils.isEmpty(numStr)) {
					num = Integer.parseInt(numStr);
				}
				if (num > 30) {
					Toast.show(context, "最大数量30");
					return;
				}
				SetSubNum(subEty, num, null, shopcar_subitem_num);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
		shopcar_subitem_minus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int num = Integer.parseInt(shopcar_subitem_et_num.getText().toString());
				if (num > 1) {
					num -= 1;
					SetSubNum(subEty, num, shopcar_subitem_et_num, shopcar_subitem_num);
				} else {
					SetSubNum(subEty, num, shopcar_subitem_et_num, shopcar_subitem_num);
				}
			}
		});
		shopcar_subitem_plus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int num = Integer.parseInt(shopcar_subitem_et_num.getText().toString());
				if (num > 30) {
					Toast.show(context, "最大数量30");
					return;
				}
				num += 1;
				SetSubNum(subEty, num, shopcar_subitem_et_num, shopcar_subitem_num);
			}
		});

		shopcar_subitem_minus.setTag(subEty);
		shopcar_subitem_plus.setTag(subEty);
		subitem.setTag(subEty);
		return subitem;
	}

	/***
	 * 删除失效商品
	 */
	public void deleteInvalidGoods() {
		for (int i = 0; i < rootListContentView.getChildCount(); i++) {
			View item = rootListContentView.getChildAt(i);
			LinearLayout shopcar_subitem_layout = (LinearLayout) item.findViewById(R.id.shopcar_subitem_layout);
			for (int j = 0; j < shopcar_subitem_layout.getChildCount(); j++) {
				View view = shopcar_subitem_layout.getChildAt(j);
				Good good = (Good) view.getTag();
				CheckBox shopcar_subitem_check = (CheckBox) view.findViewById(R.id.shopcar_subitem_check);
				if (good.getOnsale() != 1) {
					shopcar_subitem_check.setChecked(true);
				} else {
					shopcar_subitem_check.setChecked(false);
				}
			}
		}

		deleteShopCart();
	}

	/**
	 * 删除商品
	 */
	public void deleteShopCart() {
		List<ReqCartDeleteGoods> goodList = new ArrayList<ReqCartDeleteGoods>();
		for (int i = 0; i < rootListContentView.getChildCount(); i++) {
			View item = rootListContentView.getChildAt(i);
			LinearLayout shopcar_subitem_layout = (LinearLayout) item.findViewById(R.id.shopcar_subitem_layout);
			for (int j = 0; j < shopcar_subitem_layout.getChildCount(); j++) {
				View view = shopcar_subitem_layout.getChildAt(j);
				CheckBox shopcar_subitem_check = (CheckBox) view.findViewById(R.id.shopcar_subitem_check);
				if (shopcar_subitem_check.isChecked()) {
					Good good = (Good) view.getTag();
					ReqCartDeleteGoods goodtemp = new ReqCartDeleteGoods();
					goodtemp.goodsid = good.goodsid;
					goodtemp.spec = good.spec;
					goodList.add(goodtemp);
				}
			}
		}
		if (goodList.size() == 0) {
			Toast.show(context, "请选择商品");
		} else {
			String goodlist = new Gson().toJson(goodList);
			Api.cartDelete(context, goodlist, new NetCallBack<ServiceResult>() {
				@Override
				public void success(ServiceResult rspData) {
					RspSuccessCommon rsq = (RspSuccessCommon) rspData;
					RspSuccessCommon.Data data = rsq.data;
					if (data.getSuccess() == 0) {
						Toast.show(context, data.getMessage());
						return;
					}
					for (int i = 0; i < rootListContentView.getChildCount(); i++) {
						View item = rootListContentView.getChildAt(i);
						LinearLayout shopcar_subitem_layout = (LinearLayout) item.findViewById(R.id.shopcar_subitem_layout);
						CheckBox shopcar_item_check = (CheckBox) item.findViewById(R.id.shopcar_item_check);
						if (shopcar_item_check.isChecked()) {
							rootListContentView.removeViewAt(i);
							i--;
							continue;
						} else {
							for (int j = 0; j < shopcar_subitem_layout.getChildCount(); j++) {
								View view = shopcar_subitem_layout.getChildAt(j);
								CheckBox shopcar_subitem_check = (CheckBox) view.findViewById(R.id.shopcar_subitem_check);
								if (shopcar_subitem_check.isChecked()) {
									shopcar_subitem_layout.removeViewAt(j);
									j--;
								}
							}
						}
					}
					refrashViewStatu();
					setTotalPrice();
				}

				@Override
				public void failed(String msg) {
					Toast.show(context, msg);
				}
			}, RspSuccessCommon.class);
		}
	}

	/***
	 * 检查是否可以提交
	 * 
	 * @return
	 */
	public boolean checkShopCar() {
		boolean b = true;
		for (int i = 0; i < rootListContentView.getChildCount(); i++) {
			View item = rootListContentView.getChildAt(i);
			LinearLayout shopcar_subitem_layout = (LinearLayout) item.findViewById(R.id.shopcar_subitem_layout);
			for (int j = 0; j < shopcar_subitem_layout.getChildCount(); j++) {
				View view = shopcar_subitem_layout.getChildAt(j);
				Good good = (Good) view.getTag();
				CheckBox shopcar_subitem_check = (CheckBox) view.findViewById(R.id.shopcar_subitem_check);
				if (shopcar_subitem_check.isChecked()) {
					if (good.getStock() != 1) {
						b = false;
						Toast.show(context, "提交商品中有商品库存不足");
						break;
					}

					if (good.getOnsale() != 1) {
						b = false;
						CommonConfirmDialog.show(context, "购物车中有失效商品，是否清空？", "不需要", "清空", new CommonAlertCallBack() {
							@Override
							public void onConfirm() {
								// TODO Auto-generated method stub
								deleteInvalidGoods();
							}

							@Override
							public void onCancel() {
								// TODO Auto-generated method stub
							}
						});
						break;
					}
				}
			}
			if (!b) {
				break;
			}
		}
		return b;
	}

	/**
	 * 获得选中商品信息
	 * 
	 * @return
	 */
	public List<Good> getGoodlist() {
		List<Good> goodslist = new ArrayList<Good>();
		for (int i = 0; i < rootListContentView.getChildCount(); i++) {
			View item = rootListContentView.getChildAt(i);
			LinearLayout shopcar_subitem_layout = (LinearLayout) item.findViewById(R.id.shopcar_subitem_layout);
			for (int j = 0; j < shopcar_subitem_layout.getChildCount(); j++) {
				View view = shopcar_subitem_layout.getChildAt(j);
				CheckBox shopcar_subitem_check = (CheckBox) view.findViewById(R.id.shopcar_subitem_check);
				if (shopcar_subitem_check.isChecked()) {
					goodslist.add((Good) view.getTag());
				}
			}
		}
		return goodslist;
	}

	public void setCommitBtnStatus() {
		if (!ShopCartFragment.isEdit) {
			List<Good> list = getGoodlist();
			if (list != null && list.size() > 0) {
				rootCommitBtn.setBackgroundResource(R.drawable.selector_btn_orange_alpha);
				rootCommitBtn.setText("结算(" + list.size() + ")");
				rootCommitBtn.setClickable(true);
			} else {
				rootCommitBtn.setBackgroundResource(R.color.app_text_gray);
				rootCommitBtn.setText("结算");
				rootCommitBtn.setClickable(false);
			}
		} else {
			rootCommitBtn.setBackgroundResource(R.drawable.selector_btn_orange_alpha);
			rootCommitBtn.setClickable(true);
		}
	}

	public void setCheckAllStatus() {
		boolean check = true;
		for (int i = 0; i < rootListContentView.getChildCount(); i++) {
			View item = rootListContentView.getChildAt(i);
			LinearLayout shopcar_subitem_layout = (LinearLayout) item.findViewById(R.id.shopcar_subitem_layout);
			for (int j = 0; j < shopcar_subitem_layout.getChildCount(); j++) {
				View view = shopcar_subitem_layout.getChildAt(j);
				CheckBox shopcar_subitem_check = (CheckBox) view.findViewById(R.id.shopcar_subitem_check);
				if (!shopcar_subitem_check.isChecked()) {
					check = false;
					break;
				}
			}
			if (!check) {
				break;
			}
		}
		
		rootCheckLayout.setSelected(check);
		rootCheckAll.setChecked(check);
	}

	/**
	 * 设置商品数量
	 * 
	 * @param num
	 * @param shopcar_subitem_et_num
	 * @param shopcar_subitem_num
	 */
	private void SetSubNum(Good subEty, final int num, final EditText shopcar_subitem_et_num, final TextView shopcar_subitem_num) {
		if (subEty != null) {
			List<ReqGoodsInfo> goodslist = new ArrayList<ReqGoodsInfo>();
			ReqGoodsInfo good = new ReqGoodsInfo();
			good.goodsid = subEty.getGoodsid();
			good.spec = subEty.getSpec();
			good.num = num;
			goodslist.add(good);
			String goodlist = new Gson().toJson(goodslist);
			if (shopcar_subitem_et_num != null) {
				shopcar_subitem_et_num.setText(Integer.toString(num));
			}
			shopcar_subitem_num.setText("x" + num);
			setTotalPrice();
			Api.cartUpdate(context, goodlist, new NetCallBack<ServiceResult>() {
				@Override
				public void success(ServiceResult rspData) {
					RspSuccessCommon rsq = (RspSuccessCommon) rspData;
					RspSuccessCommon.Data data = rsq.data;
					if (1 != data.getSuccess() && !TextUtils.isEmpty(data.getMessage())) {
						Toast.show(context, data.getMessage());
						return;
					}
				}

				@Override
				public void failed(String msg) {
					// Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
				}
			}, RspSuccessCommon.class);
		} else {
			// 设置数目
			shopcar_subitem_et_num.setText(Integer.toString(num));
			shopcar_subitem_num.setText("x" + num);
			setTotalPrice();
		}
	}

	private void setTotalPrice() {
		float totalnum = 0.0f;
		for (int i = 0; i < rootListContentView.getChildCount(); i++) {
			View item = rootListContentView.getChildAt(i);
			LinearLayout shopcar_subitem_layout = (LinearLayout) item.findViewById(R.id.shopcar_subitem_layout);
			for (int j = 0; j < shopcar_subitem_layout.getChildCount(); j++) {
				View view = shopcar_subitem_layout.getChildAt(j);
				CheckBox shopcar_subitem_check = (CheckBox) view.findViewById(R.id.shopcar_subitem_check);
				TextView shopcar_subitem_price = (TextView) view.findViewById(R.id.shopcar_subitem_price);
				EditText shopcar_subitem_et_num = (EditText) view.findViewById(R.id.shopcar_subitem_et_num);
				if (shopcar_subitem_check.isChecked()) {
					float price = Float.parseFloat(shopcar_subitem_price.getText().toString())
							* Integer.parseInt(shopcar_subitem_et_num.getText().toString());
					totalnum += price;
				}
			}
		}
		DecimalFormat fnum = new DecimalFormat("##0.00");
		String numString = fnum.format(totalnum);
		rootAllPrice.setText(numString);
	}

	/***
	 * 界面状态
	 */
	public void refrashViewStatu() {
		int count = rootListContentView.getChildCount();
		if (count == 0) {
			rootShopCarLayout.setVisibility(View.GONE);
			rootNoDataView.setVisibility(View.VISIBLE);
			rootNoDataView.postHandle(NoDataView.noshopcart);
		}
	}
}
