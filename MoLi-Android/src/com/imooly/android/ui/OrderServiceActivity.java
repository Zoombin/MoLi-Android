package com.imooly.android.ui;

import java.io.File;
import java.util.List;

import org.json.JSONArray;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.adapter.OrderCommHListAdapter;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.entity.RspAddressList;
import com.imooly.android.entity.RspOrderService;
import com.imooly.android.entity.RspOrderService.ServiceAddressEty;
import com.imooly.android.entity.RspOrderService.ServiceEty;
import com.imooly.android.entity.RspOrderService.ServiceGoodsEty;
import com.imooly.android.entity.RspOrderService.ServiceStoreEty;
import com.imooly.android.entity.RspSuccessCommon;
import com.imooly.android.entity.RspUploadimg;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.utils.FileUtils;
import com.imooly.android.utils.ImageUtil;
import com.imooly.android.utils.PickPhotoUtils;
import com.imooly.android.utils.PickPhotoUtils.PickCallback;
import com.imooly.android.utils.Utils;
import com.imooly.android.widget.HorizontalListView;
import com.imooly.android.widget.Toast;

/***
 * 申请售后
 * 
 * @author lsd
 * 
 */
public class OrderServiceActivity extends BaseActivity implements OnClickListener {
	public static final int ADD_ADDRESS = 10010;
	public static final int EDIT_ADDRESS = 10011;
	public static String ORDER_NO = "orderno";
	public static String GOODS_ID = "goodsid";
	public static String TRADE_ID = "tradeid";
	private RelativeLayout ll_title;
	private ImageView iv_back;
	private TextView tv_title;

	private HorizontalListView hListView;
	private OrderCommHListAdapter adapter;

	private ImageView order_server_icon;
	private TextView order_server_name;
	private TextView order_server_top_price;
	private ImageView order_server_pic;
	private TextView order_server_detail;
	private TextView order_server_price;
	private TextView order_server_num;

	private RelativeLayout rl_server_return;
	private RelativeLayout rl_server_change;
	private ImageView img_ck_return;
	private ImageView img_ck_change;
	
	private EditText order_server_reason;
	
	private LinearLayout layout_add_address;
	private View layout_address;
	private TextView fillorder_name;
	private TextView fillorder_address_content;
	
	private Button order_comment_commit;
	private ImageUtil mImageUtil;
	
	////-----公共参数
	String orderno ="";//传递过来的订单编号
	String goodsid ="";
	String tradeid ="";
	String type="";

	int returns = -1;
	int change = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_service);

		logActivityName(this);
		
		initView();
		initData();
		getData();
	}

	private void initView() {
		// TODO Auto-generated method stub
		ll_title = (RelativeLayout) findViewById(R.id.ll_title);

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);

		tv_title = (TextView) findViewById(R.id.tv_title);

		hListView = (HorizontalListView) findViewById(R.id.order_server_hlistview);

		order_server_icon = (ImageView) findViewById(R.id.order_comm_top_icon);
		order_server_name = (TextView) findViewById(R.id.order_comm_top_name);
		order_server_top_price = (TextView) findViewById(R.id.order_comm_top_price);
		order_server_pic = (ImageView) findViewById(R.id.order_comm_top_pic);
		order_server_detail = (TextView) findViewById(R.id.order_comm_top_detail);
		order_server_price = (TextView) findViewById(R.id.order_comm_price);
		order_server_num = (TextView) findViewById(R.id.order_comm_top_num);

		rl_server_return = (RelativeLayout) findViewById(R.id.rl_server_return);
		rl_server_return.setOnClickListener(this);
		rl_server_change = (RelativeLayout) findViewById(R.id.rl_server_change);
		rl_server_change.setOnClickListener(this);
		img_ck_change = (ImageView) findViewById(R.id.img_ck_change);
		img_ck_return = (ImageView) findViewById(R.id.img_ck_return);

		order_server_reason = (EditText) findViewById(R.id.order_server_reason);
		
		layout_add_address = (LinearLayout) findViewById(R.id.layout_add_address);
		layout_add_address.setOnClickListener(this);
		layout_address = findViewById(R.id.layout_address);
		layout_address.setOnClickListener(this);
		fillorder_name = (TextView) findViewById(R.id.fillorder_name);
		fillorder_address_content = (TextView) findViewById(R.id.fillorder_address_content);
		
		order_comment_commit = (Button) findViewById(R.id.order_comment_commit);
		order_comment_commit.setOnClickListener(this);
	}

	private void initData() {
		// TODO Auto-generated method stub
		mImageUtil = new ImageUtil(this, 250, 250, "server_img.jpg", "server_tempimg.jpg");

		adapter = new OrderCommHListAdapter();
		adapter.setOnClickListener(hItemClick);
		hListView.setAdapter(adapter);
	}

	private void getData() {
		// TODO Auto-generated method stub
		Intent intent = getIntent();
		if(intent.hasExtra(ORDER_NO)){
			orderno = intent.getStringExtra(ORDER_NO);
		}
		if(intent.hasExtra(GOODS_ID)){
			goodsid = intent.getStringExtra(GOODS_ID);
		}
		if(intent.hasExtra(TRADE_ID)){
			tradeid = intent.getStringExtra(TRADE_ID);
		}
		
		Api.service(self, orderno, goodsid, tradeid, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				// TODO Auto-generated method stub
				RspOrderService rsp = (RspOrderService) rspData;
				if (rsp.data != null) {
					RspOrderService.Data data = rsp.data;

					ServiceStoreEty store = data.getStore();
					if (store != null) {
						order_server_name.setText(store.getStorename());
						order_server_top_price.setText("￥" + store.getTotalprice());
					}

					ServiceGoodsEty goods = data.getGoods();
					if (goods != null) {
						imageLoader.displayImage(goods.getImage(), order_server_pic);
						order_server_detail.setText(goods.getName());
						order_server_price.setText("￥" + goods.getPrice());
						order_server_num.setText("数量：" + goods.getNum());
					}

					ServiceEty service = data.getService();
					if (service != null) {
						returns = service.getReturns();
						change = service.getChange();
					}
					
					ServiceAddressEty address = data.getAddress();
					if(address != null){
						layout_add_address.setVisibility(View.GONE);
						layout_address.setVisibility(View.VISIBLE);
						
						String nameAndPhone = address.getName()+"       "+address.getTel();
						fillorder_name.setText(nameAndPhone);
						
						String addressContent = address.getAddress();
						fillorder_address_content.setTag(address.getAddressid());
						fillorder_address_content.setText(addressContent);
					}
				}
			}

			@Override
			public void failed(String msg) {
				// TODO Auto-generated method stub
				Toast.show(self, msg);
			}
		}, RspOrderService.class);

	}
	
	
	private void setDataToAddressView(RspAddressList.Address address){
		String nameAndPhone = address.getName()+"       "+address.getMobile();
		fillorder_name.setText(nameAndPhone);
		
		String addressContent = address.getProvince()+address.getCity()+address.getArea()+address.getStreet();
		fillorder_address_content.setTag(address.getAddressid());
		fillorder_address_content.setText(addressContent);
	}
	
	private void commitOrderService(){
		String remark = order_server_reason.getText().toString();
		String addressid = (String) fillorder_address_content.getTag();
		
		JSONArray array = new JSONArray();
		List<RspUploadimg.Data> imgList = adapter.getimgs();
		if(imgList !=null && imgList.size()>0){
			for(RspUploadimg.Data data : imgList){
				if(data != null){
					array.put(data.getImgpath()+"");
				}
			}
		}
		
		String images = array.toString();
		Api.serviceadd(self, orderno, goodsid, tradeid, type, remark, images, addressid, new NetCallBack<ServiceResult>() {
			
			@Override
			public void success(ServiceResult rspData) {
				// TODO Auto-generated method stub
				RspSuccessCommon rsp = (RspSuccessCommon) rspData;
				if(rsp.data != null && rsp.data.success == 1){
					Toast.show(self, "申请成功");
					setResult(RESULT_OK);
					finish();
				}else{
					Toast.show(self, rsp.data.message);
				}
			}
			
			@Override
			public void failed(String msg) {
				// TODO Auto-generated method stub
				Toast.show(self, msg);
			}
		}, RspSuccessCommon.class);
	}
	
	
	private void uploadImg(File file){
		byte[] fileBytes = FileUtils.getInstance().getCodeByFile(file);
		Api.uploadimg(self, fileBytes, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				// TODO Auto-generated method stub
				RspUploadimg rsp = (RspUploadimg) rspData;
				if(rsp.data != null && rsp.data.getSuccess() == 1){
					adapter.addData(rsp.data);
				}
			}
			
			@Override
			public void failed(String msg) {
				// TODO Auto-generated method stub
				Toast.show(self, msg);
			}
		}, RspUploadimg.class);
	}
	
	
	private void getAddressList(){
		Api.addresslist(self, Utils.getUnixTime() + "", 1, 20, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {

				// TODO Auto-generated method stub
				RspAddressList rsp = (RspAddressList) rspData;
				if (rsp.data != null) {
					List<RspAddressList.Address> list = rsp.data.getAddresslist();
					if (list != null && list.size()>0) {
						RspAddressList.Address defultAddress = null;
						for(RspAddressList.Address adress : list){
							if(adress.getIsdefault() == 1){
								defultAddress = adress;
								break;
							}
						}
						if(defultAddress == null){
							defultAddress = list.get(0);
						}
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
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.rl_server_return:
			if (returns == 0) {
				Toast.show(self, "该商品不支持退货");
			} else if (returns == 1) {
				type = "return";
				img_ck_return.setVisibility(View.VISIBLE);
				img_ck_change.setVisibility(View.INVISIBLE);
			}
			break;
		case R.id.rl_server_change:
			if (change == 0) {
				Toast.show(self, "该商品不支持换货");
			} else if (change == 1) {
				type = "change";
				img_ck_return.setVisibility(View.INVISIBLE);
				img_ck_change.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.layout_add_address:
			startActivityForResult(new Intent(self,AddressModifyActivity.class),ADD_ADDRESS);
			break;
		case R.id.layout_address:
			startActivityForResult(new Intent(self, AddressActivity.class),EDIT_ADDRESS);
			break;
		case R.id.order_comment_commit:
			commitOrderService();
			break;
		default:
			break;
		}
	}
	

	/**
	 * adapterView 点击处理
	 */
	OnClickListener hItemClick = new OnClickListener() {
		@Override
		public void onClick(View view) {
			// TODO Auto-generated method stub
			String tag = (String) view.getTag();
			switch (view.getId()) {
			case R.id.order_comment_pic:
				int count = adapter.getCount();
				if(count >= 5){
					//有一张图片是放在最后可以点击的那张
					Toast.show(self, "最多可以上传4张图片");
					return;
				}
				if(tag.contains("#")){
					//这个页面没有多行 这里不用处理
				}else{
					showPickDialog();
				}
				break;
			case R.id.order_comment_delete:
				if(tag.contains("#")){
					//这个页面没有多行 这里不用处理
				}else{
					int position = Integer.parseInt(tag);
					adapter.deleteData(position);
				}
				break;
			default:
				break;
			}
		}
	};

	private void showPickDialog() {
		PickPhotoUtils.showPickDialog(OrderServiceActivity.this, "", new PickCallback() {
			@Override
			public void onPhoto() {
				// TODO Auto-generated method stub
				mImageUtil.getAndCropPhoto();
			}

			@Override
			public void onCamera() {
				// TODO Auto-generated method stub
				mImageUtil.takeCameraPhoto();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case ImageUtil.CAMERA_PIC:
			mImageUtil.cropPhoto(Uri.fromFile(mImageUtil.getPicFile()));
			break;
		case ImageUtil.HANDLE_PIC:
			File file = mImageUtil.getPicFile();
			uploadImg(file);
			break;
		case ADD_ADDRESS:
		case EDIT_ADDRESS:
			getAddressList();
			break;
		default:
			break;
		}
	}

}
