package com.imooly.android.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/***
 * 获取我收藏的商品列表
 * 
 * @author
 * 
 */
public class RspCellectionGoodsList extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {
		private List<CollectionGood> goodslist;

		public List<CollectionGood> getGoodslist() {
			return goodslist;
		}

		public void setGoodslist(List<CollectionGood> goodslist) {
			this.goodslist = goodslist;
		}

	}

	public class CollectionGood {
		private String goodsid;
		private String goodsname;
		private String goodsimage;
		private float price;
		private int salesvolume;
		private int highopinion;

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
}
