package com.imooly.android.entity;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;

/***
 * 购物车同步
 * 
 * @author daiye
 *
 */
public class RspCartSync extends ServiceResult implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@SerializedName("data")
	public Data data;

	public static class Data implements Serializable {

		private static final long serialVersionUID = 1L;
		
		public int flag;
		public int totalnum;
		public List<Goods> goodslist;
		
		public int getFlag() {
			return flag;
		}
		public void setFlag(int flag) {
			this.flag = flag;
		}
		public int getTotalnum() {
			return totalnum;
		}
		public void setTotalnum(int totalnum) {
			this.totalnum = totalnum;
		}
		public List<Goods> getGoodslist() {
			return goodslist;
		}
		public void setGoodslist(List<Goods> goodslist) {
			this.goodslist = goodslist;
		}
	}

	public static class Goods implements Serializable {
		
		private static final long serialVersionUID = 1L;
		
		public String storeid;
		public String storename;
		public List<Good> goods;
		public boolean check;
		
		public String getStoreid() {
			return storeid;
		}
		public void setStoreid(String storeid) {
			this.storeid = storeid;
		}
		public String getStorename() {
			return storename;
		}
		public void setStorename(String storename) {
			this.storename = storename;
		}
		public List<Good> getGoods() {
			return goods;
		}
		public void setGoods(List<Good> goods) {
			this.goods = goods;
		}
		public boolean isCheck() {
			return check;
		}
		public void setCheck(boolean check) {
			this.check = check;
		}
	}
	
	public static class Good implements Serializable {
		
		private static final long serialVersionUID = 1L;
		
		public String goodsid;
		public String goodsname;
		public String goodsimage;
		public String spec;
		public String specshow;
		public float price;
		public int salesvolume;
		public float highopinion;
		public int num;
		public int onsale;
		public boolean check;
		public int isstock;
		public int stock;
		public int totalnum;
		
		
		public String getGoodsid() {
			return goodsid;
		}
		public void setGoodsid(String goodsid) {
			this.goodsid = goodsid;
		}
		public String getGoodsname() {
			return goodsname;
		}
		public void setGoodsname(String goodsname) {
			this.goodsname = goodsname;
		}
		public String getGoodsimage() {
			return goodsimage;
		}
		public void setGoodsimage(String goodsimage) {
			this.goodsimage = goodsimage;
		}
		public String getSpec() {
			return spec;
		}
		public void setSpec(String spec) {
			this.spec = spec;
		}
		public String getSpecshow() {
			return specshow;
		}
		public void setSpecshow(String specshow) {
			this.specshow = specshow;
		}
		public float getPrice() {
			return price;
		}
		public void setPrice(float price) {
			this.price = price;
		}
		public int getSalesvolume() {
			return salesvolume;
		}
		public void setSalesvolume(int salesvolume) {
			this.salesvolume = salesvolume;
		}
		public float getHighopinion() {
			return highopinion;
		}
		public void setHighopinion(float highopinion) {
			this.highopinion = highopinion;
		}
		public int getNum() {
			return num;
		}
		public void setNum(int num) {
			this.num = num;
		}
		public int getOnsale() {
			return onsale;
		}
		public void setOnsale(int onsale) {
			this.onsale = onsale;
		}
		public boolean isCheck() {
			return check;
		}
		public void setCheck(boolean check) {
			this.check = check;
		}
		public int getIsstock() {
			return isstock;
		}
		public void setIsstock(int isstock) {
			this.isstock = isstock;
		}
		public int getTotalnum() {
			return totalnum;
		}
		public void setTotalnum(int totalnum) {
			this.totalnum = totalnum;
		}
		public int getStock() {
			return stock;
		}
		public void setStock(int stock) {
			this.stock = stock;
		}
		
	}
}
