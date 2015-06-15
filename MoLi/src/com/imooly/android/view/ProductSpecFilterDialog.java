package com.imooly.android.view;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
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

import com.google.gson.Gson;
import com.imooly.android.MoLiApplication;
import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.adapter.ProdectSpecFilterAdapter;
import com.imooly.android.db.DB_Location;
import com.imooly.android.entity.ReqGoodsInfo;
import com.imooly.android.entity.RspGoodsPrice;
import com.imooly.android.entity.RspGoodsSpec;
import com.imooly.android.entity.RspGoodsSpec.GoodsSpec;
import com.imooly.android.entity.RspOrderMake;
import com.imooly.android.entity.RspSuccessCommon;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.tool.ClickUtil;
import com.imooly.android.ui.FillOrderActivity;
import com.imooly.android.ui.LoginActivity;
import com.imooly.android.utils.Utils;
import com.imooly.android.widget.CannotRollGridView;
import com.imooly.android.widget.CustomDialog;
import com.imooly.android.widget.CustomDialog.CGravity;
import com.imooly.android.widget.Toast;
import com.imooly.android.widget.TouchCloseRelativeLayout;
import com.nostra13.universalimageloader.core.ImageLoader;

/***
 * 
 * 选择商品规格
 * 
 * @author LSD
 * 
 */
public class ProductSpecFilterDialog {
	private TouchCloseRelativeLayout layout_filter_productspec;
	private ProdectSpecFilterCallBack callback;
	private int[] specSelect;
	private LinearLayout ll_price_top;
	private TextView get_voucher;
	private Context ctx;
	private TextView total_price;
	private TextView total_price_top;
	private EditText numEditText;
	String goodsid;
    CustomDialog dialog;
    DecimalFormat fnum = new DecimalFormat("##0.00");
    boolean loadPrice = false;

	public interface ProdectSpecFilterCallBack {
		void onDismiss();

		void onSuccess();
	}

	public class SelectMode {
		public String spec;
		public int num;
	}

	public void show(final Context context, final int from, final String goodsid, RspGoodsSpec goodsSpec,
			final ProdectSpecFilterCallBack callback) {
		this.ctx = context;
		this.callback = callback;
		this.goodsid = goodsid;
		final View view = LayoutInflater.from(context).inflate(R.layout.view_filter_prodectspec_dialog, null);
		if(dialog == null){
			dialog = CustomDialog.create(context, R.style.FilterDialogStyleBottom);
		}
		dialog.setCanceledOnTouchOutside(true);

		final RspGoodsSpec.Data data = goodsSpec.data;

		layout_filter_productspec = (TouchCloseRelativeLayout) view.findViewById(R.id.layout_filter_productspec);
		layout_filter_productspec.setDialog(dialog);
		
		// FindViews
		ImageView prodect_pic = (ImageView) view.findViewById(R.id.prodect_pic);
		TextView prodect_describe = (TextView) view.findViewById(R.id.prodect_describe);
		LinearLayout ll_spec = (LinearLayout) view.findViewById(R.id.ll_spec);
		TextView minus = (TextView) view.findViewById(R.id.minus);
		numEditText = (EditText) view.findViewById(R.id.num);
		TextView plus = (TextView) view.findViewById(R.id.plus);
		get_voucher = (TextView) view.findViewById(R.id.get_voucher);
		total_price = (TextView) view.findViewById(R.id.total_price);
		Button commit_bt = (Button) view.findViewById(R.id.commit_bt);

		ll_price_top = (LinearLayout) view.findViewById(R.id.ll_price_top);
		RelativeLayout confirm_view1 = (RelativeLayout) view.findViewById(R.id.confirm_view1);
		LinearLayout confirm_view2 = (LinearLayout) view.findViewById(R.id.confirm_view2);
		total_price_top = (TextView) view.findViewById(R.id.total_price_top);
		goodprice = Float.parseFloat(goodsSpec.data.getGoodsprice());
		total_price_top.setText("￥" + fnum.format(goodprice));
		TextView tv_buynow = (TextView) view.findViewById(R.id.tv_buynow);
		TextView tv_addtoshop = (TextView) view.findViewById(R.id.tv_addtoshop);

		if (from == 1) {
			commit_bt.setText("确认添加");
		} else if (from == 2) {
			commit_bt.setText("确认购买");
		} else if (from == 0) {
			ll_price_top.setVisibility(View.VISIBLE);
			confirm_view1.setVisibility(View.GONE);
			confirm_view2.setVisibility(View.VISIBLE);
		}

		List<GoodsSpec> specs = data.getGoodsspec();
		if (specs != null && specs.size() > 0) {
			specSelect = new int[specs.size()];
			for (int i = 0; i < specs.size(); i++) {
				specSelect[i] = -1;
				GoodsSpec spec = specs.get(i);
				View subView = LayoutInflater.from(context).inflate(R.layout.view_filter_prodectspec_item, null);
				TextView specName = (TextView) subView.findViewById(R.id.spec_name);

				CannotRollGridView gridView = (CannotRollGridView) subView.findViewById(R.id.spec_values);
				gridView.setTag(i);
				gridView.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						int index = (Integer) parent.getTag();
						specSelect[index] = position;
						((ProdectSpecFilterAdapter) parent.getAdapter()).changeSelect(position);
						calculatePrice(goodsid);
					}
				});

				specName.setText(spec.getName());
				List<String> values = spec.getList();
				ProdectSpecFilterAdapter adapter = new ProdectSpecFilterAdapter();
				gridView.setAdapter(adapter);
				adapter.setData(values);

				ll_spec.addView(subView);
			}
		} else {
			// 没有属性的情况
			specSelect = new int[1];
			specSelect[0] = 0;
			calculatePrice(goodsid);
		}

		// InitData
		prodect_describe.setText(data.getGoodsname());
		ImageLoader.getInstance().displayImage(data.getGoodsimage(), prodect_pic);
		numEditText.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
			}
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				String numStr = arg0.toString();
				if("0".equals(numStr)){
					numEditText.setText("1");
				}
				if(!TextUtils.isEmpty(numStr)){
					int cout = Integer.parseInt(numStr);
					if(stock !=0 && cout > stock){
						Toast.show(context, "最大数量"+stock);
						numEditText.setText(stock+"");
						return;
					}
					total_price.setText("￥" + fnum.format(goodprice*cout));
					total_price_top.setText("￥" + fnum.format(goodprice));
					get_voucher.setText("赠送代金券:" + fnum.format(voucher*cout));
				}
			}
		});
		minus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String count = numEditText.getText().toString();
				if(TextUtils.isEmpty(count)){
					count = "1";
					numEditText.setText("1");
				}
				int min = Integer.parseInt(count);
				if (min > 1) {
					numEditText.setText("" + (min - 1));
				}
				//内容变化是再计算金额 （代码见上↑️）
			}
		});
		plus.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String count = numEditText.getText().toString();
				if(TextUtils.isEmpty(count)){
					count = "0";
				}
				int plu = Integer.parseInt(count);
				numEditText.setText("" + (plu + 1));
				//内容变化是再计算金额 （代码见上↑️）
			}
		});

		commit_bt.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				SelectMode mode = specSelectMthd(dialog);
				if (mode == null) {
					return;
				}
				if (1 == from) {
					addToShopCar(mode, dialog);
				} else if (2 == from) {
					dialog.dismiss();
					confirm(mode);
				}
			}
		});

		tv_buynow.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(ClickUtil.isFastDoubleClick()){
					return;
				}
				SelectMode mode = specSelectMthd(dialog);
				if (mode == null) {
					return;
				}
				dialog.dismiss();
				confirm(mode);
			}
		});

		tv_addtoshop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if(ClickUtil.isFastDoubleClick()){
					return;
				}
				SelectMode mode = specSelectMthd(dialog);
				if (mode == null) {
					return;
				}
				addToShopCar(mode, dialog);
			}
		});

		dialog.setContentVw(view).setGravity(CGravity.CENTER);
		if (!dialog.isShowing()) {
			dialog.show();
		}
	}

	private float goodprice;
	private float voucher;
	private int stock = 0;
	
	private void calculatePrice(String goodsid) {
		boolean calcu = true;
		String goodsspec = "";
		if (specSelect != null && specSelect.length > 0) {
			for (int spec : specSelect) {
				if (spec == -1) {
					// 属性没有选择全
					calcu = false;
					break;
				} else {
					goodsspec += spec + "-";
				}
			}
			if (!calcu) {
				return;
			}
		} else {
			goodsspec = "0";
		}

		if (goodsspec.length() > 1) {
			goodsspec = goodsspec.substring(0, goodsspec.length() - 1);
		}
		loadPrice = true;
		Api.goodsprice(ctx, goodsid, goodsspec, new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {
				loadPrice = false;
				RspGoodsPrice.Data data = ((RspGoodsPrice) rspData).data;
				if (data != null) {
					ll_price_top.setVisibility(View.VISIBLE);
					goodprice = data.getGoodsprice();
					stock = data.getStock();
					String count = numEditText.getText().toString();
					if(TextUtils.isEmpty(count)){
						count = "1";
					}
					int c = Integer.parseInt(count);
					total_price.setText("￥" + fnum.format(goodprice*c));
					total_price_top.setText("￥" + fnum.format(goodprice));
					
					if (data.isvoucher == 0) {
						get_voucher.setVisibility(View.GONE);
					} else {
						voucher = data.getVoucher();
						get_voucher.setVisibility(View.VISIBLE);
						get_voucher.setText("赠送代金券:" + voucher*c);
					}
				}
			}

			@Override
			public void failed(String msg) {
				loadPrice = false;
			}
		}, RspGoodsPrice.class);
	}

	private SelectMode specSelectMthd(CustomDialog dialog) {
		boolean calcu = true;
		String goodsspec = "";
		if (specSelect != null && specSelect.length > 0) {
			for (int spec : specSelect) {
				if (spec == -1) {
					// 属性没有选择全
					calcu = false;
					break;
				} else {
					goodsspec += spec + "-";
				}
			}
			if (!calcu) {
				Toast.show(ctx, "请选择属性");
				return null;
			}
		} else {
			goodsspec = "";
		}

		if (goodsspec.length() > 1) {
			goodsspec = goodsspec.substring(0, goodsspec.lastIndexOf("-"));
		}

		SelectMode mode = new SelectMode();
		mode.spec = goodsspec;
		String count = numEditText.getText().toString();
		if(TextUtils.isEmpty(count)){
			Toast.show(ctx, "请输入商品数量");
			return null;
		}
		if(loadPrice){
			Toast.show(ctx, "请稍后…");
			return null;
		}
		if(stock == 0){
			Toast.show(ctx, "商品库存不足,请重新选择");
			return null;
		}
		if(Integer.parseInt(count)>stock){
			Toast.show(ctx, "最大购买数量"+stock);
			return null;
		}
		mode.num = Integer.parseInt(count);

		// 未登陆
		if (!MoLiApplication.getInstance().isLogin()) {
			Toast.show(ctx, "请先登录");
			dialog.dismiss();
			ctx.startActivity(new Intent(ctx, LoginActivity.class));
			return null;
		}

		return mode;
	}

	private void confirm(SelectMode mode) {
		String tempSpec = mode.spec;
		int num = mode.num;

		List<ReqGoodsInfo> goodsList = new ArrayList<ReqGoodsInfo>();
		ReqGoodsInfo reqGoodsInfo = new ReqGoodsInfo();
		reqGoodsInfo.goodsid = goodsid;
		reqGoodsInfo.num = num;
		reqGoodsInfo.spec = tempSpec;
		goodsList.add(reqGoodsInfo);

		orderMake(goodsList);

	}

	private void addToShopCar(SelectMode mode, final CustomDialog dialog) {
		String tempSpec = mode.spec;
		int num = mode.num;

		Api.add(ctx, goodsid, tempSpec, num, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				RspSuccessCommon rsp = (RspSuccessCommon) rspData;
				if (rsp != null && rsp.data.success == 1) {
					dialog.dismiss();
					Toast.show(ctx, "添加的购物车成功");
					if(callback != null){
						callback.onSuccess();
					}
				}
			}

			@Override
			public void failed(String msg) {
				Toast.show(ctx, msg + "");
			}
		}, RspSuccessCommon.class);
	}

	private void orderMake(final List<ReqGoodsInfo> goodsList) {
		String ip = Utils.getLocalIpAddress();
		DB_Location db_location = new DB_Location(ctx);
		String lng = db_location.getLontitude();
		String lat = db_location.getLatitude();

		String goods = new Gson().toJson(goodsList);
		Log.d("xxx", "goods =" + goods);
		
		Api.make(ctx, "buynow", goods, ip, lng, lat, "",new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				RspOrderMake rsp = (RspOrderMake) rspData;
				if (rsp.data != null) {
					RspOrderMake.Data data = rsp.data;
					if (data != null) {
						if (data.getVipmember() != 1) {
							MembershipDialog.show(ctx);
						} else {
							Intent intent = new Intent(ctx, FillOrderActivity.class);
							intent.putExtra(FillOrderActivity.ORDER_OP, "buynow");
							intent.putExtra(FillOrderActivity.ORDER_GOODS_LIST, (Serializable) goodsList);
							ctx.startActivity(intent);
						}
					}
				}
			}

			@Override
			public void failed(String msg) {
				Toast.show(ctx, msg);
				Log.d("xxx", "order make =" + msg);
			}
		}, RspOrderMake.class);
	}
}
