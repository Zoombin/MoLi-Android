package com.imooly.android.ui;

import java.lang.ref.SoftReference;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooly.android.MoLiApplication;
import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.entity.RspMemberCard;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.tool.ClickUtil;
import com.imooly.android.tool.Config;
import com.imooly.android.tool.TicketUtil;
import com.imooly.android.utils.BitmapUtils;
import com.imooly.android.utils.ImageLoaderUtil;
import com.imooly.android.utils.ImageUtil;
import com.imooly.android.widget.Toast;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 电子会员卡界面
 * 
 * @author
 * 
 */
public class MemberCardActivity extends BaseActivity implements OnClickListener {

	private RelativeLayout rl_title, rl_member_yes, rl_member_no;

	private ImageView image_back;
	private TextView tv_title, text_day, text_hour, text_minute, text_second, text_not_vip_tip, text_refrsh;
	private TextView text_member_right, text_join;
	private ImageView image_top;
	private ImageView image_bottom;
	private ImageView image_code;

	private int mDay = 0;
	private int mHour = 0;
	private int mMin = 0;
	private int mSecond = 0;

	private static final String Type_BAR_CODE = "type_bar_code";
	private static final String Type_2D_CODE = "type_2d_code";

	private SoftReference<Bitmap> bitmap2dCode = null;
	private Bitmap bitmapBarCode = null;

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				text_day.setText(mDay + "");
				text_hour.setText(mHour + "");
				text_minute.setText(mMin + "");
				text_second.setText(mSecond + "");
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_member_card);

		logActivityName(this);

		if (MoLiApplication.getInstance().isLogin() == false) {
			self.startActivityForResult(new Intent(self, LoginActivity.class), 0);
		}

		initView();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			finish();
		}
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		// 加载数据
		if (MoLiApplication.getInstance().isLogin() == true) {
			initData();
		}
	}

	/**
	 * 倒计时计算
	 */
	private void ComputeTime() {
		mSecond--;
		if (mSecond < 0) {
			mMin--;
			mSecond = 59;
			if (mMin < 0) {
				mMin = 59;
				mHour--;
				if (mHour < 0) {
					// 倒计时结束
					mHour = 59;
					mDay--;

				}
			}

		}

	}

	Runnable run = new Runnable() {

		@Override
		public void run() {
			ComputeTime();
			handler.sendEmptyMessage(0);
			handler.postDelayed(run, 1000);
		}
	};

	private void initTime(int sec) {

		Log.e("xxx", "sec = " + sec);

		mDay = sec / (60 * 60 * 24);
		mHour = (sec % (60 * 60 * 24)) / (60 * 60);
		mMin = (sec % (60 * 60)) / 60;
		mSecond = sec % 60;
	}

	private void initData() {
		Api.memberCard(self, new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {
				RspMemberCard rsp = (RspMemberCard) rspData;
				RspMemberCard.Data data = rsp.data;
				int vipFlag = data.getVipflag();
				String showMessage = data.getShowmsg();

				if (vipFlag == 0) { // 不是会员

					rl_member_no.setVisibility(View.VISIBLE);
					rl_member_yes.setVisibility(View.GONE);
					text_not_vip_tip.setText(showMessage);

				} else { // 是会员

					rl_member_yes.setVisibility(View.VISIBLE);
					rl_member_no.setVisibility(View.GONE);
					initTime(data.getExpiresec());
					if (data.getBarcode().getShow() == 1) {

						imageLoader.loadImage(data.getBarcode().getImage() + getUrl(), ImageLoaderUtil.getMemberCardImageOption(),
								new SimpleImageLoadingListener() {
									@Override
									public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
										image_top.setImageBitmap(loadedImage);
										bitmapBarCode = loadedImage;
										super.onLoadingComplete(imageUri, view, loadedImage);
									}
								});

					} else {
						image_top.setVisibility(View.GONE);
					}

					if (data.getQrcode().getShow() == 1) {

						imageLoader.loadImage(data.getQrcode().getImage() + getUrl(), ImageLoaderUtil.getMemberCardImageOption(),
								new SimpleImageLoadingListener() {
									@Override
									public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
										image_bottom.setImageBitmap(loadedImage);
										bitmap2dCode = new SoftReference<Bitmap>(loadedImage);
										super.onLoadingComplete(imageUri, view, loadedImage);
									}
								});

						// imageLoader.displayImage(data.getQrcode().getImage()
						// + getUrl(), image_bottom,
						// ImageLoaderUtil.getMemberCardImageOption());

					} else {
						image_bottom.setVisibility(View.GONE);
					}
					handler.removeCallbacks(run); // 防止多次运行
					handler.post(run);

				}

			}

			@Override
			public void failed(String msg) {

				Toast.show(self, msg);

			}
		}, RspMemberCard.class);

	}

	private void initView() {

		// title
		rl_title = (RelativeLayout) findViewById(R.id.rl_title);

		image_back = (ImageView) findViewById(R.id.image_back);
		image_back.setOnClickListener(this);

		tv_title = (TextView) findViewById(R.id.tv_title);

		// 时间
		text_day = (TextView) findViewById(R.id.text_day);
		text_hour = (TextView) findViewById(R.id.text_hour);
		text_minute = (TextView) findViewById(R.id.text_minute);
		text_second = (TextView) findViewById(R.id.text_second);

		rl_member_yes = (RelativeLayout) findViewById(R.id.relative_member_yes);
		rl_member_no = (RelativeLayout) findViewById(R.id.relative_member_no);
		text_not_vip_tip = (TextView) findViewById(R.id.text_not_vip_tip);

		// 刷新按钮
		text_refrsh = (TextView) findViewById(R.id.text_refresh);
		text_refrsh.setOnClickListener(this);

		// 会员特权和充值缴费
		text_member_right = (TextView) findViewById(R.id.text_member_right);
		text_member_right.setOnClickListener(this);
		text_join = (TextView) findViewById(R.id.text_join);
		text_join.setOnClickListener(this);

		image_top = (ImageView) findViewById(R.id.image_top);
		image_top.setOnClickListener(this);

		image_bottom = (ImageView) findViewById(R.id.image_bottom);
		image_bottom.setOnClickListener(this);
	}

	/** 推送进来的特别处理 */
	private void goBack() {
		String pushAction = getIntent().getStringExtra("pushAction");
		if (TextUtils.isEmpty(pushAction)) {
			finish();
		} else {
			Intent intent = new Intent(MainActivity.ACTION);
			sendBroadcast(intent);

			startActivity(new Intent(self, MainActivity.class));
			finish();
		}
	}

	@Override
	public void onBackPressed() {
		goBack();
	}

	@Override
	public void onClick(View v) {
		
		if (ClickUtil.isFastDoubleClick()) {
			return;
		}
		
		switch (v.getId()) {
		case R.id.image_back:
			goBack();
			break;

		case R.id.text_refresh:
			initData();
			break;

		case R.id.text_member_right:
			startActivity(new Intent(self, MemberRightActivity.class));
			break;

		case R.id.text_join:
			startActivity(new Intent(self, MemberRechargeActivity.class));
			break;

		case R.id.image_top:
			showBarCodeDialog(self, bitmapBarCode);
			break;

		case R.id.image_bottom:
			showCodeDialog(self, bitmap2dCode, Type_2D_CODE);
			break;
		default:
			break;
		}

	}

	private void showBarCodeDialog(Context context, final Bitmap bitmap) {
		
		if (bitmap == null) {
			return;
		}

		final Dialog dialog = new Dialog(context, R.style.Dialog_Full);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_show_code, null);
		
		final Bitmap bitmapTemp = BitmapUtils.rotateBitmap(90, bitmap);
		
		image_code = (ImageView) view.findViewById(R.id.image_code);
		image_code.setImageBitmap(bitmapTemp);
		
		view.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
				if (!bitmapTemp.isRecycled()) {
					bitmapTemp.recycle();
				}
				
			}
		});

		dialog.setContentView(view);


		dialog.show();
	}

	private void showCodeDialog(Context context, final SoftReference<Bitmap> bitmap, String type) {

		if (bitmap == null) {
			return;
		}

		final Dialog dialog = new Dialog(context, R.style.Dialog_Full);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_show_code, null);

		view.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});

		dialog.setContentView(view);
		image_code = (ImageView) view.findViewById(R.id.image_code);

		image_code.setImageBitmap(bitmap.get());

		dialog.show();
	}

	/***
	 * API请求公共参数
	 * 
	 * APP唯一标识appid，校验签名signature，时间戳timestamp，会话sessionid（cookie中）
	 * 
	 * @param params
	 * @return
	 */
	public static String getUrl() {
		String appid = Config.getAppID();
		String appsecret = Config.getAppSecret();
		String ticket = TicketUtil.instance().getTicket();
		String timestamp = (System.currentTimeMillis() / 1000) + "";
		String sessionid = TicketUtil.instance().getSessionid();
		String signature = Api.signature(appid, appsecret, ticket, timestamp);

		StringBuffer buffer = new StringBuffer();
		buffer.append("&appid=");
		buffer.append(appid);
		buffer.append("&timestamp=");
		buffer.append(timestamp);
		buffer.append("&sessionid=");
		buffer.append(sessionid);
		buffer.append("&signature=");
		buffer.append(signature);

		Log.d("imooly", "url = " + buffer.toString());

		return buffer.toString();
	}
	
	@Override
	protected void onStop() {
		
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		ImageUtil.recycleImageVIew(image_top);
		ImageUtil.recycleImageVIew(image_bottom);
		
		if (image_code != null) {
			ImageUtil.recycleImageVIew(image_code);
		}
		
		

		if (bitmap2dCode != null && bitmapBarCode != null) {

			if (bitmap2dCode.get() != null) {
				bitmap2dCode.get().recycle();
			}

			if (bitmapBarCode != null) {
				bitmapBarCode.recycle();
			}
		}
		
	}
}
