package com.imooly.android.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.db.DB_User;
import com.imooly.android.entity.RspSuccessCommon;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.utils.BitmapUtils;
import com.imooly.android.utils.ImageUtil;
import com.imooly.android.utils.PickPhotoUtils;
import com.imooly.android.utils.PickPhotoUtils.PickCallback;
import com.imooly.android.widget.CustomProgressDialog;
import com.imooly.android.widget.Toast;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

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

	private CustomProgressDialog dialog;
	private String mNickName = null;
	private ImageView image_portrait;
	private ImageUtil imageUtil;
	private Bitmap bitmapAvatar;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_personal_info);

		logActivityName(this);

		imageUtil = new ImageUtil(self, 150, 150, USER_LOGO, USER_LOGO);

		initView();
		initData();

	}

	@Override
	protected void onStart() {

		// 从本地获得用户昵称
		DB_User user = new DB_User(self);
		String nickName = user.getLoginData().getNickname();
		text_nickname.setText(nickName);

		// 头像的设置
		if (bitmapAvatar != null) {
			image_portrait.setImageBitmap(bitmapAvatar);
		}
		
		super.onStart();
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
		
		imageLoader.loadImage(user.getLoginData().getAvatar(), new SimpleImageLoadingListener() {
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
				super.onLoadingComplete(imageUri, view, loadedImage);
				image_portrait.setImageBitmap(BitmapUtils.getRoundedCornerBitmap(loadedImage));
			}
		});

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
					imageUtil.getAndCropPhoto();
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
					image_portrait.setImageBitmap(bitmapAvatar);
					Toast.show(self, "更新头像成功");
				}

			}

			@Override
			public void failed(String msg) {
				Toast.show(self, msg);
			}

		}, RspSuccessCommon.class);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (resultCode != RESULT_OK) {
			return;
		}
		switch (requestCode) {
		case ImageUtil.CAMERA_PIC:
			
			imageUtil.cropPhoto(Uri.fromFile(imageUtil.getPicFile()));

			break;
			
		case ImageUtil.HANDLE_PIC:
			
			bitmapAvatar = imageUtil.decodeUriAsBitmap(self, Uri.fromFile(imageUtil.getPicFile()));
			
			bitmapAvatar = BitmapUtils.getRoundedCornerBitmap(bitmapAvatar);
			
			byte[] bytes = BitmapUtils.bitmap2ByteArray(bitmapAvatar);
			
			uploadImage(bytes);

			break;
		default:
			break;
		}
	}

}
