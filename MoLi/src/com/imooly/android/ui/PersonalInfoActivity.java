package com.imooly.android.ui;

import java.io.File;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.db.DB_User;
import com.imooly.android.entity.RspLogin;
import com.imooly.android.entity.RspSuccessCommon;
import com.imooly.android.entity.RspUserInfo;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.utils.BitmapUtils;
import com.imooly.android.utils.FileUtils;
import com.imooly.android.utils.ImageUtil;
import com.imooly.android.utils.ImageUtilOverKitkat;
import com.imooly.android.utils.PickPhotoUtils;
import com.imooly.android.utils.PickPhotoUtils.PickCallback;
import com.imooly.android.widget.CustomProgressDialog;
import com.imooly.android.widget.Toast;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * 个人信息画面
 * 
 * @author
 * 
 */
public class PersonalInfoActivity extends BaseActivity implements OnClickListener {

	private RelativeLayout rl_title, rl_nickname, rl_portrait;
	private TextView tv_title, text_nickname;
	private static String USER_LOGO = "user_logo";
	private static String USER_TEMP_LOGO = "user_temp_logo";

	private CustomProgressDialog dialog;
	private String mNickName = null;
	private ImageView image_portrait;
	private ImageUtil imageUtil;
	private ImageUtilOverKitkat imageUtilKK;
	private Bitmap bitmapAvatar;
	DisplayImageOptions defaultOptions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_info);

		logActivityName(this);

		defaultOptions = new DisplayImageOptions.Builder().displayer(new RoundedBitmapDisplayer(100)).cacheInMemory(true).cacheOnDisc(true)
				.imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();
		imageUtil = new ImageUtil(self, 250, 250, USER_LOGO, USER_TEMP_LOGO);
		imageUtilKK = new ImageUtilOverKitkat(self, 150, 150);

		initView();
		initData();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void initView() {

		// title
		rl_title = (RelativeLayout) findViewById(R.id.rl_title);

		findViewById(R.id.button_back).setOnClickListener(this);

		tv_title = (TextView) findViewById(R.id.tv_title);

		// 昵称
		rl_nickname = (RelativeLayout) findViewById(R.id.rl_nickname);
		rl_nickname.setOnClickListener(this);
		text_nickname = (TextView) findViewById(R.id.text_nickname);

		// 头像
		rl_portrait = (RelativeLayout) findViewById(R.id.rl_portrait);
		rl_portrait.setOnClickListener(this);
		image_portrait = (ImageView) findViewById(R.id.image_portrait);
	}

	private void initData() {
		// 从本地获取昵称和头像
		DB_User user = new DB_User(self);
		String nickName = user.getLoginData().getNickname();
		text_nickname.setText(nickName);

		ImageLoader.getInstance().displayImage(user.getLoginData().getAvatar(), image_portrait, defaultOptions);
	}

	@Override
	protected void onStart() {
		super.onStart();

		initData();
	}

	@Override
	public void onClick(View v) {

		Intent intent;
		switch (v.getId()) {
		case R.id.button_back:
			finish();
			break;
		case R.id.rl_nickname:
			String nickName = (String) text_nickname.getText();
			intent = new Intent(self, NicknameEditActivity.class);
			intent.putExtra("nick_name", nickName);
			startActivity(intent);

			break;

		case R.id.rl_portrait:
			PickPhotoUtils.showPickDialog(self, "修改我的头像", new PickCallback() {
				@Override
				public void onPhoto() {
					// TODO Auto-generated method stub
					if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
						imageUtilKK.selectPic();
					} else {
						imageUtil.getAndCropPhoto();
					}
				}

				@Override
				public void onCamera() {
					// TODO Auto-generated method stub
					imageUtil.takeCameraPhoto();
				}
			});

			break;
		default:
			break;
		}
	}

	private void uploadImage(byte[] bytes) {
		// 上传头像图片
		Api.updateAvatar(self, bytes, new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				RspSuccessCommon rsp = (RspSuccessCommon) rspData;
				RspSuccessCommon.Data data = rsp.data;
				if (data.getSuccess() == 1) {
					// image_portrait.setImageBitmap(bitmapAvatar);
					Api.userInfo(self, new NetCallBack<ServiceResult>() {
						@Override
						public void success(ServiceResult rspData) {
							// TODO Auto-generated method stub
							RspUserInfo rsp = (RspUserInfo) rspData;
							if (rsp.data != null) {
								String avatar = rsp.data.avatar;
								RspLogin.Data data = new DB_User(self).getLoginData();
								data.setAvatar(avatar);
								new DB_User(self).setLoginData(data);

								ImageLoader.getInstance().clearDiscCache();
								ImageLoader.getInstance().clearMemoryCache();
								ImageLoader.getInstance().displayImage(avatar, image_portrait, defaultOptions);
								Toast.show(self, "更新头像成功");
							}
						}

						@Override
						public void failed(String msg) {
							// TODO Auto-generated method stub
						}
					}, RspUserInfo.class);
				}
			}

			@Override
			public void failed(String msg) {
				Toast.show(self, msg);
			}

		}, RspSuccessCommon.class);
	}

	@SuppressLint("NewApi")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case ImageUtilOverKitkat.SELECT_PIC_KITKAT:
			imageUtilKK.cropPic(data.getData());
			Log.d("xx", "data.getData() = " + data.getData());
			break;
		case ImageUtilOverKitkat.HANDLE_PIC:
			Bundle extras = data.getExtras();
			if (extras != null) {
				Bitmap photo = extras.getParcelable("data");
				Log.d("xx", "extras=============================");
				uploadImage(BitmapUtils.bitmap2ByteArray(photo));
			}
			break;
		case ImageUtil.CAMERA_PIC:
			imageUtil.cropPhoto(Uri.fromFile(imageUtil.getPicFile()));
			break;

		case ImageUtil.HANDLE_PIC: // 上传图片
			File file = imageUtil.getPicFile();
			
			byte[] fileBytes = FileUtils.getInstance(this).getCodeByFile(file);
			uploadImage(fileBytes);
			break;
		default:
			break;
		}
	}

}
