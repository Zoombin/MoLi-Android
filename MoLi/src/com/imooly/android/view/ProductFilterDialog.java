package com.imooly.android.view;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.adapter.ProdectSelectFilterAdapter;
import com.imooly.android.adapter.ProdectSelectFilterAdapter.ItemClick;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.entity.RspAreaGood;
import com.imooly.android.entity.RspAreaGood.CityEty;
import com.imooly.android.entity.RspAreaGood.ProvinceEty;
import com.imooly.android.entity.RspGoodsSearch.SpecEty;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.view.AddressFilterDialogTwo.AddressTwoFilterCallBack;
import com.imooly.android.widget.CannotRollGridView;
import com.imooly.android.widget.HorizontalListView;
import com.imooly.android.widget.Toast;
import com.imooly.android.widget.TouchCloseDialog;
import com.imooly.android.widget.TouchCloseLinerLayout;

/**
 * 产品筛选对话框
 * 
 * @author 烨
 * 
 */
public class ProductFilterDialog implements OnClickListener {
	private TouchCloseLinerLayout layout_dialog_productfilter;
	public List<SpecEty> specs;
	public List<ProvinceEty> cityList;
	public TouchCloseDialog dialog;
	private BaseActivity activity;
	private ProductFilterCallBack callBack;
	private View view;

	private LinearLayout layout_price_filter;// 价格筛选

	private LinearLayout layout_filter_has_goods;
	private LinearLayout layout_filter_has_voucher;
	
	private RelativeLayout address_layout;//发货地
	private TextView address_select;

	private LinearLayout lv_filter;
	private Button bottom_clear_btn;
	private Button bottom_submit_btn;
	private EditText priceFrom, priceTo;

	private String tempprice="";
	private int tempstockflag = 1;
	private int tempvoucherflag = 0;
	private String tempaddress="";
	private String tempspces[];

	public interface ProductFilterCallBack {
		void onSelect(SelectModel model);
	}

	public static class SelectModel {
		public String price="";// 0: 无限制 ，其他价格区间
		public int stockflag; // 是否仅显示有货商品 0不限 1是
		public int voucherflag; // 是否仅显示能使用代金券商品 0不限 1是
		public String address;//发货地pid:0877;cid:887;
		public String spec;// 属性
	}

	public ProductFilterDialog(final BaseActivity activity, List<SpecEty> specs, List<String> pricelist,ProductFilterCallBack callBack) {
		this.activity = activity;
		this.callBack = callBack;
		this.specs = specs;

		getCityList();

		LayoutInflater mInflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		view = mInflater.inflate(R.layout.dialog_productfilter, null);
		
		layout_dialog_productfilter = (TouchCloseLinerLayout) view.findViewById(R.id.layout_dialog_productfilter);

		priceFrom = (EditText) view.findViewById(R.id.filter_price_from);
		priceTo = (EditText) view.findViewById(R.id.filter_price_to);

		layout_price_filter = (LinearLayout) view.findViewById(R.id.layout_price_filter);
		View priceHListLayout = mInflater.inflate(R.layout.view_filter_price_glist, null);
		CannotRollGridView priceGlist = (CannotRollGridView) priceHListLayout.findViewById(R.id.comm_glist);
		final ProdectSelectFilterAdapter priceAdapter = new ProdectSelectFilterAdapter();
		priceGlist.setAdapter(priceAdapter);
		priceAdapter.setOnItemClick(new ItemClick() {
			@Override
			public void onClick(int position) {
				// TODO Auto-generated method stub
				String string = (String) priceAdapter.getItem(position);
				if("不限".equals(string)){
					tempprice = "";
				}else{
					tempprice = string;
				}
				priceAdapter.changeSelect(position);
				
				if(!TextUtils.isEmpty(string) && string.contains("-")){
					//填充上面的价格区间
					String [] priceSpace = string.split("-");
					if(priceSpace.length == 2){
						priceFrom.setText(priceSpace[0]);
						priceTo.setText(priceSpace[1]);
					}
				}else{
					priceFrom.setText("");
					priceTo.setText("");
				}
			}
		});
		if(pricelist != null && pricelist.size()>0){
			pricelist.add(0, "不限");
		}
		priceAdapter.setData(pricelist);
		layout_price_filter.setTag(priceAdapter);
		layout_price_filter.addView(priceHListLayout);

		// 有货 或 可用代金券
		layout_filter_has_goods = (LinearLayout) view.findViewById(R.id.layout_filter_has_goods);
		layout_filter_has_goods.setOnClickListener(this);
		layout_filter_has_voucher = (LinearLayout) view.findViewById(R.id.layout_filter_has_voucher);
		layout_filter_has_voucher.setOnClickListener(this);
		
		//发货地
		address_layout = (RelativeLayout) view.findViewById(R.id.address_layout);
		address_select = (TextView) view.findViewById(R.id.address_select);
		address_layout.setOnClickListener(this);

		// 其他选项
		lv_filter = (LinearLayout) view.findViewById(R.id.lv_filter);
		for (int i = 0; i < specs.size(); i++) {
			tempspces = new String[specs.size()];
			SpecEty spec = specs.get(i);
			View childLayout = mInflater.inflate(R.layout.filter_item, null);
			final TextView tv_title = (TextView) childLayout.findViewById(R.id.tv_title);
			final TextView tv_select = (TextView) childLayout.findViewById(R.id.tv_select);
			final ImageView arrow_right = (ImageView) childLayout.findViewById(R.id.arrow_right);
			final LinearLayout ll_sub_layout = (LinearLayout) childLayout.findViewById(R.id.ll_sub_item);

			tv_title.setText(spec.getName());
			// 子选项
			List<String> subDatas = spec.getList();
			if (subDatas != null && subDatas.size() > 0) {
				subDatas.add(0, "不限");
				View subChildLayout = mInflater.inflate(R.layout.layout_product_filter_glist, null);
				CannotRollGridView subList = (CannotRollGridView) subChildLayout.findViewById(R.id.comm_glist);
				final ProdectSelectFilterAdapter subAdapter = new ProdectSelectFilterAdapter();
				subList.setAdapter(subAdapter);
				subList.setTag(i);
				subList.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						// TODO Auto-generated method stub
						int index = (Integer) parent.getTag();
						String selectTxt = (String) subAdapter.getItem(position);
						tv_select.setText(selectTxt);

						String selectSpec = tv_title.getText().toString() + ":" + selectTxt;
						if(position !=0){
							//第一个位置是“不限”，所以不需要加入
							tempspces[index] = selectSpec;
						}else{
							tempspces[index] = "";
						}
						subAdapter.changeSelect(position);
					}
				});
				subAdapter.setData(subDatas);
				childLayout.setTag(subAdapter);
				ll_sub_layout.addView(subChildLayout);
			}

			childLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if (ll_sub_layout.getVisibility() == View.VISIBLE) {
						arrow_right.setImageResource(R.drawable.ic_arrow_right);
						ll_sub_layout.setVisibility(View.GONE);
					} else {
						arrow_right.setImageResource(R.drawable.ic_arrow_down);
						ll_sub_layout.setVisibility(View.VISIBLE);
					}
				}
			});

			lv_filter.addView(childLayout);
		}

		bottom_clear_btn = (Button) view.findViewById(R.id.bottom_clear_btn);
		bottom_submit_btn = (Button) view.findViewById(R.id.bottom_submit_btn);
		bottom_clear_btn.setOnClickListener(this);
		bottom_submit_btn.setOnClickListener(this);

		dialog = new TouchCloseDialog(activity, R.style.FilterDialogStyleBottom);
		dialog.setContentView(view);
		layout_dialog_productfilter.setDialog(dialog);
		// 默认选中仅显示有货商品
		clickStock(tempstockflag);
	}

	private void clickStock(int tempstockflag) {
		for (int i = 0; i < layout_filter_has_goods.getChildCount(); i++) {
			View child = layout_filter_has_goods.getChildAt(i);
			child.setSelected(tempstockflag == 1 ? true : false);
		}
	}
	
	private void clickVoucher(int tempvoucherflag) {
		for (int i = 0; i < layout_filter_has_voucher.getChildCount(); i++) {
			View child = layout_filter_has_voucher.getChildAt(i);
			child.setSelected(tempvoucherflag == 1 ? true : false);
		}
	}
	
	private ProvinceEty tempProvince;
	private String pid = "";
	private CityEty tempRegion;
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bottom_submit_btn:
			submitSelect();
			break;
		case R.id.bottom_clear_btn:
			cleanSelect();
			break;
		case R.id.layout_filter_has_goods:
			tempstockflag = (tempstockflag == 0 ? 1: 0);
			clickStock(tempstockflag);
			
			break;
		case R.id.layout_filter_has_voucher:
			tempvoucherflag = (tempvoucherflag == 0 ? 1: 0);
			clickVoucher(tempvoucherflag);

			break;
		case R.id.address_layout:
			if(cityList != null) {
				ProvinceEty provinceEty  = cityList.get(0);
				if(!"654321".equals(provinceEty.getPid())){
					provinceEty = new ProvinceEty();
					provinceEty.setName("不限");
					provinceEty.setPid("654321");
					cityList.add(0, provinceEty);
				}
				new AddressFilterDialogTwo().show(activity, "province", cityList, new AddressTwoFilterCallBack() {
					@Override
					public void onSelect(Object model) {
						ProvinceEty ent = (ProvinceEty) model;
						if ("654321".equals(ent.getPid())) {
							address_select.setText("不限");
							tempaddress = "";
						} else {
							tempProvince = ent;
							pid = ent.getPid();
							new AddressFilterDialogTwo().show(activity, "region", getCitys(pid), new AddressTwoFilterCallBack() {
								@Override
								public void onSelect(Object model) {
									CityEty ent = (CityEty) model;
									tempRegion = ent;
									address_select.setText(tempProvince.getName() + tempRegion.getName());
								}
							});
						}
					}
				});
			}else{
				Toast.show(activity, "正在获取数据，请稍后再试");
				getCityList();
			}
			break;
		default:
			break;
		}
	}

	private List<CityEty> getCitys(String pid) {
		ProvinceEty aEty = null;
		if (cityList != null && cityList.size() > 0) {
			for (ProvinceEty area : cityList) {
				if (pid.equals(area.getPid())) {
					aEty = area;
					break;
				}
			}
			if (aEty != null) {
				return aEty.getCity();
			}
		}
		return null;
	}
	
	private void cleanSelect() {
		// 价格筛选
		priceFrom.setText("");
		priceTo.setText("");
		tempprice = "";

		ProdectSelectFilterAdapter priceAdapter = (ProdectSelectFilterAdapter) layout_price_filter.getTag();
		if (priceAdapter != null) {
			priceAdapter.cleanSelect();
		}

		// 默认选中仅显示有货商品
		tempstockflag = 1;
		tempvoucherflag = 0;
		clickStock(tempstockflag);
		clickVoucher(tempvoucherflag);
		
		//地址
		address_select.setText("不限");
		tempaddress = "";

		// 其他选项
		int cout = lv_filter.getChildCount();
		for (int i = 0; i < cout; i++) {
			View view = lv_filter.getChildAt(i);
			TextView tv_select = (TextView) view.findViewById(R.id.tv_select);
			tv_select.setText("不限");

			ProdectSelectFilterAdapter subAdapter = (ProdectSelectFilterAdapter) view.getTag();
			if (subAdapter != null) {
				subAdapter.cleanSelect();
			}
		}
		tempspces = new String[cout];

	}

	private void submitSelect() {
		dialog.dismiss();
		// 价格选择
		SelectModel model = new SelectModel();
		String f = priceFrom.getText().toString();
		String t = priceTo.getText().toString();
		if (!TextUtils.isEmpty(f) && !TextUtils.isEmpty(t)) {
			model.price = f + "-" + t;
		} else {
			model.price = tempprice;
		}

//		// 单选
//		model.single = tempsingle;
//		Log.i("Tag", tempsingle + "");
		
		//地址
		model.address = tempaddress;

		// 属性
		String specT = "";
		if (tempspces != null && tempspces.length > 0) {
			for (String string : tempspces) {
				if (!TextUtils.isEmpty(string)) {
					specT += string + ";";
				}
			}
			if(specT.length()>1){
				//去掉最后一  ; 号
				specT = specT.substring(0, specT.length()-1);
			}
			model.spec = specT;
		} else {
			model.spec = "";
		}
		
		//有货商品
		model.stockflag = tempstockflag;
		
		//有代金券
		model.voucherflag = tempvoucherflag;
		
		Log.i("Tag", model.address);
		Log.i("Tag", model.price);
		Log.i("Tag", model.spec);
		Log.i("Tag", model.stockflag+"");
		Log.i("Tag", model.voucherflag+"");
		
		if (callBack != null) {
			callBack.onSelect(model);
		}
	}

	private void getCityList() {
		Api.goodsarea(activity,new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {
				RspAreaGood rsp = (RspAreaGood) rspData;
				if (rsp.data != null) {
					cityList = rsp.data.getArea();
				}
			}

			@Override
			public void failed(String msg) {
				
			}
		}, RspAreaGood.class);
	}
}
