package com.imooly.android.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooly.android.MoLiApplication;
import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.db.DB_User;
import com.imooly.android.db.DataHelper;
import com.imooly.android.entity.RspSuccessCommon;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.enums.ShareObject;
import com.imooly.android.tool.Config;
import com.imooly.android.view.UpdateDialog;
import com.imooly.android.widget.ShareDialog;
import com.imooly.android.widget.Toast;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.message.PushAgent;

public class SettingsActivity extends BaseActivity implements OnClickListener {

	private RelativeLayout rl_title;

	private Button button_exit, button_clear_all;
	private ImageView button_back;
	private TextView tv_title;

	private TextView text_about_us;

	private RelativeLayout rl_about, rl_check_update, rl_share_app;

	private RelativeLayout rl_auto, rl_normal, rl_hight;

	private ImageView image_auto, image_normal, image_hight;

	private ImageView image_accept, image_voice;

	boolean clean = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);

		logActivityName(this);

		initView();
	}

	private void initView() {
		
		// title
		rl_title = (RelativeLayout) findViewById(R.id.rl_title);
		button_back = (ImageView) findViewById(R.id.button_back);
		button_back.setOnClickListener(this);
		button_exit = (Button) findViewById(R.id.button_exit);
		button_exit.setOnClickListener(this);
		tv_title = (TextView) findViewById(R.id.tv_title);

		// 关于
		rl_about = (RelativeLayout) findViewById(R.id.rl_about);
		rl_about.setOnClickListener(this);

		// 检测更新
		rl_check_update = (RelativeLayout) findViewById(R.id.rl_check_update);
		rl_check_update.setOnClickListener(this);

		// 分享app
		rl_share_app = (RelativeLayout) findViewById(R.id.rl_share_app);
		rl_share_app.setOnClickListener(this);

		// 智能模式
		rl_auto = (RelativeLayout) findViewById(R.id.rl_auto);
		rl_auto.setOnClickListener(this);

		image_auto = (ImageView) findViewById(R.id.image_auto);

		// 普通模式
		rl_normal = (RelativeLayout) findViewById(R.id.rl_normal);
		rl_normal.setOnClickListener(this);

		image_normal = (ImageView) findViewById(R.id.image_normal);

		// 高质量模式
		rl_hight = (RelativeLayout) findViewById(R.id.rl_hight);
		rl_hight.setOnClickListener(this);

		image_hight = (ImageView) findViewById(R.id.image_hight);

		// 通知设置
		findViewById(R.id.rl_notification).setOnClickListener(this);
		findViewById(R.id.rl_push_voice_notice).setOnClickListener(this);
		image_accept = (ImageView) findViewById(R.id.image_accept);
		image_voice = (ImageView) findViewById(R.id.image_voice);

		button_clear_all = (Button) findViewById(R.id.button_clear_all);
		button_clear_all.setOnClickListener(this);

		setUI();

	}

	private void setUI() {
		
		// 是否显示退出按钮
		if(((MoLiApplication)getApplication()).isLogin()) {
			button_exit.setVisibility(View.VISIBLE);
		} else {
			button_exit.setVisibility(View.GONE);
		}
		

		if (TextUtils.equals(Config.getPicQuality(), "auto")) {
			image_auto.setVisibility(View.VISIBLE);
		}

		if (TextUtils.equals(Config.getPicQuality(), "WIFI")) {
			image_hight.setVisibility(View.VISIBLE);
		}

		if (TextUtils.equals(Config.getPicQuality(), "3G")) {
			image_normal.setVisibility(View.VISIBLE);
		}

		if (Config.getPushEnable()) {
			image_accept.setVisibility(View.VISIBLE);
		} else {
			image_accept.setVisibility(View.GONE);
		}

		if (Config.getPushNotice()) {
			image_voice.setVisibility(View.VISIBLE);
		} else {
			image_voice.setVisibility(View.GONE);
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

		case R.id.button_back:

			this.finish();

			break;

		case R.id.rl_about:

			Intent intent = new Intent(SettingsActivity.this, AboutUsActivity.class);
			SettingsActivity.this.startActivity(intent);

			break;
		case R.id.rl_check_update:
			new UpdateDialog(this).checkUpdate();
			break;

		case R.id.rl_share_app:
			new ShareDialog(self, ShareObject.app, "");
			break;
		case R.id.rl_normal:
			setPicQualityType(R.id.rl_normal);
			break;
		case R.id.rl_hight:
			setPicQualityType(R.id.rl_hight);
			break;
		case R.id.rl_auto:
			setPicQualityType(R.id.rl_auto);
			break;
		case R.id.rl_notification:
			if (image_accept.getVisibility() == View.VISIBLE) {
				image_accept.setVisibility(View.GONE);
				Config.setPushEnable(false);
				PushAgent.getInstance(self).disable();
			} else {
				image_accept.setVisibility(View.VISIBLE);
				Config.setPushEnable(true);
				PushAgent.getInstance(self).enable();
			}
			break;
		case R.id.rl_push_voice_notice:
			if (image_voice.getVisibility() == View.VISIBLE) {
				image_voice.setVisibility(View.GONE);
				Config.setPushNotice(false);
			} else {
				image_voice.setVisibility(View.VISIBLE);
				Config.setPushNotice(true);
			}
			break;
		case R.id.button_exit:

			Api.logout(self, new NetCallBack<ServiceResult>() {

				@Override
				public void success(ServiceResult rspData) {

					RspSuccessCommon rsp = (RspSuccessCommon) rspData;
					RspSuccessCommon.Data data = rsp.data;

					if (data.getSuccess() == 1) {
						Toast.show(self, "用户退出成功！");
						SettingsActivity.this.finish();
						MoLiApplication application = MoLiApplication.getInstance();
						application.setLogin(false);
						new DB_User(self).setSigntoken("");
						DataHelper.getInstance().deleteFootstep();//删除足迹
						//清除缓存
						ImageLoader.getInstance().clearDiscCache();
						ImageLoader.getInstance().clearMemoryCache();
					} else {
						Toast.show(self, "用户退出失败！");
					}
				}

				@Override
				public void failed(String msg) {

					Toast.show(self, "用户退出失败！");
				}
			}, RspSuccessCommon.class);
			break;
		case R.id.button_clear_all: // 缓存清除

			if (clean) {
				Toast.show(self, "没有可清除的缓存!");
				return;
			}
			ImageLoader.getInstance().clearMemoryCache();
			ImageLoader.getInstance().clearDiscCache();
			Toast.show(self, "清除缓存成功!");
			clean = true;
			break;
		default:
			break;

		}

	}

	private void setPicQualityType(int resID) {
		image_auto.setVisibility(View.GONE);
		image_hight.setVisibility(View.GONE);
		image_normal.setVisibility(View.GONE);

		switch (resID) {

		case R.id.rl_normal:
			image_normal.setVisibility(View.VISIBLE);
			Config.setPicQuality("3G");
			break;
		case R.id.rl_hight:
			image_hight.setVisibility(View.VISIBLE);
			Config.setPicQuality("WIFI");
			break;
		case R.id.rl_auto:
			image_auto.setVisibility(View.VISIBLE);
			Config.setPicQuality("auto");
			break;

		default:
			break;
		}

	}

}
