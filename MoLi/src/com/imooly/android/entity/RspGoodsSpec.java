package com.imooly.android.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;

/***
 * 获取商品规格
 * 
 * @author LSD
 * 
 */
public class RspGoodsSpec extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {
		public String goodsid;
		public String goodsimage;
		public String goodsname;
		public String goodsprice;
		public List<GoodsSpec> goodsspec;

		public String getGoodsid() {
			return goodsid;
		}

		public void setGoodsid(String goodsid) {
			this.goodsid = goodsid;
		}

		public String getGoodsimage() {
			return goodsimage;
		}

		public void setGoodsimage(String goodsimage) {
			this.goodsimage = goodsimage;
		}

		public String getGoodsname() {
			return goodsname;
		}

		public void setGoodsname(String goodsname) {
			this.goodsname = goodsname;
		}

		public String getGoodsprice() {
			return goodsprice;
		}

		public void setGoodsprice(String goodsprice) {
			this.goodsprice = goodsprice;
		}

		public List<GoodsSpec> getGoodsspec() {
			return goodsspec;
		}

		public void setGoodsspec(List<GoodsSpec> goodsspec) {
			this.goodsspec = goodsspec;
		}

	}

	public static class GoodsSpec {
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
