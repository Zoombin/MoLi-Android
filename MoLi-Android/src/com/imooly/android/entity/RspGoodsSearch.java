package com.imooly.android.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/***
 * 商品搜索
 * 
 * @author LSD
 * 
 */
public class RspGoodsSearch extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {
		public int totalpage;
		public List<StoreEty> storelist;
		public List<GoodsEty> goodslist;
		public List<SpecEty> speclist;

		public int getTotalpage() {
			return totalpage;
		}

		public void setTotalpage(int totalpage) {
			this.totalpage = totalpage;
		}

		public List<StoreEty> getStorelist() {
			return storelist;
		}

		public void setStorelist(List<StoreEty> storelist) {
			this.storelist = storelist;
		}

		public List<GoodsEty> getGoodslist() {
			return goodslist;
		}

		public void setGoodslist(List<GoodsEty> goodslist) {
			this.goodslist = goodslist;
		}

		public List<SpecEty> getSpeclist() {
			return speclist;
		}

		public void setSpeclist(List<SpecEty> speclist) {
			this.speclist = speclist;
		}

	}

	/** 旗舰店 */
	public static class StoreEty {
		public String businessid;
		public String businessname;
		public String businessicon;
		public String businessimage;// 旗舰店广告图

		public String getBusinessid() {
			return businessid;
		}

		public void setBusinessid(String businessid) {
			this.businessid = businessid;
		}

		public String getBusinessname() {
			return businessname;
		}

		public void setBusinessname(String businessname) {
			this.businessname = businessname;
		}

		public String getBusinessicon() {
			return businessicon;
		}

		public void setBusinessicon(String businessicon) {
			this.businessicon = businessicon;
		}

		public String getBusinessimage() {
			return businessimage;
		}

		public void setBusinessimage(String businessimage) {
			this.businessimage = businessimage;
		}

	}

	/** 商品 */
	public static class GoodsEty {
		public String goodsid;
		public String goodsname;
		public String goodsimage;
		public float price;
		public int isvoucher;
		public int salesvolume;
		public int highopinion;

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

		public float getPrice() {
			return price;
		}

		public void setPrice(float price) {
			this.price = price;
		}

		public int getIsvoucher() {
			return isvoucher;
		}

		public void setIsvoucher(int isvoucher) {
			this.isvoucher = isvoucher;
		}

		public int getSalesvolume() {
			return salesvolume;
		}

		public void setSalesvolume(int salesvolume) {
			this.salesvolume = salesvolume;
		}

		public int getHighopinion() {
			return highopinion;
		}

		public void setHighopinion(int highopinion) {
			this.highopinion = highopinion;
		}

	}

	/** 规格 */
	public static class SpecEty {
		public String name;
		public List<String> list;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public List<String> getList() {
			return list;
		}

		public void setList(List<String> list) {
			this.list = list;
		}

	}
}
