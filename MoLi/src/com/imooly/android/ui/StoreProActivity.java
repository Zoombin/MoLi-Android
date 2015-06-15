package com.imooly.android.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooly.android.MoLiApplication;
import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.adapter.StoreProGridViewAdapter;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.entity.RspGoodsSearch.GoodsEty;
import com.imooly.android.entity.RspStoreGoodsList;
import com.imooly.android.entity.RspStoreproFile;
import com.imooly.android.entity.RspSuccessCommon;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.enums.ShareObject;
import com.imooly.android.tool.Config;
import com.imooly.android.widget.PullToRefreshListView;
import com.imooly.android.widget.PullToRefreshListView.OnLoadMoreListener;
import com.imooly.android.widget.ShareDialog;
import com.imooly.android.widget.Toast;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

/**
 * 旗舰店
 * @author daiye
 *
 */
public class StoreProActivity extends BaseActivity implements OnClickListener {

	public static final String EXTRA_BUSINESSID = "businessid";
	private int page = 1;
	private int pagesize = 20;
	private RelativeLayout ll_title;
	private ImageView iv_back;
	private TextView tv_businessname;
	private ImageView iv_share;
	private Button btn_collect;
	private PullToRefreshListView lv_goodslist;
	private String businessid;
	List<GoodsEty> goods;
	private ImageView iv_businessbanner;
	private StoreProGridViewAdapter storeprogridviewadapter;
	
	private Button btn_lv_page;
	private Button btn_lv_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_storepro);
		
		logActivityName(this);
		
		initView();
		
		goods = new ArrayList<GoodsEty>();
		businessid = getIntent().getStringExtra(EXTRA_BUSINESSID);
		
		initStoreInfo();
		getData();
	}
	
	private void initView() {
		ll_title = (RelativeLayout) findViewById(R.id.ll_title);

		iv_back = (ImageView) findViewById(R.id.iv_back);
		iv_back.setOnClickListener(this);

		tv_businessname = (TextView) findViewById(R.id.tv_businessname);
		
		iv_share = (ImageView) findViewById(R.id.iv_share);
		iv_share.setOnClickListener(this);
		
		btn_collect = (Button) findViewById(R.id.btn_collect);
		btn_collect.setOnClickListener(this);
		
		lv_goodslist = (PullToRefreshListView) findViewById(R.id.lv_goodslist);
		iv_businessbanner = (ImageView) findViewById(R.id.iv_businessbanner);
		int width = Config.width;
		int height = width * 120 / 640;
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
		iv_businessbanner.setLayoutParams(params);
		lv_goodslist.setOnLoadListener(new OnLoadMoreListener() {

			@Override
			public void onLoadMore() {
				page++;
				getData();
			}
		});
		
		btn_lv_page = (Button) findViewById(R.id.btn_lv_page);
		btn_lv_back = (Button) findViewById(R.id.btn_lv_back);
		btn_lv_back.setOnClickListener(this);
		lv_goodslist.setButton(btn_lv_page, btn_lv_back);
	}
	
	private void initStoreInfo() {
		Api.storeproFile(this, businessid, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				RspStoreproFile rsp = (RspStoreproFile) rspData;
				String businessname = rsp.data.businessname;
				tv_businessname.setText(businessname);
				
				String businessbanner = rsp.data.businessbanner;
				DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.ic_loading_640_160)
				    .showImageForEmptyUri(R.drawable.ic_error_640_160)  // empty URI时显示的图片  
				    .showImageOnFail(R.drawable.ic_error_640_160)      // 不是图片文件 显示图片  
					.cacheInMemory(true)
					.bitmapConfig(Bitmap.Config.RGB_565)  // 图片压缩质量
					.cacheOnDisc(true)			
					.imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
					.build();
				ImageLoader.getInstance().displayImage(businessbanner, iv_businessbanner, defaultOptions);
				
				int favorite = rsp.data.getIsfavorite();
				if (1 == favorite) {
					btn_collect.setSelected(true);
				} else {
					btn_collect.setSelected(false);
				}
			}
			
			@Override
			public void failed(String msg) {
				Toast.show(self, msg);
			}
		}, RspStoreproFile.class);
	}
	
	private void getData() {
		Api.storeGoodsList(this, businessid, page, pagesize,  new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				lv_goodslist.onLoadMoreComplete();
				RspStoreGoodsList rsp = (RspStoreGoodsList) rspData;
				List<GoodsEty> rspgoods = rsp.data.goodslist;
				
				if (rspgoods == null || rspgoods.size() == 0) {
					return;
				}
				
				int totalpage = rsp.data.getTotalpage();
				lv_goodslist.setNum(totalpage, pagesize);
				lv_goodslist.setGridType(true);
				    
				goods.addAll(rspgoods);
					
				if (storeprogridviewadapter == null) {
					storeprogridviewadapter = new StoreProGridViewAdapter(self, goods);
					lv_goodslist.setAdapter(storeprogridviewadapter);
				} else {
//					storeprogridviewadapter.addGoodsList(goods);
					storeprogridviewadapter.notifyDataSetChanged();
				}
			}
			
			@Override
			public void failed(String msg) {
				lv_goodslist.onLoadMoreComplete();
				Toast.show(self, msg);
			}
		}, RspStoreGoodsList.class);
	}

	/**推送进来的特别处理*/
	private void goBack(){
		String pushAction = getIntent().getStringExtra("pushAction");
		if(TextUtils.isEmpty(pushAction)){
			finish();
		}else{
			Intent intent = new Intent(MainActivity.ACTION);
			sendBroadcast(intent);
			
			startActivity(new Intent(self,MainActivity.class));
			finish();
		}
	}
	
	@Override
	public void onBackPressed() {
		goBack();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_back:
			goBack();
			break;
		case R.id.iv_share:
			new ShareDialog(self, ShareObject.fstore, businessid);
			break;
		case R.id.btn_collect:
			if (!MoLiApplication.getInstance().isLogin()) {
				Toast.show(self, "请先登录");
				startActivity(new Intent(self, LoginActivity.class));
				return;
			}
			final Button btn_collect = (Button) v;
			String ids = "[\""+businessid+"\"]";
			if (btn_collect.isSelected()) {
				Api.delteMyCollectionFlagStore(self, ids,
						new NetCallBack<ServiceResult>() {
							@Override
							public void success(ServiceResult rspData) {
								RspSuccessCommon rsp = (RspSuccessCommon) rspData;
								if (rsp.data != null && rsp.data.success == 1) {
									btn_collect.setSelected(false);
									Toast.show(self, "取消收藏");
								} else {
									btn_collect.setSelected(true);
									Toast.show(self, "取消收藏失败");
								}
							}
							
							@Override
							public void failed(String msg) {
								Toast.show(self, msg);
							}
						}, RspSuccessCommon.class);
			} else {
				Api.addfavstore(self, businessid,
						new NetCallBack<ServiceResult>() {
							@Override
							public void success(ServiceResult rspData) {
								RspSuccessCommon rsp = (RspSuccessCommon) rspData;
								if (rsp.data != null && rsp.data.success == 1) {
									btn_collect.setSelected(true);
									Toast.show(self, "收藏成功");
								} else {
									Toast.show(self, "收藏失败");
								}
							}

							@Override
							public void failed(String msg) {
								Toast.show(self, msg);
							};
						}, RspSuccessCommon.class);
			}
			break;
		case R.id.btn_lv_back:
			// 回到顶部
			lv_goodslist.requestFocusFromTouch();
			lv_goodslist.setSelection(0);
			break;
		default:
			break;
		}
	}
}
