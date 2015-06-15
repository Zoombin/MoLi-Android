package com.imooly.android.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.imooly.android.MoLiApplication;
import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.base.BaseFragment;
import com.imooly.android.db.DB_User;
import com.imooly.android.entity.RspCollectionNumbers;
import com.imooly.android.entity.RspMyMessageNumber;
import com.imooly.android.entity.RspOrderNumbers;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.tool.Config;
import com.imooly.android.tool.InitUtil;
import com.imooly.android.tool.InitUtil.InitCallBack;
import com.imooly.android.ui.AccountSafeActivity;
import com.imooly.android.ui.CollectionActivity;
import com.imooly.android.ui.FootStepActivity;
import com.imooly.android.ui.LoginActivity;
import com.imooly.android.ui.MemberCenterActivity;
import com.imooly.android.ui.MyMessageActivity;
import com.imooly.android.ui.OrderActivity;
import com.imooly.android.ui.PersonalInfoActivity;
import com.imooly.android.ui.ServiceActivity;
import com.imooly.android.ui.SettingsActivity;
import com.imooly.android.ui.VoucherActivity;
import com.imooly.android.utils.Utils;
import com.imooly.android.widget.Toast;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class MyCenterFragment extends BaseFragment implements View.OnClickListener {

	private Activity mActivity = null;
	private RelativeLayout relative_login_state = null;
	private TextView btnLoginRegister = null;
	private boolean isLogin = false;
	private TextView text_user_name = null;
	private TextView text_message_number = null;
	private TextView text_collection_number = null;
	private ImageView image_user_portrait = null;

	private int mGoodNumber, mFlagStoreNumber, mStoreNumber;
	private TextView text_payment, text_send, text_receive, text_comment;
	private int mMsgNumber;
	boolean loadAppId = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mActivity = getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_my_center, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		RelativeLayout ll_title_mycenter = (RelativeLayout) mActivity.findViewById(R.id.ll_title_mycenter);
		// fragment重叠bug
		ll_title_mycenter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// 不做操作
			}
		});

		RelativeLayout relative_account_safe = (RelativeLayout) mActivity.findViewById(R.id.relative_account_safe);
		relative_account_safe.setOnClickListener(this);

		RelativeLayout relative_all_order = (RelativeLayout) mActivity.findViewById(R.id.relative_all_order);
		relative_all_order.setOnClickListener(this);

		// service lable
		RelativeLayout relative_service = (RelativeLayout) mActivity.findViewById(R.id.relative_service);
		relative_service.setOnClickListener(this);

		// my collection
		RelativeLayout relative_my_collection = (RelativeLayout) mActivity.findViewById(R.id.relative_my_collection);
		relative_my_collection.setOnClickListener(this);

		// footmark
		RelativeLayout relative_my_footstep = (RelativeLayout) mActivity.findViewById(R.id.relative_my_footstep);
		relative_my_footstep.setOnClickListener(this);

		// 消息
		RelativeLayout relative_my_message = (RelativeLayout) mActivity.findViewById(R.id.relative_my_message);
		relative_my_message.setOnClickListener(this);

		// 代金券
		RelativeLayout relative_rouncher = (RelativeLayout) mActivity.findViewById(R.id.relative_voucher);
		relative_rouncher.setOnClickListener(this);

		// 会员中心
		RelativeLayout ralative_member_center = (RelativeLayout) mActivity.findViewById(R.id.ralative_member_center);
		ralative_member_center.setOnClickListener(this);

		// 设置
		Button button = (Button) mActivity.findViewById(R.id.Button_settings);
		button.setOnClickListener(this);

		// 代付款
		Button button_pay = (Button) mActivity.findViewById(R.id.button_pay);
		button_pay.setOnClickListener(this);

		// 待评价
		Button button_evaluation = (Button) mActivity.findViewById(R.id.button_comment);
		button_evaluation.setOnClickListener(this);

		// 待收货
		Button button_reciever = (Button) mActivity.findViewById(R.id.button_reciever);
		button_reciever.setOnClickListener(this);

		// 带发货
		Button button_delivery = (Button) mActivity.findViewById(R.id.button_delivery);
		button_delivery.setOnClickListener(this);

		relative_login_state = (RelativeLayout) mActivity.findViewById(R.id.relative_login_state);
		btnLoginRegister = (TextView) mActivity.findViewById(R.id.text_login_register);
		btnLoginRegister.setOnClickListener(this);

		// 修改信息
		TextView text_modify_info = (TextView) mActivity.findViewById(R.id.text_modify_info);
		text_modify_info.setOnClickListener(this);

		// nickname
		text_user_name = (TextView) mActivity.findViewById(R.id.text_user_name);
		text_message_number = (TextView) mActivity.findViewById(R.id.text_message_number);

		// 用户头像
		image_user_portrait = (ImageView) mActivity.findViewById(R.id.image_user_portrait);

		// 收藏栏多少件商品
		text_collection_number = (TextView) mActivity.findViewById(R.id.text_collection_number);

		// 待付款
		text_payment = (TextView) mActivity.findViewById(R.id.text_Payment);

		// 待发货
		text_send = (TextView) mActivity.findViewById(R.id.text_send);

		// 待收货
		text_receive = (TextView) mActivity.findViewById(R.id.text_receive);

		// 待评价
		text_comment = (TextView) mActivity.findViewById(R.id.text_comment);
	}

	@Override
	public void onStart() {

		Log.e("xxx", "isLogin = " + isLogin);

		// 判断用回是否登录
		MoLiApplication application = MoLiApplication.getInstance();

		Log.e("xxx", "applicatin isLogin = " + application.isLogin());

		if (application.isLogin() == true) { // 登录成功
			this.isLogin = true;
			relative_login_state.setVisibility(View.VISIBLE);
			btnLoginRegister.setVisibility(View.GONE);

			// 设置用户昵称
			DB_User user = new DB_User(mActivity);
			
			if (TextUtils.isEmpty(user.getLoginData().getNickname())) {
				text_user_name.setText(user.getUserName());  // 用户昵称为空，设置为用户名（手机号）
			}else {
				text_user_name.setText(user.getLoginData().getNickname());
			}

			// 设置用户头像

			displayUserPortrait(user.getLoginData().getAvatar());

			// 获取订单的详情
			getOrderNumbers();

			// 获取消息个数
			getMessageNumber();

			// 获取收藏个数
			getCollectionNumber();

		} else { // 登录失败

			this.isLogin = false;
			relative_login_state.setVisibility(View.GONE);
			btnLoginRegister.setVisibility(View.VISIBLE);

			// 设置不显示消息
			text_message_number.setVisibility(View.INVISIBLE);
			// 设置订单个数不显示
			text_payment.setVisibility(View.INVISIBLE);
			text_send.setVisibility(View.INVISIBLE);
			text_receive.setVisibility(View.INVISIBLE);
			text_comment.setVisibility(View.INVISIBLE);

			// 设置收藏个数不显示
			text_collection_number.setVisibility(View.INVISIBLE);
		}

		super.onStart();
	}

	private void displayUserPortrait(String url) {
		if(TextUtils.isEmpty(url)){
			return;
		}
		DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder().displayer(new RoundedBitmapDisplayer(200))
				.cacheInMemory(true).cacheOnDisc(true).imageScaleType(ImageScaleType.EXACTLY_STRETCHED).build();
		ImageLoader.getInstance().displayImage(url, image_user_portrait, defaultOptions);
	}

	private void getCollectionNumber() {
		Api.myCollectionNumbers(mActivity, null, new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {
				RspCollectionNumbers rsp = (RspCollectionNumbers) rspData;
				RspCollectionNumbers.Data data = rsp.data;
				mGoodNumber = data.goods;
				mStoreNumber = data.business;
				mFlagStoreNumber = data.store;
				text_collection_number.setVisibility(View.VISIBLE);
				text_collection_number.setText(String.format("%d件商品", mGoodNumber));

			}

			@Override
			public void failed(String msg) {
				// TODO Auto-generated method stub

			}
		}, RspCollectionNumbers.class);
	}

	private void getMessageNumber() {

		Api.myMessageNumber(mActivity, Utils.getNowTime(), new NetCallBack<ServiceResult>() {
			@Override
			public void success(ServiceResult rspData) {
				RspMyMessageNumber rsp = (RspMyMessageNumber) rspData;
				RspMyMessageNumber.Data data = rsp.data;
				text_message_number.setVisibility(View.VISIBLE);
				text_message_number.setText(String.valueOf(data.getNum()) + "条消息未读");
			}

			@Override
			public void failed(String msg) {
				Toast.show(mActivity, msg);
				mMsgNumber = 0;
			}

		}, RspMyMessageNumber.class);

	}

	private void getOrderNumbers() {
		Api.getOrderNumbers(mActivity, new NetCallBack<ServiceResult>() {

			@Override
			public void success(ServiceResult rspData) {
				RspOrderNumbers rsp = (RspOrderNumbers) (rspData);
				RspOrderNumbers.Data data = rsp.data;

				Log.d("xxx", "RspOrderNumbers.Data = " + data.getForpay());

				// 待付款
				if (data.getForpay() != 0) {
					text_payment.setVisibility(View.VISIBLE);
					if (data.getForpay() < 100) {
						text_payment.setText(String.valueOf(data.getForpay()));
					} else {
						text_payment.setText("...");
					}

				} else {
					text_payment.setVisibility(View.GONE);
				}

				// 待发货
				if (data.getForsend() != 0) {
					text_send.setVisibility(View.VISIBLE);
					if (data.getForsend() < 100) {
						text_send.setText(String.valueOf(data.getForsend()));
					} else {
						text_send.setText(String.valueOf("..."));
					}
				} else {
					text_send.setVisibility(View.GONE);
				}

				// 待收货
				if (data.getFortake() != 0) {
					text_receive.setVisibility(View.VISIBLE);
					if (data.getFortake() < 100) {
						text_receive.setText(String.valueOf(data.getFortake()));
					} else {
						text_receive.setText(String.valueOf("..."));
					}

				} else {
					text_receive.setVisibility(View.GONE);
				}

				// 待评价
				if (data.getForcomment() != 0) {
					text_comment.setVisibility(View.VISIBLE);
					if (data.getForcomment() < 100) {
						text_comment.setText(String.valueOf(data.getForcomment()));
					} else {
						text_comment.setText(String.valueOf("..."));
					}

				} else {
					text_comment.setVisibility(View.GONE);
				}
			}

			@Override
			public void failed(String msg) {
				Toast.show(mActivity, msg);
			}
		}, RspOrderNumbers.class);

	}

	@Override
	public void onClick(final View v) {
		// 没有网络进来的时候初始化APP
		if(loadAppId){
			return;
		}
		if (TextUtils.isEmpty(Config.getAppID())) {
			loadAppId = true;
			new InitUtil(mActivity).start(new InitCallBack() {
				@Override
				public void success() {
					// TODO Auto-generated method stub
					loadAppId = false;
					onClick(v);
				}
				@Override
				public void failed(String msg) {
					// TODO Auto-generated method stub
					loadAppId = false;
					Toast.show(mActivity, "网络连接异常");
				}
			});
			return;
		}
		
		switch (v.getId()) {
		case R.id.text_login_register:

			Intent intent = new Intent(mActivity, LoginActivity.class);
			mActivity.startActivity(intent);
			break;

		// 账户与安全栏
		case R.id.relative_account_safe:

			if (isLogin == true) {
				Intent intent2 = new Intent(mActivity, AccountSafeActivity.class);
				mActivity.startActivity(intent2);
			} else {
				mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
			}

			break;

		// 所有订单栏
		case R.id.relative_all_order:

			if (isLogin == true) {
				startActivity(new Intent(mActivity, OrderActivity.class).putExtra("page", OrderActivity.PAGE_ALL));
			} else {
				mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
			}
			break;

		// 服务栏
		case R.id.relative_service:
			if (isLogin == true) {
				startActivity(new Intent(mActivity, ServiceActivity.class));
			} else {
				mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
			}
			break;

		// 我的收藏栏
		case R.id.relative_my_collection:

			if (isLogin == true) {
				Intent collectionIntent = new Intent(mActivity, CollectionActivity.class);
				mActivity.startActivity(collectionIntent);
			} else {
				mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
			}

			break;

		// 我的足迹栏
		case R.id.relative_my_footstep:

			if (isLogin == true) {
				startActivity(new Intent(mActivity, FootStepActivity.class));
			} else {
				mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
			}
			break;

		// 我的消息栏
		case R.id.relative_my_message:

			if (isLogin == true) {
				startActivity(new Intent(mActivity, MyMessageActivity.class));
			} else {
				mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
			}

			break;

		// 设置按钮
		case R.id.Button_settings:

			startActivity(new Intent(mActivity, SettingsActivity.class));

			break;

		// 待付款按钮
		case R.id.button_pay:

			if (isLogin == true) {
				startActivity(new Intent(mActivity, OrderActivity.class).putExtra("page", OrderActivity.PAGE_NONPAY));
			} else {
				mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
			}

			break;

		// 待付款评价
		case R.id.button_comment:

			if (isLogin == true) {
				startActivity(new Intent(mActivity, OrderActivity.class).putExtra("page", OrderActivity.PAGE_NONDELIVER));
			} else {
				mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
			}

			break;

		// 待收货
		case R.id.button_reciever:

			if (isLogin == true) {
				startActivity(new Intent(mActivity, OrderActivity.class).putExtra("page", OrderActivity.PAGE_NONHOLD));
			} else {
				mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
			}

			break;

		// 待发货
		case R.id.button_delivery:

			if (isLogin == true) {
				startActivity(new Intent(mActivity, OrderActivity.class).putExtra("page", OrderActivity.PAGE_NONCOMMENT));
			} else {
				mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
			}

			break;

		// 代金券
		case R.id.relative_voucher:

			if (isLogin == true) {
				startActivity(new Intent(mActivity, VoucherActivity.class));
			} else {
				mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
			}

			break;

		// 会员中心
		case R.id.ralative_member_center:

			if (isLogin == true) {
				startActivity(new Intent(mActivity, MemberCenterActivity.class));
			} else {
				mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
			}

			break;

		// 个人信息
		case R.id.text_modify_info:
			startActivity(new Intent(mActivity, PersonalInfoActivity.class));
			break;

		default:
			break;

		}

	}

}
