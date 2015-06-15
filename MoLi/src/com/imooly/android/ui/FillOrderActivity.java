package com.imooly.android.ui;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.f;
import com.baidu.platform.comapi.map.s;
import com.google.gson.Gson;
import com.imooly.android.MoLiApplication;
import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.adapter.StoreFillorderListAdapter;
import com.imooly.android.animation.RotateFlipCardAnimation;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.db.DB_Location;
import com.imooly.android.entity.ReqGoodsEntity;
import com.imooly.android.entity.ReqGoodsInfo;
import com.imooly.android.entity.RspAddressList;
import com.imooly.android.entity.RspFlagCommon;
import com.imooly.android.entity.RspVoucherTerm;
import com.imooly.android.entity.RspAddressList.Address;
import com.imooly.android.entity.RspOrderMake;
import com.imooly.android.entity.RspOrderMake.OrderGoodsEty;
import com.imooly.android.entity.RspOrderSave;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.tool.ClickUtil;
import com.imooly.android.tool.Rotation3D;
import com.imooly.android.tool.Rotation3D.RotationListener;
import com.imooly.android.utils.Utils;
import com.imooly.android.view.PayConfirmDialog;
import com.imooly.android.view.PayConfirmDialog.PayConfirmCallBack;
import com.imooly.android.widget.Toast;

/***
 * 商城 - 填写订单
 * 
 * @author lsd
 * 
 */
public class FillOrderActivity extends BaseActivity implements OnClickListener {
	DecimalFormat fnum = new DecimalFormat("##0.00");
	public static final int ADD_ADDRESS = 10010;
	public static final int EDIT_ADDRESS = 10011;
	public static String ORDER_OP = "order_op";
	public static String ORDER_GOODS_LIST = "goods_list";
	private RelativeLayout ll_title;
	private ImageView iv_back;
	private TextView tv_title;

	private ListView listView;
	private StoreFillorderListAdapter adapter;
	private View listHeader;
	private View listFooter;

	private LinearLayout layout_add_address;
	private View layout_address;
	private TextView address_info;
	private TextView address_default;// 已默认
	private TextView fillorder_name;
	private TextView address_content;

	private LinearLayout layout_voucher_show;
	private RelativeLayout layout_voucher;
	private RelativeLayout rl_service;
	private LinearLayout llLayout_voucher;
	private TextView voucher_value;
	private TextView voucher_tip;
	private TextView voucher_guide;
	private CheckBox fillorder_ckbox;
	private EditText voucher_edittext;
	private TextView total_price;
	private Button commit_button;
	private TextView text_1, text_2, text_3, text_4;

	String tempOp;
	RspAddressList.Address tempAddress;
	List<RspOrderMake.OrderStoreProEty> tempOrderEtys;
	RspOrderMake.Data tempData;

	private boolean isShowing = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_store_fillorder);

		logActivityName(this);

		initView();
		initData();
		orderMake();
		getVoucherRoleData();
	}

	private void initView() {
		// TODO Auto-generated method stub
		ll_title = (RelativeLayout) findViewById(R.id.ll_title);

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);

		tv_title = (TextView) findViewById(R.id.tv_title);

		listView = (ListView) findViewById(R.id.list);
		listHeader = LayoutInflater.from(self).inflate(R.layout.activity_store_fillorder_header, null);
		listFooter = LayoutInflater.from(self).inflate(R.layout.activity_store_fillorder_footer, null);
		listView.addHeaderView(listHeader);
		listView.addFooterView(listFooter);
		listHeader.setVisibility(View.GONE);
		listFooter.setVisibility(View.GONE);

		layout_add_address = (LinearLayout) listHeader.findViewById(R.id.layout_add_address);
		layout_add_address.setOnClickListener(this);
		layout_address = listHeader.findViewById(R.id.layout_address);
		layout_address.setOnClickListener(this);

		address_info = (TextView) listHeader.findViewById(R.id.fillorder_address_info);
		address_default = (TextView) listHeader.findViewById(R.id.fillorder_address_default);
		address_default.setVisibility(View.INVISIBLE);
		fillorder_name = (TextView) listHeader.findViewById(R.id.fillorder_name);
		address_content = (TextView) listHeader.findViewById(R.id.fillorder_address_content);

		voucher_value = (TextView) listFooter.findViewById(R.id.voucher_value);
		voucher_tip = (TextView) listFooter.findViewById(R.id.voucher_tip);
		voucher_edittext = (EditText) listFooter.findViewById(R.id.voucher_edittext);
		fillorder_ckbox = (CheckBox) listFooter.findViewById(R.id.fillorder_ckbox);
		total_price = (TextView) listFooter.findViewById(R.id.total_price);
		commit_button = (Button) listFooter.findViewById(R.id.fillorder_commit_bt);
		commit_button.setOnClickListener(this);

		layout_voucher_show = (LinearLayout) listFooter.findViewById(R.id.layout_voucher_show);
		layout_voucher_show.setOnClickListener(this);
		layout_voucher = (RelativeLayout) listFooter.findViewById(R.id.layout_voucher);
		rl_service = (RelativeLayout) listFooter.findViewById(R.id.rl_service);
		voucher_guide = (TextView) listFooter.findViewById(R.id.voucher_guide);

		llLayout_voucher = (LinearLayout) listFooter.findViewById(R.id.layout_fillorder_use_voucher);
		
		text_1 = (TextView) listFooter.findViewById(R.id.text_1);
		text_2 = (TextView) listFooter.findViewById(R.id.text_2);
		text_3 = (TextView) listFooter.findViewById(R.id.text_3);
		text_4 = (TextView) listFooter.findViewById(R.id.text_4);
	}
	
	private void getVoucherRoleData() {
		
		Api.getVoucherTerm(self, Utils.getUnixTime()+"", new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {
				RspVoucherTerm rsp =  (RspVoucherTerm)rspData;
				RspVoucherTerm.Data data = rsp.data;
				
				
				if (data.getFlag() ==1) { // 需要更新
					text_1.setText(data.getContent().get(0));
					text_2.setText(data.getContent().get(1));
					text_3.setText(data.getContent().get(2));
					text_4.setText(data.getContent().get(3));
				}
			}

			@Override
			public void failed(String msg) {
				Toast.show(self, msg);
				
			}
		}, RspVoucherTerm.class);
		
	}

	private void initData() {
		// TODO Auto-generated method stub
		adapter = new StoreFillorderListAdapter();
		listView.setAdapter(adapter);
	}

	@SuppressWarnings("unchecked")
	private void orderMake() {
		Intent intent = getIntent();
		String op = "";
		if (intent.hasExtra(ORDER_OP)) {
			tempOp = op = intent.getStringExtra(ORDER_OP);
		}
		List<ReqGoodsInfo> goodsList = null;
		if (intent.hasExtra(ORDER_GOODS_LIST)) {
			goodsList = (List<ReqGoodsInfo>) intent.getSerializableExtra(ORDER_GOODS_LIST);
		}

		String ip = Utils.getLocalIpAddress();
		DB_Location db_location = new DB_Location(self);
		String lng = db_location.getLontitude();
		String lat = db_location.getLatitude();
		String deaddressid = "";
		if(tempAddress != null){
			deaddressid = tempAddress.getAddressid();	
		}

		String goods = new Gson().toJson(goodsList);
		Api.make(self, op, goods, ip, lng, lat, deaddressid,new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				// TODO Auto-generated method stub
				RspOrderMake rsp = (RspOrderMake) rspData;
				if (rsp.data != null) {
					listHeader.setVisibility(View.VISIBLE);
					listFooter.setVisibility(View.VISIBLE);

					RspOrderMake.Data data = tempData = rsp.data;

					RspAddressList.Address addressEty = data.getAddress();
					if (addressEty != null && !TextUtils.isEmpty(addressEty.getAddressid())) {
						tempAddress = addressEty;

						layout_add_address.setVisibility(View.GONE);
						layout_address.setVisibility(View.VISIBLE);
						setDataToAddressView(tempAddress);
					} else {
						layout_add_address.setVisibility(View.VISIBLE);
						layout_address.setVisibility(View.GONE);
					}

					List<RspOrderMake.OrderStoreProEty> orderEtys = tempOrderEtys = data.getGoodslist();
					if (orderEtys != null && orderEtys.size() > 0) {
						adapter.setData(orderEtys);
					}

					// 代金券
					String vouValue = data.getVoucher() + "";// 金额
					String vouImg = data.getVoucherimage();//
					voucher_guide.setText("点击翻开查看使用细则");
					if (!TextUtils.isEmpty(vouValue)) {
						voucher_value.setText(vouValue);
						voucher_tip.setText(String.format(getString(R.string.fillorder_voucher_format), vouValue));
					}
					
					
					final float totalPrice = data.getTotalprice();
					//使用代金券
					final float Totalvoucher = data.getTotalvoucher();
					fillorder_ckbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						@Override
						public void onCheckedChanged(CompoundButton checkBtn, boolean isChecke) {
							// TODO Auto-generated method stub
							if(isChecke){
								if(Totalvoucher > totalPrice){
									voucher_edittext.setText(fnum.format(totalPrice));
									//总价
									total_price.setText("￥"+0.00);
								}else{
									voucher_edittext.setText(fnum.format(Totalvoucher));
									//总价
									total_price.setText("￥"+fnum.format((totalPrice-Totalvoucher)));
								}
							}else{
								//总价
								total_price.setText("￥"+fnum.format(totalPrice));
							}
						}
					});
					voucher_edittext.addTextChangedListener(new TextWatcher() {
						@Override
						public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
							// TODO Auto-generated method stub
						}
						
						@Override
						public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
							// TODO Auto-generated method stub
						}
						
						@Override
						public void afterTextChanged(Editable arg0) {
							// TODO Auto-generated method stub
							if (fillorder_ckbox.isChecked()) {
								String voucherS = arg0.toString();
								if (TextUtils.isEmpty(voucherS)) {
									return;
								}
								if (Totalvoucher <=0 ) {
									Toast.show(self, "你没有可以使用的代金券");
									return;
								}
								float voucherF = Float.parseFloat(voucherS);
								if (voucherF < 0) {
									voucherF = 0;
									voucher_edittext.setText(fnum.format(voucherF));
								}
								if(voucherF > Totalvoucher){
									voucherF = Totalvoucher;
									voucher_edittext.setText(fnum.format(voucherF));
									Toast.show(self, "你最多可以使用代金券 " + Totalvoucher + " 元");
								}
								//总价
								total_price.setText("￥"+fnum.format((totalPrice-voucherF)));
							}
						}
					});

					//总价
					total_price.setText("￥"+fnum.format(totalPrice));
					String showmsg = data.getShowmsg();
					if(!TextUtils.isEmpty(showmsg)){
						Toast.show(self, showmsg);
					}
				}
			}

			@Override
			public void failed(String msg) {
				// TODO Auto-generated method stub
				Toast.show(self, msg);
			}
		}, RspOrderMake.class);
	}

	private void orderSave() {
		// 属性值
		List<ReqGoodsEntity> list = new ArrayList<ReqGoodsEntity>();
		if (tempOrderEtys != null && tempOrderEtys.size() > 0) {
			for (int i = 0; i < tempOrderEtys.size(); i++) {
				RspOrderMake.OrderStoreProEty proEty = tempOrderEtys.get(i);

				ReqGoodsEntity enEty = new ReqGoodsEntity();
				String storeid = proEty.getStoreid();
				enEty.storeid = storeid;
				if(adapter.getMarkMap().containsKey(storeid)){
					enEty.remark = adapter.getMarkMap().get(storeid);
				}
				List<ReqGoodsInfo> goodslist = new ArrayList<ReqGoodsInfo>();
				for (OrderGoodsEty gEty : proEty.getGoods()) {
					ReqGoodsInfo info = new ReqGoodsInfo();
					info.goodsid = gEty.getGoodsid();
					info.num = gEty.getNum();
					info.spec = gEty.getSpec();
					goodslist.add(info);
				}
				enEty.goodslist = goodslist;
				list.add(enEty);
			}
			// 参数
			final String goods = new Gson().toJson(list);

			// 地址
			String addressId = "";
			if (tempAddress == null) {
				Toast.show(self, "请添加地址");
				return;
			}
			addressId = tempAddress.getAddressid();

			// 使用代金券
			float userVoucherValue = 0;
			float totalVoucher = tempData.getTotalvoucher();//可使用代金券的总额
			float total_price = tempData.getTotalprice();//需要支付的总额
			if (fillorder_ckbox.isChecked() && totalVoucher > 0) {
				String num = voucher_edittext.getText().toString();
				if (!TextUtils.isEmpty(num)) {
					userVoucherValue = Float.parseFloat(num);
				}
				if(userVoucherValue >= total_price){
					//使用全额代金券   ---  能够用代金券全额支付时才要求输入密码
					final float vouValue = userVoucherValue;
					//检查是否设置了交易密码
					Api.checkIsSettedTraderPassword(self, new NetCallBack<ServiceResult>() {
						@Override
						public void success(ServiceResult rspData) {
							RspFlagCommon rsp = (RspFlagCommon) rspData;
							RspFlagCommon.Data data = rsp.data;
							if (data.getFlag() == 1) {
								PayConfirmDialog.showConfirmDialog(self, "使用代金券支付", "代金券：" + vouValue + " 元", new PayConfirmCallBack() {
									@Override
									public void onRight(Object obj) {
										// TODO Auto-generated method stub
										String addressId = tempAddress.getAddressid();
										float userVoucherValue = Float.parseFloat(voucher_edittext.getText().toString());

										//使用全额代金券
										orderSavePost("voucher", addressId, goods, userVoucherValue, (String) obj);
										return;
									}

									@Override
									public void onLeft() {
										// TODO Auto-generated method stub
									}
								});
							} else {
								//设置交易密码
								Toast.show(self, "请设置交易密码");
								startActivity(new Intent(self,TradePwdSettingActivity.class));
							}
						}

						@Override
						public void failed(String msg) {
							//设置交易密码
							Toast.show(self, "请设置交易密码");
							startActivity(new Intent(self,TradePwdSettingActivity.class));
						}
					}, RspFlagCommon.class);
				}else{
					//使用部分代金券
					orderSavePost("money",addressId, goods, userVoucherValue, "");
				}
			}else{
				//现金支付
				orderSavePost("money",addressId, goods, 0, "");
			}
		}
	}

	// 提交生成订单
	private void orderSavePost(final String type, String addressId, String goods, float voucher, String walletpwd) {
		Api.save(self, tempOp, addressId, goods, voucher, walletpwd, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				// TODO Auto-generated method stub
				RspOrderSave rsp = (RspOrderSave) rspData;
				if (rsp.data != null && rsp.data.success == 1) {
					if ("voucher".equals(type)) {
						// 设置会员充值为false
						MoLiApplication.getInstance().setMemeberPay(false);
						
						Intent intent = new Intent();
						intent.setClass(self, PaymentSuccess.class);
						intent.putExtra(PaymentActivity.PAYNO, rsp.data.getPayno());
						intent.putExtra(PaymentActivity.PAY_MONEY, rsp.data.getPayamount());
						intent.putExtra(PaymentActivity.GOOD_NAME, rsp.data.getPaysubject());
						intent.putExtra(PaymentActivity.GOOD_INFO, rsp.data.getPaybody());
						intent.putExtra(PaymentActivity.PAY_TYPE,"代金券支付");

						startActivity(intent);
						finish();
					} else {
						Toast.show(self, "订单提交成功,去付款");
						// 设置会员充值为false
						MoLiApplication.getInstance().setMemeberPay(false);
						
						Intent intent = new Intent();
						intent.setClass(self, PaymentActivity.class);
						intent.putExtra(PaymentActivity.PAYNO, rsp.data.getPayno());
						intent.putExtra(PaymentActivity.PAY_MONEY, rsp.data.getPayamount());
						
						Log.d("xxx", "rsp.data.getPayamount() = " + rsp.data.getPayamount());
						Log.d("xxx", "rsp.data.getPayamount() = " + rsp.data.ordernos);
						
						intent.putExtra(PaymentActivity.GOOD_NAME, rsp.data.getPaysubject());
						intent.putExtra(PaymentActivity.GOOD_INFO, rsp.data.getPaybody());

						startActivity(intent);
						finish();
					}
				}
			}

			@Override
			public void failed(String msg) {
				// TODO Auto-generated method stub
				Toast.show(self, msg);
			}
		}, RspOrderSave.class);
	}

	private void getAddressList() {
		Api.addresslist(self, Utils.getUnixTime() + "", 1, 20, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				// TODO Auto-generated method stub
				RspAddressList rsp = (RspAddressList) rspData;
				if (rsp.data != null) {
					List<RspAddressList.Address> list = rsp.data.getAddresslist();
					if (list != null && list.size() > 0) {
						RspAddressList.Address defultAddress = null;
						for (RspAddressList.Address adress : list) {
							if (adress.getIsdefault() == 1) {
								defultAddress = adress;
								break;
							}
						}
						if (defultAddress == null) {
							defultAddress = list.get(0);
						}
						tempAddress = defultAddress;
						layout_add_address.setVisibility(View.GONE);
						layout_address.setVisibility(View.VISIBLE);
						setDataToAddressView(defultAddress);
					}
				}
			}

			@Override
			public void failed(String msg) {
				// TODO Auto-generated method stub
			}
		}, RspAddressList.class);
	}

	private void setDataToAddressView(RspAddressList.Address address){
		String nameAndPhone = address.getName()+"       "+address.getMobile();
		fillorder_name.setText(nameAndPhone);
		
		String addressContent = address.getProvince()+address.getCity()+address.getArea()+address.getStreet();
		address_content.setTag(address.getAddressid());
		address_content.setText(addressContent);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(ClickUtil.isFastDoubleClick()){
			return;
		}
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.layout_voucher_show:
			
			new Rotation3D(0.0f, 180.0f, layout_voucher_show, new RotationListener() {
				
				@Override
				public void onEndRotation() {
					
					 if (isShowing == true) {
					 voucher_guide.setText("点击翻开查看使用细则");
					 isShowing = false;
					 layout_voucher.setVisibility(View.VISIBLE);
					 rl_service.setVisibility(View.GONE);
					 } else {
					 voucher_guide.setText("点击查看可获得代金卷金额");
					 isShowing = true;
					 layout_voucher.setVisibility(View.GONE);
					 rl_service.setVisibility(View.VISIBLE);
					 }
				}
			});
			
			break;
		case R.id.layout_add_address:
			startActivityForResult(new Intent(self, AddressModifyActivity.class), ADD_ADDRESS);
			break;
		case R.id.layout_address:
			startActivityForResult(new Intent(self, AddressActivity.class).putExtra("action", "editAdr"), EDIT_ADDRESS);
			break;
		case R.id.fillorder_commit_bt:
			orderSave();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case ADD_ADDRESS:
			getAddressList();
			orderMake();
			break;
		case EDIT_ADDRESS:
			if(data != null && data.hasExtra("selAddr")){
				RspAddressList.Address address = (Address) data.getSerializableExtra("selAddr");
				tempAddress = address;
				setDataToAddressView(address);
				orderMake();
			}
			break;
		default:
			break;
		}
	}

}
