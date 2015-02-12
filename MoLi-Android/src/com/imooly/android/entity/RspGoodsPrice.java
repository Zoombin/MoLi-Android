package com.imooly.android.entity;

import com.google.gson.annotations.SerializedName;

/***
 * 获取商品某组规格所对应价格
 * 
 * @author LSD
 * 
 */
public class RspGoodsPrice extends ServiceResult {
	@SerializedName("data")
	public Data data;

	public static class Data {
		public float goodsprice;
		public int isvoucher;
		public int voucher;
		public int stock;

		public float getGoodsprice() {
			return goodsprice;
		}

		public void setGoodsprice(float goodsprice) {
			this.goodsprice = goodsprice;
		}

		public int getIsvoucher() {
			return isvoucher;
		}

		public void setIsvoucher(int isvoucher) {
			this.isvoucher = isvoucher;
		}

		public int getVoucher() {
			return voucher;
		}

		public void setVoucher(int voucher) {
			this.voucher = voucher;
		}

		public int getStock() {
			return stock;
		}

		public void setStock(int stock) {
			this.stock = stock;
		}

	}
}
