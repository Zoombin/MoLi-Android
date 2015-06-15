package com.imooly.android.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/***
 * 获取订单评价
 * 
 * @author LSD
 * 
 */
public class RspOrderComment extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {
		String orderno;
		List<CommentGoods> goodslist;

		public String getOrderno() {
			return orderno;
		}

		public void setOrderno(String orderno) {
			this.orderno = orderno;
		}

		public List<CommentGoods> getGoodslist() {
			return goodslist;
		}

		public void setGoodslist(List<CommentGoods> goodslist) {
			this.goodslist = goodslist;
		}

	}

	public class CommentGoods {
		String goodsid;
		String unique;
		String image;
		String name;
		float price;
		int num;
		String spec;

		public String getGoodsid() {
			return goodsid;
		}

		public void setGoodsid(String goodsid) {
			this.goodsid = goodsid;
		}

		public String getUnique() {
			return unique;
		}

		public void setUnique(String unique) {
			this.unique = unique;
		}

		public String getImage() {
			return image;
		}

		public void setImage(String image) {
			this.image = image;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public float getPrice() {
			return price;
		}

		public void setPrice(float price) {
			this.price = price;
		}

		public int getNum() {
			return num;
		}

		public void setNum(int num) {
			this.num = num;
		}

		public String getSpec() {
			return spec;
		}

		public void setSpec(String spec) {
			this.spec = spec;
		}

	}

}
