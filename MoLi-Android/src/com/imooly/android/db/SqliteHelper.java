package com.imooly.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 该类是用来创建数据库的
 * 
 * @author daiye
 * 
 */
public class SqliteHelper extends SQLiteOpenHelper {
	// 搜索历史
	public static final String TB_SEARCH_HISTORY = "search_history";
	// 实体店搜索历史
	public static final String TB_STORE_SEARCH = "store_search";
	
	// 关键词
	public static final String HISTORY_KEYWORD = "keyword";
	// 更新时间
	public static final String HISTORY_UPDATETIME = "updatetime";
	
	
	//收货地址
	public static String ADDRESS_TABLE = "address_table";
	public static String ADDRESS_ID = "addressid";
	public static String PROVICEDE_ID = "provinceid";
	public static String CITY_ID = "cityid";
	public static String AREA_ID = "areaid";
	public static String PROVICE = "province";
	public static String CITY = "city";
	public static String AREA = "area";
	public static String STREET = "street";
	public static String POSTCODE = "postcode";
	public static String NAME = "name";
	public static String TEL = "tel";
	public static String MOBLIE = "mobile";
	public static String IS_DEFAULT = "isdefault";
	public static final String CREATE_ADDRESS_TABLE = "CREATE TABLE IF NOT EXISTS "
			+ ADDRESS_TABLE + "(" 
			+ ADDRESS_ID + " STRING PRIMARY KEY, " 
			+ PROVICEDE_ID + " STRING, " 
			+ CITY_ID + " INTEGER, " 
			+ AREA_ID + " INTEGER, " 
			+ PROVICE + " STRING, "
			+ CITY + " STRING, " 
			+ AREA + " STRING, " 
			+ STREET + " STRING, " 
			+ POSTCODE + " STRING, " 
			+ NAME + " STRING, " 
			+ TEL + " STRING, " 
			+ MOBLIE + " STRING, " 
			+ IS_DEFAULT + " INTEGER " 
			+ ");";
	
	
	
	//足迹
	public static String FOOTSTEP_TABLE = "footstep_table";
	public static String GOODS_ID = "goods_id";
	public static String GOODS_IMG = "goods_img";
	public static String GOODS_NAME = "goods_name";
	public static String GOODS_PRICE = "goods_price";
	public static String UPDATETIME = "update_time";
	public static final String CREATE_FOOTSTEP_TABLE = "CREATE TABLE IF NOT EXISTS "
			+ FOOTSTEP_TABLE + "(" 
			+ GOODS_ID + " STRING PRIMARY KEY, " 
			+ GOODS_IMG + " STRING, " 
			+ GOODS_NAME + " STRING, " 
			+ GOODS_PRICE + " STRING, " 
			+ UPDATETIME + " INTEGER " 
			+ ");";
	
	

	/**
	 * @param context
	 *            上下文context
	 * @param name
	 *            名称
	 * @param factory
	 *            CursorFactory
	 * @param version
	 *            版本号
	 */
	public SqliteHelper(Context context, String name, CursorFactory factory, int version) {
		super(context, name, factory, version);

	}

	/**
	 * @brief 该方法用来创建表
	 * @return 无
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table if not exists " + TB_SEARCH_HISTORY + "(" + HISTORY_KEYWORD + " text primary key, "
				+ HISTORY_UPDATETIME + " integer" + ")");
		db.execSQL("create table if not exists " + TB_STORE_SEARCH + "(" + HISTORY_KEYWORD + " text primary key, "
				+ HISTORY_UPDATETIME + " integer" + ")");
		db.execSQL(CREATE_ADDRESS_TABLE);
		db.execSQL(CREATE_FOOTSTEP_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TB_SEARCH_HISTORY);
		db.execSQL("DROP TABLE IF EXISTS " + TB_STORE_SEARCH);
		db.execSQL("DROP TABLE IF EXISTS " + ADDRESS_TABLE);
		db.execSQL("DROP TABLE IF EXISTS " + FOOTSTEP_TABLE);
		onCreate(db);
		Log.e("Database", "onUpgrade");
	}
}
