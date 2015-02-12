package com.imooly.android.tool;

import java.util.Iterator;
import java.util.Map;

import com.imooly.android.ui.CollectionActivity;
import com.imooly.android.ui.MemberCardActivity;
import com.imooly.android.ui.MemberCenterActivity;
import com.imooly.android.ui.ProductDetailActivity;
import com.imooly.android.ui.StoreDetailActivity;
import com.imooly.android.ui.StoreProActivity;
import com.imooly.android.ui.VoucherDetailActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class ActivityHandoffUtil {
	public static String BN01 = "BN01";// 商品详情
	public static String SH01 = "SH01";// 店铺
	public static String PH01 = "PH01";// 实体店详情
	public static String CD01 = "CD01";// 电子会员卡
	public static String CD02 = "CD02";// 代金卷
	public static String MP01 = "MP01";// 会员特权
	public static String MC01 = "MC01";// 我的收藏

	public static void startActivity(Context context,String pageCode, Map<String, String> params) {
		Intent intent = new Intent();
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		/** 参数 */
		Bundle bundle;
		if (params != null) {
			bundle = new Bundle();
			Iterator<?> iterator = params.keySet().iterator();
			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				bundle.putString(key, params.get(key));
			}
			intent.putExtras(bundle);
		}

		if (BN01.equals(pageCode)) {
			// 商品详情
			intent.setClass(context, ProductDetailActivity.class);
		} else if (SH01.equals(pageCode)) {
			// 店铺
			intent.setClass(context, StoreProActivity.class);
		} else if (PH01.equals(pageCode)) {
			// 实体店详情
			intent.setClass(context, StoreDetailActivity.class);
		} else if (CD01.equals(pageCode)) {
			// 电子会员卡
			intent.setClass(context, MemberCardActivity.class);
		} else if (CD02.equals(pageCode)) {
			// 代金卷
			intent.setClass(context, VoucherDetailActivity.class);
		} else if (MP01.equals(pageCode)) {
			// 会员特权
			intent.setClass(context, MemberCenterActivity.class);
		} else if (MC01.equals(pageCode)) {
			// 我的收藏
			intent.setClass(context, CollectionActivity.class);
		}
		context.startActivity(intent);

	}

}
