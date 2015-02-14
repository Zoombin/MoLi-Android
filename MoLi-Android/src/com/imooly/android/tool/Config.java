package com.imooly.android.tool;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.List;

import org.apache.commons.codec.binary.Base64;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.DisplayMetrics;

import com.imooly.android.MoLiApplication;
import com.imooly.android.entity.RspAdvertiseIndexads;
import com.imooly.android.entity.RspBusinessCirclelist;
import com.imooly.android.entity.RspBusinessClassifyList.ClassifyEntity;
import com.imooly.android.entity.RspBusinessStoreList;
import com.imooly.android.entity.RspGoodsClassList;
import com.imooly.android.entity.RspStoreCityList;

/**
 * SharedPreferences配置信息
 * 
 * @author daiye
 * 
 */
public class Config {

	// 微信APP_ID
	public static final String WX_APP_ID = "";

	// 腾讯APP_ID
	public static final String Tencent_APP_ID = "";

	// 用户APP_ID
	public static final String APP_ID = "APP_ID";

	// APP推送ALIAS_TYPE
	public static final String IMOOLY_APP = "IMOOLY_APP";

	// 用户APP_SECRET
	public static final String APP_SECRET = "APP_SECRET";

	// 用户信息
	private static String USER_INFO = "USER_INFO";

	// 第一次启动
	private static String FIRST_START = "FIRST_START";

	// 教程查看
	private static String TUTORIAL_START = "TUTORIAL_START";

	// 商品分类最后更新时间
	private static String CATEGORY_LASTPULLTIME = "CATEGORY_LASTPULLTIME";

	// 实体店分类分类最后更新时间
	private static String BUSINESSCLASS_LASTPULLTIME = "BUSINESSCLASS_LASTPULLTIME";

	// 商品分类信息
	private static String CATEGORY_INFO = "CATEGORY_INFO";

	// 首页信息
	private static String HOME_INFO = "HOME_INFO";
	
	// 实体店信息
	private static String STORE_INFO = "STORE_INFO";
	
	// 商圈信息
	private static String BUSINESSCIRCLEINFO = "BUSINESSCIRCLEINFO";
	
	// 城市信息
	private static String STORECITYINFO = "STORECITYINFO";
	
	// 猜你喜欢
	private static String BUSINESSSTOREINFO = "BUSINESSSTOREINFO";
	
	// 实体店分类信息
	private static String BUSINESSCLASS_LIST = "BUSINESSCLASS_INFO";

	// 购物车同步最后更新时间
	private static String LASTSYNCTIME = "LASTSYNCTIME";

	// 设置--选择图片质量
	public static final String PIC_TYPE = "pic_type";

	//推送可用
	public static final String PUSH_ENABLE = "push_enable";
	
	//推送声音通知
	public static final String PUSH_NOTICE = "push_notice";

	public static SharedPreferences UserInfoPreferences = MoLiApplication.getContext().getSharedPreferences(USER_INFO, 0);

	public static boolean getPushNotice() {
		return UserInfoPreferences.getBoolean(PUSH_NOTICE, true);
	}
	
	public static void setPushNotice(boolean enable) {
		UserInfoPreferences.edit().putBoolean(PUSH_NOTICE, enable).commit();
	}
	
	public static boolean getPushEnable() {
		return UserInfoPreferences.getBoolean(PUSH_ENABLE, true);
	}
	
	public static void setPushEnable(boolean enable) {
		UserInfoPreferences.edit().putBoolean(PUSH_ENABLE, enable).commit();
	}

	public static String getPicQuality() {
		return UserInfoPreferences.getString(PIC_TYPE, "auto");
	}

	public static void setPicQuality(String type) {
		UserInfoPreferences.edit().putString(PIC_TYPE, type).commit();
	}

	public static boolean isFirst() {
		return UserInfoPreferences.getBoolean(FIRST_START, true);
	}

	public static void setFirst(boolean isFirst) {
		UserInfoPreferences.edit().putBoolean(FIRST_START, isFirst).commit();
	}

	public static boolean isTutorial() {
		return UserInfoPreferences.getBoolean(TUTORIAL_START, false);
	}

	public static void setTutorial(boolean isTutorial) {
		UserInfoPreferences.edit().putBoolean(TUTORIAL_START, isTutorial).commit();
	}

	public static String getAppID() {
		return UserInfoPreferences.getString(APP_ID, "");
	}

	public static void setAppID(String appid) {
		UserInfoPreferences.edit().putString(APP_ID, appid).commit();
	}

	public static String getAppSecret() {
		return UserInfoPreferences.getString(APP_SECRET, "");
	}

	public static void setAppSecret(String appsecret) {
		UserInfoPreferences.edit().putString(APP_SECRET, appsecret).commit();
	}

	public static String getCategoryLastpulltime() {
		return UserInfoPreferences.getString(CATEGORY_LASTPULLTIME, "");
	}

	public static void setCategoryLastpulltime(String categorylastpulltime) {
		UserInfoPreferences.edit().putString(CATEGORY_LASTPULLTIME, categorylastpulltime).commit();
	}

	public static String getBusinessClassLastpulltime() {
		return UserInfoPreferences.getString(BUSINESSCLASS_LASTPULLTIME, "");
	}

	public static void setBusinessClassLastpulltime(String BusinessClasslastpulltime) {
		UserInfoPreferences.edit().putString(BUSINESSCLASS_LASTPULLTIME, BusinessClasslastpulltime).commit();
	}

	public static String getLastsynctime() {
		return UserInfoPreferences.getString(LASTSYNCTIME, "");
	}

	public static void setLastsynctime(String lastsynctime) {
		UserInfoPreferences.edit().putString(LASTSYNCTIME, lastsynctime).commit();
	}
	
	public static void setBusinessStoreList(RspBusinessStoreList rspbusinessstorelist) {
		SharedPreferences preferences = MoLiApplication.getContext().getSharedPreferences("base64", Context.MODE_PRIVATE);
		// 创建字节输出流
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			// 创建对象输出流，并封装字节流
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			// 将对象写入字节流
			oos.writeObject(rspbusinessstorelist);
			// 将字节流编码成base64的字符窜
			String oAuth_Base64 = new String(Base64.encodeBase64(baos.toByteArray()));
			Editor editor = preferences.edit();
			editor.putString(BUSINESSSTOREINFO, oAuth_Base64);

			editor.commit();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static RspBusinessStoreList getBusinessStoreList() {
		RspBusinessStoreList rspbusinessstorelist = null;
		SharedPreferences preferences = MoLiApplication.getContext().getSharedPreferences("base64", Context.MODE_PRIVATE);
		String productBase64 = preferences.getString(BUSINESSSTOREINFO, "");

		// 读取字节
		byte[] base64 = Base64.decodeBase64(productBase64.getBytes());

		// 封装到字节流
		ByteArrayInputStream bais = new ByteArrayInputStream(base64);
		try {
			// 再次封装
			ObjectInputStream bis = new ObjectInputStream(bais);
			try {
				// 读取对象
				rspbusinessstorelist = (RspBusinessStoreList) bis.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rspbusinessstorelist;
	}
	
	public static void setStoreCityList(RspStoreCityList rspstorecitylist) {
		SharedPreferences preferences = MoLiApplication.getContext().getSharedPreferences("base64", Context.MODE_PRIVATE);
		// 创建字节输出流
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			// 创建对象输出流，并封装字节流
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			// 将对象写入字节流
			oos.writeObject(rspstorecitylist);
			// 将字节流编码成base64的字符窜
			String oAuth_Base64 = new String(Base64.encodeBase64(baos.toByteArray()));
			Editor editor = preferences.edit();
			editor.putString(STORECITYINFO, oAuth_Base64);

			editor.commit();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static RspStoreCityList getStoreCityList() {
		RspStoreCityList rspstorecitylist = null;
		SharedPreferences preferences = MoLiApplication.getContext().getSharedPreferences("base64", Context.MODE_PRIVATE);
		String productBase64 = preferences.getString(STORECITYINFO, "");

		// 读取字节
		byte[] base64 = Base64.decodeBase64(productBase64.getBytes());

		// 封装到字节流
		ByteArrayInputStream bais = new ByteArrayInputStream(base64);
		try {
			// 再次封装
			ObjectInputStream bis = new ObjectInputStream(bais);
			try {
				// 读取对象
				rspstorecitylist = (RspStoreCityList) bis.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rspstorecitylist;
	}
	
	public static void setBusinessCirclelist(RspBusinessCirclelist rspbusinesscirclelist) {
		SharedPreferences preferences = MoLiApplication.getContext().getSharedPreferences("base64", Context.MODE_PRIVATE);
		// 创建字节输出流
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			// 创建对象输出流，并封装字节流
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			// 将对象写入字节流
			oos.writeObject(rspbusinesscirclelist);
			// 将字节流编码成base64的字符窜
			String oAuth_Base64 = new String(Base64.encodeBase64(baos.toByteArray()));
			Editor editor = preferences.edit();
			editor.putString(BUSINESSCIRCLEINFO, oAuth_Base64);

			editor.commit();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static RspBusinessCirclelist getBusinessCirclelist() {
		RspBusinessCirclelist rspbusinesscirclelist = null;
		SharedPreferences preferences = MoLiApplication.getContext().getSharedPreferences("base64", Context.MODE_PRIVATE);
		String productBase64 = preferences.getString(BUSINESSCIRCLEINFO, "");

		// 读取字节
		byte[] base64 = Base64.decodeBase64(productBase64.getBytes());

		// 封装到字节流
		ByteArrayInputStream bais = new ByteArrayInputStream(base64);
		try {
			// 再次封装
			ObjectInputStream bis = new ObjectInputStream(bais);
			try {
				// 读取对象
				rspbusinesscirclelist = (RspBusinessCirclelist) bis.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rspbusinesscirclelist;
	}
	
	public static void setStoreRspAdvertiseIndexads(RspAdvertiseIndexads rspadvertiseindexads) {
		SharedPreferences preferences = MoLiApplication.getContext().getSharedPreferences("base64", Context.MODE_PRIVATE);
		// 创建字节输出流
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			// 创建对象输出流，并封装字节流
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			// 将对象写入字节流
			oos.writeObject(rspadvertiseindexads);
			// 将字节流编码成base64的字符窜
			String oAuth_Base64 = new String(Base64.encodeBase64(baos.toByteArray()));
			Editor editor = preferences.edit();
			editor.putString(STORE_INFO, oAuth_Base64);

			editor.commit();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static RspAdvertiseIndexads getStoreRspAdvertiseIndexads() {
		RspAdvertiseIndexads rspadvertiseindexads = null;
		SharedPreferences preferences = MoLiApplication.getContext().getSharedPreferences("base64", Context.MODE_PRIVATE);
		String productBase64 = preferences.getString(STORE_INFO, "");

		// 读取字节
		byte[] base64 = Base64.decodeBase64(productBase64.getBytes());

		// 封装到字节流
		ByteArrayInputStream bais = new ByteArrayInputStream(base64);
		try {
			// 再次封装
			ObjectInputStream bis = new ObjectInputStream(bais);
			try {
				// 读取对象
				rspadvertiseindexads = (RspAdvertiseIndexads) bis.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rspadvertiseindexads;
	}
	
	public static void setHomeRspAdvertiseIndexads(RspAdvertiseIndexads rspadvertiseindexads) {
		SharedPreferences preferences = MoLiApplication.getContext().getSharedPreferences("base64", Context.MODE_PRIVATE);
		// 创建字节输出流
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			// 创建对象输出流，并封装字节流
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			// 将对象写入字节流
			oos.writeObject(rspadvertiseindexads);
			// 将字节流编码成base64的字符窜
			String oAuth_Base64 = new String(Base64.encodeBase64(baos.toByteArray()));
			Editor editor = preferences.edit();
			editor.putString(HOME_INFO, oAuth_Base64);

			editor.commit();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static RspAdvertiseIndexads getHomeRspAdvertiseIndexads() {
		RspAdvertiseIndexads rspadvertiseindexads = null;
		SharedPreferences preferences = MoLiApplication.getContext().getSharedPreferences("base64", Context.MODE_PRIVATE);
		String productBase64 = preferences.getString(HOME_INFO, "");

		// 读取字节
		byte[] base64 = Base64.decodeBase64(productBase64.getBytes());

		// 封装到字节流
		ByteArrayInputStream bais = new ByteArrayInputStream(base64);
		try {
			// 再次封装
			ObjectInputStream bis = new ObjectInputStream(bais);
			try {
				// 读取对象
				rspadvertiseindexads = (RspAdvertiseIndexads) bis.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rspadvertiseindexads;
	}
	
	public static void setCategoryInfo(RspGoodsClassList rspgoodsclasslist) {
		SharedPreferences preferences = MoLiApplication.getContext().getSharedPreferences("base64", Context.MODE_PRIVATE);
		// 创建字节输出流
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			// 创建对象输出流，并封装字节流
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			// 将对象写入字节流
			oos.writeObject(rspgoodsclasslist);
			// 将字节流编码成base64的字符窜
			String oAuth_Base64 = new String(Base64.encodeBase64(baos.toByteArray()));
			Editor editor = preferences.edit();
			editor.putString(CATEGORY_INFO, oAuth_Base64);

			editor.commit();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static RspGoodsClassList getCategoryInfo() {
		RspGoodsClassList rspgoodsclasslist = null;
		SharedPreferences preferences = MoLiApplication.getContext().getSharedPreferences("base64", Context.MODE_PRIVATE);
		String productBase64 = preferences.getString(CATEGORY_INFO, "");

		// 读取字节
		byte[] base64 = Base64.decodeBase64(productBase64.getBytes());

		// 封装到字节流
		ByteArrayInputStream bais = new ByteArrayInputStream(base64);
		try {
			// 再次封装
			ObjectInputStream bis = new ObjectInputStream(bais);
			try {
				// 读取对象
				rspgoodsclasslist = (RspGoodsClassList) bis.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rspgoodsclasslist;
	}

	public static void setBusinessClassList(List<ClassifyEntity> list) {
		SharedPreferences preferences = MoLiApplication.getContext().getSharedPreferences("base64", Context.MODE_PRIVATE);
		// 创建字节输出流
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			// 创建对象输出流，并封装字节流
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			// 将对象写入字节流
			oos.writeObject(list);
			// 将字节流编码成base64的字符窜
			String oAuth_Base64 = new String(Base64.encodeBase64(baos.toByteArray()));
			Editor editor = preferences.edit();
			editor.putString(BUSINESSCLASS_LIST, oAuth_Base64);

			editor.commit();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static List<ClassifyEntity> getBusinessClassList() {
		List<ClassifyEntity> classifylist = null;
		SharedPreferences preferences = MoLiApplication.getContext().getSharedPreferences("base64", Context.MODE_PRIVATE);
		String productBase64 = preferences.getString(BUSINESSCLASS_LIST, "");

		// 读取字节
		byte[] base64 = Base64.decodeBase64(productBase64.getBytes());

		// 封装到字节流
		ByteArrayInputStream bais = new ByteArrayInputStream(base64);
		try {
			// 再次封装
			ObjectInputStream bis = new ObjectInputStream(bais);
			try {
				// 读取对象
				classifylist = (List<ClassifyEntity>) bis.readObject();
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
		} catch (StreamCorruptedException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return classifylist;
	}

	/**
	 * 清空数据
	 */
	public static void cleanData() {
		UserInfoPreferences.edit().clear();
	}
	
	public static int width = 0;
	public static int height = 0;
	public static float density;
	/**
	 * 得到屏幕长宽
	 * @param context
	 */
	public static void setScreenSize(Activity activity) {
		DisplayMetrics displayMetrics = new DisplayMetrics();
		activity.getWindowManager().getDefaultDisplay()
				.getMetrics(displayMetrics);
		width = displayMetrics.widthPixels;
		height = displayMetrics.heightPixels;
		density = displayMetrics.density;
	}
}
