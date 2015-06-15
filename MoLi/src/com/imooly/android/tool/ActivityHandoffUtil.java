package com.imooly.android.tool;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.imooly.android.entity.PushEntity.ParamsEty;
import com.imooly.android.ui.CollectionActivity;
import com.imooly.android.ui.MemberCardActivity;
import com.imooly.android.ui.MemberCenterActivity;
import com.imooly.android.ui.ProductDetailActivity;
import com.imooly.android.ui.StoreDetailActivity;
import com.imooly.android.ui.StoreProActivity;
import com.imooly.android.ui.VoucherActivity;

public class ActivityHandoffUtil {
	public static String BN01 = "BN01";// 商品详情
	public static String SH01 = "SH01";// 旗舰店
	public static String PH01 = "PH01";// 实体店详情
	public static String CD01 = "CD01";// 电子会员卡
	public static String CD02 = "CD02";// 代金卷
	public static String MP01 = "MP01";// 会员特权
	public static String MC01 = "MC01";// 我的收藏

	public static void startActivity(Context context,String pageCode, ParamsEty paramsEty) {
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		/** 参数 */
		Bundle bundle = new Bundle();
		bundle.putString("pushAction", "goHomepage");
		bundle.putString("bclassifyid", paramsEty.getBclassifyid());//商家分类（不知道具体是哪里）

		if (BN01.equals(pageCode)) {
			// 商品详情
			bundle.putString(ProductDetailActivity.EXTRA_GOODSID, paramsEty.getGoodsid());
			intent.setClass(context, ProductDetailActivity.class);
		} else if (SH01.equals(pageCode)) {
			// 旗舰店
			bundle.putString(StoreProActivity.EXTRA_BUSINESSID, paramsEty.getFstoreid());
			intent.setClass(context, StoreProActivity.class);
		} else if (PH01.equals(pageCode)) {
			// 实体店详情
			bundle.putString(StoreDetailActivity.EXTRA_BUSNESSID, paramsEty.getEstoreid());
			intent.setClass(context, StoreDetailActivity.class);
		} else if (CD01.equals(pageCode)) {
			// 电子会员卡
			intent.setClass(context, MemberCardActivity.class);
		} else if (CD02.equals(pageCode)) {
			// 代金卷
			intent.setClass(context, VoucherActivity.class);
		} else if (MP01.equals(pageCode)) {
			// 会员特权
			intent.setClass(context, MemberCenterActivity.class);
		} else if (MC01.equals(pageCode)) {
			// 我的收藏
			intent.setClass(context, CollectionActivity.class);
		}
		intent.putExtras(bundle);
		context.startActivity(intent);

	}

}
