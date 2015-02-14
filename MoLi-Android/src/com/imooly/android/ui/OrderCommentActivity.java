package com.imooly.android.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.adapter.OrderCommHListAdapter;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.entity.RspOrderComment;
import com.imooly.android.entity.RspOrderComment.CommentGoods;
import com.imooly.android.entity.RspSuccessCommon;
import com.imooly.android.entity.RspUploadimg;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.utils.FileUtils;
import com.imooly.android.utils.ImageUtil;
import com.imooly.android.utils.PickPhotoUtils;
import com.imooly.android.utils.PickPhotoUtils.PickCallback;
import com.imooly.android.widget.HorizontalListView;
import com.imooly.android.widget.Toast;

/***
 * 评价订单
 * 
 * @author lsd
 * 
 */
public class OrderCommentActivity extends BaseActivity implements OnClickListener {
	public static String ORDER_NO = "order_no";
	private RelativeLayout ll_title;
	private ImageView iv_back;
	private TextView tv_title;

	private LinearLayout content_list;
	private Button order_comment_commit;
	private ImageUtil mImageUtil;

	String orderno;
	List<OrderCommHListAdapter> adapters;
	int curViewLine = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_order_comment);

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

		content_list = (LinearLayout) findViewById(R.id.content_list);

		order_comment_commit = (Button) findViewById(R.id.order_comment_commit);
		order_comment_commit.setOnClickListener(this);
	}

	private void initData() {
		// TODO Auto-generated method stub
		adapters = new ArrayList<OrderCommHListAdapter>();
		mImageUtil = new ImageUtil(this, 250, 250, "comment_img.jpg", "comment_tempimg.jpg");
	}

	private void getData() {
		// TODO Auto-generated method stub
		if (getIntent().hasExtra(ORDER_NO)) {
			orderno = getIntent().getStringExtra(ORDER_NO);
		}

		Api.orderComment(self, orderno, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				// TODO Auto-generated method stub
				RspOrderComment rsp = (RspOrderComment) rspData;
				if (rsp.data != null) {
					setView(rsp.data);
				}
			}

			@Override
			public void failed(String msg) {
				// TODO Auto-generated method stub
				Toast.show(self, msg);
			}
		}, RspOrderComment.class);
	}

	private void setView(RspOrderComment.Data data) {
		List<CommentGoods> goodsList = data.getGoodslist();
		if (goodsList != null && goodsList.size() > 0) {
			for (int i = 0; i < goodsList.size(); i++) {
				CommentGoods goods = goodsList.get(i);
				View view = LayoutInflater.from(self).inflate(R.layout.layout_order_comment_item, null);
				view.setTag(goods);
				ImageView order_comment_pic = (ImageView) view.findViewById(R.id.order_comment_pic);
				TextView order_comment_detail = (TextView) view.findViewById(R.id.order_comment_detail);
				TextView order_comment_price = (TextView) view.findViewById(R.id.order_comment_price);
				TextView order_comment_num = (TextView) view.findViewById(R.id.order_comment_num);

				EditText order_comment_content = (EditText) view.findViewById(R.id.order_comment_content);
				RatingBar order_comment_ratingBar = (RatingBar) view.findViewById(R.id.order_comment_ratingBar);

				imageLoader.displayImage(goods.getImage(), order_comment_pic);
				order_comment_detail.setText(goods.getName());
				order_comment_price.setText("价格：" + goods.getPrice() + "");
				order_comment_num.setText("数量：" + goods.getNum());

				// 评论的图片
				HorizontalListView hListView = (HorizontalListView) view.findViewById(R.id.hlistview);
				OrderCommHListAdapter adapter = new OrderCommHListAdapter();
				adapter.setOnClickListener(hItemClick);
				adapter.setViewLine(i);
				hListView.setAdapter(adapter);
				adapters.add(adapter);
				content_list.addView(view);
			}
		}
	}

	private void uploadImg(File file) {
		byte[] fileBytes = FileUtils.getInstance().getCodeByFile(file);
		Api.uploadimg(self, fileBytes, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				// TODO Auto-generated method stub
				RspUploadimg rsp = (RspUploadimg) rspData;
				if (rsp.data != null && rsp.data.getSuccess() == 1) {
					OrderCommHListAdapter adapter = adapters.get(curViewLine);
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

	private void sendcomment() {
		int count = content_list.getChildCount();
		String commentlist = "";
		if (count > 0) {
			try {
				JSONArray array = new JSONArray();
				for (int i = 0; i < count; i++) {
					View view = content_list.getChildAt(i);
					CommentGoods goods = (CommentGoods) view.getTag();

					JSONObject object = new JSONObject();
					object.put("goodsid", goods.getGoodsid());
					object.put("unique", goods.getUnique());

					EditText order_comment_content = (EditText) view.findViewById(R.id.order_comment_content);
					object.put("content", order_comment_content.getText().toString());

					JSONArray imgArray = new JSONArray();
					OrderCommHListAdapter adapter = adapters.get(i);
					List<RspUploadimg.Data> imgList = adapter.getimgs();
					if(imgList != null  && imgList.size()>0){
						for (RspUploadimg.Data data : imgList) {
							if(data != null){
								imgArray.put(data.getImgpath()+"");
							}
						}
					}
					object.put("images", imgArray.toString());

					RatingBar order_comment_ratingBar = (RatingBar) view.findViewById(R.id.order_comment_ratingBar);
					object.put("stars", (int) order_comment_ratingBar.getRating());

					array.put(object);
				}
				commentlist = array.toString();
			} catch (Exception e) {
				// TODO: handle exception
			}
		}

		Api.sendcomment(self, orderno, commentlist, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				// TODO Auto-generated method stub
				RspSuccessCommon rsp = (RspSuccessCommon) rspData;
				if (rsp.data != null && rsp.data.success == 1) {
					Toast.show(self, "评论成功");
					setResult(RESULT_OK);
					finish();
				}else{
					String msg = rsp.data.getMessage();
					if(!TextUtils.isEmpty(msg)){
						Toast.show(self, msg);
					}
				}
			}

			@Override
			public void failed(String msg) {
				// TODO Auto-generated method stub
				if(!TextUtils.isEmpty(msg)){
					Toast.show(self, msg);
				}
			}
		}, RspSuccessCommon.class);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.iv_back:
			finish();
			break;
		case R.id.order_comment_commit:
			sendcomment();
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
				if (tag.contains("#")) {
					// 这个页面有多行
					String[] wheres = tag.split("#");
					int viewLine = Integer.parseInt(wheres[0]);
					curViewLine = viewLine;
					showPickDialog();
				} else {
				}
				break;
			case R.id.order_comment_delete:
				if (tag.contains("#")) {
					// 这个页面有多行
					String[] wheres = tag.split("#");
					int viewLine = Integer.parseInt(wheres[0]);
					int position = Integer.parseInt(wheres[1]);

					OrderCommHListAdapter adapter = adapters.get(viewLine);
					adapter.deleteData(position);
				} else {
				}
				break;
			default:
				break;
			}
		}
	};

	private void showPickDialog() {
		PickPhotoUtils.showPickDialog(OrderCommentActivity.this, "", new PickCallback() {
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
		default:
			break;
		}
	}

}
