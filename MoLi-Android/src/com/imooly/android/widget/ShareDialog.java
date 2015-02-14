package com.imooly.android.widget;

import java.io.Serializable;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.imooly.android.R;
import com.imooly.android.Net.Api;
import com.imooly.android.Net.NetUtils.NetCallBack;
import com.imooly.android.base.BaseActivity;
import com.imooly.android.entity.RspSharesInfo;
import com.imooly.android.entity.ServiceResult;
import com.imooly.android.enums.ShareObject;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.controller.listener.SocializeListeners.SnsPostListener;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.QZoneShareContent;
import com.umeng.socialize.media.SinaShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.SinaSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

/**
 * 分享对话框
 * 
 * @author daiye
 * 
 */
public class ShareDialog implements OnClickListener {

	private final UMSocialService mController = UMServiceFactory
			.getUMSocialService("com.umeng.share");
	private SHARE_MEDIA mPlatform_qq = SHARE_MEDIA.QQ;
	private SHARE_MEDIA mPlatform_sina = SHARE_MEDIA.SINA;
	private SHARE_MEDIA mPlatform_weixincircle = SHARE_MEDIA.WEIXIN_CIRCLE;
	private SHARE_MEDIA mPlatform_weixin = SHARE_MEDIA.WEIXIN;
	private SHARE_MEDIA mPlatform_qzone = SHARE_MEDIA.QZONE;

	private final String TYPE_WORD = "word";
	private final String TYPE_IMAGE = "image";
	private final String TYPE_WIMG = "wimg";
	private final String TYPE_APP = "app";

	private Dialog dialog;
	private BaseActivity activity;

	ShareObject so;
	String sid;

	public ShareDialog(BaseActivity activity, ShareObject so, String sid) {
		this.activity = activity;
		this.so = so;
		this.sid = sid;

		addCustomPlatforms();

		LayoutInflater mInflater = (LayoutInflater) activity
				.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		View view = mInflater.inflate(R.layout.dlg_share, null);

		ImageView iv_close = (ImageView) view.findViewById(R.id.iv_close);
		LinearLayout item_weixin = (LinearLayout) view
				.findViewById(R.id.item_weixin);
		LinearLayout item_qq = (LinearLayout) view.findViewById(R.id.item_qq);
		LinearLayout item_qqzone = (LinearLayout) view
				.findViewById(R.id.item_qqzone);
		LinearLayout item_friends = (LinearLayout) view
				.findViewById(R.id.item_friends);
		LinearLayout item_weibo = (LinearLayout) view
				.findViewById(R.id.item_weibo);
		item_weixin.setOnClickListener(this);
		item_qq.setOnClickListener(this);
		item_qqzone.setOnClickListener(this);
		item_friends.setOnClickListener(this);
		item_weibo.setOnClickListener(this);
		iv_close.setOnClickListener(this);

		dialog = new Dialog(activity, R.style.ShareDialogStyleBottom);
		dialog.setContentView(view);
		dialog.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.item_qq:
			directShare(mPlatform_qq);
			break;
		case R.id.item_weibo:
			directShare(mPlatform_sina);
			break;
		case R.id.item_friends:
			directShare(mPlatform_weixincircle);
			break;
		case R.id.item_weixin:
			directShare(mPlatform_weixin);
			break;
		case R.id.item_qqzone:
			directShare(mPlatform_qzone);
			break;
		case R.id.iv_close:
			dialog.dismiss();
			break;
		default:
			break;
		}
	}

	SnsPostListener mSnsPostListener;

	/**
	 * 直接分享，底层分享接口。如果分享的平台是新浪、腾讯微博、豆瓣、人人，则直接分享，无任何界面弹出； 其它平台分别启动客户端分享</br>
	 */
	private void directShare(final SHARE_MEDIA mPlatform) {
		dialog.dismiss();

		Api.getShareSInfo(activity, getObject(so), getPlatform(mPlatform),
				getParams(so), new NetCallBack<ServiceResult>() {
					@Override
					public void success(ServiceResult rspData) {
						RspSharesInfo rsp = (RspSharesInfo) rspData;
						RspSharesInfo.Data data = rsp.data;
						if (data == null) {
							Toast.show(activity, "获取分享信息失败");
						} else {
							String stype = data.getStype();
							switch (mPlatform) {
							case QQ:
								QQShareContent qqShareContent = new QQShareContent();
								if (stype.equals(TYPE_WORD)) {
									qqShareContent.setTitle(data.getTitle());
									qqShareContent.setShareContent(data
											.getWord());
									qqShareContent.setTargetUrl(data.getLink());
								} else if (stype.equals(TYPE_IMAGE)) {
									qqShareContent.setShareImage(new UMImage(
											activity, data.getImage()));
									qqShareContent.setTargetUrl(data.getLink());
								} else if (stype.equals(TYPE_WIMG)
										|| stype.equals(TYPE_APP)) {
									qqShareContent.setTitle(data.getTitle());
									qqShareContent.setShareContent(data
											.getWord());
									qqShareContent.setShareImage(new UMImage(
											activity, data.getImage()));
									qqShareContent.setTargetUrl(data.getLink());
								}
								mController.setShareMedia(qqShareContent);

								mController.postShare(activity, SHARE_MEDIA.QQ,
										new SnsPostListener() {
											@Override
											public void onStart() {
												// Toast.makeText(activity,
												// "开始分享.",
												// Toast.LENGTH_SHORT).show();
											}

											@Override
											public void onComplete(
													SHARE_MEDIA platform,
													int eCode,
													SocializeEntity entity) {
												// if (eCode == 200) {
												// Toast.makeText(activity,
												// "分享成功.",
												// Toast.LENGTH_SHORT).show();
												// } else {
												// Toast.makeText(activity,
												// "分享失败",Toast.LENGTH_SHORT).show();
												// }
											}
										});
								break;
							case SINA:
								SinaShareContent sinaContent = new SinaShareContent();
								if (stype.equals(TYPE_WORD)) {
									sinaContent.setTitle(data.getTitle());
									sinaContent.setShareContent(data.getWord());
									sinaContent.setTargetUrl(data.getLink());
								} else if (stype.equals(TYPE_IMAGE)) {
									sinaContent.setShareImage(new UMImage(
											activity, data.getImage()));
									sinaContent.setTargetUrl(data.getLink());
								} else if (stype.equals(TYPE_WIMG)
										|| stype.equals(TYPE_APP)) {
									sinaContent.setTitle(data.getTitle());
									sinaContent.setShareContent(data.getWord());
									sinaContent.setShareImage(new UMImage(
											activity, data.getImage()));
									sinaContent.setTargetUrl(data.getLink());
								}
								mController.setShareMedia(sinaContent);

								mController.postShare(activity,
										SHARE_MEDIA.SINA,
										new SnsPostListener() {
											@Override
											public void onStart() {
												// Toast.makeText(activity,
												// "开始分享.",
												// Toast.LENGTH_SHORT).show();
											}

											@Override
											public void onComplete(
													SHARE_MEDIA platform,
													int eCode,
													SocializeEntity entity) {
												// if (eCode == 200) {
												// Toast.makeText(activity,
												// "分享成功.",
												// Toast.LENGTH_SHORT).show();
												// } else {
												// Toast.makeText(activity,
												// "分享失败",
												// Toast.LENGTH_SHORT).show();
												// }
											}
										});
								break;
							case WEIXIN_CIRCLE:
								// 接口说明：
								// 微信分享必须设置targetURL，需要为http链接格式
								// 微信朋友圈只能显示title，并且过长会被微信截取部分内容
								// 设置微信朋友圈分享内容
								CircleShareContent circleMedia = new CircleShareContent();
								if (stype.equals(TYPE_WORD)) {
									circleMedia.setTitle(data.getTitle());
									circleMedia.setShareContent(data.getWord());
									circleMedia.setTargetUrl(data.getLink());
								} else if (stype.equals(TYPE_IMAGE)) {
									circleMedia.setShareImage(new UMImage(
											activity, data.getImage()));
									circleMedia.setTargetUrl(data.getLink());
								} else if (stype.equals(TYPE_WIMG)
										|| stype.equals(TYPE_APP)) {
									circleMedia.setTitle(data.getTitle());
									circleMedia.setShareContent(data.getWord());
									circleMedia.setShareImage(new UMImage(
											activity, data.getImage()));
									circleMedia.setTargetUrl(data.getLink());
								}
								mController.setShareMedia(circleMedia);

								mController.postShare(activity,
										SHARE_MEDIA.WEIXIN_CIRCLE,
										new SnsPostListener() {
											@Override
											public void onStart() {
												// Toast.makeText(activity,
												// "开始分享.",
												// Toast.LENGTH_SHORT).show();
											}

											@Override
											public void onComplete(
													SHARE_MEDIA platform,
													int eCode,
													SocializeEntity entity) {
												// if (eCode == 200) {
												// Toast.makeText(activity,
												// "分享成功.",
												// Toast.LENGTH_SHORT).show();
												// } else {
												// Toast.makeText(activity,
												// "分享失败",
												// Toast.LENGTH_SHORT).show();
												// }
											}
										});

								break;
							case WEIXIN:
								// 设置分享内容
								// 设置微信好友分享内容
								WeiXinShareContent weixinContent = new WeiXinShareContent();
								if (stype.equals(TYPE_WORD)) {
									weixinContent.setTitle(data.getTitle());
									weixinContent.setShareContent(data
											.getWord());
									weixinContent.setTargetUrl(data.getLink());
								} else if (stype.equals(TYPE_IMAGE)) {
									weixinContent.setShareImage(new UMImage(
											activity, data.getImage()));
									weixinContent.setTargetUrl(data.getLink());
								} else if (stype.equals(TYPE_WIMG)
										|| stype.equals(TYPE_APP)) {
									weixinContent.setTitle(data.getTitle());
									weixinContent.setShareContent(data
											.getWord());
									weixinContent.setShareImage(new UMImage(
											activity, data.getImage()));
									weixinContent.setTargetUrl(data.getLink());
								}
								mController.setShareMedia(weixinContent);

								mController.postShare(activity,
										SHARE_MEDIA.WEIXIN,
										new SnsPostListener() {
											@Override
											public void onStart() {
												// Toast.makeText(activity,
												// "开始分享.",
												// Toast.LENGTH_SHORT).show();
											}

											@Override
											public void onComplete(
													SHARE_MEDIA platform,
													int eCode,
													SocializeEntity entity) {
												// if (eCode == 200) {
												// Toast.makeText(activity,
												// "分享成功.",
												// Toast.LENGTH_SHORT).show();
												// } else {
												// Toast.makeText(activity,
												// "分享失败",
												// Toast.LENGTH_SHORT).show();
												// }
											}
										});

								break;
							case QZONE:
								QZoneShareContent qzone = new QZoneShareContent();
								if (stype.equals(TYPE_WORD)) {
									qzone.setTitle(data.getTitle());
									qzone.setShareContent(data.getWord());
									qzone.setTargetUrl(data.getLink());
								} else if (stype.equals(TYPE_IMAGE)) {
									qzone.setShareImage(new UMImage(activity,
											data.getImage()));
									qzone.setTargetUrl(data.getLink());
								} else if (stype.equals(TYPE_WIMG)
										|| stype.equals(TYPE_APP)) {
									qzone.setTitle(data.getTitle());
									qzone.setShareContent(data.getWord());
									qzone.setShareImage(new UMImage(activity,
											data.getImage()));
									qzone.setTargetUrl(data.getLink());
								}
								mController.setShareMedia(qzone);

								mController.postShare(activity,
										SHARE_MEDIA.QZONE,
										new SnsPostListener() {
											@Override
											public void onStart() {
												// Toast.makeText(activity,
												// "开始分享.",
												// Toast.LENGTH_SHORT).show();
											}

											@Override
											public void onComplete(
													SHARE_MEDIA platform,
													int eCode,
													SocializeEntity entity) {
												// if (eCode == 200) {
												// Toast.makeText(activity,
												// "分享成功.",
												// Toast.LENGTH_SHORT).show();
												// } else {
												// Toast.makeText(activity,
												// "分享失败"
												// ,Toast.LENGTH_SHORT).show();
												// }
											}
										});

								break;
							default:
								break;
							}
						}
					}

					@Override
					public void failed(String msg) {
						Toast.show(activity, "获取分享信息失败");
					}
				}, RspSharesInfo.class);
	}

	// public static final String Object_Goods = "goods";
	// public static final String Object_Estore = "estore";
	// public static final String Object_Fstore = "fstore";
	// public static final String Object_App = "app";
	private String getObject(ShareObject so) {
		switch (so) {
		case goods:
			return "goods";
		case estore:
			return "estore";
		case fstore:
			return "fstore";
		case app:
			return "app";
		default:
			return "";
		}
	}

	// public static final String Platform_Weibo = "weibo";
	// public static final String Platform_Qzone = "qzone";
	// public static final String Platform_QQmsg = "qqmsg";
	// public static final String Platform_WXmsg = "wxmsg";
	// public static final String Platform_WXcircle = "wxcircle";
	private String getPlatform(SHARE_MEDIA mPlatform) {
		switch (mPlatform) {
		case QQ:
			return "qqmsg";
		case SINA:
			return "weibo";
		case WEIXIN_CIRCLE:
			return "wxcircle";
		case WEIXIN:
			return "wxmsg";
		case QZONE:
			return "qzone";
		default:
			return "";
		}
	}

	private String getParams(ShareObject so) {
		switch (so) {
		case goods:
			Goods good = new Goods();
			good.goodsid = sid;
			return new Gson().toJson(good);
		case estore:
			EStore estore = new EStore();
			estore.estoreid = sid;
			return new Gson().toJson(estore);
		case fstore:
			EStore fstore = new EStore();
			fstore.estoreid = sid;
			return new Gson().toJson(fstore);
		case app:
			return "";
		default:
			return "";
		}
	}

	public class Goods implements Serializable {
		private static final long serialVersionUID = 1L;

		private String goodsid;

		public String getGoodsid() {
			return goodsid;
		}

		public void setGoodsid(String goodsid) {
			this.goodsid = goodsid;
		}
	}

	public class EStore implements Serializable {
		private static final long serialVersionUID = 1L;

		private String estoreid;

		public String getEstoreid() {
			return estoreid;
		}

		public void setEstoreid(String estoreid) {
			this.estoreid = estoreid;
		}
	}

	public class FStore implements Serializable {
		private static final long serialVersionUID = 1L;

		private String fstoreid;

		public String getFstoreid() {
			return fstoreid;
		}

		public void setFstoreid(String fstoreid) {
			this.fstoreid = fstoreid;
		}
	}

	/**
	 * 添加所有的平台</br>
	 */
	private void addCustomPlatforms() {
		// 添加微信平台
		addWXPlatform();
		// 添加QQ平台
		addQQQZonePlatform();

		// 设置新浪SSO handler
		mController.getConfig().setSsoHandler(new SinaSsoHandler());

//		initSina();

		mController.getConfig().setPlatforms(SHARE_MEDIA.WEIXIN,
				SHARE_MEDIA.WEIXIN_CIRCLE, SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE,
				SHARE_MEDIA.SINA);
	}

	/**
	 * @功能描述 : 添加微信平台分享
	 * @return
	 */
	private void addWXPlatform() {
		// 注意：在微信授权的时候，必须传递appSecret
		// wx967daebe835fbeac是你在微信开发平台注册应用的AppID, 这里需要替换成你注册的AppID
		String appId = "wx501bd7cea77cc83a";
		String appSecret = "89f629c822b71cabfe761f96265b4f71";
		// 添加微信平台
		UMWXHandler wxHandler = new UMWXHandler(activity, appId, appSecret);
		wxHandler.addToSocialSDK();
		// 支持微信朋友圈
		UMWXHandler wxCircleHandler = new UMWXHandler(activity, appId,
				appSecret);
		wxCircleHandler.setToCircle(true);
		wxCircleHandler.addToSocialSDK();
	}

	/**
	 * @功能描述 : 添加QQ平台支持 QQ分享的内容， 包含四种类型， 即单纯的文字、图片、音乐、视频. 参数说明 : title, summary,
	 *       image url中必须至少设置一个, targetUrl必须设置,网页地址必须以"http://"开头 . title :
	 *       要分享标题 summary : 要分享的文字概述 image url : 图片地址 [以上三个参数至少填写一个] targetUrl
	 *       : 用户点击该分享时跳转到的目标地址 [必填] ( 若不填写则默认设置为友盟主页 )
	 * @return
	 */
	private void addQQQZonePlatform() {
		// 添加QQ在分享列表页中
		// 参数1为当前Activity， 参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(activity,
				"1104102913", "2VoowDlyohMxwSDv");
		qqSsoHandler.addToSocialSDK();

		// 添加Qzone在分享列表页中
		// 参数1为当前Activity， 参数2为开发者在QQ互联申请的APP ID，参数3为开发者在QQ互联申请的APP kEY.
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(activity,
				"1104102913", "2VoowDlyohMxwSDv");
		qZoneSsoHandler.addToSocialSDK();
	}

//	private void initSina() { // 判断是否已授权
//		if (OauthHelper.isAuthenticated(activity, SHARE_MEDIA.SINA)) {
//			mController.doOauthVerify(activity, SHARE_MEDIA.SINA,
//				new UMAuthListener() {
//					@Override
//					public void onStart(SHARE_MEDIA platform) {
//						DialogUtils.showToast(activity, "授权开始");
//					}
//
//					@Override
//					public void onError(SocializeException e,
//							SHARE_MEDIA platform) {
//						DialogUtils.showToast(activity, "授权错误");
//					}
//
//					@Override
//					public void onComplete(Bundle value,
//							SHARE_MEDIA platform) {
//						DialogUtils.showToast(activity, "授权完成");
//						// 获取相关授权信息或者跳转到自定义的分享编辑页面
//						String uid = value.getString("uid");
//					}
//
//					@Override
//					public void onCancel(SHARE_MEDIA platform) {
//						DialogUtils.showToast(activity, "授权取消");
//					}
//				});
//		}
//	}
}
