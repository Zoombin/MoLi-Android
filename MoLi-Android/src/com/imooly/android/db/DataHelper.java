package com.imooly.android.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.imooly.android.MoLiApplication;
import com.imooly.android.entity.FootStepEntity;
import com.imooly.android.entity.RspAddressList;

/**
 * 数据库相关操作类
 * 
 * @author daiye
 * 
 */
public class DataHelper {
	// 数据库名称
	private static String DB_NAME = "imooly.db";
	// 数据库版本号
	private static int DB_VERSION = 1;

	public SQLiteDatabase db;

	private SqliteHelper dbHelper;

	private static DataHelper dataHelper = null;

	/**
	 * 第一次实例化时调用
	 * 
	 * @return
	 */
	public synchronized static DataHelper getInstance() {
		if (dataHelper == null) {
			dataHelper = new DataHelper(MoLiApplication.getContext(), DB_NAME);
		}
		return dataHelper;
	}

	/**
	 * @brief 构造函数
	 * @param context
	 *            context
	 */
	public DataHelper(Context context, String db_name) {
		dbHelper = new SqliteHelper(context, db_name, null, DB_VERSION);
		db = context.openOrCreateDatabase(db_name, Context.MODE_PRIVATE, null);
		dbHelper.onCreate(db);
	}

	/**
	 * 关闭数据库
	 * 
	 * @return 无
	 */
	public void Close() {
		db.close();
		dbHelper.close();
	}

	/**
	 * 保存搜索记录
	 * 
	 * @param keyword
	 * @param updattime
	 */
	public void SaveOrUpdateOrder(String keyword, int updattime) {
		ContentValues values = new ContentValues();
		values.put(SqliteHelper.HISTORY_KEYWORD, keyword);
		values.put(SqliteHelper.HISTORY_UPDATETIME, Integer.toString(updattime));
		try {
			db.replaceOrThrow(SqliteHelper.TB_SEARCH_HISTORY, null, values);
		} catch (Exception e) {
			
		}
	}

	/**
	 * 搜索10条搜索记录
	 * 
	 * @return
	 */
	public List<String> QueryKeyWords() {
		List<String> keywords = new ArrayList<String>();
		String sql = String.format("SELECT * FROM %s ORDER BY " + SqliteHelper.HISTORY_UPDATETIME + " DESC limit 10",
				SqliteHelper.TB_SEARCH_HISTORY);
		Cursor cursor = db.rawQuery(sql, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String keyword = cursor.getString(cursor.getColumnIndex(SqliteHelper.HISTORY_KEYWORD));
			keywords.add(keyword);
			cursor.moveToNext();
		}
		return keywords;
	}

	/**
	 * 删除搜索记录
	 */
	public void deleteHistory() {
		db.execSQL(String.format("DELETE FROM %s", SqliteHelper.TB_SEARCH_HISTORY));
	}
	
	
	//----------------- 足迹 ------------
	
	/**
	 * 保存记录
	 * 
	 * @param keyword
	 * @param updattime
	 */
	public void SaveOrUpdateFootstep(String goodsid,String goodsname,String goodsimg,String goodsprice) {
		long line =0;
		int time = Integer.valueOf((int) System.currentTimeMillis());
		ContentValues values = new ContentValues();
		values.put(SqliteHelper.GOODS_ID, goodsid);
		values.put(SqliteHelper.GOODS_NAME, goodsname);
		values.put(SqliteHelper.GOODS_IMG, goodsimg);
		values.put(SqliteHelper.GOODS_PRICE, goodsprice);
		values.put(SqliteHelper.UPDATETIME, time);
		try {
			line = db.replaceOrThrow(SqliteHelper.FOOTSTEP_TABLE, null, values);
		} catch (Exception e) {
		}
		if(line >30){
			
		}
	}

	/**
	 * 搜索记录
	 * 
	 * @return
	 */
	public List<FootStepEntity> QueryFootstep() {
		List<FootStepEntity> etys = new ArrayList<FootStepEntity>();
		String sql = String.format("SELECT * FROM %s ORDER BY " + SqliteHelper.UPDATETIME + " DESC limit 30",
				SqliteHelper.FOOTSTEP_TABLE);
		Cursor cursor = db.rawQuery(sql, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			FootStepEntity fEntity = new FootStepEntity();
			String goods_id = cursor.getString(cursor.getColumnIndex(SqliteHelper.GOODS_ID));
			String goods_name = cursor.getString(cursor.getColumnIndex(SqliteHelper.GOODS_NAME));
			String goods_img = cursor.getString(cursor.getColumnIndex(SqliteHelper.GOODS_IMG));
			String goods_price = cursor.getString(cursor.getColumnIndex(SqliteHelper.GOODS_PRICE));
			
			fEntity.setGoods_id(goods_id);
			fEntity.setGoods_name(goods_name);
			fEntity.setGoods_img(goods_img);
			fEntity.setGoods_price(goods_price);
			etys.add(fEntity);
			cursor.moveToNext();
		}
		return etys;
	}
	
	
	/**
	 * 删除搜索记录
	 */
	public void deleteFootstep() {
		db.execSQL(String.format("DELETE FROM %s", SqliteHelper.FOOTSTEP_TABLE));
	}
	
	
	

	// ----------------实体店------
	
	/** 保存实体店搜索历史 */
	public void SaveStoreSearch(String keyword, int updattime) {
		ContentValues values = new ContentValues();
		values.put(SqliteHelper.HISTORY_KEYWORD, keyword);
		values.put(SqliteHelper.HISTORY_UPDATETIME, Integer.toString(updattime));
		try {
			db.replaceOrThrow(SqliteHelper.TB_STORE_SEARCH, null, values);
		} catch (Exception e) {

		}
	}

	/***
	 * 查询实体店搜索历史
	 * 
	 * @return
	 */
	public List<String> QueryStoreSearch() {
		List<String> keywords = new ArrayList<String>();
		String sql = String.format("SELECT * FROM %s ORDER BY " + SqliteHelper.HISTORY_UPDATETIME + " DESC limit 10",
				SqliteHelper.TB_STORE_SEARCH);
		Cursor cursor = db.rawQuery(sql, null);
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			String keyword = cursor.getString(cursor.getColumnIndex(SqliteHelper.HISTORY_KEYWORD));
			keywords.add(keyword);
			cursor.moveToNext();
		}
		return keywords;
	}

	/**
	 * 删除实体店搜索记录
	 */
	public void deleteStoreSearch() {
		db.execSQL(String.format("DELETE FROM %s", SqliteHelper.TB_STORE_SEARCH));
	}

	// ---- 收货地址操作
	public void addaddress(String addressid, int provinceid, int cityid, int areaid, String province,
			String city, String area, String street, String postcode, String name, String tel, String mobile,
			int isdefault) {
		try {
			db.execSQL(
					"INSERT INTO "
							+ SqliteHelper.ADDRESS_TABLE
							+ "(addressid,provinceid,cityid,areaid,province,city,area,street,postcode,name,tel,mobile,isdefault) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)",
					new Object[] { addressid, provinceid, cityid, areaid, province, city, area, street, postcode, name,
							tel, mobile, isdefault });
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void updateaddress(String addressid, int provinceid, int cityid, int areaid, String province,
			String city, String area, String street, String postcode, String name, String tel, String mobile,
			int isdefault) {
		try {
			db.execSQL(
					"UPDATE "
							+ SqliteHelper.ADDRESS_TABLE
							+ " SET provinceid=?,cityid=?,areaid=?,province=?,city=?,area=?,street=?,postcode=?,name=?,tel=?,mobile=?,isdefault=? WHERE addressid=?",
					new Object[] { provinceid, cityid, areaid, province, city, area, street, postcode, name, tel,
							mobile, isdefault, addressid });
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void deleteaddress(String addressid) {
		try {
			db.execSQL("DELETE FROM " + SqliteHelper.ADDRESS_TABLE + " WHERE addressid = ?", new Object[] { addressid });
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public List<RspAddressList.Address> getaddresslist() {
		String sql = "SELECT * FROM " + SqliteHelper.ADDRESS_TABLE;
		try {
			Cursor cursor = db.rawQuery(sql, null);
			List<RspAddressList.Address> models = new ArrayList<RspAddressList.Address>();
			while (cursor.moveToNext()) {
				RspAddressList.Address model = matchModel(cursor);
				if (model != null) {
					models.add(model);
				}
			}
			return models;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	public RspAddressList.Address getaddressinfo(String addressid) {
		String sql = "SELECT * FROM " + SqliteHelper.ADDRESS_TABLE + " WHERE addressid = " + addressid;
		try {
			Cursor cursor = db.rawQuery(sql, null);
			cursor.moveToFirst();
			return matchModel(cursor);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	public RspAddressList.Address getdefultaddress() {
		String sql = "SELECT * FROM " + SqliteHelper.ADDRESS_TABLE + " WHERE isdefault = " + 1;
		try {
			Cursor cursor = db.rawQuery(sql, null);
			cursor.moveToFirst();
			return matchModel(cursor);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}

	public void setdefultaddress(Context context, String addressid) {
		RspAddressList.Address address = getdefultaddress();
		if (address != null) {
			try {
				db.execSQL("UPDATE " + SqliteHelper.ADDRESS_TABLE + " SET isdefault  = ? WHERE addressid = ?",
						new Object[] { 0, address.getAddressid() });
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}
		}
		try {
			db.execSQL("UPDATE " + SqliteHelper.ADDRESS_TABLE + " SET isdefault  = ? WHERE addressid = ?",
					new Object[] { 1, addressid });
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	public void deleteAddressList() {
		db.execSQL(String.format("DELETE FROM %s", SqliteHelper.ADDRESS_TABLE));
	}

	public RspAddressList.Address matchModel(Cursor cursor) {
		RspAddressList.Address model = new RspAddressList.Address();
		model.setAddressid(cursor.getString(cursor.getColumnIndex("addressid")));
		model.setProvinceid(cursor.getInt(cursor.getColumnIndex("provinceid")));
		model.setCityid(cursor.getInt(cursor.getColumnIndex("cityid")));
		model.setAreaid(cursor.getInt(cursor.getColumnIndex("areaid")));
		model.setProvince(cursor.getString(cursor.getColumnIndex("province")));
		model.setCity(cursor.getString(cursor.getColumnIndex("city")));
		model.setArea(cursor.getString(cursor.getColumnIndex("area")));
		model.setStreet(cursor.getString(cursor.getColumnIndex("street")));
		model.setPostcode(cursor.getString(cursor.getColumnIndex("postcode")));
		model.setName(cursor.getString(cursor.getColumnIndex("name")));
		model.setTel(cursor.getString(cursor.getColumnIndex("tel")));
		model.setMobile(cursor.getString(cursor.getColumnIndex("mobile")));
		model.setIsdefault(cursor.getInt(cursor.getColumnIndex("isdefault")));
		return model;
	}
}
